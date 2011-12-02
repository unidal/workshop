package com.site.wdbc.query;

import com.site.wdbc.WdbcException;
import com.site.wdbc.WdbcResult;
import com.site.wdbc.WdbcSource;

public interface WdbcParser {
   public WdbcResult parse(WdbcHandler handler, WdbcSource source)
         throws WdbcException;
}
