package com.site.kernel.common;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.net.URL;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Templates;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.URIResolver;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

public abstract class BaseXslTransformer {
   protected static final class ManifestParser extends BaseXmlHandler {
      private List<ResultFile> m_resultFiles;

      private String m_propertyName;

      public ManifestParser() {
         m_resultFiles = new LinkedList<ResultFile>();
      }

      public void characters(char[] ch, int start, int length) throws SAXException {
         if (m_propertyName != null) {
            String value = new String(ch, start, length);

            if (!m_resultFiles.isEmpty()) {
               int size = m_resultFiles.size();
               ResultFile file = m_resultFiles.get(size - 1);

               file.addProperties(m_propertyName, value);
            }
         }
      }

      public void endElement(String namespaceURI, String localName, String rawName) throws SAXException {
         m_propertyName = null;
      }

      public List<ResultFile> getResultFiles() {
         return m_resultFiles;
      }

      public void startElement(String namespaceURI, String localName, String rawName, Attributes attrs) throws SAXException {
         String tag = localName;

         m_propertyName = null;

         if (tag.equals("file")) {
            ResultFile file = new ResultFile();

            file.setPath(new File(attrs.getValue("path")));
            file.setTemplate(new File(attrs.getValue("template")));
            file.setMode(OutputFileMode.getByName(attrs.getValue("mode")));

            m_resultFiles.add(file);
         } else if (tag.equals("property")) {
            m_propertyName = attrs.getValue("name");
         }
      }
   }

   protected static final class OutputFileMode {
      public static final OutputFileMode CREATE_OR_OVERWRITE = new OutputFileMode(1, "create_or_overwrite");

      public static final OutputFileMode CREATE_IF_NOT_EXISTS = new OutputFileMode(2, "create_if_not_exists");

      public static final OutputFileMode CREATE_OR_APPEND = new OutputFileMode(3, "create_or_append");

      public static OutputFileMode getByName(String name) {
         if (CREATE_OR_OVERWRITE.getName().equals(name)) {
            return CREATE_OR_OVERWRITE;
         } else if (CREATE_IF_NOT_EXISTS.getName().equals(name)) {
            return CREATE_IF_NOT_EXISTS;
         } else if (CREATE_OR_APPEND.getName().equals(name)) {
            return CREATE_OR_APPEND;
         }

         throw new RuntimeException("No ResultFileMode defined for " + name);
      }

      private int m_mode;

      private String m_name;

      private OutputFileMode(int mode, String name) {
         m_mode = mode;
         m_name = name;
      }

      public int getMode() {
         return m_mode;
      }

      public String getName() {
         return m_name;
      }
   }

   protected static final class ResultFile {
      private File m_path;

      private File m_template;

      private OutputFileMode m_mode;

      private HashMap<String, String> m_properties;

      public ResultFile() {
         m_properties = new HashMap<String, String>();
      }

      public void addProperties(String name, String value) {
         String firstPart = m_properties.get(name);

         if (firstPart == null) {
            m_properties.put(name, value);
         } else {
            m_properties.put(name, firstPart + value);
         }
      }

      public OutputFileMode getMode() {
         return m_mode;
      }

      public File getPath() {
         return m_path;
      }

      public HashMap<String, String> getProperties() {
         return m_properties;
      }

      public File getTemplate() {
         return m_template;
      }

      public void setMode(OutputFileMode mode) {
         m_mode = mode;
      }

      public void setPath(File path) {
         m_path = path;
      }

      public void setTemplate(File template) {
         m_template = template;
      }
   }

   protected static final class XslURIResolver implements URIResolver {
      public Source resolve(String href, String base) throws TransformerException {
         try {
            URL uri = new URL(new URL(base), href);

            return new StreamSource(uri.openStream(), uri.toString());
         } catch (Exception e) {
            // ignore it
            e.printStackTrace();
         }

         // let the processor to resolve the URI itself
         return null;
      }
   }

   private static final HashMap<URL, Templates> s_cachedTemplates = new HashMap<URL, Templates>();

   private static final HashMap<URL, Long> s_lastModifiedDates = new HashMap<URL, Long>();

   /**
    * Get template from cache if exists, otherwise create a new and put it into
    * cache
    */
   protected static Templates getTemplates(URL style) throws TransformerException {
      Templates templates = s_cachedTemplates.get(style);
      Long lastModifiedDate = s_lastModifiedDates.get(style);
      long lastModified = 0;

      if ("file".equals(style.getProtocol())) {
         lastModified = new File(style.getFile()).lastModified();
      }

      if (templates == null || lastModifiedDate.longValue() != lastModified) {
         TransformerFactory factory = TransformerFactory.newInstance();

         try {
            factory.setURIResolver(new XslURIResolver());
            templates = factory.newTemplates(new StreamSource(style.openStream(), style.toString()));

            s_cachedTemplates.put(style, templates);
            s_lastModifiedDates.put(style, new Long(lastModified));
         } catch (IOException e) {
            throw new TransformerException("Fail to open XSL template: " + style, e);
         }
      }

      return templates;
   }

   protected static void message(String msg) {
      System.out.println(msg);
   }

   private static void setParameters(Transformer transformer, Map properties) {
      if (properties == null) {
         return;
      }

      for (Iterator i = properties.keySet().iterator(); i.hasNext();) {
         String name = (String) i.next();
         String value = (String) properties.get(name);

         transformer.setParameter(name, value);
      }
   }

   private void createDir(File dir) {
      if (!dir.exists() && !dir.mkdirs()) {
         message("Can not create package directory: " + dir);
      }
   }

   protected byte[] doTransform(URL xslTemplate, InputStream stream, Map properties) throws IOException, TransformerConfigurationException, TransformerException {
      ByteArrayOutputStream buffer = new ByteArrayOutputStream(64 * 1024);

      transform(xslTemplate, new StreamSource(stream), new StreamResult(buffer), properties);

      return buffer.toByteArray();
   }

   protected String doTransform(URL xslTemplate, Reader reader, Map properties) throws IOException, TransformerConfigurationException, TransformerException {
      StringWriter buffer = new StringWriter(64 * 1024);

      transform(xslTemplate, new StreamSource(reader), new StreamResult(buffer), properties);

      return buffer.toString();
   }

   protected int doTransform(URL xslTemplate, Source source, Map properties, File outputFile, OutputFileMode mode) throws IOException, TransformerConfigurationException,
         TransformerException {

      if (outputFile.exists()) {
         if (mode == OutputFileMode.CREATE_IF_NOT_EXISTS) {
            // Skip it, return directly
            return 0;
         } else if (!outputFile.canWrite()) {
            message("File " + outputFile + " is read-only");
            return 0;
         }
      }

      // create the parent directory if not exists
      createDir(outputFile.getParentFile());

      Templates pss = getTemplates(xslTemplate);
      Transformer transformer = pss.newTransformer();
      boolean append = (mode == OutputFileMode.CREATE_OR_APPEND);
      Writer resultWriter = new BufferedWriter(new FileWriter(outputFile, append));

      setParameters(transformer, properties);

      try {
         transformer.transform(source, new StreamResult(resultWriter));
      } finally {
         resultWriter.close();

         message("[" + new Date() + "] File " + outputFile + " is generated");
      }

      return 1;
   }

   protected void transform(URL xslTemplate, Source source, Result result, Map properties) throws IOException, TransformerException, TransformerConfigurationException {
      Templates pss = getTemplates(xslTemplate);
      Transformer transformer = pss.newTransformer();

      setParameters(transformer, properties);

      transformer.transform(source, result);
   }
}
