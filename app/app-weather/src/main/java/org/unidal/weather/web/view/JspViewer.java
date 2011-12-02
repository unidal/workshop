package org.unidal.weather.web.view;

import org.unidal.weather.web.WebAction;
import org.unidal.weather.web.WebContext;
import org.unidal.weather.web.WebModel;

import com.site.web.mvc.view.BaseJspViewer;

public class JspViewer extends BaseJspViewer<WebAction, WebContext, WebModel> {
   @Override
   protected String getJspFilePath(WebContext ctx, WebModel model) {
      switch (model.getAction()) {
      case REPORT:
         return JspFile.REPORT.getPath();
      }

      throw new RuntimeException("Unknown action: " + model.getAction());
   }
}
