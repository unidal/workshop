package com.ebay.eunit.model.transform;

import com.ebay.eunit.model.IVisitor;
import com.ebay.eunit.model.entity.EunitClass;
import com.ebay.eunit.model.entity.EunitException;
import com.ebay.eunit.model.entity.EunitField;
import com.ebay.eunit.model.entity.EunitMethod;
import com.ebay.eunit.model.entity.EunitParameter;

public abstract class BaseVisitor implements IVisitor {
   @Override
   public void visitEunitClass(EunitClass eunitClass) {
      for (EunitField eunitField : eunitClass.getFields()) {
         visitEunitField(eunitField);
      }

      for (EunitMethod eunitMethod : eunitClass.getMethods()) {
         visitEunitMethod(eunitMethod);
      }
   }

   @Override
   public void visitEunitException(EunitException eunitException) {
   }

   @Override
   public void visitEunitField(EunitField eunitField) {
   }

   @Override
   public void visitEunitMethod(EunitMethod eunitMethod) {
      for (EunitParameter eunitParameter : eunitMethod.getParameters()) {
         visitEunitParameter(eunitParameter);
      }

      for (EunitException eunitException : eunitMethod.getExpectedExceptions()) {
         visitEunitException(eunitException);
      }
   }

   @Override
   public void visitEunitParameter(EunitParameter eunitParameter) {
   }
}
