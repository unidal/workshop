package org.unidal.ezsell.view;

import java.io.OutputStream;
import java.io.StringReader;
import java.util.Map;
import java.util.regex.Pattern;

import org.codehaus.plexus.personality.plexus.lifecycle.phase.Initializable;
import org.codehaus.plexus.personality.plexus.lifecycle.phase.InitializationException;
import org.codehaus.plexus.util.IOUtil;
import org.xml.sax.InputSource;

import com.site.lookup.annotation.Inject;
import com.site.service.pdf.tag.DataParser;

public class ReportGenerator implements Initializable {
   @Inject
   private String m_xmlTemplate;

   public void generate(Map<String, Object> params, OutputStream out) throws Exception {
      String template = IOUtil.toString(getClass().getResourceAsStream(m_xmlTemplate));
      String xmlContent = parseXml(template, params);

      generate(xmlContent, out);
   }

   public void generate(String xmlContent, OutputStream out) throws Exception {
      DataParser parser = new DataParser();

      parser.setOutputStream(out);
      parser.parse(new InputSource(new StringReader(xmlContent)));
   }

   public void initialize() throws InitializationException {
      System.setProperty("appRoot", ".");
   }

   String parseXml(String template, Map<String, Object> params) {
      String str = template;

      for (Map.Entry<String, Object> e : params.entrySet()) {
         String name = e.getKey();
         String value = e.getValue() == null ? "" : e.getValue().toString();

         str = str.replaceAll(Pattern.quote("{" + name + "}"), xmlEncode(value));
      }

      return str;
   }

   public void setXmlTemplate(String xmlTemplate) {
      m_xmlTemplate = xmlTemplate;
   }

   private String xmlEncode(String value) {
      StringBuilder sb = new StringBuilder();
      int len = value.length();

      for (int i = 0; i < len; i++) {
         char ch = value.charAt(i);

         if (ch < 32 && ch != '\t' && ch != '\r' && ch != '\n') {
            sb.append(" ");
         } else if (ch == '\\' || ch == '$') { // group reference
            sb.append('\\').append(ch);
         } else {
            sb.append(ch);
         }
      }

      return sb.toString();
   }
}
