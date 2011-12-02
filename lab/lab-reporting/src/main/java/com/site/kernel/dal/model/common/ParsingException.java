package com.site.kernel.dal.model.common;

/**
 * @author qwu
 */
public class ParsingException extends Exception {
   private static final long serialVersionUID = 7081349408702471385L;

   public ParsingException(String message) {
      super(message);
   }

   public ParsingException(String message, Throwable cause) {
      super(message, cause);
   }
}
