package org.unidal.xml;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.StringReader;
import java.net.URL;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import com.site.codegen.generator.AbstractGenerateContext;
import com.site.codegen.generator.GenerateContext;
import com.site.codegen.generator.Generator;
import com.site.codegen.manifest.Manifest;
import com.site.codegen.manifest.ManifestCreator;
import com.site.codegen.meta.XmlMetaHelper;

public class XmlCodeGenerator {
   private ManifestCreator m_manifestCreator;

   private XmlMetaHelper m_xmlMetaHelper;

   private Generator m_generator;

   public void generateXml(OutputStream out, String content, String[] namespaces, String[] packages) throws Exception {
      final URL manifestXml = new File(getManifest(content, namespaces, packages)).toURI().toURL();
      final GenerateContext ctx = new ZipGenerateContext(out, manifestXml);

      m_generator.generate(ctx);
   }

   private String getManifest(String content, String[] namespaces, String[] packages) throws IOException {
      if (content == null || content.length() == 0) {
         throw new IllegalArgumentException("content: " + content);
      } else if (packages == null || packages.length == 0 || packages[0] == null) {
         throw new IllegalArgumentException("packages: " + packages);
      }

      String userContent = "<?xml version=\"1.0\" encoding=\"utf-8\"?>" + "<root do-package=\"" + packages[0] + "\"/>";

      StringReader reader = new StringReader(content);
      String xmlMetaContent = m_xmlMetaHelper.getXmlMetaContent(reader);

      return m_manifestCreator.create(xmlMetaContent, userContent);
   }

   private class ZipGenerateContext extends AbstractGenerateContext {
      private URL m_manifestXml;

      private int m_generatedFiles;

      private ZipOutputStream m_out;

      public ZipGenerateContext(OutputStream out, URL manifestXml) {
         super(null, "/META-INF/dal/xml", null);

         m_out = new ZipOutputStream(out);
         m_manifestXml = manifestXml;
      }

      public URL getManifestXml() {
         return m_manifestXml;
      }

      @Override
      public void addFileToStorage(Manifest manifest, String content) throws IOException {
         String path = manifest.getPath();
         ZipEntry entry = new ZipEntry(path.substring(1));

         entry.setTime(System.currentTimeMillis());
         m_out.putNextEntry(entry);
         m_out.write(content.getBytes("utf-8"));
         log(LogLevel.INFO, manifest.getPath() + " generated");
         m_generatedFiles++;
      }

      @Override
      public void closeStorage() throws IOException {
         m_out.finish();
         m_out.close();
      }

      @Override
      public int getGeneratedFiles() {
         return m_generatedFiles;
      }

      public void log(LogLevel logLevel, String message) {
         // ignore it
      }
   }
}
