package com.site.game.sanguo.thread.handler.building;

import org.codehaus.plexus.personality.plexus.lifecycle.phase.Initializable;
import org.codehaus.plexus.personality.plexus.lifecycle.phase.InitializationException;

public class SecondaryBuildingPlan extends AbstractBuildingPlan implements Initializable {
   public void initialize() throws InitializationException {
      build("粮仓", 8);
      build("仓库", 8);
      build("中军帐", 5);
      build("别院", 10);
      build("粮仓", 20);
      build("仓库", 20);
      build("兵舍", 3);
      build("校场", 5);
      build("土木司", 10);
      build("集市", 10);
      build("校场", 10);
      build("学馆", 10);
      build("土木司", 20);
      build("兵舍", 10);
      build("集市", 20);
      build("粮仓", 20);
      build("仓库", 20);
   }
}
