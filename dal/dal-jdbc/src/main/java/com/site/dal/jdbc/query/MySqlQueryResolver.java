package com.site.dal.jdbc.query;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.codehaus.plexus.personality.plexus.lifecycle.phase.Initializable;
import org.codehaus.plexus.personality.plexus.lifecycle.phase.InitializationException;

import com.site.dal.jdbc.DalRuntimeException;
import com.site.dal.jdbc.QueryType;
import com.site.dal.jdbc.engine.QueryContext;
import com.site.dal.jdbc.query.token.SimpleTagToken;
import com.site.dal.jdbc.query.token.Token;
import com.site.dal.jdbc.query.token.TokenParser;
import com.site.dal.jdbc.query.token.TokenType;
import com.site.dal.jdbc.query.token.resolver.TokenResolver;
import com.site.lookup.ContainerHolder;

public class MySqlQueryResolver extends ContainerHolder implements QueryResolver, Initializable {
   private TokenParser m_tokenParser;

   private Map<TokenType, TokenResolver> m_map = new HashMap<TokenType, TokenResolver>();

   public void initialize() throws InitializationException {
      registerTokenResolver(TokenType.STRING);
      registerTokenResolver(TokenType.PARAM);
      registerTokenResolver(TokenType.FIELD);
      registerTokenResolver(TokenType.FIELDS);
      registerTokenResolver(TokenType.TABLE);
      registerTokenResolver(TokenType.TABLES);
      registerTokenResolver(TokenType.VALUES);
      registerTokenResolver(TokenType.JOINS);
      registerTokenResolver(TokenType.IN);
      registerTokenResolver(TokenType.IF);
      registerTokenResolver(TokenType.VALUE);
   }

   protected void registerTokenResolver(TokenType tokenType) {
      TokenResolver tokenResolver = lookup(TokenResolver.class, tokenType.name());

      m_map.put(tokenType, tokenResolver);
   }

   public void resolve(QueryContext ctx) {
      List<Token> tokens = ctx.getQuery().parse(m_tokenParser);
      StringBuilder sb = new StringBuilder(1024);

      for (Token token : tokens) {
         TokenType type = token.getType();
         TokenResolver resolver = m_map.get(type);

         if (resolver == null) {
            throw new DalRuntimeException("No TokenResolver registered for token type (" + token + ")");
         }

         if (type == TokenType.IF) {
            sb.append(resolver.resolve(token, ctx));
         } else {
            if (!ctx.isSqlResolveDisabled()) {
               sb.append(resolver.resolve(token, ctx));
            }
         }
      }

      // add tag <FIELDS/> on the fly for Store Proceduce Query
      if (ctx.getQuery().isStoreProcedure() && ctx.getQuery().getType() == QueryType.SELECT
            && ctx.getOutFields().isEmpty()) {
         TokenResolver resolver = m_map.get(TokenType.FIELDS);
         Map<String, String> attributes = Collections.emptyMap();

         resolver.resolve(new SimpleTagToken("FIELDS", attributes), ctx);
      }

      ctx.setSqlStatement(sb.toString());
   }
}
