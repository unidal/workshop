package com.site.game.sanguo.thread;

public class ThreadException extends Exception {
   private static final long serialVersionUID = 1L;

   public ThreadException(String message) {
      super(message);
   }

   public ThreadException(String message, Throwable cause) {
      super(message, cause);
   }
}
