package com.site.kernel.logging.cal;

public interface CalTransaction {
   public void addData(String name,Object[] values);
   public void addData(String name,String value);
   public void addData(String nameValuePairs);
   public void complete();
   public void complete(String status);
   public String getData();
   public String getName();
   public CalTransaction getParent();
   public String getStatus();
   public String getType();
   public void setStatus(String status);
   public void setStatus(Throwable t);
}
