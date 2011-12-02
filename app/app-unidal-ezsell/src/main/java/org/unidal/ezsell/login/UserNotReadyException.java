package org.unidal.ezsell.login;

public class UserNotReadyException extends Exception {
   private static final long serialVersionUID = 1L;

   public UserNotReadyException(String message) {
      super(message);
   }
}
