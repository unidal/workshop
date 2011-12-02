package com.ebay.eunit.testfwk.spi;

import com.ebay.eunit.model.entity.EunitClass;
import com.ebay.eunit.model.entity.EunitMethod;
import com.ebay.eunit.model.entity.EunitParameter;
import com.ebay.eunit.testfwk.spi.task.ITask;
import com.ebay.eunit.testfwk.spi.task.ITaskType;

public interface ICaseContext {
   public Object findAttributeFor(EunitParameter eunitParameter);

   public Object getAttribute(Class<?> targetType, String id);

   public IClassContext getClassContext();

   public EunitClass getEunitClass();

   public EunitMethod getEunitMethod();

   public <M> M getModel();

   public <T extends ITaskType> ITask<T> getTask();

   public Object getTestInstance();

   public Object invokeWithInjection(EunitMethod eunitMethod) throws Throwable;

   public Object removeAttribute(Class<?> type, String id);

   public Object removeAttribute(Object attribute, String id);

   public void setAttribute(Class<?> type, Object attribute, String id);

   public void setAttribute(Object attribute, String id);

   public <T extends ITaskType> void setTask(ITask<T> task);
}
