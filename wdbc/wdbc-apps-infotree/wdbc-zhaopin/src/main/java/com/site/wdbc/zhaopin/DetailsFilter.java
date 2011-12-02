package com.site.wdbc.zhaopin;

import com.site.wdbc.WdbcResult;
import com.site.wdbc.query.DefaultWdbcFilter;

public class DetailsFilter extends DefaultWdbcFilter {

   @Override
   protected boolean shouldRemoveRow(WdbcResult result, int row) {
      String desc = (String) result.getCell(row, "desc");

      if (desc == null) {
         return true;
      } else {
         desc = desc.replace("[职位信息收藏]", "");
         desc = desc.replace("[该公司所有职位]", "");
         desc = desc.replace("[介绍给朋友]", "");

         result.setValue(row, "desc", desc);

         return false;
      }
   }
}
