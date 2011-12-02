package com.ebay.eunit.testfwk.junit;

import com.ebay.eunit.model.entity.EunitClass;
import com.ebay.eunit.model.entity.EunitMethod;
import com.ebay.eunit.model.transform.BaseVisitor;
import com.ebay.eunit.testfwk.EunitTaskType;
import com.ebay.eunit.testfwk.spi.IClassContext;
import com.ebay.eunit.testfwk.spi.ITestCase;
import com.ebay.eunit.testfwk.spi.ITestCaseBuilder;
import com.ebay.eunit.testfwk.spi.ITestPlan;
import com.ebay.eunit.testfwk.spi.ITestPlanBuilder;
import com.ebay.eunit.testfwk.spi.Registry;
import com.ebay.eunit.testfwk.spi.task.Task;

public class EunitJUnitTestPlanBuilder extends BaseVisitor implements ITestPlanBuilder<JUnitCallback> {
   private IClassContext m_ctx;

   private ITestPlan<JUnitCallback> m_plan;

   @Override
   @SuppressWarnings("unchecked")
   public void build(IClassContext ctx) {
      m_plan = (ITestPlan<JUnitCallback>) ctx.getTestPlan();
      m_ctx = ctx;

      ctx.forEunit().getEunitClass().accept(this);
      m_plan.executeDeferredActions();
   }

   @Override
   public void visitEunitClass(EunitClass eunitClass) {
      m_plan.addBeforeClass(new Task<EunitTaskType>(EunitTaskType.BEFORE_CLASS, null));

      super.visitEunitClass(eunitClass);

      m_plan.addAfterClass(new Task<EunitTaskType>(EunitTaskType.AFTER_CLASS, null));
   }

   @Override
   public void visitEunitMethod(EunitMethod eunitMethod) {
      if (eunitMethod.getBeforeAfter() != null) {
         if (!eunitMethod.isStatic()) {
            if (eunitMethod.isBeforeAfter()) { // @Before
               m_plan.addBefore(new Task<EunitTaskType>(EunitTaskType.METHOD, eunitMethod));
            } else { // @After
               m_plan.addAfter(new Task<EunitTaskType>(EunitTaskType.METHOD, eunitMethod));
            }
         } else {
            if (eunitMethod.isBeforeAfter()) { // @BeforeClass
               m_plan.addBeforeClass(new Task<EunitTaskType>(EunitTaskType.METHOD, eunitMethod));
            } else { // @AfterClass
               m_plan.addAfterClass(new Task<EunitTaskType>(EunitTaskType.METHOD, eunitMethod));
            }
         }
      } else if (eunitMethod.isTest()) { // @Test
         super.visitEunitMethod(eunitMethod);

         Registry registry = m_ctx.getRegistry();
         ITestCase<JUnitCallback> testCase = null;

         if (!eunitMethod.isIgnored()) {
            @SuppressWarnings("unchecked")
            ITestCaseBuilder<JUnitCallback> builder = (ITestCaseBuilder<JUnitCallback>) registry.getTestCaseBuilder();

            testCase = builder.build(m_ctx, eunitMethod);
         }

         m_plan.addTestCase(eunitMethod, testCase);
      }
   }
}
