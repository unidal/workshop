package com.site.wdbc;

import java.io.IOException;
import java.io.Reader;

public interface WdbcSource {
	public Reader getReader() throws IOException;

   public WdbcSourceType getType();
}
