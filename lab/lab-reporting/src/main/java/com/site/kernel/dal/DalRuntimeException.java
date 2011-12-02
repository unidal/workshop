package com.site.kernel.dal;

/**
 * @author qwu
 */
public class DalRuntimeException extends RuntimeException {
   private static final long serialVersionUID = 6575424091202810889L;

   public DalRuntimeException(String message) {
      super(message);
   }

   public DalRuntimeException(String message, Throwable cause) {
      super(message, cause);
   }

}
