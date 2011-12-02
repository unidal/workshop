package com.ebay.eunit.model;

import com.ebay.eunit.model.entity.EunitClass;
import com.ebay.eunit.model.entity.EunitException;
import com.ebay.eunit.model.entity.EunitField;
import com.ebay.eunit.model.entity.EunitMethod;
import com.ebay.eunit.model.entity.EunitParameter;

public interface IVisitor {

   public void visitEunitClass(EunitClass eunitClass);

   public void visitEunitException(EunitException eunitException);

   public void visitEunitField(EunitField eunitField);

   public void visitEunitMethod(EunitMethod eunitMethod);

   public void visitEunitParameter(EunitParameter eunitParameter);
}
