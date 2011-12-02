package com.site.wdbc.query;

import static com.site.wdbc.WdbcSourceType.HTML;

import com.site.lookup.ComponentTestCase;
import com.site.wdbc.ResourceSource;
import com.site.wdbc.WdbcSource;

public class HtmlToXmlTest extends ComponentTestCase {
   public void testConvert() throws Exception {
      WdbcSource source = new ResourceSource(HTML, "/pages/details2.html");
      HtmlToXmlConverter converter = new HtmlToXmlConverter();

      String result = converter.convert(source.getReader());

      assertNotNull(result);
   }
}
