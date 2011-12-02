package org.unidal.xml;

import java.io.File;
import java.io.FileOutputStream;

import org.junit.Test;
import org.unidal.xml.XmlCodeGenerator;

import com.site.lookup.ComponentTestCase;

public class XmlCodeGeneratorTest extends ComponentTestCase {
   @Test
   public void testGenerate() throws Exception {
      XmlCodeGenerator generator = lookup(XmlCodeGenerator.class);
      File file = new File("target/generated-bin/html.zip");

      file.getParentFile().mkdirs();

      FileOutputStream fos = new FileOutputStream(file);
      String sample = "<html><head><title>this is title</title></head><body><h1>this is body</h1></body></html>";

      generator.generateXml(fos, sample, new String[] { "" },
            new String[] { "com.site.test" });
      fos.close();
      assertEquals(1246, file.length());
   }
}
