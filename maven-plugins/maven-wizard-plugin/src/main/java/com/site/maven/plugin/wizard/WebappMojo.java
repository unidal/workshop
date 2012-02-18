package com.site.maven.plugin.wizard;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.project.MavenProject;
import org.jdom.Document;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;
import org.xml.sax.SAXException;

import com.site.codegen.generator.AbstractGenerateContext;
import com.site.codegen.generator.GenerateContext;
import com.site.codegen.generator.Generator;
import com.site.codegen.meta.WizardMeta;
import com.site.helper.Files;
import com.site.maven.plugin.common.PropertyProviders;
import com.site.maven.plugin.wizard.model.entity.Module;
import com.site.maven.plugin.wizard.model.entity.Page;
import com.site.maven.plugin.wizard.model.entity.Wizard;
import com.site.maven.plugin.wizard.model.transform.DefaultParser;

/**
 * Create a new page of web application project.
 * 
 * @goal webapp
 */
public class WebAppMojo extends AbstractMojo {
   /**
    * Current project
    * 
    * @parameter expression="${project}"
    * @required
    * @readonly
    */
   protected MavenProject m_project;

   /**
    * Current project base directory
    * 
    * @parameter expression="${basedir}"
    * @required
    * @readonly
    */
   protected File baseDir;

   /**
    * XSL code generator implementation
    * 
    * @component role="com.site.codegen.generator.Generator"
    *            role-hint="wizard-webapp"
    * @required
    * @readonly
    */
   protected Generator m_generator;

   /**
    * Wizard meta component
    * 
    * @component
    * @required
    * @readonly
    */
   protected WizardMeta m_meta;

   /**
    * Current project base directory
    * 
    * @parameter expression="${sourceDir}" default-value="${basedir}"
    * @required
    */
   protected String sourceDir;

   /**
    * Location of manifest.xml file
    * 
    * @parameter expression="${manifest}" default-value=
    *            "${basedir}/src/main/resources/META-INF/wizard/webapp/manifest.xml"
    * @required
    */
   protected String manifest;

   /**
    * Location of generated source directory
    * 
    * @parameter expression="${resource.base}"
    *            default-value="/META-INF/wizard/webapp"
    * @required
    */
   protected String resouceBase;

   /**
    * Verbose information or not
    * 
    * @parameter expression="${verbose}" default-value="false"
    */
   protected boolean verbose;

   /**
    * Verbose information or not
    * 
    * @parameter expression="${debug}" default-value="false"
    */
   protected boolean debug;

   protected Wizard buildWizard(File wizardFile) throws IOException, SAXException {
      Wizard wizard;

      if (wizardFile.isFile()) {
         String content = Files.forIO().readFrom(wizardFile, "utf-8");

         wizard = new DefaultParser().parse(content);
      } else {
         wizard = new Wizard();
         String packageName = PropertyProviders.fromConsole().forString("package", "Java package name for webapp:",
               null, null);
         boolean webres = PropertyProviders.fromConsole().forBoolean("webres", "Support WebRes framework?", false);

         wizard.setPackage(packageName);
         wizard.setWebres(webres);
      }

      List<String> moduleNames = new ArrayList<String>(wizard.getModules().size());

      for (Module module : wizard.getModules()) {
         moduleNames.add(module.getName());
      }

      String moduleName = PropertyProviders.fromConsole().forString("module", "Module name:", moduleNames, null, null);
      Module module = wizard.findModule(moduleName);

      if (module == null) {
         String path = PropertyProviders.fromConsole().forString("path", "Module path:", moduleName.substring(0, 1),
               null);

         module = new Module(moduleName);

         module.setPath(path);
         wizard.addModule(module);
      }

      List<String> pageNames = new ArrayList<String>(module.getPages().size());

      for (Page page : module.getPages()) {
         pageNames.add(page.getName());
      }

      String pageName = PropertyProviders.fromConsole().forString("page", "Page name:", pageNames, null, null);
      Page page = module.findPage(pageName);

      if (page == null) {
         String path = PropertyProviders.fromConsole().forString("path", "Page path:", pageName, null);

         page = new Page(pageName);

         if (module.getPages().isEmpty()) {
            page.setDefault(true);
         }

         page.setPath(path);
         page.setDescription(Character.toUpperCase(pageName.charAt(0)) + pageName.substring(1));
         module.addPage(page);
      }

      return wizard;
   }

   public void execute() throws MojoExecutionException, MojoFailureException {
      try {
         final File manifestFile = getFile(manifest);
         File wizardFile = new File(manifestFile.getParentFile(), "wizard.xml");
         Reader reader = new StringReader(buildWizard(wizardFile).toString());

         if (!manifestFile.exists()) {
            saveXml(m_meta.getManifest("wizard.xml"), manifestFile);
         }

         saveXml(m_meta.getWizard(reader), wizardFile);

         final URL manifestXml = manifestFile.toURI().toURL();
         final GenerateContext ctx = new AbstractGenerateContext(m_project.getBasedir(), resouceBase, sourceDir) {
            public URL getManifestXml() {
               return manifestXml;
            }

            public void log(LogLevel logLevel, String message) {
               switch (logLevel) {
               case DEBUG:
                  if (debug) {
                     getLog().debug(message);
                  }
                  break;
               case INFO:
                  if (debug || verbose) {
                     getLog().info(message);
                  }
                  break;
               case ERROR:
                  getLog().error(message);
                  break;
               }
            }
         };

         m_generator.generate(ctx);
         m_project.addCompileSourceRoot(sourceDir);
         getLog().info(ctx.getGeneratedFiles() + " files generated.");
      } catch (Exception e) {
         throw new MojoExecutionException("Code generating failed.", e);
      }
   }

   protected File getFile(String path) {
      File file;

      if (path.startsWith("/") || path.indexOf(':') > 0) {
         file = new File(path);
      } else {
         file = new File(baseDir, path);
      }

      return file;
   }

   protected void saveXml(Document doc, File file) throws IOException {
      File parent = file.getCanonicalFile().getParentFile();

      if (!parent.exists()) {
         parent.mkdirs();
      }

      Format format = Format.getPrettyFormat();
      XMLOutputter outputter = new XMLOutputter(format);
      FileWriter writer = new FileWriter(file);

      try {
         outputter.output(doc, writer);
         getLog().info("File " + file.getCanonicalPath() + " generated.");
      } finally {
         writer.close();
      }
   }
}
