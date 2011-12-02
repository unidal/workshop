package com.site.bes.server.admin;

import java.io.IOException;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import com.site.kernel.dal.model.common.FormatingException;
import com.site.web.WebModelFormatter;
import com.site.web.page.PageContext;

public class AdminApp {
   private AdminApp() {
   }

   public static AdminApp newInstance() {
      return new AdminApp();
   }

   public void handle(PageContext ctx, AdminRequest req, AdminResponse res) throws IOException {
      AdminFormAction action = req.getFormAction();
      AdminHandler handler = AdminHandler.getInstance();

      if (action == AdminFormAction.LIST) {
         handler.handleList(ctx, req, res);
      } else if (action == AdminFormAction.EVENT) {
         handler.handleEvent(ctx, req, res);

         if (req.getPageMode() == AdminPageMode.XML) {
            try {
               showXml(ctx, new WebModelFormatter(res).buildXml(true));
            } catch (FormatingException e) {
               e.printStackTrace();
            }
         }
      } else if (action == AdminFormAction.DASHBOARD) {
         handler.handleDashboard(ctx, req, res);
      } else {
         throw new RuntimeException("Unknown action: " + action);
      }
   }

   private void showXml(PageContext ctx, String model) throws IOException {
      HttpServletResponse response = ctx.getResponse();
      ServletOutputStream out = response.getOutputStream();
      
      response.setContentType("text/xml");
      out.print(model);
      out.close();
   }
}
