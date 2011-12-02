package com.ebay.eunit.cmd.model.transform;

import static com.ebay.eunit.cmd.model.Constants.ATTR_METHOD_NAME;
import static com.ebay.eunit.cmd.model.Constants.ATTR_NAME;
import static com.ebay.eunit.cmd.model.Constants.ENTITY_COMMAND;
import static com.ebay.eunit.cmd.model.Constants.ENTITY_HEADER;
import static com.ebay.eunit.cmd.model.Constants.ENTITY_HEADERS;
import static com.ebay.eunit.cmd.model.Constants.ENTITY_PARAMETER;
import static com.ebay.eunit.cmd.model.Constants.ENTITY_PARAMETERS;
import static com.ebay.eunit.cmd.model.Constants.ENTITY_REQUEST;
import static com.ebay.eunit.cmd.model.Constants.ENTITY_RESPONSE;
import static com.ebay.eunit.cmd.model.Constants.ENTITY_ROOT;

import java.util.Stack;

import com.ebay.eunit.cmd.model.IVisitor;
import com.ebay.eunit.cmd.model.entity.CommandEntity;
import com.ebay.eunit.cmd.model.entity.HeaderEntity;
import com.ebay.eunit.cmd.model.entity.ParameterEntity;
import com.ebay.eunit.cmd.model.entity.RequestEntity;
import com.ebay.eunit.cmd.model.entity.ResponseEntity;
import com.ebay.eunit.cmd.model.entity.RootEntity;

public class DefaultValidator implements IVisitor {

   private Path m_path = new Path();
   
   protected void assertRequired(String name, Object value) {
      if (value == null) {
         throw new RuntimeException(String.format("%s at path(%s) is required!", name, m_path));
      }
   }

   @Override
   public void visitCommand(CommandEntity command) {
      m_path.down(ENTITY_COMMAND);

      assertRequired(ATTR_METHOD_NAME, command.getMethodName());

      visitCommandChildren(command);

      m_path.up(ENTITY_COMMAND);
   }

   protected void visitCommandChildren(CommandEntity command) {
      if (command.getWithRequest() != null) {
         visitRequest(command.getWithRequest());
      }

      if (command.getExpectedResponse() != null) {
         visitResponse(command.getExpectedResponse());
      }
   }

   @Override
   public void visitHeader(HeaderEntity header) {
      m_path.down(ENTITY_HEADER);

      assertRequired(ATTR_NAME, header.getName());

      m_path.up(ENTITY_HEADER);
   }

   @Override
   public void visitParameter(ParameterEntity parameter) {
      m_path.down(ENTITY_PARAMETER);

      assertRequired(ATTR_NAME, parameter.getName());

      m_path.up(ENTITY_PARAMETER);
   }

   @Override
   public void visitRequest(RequestEntity request) {
      m_path.down(ENTITY_REQUEST);

      visitRequestChildren(request);

      m_path.up(ENTITY_REQUEST);
   }

   protected void visitRequestChildren(RequestEntity request) {
      m_path.down(ENTITY_PARAMETERS);

      for (ParameterEntity parameter : request.getParameters().values()) {
         visitParameter(parameter);
      }

      m_path.up(ENTITY_PARAMETERS);

      m_path.down(ENTITY_HEADERS);

      for (HeaderEntity header : request.getHeaders().values()) {
         visitHeader(header);
      }

      m_path.up(ENTITY_HEADERS);
   }

   @Override
   public void visitResponse(ResponseEntity response) {
      m_path.down(ENTITY_RESPONSE);

      visitResponseChildren(response);

      m_path.up(ENTITY_RESPONSE);
   }

   protected void visitResponseChildren(ResponseEntity response) {
      m_path.down(ENTITY_HEADERS);

      for (HeaderEntity header : response.getHeaders().values()) {
         visitHeader(header);
      }

      m_path.up(ENTITY_HEADERS);
   }

   @Override
   public void visitRoot(RootEntity root) {
      m_path.down(ENTITY_ROOT);

      visitRootChildren(root);

      m_path.up(ENTITY_ROOT);
   }

   protected void visitRootChildren(RootEntity root) {
      for (CommandEntity command : root.getCommands()) {
         visitCommand(command);
      }
   }

   static class Path {
      private Stack<String> m_sections = new Stack<String>();

      public Path down(String nextSection) {
         m_sections.push(nextSection);

         return this;
      }

      @Override
      public String toString() {
         StringBuilder sb = new StringBuilder();

         for (String section : m_sections) {
            sb.append('/').append(section);
         }

         return sb.toString();
      }

      public Path up(String currentSection) {
         if (m_sections.isEmpty() || !m_sections.peek().equals(currentSection)) {
            throw new RuntimeException("INTERNAL ERROR: stack mismatched!");
         }

         m_sections.pop();
         return this;
      }
   }
}
