package org.unidal.ezsell.api.ebay.notification;

import java.io.StringReader;
import java.util.List;

import org.codehaus.plexus.util.IOUtil;
import org.unidal.ezsell.api.ebay.notification.NotificationEvent;
import org.unidal.ezsell.api.ebay.notification.NotificationParser;
import org.xml.sax.InputSource;

import com.site.lookup.ComponentTestCase;

public abstract class AbstractNotificationTest extends ComponentTestCase {
   protected String getEventResponse(String eventName) throws Exception {
      String resource = "/xml/notification/" + eventName + ".xml";
      String content = IOUtil.toString(getClass().getResourceAsStream(resource));

      assertNotNull("No resource found for " + resource, content);

      List<NotificationEvent> events = new NotificationParser().parse(new InputSource(new StringReader(content)));
      String body = null;

      for (NotificationEvent event : events) {
         if (event.getEventName().name().equals(eventName)) {
            body = event.getResponseContent();
         }
      }

      assertNotNull("No event(" + eventName + ") response found at resource(" + resource + ")", body);

      return body;
   }
}
