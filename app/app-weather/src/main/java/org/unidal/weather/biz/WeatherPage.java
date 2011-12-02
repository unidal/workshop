package org.unidal.weather.biz;

import static org.unidal.weather.web.WebConstant.DEFAULT;
import static org.unidal.weather.web.WebConstant.REPORT;

import java.io.IOException;
import java.util.Arrays;
import java.util.Date;

import javax.servlet.ServletException;

import org.unidal.weather.web.WebAction;
import org.unidal.weather.web.WebContext;
import org.unidal.weather.web.WebModel;
import org.unidal.weather.web.WebPayload;
import org.unidal.weather.web.view.JspViewer;

import com.site.app.weather.dal.WeatherDao;
import com.site.lookup.annotation.Inject;
import com.site.web.mvc.annotation.ErrorActionMeta;
import com.site.web.mvc.annotation.InboundActionMeta;
import com.site.web.mvc.annotation.OutboundActionMeta;
import com.site.web.mvc.annotation.PageMeta;
import com.site.web.mvc.annotation.PayloadMeta;
import com.site.web.mvc.annotation.TransitionMeta;

@PageMeta(name = "weather", module = "default", defaultInboundAction = REPORT, defaultTransition = DEFAULT, defaultErrorAction = DEFAULT)
public class WeatherPage {
   @Inject
   private WeatherDao m_weatherDao;

   @Inject
   private JspViewer m_jspViewer;

   @PayloadMeta(WebPayload.class)
   @InboundActionMeta(name = REPORT)
   public void doReport(WebContext ctx) {
      // TODO Auto-generated method stub
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
   public void showReport(WebContext ctx) throws ServletException, IOException {
      WebModel model = new WebModel(ctx);

      model.setAction(WebAction.REPORT);
      model.setCityName("Shanghai");
      model.setLastUpdatedTime(new Date());
      model.setToday(new Weather("Mon", 29, 30, 22).setCurrent(25));
      model.setNextDays(Arrays.asList(new Weather("Mon", 29, 30, 22), new Weather("Mon", 29, 30, 22), new Weather(
            "Mon", 29, 30, 22), new Weather("Mon", 29, 30, 22), new Weather("Mon", 29, 30, 22)));

      m_jspViewer.view(ctx, model);
   }
}
