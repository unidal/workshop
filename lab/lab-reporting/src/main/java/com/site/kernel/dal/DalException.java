package com.site.kernel.dal;

/**
 * @author qwu
 */
public class DalException extends Exception {
   private static final long serialVersionUID = 2469506067572782651L;

   public DalException(Throwable cause) {
      super(cause);
   }

   public DalException(String message) {
      super(message);
   }

   public DalException(String message, Throwable cause) {
      super(message, cause);
   }
}
