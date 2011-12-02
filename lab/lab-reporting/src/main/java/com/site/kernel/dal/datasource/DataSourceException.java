package com.site.kernel.dal.datasource;

import com.site.kernel.dal.DalRuntimeException;


/**
 * @author qwu
 */
public class DataSourceException extends DalRuntimeException {
   private static final long serialVersionUID = 3554587881960938641L;

   public DataSourceException(String message) {
      super(message);
   }

   public DataSourceException(String message, Throwable cause) {
      super(message, cause);
   }
}
