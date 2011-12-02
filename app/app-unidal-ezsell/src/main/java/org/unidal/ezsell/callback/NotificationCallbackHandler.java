package org.unidal.ezsell.callback;

import java.io.IOException;
import java.io.StringReader;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.codehaus.plexus.logging.LogEnabled;
import org.codehaus.plexus.logging.Logger;
import org.codehaus.plexus.util.IOUtil;
import org.unidal.ezsell.api.ebay.notification.NotificationEvent;
import org.unidal.ezsell.api.ebay.notification.NotificationParser;
import org.unidal.ezsell.api.ebay.notification.NotificationStatus;
import org.unidal.ezsell.dal.Notification;
import org.unidal.ezsell.dal.NotificationDao;
import org.xml.sax.InputSource;

import com.site.dal.jdbc.DalException;
import com.site.dal.xml.XmlException;
import com.site.lookup.annotation.Inject;

public class NotificationCallbackHandler implements LogEnabled {
   @Inject
   private NotificationDao m_notificationDao;

   private Logger m_logger;

   public void handle(HttpServletRequest request, HttpServletResponse response) throws IOException, DalException {
      String payload = IOUtil.toString(request.getInputStream());
      List<NotificationEvent> events = null;

      if (payload.length() > 0) {
         InputSource source = new InputSource(new StringReader(payload));

         try {
            events = new NotificationParser().parse(source);
         } catch (XmlException e) {
            m_logger.error("Error when parsing XML payload: " + payload, e);
         }
      }

      if (events != null) {
         for (NotificationEvent event : events) {
            Notification notification = new Notification();

            notification.setEventName(event.getEventName().name());
            notification.setEventPayload(event.getResponseContent());
            notification.setSellerAccount(event.getUserAccount());
            notification.setStatus(NotificationStatus.RECIEVED.getValue());

            m_notificationDao.insert(notification);
            handleEvent(notification, event);
         }
      } else {
         response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Bad request!");
      }
   }

   private boolean handleEvent(Notification notification, NotificationEvent event) {
      // TODO Auto-generated method stub
      return false;
   }

   public void enableLogging(Logger logger) {
      m_logger = logger;
   }
}
