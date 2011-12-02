package com.ebay.eunit.helper;

import java.util.List;

public class Joiners {
   public static StringJoiner by(final char delimiter) {
      return new StringJoiner() {
         @Override
         protected void appendDelimiter(StringBuilder sb) {
            sb.append(delimiter);
         }
      };
   }

   public static StringJoiner by(final String delimiter) {
      return new StringJoiner() {
         @Override
         protected void appendDelimiter(StringBuilder sb) {
            sb.append(delimiter);
         }
      };
   }

   public static interface IBuilder<T> {
      public String asString(T item);
   }

   public static abstract class StringJoiner {
      private boolean m_prefixDelimiter;

      private boolean m_noEmptyItem;

      protected abstract void appendDelimiter(StringBuilder sb);

      public String join(List<String> list) {
         return this.<String> join(list, null);
      }

      public <T> String join(List<T> list, IBuilder<T> builder) {
         if (list == null) {
            return null;
         }

         StringBuilder sb = new StringBuilder();

         join(sb, list, builder);

         return sb.toString();
      }

      public String join(String... array) {
         if (array == null) {
            return null;
         }

         StringBuilder sb = new StringBuilder();
         boolean first = true;

         if (m_prefixDelimiter) {
            appendDelimiter(sb);
         }

         for (String item : array) {
            if (m_noEmptyItem && (item == null || item.length() == 0)) {
               continue;
            }

            if (first) {
               first = false;
            } else {
               appendDelimiter(sb);
            }

            sb.append(item);
         }

         return sb.toString();
      }

      public <T> void join(StringBuilder sb, List<T> list, IBuilder<T> builder) {
         boolean first = true;

         if (list != null) {
            for (T item : list) {
               if (first) {
                  first = false;

                  if (m_prefixDelimiter) {
                     appendDelimiter(sb);
                  }
               } else {
                  appendDelimiter(sb);
               }

               if (builder == null) {
                  sb.append(item);
               } else {
                  sb.append(builder.asString(item));
               }
            }
         }
      }

      public StringJoiner prefixDelimiter() {
         m_prefixDelimiter = true;
         return this;
      }

      public StringJoiner noEmptyItem() {
         m_noEmptyItem = true;
         return this;
      }
   }
}
