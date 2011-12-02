package com.site.codegen.transformer;

import java.io.FileNotFoundException;
import java.io.InputStream;

import org.codehaus.plexus.util.IOUtil;
import org.junit.Test;

import com.site.lookup.ComponentTestCase;

public class XslNormalizeTest extends ComponentTestCase {
   private static final String JDBC_NORMALIZE_XSL = "/META-INF/dal/jdbc/normalize.xsl";

   private static final String XML_NORMALIZE_XSL = "/META-INF/dal/xml/normalize.xsl";

   @Test
   public void testJdbcBasic() throws Exception {
      testJdbcNormalize("basic");
   }

   @Test
   public void testJdbcEntity() throws Exception {
      testJdbcNormalize("entity");
   }

   @Test
   public void testJdbcFlag() throws Exception {
      testJdbcNormalize("flag");
   }

   @Test
   public void testJdbcMember() throws Exception {
      testJdbcNormalize("member");
   }

   private void testJdbcNormalize(String caseName) throws Exception {
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
      String result = transformer.transform(getClass().getResource(JDBC_NORMALIZE_XSL), source);

      assertEquals("[" + caseName + "] Content does not match.", expected, result);
   }

   @Test
   public void testJdbcRelation() throws Exception {
      testJdbcNormalize("relation");
   }

   @Test
   public void testJdbcValueType() throws Exception {
      testJdbcNormalize("valuetype");
   }

   @Test
   public void testXmlElement() throws Exception {
      testXmlNormalize("element");
   }

   private void testXmlNormalize(String caseName) throws Exception {
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
      String result = transformer.transform(getClass().getResource(XML_NORMALIZE_XSL), source);

      assertEquals("[" + caseName + "] Content does not match.", expected, result);
   }
}
