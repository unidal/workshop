package com.site.kernel.dal.db.helpers.token;

import java.lang.reflect.Array;

import com.site.kernel.dal.ValueType;
import com.site.kernel.dal.db.DataRow;
import com.site.kernel.dal.db.DataRowField;
import com.site.kernel.dal.db.helpers.Parameter;
import com.site.kernel.dal.db.helpers.QueryContext;
import com.site.kernel.dal.db.helpers.Token;

public final class ParameterToken extends Token {
   private boolean m_in;

   private boolean m_out;

   public ParameterToken(String token, boolean isIn, boolean isOut) {
      super(token.trim());

      m_in = isIn;
      m_out = isOut;
   }

   public String getToken(QueryContext ctx) {
      if (ctx.isNoOutput()) {
         return "";
      }

      DataRow protoDo = ctx.getProtoDo();
      DataRowField field = (DataRowField) protoDo.getMetaData().getField(getToken());

      if (field.getValueType() == ValueType.ARRAY) {
         StringBuffer sb = new StringBuffer(256);
         Object value = protoDo.getFieldValue(field);
         int len = (value == null ? 0 : Array.getLength(value));

         for (int i = 0; i < len; i++) {
            if (i > 0) {
               sb.append(',');
            }

            // add in parameter for later
            // PreparedStatement.setParameter(...)
            ctx.addParameter(new Parameter(field, i));
            sb.append('?');
         }

         if (len == 0) {
            // if no array is put in, then
            // put null within the brackets, "... IN (null)"
            sb.append("null");
         }

         return sb.toString();
      } else {
         // add parameter for later PreparedStatement.setXXX(...)
         // or CallableStatement.registerOutParameter(...)
         // or CallableStatement.setXXX(...)
         ctx.addParameter(new Parameter(field, m_in, m_out));

         return "?";
      }
   }

   public String toString() {
      StringBuffer sb = new StringBuffer(64);

      if (m_in) {
         sb.append('$');
      }

      if (m_out) {
         sb.append('#');
      }

      sb.append('{').append(getToken()).append('}');

      return sb.toString();
   }
}