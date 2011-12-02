package com.ebay.eunit.cmd.spi;

public interface IRequest {
   public void addHeader(String name, String value);

   public void setHeader(String name, String value);
}
