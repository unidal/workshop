package com.site.dal.jdbc.entity;

import com.site.lookup.ComponentTestCase;
import com.site.test.user.dal.UserEntity;
import com.site.test.user.dal.invalid.DuplicatedEntity;
import com.site.test.user.dal.invalid.NoAnnotatedEntity;

public class EntityManagerTest extends ComponentTestCase {
   public void testRegisterWithAnnotation() throws Exception {
      EntityInfoManager manager = lookup(EntityInfoManager.class);

      manager.register(UserEntity.class);
      assertNotNull(manager.getEntityInfo(UserEntity.class));
      assertNotNull(manager.getEntityInfo("user"));

      try {
         manager.register(DuplicatedEntity.class);

         fail("Logical name should be unique");
      } catch (RuntimeException e) {
         // expected
      }

      try {
         manager.getEntityInfo("unknown");

         fail("Entity should be registered before be used");
      } catch (RuntimeException e) {
         // expected
      }
   }

   public void testRegisterWithoutAnnotation() throws Exception {
      EntityInfoManager manager = lookup(EntityInfoManager.class);

      try {
         manager.register(NoAnnotatedEntity.class);

         fail("Entity should be annotated");
      } catch (RuntimeException e) {
         // expected
      }

   }
}
