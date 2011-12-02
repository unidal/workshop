package com.site.game.sanguo.thread.handler.building;

import java.io.BufferedReader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.codehaus.plexus.personality.plexus.lifecycle.phase.Initializable;
import org.codehaus.plexus.personality.plexus.lifecycle.phase.InitializationException;

import com.site.game.sanguo.Configuration;
import com.site.game.sanguo.thread.ThreadContext;

public class CustomizedBuildingPlan extends AbstractBuildingPlan implements Initializable {
   private Configuration m_configuration;

   private Map<String, List<BuildItem>> m_buildItemMap = new HashMap<String, List<BuildItem>>();

   @Override
   protected List<BuildItem> getBuildItems(ThreadContext ctx) {
      String villageName = ctx.getGame().getVillage().getName();
      List<BuildItem> buildItems = m_buildItemMap.get(villageName);

      if (buildItems == null) {
         return Collections.emptyList();
      } else {
         return buildItems;
      }
   }

   /**
    * 东北岙=>中军帐:1,兵舍:3
    * 
    * 北岙=>中军帐:1,兵舍:5
    */
   public void initialize() throws InitializationException {
      String plan = m_configuration.getBuildPlan().trim();
      BufferedReader reader = new BufferedReader(new StringReader(plan));

      try {
         while (true) {
            String line = reader.readLine();

            if (line == null) {
               break;
            }

            int pos = line.indexOf("=>");

            if (pos > 0) {
               String villageName = line.substring(0, pos);
               String[] parts = line.substring(pos + 2).split(",");
               List<BuildItem> buildItems = new ArrayList<BuildItem>();

               for (String part : parts) {
                  int index = part.indexOf(':');

                  if (index > 0) {
                     String build = part.substring(0, index).trim();
                     String level = part.substring(index + 1).trim();

                     buildItems.add(new BuildItem(build, Integer.parseInt(level)));
                  }
               }

               m_buildItemMap.put(villageName, buildItems);
            }
         }
      } catch (Exception e) {
         throw new InitializationException("Error when initializing CustomizedBuildingPlan. Invalid building plan: "
               + plan, e);
      }
   }
}
