package com.ebay.eunit.cmd.model.transform;

import static com.ebay.eunit.cmd.model.Constants.ATTR_BODY;
import static com.ebay.eunit.cmd.model.Constants.ATTR_CODE;
import static com.ebay.eunit.cmd.model.Constants.ATTR_CONTENT_LENGTH;
import static com.ebay.eunit.cmd.model.Constants.ATTR_CONTENT_TYPE;
import static com.ebay.eunit.cmd.model.Constants.ATTR_FORM_DATA;
import static com.ebay.eunit.cmd.model.Constants.ATTR_METHOD_NAME;
import static com.ebay.eunit.cmd.model.Constants.ATTR_NAME;
import static com.ebay.eunit.cmd.model.Constants.ATTR_REQUEST_METHOD;
import static com.ebay.eunit.cmd.model.Constants.ATTR_REQUEST_URL;
import static com.ebay.eunit.cmd.model.Constants.ELEMENT_VALUE;
import static com.ebay.eunit.cmd.model.Constants.ENTITY_COMMAND;
import static com.ebay.eunit.cmd.model.Constants.ENTITY_HEADER;
import static com.ebay.eunit.cmd.model.Constants.ENTITY_HEADERS;
import static com.ebay.eunit.cmd.model.Constants.ENTITY_PARAMETER;
import static com.ebay.eunit.cmd.model.Constants.ENTITY_PARAMETERS;
import static com.ebay.eunit.cmd.model.Constants.ENTITY_REQUEST;
import static com.ebay.eunit.cmd.model.Constants.ENTITY_RESPONSE;
import static com.ebay.eunit.cmd.model.Constants.ENTITY_ROOT;

import com.ebay.eunit.cmd.model.IVisitor;
import com.ebay.eunit.cmd.model.entity.CommandEntity;
import com.ebay.eunit.cmd.model.entity.HeaderEntity;
import com.ebay.eunit.cmd.model.entity.ParameterEntity;
import com.ebay.eunit.cmd.model.entity.RequestEntity;
import com.ebay.eunit.cmd.model.entity.ResponseEntity;
import com.ebay.eunit.cmd.model.entity.RootEntity;

public class DefaultXmlBuilder implements IVisitor {

   private int m_level;

   private StringBuilder m_sb = new StringBuilder(2048);

   private void endTag(String name) {
      m_level--;

      indent();
      m_sb.append("</").append(name).append(">\r\n");
   }

   public String getString() {
      return m_sb.toString();
   }

   private void indent() {
      for (int i = m_level - 1; i >= 0; i--) {
         m_sb.append("   ");
      }
   }

   private void startTag(String name, boolean closed, Object... nameValues) {
      startTag(name, null, closed, nameValues);
   }

   private void startTag(String name, Object... nameValues) {
      startTag(name, false, nameValues);
   }
   
   private void startTag(String name, Object text, boolean closed, Object... nameValues) {
      indent();

      m_sb.append('<').append(name);

      int len = nameValues.length;

      for (int i = 0; i + 1 < len; i += 2) {
         Object attrName = nameValues[i];
         Object attrValue = nameValues[i + 1];

         if (attrValue != null) {
            m_sb.append(' ').append(attrName).append("=\"").append(attrValue).append('"');
         }
      }

      if (text != null && closed) {
         m_sb.append('>');
         m_sb.append(text == null ? "" : text);
         m_sb.append("</").append(name).append(">\r\n");
      } else {
         if (closed) {
            m_sb.append('/');
         } else {
            m_level++;
         }
   
         m_sb.append(">\r\n");
      }
   }

   private void tagWithText(String name, String text, Object... nameValues) {
      if (text == null) {
         return;
      }
      
      indent();

      m_sb.append('<').append(name);

      int len = nameValues.length;

      for (int i = 0; i + 1 < len; i += 2) {
         Object attrName = nameValues[i];
         Object attrValue = nameValues[i + 1];

         if (attrValue != null) {
            m_sb.append(' ').append(attrName).append("=\"").append(attrValue).append('"');
         }
      }

      m_sb.append(">");
      m_sb.append(text);
      m_sb.append("</").append(name).append(">\r\n");
   }

   @Override
   public void visitCommand(CommandEntity command) {
      startTag(ENTITY_COMMAND, ATTR_METHOD_NAME, command.getMethodName());

      if (command.getWithRequest() != null) {
         visitRequest(command.getWithRequest());
      }

      if (command.getExpectedResponse() != null) {
         visitResponse(command.getExpectedResponse());
      }

      endTag(ENTITY_COMMAND);
   }

   @Override
   public void visitHeader(HeaderEntity header) {
      startTag(ENTITY_HEADER, ATTR_NAME, header.getName());

      if (!header.getValues().isEmpty()) {
         for (String value : header.getValues()) {
            tagWithText(ELEMENT_VALUE, value);
         }
      }

      endTag(ENTITY_HEADER);
   }

   @Override
   public void visitParameter(ParameterEntity parameter) {
      startTag(ENTITY_PARAMETER, ATTR_NAME, parameter.getName());

      if (!parameter.getValues().isEmpty()) {
         for (String value : parameter.getValues()) {
            tagWithText(ELEMENT_VALUE, value);
         }
      }

      endTag(ENTITY_PARAMETER);
   }

   @Override
   public void visitRequest(RequestEntity request) {
      startTag(ENTITY_REQUEST, ATTR_REQUEST_METHOD, request.getRequestMethod(), ATTR_REQUEST_URL, request.getRequestUrl(), ATTR_FORM_DATA, request.getFormData());

      if (!request.getParameters().isEmpty()) {
         startTag(ENTITY_PARAMETERS);

         for (ParameterEntity parameter : request.getParameters().values()) {
            visitParameter(parameter);
         }

         endTag(ENTITY_PARAMETERS);
      }

      if (!request.getHeaders().isEmpty()) {
         startTag(ENTITY_HEADERS);

         for (HeaderEntity header : request.getHeaders().values()) {
            visitHeader(header);
         }

         endTag(ENTITY_HEADERS);
      }

      endTag(ENTITY_REQUEST);
   }

   @Override
   public void visitResponse(ResponseEntity response) {
      startTag(ENTITY_RESPONSE, ATTR_CODE, response.getCode(), ATTR_CONTENT_LENGTH, response.getContentLength(), ATTR_CONTENT_TYPE, response.getContentType(), ATTR_BODY, response.getBody());

      if (!response.getHeaders().isEmpty()) {
         startTag(ENTITY_HEADERS);

         for (HeaderEntity header : response.getHeaders().values()) {
            visitHeader(header);
         }

         endTag(ENTITY_HEADERS);
      }

      endTag(ENTITY_RESPONSE);
   }

   @Override
   public void visitRoot(RootEntity root) {
      startTag(ENTITY_ROOT);

      if (!root.getCommands().isEmpty()) {
         for (CommandEntity command : root.getCommands()) {
            visitCommand(command);
         }
      }

      endTag(ENTITY_ROOT);
   }
}
