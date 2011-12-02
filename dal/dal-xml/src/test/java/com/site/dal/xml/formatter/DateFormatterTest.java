package com.site.dal.xml.formatter;

import com.site.dal.xml.XmlException;
import com.site.lookup.ComponentTestCase;

public class DateFormatterTest extends ComponentTestCase {
   public void testParseAndFormat() throws XmlException {
      DateFormatter formatter = new DateFormatter();

      assertEquals("2008-09-13 13:06:02", formatter.format("yyyy-MM-dd HH:mm:ss", formatter.parse(
               "yyyy-MM-dd HH:mm:ss", "2008-09-13 13:06:02")));

      assertEquals("2008-9-13", formatter.format("yyyy-M-d", formatter.parse("yyyy-MM-dd", "2008-09-13")));
   }
}
