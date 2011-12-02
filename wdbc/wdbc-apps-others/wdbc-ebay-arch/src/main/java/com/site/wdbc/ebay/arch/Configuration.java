package com.site.wdbc.ebay.arch;

import java.io.File;

import com.site.wdbc.http.configuration.AbstractConfiguration;

public class Configuration extends AbstractConfiguration {
   public Configuration() {
      super("wdbc-ebay-arch");
   }

   public static void main(String[] args) {
      generateScriptFile(new Configuration(), new File("src/main/scripts/robot.sh"), true);
      generateScriptFile(new Configuration(), new File("src/main/scripts/robot.bat"), false);
   }

   @Property(name = "loginId", desc = "Login id.")
   public String getUserName() {
      return getParameterValue("loginId");
   }

   @Property(name = "password", desc = "Password.")
   public String getPassword() {
      return getParameterValue("password");
   }

   @Property(name = "outputDir", desc = "Output directory.")
   public File getOutputDir() {
      return new File(getParameterValue("outputDir", "target"));
   }
}
