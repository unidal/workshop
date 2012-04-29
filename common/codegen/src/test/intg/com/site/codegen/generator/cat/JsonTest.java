package com.site.codegen.generator.cat;

import org.junit.Assert;
import org.junit.Test;

import com.dianping.cat.consumer.problem.model.entity.ProblemReport;
import com.dianping.cat.consumer.problem.model.transform.DefaultXmlParser;
import com.site.helper.Files;

public class JsonTest {
   @Test
   public void testDefault() throws Exception {
      DefaultXmlParser parser = new DefaultXmlParser();
      String xml = Files.forIO().readFrom(getClass().getResourceAsStream("problem.xml"), "utf-8");
      ProblemReport report = parser.parse(xml);
      String json = String.format("%2.1s", report);
      String expected = Files.forIO().readFrom(getClass().getResourceAsStream("problem.json"), "utf-8");

      Assert.assertEquals("JSON is not well built!", expected.replace("\r", ""), json.replace("\r", ""));
   }

   @Test
   public void testCompact() throws Exception {
      DefaultXmlParser parser = new DefaultXmlParser();
      String xml = Files.forIO().readFrom(getClass().getResourceAsStream("problem.xml"), "utf-8");
      ProblemReport report = parser.parse(xml);
      String json = String.format("%2.0s", report);
      String expected = Files.forIO().readFrom(getClass().getResourceAsStream("problem-compact.json"), "utf-8");

      Assert.assertEquals("Compact JSON is not well built!", expected.replace("\r", ""), json.replace("\r", ""));
   }
}
