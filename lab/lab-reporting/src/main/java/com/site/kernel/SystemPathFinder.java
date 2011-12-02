package com.site.kernel;

import java.io.File;
import java.io.IOException;

public class SystemPathFinder {
   public static final String CONFIG_DIR = "config";

   public static final String PROPERTY_APP_ROOT = "appRoot";

   public static final String PROPERTY_CONFIG_DIR = "configDir";

   private static File s_appRoot;

   private static File s_configDir;

   public static File getAppConfig() {
      if (s_configDir == null) {
         s_configDir = getDefaultConfigDir();
      }

      return s_configDir;
   }

   public static File getAppRoot() {
      if (s_appRoot == null) {
         s_appRoot = getDefaultAppRoot();
      }

      return s_appRoot;
   }

   private static File getDefaultAppRoot() {
      // Try to find appRoot from system properties, which is
      // specified with -DappRoot=...
      String appRoot = System.getProperty(PROPERTY_APP_ROOT, null);

      if (appRoot != null) {
         File dir = new File(appRoot);

         if (!dir.isDirectory()) {
            throw new IllegalArgumentException("Application root specified with -D" + PROPERTY_APP_ROOT + "=... must be an existing directory.");
         } else {
            return getCanonicalFile(dir);
         }
      }

      // Try to find "config" directory from current directory,
      // if not found, then try at parent of current directory
      File root = new File(".");

      // check if there is a "config" sub-directory
      if (new File(root, CONFIG_DIR).isDirectory()) {
         return getCanonicalFile(root);
      }

      // check its parent dierctory
      if (new File("../" + CONFIG_DIR).isDirectory()) {
         return getCanonicalFile(new File(".."));
      }

      return new File(".");
//      throw new IllegalArgumentException("No appRoot is configured, system will probe it from next ways sequently:\n" + "   a) System property \"" + PROPERTY_APP_ROOT
//            + "\", which is added with -D" + PROPERTY_APP_ROOT + "=... in JVM parameter;\n" + "   b) Current directory(" + new File(".").getAbsolutePath()
//            + ") if there is a \"config\" sub-directory there;\n" + "   c) Parent of current directory if there is a \"config\" sub-directory.");
   }

   private static File getDefaultConfigDir() {
      // Try to find appRoot from system properties, which is
      // specified with -DconfigDir=...
      String configDir = System.getProperty(PROPERTY_CONFIG_DIR, null);

      if (configDir != null) {
         File dir = new File(configDir);

         if (!dir.isDirectory()) {
            throw new IllegalArgumentException("Config dir specified with -D" + PROPERTY_CONFIG_DIR + "=... must be an existing directory.");
         } else {
            return getCanonicalFile(dir);
         }
      }

      return new File(getAppRoot(), CONFIG_DIR);
   }

   private static File getCanonicalFile(File file) {
      try {
         return file.getCanonicalFile();
      } catch (IOException e) {
         return file;
      }
   }

   // used by unit test
   public static void setAppRoot(File appRoot) {
      s_appRoot = appRoot;
   }

   public static void setConfigDir(File configDir) {
      s_configDir = configDir;
   }
}
