package org.unidal.weather.web;

import com.site.web.mvc.Payload;
import com.site.web.mvc.payload.annotation.FieldMeta;

public class WebPayload implements Payload<WebAction> {
   private WebAction m_action;

   @FieldMeta("lastUrl")
   private String m_lastUrl;

   public WebAction getAction() {
      return m_action;
   }

   public String getLastUrl() {
      return m_lastUrl;
   }

   public void setNextAction(WebAction action) {
      m_action = action;
   }

   public void setAction(String action) {
      m_action = WebAction.getByName(action, WebAction.REPORT);
   }

   public void setLastUrl(String lastUrl) {
      m_lastUrl = lastUrl;
   }
}
