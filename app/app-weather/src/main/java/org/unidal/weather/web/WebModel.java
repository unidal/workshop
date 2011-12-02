package org.unidal.weather.web;

import java.util.Date;
import java.util.List;

import org.unidal.weather.biz.Weather;

import com.site.web.mvc.Model;

public class WebModel extends Model<WebAction, WebContext> {
   private String m_cityName;

   private Date m_lastUpdatedTime;

   private Weather m_today;

   private List<Weather> m_nextDays;

   public WebModel(WebContext actionContext) {
      super(actionContext);
   }

   public String getCityName() {
      return m_cityName;
   }

   public Date getLastUpdatedTime() {
      return m_lastUpdatedTime;
   }

   public Weather getToday() {
      return m_today;
   }

   public List<Weather> getNextDays() {
      return m_nextDays;
   }

   public void setCityName(String cityName) {
      m_cityName = cityName;
   }

   public void setLastUpdatedTime(Date lastUpdatedTime) {
      m_lastUpdatedTime = lastUpdatedTime;
   }

   public void setToday(Weather today) {
      m_today = today;
   }

   public void setNextDays(List<Weather> nextDays) {
      m_nextDays = nextDays;
   }
}
