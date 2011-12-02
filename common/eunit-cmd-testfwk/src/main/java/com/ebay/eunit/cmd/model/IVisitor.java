package com.ebay.eunit.cmd.model;

import com.ebay.eunit.cmd.model.entity.CommandEntity;
import com.ebay.eunit.cmd.model.entity.HeaderEntity;
import com.ebay.eunit.cmd.model.entity.ParameterEntity;
import com.ebay.eunit.cmd.model.entity.RequestEntity;
import com.ebay.eunit.cmd.model.entity.ResponseEntity;
import com.ebay.eunit.cmd.model.entity.RootEntity;

public interface IVisitor {

   public void visitCommand(CommandEntity command);

   public void visitHeader(HeaderEntity header);

   public void visitParameter(ParameterEntity parameter);

   public void visitRequest(RequestEntity request);

   public void visitResponse(ResponseEntity response);

   public void visitRoot(RootEntity root);
}
