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
         desc = desc.replace("[ְλ��Ϣ�ղ�]", "");
         desc = desc.replace("[�ù�˾����ְλ]", "");
         desc = desc.replace("[���ܸ�����]", "");

         result.setValue(row, "desc", desc);

         return false;
      }
   }
}
