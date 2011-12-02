package com.site.wdbc.n51job;

import org.codehaus.plexus.util.StringUtils;

import com.site.wdbc.WdbcResult;
import com.site.wdbc.query.DefaultWdbcFilter;

public class DetailsFilter extends DefaultWdbcFilter {

   @Override
   public void doFilter(WdbcResult result) {
      String title = aggregate(result, "title", "");
      String job1 = aggregate(result, "job1", "");
      String job2 = aggregate(result, "job2", "");
      String job = job1 + job2;
      String type = aggregate(result, "type", "");
      String jobAttrs = aggregate(result, "job-attrs", "\r\n");
      String jobDesc = aggregate(result, "job-desc", "\r\n");
      String companyDesc = aggregate(result, "company-desc", "\r\n");
      String picture = null;
      String category = null;
      String district = null;
      String mobilephone = null;
      String telephone = null;
      String msn = null;
      String qq = null;
      String email = aggregate(result, "email", "");

      jobAttrs = StringUtils.replace(jobAttrs, "£º\r\n", "£º");

      result.clear();
      result.addValue("title", title + " - " + job);
      result.addValue("body", title + "\r\n" + type + "\r\n\r\n" + jobAttrs + "\r\n\r\n" + jobDesc + "\r\n\r\n"
            + companyDesc);
      result.addValue("contact", email);
      result.addValue("category", category);
      result.addValue("district", district);
      result.addValue("picture", picture);
      result.addValue("mobilephone", mobilephone);
      result.addValue("telephone", telephone);
      result.addValue("msn", msn);
      result.addValue("qq", qq);
      result.addValue("email", email);
      result.applyLastRow();
   }
}
