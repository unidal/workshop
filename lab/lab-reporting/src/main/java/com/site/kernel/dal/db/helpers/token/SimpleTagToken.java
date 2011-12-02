package com.site.kernel.dal.db.helpers.token;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.site.kernel.dal.DalRuntimeException;
import com.site.kernel.dal.DataObjectField;
import com.site.kernel.dal.db.DataRow;
import com.site.kernel.dal.db.DataRowField;
import com.site.kernel.dal.db.Entity;
import com.site.kernel.dal.db.EntityJoin;
import com.site.kernel.dal.db.Readset;
import com.site.kernel.dal.db.TableProvider;
import com.site.kernel.dal.db.helpers.Parameter;
import com.site.kernel.dal.db.helpers.QueryContext;
import com.site.kernel.dal.db.helpers.TableProviderManager;
import com.site.kernel.dal.db.helpers.Token;

public final class SimpleTagToken extends Token {
   private Map<String, String> m_attrs;

   public SimpleTagToken(String token, Map<String, String> attrs) {
      super(token.trim().toUpperCase());

      int size = attrs.size();
      if (size > 0) {
         m_attrs = new HashMap<String, String>(size);
         m_attrs.putAll(attrs);
      }
   }

   public String getToken(QueryContext ctx) {
      String token = getToken();

      if (ctx.isNoOutput()) {
         return "";
      } else if (Tag.FIELDS.equals(token)) {
         return getTokenForFields(ctx);
      } else if (Tag.TABLE.equals(token)) {
         return getTokenForTable(ctx);
      } else if (Tag.TABLES.equals(token)) {
         return getTokenForTables(ctx);
      } else if (Tag.JOINS.equals(token)) {
         return getTokenForJoins(ctx);
      } else if (Tag.ALIAS.equals(token)) {
         return getTokenForAlias(ctx);
      } else {
         // TODO more tags here
      }

      return toString();
   }

   private String getTokenForAlias(QueryContext ctx) {
      return ctx.getEntity().getAlias();
   }

   private String getTokenForTable(QueryContext ctx) {
      StringBuffer sb = new StringBuffer(64);
      TableProviderManager manager = TableProviderManager.getInstance();
      DataRow proto = ctx.getProtoDo();

      if (ctx.isQuerySelect()) {
         Entity entity = ctx.getEntity();
         TableProvider provider = manager.getTableProvider(entity.getLogicalName());

         sb.append(provider.getPhysicalTableName(proto.getQueryHints()));
         sb.append(' ');
         sb.append(entity.getAlias());

         return sb.toString();
      } else if (ctx.isQueryInsert() || ctx.isQueryUpdate() || ctx.isQueryDelete()) {
         Entity entity = ctx.getEntity();
         TableProvider provider = manager.getTableProvider(entity.getLogicalName());

         sb.append(provider.getPhysicalTableName(proto.getQueryHints()));

         return sb.toString();
      } else {
         return toString();
      }
   }

   private String getTokenForTables(QueryContext ctx) {
      if (ctx.isQuerySelect()) {
         return getTableList(ctx, ctx.getEntity(), new HashSet<Entity>());
      } else if (ctx.isQueryInsert() || ctx.isQueryUpdate() || ctx.isQueryDelete()) {
         throw new RuntimeException("Can't perform INSERT/UPDATE/DELETE query against mutliple tables");
      } else {
         return toString();
      }
   }

   private String getTableList(QueryContext ctx, Entity entity, HashSet<Entity> set) {
      StringBuffer sb = new StringBuffer(64);
      TableProviderManager manager = TableProviderManager.getInstance();
      TableProvider provider = manager.getTableProvider(entity.getLogicalName());

      sb.append(provider.getPhysicalTableName(ctx.getProtoDo().getQueryHints()));
      sb.append(' ');
      sb.append(entity.getAlias());
      set.add(entity);

      EntityJoin[] joins = entity.getEntityJoins();
      Readset readset = ctx.getReadset();

      for (int i = 0; i < joins.length; i++) {
         if (joins[i].matches(ctx.getReadset())) {
            Entity leftEntity = joins[i].getLeftEntity();
            Entity rightEntity = joins[i].getRightEntity();

            if (!set.contains(leftEntity) && readset.contains(leftEntity)) {
               sb.append(", ");
               sb.append(getTableList(ctx, leftEntity, set));
            }

            if (!set.contains(rightEntity) && readset.contains(rightEntity)) {
               sb.append(", ");
               sb.append(getTableList(ctx, rightEntity, set));
            }
         }
      }

      if (ctx.getEntity() != entity || readset.matches(set)) {
         return sb.toString();
      } else {
         throw new DalRuntimeException("Invalid readset passed for query");
      }
   }

