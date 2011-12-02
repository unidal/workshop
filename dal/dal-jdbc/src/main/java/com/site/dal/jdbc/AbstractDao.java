package com.site.dal.jdbc;

import org.codehaus.plexus.personality.plexus.lifecycle.phase.Initializable;
import org.codehaus.plexus.personality.plexus.lifecycle.phase.InitializationException;

import com.site.dal.jdbc.entity.EntityInfoManager;
import com.site.dal.jdbc.raw.RawEntity;
import com.site.lookup.ContainerHolder;
import com.site.lookup.annotation.Inject;

public abstract class AbstractDao extends ContainerHolder implements Initializable {
   @Inject
   private QueryEngine m_queryEngine;

   protected QueryEngine getQueryEngine() {
      return m_queryEngine;
   }

   protected abstract Class<?>[] getEntityClasses();

   public void initialize() throws InitializationException {
      m_queryEngine = lookup(QueryEngine.class);

      // register relevant entity class
      EntityInfoManager entityInfoManager = lookup(EntityInfoManager.class);

      entityInfoManager.register(RawEntity.class);

      for (Class<?> entityClass : getEntityClasses()) {
         entityInfoManager.register(entityClass);
      }
   }
}
