package org.unidal.weather.biz;

import java.io.IOException;
import java.util.Arrays;
import java.util.Date;

import javax.servlet.ServletException;

import org.unidal.weather.web.WebAction;
import org.unidal.weather.web.WebContext;
import org.unidal.weather.web.WebModel;
import org.unidal.weather.web.view.JspViewer;

import com.site.app.weather.dal.WeatherDao;

public class ReportPage {
   private WeatherDao m_weatherDao;

   private JspViewer m_jspViewer;

   public void handleInbound(WebContext ctx) {
      // TODO Auto-generated method stub

   }

   public void handleOutbound(WebContext ctx) throws ServletException, IOException {
      WebModel model = new WebModel(ctx);

      model.setAction(WebAction.REPORT);
      model.setCityName("Shanghai");
      model.setLastUpdatedTime(new Date());
      model.setToday(new Weather("Mon", 29, 30, 22).setCurrent(25));
      model.setNextDays(Arrays.asList(
            new Weather("Mon", 29, 30, 22), 
            new Weather("Mon", 29, 30, 22), 
            new Weather("Mon", 29, 30, 22), 
            new Weather("Mon", 29, 30, 22), 
            new Weather("Mon", 29, 30, 22)
            ));

      m_jspViewer.view(ctx, model);
   }
}
