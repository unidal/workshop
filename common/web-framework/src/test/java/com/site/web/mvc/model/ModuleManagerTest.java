package com.site.web.mvc.model;

import com.site.lookup.ComponentTestCase;
import com.site.web.mvc.ActionContext;
import com.site.web.mvc.annotation.ErrorActionMeta;
import com.site.web.mvc.annotation.InboundActionMeta;
import com.site.web.mvc.annotation.ModuleMeta;
import com.site.web.mvc.annotation.TransitionMeta;

public class ModuleManagerTest extends ComponentTestCase {
   public void testBuild() throws Exception {
      ModelManager manager = lookup(ModelManager.class);
      ModuleModel module = manager.build(TestModule2.class);

      assertEquals("test2", module.getModuleName());
      assertEquals("action1", module.getDefaultInboundActionName());
      assertEquals("default", module.getDefaultErrorActionName());
   }

   public void testRegister() throws Exception {
      ModelManager registry = lookup(ModelManager.class);

      registry.register(TestModule1.class);
      registry.register(TestModule2.class);

      assertEquals("test1", registry.getModule("test1").getModuleName());
      assertEquals("test2", registry.getModule("test2").getModuleName());
      assertNull(registry.getModule("test"));

      try {
         registry.register(TestModule3.class);
         fail("No transition and error methods defined");
      } catch (RuntimeException e) {
         // expected
      }

      try {
         registry.register(TestModule4.class);
         fail("Require transition and error methods defined");
      } catch (RuntimeException e) {
         // expected
      }

      try {
         registry.register(TestModule2Copy.class);
         fail("Can't register two modules with same name.");
      } catch (Exception e) {
         // expected
      }
   }

   @ModuleMeta(name = "test1")
   public static final class TestModule1 {
   }

   @ModuleMeta(name = "test2", defaultInboundAction = "action1", defaultTransition = "default", defaultErrorAction = "default")
   public static final class TestModule2 {
      @TransitionMeta(name = "secondary")
      public void doSecondaryTransition(ActionContext<?> ctx) {
      }

      @TransitionMeta(name = "default")
      public void doTransition(ActionContext<?> ctx) {
      }

      @InboundActionMeta(name = "action1")
      public void inboundAction1(ActionContext<?> ctx) {
      }

      @InboundActionMeta(name = "action2", transition = "secondary", errorAction = "secondary")
      public void inboundAction2(ActionContext<?> ctx) {
      }

      @ErrorActionMeta(name = "default")
      public void onError(ActionContext<?> ctx) {
      }

      @ErrorActionMeta(name = "secondary")
      public void onSecondaryError(ActionContext<?> ctx) {
      }
   }

   @ModuleMeta(name = "test2")
   public static final class TestModule2Copy {
   }

   @ModuleMeta(name = "test3", defaultTransition = "default", defaultErrorAction = "default")
   public static final class TestModule3 {
   }

   @ModuleMeta(name = "test4")
   public static final class TestModule4 {
      @InboundActionMeta(name = "action1")
      public void inboundAction1(ActionContext<?> ctx) {
      }
   }
}
