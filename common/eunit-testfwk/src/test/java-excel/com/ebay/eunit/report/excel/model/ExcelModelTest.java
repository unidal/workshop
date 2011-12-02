package com.ebay.eunit.report.excel.model;

import java.io.IOException;

import junit.framework.Assert;

import org.junit.Test;
import org.xml.sax.SAXException;

import com.ebay.eunit.helper.Files;
import com.ebay.eunit.report.excel.model.entity.WorkbookEntity;
import com.ebay.eunit.report.excel.model.transform.DefaultParser;
import com.ebay.eunit.report.excel.model.transform.ExcelBuilder;

public class ExcelModelTest {
   @Test
   public void testParser() throws IOException, SAXException {
      DefaultParser parser = new DefaultParser();
      String xml = Files.forIO().readFrom(getClass().getResourceAsStream("instruments.xml"), "utf-8");
      WorkbookEntity workbook = parser.parse(xml);

      Assert.assertEquals(xml, workbook.toString());
   }

   @Test
   public void testModelToExcel() throws IOException, SAXException {
      DefaultParser parser = new DefaultParser();
      String xml = Files.forIO().readFrom(getClass().getResourceAsStream("instruments.xml"), "utf-8");
      WorkbookEntity workbook = parser.parse(xml);
      ExcelBuilder builder = new ExcelBuilder();

      workbook.accept(builder);
   }
}
