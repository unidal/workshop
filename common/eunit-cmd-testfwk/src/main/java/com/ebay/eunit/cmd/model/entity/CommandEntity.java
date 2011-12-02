package com.ebay.eunit.cmd.model.entity;

import static com.ebay.eunit.cmd.model.Constants.ATTR_METHOD_NAME;
import static com.ebay.eunit.cmd.model.Constants.ENTITY_COMMAND;

import com.ebay.eunit.cmd.model.BaseEntity;
import com.ebay.eunit.cmd.model.IVisitor;

public class CommandEntity extends BaseEntity<CommandEntity> {
   private String m_methodName;

   private java.lang.reflect.Method m_method;

   private RequestEntity m_withRequest;

   private ResponseEntity m_expectedResponse;

   public CommandEntity(String methodName) {
      m_methodName = methodName;
   }

   @Override
   public void accept(IVisitor visitor) {
      visitor.visitCommand(this);
   }

   public java.lang.reflect.Method getMethod() {
      return m_method;
   }

   public String getMethodName() {
      return m_methodName;
   }

   public RequestEntity getWithRequest() {
      return m_withRequest;
   }

   public ResponseEntity getExpectedResponse() {
      return m_expectedResponse;
   }

   @Override
   public void mergeAttributes(CommandEntity other) {
      assertAttributeEquals(other, ENTITY_COMMAND, ATTR_METHOD_NAME, m_methodName, other.getMethodName());

      if (other.getMethod() != null) {
         m_method = other.getMethod();
      }
   }

   public CommandEntity setMethod(java.lang.reflect.Method method) {
      m_method=method;
      return this;
   }

   public CommandEntity setWithRequest(RequestEntity withRequest) {
      m_withRequest=withRequest;
      return this;
   }

   public CommandEntity setExpectedResponse(ResponseEntity expectedResponse) {
      m_expectedResponse=expectedResponse;
      return this;
   }

}
