package com.site.bes.server.admin;

import com.site.web.page.PageContext;

public class AdminNormalizer {
   private static final AdminNormalizer s_instance = new AdminNormalizer();

   public static final AdminNormalizer getInstance() {
      return s_instance;
   }

   private AdminNormalizer() {
   }

   public AdminRequest normalize(PageContext ctx) {
      AdminRequest req = new AdminRequest();
      String strAction = ctx.getParameter(AdminFormField.FORM_ACTION);
      AdminFormAction action = AdminFormAction.getByName(strAction, AdminFormAction.LIST);

      req.setFormAction(action);

      if (action == AdminFormAction.LIST) {
         normalizeList(ctx, req);
      } else if (action == AdminFormAction.EVENT) {
         normalizeEvent(ctx, req);
      } else if (action == AdminFormAction.DASHBOARD) {
         normalizeDashboard(ctx, req);
      } else {
         throw new RuntimeException("Undefined action(" + strAction + ") found!");
      }

      return req;
   }

   private void normalizeDashboard(PageContext ctx, AdminRequest req) {
      String strPageMode = ctx.getParameter(AdminFormField.PAGE_MODE);
      AdminPageMode pageMode = AdminPageMode.getByName(strPageMode, AdminPageMode.DISPLAY);

      req.setPageMode(pageMode);

      if (pageMode == AdminPageMode.DISPLAY) {
         setEventType(ctx, req, AdminFormField.EVENT_TYPE, true);
         setConsumerType(ctx, req, AdminFormField.CONSUMER_TYPE, true);
      } else if (pageMode == AdminPageMode.SUBMIT) {
         setEventType(ctx, req, AdminFormField.EVENT_TYPE, true);
         setConsumerType(ctx, req, AdminFormField.CONSUMER_TYPE, true);
         setLastFetchedId(ctx, req, AdminFormField.LAST_FETCHED_ID, false);
         setBatchTimeout(ctx, req, AdminFormField.BATCH_TIMEOUT, false);
      } else if (pageMode == AdminPageMode.START || pageMode == AdminPageMode.STOP) {
         setEventType(ctx, req, AdminFormField.EVENT_TYPE, true);
         setConsumerType(ctx, req, AdminFormField.CONSUMER_TYPE, true);
      }
   }

   private void normalizeEvent(PageContext ctx, AdminRequest req) {
      setEventId(ctx, req, AdminFormField.EVENT_ID, true);
   }

   private void normalizeList(PageContext ctx, AdminRequest req) {
      req.setSortedBy(AdminSortedByEnum.CONSUMER);
      setSortedBy(ctx, req, AdminFormField.SORTED_BY, false);
   }

   private void setBatchTimeout(PageContext ctx, AdminRequest req, String fieldName, boolean required) {
      String strBatchTimeout = ctx.getParameter(fieldName);

      if (strBatchTimeout == null || strBatchTimeout.length() == 0) {
         if (required) {
            ctx.addError(AdminErrorId.REQUIRED, fieldName);
         }
         return;
      }

      try {
         long batchTimeout = Long.parseLong(strBatchTimeout);

         req.setBatchTimeout(batchTimeout);
      } catch (NumberFormatException e) {
         ctx.addError(AdminErrorId.INVALID_FORMAT, fieldName);
      }
   }

   private void setConsumerType(PageContext ctx, AdminRequest req, String fieldName, boolean required) {
      String strConsumerType = ctx.getParameter(fieldName);

      if (strConsumerType == null || strConsumerType.length() == 0) {
         if (required) {
            ctx.addError(AdminErrorId.REQUIRED, fieldName);
         }
         return;
      }

      req.setConsumerType(strConsumerType);
   }

   private void setEventId(PageContext ctx, AdminRequest req, String fieldName, boolean required) {
      String strEventId = ctx.getParameter(fieldName);

      if (strEventId == null || strEventId.length() == 0) {
         if (required) {
            ctx.addError(AdminErrorId.REQUIRED, fieldName);
         }
         return;
      }

      try {
         int id = Integer.parseInt(strEventId);

         req.setEventId(id);
      } catch (NumberFormatException e) {
         ctx.addError(AdminErrorId.INVALID_FORMAT, fieldName);
      }
   }

   private void setEventType(PageContext ctx, AdminRequest req, String fieldName, boolean required) {
      String strEventType = ctx.getParameter(fieldName);

      if (strEventType == null || strEventType.length() == 0) {
         if (required) {
            ctx.addError(AdminErrorId.REQUIRED, fieldName);
         }
         return;
      }

      req.setEventType(strEventType);
   }

   private void setLastFetchedId(PageContext ctx, AdminRequest req, String fieldName, boolean required) {
      String strLastFetchedId = ctx.getParameter(fieldName);

      if (strLastFetchedId == null || strLastFetchedId.length() == 0) {
         if (required) {
            ctx.addError(AdminErrorId.REQUIRED, fieldName);
         }
         return;
      }

      try {
         int lastFetchedId = Integer.parseInt(strLastFetchedId);

         req.setLastFetchedId(lastFetchedId);
      } catch (NumberFormatException e) {
         ctx.addError(AdminErrorId.INVALID_FORMAT, fieldName);
      }
   }

   private void setSortedBy(PageContext ctx, AdminRequest req, String fieldName, boolean required) {
      String strSortedBy = ctx.getParameter(fieldName);

      if (strSortedBy == null || strSortedBy.length() == 0) {
         if (required) {
            ctx.addError(AdminErrorId.REQUIRED, fieldName);
         }
         return;
      }

      AdminSortedByEnum sortedBy = AdminSortedByEnum.getByName(strSortedBy);

      if (sortedBy != null) {
         req.setSortedBy(sortedBy);
      }
   }

}
