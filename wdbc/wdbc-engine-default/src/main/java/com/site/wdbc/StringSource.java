package com.site.wdbc;

import java.io.Reader;
import java.io.StringReader;

public class StringSource implements WdbcSource {
   private String m_content;

   private WdbcSourceType m_type;

   public StringSource(WdbcSourceType type, String content) {
      m_type = type;
      m_content = content;
   }

   public Reader getReader() {
      return new StringReader(m_content);
   }

   public WdbcSourceType getType() {
      return m_type;
   }
}
