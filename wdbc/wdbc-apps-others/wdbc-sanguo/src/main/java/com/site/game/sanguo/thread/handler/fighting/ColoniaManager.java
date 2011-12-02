package com.site.game.sanguo.thread.handler.fighting;

import java.util.List;

import com.site.game.sanguo.model.Colonia;
import com.site.game.sanguo.thread.ThreadContext;

public interface ColoniaManager {
   public List<Colonia> getColonias(ThreadContext ctx);
}
