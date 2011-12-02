package com.ebay.eunit.cmd.handler;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import com.ebay.eunit.cmd.annotation.HttpProtocol;
import com.ebay.eunit.mock.http.HttpServerMocks;
import com.ebay.eunit.mock.http.IHttpHandler;
import com.ebay.eunit.testfwk.spi.ICaseContext;
import com.ebay.eunit.testfwk.spi.IClassContext;
import com.ebay.eunit.testfwk.spi.IMetaAnnotationHandler;
import com.ebay.eunit.testfwk.spi.task.IValve;
import com.ebay.eunit.testfwk.spi.task.IValveChain;
import com.ebay.eunit.testfwk.spi.task.Priority;

public enum HttpProtocolHandler implements IMetaAnnotationHandler<HttpProtocol, Class<?>> {
   INSTANCE;

   @Override
   public Class<HttpProtocol> getTargetAnnotation() {
      return HttpProtocol.class;
   }

   @Override
   public void handle(IClassContext ctx, final Annotation annotation, final HttpProtocol meta, Class<?> target, boolean after) {
      if (!after) {
         ctx.getRegistry().registerClassValve(Priority.MIDDLE, new IValve<ICaseContext>() {
            @Override
            public void execute(ICaseContext ctx, IValveChain chain) throws Throwable {
               try {
                  IHttpHandler handler = newInstance(meta.value());

                  HttpServerMocks.forBinding().bind(handler);

                  try {
                     chain.executeNext(ctx);
                  } finally {
                     HttpServerMocks.forBinding().unbind(handler);
                  }
               } catch (Throwable e) {
                  throw new RuntimeException(String.format("Unable to create handler instance by class %s, " //
                        + "which should have a public constructor with zero arguments, " //
                        + "or it's an enum with only one instance!", meta.value().getName()), e);
               }

            }
         });
      }
   }

   private IHttpHandler newInstance(Class<? extends IHttpHandler> clazz) throws Throwable {
      if (clazz.isEnum()) {
         Object[] values;

         try {
            Method method = clazz.getMethod("values");

            if (!method.isAccessible()) {
               method.setAccessible(true);
            }

            values = (Object[]) method.invoke(null);
         } catch (InvocationTargetException e) {
            throw e.getCause();
         }

         if (values.length == 1) {
            Object instance = values[0];

            if (instance instanceof IHttpHandler) {
               return (IHttpHandler) instance;
            } else {
               throw new RuntimeException(String.format("Enum(%s) should implement %s!", clazz.getName(),
                     IHttpHandler.class.getName()));
            }
         } else {
            throw new RuntimeException(String.format("Enum(%s) can only have one enum field!", clazz.getName()));
         }
      } else {
         return clazz.newInstance();
      }
   }

   @Override
   public String toString() {
      return String.format("%s.%s", getClass().getSimpleName(), name());
   }
}
