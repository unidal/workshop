package com.site.game.sanguo.thread.handler.building;

import org.codehaus.plexus.personality.plexus.lifecycle.phase.Initializable;
import org.codehaus.plexus.personality.plexus.lifecycle.phase.InitializationException;

public class PrimaryBuildingPlan extends AbstractBuildingPlan implements Initializable {
   public void initialize() throws InitializationException {
      build("中军帐", 1);
      build("兵舍", 3);
      build("集市", 1);
      build("校场", 5);
      build("土木司", 10);
      build("中军帐", 5);
      build("招贤馆", 1);
      build("粮仓", 10);
      build("仓库", 10);
      build("官邸", 10);
      build("土木司", 20);
      build("集市", 15);
      build("学馆", 10);
      build("粮仓", 20);
      build("仓库", 20);
      build("兵器司", 10);
      build("冶铁监", 10);
      build("马场", 10);
      build("斥候营", 10);
      build("校场", 10);
      build("兵器司", 15);
      build("冶铁监", 15);
      build("马场", 15);
      build("斥候营", 15);
      build("校场", 20);
      build("中军帐", 20);
      build("虎贲营", 10);
   }
}
