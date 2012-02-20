package com.site.wdbc.scbit;

import java.io.File;

import com.site.wdbc.http.configuration.AbstractConfiguration;

public class Configuration extends AbstractConfiguration {
   public Configuration() {
      super("wdbc-scbit");
   }

   @Property(name = "maxPages", desc = "Max pages to be downloaded?")
   public int getMaxPages() {
      return getParameterValue("maxPages", 10);
   }

   public static void main(String[] args) {
      generateScriptFile(new Configuration(), new File("src/main/scripts/robot.sh"), true);
      generateScriptFile(new Configuration(), new File("src/main/scripts/robot.bat"), false);
   }
}