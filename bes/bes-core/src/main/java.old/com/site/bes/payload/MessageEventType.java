package com.site.bes.payload;

import com.site.kernel.common.BaseEnum;

public class MessageEventType extends BaseEnum {
   public static final MessageEventType MESSAGE_NEW = new MessageEventType(1, "Message.New");

   public static final MessageEventType MESSAGE_MODIFIED = new MessageEventType(2, "Message.Modified");

   public static final MessageEventType MESSAGE_DELETED = new MessageEventType(3, "Message.Deleted");

   protected MessageEventType(int id, String name) {
      super(id, name);
   }

   public static MessageEventType getByName(String name) {
      return (MessageEventType) getByName(MessageEventType.class, name);
   }
}
