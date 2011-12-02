package com.site.wdbc.linkedin;

import java.io.FileWriter;
import java.io.Writer;

import com.site.lookup.ComponentTestCase;

public class DumpTest extends ComponentTestCase {
   public void testDump() throws Exception {
      DatabaseAccess da = lookup(DatabaseAccess.class);
      Writer writer = new FileWriter("dump.txt");

      da.dumpToCsv(writer);
      writer.close();
   }
}
