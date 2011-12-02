package com.ebay.eunit.benchmark.handler;

import java.lang.reflect.AnnotatedElement;

import com.ebay.eunit.benchmark.CpuMeta;
import com.ebay.eunit.benchmark.model.entity.CaseEntity;
import com.ebay.eunit.benchmark.model.entity.CpuEntity;
import com.ebay.eunit.benchmark.model.entity.SuiteEntity;
import com.ebay.eunit.model.entity.EunitClass;
import com.ebay.eunit.model.entity.EunitMethod;
import com.ebay.eunit.testfwk.spi.IAnnotationHandler;
import com.ebay.eunit.testfwk.spi.IClassContext;

public enum CpuHandler implements IAnnotationHandler<CpuMeta, AnnotatedElement> {
   INSTANCE;

   @Override
   public Class<CpuMeta> getTargetAnnotation() {
      return CpuMeta.class;
   }

   @Override
   public void handle(IClassContext ctx, CpuMeta meta, AnnotatedElement target) {
      Object source = ctx.forEunit().peek();
      CpuEntity cpu = new CpuEntity();

      cpu.setLoops(meta.loops());
      cpu.setWarmups(meta.warmups());

      if (source instanceof EunitMethod) {
         EunitMethod eunitMethod = (EunitMethod) source;
         CaseEntity c = ctx.forModel().peek();

         eunitMethod.setTest(true);
         c.setCpu(cpu);
      } else if (source instanceof EunitClass) {
         SuiteEntity s = ctx.forModel().peek();

         s.setCpu(cpu);
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
