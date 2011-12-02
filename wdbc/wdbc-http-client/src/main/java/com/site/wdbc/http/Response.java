package com.site.wdbc.http;

import java.io.IOException;
import java.io.InputStream;

import com.site.wdbc.WdbcSourceType;

public interface Response {
   public String getCharset();

   public InputStream getContent() throws IOException;

   public WdbcSourceType getContentType();
}
