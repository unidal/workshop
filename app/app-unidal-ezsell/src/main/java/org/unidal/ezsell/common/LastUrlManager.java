package org.unidal.ezsell.common;

import org.unidal.ezsell.EbayContext;

import com.site.lookup.annotation.Inject;
import com.site.web.jsp.function.Encoder;
import com.site.web.lifecycle.ActionResolver;
import com.site.web.lifecycle.DefaultUrlMapping;
import com.site.web.mvc.lifecycle.RequestContext;
import com.site.web.mvc.payload.ParameterProvider;

public class LastUrlManager {
   private static final String LAST_URL = "lu";

   @Inject
   private CookieManager m_cookieManager;

   public String getLastUrl(EbayContext ctx) {
      return m_cookieManager.getCookie(ctx.getHttpServletRequest(), LAST_URL);
   }

   private String getQueryString(EbayContext ctx) {
      ParameterProvider provider = ctx.getRequestContext().getParameterProvider();
      StringBuilder sb = new StringBuilder(1024);

      for (String name : provider.getParameterNames()) {
         String[] values = provider.getParameterValues(name);

         if (values != null) {
            for (String value : values) {
               sb.append('&').append(name).append('=').append(Encoder.urlEncode(value));
            }
         }
      }

      if (sb.length() == 0) {
         return null;
      } else {
         return sb.substring(1);
      }
   }

   public void setLastUrl(EbayContext ctx) {
      RequestContext requestContext = ctx.getRequestContext();
      ParameterProvider provider = requestContext.getParameterProvider();
      ActionResolver resolver = requestContext.getActionResovler();
      DefaultUrlMapping mapping = (DefaultUrlMapping) requestContext.getUrlMapping();

      // enable POST method
      mapping.setQueryString(getQueryString(ctx));

      String url = resolver.buildUrl(provider, mapping);

      m_cookieManager.setCookie(ctx.getHttpServletResponse(), LAST_URL, url, "/");
   }
}
