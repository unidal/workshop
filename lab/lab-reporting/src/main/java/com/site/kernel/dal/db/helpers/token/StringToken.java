package com.site.kernel.dal.db.helpers.token;

import com.site.kernel.dal.db.helpers.QueryContext;
import com.site.kernel.dal.db.helpers.Token;

public final class StringToken extends Token {
   public StringToken(String token) {
      super(token);
   }

   public String getToken(QueryContext ctx) {
      return getToken();
   }

   public String toString() {
      return getToken();
   }
}