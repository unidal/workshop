package org.unidal.weather.web;

import static org.unidal.weather.web.WebConstant.DEFAULT;
import static org.unidal.weather.web.WebConstant.MODULE_WEATHER;
import static org.unidal.weather.web.WebConstant.REPORT;

import java.io.IOException;

import javax.servlet.ServletException;

import org.unidal.weather.biz.ReportPage;

import com.site.lookup.annotation.Inject;
import com.site.web.mvc.annotation.ErrorActionMeta;
import com.site.web.mvc.annotation.InboundActionMeta;
import com.site.web.mvc.annotation.ModuleMeta;
import com.site.web.mvc.annotation.OutboundActionMeta;
import com.site.web.mvc.annotation.PayloadMeta;
import com.site.web.mvc.annotation.TransitionMeta;

@ModuleMeta(name = MODULE_WEATHER, defaultInboundAction = REPORT, defaultTransition = DEFAULT, defaultErrorAction = DEFAULT)
public class WebModule {
   @Inject
   private ReportPage m_report;

   @PayloadMeta(WebPayload.class)
   @InboundActionMeta(name = REPORT)
   public void doReport(WebContext ctx) {
      m_report.handleInbound(ctx);
   }

   @TransitionMeta(name = DEFAULT)
   public void handleTransition(WebContext ctx) {
      // simple cases, nothing here
   }

   @ErrorActionMeta(name = DEFAULT)
   public void onError(WebContext ctx) {
      ctx.getException().printStackTrace();
   }

   @OutboundActionMeta(name = REPORT)
   public void showReport(WebContext ctx) throws IOException, ServletException {
      m_report.handleOutbound(ctx);
   }
}
