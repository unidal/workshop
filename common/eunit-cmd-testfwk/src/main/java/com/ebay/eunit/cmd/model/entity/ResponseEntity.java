package com.ebay.eunit.cmd.model.entity;

import java.util.LinkedHashMap;
import java.util.Map;

import com.ebay.eunit.cmd.model.BaseEntity;
import com.ebay.eunit.cmd.model.IVisitor;

public class ResponseEntity extends BaseEntity<ResponseEntity> {
   private Integer m_code;

   private Integer m_contentLength;

   private String m_contentType;

   private String m_body;

   private Map<String, HeaderEntity> m_headers = new LinkedHashMap<String, HeaderEntity>();

   public ResponseEntity() {
   }

   @Override
   public void accept(IVisitor visitor) {
      visitor.visitResponse(this);
   }

   public ResponseEntity addHeader(HeaderEntity header) {
      m_headers.put(header.getName(), header);
      return this;
   }

   public HeaderEntity findHeader(String name) {
      return m_headers.get(name);
   }

   public String getBody() {
      return m_body;
   }

   public Integer getCode() {
      return m_code;
   }

   public Integer getContentLength() {
      return m_contentLength;
   }

   public String getContentType() {
      return m_contentType;
   }

   public Map<String, HeaderEntity> getHeaders() {
      return m_headers;
   }

   @Override
   public void mergeAttributes(ResponseEntity other) {
      if (other.getCode() != null) {
         m_code = other.getCode();
      }

      if (other.getContentLength() != null) {
         m_contentLength = other.getContentLength();
      }

      if (other.getContentType() != null) {
         m_contentType = other.getContentType();
      }

      if (other.getBody() != null) {
         m_body = other.getBody();
      }
   }

   public boolean removeHeader(String name) {
      if (m_headers.containsKey(name)) {
         m_headers.remove(name);
      }

      return false;
   }

   public ResponseEntity setBody(String body) {
      m_body=body;
      return this;
   }

   public ResponseEntity setCode(Integer code) {
      m_code=code;
      return this;
   }

   public ResponseEntity setContentLength(Integer contentLength) {
      m_contentLength=contentLength;
      return this;
   }

   public ResponseEntity setContentType(String contentType) {
      m_contentType=contentType;
      return this;
   }

}
