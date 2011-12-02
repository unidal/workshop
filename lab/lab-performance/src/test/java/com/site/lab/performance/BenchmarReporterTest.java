package com.site.lab.performance;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.site.lab.performance.BenchmarkReporter.Entry;

public class BenchmarReporterTest {
   @Test
   public void testParse() throws IOException {
      BenchmarkReporter reporter = new BenchmarkReporter();
      List<Entry> entries = reporter.parseGcLog(new File(getClass().getResource("gc.log").getFile()));

      Assert.assertEquals(840, entries.size());
   }
}
