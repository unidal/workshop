package org.unidal.ezsell.login;

public class UserSuspendedException extends Exception {
   private static final long serialVersionUID = 1L;

   public UserSuspendedException(String message) {
      super(message);
   }
}
