package com.site.maven.plugin.project;

import java.io.File;
import java.util.List;

import org.apache.maven.model.Resource;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.project.MavenProject;

import com.site.helper.Files;

/**
 * Migrate all source files from one project to another project using a
 * different package base.
 * 
 * @goal migrate
 */
public class MigrateMojo extends AbstractMojo {
   /**
    * Current project
    * 
    * @parameter expression="${project}"
    * @required
    * @readonly
    */
   private MavenProject project;

   /**
    * @parameter expression="${sourcePackage}"
    * @required
    */
   private String sourcePackage;

   /**
    * @parameter expression="${targetPackage}"
    * @required
    */
   private String targetPackage;

   /**
    * @parameter expression="${targetDir}" default-value="target/${project.artifactId}"
    * @required
    */
   private String targetDir;

   /**
    * @parameter expression="${verbose}" default-value="false"
    * @required
    */
   private boolean verbose;

   private int m_success;

   private int m_failure;

   @SuppressWarnings("unchecked")
   public void execute() throws MojoExecutionException, MojoFailureException {
      try {
         File baseDir = project.getBasedir();
         File targetBase = new File(baseDir, targetDir).getCanonicalFile();
         long start = System.currentTimeMillis();

         m_success = 0;
         m_failure = 0;

         for (String source : (List<String>) project.getCompileSourceRoots()) {
            migrateSource(baseDir, targetBase, source);
         }

         for (String testSource : (List<String>) project.getTestCompileSourceRoots()) {
            migrateSource(baseDir, targetBase, testSource);
         }

         for (Resource resource : (List<Resource>) project.getResources()) {
            migrateSource(baseDir, targetBase, resource.getDirectory());
         }

         for (Resource testResource : (List<Resource>) project.getTestResources()) {
            migrateSource(baseDir, targetBase, testResource.getDirectory());
         }

         if (new File(baseDir, "pom.xml").exists()) {
            migrateFile(new File(baseDir, "pom.xml"), new File(targetBase, "pom.xml"));
         }

         log(String.format("[INFO] %s files migrated%s in %s ms.", m_success, m_failure == 0 ? "" : " but " + m_failure
               + " failures", (System.currentTimeMillis() - start)));
      } catch (Exception e) {
         throw new MojoFailureException(String.format(
               "[ERROR] Error when migrating project[sourcePackage: %s, targetPackage: %s, targetDir: %s].", sourcePackage,
               targetPackage, targetDir), e);
      }
   }

   protected void log(String message) {
      log(message, null);
   }

   protected void log(String message, Exception e) {
      if (verbose) {
         System.out.println(message);
      }

      if (e != null) {
         e.printStackTrace(System.err);
      }
   }

   protected void migrateDirectory(File source, File target, String sourcePath, String sourcePackageName) {
      String targetPath;

      if (sourcePackageName != null && sourcePackageName.startsWith(sourcePackage)) {
         String targetPackageName = targetPackage + sourcePackageName.substring(sourcePackage.length());
         targetPath = targetPackageName.replace('.', '/');
      } else {
         targetPath = sourcePath;
      }

      File base = sourcePath == null ? source : new File(source, sourcePath);
      String[] names = base.list();

      for (String name : names) {
         File file = new File(base, name);

         if (file.isDirectory()) {
            String newPath = sourcePath == null ? name : sourcePath + "/" + name;
            String newPackageName = sourcePackageName == null ? name : sourcePackageName + "." + name;

            migrateDirectory(source, target, newPath, newPackageName);
         } else if (file.isFile()) {
            migrateFile(file, new File(target, targetPath + "/" + name));
         }
      }
   }

   protected void migrateFile(File source, File target) {
      try {
         String original = Files.forIO().readFrom(source, "utf-8");
         String migrated = replaceAll(original, sourcePackage, targetPackage);

         Files.forIO().writeTo(target, migrated);
         log(String.format("[INFO] File(%s) created, content length is %s.", target, target.length()));
         m_success++;
      } catch (Exception e) {
         log(String.format("[ERROR] Error when migrating file(%s)!", source), e);
         m_failure++;
      }
   }

   protected void migrateSource(File sourceBase, File targetBase, String path) {
      String basePath = sourceBase.getPath();
      String relativePath;

      if (path.startsWith(basePath)) {
         relativePath = path.substring(basePath.length());
      } else {
         relativePath = path;
      }

      File source = new File(sourceBase, relativePath);
      File target = new File(targetBase, relativePath);

      migrateDirectory(source, target, null, null);
   }

   protected String replaceAll(String source, String fromToken, String toToken) {
      int toLength = toToken.length();
      int fromLength = fromToken.length();
      StringBuilder sb = new StringBuilder(source.length() + (toLength < fromLength ? 0 : toLength - fromLength) * 30);
      int offset = 0;
      int index = source.indexOf(fromToken, offset);

      while (true) {
         if (index == -1) {
            sb.append(source.substring(offset));
            break;
         } else {
            sb.append(source.substring(offset, index));
            sb.append(toToken);

            offset = index + fromLength;
            index = source.indexOf(fromToken, offset);
         }
      }

      return sb.toString();
   }
}
