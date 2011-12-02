package com.ebay.eunit.benchmark.handler;

import java.lang.reflect.AnnotatedElement;

import com.ebay.eunit.benchmark.MemoryMeta;
import com.ebay.eunit.benchmark.model.entity.CaseEntity;
import com.ebay.eunit.benchmark.model.entity.MemoryEntity;
import com.ebay.eunit.benchmark.model.entity.SuiteEntity;
import com.ebay.eunit.model.entity.EunitClass;
import com.ebay.eunit.model.entity.EunitMethod;
import com.ebay.eunit.testfwk.spi.IAnnotationHandler;
import com.ebay.eunit.testfwk.spi.IClassContext;

public enum MemoryHandler implements IAnnotationHandler<MemoryMeta, AnnotatedElement> {
   INSTANCE;

   @Override
   public Class<MemoryMeta> getTargetAnnotation() {
      return MemoryMeta.class;
   }

   @Override
   public void handle(IClassContext ctx, MemoryMeta meta, AnnotatedElement target) {
      Object source = ctx.forEunit().peek();
      MemoryEntity memory = new MemoryEntity();

      memory.setLoops(meta.loops());
      memory.setWarmups(meta.warmups());

      if (source instanceof EunitMethod) {
         EunitMethod eunitMethod = (EunitMethod) source;
         CaseEntity c = ctx.forModel().peek();

         eunitMethod.setTest(true);
         c.setMemory(memory);
      } else if (source instanceof EunitClass) {
         SuiteEntity s = ctx.forModel().peek();

         s.setMemory(memory);
      }
   }

   @Override
   public boolean isAfter() {
      return false;
   }

   @Override
   public String toString() {
      return String.format("%s.%s", getClass().getSimpleName(), name());
   }
}
