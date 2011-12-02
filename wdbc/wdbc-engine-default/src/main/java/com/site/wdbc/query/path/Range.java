package com.site.wdbc.query.path;

import java.util.ArrayList;
import java.util.List;

public class Range {
   private List<Integer> m_froms = new ArrayList<Integer>();

   private List<Integer> m_tos = new ArrayList<Integer>();

   public Range(String expression) {
      parse(expression);
   }

   private void parse(String str) {
      String[] parts = str.split(",");

      for (String part : parts) {
         int pos = part.indexOf('-');
         int from;
         int to;

         if (pos < 0) {
            if (part.equals("*")) {
               from = 0;
               to = Integer.MAX_VALUE;
            } else {
               int value = Integer.valueOf(part);

               from = value;
               to = value;
            }
         } else {
            if (pos == 0) {
               from = Integer.MIN_VALUE;
               to = Integer.valueOf(part.substring(pos + 1));
            } else if (pos == part.length() - 1) {
               from = Integer.valueOf(part.substring(0, pos));
               to = Integer.MAX_VALUE;
            } else {
               from = Integer.valueOf(part.substring(0, pos));
               to = Integer.valueOf(part.substring(pos + 1));
            }
         }

         m_froms.add(from);
         m_tos.add(to);
      }
   }

   public boolean inRange(int value) {
      int len = m_froms.size();

      for (int i = 0; i < len; i++) {
         int from = m_froms.get(i);
         int to = m_tos.get(i);

         if (from <= value && value <= to) {
            return true;
         }
      }

      return false;
   }
}