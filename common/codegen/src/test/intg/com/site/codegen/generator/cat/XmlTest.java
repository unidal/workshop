package com.site.codegen.generator.cat;

import junit.framework.Assert;

import org.junit.Test;

import com.dianping.cat.consumer.problem.model.entity.ProblemReport;
import com.dianping.cat.consumer.problem.model.transform.DefaultXmlParser;
import com.site.helper.Files;

public class XmlTest {
   @Test
   public void testDefault() throws Exception {
      DefaultXmlParser parser = new DefaultXmlParser();
      String expected = Files.forIO().readFrom(getClass().getResourceAsStream("problem.xml"), "utf-8");
      ProblemReport report = parser.parse(expected);

      Assert.assertEquals("XML is not well parsed!", expected.replace("\r", ""), report.toString().replace("\r", ""));
   }
   
   @Test
   public void testCompact() throws Exception {
      DefaultXmlParser parser = new DefaultXmlParser();
      String expected = Files.forIO().readFrom(getClass().getResourceAsStream("problem-compact.xml"), "utf-8");
      ProblemReport report = parser.parse(expected);
      String xml = String.format("%1.0s", report);
      
      Assert.assertEquals("XML is not well parsed!", expected.replace("\r", ""), xml.replace("\r", ""));
   }
}
