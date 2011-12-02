package com.site.app.bes.console;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.codehaus.plexus.util.StringUtils;

import com.site.app.DataProvider;
import com.site.app.error.ErrorObject;
import com.site.app.rule.DateRule;
import com.site.app.rule.Rule;
import com.site.app.rule.StringRule;

public class DefaultPayloadProvider implements PayloadProvider {
   private static final long ONE_DAY = 24 * 60 * 60 * 1000L;

   private static final Rule<Field, Date> DATE_FROM = new DateRule<Field>(Field.DATE_FROM).format("yyyy-MM-dd");

   private static final Rule<Field, Date> DATE_TO = new DateRule<Field>(Field.DATE_TO).format("yyyy-MM-dd");

   private static final Rule<Field, String> USERNAME = new StringRule<Field>(Field.USERNAME);

   private static final Rule<Field, String> PASSWORD = new StringRule<Field>(Field.PASSWORD);

   private static final Rule<Field, String> LAST_URL = new StringRule<Field>(Field.LAST_URL);

   private static final Rule<Field, String> STATS_GROUP_BY = new StringRule<Field>(Field.STATS_GROUP_BY, "1d");

   private static final Rule<Field, Date> DATE = new DateRule<Field>(Field.DATE).format("yyyy-MM-dd");

   private void checkSignin(HttpServletRequest req, Payload payload) {
      // Check Signin token
      Cookie[] cookies = req.getCookies();

      if (cookies != null) {
         for (Cookie cookie : cookies) {
            if (cookie.getName().equals("user")) {
               if (cookie.getValue() != null) {
                  payload.setUser(cookie.getValue());
               }

               break;
            }
         }
      }

      if (payload.getUser() == null) {
         payload.setAction(Action.SIGN_IN);
      }
   }

   public Payload getPayload(HttpServletRequest req) {
      Payload payload = new Payload();
      String pathInfo = req.getPathInfo();
      String[] parts = (pathInfo == null ? new String[0] : StringUtils.split(pathInfo, "/"));
      String name = (parts.length > 0 ? parts[0] : null);

      payload.setAction(Action.get(name, Action.MAIN_PAGE));

      if (payload.getAction() != Action.DAILY_REPORT) {
         checkSignin(req, payload);
      }

      switch (payload.getAction()) {
      case SIGN_IN:
         normalizeSignin(req, payload);
         break;
      case MAIN_PAGE:
         normalizeMainPage(req, payload);
         break;
      case EVENT_STATS:
         normalizeEventStats(req, payload);
         break;
      case DAILY_REPORT:
         normalizeDailyReport(req, payload);
         break;
      }

      return payload;
   }

   private void normalizeDailyReport(HttpServletRequest req, Payload payload) {
      DataProvider<Field> dataProvider = new DefaultDataProvider(req);
      List<ErrorObject> errors = payload.getErrors();
      Date dateFrom;

      if (dataProvider.getValue(Field.DATE) == null) {
         Calendar cal = Calendar.getInstance();

         cal.add(Calendar.DATE, -1);
         cal.set(Calendar.HOUR_OF_DAY, 0);
         cal.set(Calendar.MINUTE, 0);
         cal.set(Calendar.SECOND, 0);
         dateFrom = cal.getTime();
      } else {
         dateFrom = DATE.evaluate(dataProvider, errors);
      }

      if (errors.isEmpty()) {
         Date dateTo = new Date(dateFrom.getTime() + ONE_DAY - 1);

         payload.setDateFrom(dateFrom);
         payload.setDateTo(dateTo);
      }
   }

   private void normalizeEventStats(HttpServletRequest req, Payload payload) {
      DataProvider<Field> dataProvider = new DefaultDataProvider(req);
      List<ErrorObject> errors = payload.getErrors();
      Date dateFrom = DATE_FROM.evaluate(dataProvider, errors);
      Date dateTo = DATE_TO.evaluate(dataProvider, errors);
      String groupBy = STATS_GROUP_BY.evaluate(dataProvider, errors);

      if (errors.isEmpty()) {
         // swap them if dateFrom is after dateTo
         if (dateFrom.after(dateTo)) {
            Date tmp = dateFrom;

            dateFrom = dateTo;
            dateTo = tmp;
         }

         dateTo = new Date(dateTo.getTime() + ONE_DAY - 1);
      }

      payload.setDateFrom(dateFrom);
      payload.setDateTo(dateTo);
      payload.setStatsGroupBy(StatsGroupBy.get(groupBy, StatsGroupBy.DAY));
   }

   private void normalizeMainPage(HttpServletRequest req, Payload payload) {
      // Do nothing here
   }

   private void normalizeSignin(HttpServletRequest req, Payload payload) {
      DataProvider<Field> dataProvider = new DefaultDataProvider(req);

      if (dataProvider.getValue(Field.USERNAME) != null) {
         List<ErrorObject> errors = payload.getErrors();
         String username = USERNAME.evaluate(dataProvider, errors);
         String password = PASSWORD.evaluate(dataProvider, errors);
         String lastUrl = LAST_URL.evaluate(dataProvider, errors);

         payload.setUsername(username);
         payload.setPassword(password);
         payload.setLastUrl(lastUrl);
      } else {
         String referer = req.getHeader("Referer");

         if (referer != null) {
            payload.setLastUrl(referer);
         }
      }
   }
}