   private String getTokenForJoins(QueryContext ctx) {
      StringBuffer sb = new StringBuffer(64);

      if (ctx.isQuerySelect()) {
         EntityJoin[] joins = ctx.getEntity().getEntityJoins();
         Set<EntityJoin> set = new HashSet<EntityJoin>();
         boolean first = true;

         for (int i = 0; i < joins.length; i++) {
            if (joins[i].matches(ctx.getReadset())) {
               if (!first) {
                  sb.append(" AND ");
               }

               sb.append(getTableJoins(ctx, joins[i], set));
               first = false;
            }
         }

         if (first) {
            return "1=1";
         } else {
            return sb.toString();
         }
      } else if (ctx.isQueryInsert() || ctx.isQueryUpdate() || ctx.isQueryDelete()) {
         throw new RuntimeException("Can't use <JOINS/> for INSERT/UPDATE/DELETE query");
      } else {
         return toString();
      }
   }

   private String getTableJoins(QueryContext ctx, EntityJoin join, Set<EntityJoin> set) {
      StringBuffer sb = new StringBuffer(64);

      sb.append(join.getLeftEntity().getAlias());
      sb.append('.');
      sb.append(join.getLeftField().getField());
      sb.append(" = ");
      sb.append(join.getRightEntity().getAlias());
      sb.append('.');
      sb.append(join.getRightField().getField());
      set.add(join);

      EntityJoin[] joins;

      joins = join.getLeftEntity().getEntityJoins();
      for (int i = 0; i < joins.length; i++) {
         if (!set.contains(joins[i]) && joins[i].matches(ctx.getReadset())) {
            sb.append(" AND ");

            sb.append(getTableJoins(ctx, joins[i], set));
         }
      }

      joins = join.getRightEntity().getEntityJoins();
      for (int i = 0; i < joins.length; i++) {
         if (!set.contains(joins[i]) && joins[i].matches(ctx.getReadset())) {
            sb.append(" AND ");

            sb.append(getTableJoins(ctx, joins[i], set));
         }
      }

      return sb.toString();
   }

   private String getTokenForFields(QueryContext ctx) {
      StringBuffer sb = new StringBuffer(256);

      if (ctx.isQuerySelect()) { // SELECT
         List<DataRowField> fields = ctx.getReadset().getFields();
         boolean first = true;

         for (DataRowField field : fields) {
            if (!first) {
               sb.append(',');
            }

            first = false;

            if (!field.isComputed()) {
               sb.append(field.getEntity().getAlias()).append('.');
            }

            sb.append(field.getField());

            if (field.hasAlias()) {
               sb.append(' ').append(field.getAlias());
            }

            // add out field for later ResultSet.getString(...)
            ctx.addOutField(field);
         }

         return sb.toString();
      } else if (ctx.isQueryInsert()) { // INSERT
         DataRow protoDo = ctx.getProtoDo();
         List<DataObjectField> fields = protoDo.getMetaData().getFields();
         boolean first = true;

         // For Fields
         sb.append("(");

         for (DataObjectField f : fields) {
            DataRowField field = (DataRowField) f;

            if (!protoDo.isFieldUsed(field) && !field.hasInsertExpr() || field.isComputed() || field.isAutoIncrement()) {
               continue;
            }

            if (!first) {
               sb.append(',');
            } else {
               first = false;
            }

            sb.append(field.getField());
         }
         sb.append(") ");

         // For Values
         first = true;
         sb.append("VALUES (");

         for (DataObjectField f : fields) {
            DataRowField field = (DataRowField) f;

            if (!protoDo.isFieldUsed(field) && !field.hasInsertExpr() || field.isComputed() || field.isAutoIncrement()) {
               continue;
            }

            if (!first) {
               sb.append(',');
            } else {
               first = false;
            }

            if (field.hasInsertExpr()) {
               sb.append(field.getInsertExpr());
            } else {
               sb.append('?');
               ctx.addParameter(new Parameter(field));
            }
         }
         sb.append(")");

         return sb.toString();
      } else if (ctx.isQueryUpdate()) { // UPDATE
         DataRowField[] updateset = ctx.getUpdateset().getFields();
         boolean first = true;

         // For Fields
         for (int i = 0; i < updateset.length; i++) {
            DataRowField field = updateset[i];

            if (!first) {
               sb.append(", ");
            } else {
               first = false;
            }

            sb.append(field.getField());

            if (field.hasUpdateExpr()) {
               sb.append(" = ").append(field.getUpdateExpr());
            } else {
               sb.append(" = ?");
               ctx.addParameter(new Parameter(field));
            }
         }

         return sb.toString();
      } else {
         throw new RuntimeException("Incorrect use for FIELDS tag");
      }
   }

   public String toString() {
      return "<" + getToken() + "/>";
   }
}