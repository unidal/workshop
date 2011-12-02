package com.site.game.sanguo;

import java.io.File;

import com.site.lookup.configuration.AbstractScriptConfiguration;

public class Configuration extends AbstractScriptConfiguration {
   public Configuration() {
      super("sanguo");
   }

   @Property(name = "server", desc = "Server are you connecting to.")
   public String getServerName() {
      return getParameterValue("server", "x17.sanguo.xiaonei.com");
   }

   @Property(name = "login.url", desc = "URL to login xiaonei.com.")
   public String getLoginUrl() {
      return getParameterValue("login.url", "http://login.xiaonei.com/Login.do");
   }

   @Property(name = "login.email", desc = "Email to login xiaonei.com.")
   public String getLoginEmail() {
      return getParameterValue("login.email", "");
   }

   @Property(name = "login.password", desc = "Password to login xiaonei.com.")
   public String getLoginPassword() {
      return getParameterValue("login.password", "");
   }

   @Property(name = "fight.diameter", desc = "Fight diameter.")
   public int getFightDiameter() {
      return getParameterValue("fight.diameter", 0);
   }

   @Property(name = "fight.minCount", desc = "Min fight count.")
   public int getFightMinCount() {
      return getParameterValue("fight.minCount", 30);
   }

   @Property(name = "fight.maxCount", desc = "Max fight count.")
   public int getFightMaxCount() {
      return getParameterValue("fight.maxCount", 30);
   }

   @Property(name = "fight.maxPopulation", desc = "Max fight population.")
   public int getFightMaxPopulation() {
      return getParameterValue("fight.maxPopulation", 50);
   }

   @Property(name = "fight.villages", desc = "Fight villages list in format of x1:y1,x2:y2")
   public String getFightVillages() {
      return getParameterValue("fight.villages", "");
   }

   @Property(name = "fight.noEmperors", desc = "No fight to emperors in format of emperor1,emperor2,...")
   public String getFightExcludeEmperors() {
      return getParameterValue("fight.noEmperors", "");
   }

   @Property(name = "building.plan", desc = "Customized building plan. i.e. village1=>build1:level1,build2:level2,... one line for each village")
   public String getBuildPlan() {
      return getParameterValue("building.plan", "");
   }

   public static void main(String[] args) {
      generateScriptFile(new Configuration(), new File("src/main/scripts/robot.sh"), true);
      generateScriptFile(new Configuration(), new File("src/main/scripts/robot.bat"), false);
   }
}