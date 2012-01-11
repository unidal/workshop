package com.site.maven.plugin.project;

import java.io.File;

import org.apache.maven.model.Resource;
import org.apache.maven.project.MavenProject;

import com.site.lookup.ComponentTestCase;
import com.site.maven.plugin.common.Injector;

public class MigrateMojoTest extends ComponentTestCase {
   @SuppressWarnings("unchecked")
   private MavenProject createMavenProject(String finalName, String path) throws Exception {
      File baseDir = new File(path).getCanonicalFile();
      MavenProject project = new MavenProject();

      project.setFile(new File(baseDir, finalName + ".jar"));
      project.getCompileSourceRoots().add(new File(baseDir, "src/main/java").toString());
      project.getTestCompileSourceRoots().add(new File(baseDir, "src/test/java").toString());
      project.getResources().add(createResource(new File(baseDir, "src/main/resources")));
      project.getTestResources().add(createResource(new File(baseDir, "src/test/resources")));

      return project;
   }

   protected Resource createResource(File directory) {
      Resource resource = new Resource();

      resource.setDirectory(directory.toString());
      return resource;
   }

   public void testMigrate() throws Exception {
      MigrateMojo mojo = new MigrateMojo();

      Injector.setField(mojo, "project", createMavenProject("eunit-testfwk", "../../common/eunit-testfwk"));
      Injector.setField(mojo, "sourcePackage", "com.ebay.eunit");
      Injector.setField(mojo, "targetPackage", "com.site.eunit");
      Injector.setField(mojo, "targetDir", "target/eunit-testfwk");
      Injector.setField(mojo, "verbose", false);

      mojo.execute();
   }
}
