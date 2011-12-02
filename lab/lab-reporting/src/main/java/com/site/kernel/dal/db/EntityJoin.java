package com.site.kernel.dal.db;

import com.site.kernel.dal.Cardinality;

/**
 * @author qwu
 */
public class EntityJoin {
   private Entity m_leftEntity;

   private DataRowField m_leftField;

   private String m_leftAlias;

   private Entity m_rightEntity;

   private DataRowField m_rightField;

   private String m_rightAlias;

   private DataRowField m_subobjectField;

   private Cardinality m_cardinality;

   public EntityJoin(DataRowField leftField, String leftAlias, DataRowField rightField, String rightAlias, DataRowField subobjectField) {
      m_leftField = leftField;
      m_leftEntity = leftField.getEntity();
      m_leftAlias = leftAlias;
      m_rightField = rightField;
      m_rightEntity = rightField.getEntity();
      m_rightAlias = rightAlias;
      m_subobjectField = subobjectField;
      m_cardinality = subobjectField.getCardinality();
   }

   public boolean equals(Object obj) {
      if (obj instanceof EntityJoin) {
         EntityJoin join = (EntityJoin) obj;

         return m_leftEntity.equals(join.getLeftEntity()) && m_leftField.equals(join.getLeftField()) && m_leftAlias.equals(join.getLeftAlias())
               && m_rightEntity.equals(join.getRightEntity()) && m_rightField.equals(join.getRightField()) && m_rightAlias.equals(join.getRightAlias());
      } else {
         return false;
      }
   }

   public Cardinality getCardinality() {
      return m_cardinality;
   }

   public String getLeftAlias() {
      return m_leftAlias;
   }

   public Entity getLeftEntity() {
      return m_leftEntity;
   }

   public DataRowField getLeftField() {
      return m_leftField;
   }

   public String getRightAlias() {
      return m_rightAlias;
   }

   public Entity getRightEntity() {
      return m_rightEntity;
   }

   public DataRowField getRightField() {
      return m_rightField;
   }
   
   public DataRowField getSubObjectField() {
      return m_subobjectField;
   }

   public int hashCode() {
      int hashCode = 0;

      hashCode = hashCode * 31 + m_leftEntity.hashCode();
      hashCode = hashCode * 31 + m_leftField.hashCode();
      hashCode = hashCode * 31 + m_leftAlias.hashCode();
      hashCode = hashCode * 31 + m_rightEntity.hashCode();
      hashCode = hashCode * 31 + m_rightField.hashCode();
      hashCode = hashCode * 31 + m_rightAlias.hashCode();

      return hashCode;
   }

   public boolean matches(Readset readset) {
      return readset.contains(m_leftEntity) && readset.contains(m_rightEntity);
   }

   public String toString() {
      StringBuffer sb = new StringBuffer(64);

      sb.append("EntityJoin[");
      sb.append(m_leftAlias).append('.').append(m_leftField.getName());
      sb.append(',');
      sb.append(m_rightAlias).append('.').append(m_rightField.getName());
      sb.append(',');
      sb.append(m_subobjectField.getName());
      sb.append(',');
      sb.append(m_cardinality.getName());
      sb.append("]");

      return sb.toString();
   }
}
