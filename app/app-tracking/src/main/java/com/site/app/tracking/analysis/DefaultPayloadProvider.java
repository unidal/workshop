package com.site.app.tracking.analysis;

import java.util.Date;
import java.util.List;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.codehaus.plexus.util.StringUtils;

import com.site.app.DataProvider;
import com.site.app.error.ErrorObject;
import com.site.app.rule.DateRule;
import com.site.app.rule.IntRule;
import com.site.app.rule.Rule;
import com.site.app.rule.StringRule;

public class DefaultPayloadProvider implements PayloadProvider {
   private static final long ONE_DAY = 24 * 60 * 60 * 1000L;

   private static final Rule<Field, Date> DATE_FROM = new DateRule<Field>(Field.DATE_FROM).format("yyyy-MM-dd");

   private static final Rule<Field, Date> DATE_TO = new DateRule<Field>(Field.DATE_TO).format("yyyy-MM-dd");

   private static final Rule<Field, Integer> MAX_NUM = new IntRule<Field>(Field.MAX_NUM, 50);

   private static final Rule<Field, String> TOPN_GROUP_BY = new StringRule<Field>(Field.TOPN_GROUP_BY);

   private static final Rule<Field, String> PAGE_URL = new StringRule<Field>(Field.PAGE_URL);

   private static final Rule<Field, String> USERNAME = new StringRule<Field>(Field.USERNAME);

   private static final Rule<Field, String> PASSWORD = new StringRule<Field>(Field.PASSWORD);

   private static final Rule<Field, String> LAST_URL = new StringRule<Field>(Field.LAST_URL);

   private static final Rule<Field, String> STATS_TABLE = new StringRule<Field>(Field.STATS_TABLE);

   private static final Rule<Field, String> STATS_GROUP_BY = new StringRule<Field>(Field.STATS_GROUP_BY);

   private static final Rule<Field, String> PURGE_TABLE = new StringRule<Field>(Field.PURGE_TABLE);

   private static final Rule<Field, String> PURGE_TABLE_ACTION = new StringRule<Field>(Field.PURGE_TABLE_ACTION);

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
      checkSignin(req, payload);

      switch (payload.getAction()) {
      case SIGN_IN:
         normalizeSignin(req, payload);
         break;
      case MAIN_PAGE:
         normalizeMainPage(req, payload);
         break;
      case SEARCH_TOP_N:
         normalizeSearchTopN(req, payload);
         break;
      case PAGE_DETAIL:
         normalizePageDetail(req, payload);
         break;
      case VISIT_STATS:
         normalizeVisitStats(req, payload);
         break;
      case IP_STATS:
         normalizeIpStats(req, payload);
         break;
      case PURGE_TABLE:
         normalizePurgeTable(req, payload);
         break;
      }

      return payload;
   }

   private void normalizeIpStats(HttpServletRequest req, Payload payload) {
      DataProvider<Field> dataProvider = new DefaultDataProvider(req);
      List<ErrorObject> errors = payload.getErrors();
      Date dateFrom = DATE_FROM.evaluate(dataProvider, errors);
      Date dateTo = DATE_TO.evaluate(dataProvider, errors);
      String table = STATS_TABLE.evaluate(dataProvider, errors);
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
      payload.setStatsTable(StatsTable.get(table, StatsTable.PAGE_VISIT_TRACK));
      payload.setStatsGroupBy(StatsGroupBy.get(groupBy, StatsGroupBy.DAY));
   }

   private void normalizeMainPage(HttpServletRequest req, Payload payload) {
      // Do nothing here
   }

   private void normalizePageDetail(HttpServletRequest req, Payload payload) {
      DataProvider<Field> dataProvider = new DefaultDataProvider(req);
      List<ErrorObject> errors = payload.getErrors();
      String pageUrl = PAGE_URL.evaluate(dataProvider, errors);

      payload.setPageUrl(pageUrl);
   }

   private void normalizePurgeTable(HttpServletRequest req, Payload payload) {
      DataProvider<Field> dataProvider = new DefaultDataProvider(req);
      List<ErrorObject> errors = payload.getErrors();
      Date dateTo = DATE_TO.evaluate(dataProvider, errors);
      String purgeTable = PURGE_TABLE.evaluate(dataProvider, errors);
      String purgeTableAction = PURGE_TABLE_ACTION.evaluate(dataProvider, errors);

      if (errors.isEmpty()) {
         dateTo = new Date(dateTo.getTime() + ONE_DAY - 1);
      }

      payload.setDateTo(dateTo);
      payload.setPurgeTable(PurgeTable.get(purgeTable, PurgeTable.PAGE_VISIT_TRACK));
      payload.setPurgeTableAction(PurgeTableAction.get(purgeTableAction, PurgeTableAction.CHECK));
   }

   private void normalizeSearchTopN(HttpServletRequest req, Payload payload) {
      DataProvider<Field> dataProvider = new DefaultDataProvider(req);
      List<ErrorObject> errors = payload.getErrors();
      Date dateFrom = DATE_FROM.evaluate(dataProvider, errors);
      Date dateTo = DATE_TO.evaluate(dataProvider, errors);
      String groupBy = TOPN_GROUP_BY.evaluate(dataProvider, errors);
      Integer maxNum = MAX_NUM.evaluate(dataProvider, errors);

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
      payload.setTopnGroupBy(TopNGroupBy.get(groupBy, TopNGroupBy.PAGE));
      payload.setMaxNum(maxNum);
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

   private void normalizeVisitStats(HttpServletRequest req, Payload payload) {
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
}
