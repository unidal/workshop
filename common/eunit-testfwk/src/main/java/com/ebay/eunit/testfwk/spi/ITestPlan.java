package com.ebay.eunit.testfwk.spi;

import java.lang.reflect.Method;

import com.ebay.eunit.model.entity.EunitMethod;
import com.ebay.eunit.testfwk.spi.task.ITask;
import com.ebay.eunit.testfwk.spi.task.ITaskType;
import com.ebay.eunit.testfwk.spi.task.ValveMap;

public interface ITestPlan<T extends ITestCallback> {
   public ITestPlan<T> addAfter(ITask<? extends ITaskType> task);

   public ITestPlan<T> addAfterClass(ITask<? extends ITaskType> task);

   public ITestPlan<T> addBefore(ITask<? extends ITaskType> task);

   public ITestPlan<T> addBeforeClass(ITask<? extends ITaskType> task);

   public ITestPlan<T> addDeferredAction(IDeferredAction action);

   public ITestPlan<T> addTestCase(EunitMethod eunitMethod, ITestCase<T> testCase);

   public void bindTo(ITestClassRunner runner);

   public void executeDeferredActions();

   public ValveMap getCaseValveMap();

   public ValveMap getClassValveMap();

   public ITestCase<T> getTestCase(Method method);

   public static interface IDeferredAction {
      public void execute();
   }
}
