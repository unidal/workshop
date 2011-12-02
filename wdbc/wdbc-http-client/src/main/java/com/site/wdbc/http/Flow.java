package com.site.wdbc.http;

import com.site.wdbc.WdbcException;

public interface Flow {
   public void execute() throws WdbcException;
}
