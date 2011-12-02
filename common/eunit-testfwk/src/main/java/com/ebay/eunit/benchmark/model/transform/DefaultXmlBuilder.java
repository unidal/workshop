package com.ebay.eunit.benchmark.model.transform;

import static com.ebay.eunit.benchmark.model.Constants.ATTR_CPU_FIRST_TIME;
import static com.ebay.eunit.benchmark.model.Constants.ATTR_CPU_TIME;
import static com.ebay.eunit.benchmark.model.Constants.ATTR_CPU_TOTAL_TIME;
import static com.ebay.eunit.benchmark.model.Constants.ATTR_ELAPSED_FIRST_TIME;
import static com.ebay.eunit.benchmark.model.Constants.ATTR_ELAPSED_TIME;
import static com.ebay.eunit.benchmark.model.Constants.ATTR_ELAPSED_TOTAL_TIME;
import static com.ebay.eunit.benchmark.model.Constants.ATTR_FOOTPRINT;
import static com.ebay.eunit.benchmark.model.Constants.ATTR_GC_AMOUNT;
import static com.ebay.eunit.benchmark.model.Constants.ATTR_GC_COUNT;
import static com.ebay.eunit.benchmark.model.Constants.ATTR_GC_TIME;
import static com.ebay.eunit.benchmark.model.Constants.ATTR_LOOPS;
import static com.ebay.eunit.benchmark.model.Constants.ATTR_NAME;
import static com.ebay.eunit.benchmark.model.Constants.ATTR_PERMANENT_FOOTPRINT;
import static com.ebay.eunit.benchmark.model.Constants.ATTR_TOTAL_FOOTPRINT;
import static com.ebay.eunit.benchmark.model.Constants.ATTR_TYPE;
import static com.ebay.eunit.benchmark.model.Constants.ATTR_WARMUPS;
import static com.ebay.eunit.benchmark.model.Constants.ELEMENT_END_AT;
import static com.ebay.eunit.benchmark.model.Constants.ELEMENT_START_AT;
import static com.ebay.eunit.benchmark.model.Constants.ENTITY_BENCHMARK;
import static com.ebay.eunit.benchmark.model.Constants.ENTITY_CASE;
import static com.ebay.eunit.benchmark.model.Constants.ENTITY_CPU;
import static com.ebay.eunit.benchmark.model.Constants.ENTITY_MEMORY;
import static com.ebay.eunit.benchmark.model.Constants.ENTITY_SUITE;

import com.ebay.eunit.benchmark.model.IVisitor;
import com.ebay.eunit.benchmark.model.entity.BenchmarkEntity;
import com.ebay.eunit.benchmark.model.entity.CaseEntity;
import com.ebay.eunit.benchmark.model.entity.CpuEntity;
import com.ebay.eunit.benchmark.model.entity.MemoryEntity;
import com.ebay.eunit.benchmark.model.entity.SuiteEntity;

public class DefaultXmlBuilder implements IVisitor {

   private int m_level;

   private StringBuilder m_sb = new StringBuilder(2048);

   private void endTag(String name) {
      m_level--;

      indent();
      m_sb.append("</").append(name).append(">\r\n");
   }

   public String getString() {
      return m_sb.toString();
   }

   private void indent() {
      for (int i = m_level - 1; i >= 0; i--) {
         m_sb.append("   ");
      }
   }

   private void startTag(String name, boolean closed, Object... nameValues) {
      startTag(name, null, closed, nameValues);
   }

   private void startTag(String name, Object... nameValues) {
      startTag(name, false, nameValues);
   }
   
   private void startTag(String name, Object text, boolean closed, Object... nameValues) {
      indent();

      m_sb.append('<').append(name);

      int len = nameValues.length;

      for (int i = 0; i + 1 < len; i += 2) {
         Object attrName = nameValues[i];
         Object attrValue = nameValues[i + 1];

         if (attrValue != null) {
            m_sb.append(' ').append(attrName).append("=\"").append(attrValue).append('"');
         }
      }

      if (text != null && closed) {
         m_sb.append('>');
         m_sb.append(text == null ? "" : text);
         m_sb.append("</").append(name).append(">\r\n");
      } else {
         if (closed) {
            m_sb.append('/');
         } else {
            m_level++;
         }
   
         m_sb.append(">\r\n");
      }
   }

   private void tagWithText(String name, String text, Object... nameValues) {
      if (text == null) {
         return;
      }
      
      indent();

      m_sb.append('<').append(name);

      int len = nameValues.length;

      for (int i = 0; i + 1 < len; i += 2) {
         Object attrName = nameValues[i];
         Object attrValue = nameValues[i + 1];

         if (attrValue != null) {
            m_sb.append(' ').append(attrName).append("=\"").append(attrValue).append('"');
         }
      }

      m_sb.append(">");
      m_sb.append(text);
      m_sb.append("</").append(name).append(">\r\n");
   }

   @Override
   public void visitBenchmark(BenchmarkEntity benchmark) {
      startTag(ENTITY_BENCHMARK);

      if (!benchmark.getSuites().isEmpty()) {
         for (SuiteEntity suite : benchmark.getSuites()) {
            visitSuite(suite);
         }
      }

      endTag(ENTITY_BENCHMARK);
   }

   @Override
   public void visitCase(CaseEntity _case) {
      startTag(ENTITY_CASE, ATTR_NAME, _case.getName());

      tagWithText(ELEMENT_START_AT, _case.getStartAt());

      tagWithText(ELEMENT_END_AT, _case.getEndAt());

      if (_case.getCpu() != null) {
         visitCpu(_case.getCpu());
      }

      if (_case.getMemory() != null) {
         visitMemory(_case.getMemory());
      }

      endTag(ENTITY_CASE);
   }

   @Override
   public void visitCpu(CpuEntity cpu) {
      startTag(ENTITY_CPU, true, ATTR_LOOPS, cpu.getLoops(), ATTR_WARMUPS, cpu.getWarmups(), ATTR_CPU_TIME, cpu.getCpuTime(), ATTR_CPU_FIRST_TIME, cpu.getCpuFirstTime(), ATTR_CPU_TOTAL_TIME, cpu.getCpuTotalTime(), ATTR_ELAPSED_TIME, cpu.getElapsedTime(), ATTR_ELAPSED_FIRST_TIME, cpu.getElapsedFirstTime(), ATTR_ELAPSED_TOTAL_TIME, cpu.getElapsedTotalTime());
   }

   @Override
   public void visitMemory(MemoryEntity memory) {
      startTag(ENTITY_MEMORY, true, ATTR_LOOPS, memory.getLoops(), ATTR_WARMUPS, memory.getWarmups(), ATTR_FOOTPRINT, memory.getFootprint(), ATTR_PERMANENT_FOOTPRINT, memory.getPermanentFootprint(), ATTR_TOTAL_FOOTPRINT, memory.getTotalFootprint(), ATTR_GC_COUNT, memory.getGcCount(), ATTR_GC_AMOUNT, memory.getGcAmount(), ATTR_GC_TIME, memory.getGcTime());
   }

   @Override
   public void visitSuite(SuiteEntity suite) {
      startTag(ENTITY_SUITE, ATTR_TYPE, suite.getType() == null ? null : suite.getType().getName());

      if (!suite.getCases().isEmpty()) {
         for (CaseEntity _case : suite.getCases()) {
            visitCase(_case);
         }
      }

      if (suite.getCpu() != null) {
         visitCpu(suite.getCpu());
      }

      if (suite.getMemory() != null) {
         visitMemory(suite.getMemory());
      }

      endTag(ENTITY_SUITE);
   }
}
