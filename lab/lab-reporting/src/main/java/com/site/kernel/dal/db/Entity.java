package com.site.kernel.dal.db;

import java.util.ArrayList;
import java.util.List;

/**
 * @author qwu
 */
public abstract class Entity {
   // Instance variable
   private String m_logicalName;

   private String m_alias;

   private Class m_doClass;

   // Meta info
   private List<Class> m_doClasses;

   public Entity(String logicalName, String alias, Class doClass) {
      m_logicalName = logicalName;
      m_alias = alias;
      m_doClass = doClass;
   }

   public boolean equals(Object obj) {
      if (obj instanceof Entity) {
         Entity entity = (Entity) obj;

         return m_logicalName.equals(entity.getLogicalName()) && m_alias.equals(entity.getAlias());
      } else {
         return false;
      }
   }

   public String getAlias() {
      return m_alias;
   }

   public Class getDoClass() {
      return m_doClass;
   }

   public List<Class> getDoClasses() {
      synchronized (this) {
         if (m_doClasses == null) {
            List<Class> doClasses = new ArrayList<Class>(3);

            doClasses.add(getDoClass());

            EntityJoin[] joins = getEntityJoins();
            int len = (joins == null ? 0 : joins.length);

            for (int i = 0; i < len; i++) {
               EntityJoin join = joins[i];
               Class leftDoClass = join.getLeftEntity().getDoClass();
               Class rightDoClass = join.getRightEntity().getDoClass();

               if (!doClasses.contains(leftDoClass)) {
                  doClasses.add(leftDoClass);
               }

               if (!doClasses.contains(rightDoClass)) {
                  doClasses.add(rightDoClass);
               }
            }

            m_doClasses = doClasses;
         }
      }

      return m_doClasses;
   }

   public EntityJoin[] getEntityJoins() {
      return new EntityJoin[0];
   }

   public String getLogicalName() {
      return m_logicalName;
   }

   public int hashCode() {
      return m_logicalName.hashCode() * 31 + m_alias.hashCode();
   }
}
