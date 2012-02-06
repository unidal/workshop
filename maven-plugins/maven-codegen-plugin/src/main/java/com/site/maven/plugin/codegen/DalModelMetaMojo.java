package com.site.maven.plugin.codegen;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.project.MavenProject;
import org.jdom.Document;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

import com.site.codegen.meta.ModelMeta;
import com.site.maven.plugin.common.PropertyProviders;

/**
 * DAL Metadata generator for Model
 * 
 * @goal dal-model-meta
 * @author Frankie Wu
 */
public class DalModelMetaMojo extends AbstractMojo {
   /**
    * Current project
    * 
    * @parameter expression="${project}"
    * @required
    * @readonly
    */
   protected MavenProject m_project;

   /**
    * XML meta component
    * 
    * @component
    * @required
    * @readonly
    */
   protected ModelMeta m_meta;

   /**
    * Current project base directory
    * 
    * @parameter expression="${basedir}"
    * @required
    * @readonly
    */
   protected File baseDir;

   /**
    * @parameter expression="${inputFile}"
    */
   protected String inputFile;

   /**
    * @parameter expression="${outputDir}"
    *            default-value="${basedir}/src/main/resources/META-INF/dal/model"
    * @required
    */
   protected String outputDir;

   /**
    * @parameter expression="${packageName}"
    */
   protected String packageName;

   /**
    * @parameter expression="${prefix}"
    */
   protected String prefix;

   private String defaultPackageName() {
      if (packageName != null) {
         return packageName;
      }

      String groupId = m_project.getGroupId();
      String artifactId = m_project.getArtifactId();

      return (groupId + "." + artifactId + ".model").replace('-', '.');
   }

   public void execute() throws MojoExecutionException, MojoFailureException {
      String f = getProperty(inputFile, "inputFile", "Sample XML file path:", null);
      String p = getProperty(prefix, "prefix", "Prefix name of target files:", null);

      if (f == null) {
         throw new MojoExecutionException("please provide sample XML file path via -DinputFile=...");
      }

      try {
         File inFile = getFile(f);
         Reader reader = new InputStreamReader(new FileInputStream(inFile), "utf-8");
         Document codegen = m_meta.getCodegen(reader);
         File outDir = getFile(outputDir);
         File outFile = new File(outDir, p == null ? "codegen.xml" : p + "-codegen.xml");

         if (!outDir.exists()) {
            outDir.mkdirs();
         }

         saveFile(codegen, outFile);

         File modelFile = new File(outDir, p == null ? "model.xml" : p + "-model.xml");

         if (!modelFile.exists()) {
            String n = getProperty(packageName, "packageName", "Package name of generated model:", defaultPackageName());
            Document model = m_meta.getModel(n);

            saveFile(model, modelFile);
         }

         File manifestFile = new File(outDir, p == null ? "manifest.xml" : p + "-manifest.xml");

         if (!manifestFile.exists()) {
            Document manifest = m_meta.getManifest(outFile.getName(), modelFile.getName());

            saveFile(manifest, manifestFile);
         }
      } catch (Exception e) {
         throw new MojoExecutionException("Error when generating model meta: " + e, e);
      }
   }

   private String getProperty(String value, String name, String prompt, String defaultValue) {
      if (value != null) {
         return value;
      } else {
         return PropertyProviders.fromConsole().forString(name, prompt, defaultValue, null);
      }
   }

   private File getFile(String path) {
      File file;

      if (path.startsWith("/") || path.indexOf(':') > 0) {
         file = new File(path);
      } else {
         file = new File(baseDir, path);
      }

      return file;
   }

   private void saveFile(Document codegen, File file) throws IOException {
      Format format = Format.getPrettyFormat();
      XMLOutputter outputter = new XMLOutputter(format);
      FileWriter writer = new FileWriter(file);

      try {
         outputter.output(codegen, writer);
         getLog().info("File " + file.getCanonicalPath() + " generated.");
      } finally {
         writer.close();
      }
   }
}
