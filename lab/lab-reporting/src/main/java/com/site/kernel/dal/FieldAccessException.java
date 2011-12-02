package com.site.kernel.dal;

public class FieldAccessException extends RuntimeException {
   private static final long serialVersionUID = 1619418715996484399L;
   private Throwable m_cause = this;

   public FieldAccessException(String msg, Throwable cause) {
      super(msg);

      m_cause = cause;
   }

   public FieldAccessException(String msg) {
      super(msg);
   }

   public Throwable getCause() {
      return (m_cause == this ? null : m_cause);
   }
}