package com.ebay.eunit.cmd.model.entity;

import java.util.LinkedHashMap;
import java.util.Map;

import com.ebay.eunit.cmd.model.BaseEntity;
import com.ebay.eunit.cmd.model.IVisitor;

public class RequestEntity extends BaseEntity<RequestEntity> {
   private String m_requestMethod;

   private String m_requestUrl;

   private String m_formData;

   private Map<String, ParameterEntity> m_parameters = new LinkedHashMap<String, ParameterEntity>();

   private Map<String, HeaderEntity> m_headers = new LinkedHashMap<String, HeaderEntity>();

   public RequestEntity() {
   }

   @Override
   public void accept(IVisitor visitor) {
      visitor.visitRequest(this);
   }

   public RequestEntity addHeader(HeaderEntity header) {
      m_headers.put(header.getName(), header);
      return this;
   }

   public RequestEntity addParameter(ParameterEntity parameter) {
      m_parameters.put(parameter.getName(), parameter);
      return this;
   }

   public HeaderEntity findHeader(String name) {
      return m_headers.get(name);
   }

   public ParameterEntity findParameter(String name) {
      return m_parameters.get(name);
   }

   public String getFormData() {
      return m_formData;
   }

   public Map<String, HeaderEntity> getHeaders() {
      return m_headers;
   }

   public Map<String, ParameterEntity> getParameters() {
      return m_parameters;
   }

   public String getRequestMethod() {
      return m_requestMethod;
   }

   public String getRequestUrl() {
      return m_requestUrl;
   }

   @Override
   public void mergeAttributes(RequestEntity other) {
      if (other.getRequestMethod() != null) {
         m_requestMethod = other.getRequestMethod();
      }

      if (other.getRequestUrl() != null) {
         m_requestUrl = other.getRequestUrl();
      }

      if (other.getFormData() != null) {
         m_formData = other.getFormData();
      }
   }

   public boolean removeHeader(String name) {
      if (m_headers.containsKey(name)) {
         m_headers.remove(name);
      }

      return false;
   }

   public boolean removeParameter(String name) {
      if (m_parameters.containsKey(name)) {
         m_parameters.remove(name);
      }

      return false;
   }

   public RequestEntity setFormData(String formData) {
      m_formData=formData;
      return this;
   }

   public RequestEntity setRequestMethod(String requestMethod) {
      m_requestMethod=requestMethod;
      return this;
   }

   public RequestEntity setRequestUrl(String requestUrl) {
      m_requestUrl=requestUrl;
      return this;
   }

}
