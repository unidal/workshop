package com.site.dal.jdbc.datasource;

import javax.naming.NoInitialContextException;

import com.site.dal.jdbc.DalRuntimeException;

public class DataSourceException extends DalRuntimeException {
   private static final long serialVersionUID = -2599084531889763812L;

   public DataSourceException(String message) {
      super(message);
   }

   public DataSourceException(String message, Throwable cause) {
      super(message, cause);
   }

   public boolean isDataSourceDown() {
      Throwable cause = getCause();

      if (cause instanceof NoInitialContextException) {
         // we don't have an InitialContext setup
         return true;
      } else if (cause != null && cause.getMessage().contains("java.net.ConnectException: Connection refused: connect")) {
         return true;
      } else if (cause != null && cause.getMessage().contains("Could not create connection to database server.")) {
         return true;
      } else if (cause != null && cause.getMessage().contains("Communications link failure")) {
      	return true;
      } else if (cause != null && cause.getMessage().contains("Unknown database")) {
         return true;
      } else {
         return false;
      }
   }
}
