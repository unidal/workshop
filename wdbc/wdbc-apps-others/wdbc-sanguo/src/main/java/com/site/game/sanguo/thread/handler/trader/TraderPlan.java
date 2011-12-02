package com.site.game.sanguo.thread.handler.trader;

import java.util.List;

import com.site.game.sanguo.thread.ThreadContext;

public interface TraderPlan {
   public List<TraderTask> getTraderTasks(ThreadContext ctx);
}
