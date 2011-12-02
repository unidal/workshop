package com.site.kernel.dal.db.helpers.token;

import java.util.HashMap;
import java.util.Map;

import com.site.kernel.dal.db.DataRow;
import com.site.kernel.dal.db.DataRowField;
import com.site.kernel.dal.db.helpers.QueryContext;
import com.site.kernel.dal.db.helpers.Token;

public final class StartTagToken extends Token {
   private Map<String, String> m_attrs;

   public StartTagToken(String token, Map<String, String> attrs) {
      super(token.trim().toUpperCase());

      int size = attrs.size();
      if (size > 0) {
         m_attrs = new HashMap<String, String>(size);
         m_attrs.putAll(attrs);
      }
   }

   public String getToken(QueryContext ctx) {
      String token = getToken();

      if (Tag.IF.equals(token)) {
         if (!ctx.isWithinIfTag()) {
            ctx.setWithinIfTag(true);

            if (m_attrs == null || !m_attrs.containsKey(Tag.IF_TYPE) || !m_attrs.containsKey(Tag.IF_PARAM)) {
               throw new RuntimeException("Attribute(" + Tag.IF_TYPE + " and " + Tag.IF_PARAM + ") is expected for IF tag");
            }

            String type = m_attrs.get(Tag.IF_TYPE);
            String param = m_attrs.get(Tag.IF_PARAM);

            if (!evaluateIf(ctx, type, param)) {
               ctx.setNoOutput(true);
            }

            return "";
         } else if (Tag.IN.equals(token)) {
            if (!ctx.isWithinInTag()) {
               ctx.setWithinInTag(true);
               return "(";
            } else {
               throw new RuntimeException("IN tag can't be nested");
            }
         } else {
            throw new RuntimeException("IF tag can't be nested");
         }
      } else {
         // TODO more tags here
      }

      return toString();
   }

   private boolean evaluateIf(QueryContext ctx, String type, String paramName) {
      DataRow protoDo = ctx.getProtoDo();
      DataRowField field = (DataRowField) protoDo.getMetaData().getField(paramName);
      Object value = protoDo.getFieldValue(field);

      if (type.equals("NOT_ZERO")) {
         if (value == null) {
            return false;
         } else if (value instanceof Boolean) {
            return ((Boolean) value).booleanValue();
         } else if (value instanceof Double) {
            return ((Double) value).doubleValue() != 0.0;
         } else {
            return !"0".equals(value.toString());
         }
      } else if (type.equals("NOT_NULL")) {
         return protoDo.isFieldUsed(field) && value != null;
      } else {
         // TODO more type here
      }

      return false;
   }

   public String toString() {
      return "<" + getToken() + ">";
   }
}