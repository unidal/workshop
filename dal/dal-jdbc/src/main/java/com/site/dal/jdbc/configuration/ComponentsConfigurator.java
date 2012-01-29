package com.site.dal.jdbc.configuration;

import java.util.ArrayList;
import java.util.List;

import com.dianping.cat.message.MessageProducer;
import com.site.dal.jdbc.QueryEngine;
import com.site.dal.jdbc.datasource.DataSourceManager;
import com.site.dal.jdbc.datasource.DefaultDataSourceManager;
import com.site.dal.jdbc.engine.DefaultQueryContext;
import com.site.dal.jdbc.engine.DefaultQueryEngine;
import com.site.dal.jdbc.engine.QueryContext;
import com.site.dal.jdbc.entity.DataObjectAccessor;
import com.site.dal.jdbc.entity.DataObjectAssembly;
import com.site.dal.jdbc.entity.DataObjectNaming;
import com.site.dal.jdbc.entity.DefaultDataObjectAccessor;
import com.site.dal.jdbc.entity.DefaultDataObjectAssembly;
import com.site.dal.jdbc.entity.DefaultDataObjectNaming;
import com.site.dal.jdbc.entity.DefaultEntityInfoManager;
import com.site.dal.jdbc.entity.EntityInfoManager;
import com.site.dal.jdbc.mapping.DefaultTableProviderManager;
import com.site.dal.jdbc.mapping.RawTableProvider;
import com.site.dal.jdbc.mapping.TableProvider;
import com.site.dal.jdbc.mapping.TableProviderManager;
import com.site.dal.jdbc.query.DefaultQueryExecutor;
import com.site.dal.jdbc.query.MySqlQueryResolver;
import com.site.dal.jdbc.query.QueryExecutor;
import com.site.dal.jdbc.query.QueryResolver;
import com.site.dal.jdbc.query.token.DefaultTokenParser;
import com.site.dal.jdbc.query.token.TokenParser;
import com.site.dal.jdbc.query.token.TokenType;
import com.site.dal.jdbc.query.token.resolver.ExpressionResolver;
import com.site.dal.jdbc.query.token.resolver.FieldTokenResolver;
import com.site.dal.jdbc.query.token.resolver.FieldsTokenResolver;
import com.site.dal.jdbc.query.token.resolver.IfTokenResolver;
import com.site.dal.jdbc.query.token.resolver.InTokenResolver;
import com.site.dal.jdbc.query.token.resolver.JoinsTokenResolver;
import com.site.dal.jdbc.query.token.resolver.ParameterTokenResolver;
import com.site.dal.jdbc.query.token.resolver.StringTokenResolver;
import com.site.dal.jdbc.query.token.resolver.TableTokenResolver;
import com.site.dal.jdbc.query.token.resolver.TablesTokenResolver;
import com.site.dal.jdbc.query.token.resolver.TokenResolver;
import com.site.dal.jdbc.query.token.resolver.ValueTokenResolver;
import com.site.dal.jdbc.query.token.resolver.ValuesTokenResolver;
import com.site.dal.jdbc.raw.RawDao;
import com.site.dal.jdbc.transaction.DefaultTransactionManager;
import com.site.dal.jdbc.transaction.TransactionManager;
import com.site.lookup.configuration.AbstractResourceConfigurator;
import com.site.lookup.configuration.Component;

public class ComponentsConfigurator extends AbstractResourceConfigurator {
   @Override
   public List<Component> defineComponents() {
      List<Component> all = new ArrayList<Component>();

      all.add(C(QueryEngine.class, DefaultQueryEngine.class) //
               .req(EntityInfoManager.class, QueryExecutor.class, TransactionManager.class) //
               .req(QueryResolver.class, "MySql"));
      all.add(C(DataSourceManager.class, DefaultDataSourceManager.class));
      all.add(C(QueryContext.class, DefaultQueryContext.class).is(PER_LOOKUP));
      all.add(C(EntityInfoManager.class, DefaultEntityInfoManager.class));
      all.add(C(DataObjectAccessor.class, DefaultDataObjectAccessor.class) //
               .req(DataObjectNaming.class));
      all.add(C(DataObjectAssembly.class, DefaultDataObjectAssembly.class) //
               .req(DataObjectAccessor.class, DataObjectNaming.class));
      all.add(C(DataObjectNaming.class, DefaultDataObjectNaming.class));
      all.add(C(TableProviderManager.class, DefaultTableProviderManager.class));
      all.add(C(QueryResolver.class, "MySql", MySqlQueryResolver.class) //
               .req(TokenParser.class));
      all.add(C(QueryExecutor.class, DefaultQueryExecutor.class) //
               .req(TransactionManager.class, DataObjectAccessor.class, DataObjectAssembly.class) //
               .req(MessageProducer.class));
      all.add(C(TransactionManager.class, DefaultTransactionManager.class) //
               .req(TableProviderManager.class, DataSourceManager.class));

      all.add(C(TokenParser.class, DefaultTokenParser.class));
      all.add(C(ExpressionResolver.class) //
               .req(TokenParser.class));

      all.add(C(TokenResolver.class, TokenType.STRING, StringTokenResolver.class));
      all.add(C(TokenResolver.class, TokenType.PARAM, ParameterTokenResolver.class) //
               .req(DataObjectAccessor.class));
      all.add(C(TokenResolver.class, TokenType.FIELD, FieldTokenResolver.class) //
               .req(EntityInfoManager.class, ExpressionResolver.class));
      all.add(C(TokenResolver.class, TokenType.FIELDS, FieldsTokenResolver.class) //
               .req(EntityInfoManager.class, ExpressionResolver.class));
      all.add(C(TokenResolver.class, TokenType.TABLE, TableTokenResolver.class) //
               .req(TableProviderManager.class));
      all.add(C(TokenResolver.class, TokenType.TABLES, TablesTokenResolver.class) //
               .req(TableProviderManager.class));
      all.add(C(TokenResolver.class, TokenType.VALUES, ValuesTokenResolver.class) //
               .req(ExpressionResolver.class));
      all.add(C(TokenResolver.class, TokenType.JOINS, JoinsTokenResolver.class));
      all.add(C(TokenResolver.class, TokenType.IN, InTokenResolver.class));
      all.add(C(TokenResolver.class, TokenType.IF, IfTokenResolver.class) //
               .req(DataObjectAccessor.class));
      all.add(C(TokenResolver.class, TokenType.VALUE, ValueTokenResolver.class) //
               .req(ExpressionResolver.class));

      all.add(C(TableProvider.class, "raw", RawTableProvider.class) //
               .config(E("logical-table-name").value("raw")));
      all.add(C(RawDao.class).req(QueryEngine.class));

      return all;
   }

   public static void main(String[] args) {
      generatePlexusComponentsXmlFile(new ComponentsConfigurator());
   }
}
