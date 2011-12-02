package com.site.dal.jdbc.query.token.resolver;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

import com.site.dal.jdbc.DalRuntimeException;
import com.site.dal.jdbc.DataField;
import com.site.dal.jdbc.DataObject;
import com.site.dal.jdbc.Readset;
import com.site.dal.jdbc.annotation.Attribute;
import com.site.dal.jdbc.annotation.Relation;
import com.site.dal.jdbc.annotation.SubObjects;
import com.site.dal.jdbc.engine.QueryContext;
import com.site.dal.jdbc.entity.EntityInfo;
import com.site.dal.jdbc.entity.EntityInfoManager;
import com.site.dal.jdbc.query.Parameter;
import com.site.dal.jdbc.query.token.SimpleTagToken;
import com.site.dal.jdbc.query.token.Token;
import com.site.dal.jdbc.query.token.TokenType;
import com.site.lookup.annotation.Inject;

/**
 * &lt;fields /&gt;
 */
public class FieldsTokenResolver implements TokenResolver {
   @Inject
   private EntityInfoManager m_manager;

   @Inject
   private ExpressionResolver m_expressionResolver;

   @SuppressWarnings("unchecked")
   public String resolve(Token token, QueryContext ctx) {
      if (token.getType() != TokenType.FIELDS) {
         throw new DalRuntimeException("Internal error: only FIELDS token is supported by " + getClass());
      }

      EntityInfo entityInfo = ctx.getEntityInfo();
      SimpleTagToken fields = (SimpleTagToken) token;
      String output = fields.getAttribute("output", "true");
      List<String> excludes = split(fields.getAttribute("excludes", ""), ",");
      StringBuilder sb = new StringBuilder(1024);
      DataObject proto = ctx.getProto();

      switch (ctx.getQuery().getType()) {
      case SELECT:
         SubObjects subobjects = entityInfo.getSubobjects(ctx.getReadset());
         String[] names;
         List<Readset<Object>> readsets;

         if (subobjects != null) {
            names = subobjects.value();
            readsets = ((Readset<Object>) ctx.getReadset()).getChildren();
         } else {
            names = null;
            readsets = new ArrayList<Readset<Object>>(1);
            readsets.add((Readset<Object>) ctx.getReadset());
         }

         int size = readsets.size();

         for (int i = 0; i < size; i++) {
            Readset<Object> readset = readsets.get(i);
            Relation relation = (names == null ? null : entityInfo.getRelation(names[i]));
            String alias = (relation == null ? entityInfo.getAlias() : relation.alias());
            String subObjectName = (relation == null ? null : names[i]);

            for (DataField field : readset.getFields()) {
               EntityInfo ei = m_manager.getEntityInfo(field.getEntityClass());
               Attribute attribute = ei.getAttribute(field);

               if (attribute != null) {
                  if (excludes.contains(field.getName())) {
                     continue;
                  }

                  if (sb.length() > 0) {
                     sb.append(',');
                  }

                  if (attribute.selectExpr().length() > 0) {
                     sb.append(m_expressionResolver.resolve(ctx, attribute.selectExpr()));
                  } else {
                     sb.append(alias).append('.').append(attribute.field());
                  }

                  if ("true".equals(output)) {
                     ctx.addOutField(field);
                     ctx.addOutSubObjectName(subObjectName);
                  }
               } else {
                  throw new DalRuntimeException("Internal error: No Attribute annotation defined for field: " + field);
               }
            }
         }

         break;
      case INSERT:
         for (DataField field : entityInfo.getAttributeFields()) {
            Attribute attribute = entityInfo.getAttribute(field);

            if (attribute != null) {
               if (attribute.field().length() > 0 && !(attribute.autoIncrement() && !proto.isFieldUsed(field))) {
                  if (sb.length() > 0) {
                     sb.append(',');
                  }

                  sb.append(attribute.field());
               }
            } else {
               throw new DalRuntimeException("Internal error: No Attribute annotation defined for field: " + field);
            }
         }

         break;
      case UPDATE:
         for (DataField field : ctx.getUpdateset().getFields()) {
            Attribute attribute = entityInfo.getAttribute(field);

            if (attribute != null) {
               if (proto.isFieldUsed(field) || attribute.updateExpr().length() > 0) {
                  if (sb.length() > 0) {
                     sb.append(',');
                  }

                  if (!proto.isFieldUsed(field) && attribute.updateExpr().length() > 0) {
                     sb.append(attribute.field()).append('=').append(m_expressionResolver.resolve(ctx, attribute.updateExpr()));
                  } else {
                     sb.append(attribute.field()).append("=?");
                     ctx.addParameter(new Parameter(field));
                  }
               }
            } else {
               throw new DalRuntimeException("Internal error: No Attribute annotation defined for field: " + field);
            }
         }

         break;
      case DELETE:
         throw new DalRuntimeException("FIELDS token does not support query type: " + ctx.getQuery().getType());
      default:
         throw new DalRuntimeException("FIELDS token does not support query type: " + ctx.getQuery().getType());
      }

      return sb.toString();
   }

   private List<String> split(String data, String delimiter) {
      if (data != null && data.length() > 0) {
         String[] parts = data.split(Pattern.quote(delimiter));

         return Arrays.asList(parts);
      } else {
         return Collections.emptyList();
      }
   }
}
