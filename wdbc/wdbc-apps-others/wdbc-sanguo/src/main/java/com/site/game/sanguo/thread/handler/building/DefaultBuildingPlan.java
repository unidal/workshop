package com.site.game.sanguo.thread.handler.building;

import com.site.game.sanguo.model.Build;
import com.site.game.sanguo.thread.ThreadContext;

public class DefaultBuildingPlan implements BuildingPlan {
   private BuildingPlan m_primary;

   private BuildingPlan m_secondary;

   private BuildingPlan m_customized;

   public Build determineBuilding(ThreadContext ctx) {
      Build build = m_customized.determineBuilding(ctx);

      if (build != null) {
         return build;
      } else if (ctx.getFarm().isMain()) {
         return m_primary.determineBuilding(ctx);
      } else {
         return m_secondary.determineBuilding(ctx);
      }
   }
}
