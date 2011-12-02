package com.site.game.sanguo.thread.handler.trader;

import java.util.ArrayList;
import java.util.List;

import com.site.game.sanguo.model.Farm;
import com.site.game.sanguo.model.Price;
import com.site.game.sanguo.thread.ThreadContext;
import com.site.game.sanguo.thread.handler.ResourceType;

public class DefaultTraderPlan implements TraderPlan {
   private TraderCalculator m_calculator;

   public List<TraderTask> getTraderTasks(ThreadContext ctx) {
      List<Price> deltas = m_calculator.calcDeltas(ctx);
      List<TraderTask> tasks = getTraderTasks(ctx, deltas);

      mergeTasks(tasks);
      filterTasks(tasks);

      return tasks;
   }

   private void filterTasks(List<TraderTask> tasks) {
      int len = tasks.size();

      for (int i = len - 1; i >= 0; i--) {
         TraderTask t = tasks.get(i);
         int total = t.getLumber() + t.getClay() + t.getIron() + t.getCrop();

         if (total < 3000) {
            double capacity = m_calculator.getCapacity(t.getToFarm());

            if (total < capacity / 2) {
               tasks.remove(i);
            }
         }
      }
   }

   private List<TraderTask> getTraderTasks(ThreadContext ctx, List<Price> deltas) {
      List<TraderTask> tasks = new ArrayList<TraderTask>();

      for (ResourceType type : ResourceType.values()) {
         tasks.addAll(getTraderTasks(ctx, deltas, type));
      }

      return tasks;
   }

   private List<TraderTask> getTraderTasks(ThreadContext ctx, List<Price> deltas, ResourceType type) {
      List<Farm> farms = ctx.getModel().getFarms();
      List<TraderTask> tasks = new ArrayList<TraderTask>();
      int len = deltas.size();

      for (int i = 0; i < len; i++) {
         Price di = deltas.get(i);
         double vi = getValue(di, type);

         for (int j = 0; j < len; j++) {
            Price dj = deltas.get(j);
            double vj = getValue(dj, type);

            if (j == i || Math.abs(vj) < 0.1) {
               continue;
            }

            if (vi > 0 && vj < 0) {
               TraderTask task = new TraderTask();
               double value = Math.min(vi, -vj);

               task.setFromFarm(farms.get(i));
               task.setToFarm(farms.get(j));
               setValue(task, di, dj, value, type);
               vi -= value;
               vj += value;
               tasks.add(task);
            } else if (vi < 0 && vj > 0) {
               TraderTask task = new TraderTask();
               double value = Math.min(vj, -vi);

               task.setFromFarm(farms.get(j));
               task.setToFarm(farms.get(i));
               setValue(task, dj, di, value, type);
               vj -= value;
               vi += value;
               tasks.add(task);
            }

            if (Math.abs(vi) < 0.1) {
               break;
            }
         }
      }

      return tasks;
   }

   private double getValue(Price price, ResourceType type) {
      switch (type) {
      case LUMBER:
         return price.getLumber();
      case CLAY:
         return price.getClay();
      case IRON:
         return price.getIron();
      case CROP:
         return price.getCrop();
      }

      return 0;
   }

   private void mergeTasks(List<TraderTask> tasks) {
      int len = tasks.size();

      for (int i = len - 1; i >= 0; i--) {
         TraderTask ti = tasks.get(i);

         if (ti == null) {
            continue;
         }

         for (int j = i - 1; j >= 0; j--) {
            TraderTask tj = tasks.get(j);

            if (tj != null && ti.getFromFarm() == tj.getFromFarm() && ti.getToFarm() == tj.getToFarm()) {
               ti.setLumber(ti.getLumber() + tj.getLumber());
               ti.setClay(ti.getClay() + tj.getClay());
               ti.setIron(ti.getIron() + tj.getIron());
               ti.setCrop(ti.getCrop() + tj.getCrop());

               tasks.set(j, null);
            }
         }
      }

      for (int i = len - 1; i >= 0; i--) {
         if (tasks.get(i) == null) {
            tasks.remove(i);
         }
      }
   }

   private void setValue(TraderTask task, Price from, Price to, double value, ResourceType type) {
      switch (type) {
      case LUMBER:
         task.setLumber((int) value);
         from.setLumber(from.getLumber() - value);
         to.setLumber(to.getLumber() + value);
         break;
      case CLAY:
         task.setClay((int) value);
         from.setClay(from.getClay() - value);
         to.setClay(to.getClay() + value);
         break;
      case IRON:
         task.setIron((int) value);
         from.setIron(from.getIron() - value);
         to.setIron(to.getIron() + value);
         break;
      case CROP:
         task.setCrop((int) value);
         from.setCrop(from.getCrop() - value);
         to.setCrop(to.getCrop() + value);
         break;
      }
   }
}
