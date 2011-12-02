package com.site.codegen.transformer;

import java.io.FileNotFoundException;
import java.io.InputStream;

import org.codehaus.plexus.util.IOUtil;
import org.junit.Test;

import com.site.codegen.transformer.XslTransformer;
import com.site.lookup.ComponentTestCase;

public class XslDecorateTest extends ComponentTestCase {
   private static final String JDBC_NORMALIZE_XSL = "/META-INF/dal/jdbc/decorate.xsl";

   private static final String XML_NORMALIZE_XSL = "/META-INF/dal/xml/decorate.xsl";

   @Test
   public void testJavaKeywords() throws Exception {
      testDecorate("keywords");
   }

   private void testDecorate(String caseName) throws Exception {
      XslTransformer transformer = lookup(XslTransformer.class);
      InputStream in = getClass().getResourceAsStream(caseName + "_in.xml");
      InputStream out = getClass().getResourceAsStream(caseName + "_out.xml");

      if (in == null) {
         throw new FileNotFoundException("Resource not found: " + caseName + "_in.xml");
      } else if (out == null) {
         throw new FileNotFoundException("Resource not found: " + caseName + "_out.xml");
      }

      String source = IOUtil.toString(in);
      String expected = IOUtil.toString(out);
      String jdbcResult = transformer.transform(getClass().getResource(JDBC_NORMALIZE_XSL), source);
      String xmlResult = transformer.transform(getClass().getResource(XML_NORMALIZE_XSL), source);

      assertEquals("[" + caseName + "] [JDBC] Content does not match.", expected, jdbcResult);
      assertEquals("[" + caseName + "] [XML] Content does not match.", expected, xmlResult);
   }
}
