package com.site.codegen.meta;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;

import org.junit.Test;

import com.site.lookup.ComponentTestCase;

public class XmlMetaHelperTest extends ComponentTestCase {
   @Test
   public void testResource() throws Exception {
      XmlMetaHelper helper = lookup(XmlMetaHelper.class);
      Reader reader = new FileReader(getResource("sanguo.xml"));
      String content = helper.getXmlMetaContent(reader);

      assertTrue(content.contains("game"));
   }

   @Test
   public void notestSinaRss() throws Exception {
      XmlMetaHelper helper = lookup(XmlMetaHelper.class);
      URL url = new URL("http://rss.sina.com.cn/news/marquee/ddt.xml");

      try {
         String content = helper.getXmlMetaContent(new InputStreamReader(url.openStream(), "utf-8"));

         assertTrue(content.contains("rss"));
      } catch (FileNotFoundException e) {
         // ignore it
      }
   }
}
