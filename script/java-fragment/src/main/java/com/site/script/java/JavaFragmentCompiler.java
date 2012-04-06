package com.site.script.java;

import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;

import javax.script.Compilable;
import javax.script.CompiledScript;
import javax.script.ScriptException;
import javax.tools.Diagnostic;
import javax.tools.DiagnosticCollector;
import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.StandardLocation;
import javax.tools.ToolProvider;

public class JavaFragmentCompiler implements Compilable {
   private JavaFragmentEngine m_engine;

   private boolean m_forceCompile = false;

   public JavaFragmentCompiler(JavaFragmentEngine engine) {
      m_engine = engine;
   }

   private URL[] buildUrls(File outputDir) {
      URL[] urls = new URL[1];

      try {
         urls[0] = outputDir.toURI().toURL();
      } catch (MalformedURLException e) {
         throw new RuntimeException("Error when building URLs for class loader!", e);
      }

      return urls;
   }

   @Override
   public CompiledScript compile(Reader reader) throws ScriptException {
      StringBuilder sb = new StringBuilder(4096);
      char[] buf = new char[2048];

      try {
         while (true) {
            int size = reader.read(buf);

            if (size < 0) {
               break;
            }

            sb.append(buf, 0, size);
         }
      } catch (IOException e) {
         throw new RuntimeException("Error when reading script from " + reader + "!");
      } finally {
         try {
            reader.close();
         } catch (IOException e) {
            // ignore it
         }
      }

      return compile(sb.toString());
   }

   @Override
   public CompiledScript compile(String script) throws ScriptException {
      File outputDir = getOutputDirectory();
      String classpath = System.getProperty("java.class.path");
      CompiledJavaFragment compiledScript = new CompiledJavaFragment(m_engine);
      JavaSourceFromString source = new JavaSourceFromString(outputDir, script);

      if (m_forceCompile || !source.getClassFile().exists()) {
         compileInternal(script, outputDir, classpath, source);
      }

      URL[] urls = buildUrls(outputDir);
      ClassLoader parent = Thread.currentThread().getContextClassLoader();
      URLClassLoader classloader = new URLClassLoader(urls, parent);

      compiledScript.setClassLoader(classloader);
      compiledScript.setSource(source);
      return compiledScript;
   }

   private void compileInternal(String script, File outputDir, String classpath, JavaSourceFromString source)
         throws ScriptException {
      Locale locale = Locale.getDefault();
      Charset charset = Charset.defaultCharset();
      JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
      DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector<JavaFileObject>();
      StandardJavaFileManager manager = compiler.getStandardFileManager(diagnostics, locale, charset);

      outputDir.mkdirs();

      try {
         manager.setLocation(StandardLocation.CLASS_PATH, toClassFiles(classpath));
         manager.setLocation(StandardLocation.CLASS_OUTPUT, Arrays.asList(outputDir.getCanonicalFile()));

         Boolean result = compiler.getTask(null, manager, diagnostics, null, null, Arrays.asList(source)).call();

         if (!Boolean.TRUE.equals(result)) {
            for (Diagnostic<? extends JavaFileObject> diagnostic : diagnostics.getDiagnostics()) {
               throw new ScriptException(diagnostic.getMessage(locale), script, (int) diagnostic.getLineNumber()
                     - source.getLineOffset(), (int) diagnostic.getColumnNumber());
            }
         }

         manager.close();
      } catch (ScriptException e) {
         throw e;
      } catch (Exception e) {
         e.printStackTrace();
      }
   }

   private File getOutputDirectory() {
      return new File("target/tmp-classes");
   }

   private List<File> toClassFiles(String classpath) {
      String[] paths = classpath.split(Pattern.quote(File.pathSeparator));
      int len = paths.length;
      List<File> files = new ArrayList<File>(len);

      for (String path : paths) {
         files.add(new File(path));
      }

      return files;
   }

   public void setForceCompile(boolean forceCompile) {
      m_forceCompile = forceCompile;
   }
}
