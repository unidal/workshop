package com.ebay.eunit.helper;

import java.util.ArrayList;
import java.util.List;

public class Splitters {
   public static StringSplitter by(char delimiter) {
      return new StringSplitter(delimiter);
   }

   public static class StringSplitter {
      private char m_delimiter;

      private boolean m_trimmed;

      private boolean m_noEmptyItem;

      StringSplitter(char delimiter) {
         m_delimiter = delimiter;
      }

      public StringSplitter trim() {
         m_trimmed = true;
         return this;
      }

      public StringSplitter noEmptyItem() {
         m_noEmptyItem = true;
         return this;
      }

      public List<String> split(String str) {
         return split(str, new ArrayList<String>());
      }

      public List<String> split(String str, List<String> list) {
         if (str == null) {
            return null;
         }

         if (m_delimiter > 0) {
            char delimiter = m_delimiter;
            int len = str.length();
            StringBuilder sb = new StringBuilder(len);

            for (int i = 0; i < len; i++) {
               char ch = str.charAt(i);

               if (ch == delimiter) {
                  if (m_noEmptyItem && sb.length() == 0) {
                     continue;
                  }

                  if (m_trimmed) {
                     list.add(sb.toString().trim()); // TODO perf issue, may
                                                     // create two objects
                  } else {
                     list.add(sb.toString());
                  }

                  sb.setLength(0);
               } else {
                  sb.append(ch);
               }
            }

            if (!m_noEmptyItem || sb.length() > 0) {
               if (m_trimmed) {
                  list.add(sb.toString().trim()); // TODO perf issue, may create
                                                  // two objects
               } else {
                  list.add(sb.toString());
               }
            }

            return list;
         }

         throw new UnsupportedOperationException();
      }
   }
}
