package com.site.kernel.dal.db;

import com.site.kernel.dal.Cardinality;
import com.site.kernel.dal.DataObjectField;
import com.site.kernel.dal.ValueType;

/**
 * @author qwu
 */
public class DataRowField extends DataObjectField {
   public static final boolean NOT_NULL = false; // Not nullable

   public static final boolean NULL = true; // Nullable

   private String m_alias;

   private boolean m_autoIncrement;

   private boolean m_computed;

   private int m_decimal;

   private String m_field;

   private String m_insertExpr;

   private int m_length;

   private boolean m_nullable;

   private String m_selectExpr;

   private Entity m_entity;

   private String m_updateExpr;

   private Cardinality m_cardinality;

   // Define Var
   public DataRowField(Entity entity, String name, ValueType valueType) {
      this(entity, name, valueType, null, NULL, 0, 0, null, null, null, null);
   }

   // Define member
   public DataRowField(Entity entity, String name, ValueType valueType, String field, boolean nullable, int length, int decimal, String alias) {
      this(entity, name, valueType, field, nullable, length, decimal, alias, null, null, null);
   }

   public DataRowField(Entity entity, String name, ValueType valueType, String field, boolean nullable, int length, int decimal, String alias, boolean autoIncrement) {
      this(entity, name, valueType, field, nullable, length, decimal, alias, null, null, null);

      m_autoIncrement = autoIncrement;
   }

   public DataRowField(Entity entity, String name, ValueType valueType, String field, boolean nullable, int length, int decimal, String alias, String selectExpr,
         String insertExpr, String updateExpr) {
      super(name, valueType);

      m_entity = entity;
      m_field = field;
      m_nullable = nullable;
      m_length = length;
      m_decimal = decimal;
      m_alias = alias;
      m_selectExpr = selectExpr;
      m_insertExpr = insertExpr;
      m_updateExpr = updateExpr;
      m_computed = (selectExpr != null && selectExpr.length() > 0);

      if (m_computed && (m_insertExpr != null || m_updateExpr != null)) {
         throw new RuntimeException("Attribute computed can not co-exist with attribute insert-expr or update-expr in " + this);
      }
   }

   // Define sub-object
   public DataRowField(Entity entity, String name, Cardinality cardinality) {
      super(name, ValueType.SUBOBJECT);

      m_entity = entity;
      m_cardinality = cardinality;
   }

   public String getAlias() {
      return m_alias;
   }

   public Cardinality getCardinality() {
      return m_cardinality;
   }

   public int getDecimal() {
      return m_decimal;
   }

   public String getField() {
      return m_field;
   }

   public String getInsertExpr() {
      return m_insertExpr;
   }

   public int getLength() {
      return m_length;
   }

   public String getSelectExpr() {
      return m_selectExpr;
   }

   public Entity getEntity() {
      return m_entity;
   }

   public String getUpdateExpr() {
      return m_updateExpr;
   }

   public boolean hasAlias() {
      return m_alias != null && m_alias.length() > 0;
   }

   public boolean hasInsertExpr() {
      return m_insertExpr != null && m_insertExpr.length() > 0;
   }

   public boolean hasUpdateExpr() {
      return m_updateExpr != null && m_updateExpr.length() > 0;
   }

   public boolean isAutoIncrement() {
      return m_autoIncrement;
   }

   public boolean isComputed() {
      return m_computed;
   }

   public boolean isField() {
      return m_entity != null && m_field != null;
   }

   public boolean isNullable() {
      return m_nullable;
   }

   public boolean isSubObject() {
      return getValueType() == ValueType.SUBOBJECT;
   }

   public String toString() {
      StringBuffer sb = new StringBuffer(256);

      sb.append("DataRowField[");

      sb.append("entity=").append((m_entity == null ? null : m_entity.getLogicalName()));
      sb.append(",name=").append(getName());
      sb.append(",valueType=").append(getValueType());

      if (isSubObject()) {
         sb.append(",cardinality=").append(m_cardinality);
      } else {
         sb.append(",field=").append(m_field);
         sb.append(",nullable=").append(m_nullable);

         if (m_length > 0) {
            sb.append(",length=").append(m_length);
            sb.append(",decimal=").append(m_decimal);
         }

         sb.append(",index=").append(getIndex());
         sb.append(",computed=").append(m_computed);
         sb.append(",alias=").append(m_alias);
         sb.append(",select-expr=").append(m_selectExpr);
         sb.append(",insert-expr=").append(m_insertExpr);
         sb.append(",update-expr=").append(m_updateExpr);
      }

      sb.append("]");

      return sb.toString();
   }
}
