package com.site.game.sanguo.thread.handler.building;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.site.game.sanguo.model.Build;
import com.site.game.sanguo.thread.ThreadContext;

public abstract class AbstractBuildingPlan implements BuildingPlan {
   private List<BuildItem> m_buildItems = new ArrayList<BuildItem>();

   protected void build(String type, int level) {
      m_buildItems.add(new BuildItem(type, level));
   }

   public Build determineBuilding(ThreadContext ctx) {
      Map<Integer, Build> buildings = ctx.getFarm().getBuildings();
      List<BuildItem> buildItems = getBuildItems(ctx);
      Set<Build> full = new HashSet<Build>();
      Build next = null;

      for (BuildItem item : buildItems) {
         boolean found = false;

         for (Build building : buildings.values()) {
            if (full.contains(building)) {
               continue;
            }

            if (item.getType().getName().equals(building.getTypeName())) {
               if (building.getLevel() < item.getLevel()) { // upgrade
                  next = new Build();
                  next.setResourceId(building.getResourceId());
                  next.setTypeName(building.getTypeName());
                  next.setLevel(building.getLevel() + 1);

                  return next;
               } else if (building.getLevel() == item.getLevel()) {
                  if (building.getLevel() == 20 || building.getLevel() == 10 && building.getTypeName().equals("暗仓")) {
                     full.add(building);
                  }
               }

               found = true;
               break;
            }
         }

         if (!found) { // new
            int id = getAvailableBuildingId(buildings);

            if (id >= 0) {
               next = new Build();
               next.setResourceId(id);
               next.setTypeName(item.getType().getName());
               next.setTypeId(item.getType().getTypeId());
               next.setLevel(0);
               return next;
            }
         }
      }

      return next;
   }

   protected List<BuildItem> getBuildItems(ThreadContext ctx) {
      return m_buildItems;
   }

   protected int getAvailableBuildingId(Map<Integer, Build> buildings) {
      for (Build building : buildings.values()) {
         if (building.getTypeName().equals("工地")) {
            return building.getResourceId();
         }
      }

      // 空间不够,不能建造新的建筑!
      return -1;
   }

   protected static final class BuildItem {
      private BuildType m_type;

      private int m_level;

      public BuildItem(String type, int level) {
         m_type = BuildType.getByName(type);
         m_level = level;
      }

      public int getLevel() {
         return m_level;
      }

      public BuildType getType() {
         return m_type;
      }

      public String toString() {
         return m_type.getName() + "(" + m_level + ")";
      }
   }
}
