package com.site.wdbc.dianping;

import java.text.MessageFormat;

import org.codehaus.plexus.util.StringUtils;

import com.site.wdbc.WdbcResult;
import com.site.wdbc.query.DefaultWdbcFilter;

public class ListFilter extends DefaultWdbcFilter {
   @SuppressWarnings("unused")
   private Configuration m_configuration;

   @SuppressWarnings("unused")
   private int m_minHours;

   @SuppressWarnings("unused")
   private int m_maxDays;

   private MessageFormat m_rankFormat = new MessageFormat("口味:{0}  环境:{1}  服务:{2}  人均:{3}");

   @Override
   protected boolean shouldRemoveRow(WdbcResult result, int row) {
      String title = (String) result.getCell(row, "title");
      String link = (String) result.getCell(row, "link");
      String desc = (String) result.getCell(row, "desc");
      String kw = (String) result.getCell(row, "kw");
      String hj = (String) result.getCell(row, "hj");
      String fw = (String) result.getCell(row, "fw");
      String rj = (String) result.getCell(row, "rj");

      if (link == null || title == null || desc == null) {
         return true;
      } else {
         result.setValue(row, "link", "http://www.dianping.com" + link);
         result.setValue(row, "rank", m_rankFormat.format(new Object[] { kw, hj, fw, rj }));
         result.setValue(row, "desc", StringUtils.replace(desc, "\n>", "\n"));
         return false;
      }
   }

   public void setMinHours(int minHours) {
      m_minHours = minHours;
   }

   public void setMaxDays(int maxDays) {
      m_maxDays = maxDays;
   }

}
