package com.site.codegen.generator.cat;

import org.junit.Assert;
import org.junit.Test;

import com.dianping.cat.consumer.failure.model.entity.FailureReport;
import com.dianping.cat.consumer.failure.model.transform.DefaultParser;
import com.site.helper.Files;

public class JsonTest {
   @Test
   public void testDefault() throws Exception {
      DefaultParser parser = new DefaultParser();
      String xml = Files.forIO().readFrom(getClass().getResourceAsStream("failure.xml"), "utf-8");
      FailureReport report = parser.parse(xml);
      String json = String.format("%2.1s", report);
      String expected = Files.forIO().readFrom(getClass().getResourceAsStream("failure.json"), "utf-8");

      Assert.assertEquals("JSON is not well built!", expected.replace("\r", ""), json.replace("\r", ""));
   }

   @Test
   public void testCompact() throws Exception {
      DefaultParser parser = new DefaultParser();
      String xml = Files.forIO().readFrom(getClass().getResourceAsStream("failure.xml"), "utf-8");
      FailureReport report = parser.parse(xml);
      String json = String.format("%2.0s", report);
      String expected = Files.forIO().readFrom(getClass().getResourceAsStream("failure-compact.json"), "utf-8");

      Assert.assertEquals("Compact JSON is not well built!", expected.replace("\r", ""), json.replace("\r", ""));
   }
}
