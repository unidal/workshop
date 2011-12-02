package com.site.lab.performance;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.TreeMap;
import java.util.Vector;

import org.junit.runner.RunWith;

import com.site.lab.performance.cpu.CpuMeta;
import com.site.lab.performance.junit.PerformanceReportMeta;
import com.site.lab.performance.junit.PerformanceRunner;
import com.site.lab.performance.memory.MemoryMeta;

@RunWith(PerformanceRunner.class)
@PerformanceReportMeta("target/java.util.xml")
public class JavaUtil {
   private void addItems(final List<String> list, final int count) {
      final String item = "";

      for (int i = 0; i < count; i++) {
         list.add(item);
      }
   }

   private void addItems(Map<String, String> map, int count) {
      final String item = "";

      for (int i = 0; i < count; i++) {
         map.put(String.valueOf(i), item);
      }
   }

   @CpuMeta(loops = 100000)
   @MemoryMeta(loops = 10000)
   public List<String> newArrayList() {
      return new ArrayList<String>();
   }

   @CpuMeta(loops = 100000)
   @MemoryMeta(loops = 10000)
   public List<String> newArrayList01() {
      final List<String> list = new ArrayList<String>(1);

      addItems(list, 1);
      return list;
   }

   @CpuMeta(loops = 100000)
   @MemoryMeta(loops = 10000)
   public List<String> newArrayList02() {
      final List<String> list = new ArrayList<String>(2);

      addItems(list, 2);
      return list;
   }

   @CpuMeta(loops = 100000)
   @MemoryMeta(loops = 10000)
   public List<String> newArrayList30() {
      final List<String> list = new ArrayList<String>(100);

      addItems(list, 30);
      return list;
   }

   @CpuMeta(loops = 100000)
   @MemoryMeta(loops = 100000)
   public Calendar newCalendar() {
      return Calendar.getInstance();
   }

   @CpuMeta(loops = 100000)
   @MemoryMeta(loops = 10000)
   public Date newDate() {
      return new Date();
   }

   @CpuMeta(loops = 100000)
   @MemoryMeta(loops = 10000)
   public Map<String, String> newHashMap() {
      final Map<String, String> map = new HashMap<String, String>();

      addItems(map, 12);
      return map;
   }

   @CpuMeta(loops = 100000)
   @MemoryMeta(loops = 10000)
   public Map<String, String> newHashtable() {
      final Map<String, String> map = new Hashtable<String, String>();

      addItems(map, 12);
      return map;
   }

   @CpuMeta(loops = 100000)
   @MemoryMeta(loops = 10000)
   public Map<String, String> newLinkedHashMap() {
      final Map<String, String> map = new LinkedHashMap<String, String>();

      addItems(map, 12);
      return map;
   }

   @CpuMeta(loops = 100000)
   @MemoryMeta(loops = 10000)
   public List<String> newLinkedList() {
      return new LinkedList<String>();
   }

   @CpuMeta(loops = 100000)
   @MemoryMeta(loops = 10000)
   public List<String> newLinkedList01() {
      final List<String> list = new LinkedList<String>();

      addItems(list, 1);
      return list;
   }

   @CpuMeta(loops = 100000)
   @MemoryMeta(loops = 10000)
   public List<String> newLinkedList02() {
      final List<String> list = new LinkedList<String>();

      addItems(list, 2);
      return list;
   }

   @CpuMeta(loops = 100000)
   @MemoryMeta(loops = 10000)
   public List<String> newLinkedList30() {
      final List<String> list = new LinkedList<String>();

      addItems(list, 30);
      return list;
   }

   @CpuMeta(loops = 100000)
   @MemoryMeta(loops = 10000)
   public List<String> newStack() {
      return new Stack<String>();
   }

   @CpuMeta(loops = 100000)
   @MemoryMeta(loops = 10000)
   public Map<String, String> newTreeMap() {
      final Map<String, String> map = new TreeMap<String, String>();

      addItems(map, 12);
      return map;
   }

   @CpuMeta(loops = 100000)
   @MemoryMeta(loops = 10000)
   public List<String> newVector() {
      return new Vector<String>();
   }
}
