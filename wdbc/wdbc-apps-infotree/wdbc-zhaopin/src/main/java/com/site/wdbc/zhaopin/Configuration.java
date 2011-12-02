package com.site.wdbc.zhaopin;

import java.io.File;

import com.site.wdbc.infotree.AbstractConfiguration;

public class Configuration extends AbstractConfiguration {
   public Configuration() {
      super("wdbc-zhaopin");
   }

   @Property(name = "url", desc = "Search URL for the job list", required = true)
   public String getUrl() {
      return getParameterValue("url");
   }
   
   public static void main(String[] args) {
      generateScriptFile(new Configuration(), new File("src/main/scripts/robot.sh"), true);
      generateScriptFile(new Configuration(), new File("src/main/scripts/robot.bat"), false);
   }
}