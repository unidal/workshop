package com.site.kernel.dal.db;

public class EntityRef extends Entity {
   private Entity m_ref;

   public EntityRef(Entity ref, String alias) {
      super(ref.getLogicalName(), alias, ref.getDoClass());

      m_ref = ref;
   }

   public EntityJoin[] getEntityJoins() {
      return m_ref.getEntityJoins();
   }

}
