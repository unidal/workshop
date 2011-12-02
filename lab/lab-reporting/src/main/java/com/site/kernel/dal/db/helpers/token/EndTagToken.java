package com.site.kernel.dal.db.helpers.token;

import com.site.kernel.dal.db.helpers.QueryContext;
import com.site.kernel.dal.db.helpers.Token;

public final class EndTagToken extends Token {
   public EndTagToken(String token) {
      super(token.trim().toUpperCase());
   }

   public String getToken(QueryContext ctx) {
      String token = getToken();

      if (Tag.IF.equals(token)) {
         if (ctx.isWithinIfTag()) {
            ctx.setWithinIfTag(false);
            ctx.setNoOutput(false);
            return "";
         } else {
            throw new RuntimeException("No starting IF tag found");
         }
      } else if (Tag.IN.equals(token)) {
         if (ctx.isWithinInTag()) {
            ctx.setWithinInTag(false);
            return ")";
         } else {
            throw new RuntimeException("No starting IN tag found");
         }
      } else {
         // TODO more tags here
      }

      return toString();
   }

   public String toString() {
      return "</" + getToken() + ">";
   }
}