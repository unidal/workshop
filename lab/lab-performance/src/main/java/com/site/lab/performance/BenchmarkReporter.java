package com.site.lab.performance;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.MessageFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import com.site.lab.performance.model.Benchmark;
import com.site.lab.performance.model.Case;
import com.site.lab.performance.model.Suite;

public class BenchmarkReporter {
   // 2.123: [GC 2485K->1566K(5056K), 0.0007150 secs]
   private MessageFormat GC = new MessageFormat("{0,number}: [GC {1,number}K->{2,number}K({3,number}K), {4,number} secs]");

   // 2.123: [GC 2485K->1566K(5056K), 0.0007150 secs]
   private MessageFormat FULL_GC = new MessageFormat("{0,number}: [Full GC {1,number}K->{2,number}K({3,number}K), {4,number} secs]");

   // 2.456: [GC [DefNew: 64575K->959K(64576K), 0.0457646 secs]
   // 196016K->133633K(261184K), 0.0459067 secs]]
   // 111.042: [GC 111.042: [DefNew: 8128K->8128K(8128K), 0.0000505
   // secs]111.042: [Tenured: 18154K->2311K(24576K), 0.1290354 secs]
   // 26282K->2311K(32704K), 0.1293306 secs]
   private MessageFormat WITH_DETAILS = new MessageFormat(
         "{0,number}: [{8} [{9}] {1,number}K->{2,number}K({3,number}K), {4,number} secs]");

   public void generateReport(Benchmark benchmark, File report) {
      try {
         File gclog = new File("gc.log");

         // GC file exists and it's new created in 2 seconds
         if (gclog.exists() && gclog.lastModified() + Timer.getCurrentTime() + 2000 >= System.currentTimeMillis()) {
            List<Entry> entries = parseGcLog(gclog);

            scatterGcLog(benchmark, entries);
         }
      } catch (FileNotFoundException e) {
         System.out.println("GC log file is not found: " + e);
      } catch (IOException e) {
         System.out.println("Error when reading GC log file: " + e);
      }

      try {
         BenchmarkHelper.marshal(benchmark, new FileWriter(report));
      } catch (Exception e) {
         throw new RuntimeException("Error when generating report. " + e, e);
      }
   }

   private void scatterGcLog(Benchmark benchmark, List<Entry> entries) {
      List<Case> cases = new ArrayList<Case>();

      for (Suite suite : benchmark.getSuiteList()) {
         for (Case e : suite.getCaseMap().values()) {
            cases.add(e);
         }
      }

      Collections.sort(cases, new Comparator<Case>() {
         public int compare(Case o1, Case o2) {
            return (int) (o1.getStartTime() - o2.getStartTime());
         }
      });

      Iterator<Case> ci = cases.iterator();
      Iterator<Entry> ei = entries.iterator();
      Case c = ci.hasNext() ? ci.next() : null;
      Entry e = ei.hasNext() ? ei.next() : null;

      while (c != null && e != null) {
         if (e.getStart() >= c.getStartTime() && e.getStart() + e.getDuration() <= c.getEndTime()) {
            c.setMemoryGcCount(c.getMemoryGcCount() + 1);
            c.setMemoryGcAmount(c.getMemoryGcAmount() + e.getAmount());
            c.setMemoryGcTime(c.getMemoryGcTime() + e.getDuration());
            e = ei.hasNext() ? ei.next() : null;
         } else {
            c = ci.hasNext() ? ci.next() : null;
         }
      }
   }

   public List<Entry> parseGcLog(File gcFile) throws IOException {
      BufferedReader reader = new BufferedReader(new FileReader(gcFile));
      List<Entry> entries = new ArrayList<Entry>();

      while (true) {
         String line = reader.readLine();

         if (line == null) {
            break;
         }

         if (line.length() == 0) {
            continue;
         }

         Object[] parts = null;
         String type = "";

         if (parts == null) {
            try {
               parts = GC.parse(line);
               type = "GC";
            } catch (ParseException e) {
               // ignore it
            }
         }

         if (parts == null) {
            try {
               parts = FULL_GC.parse(line);
               type = "Full GC";
            } catch (ParseException e) {
               // ignore it
            }
         }

         if (parts == null) {
            try {
               parts = WITH_DETAILS.parse(line);
               type = (String) parts[8];
            } catch (ParseException e) {
               // ignore it
            }
         }

         if (parts == null) {
            System.err.println("Unknown GC line: " + line);
         } else {
            int index = 0;
            Number start = (Number) parts[index++];
            Number before = (Number) parts[index++];
            Number after = (Number) parts[index++];
            Number total = (Number) parts[index++];
            Number duration = (Number) parts[index++];

            Entry entry = new Entry(type, start, duration, before, after, total);

            entries.add(entry);
         }
      }

      return entries;
   }

   public static final class Entry {
      private String m_type;

      private long m_start;

      private long m_amount;

      private long m_duration;

      private long m_total;

      public Entry(String type, Number start, Number duration, Number before, Number after, Number total) {
         m_type = type;
         m_start = (long) (start.doubleValue() * 1000);
         m_duration = (long) (duration.doubleValue() * 1000);
         m_amount = (long) (before.doubleValue() * 1000 - after.doubleValue() * 1000);
         m_total = (long) (total.doubleValue() * 1000);
      }

      public String getType() {
         return m_type;
      }

      public long getStart() {
         return m_start;
      }

      public long getAmount() {
         return m_amount;
      }

      public long getDuration() {
         return m_duration;
      }

      public long getTotal() {
         return m_total;
      }

      public String toString() {
         return "Entry[" + m_type + "," + m_start + "," + m_duration + "," + m_amount + "," + m_total + "]";
      }
   }
}
