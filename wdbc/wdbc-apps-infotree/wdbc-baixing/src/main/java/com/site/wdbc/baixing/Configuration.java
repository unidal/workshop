package com.site.wdbc.baixing;

import java.io.File;

import com.site.wdbc.infotree.AbstractConfiguration;

public class Configuration extends AbstractConfiguration {
   public Configuration() {
      super("wdbc-baixing");
   }

   public static void main(String[] args) {
      generateScriptFile(new Configuration(), new File("src/main/scripts/robot.sh"), true);
      generateScriptFile(new Configuration(), new File("src/main/scripts/robot.bat"), false);
   }
}