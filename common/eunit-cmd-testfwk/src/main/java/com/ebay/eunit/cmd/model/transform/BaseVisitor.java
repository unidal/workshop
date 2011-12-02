package com.ebay.eunit.cmd.model.transform;

import com.ebay.eunit.cmd.model.IVisitor;
import com.ebay.eunit.cmd.model.entity.CommandEntity;
import com.ebay.eunit.cmd.model.entity.HeaderEntity;
import com.ebay.eunit.cmd.model.entity.ParameterEntity;
import com.ebay.eunit.cmd.model.entity.RequestEntity;
import com.ebay.eunit.cmd.model.entity.ResponseEntity;
import com.ebay.eunit.cmd.model.entity.RootEntity;

public abstract class BaseVisitor implements IVisitor {
   @Override
   public void visitCommand(CommandEntity command) {
      if (command.getWithRequest() != null) {
         visitRequest(command.getWithRequest());
      }

      if (command.getExpectedResponse() != null) {
         visitResponse(command.getExpectedResponse());
      }
   }

   @Override
   public void visitHeader(HeaderEntity header) {
   }

   @Override
   public void visitParameter(ParameterEntity parameter) {
   }

   @Override
   public void visitRequest(RequestEntity request) {
      for (ParameterEntity parameter : request.getParameters().values()) {
         visitParameter(parameter);
      }

      for (HeaderEntity header : request.getHeaders().values()) {
         visitHeader(header);
      }
   }

   @Override
   public void visitResponse(ResponseEntity response) {
      for (HeaderEntity header : response.getHeaders().values()) {
         visitHeader(header);
      }
   }

   @Override
   public void visitRoot(RootEntity root) {
      for (CommandEntity command : root.getCommands()) {
         visitCommand(command);
      }
   }
}
