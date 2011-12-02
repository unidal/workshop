package com.site.game.sanguo.thread.handler.building;

import com.site.game.sanguo.model.Build;
import com.site.game.sanguo.thread.ThreadContext;

public interface BuildingPlan {
   public Build determineBuilding(ThreadContext ctx);
}
