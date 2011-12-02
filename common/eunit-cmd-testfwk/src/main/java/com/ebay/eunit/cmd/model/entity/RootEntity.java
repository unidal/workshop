package com.ebay.eunit.cmd.model.entity;

import java.util.ArrayList;
import java.util.List;

import com.ebay.eunit.cmd.model.BaseEntity;
import com.ebay.eunit.cmd.model.IVisitor;

public class RootEntity extends BaseEntity<RootEntity> {
   private List<CommandEntity> m_commands = new ArrayList<CommandEntity>();

   public RootEntity() {
   }

   @Override
   public void accept(IVisitor visitor) {
      visitor.visitRoot(this);
   }

   public RootEntity addCommand(CommandEntity command) {
      m_commands.add(command);
      return this;
   }

   public CommandEntity findCommand(String methodName) {
      for (CommandEntity command : m_commands) {
         if (!command.getMethodName().equals(methodName)) {
            continue;
         }

         return command;
      }

      return null;
   }

   public List<CommandEntity> getCommands() {
      return m_commands;
   }

   @Override
   public void mergeAttributes(RootEntity other) {
   }

   public boolean removeCommand(String methodName) {
      int len = m_commands.size();

      for (int i = 0; i < len; i++) {
         CommandEntity command = m_commands.get(i);

         if (!command.getMethodName().equals(methodName)) {
            continue;
         }

         m_commands.remove(i);
         return true;
      }

      return false;
   }

}
