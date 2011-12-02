package org.unidal.ezsell.register;

public class OperatorAlreadyExistedException extends Exception {
   private static final long serialVersionUID = 1L;

   public OperatorAlreadyExistedException(String message) {
      super(message);
   }
}
