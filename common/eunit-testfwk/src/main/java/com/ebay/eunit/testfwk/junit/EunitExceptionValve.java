package com.ebay.eunit.testfwk.junit;

import java.text.MessageFormat;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import junit.framework.Assert;
import junit.framework.AssertionFailedError;

import com.ebay.eunit.model.entity.EunitException;
import com.ebay.eunit.model.entity.EunitMethod;
import com.ebay.eunit.testfwk.spi.ICaseContext;
import com.ebay.eunit.testfwk.spi.task.IValve;
import com.ebay.eunit.testfwk.spi.task.IValveChain;

public enum EunitExceptionValve implements IValve<ICaseContext> {
   INSTANCE;

   private void assertException(EunitException expectedException, Throwable actual) {
      String expectedMessage = expectedException.getMessage();
      String expectedPattern = expectedException.getPattern();
      String message = actual.getMessage();

      if (expectedMessage.length() > 0) {
         Assert.assertEquals(String.format("Exception message is not matched. Matched exception type %s for %s.",
               expectedException.getType().getName(), actual.getClass().getName()), expectedMessage, message);
      }

      if (expectedPattern.length() > 0) {
         MessageFormat format = new MessageFormat(expectedPattern);

         try {
            format.parse(message);
         } catch (Exception e) {
            Assert.assertEquals(
                  String.format("Exception message(%s) does not match the expected pattern(%s)!", message, expectedPattern),
                  expectedPattern, message);
         }
      }
   }

   private void buildDistanceMap(Map<Class<?>, Integer> map, Class<?> clazz, int distance) {
      map.put(clazz, distance);

      Class<?> superClass = clazz.getSuperclass();

      if (superClass != Object.class) {
         buildDistanceMap(map, superClass, distance + 1);
      }
   }

   private String buildMessage(List<EunitException> expectedExceptions, Throwable actual) {
      StringBuilder sb = new StringBuilder(1024);
      boolean first = true;

      sb.append("Expected one of following exceptions: ");

      for (EunitException expectedException : expectedExceptions) {
         if (first) {
            first = false;
         } else {
            sb.append(',');
         }

         sb.append(expectedException.getType().getName());
      }

      sb.append('.');

      if (actual != null) {
         sb.append(" But was: ");
         sb.append(actual.getClass().getName());
      }

      return sb.toString();
   }

   @Override
   public void execute(ICaseContext ctx, IValveChain chain) throws Throwable {
      EunitMethod eunitMethod = ctx.getEunitMethod();
      List<EunitException> expectedExceptions = eunitMethod.getExpectedExceptions();

      if (expectedExceptions.isEmpty()) {
         chain.executeNext(ctx);
      } else {
         try {
            chain.executeNext(ctx);

            throw new AssertionFailedError(buildMessage(expectedExceptions, null));
         } catch (Throwable e) {
            EunitException eunitException = findBestMatchedException(expectedExceptions, e);

            if (eunitException != null) {
               assertException(eunitException, e);
            } else {
               throw new AssertionFailedError(buildMessage(expectedExceptions, e));
            }
         }
      }
   }

   private EunitException findBestMatchedException(List<EunitException> expectedExceptions, Throwable e) {
      EunitException eunitException = null;
      int distance = Integer.MAX_VALUE;

      Map<Class<?>, Integer> map = new LinkedHashMap<Class<?>, Integer>();

      buildDistanceMap(map, e.getClass(), 0);

      for (EunitException expectedException : expectedExceptions) {
         Class<?> type = expectedException.getType();

         if (type.isAssignableFrom(e.getClass())) {
            Integer d = map.get(type);

            if (d != null && d.intValue() < distance) {
               eunitException = expectedException;
               distance = d;
            }
         }
      }

      return eunitException;
   }
}
