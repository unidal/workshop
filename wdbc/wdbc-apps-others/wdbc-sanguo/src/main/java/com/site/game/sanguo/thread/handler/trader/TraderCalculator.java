package com.site.game.sanguo.thread.handler.trader;

import java.util.ArrayList;
import java.util.List;

import com.site.game.sanguo.model.Farm;
import com.site.game.sanguo.model.Price;
import com.site.game.sanguo.model.Stock;
import com.site.game.sanguo.thread.ThreadContext;
import com.site.game.sanguo.thread.handler.ResourceType;

public class TraderCalculator {
   private int m_upHeads = 4;

   private int m_downHeads = 1;

   double calcAverage(ThreadContext ctx, ResourceType type) {
      List<Farm> farms = ctx.getModel().getFarms();
      int len = farms.size();
      Stock[] stocks = new Stock[len];
      double remained = 0;
      int index = 0;

      for (Farm farm : farms) {
         switch (type) {
         case LUMBER:
            remained += farm.getStock().getLumber();
            break;
         case CLAY:
            remained += farm.getStock().getClay();
            break;
         case IRON:
            remained += farm.getStock().getIron();
            break;
         case CROP:
            remained += farm.getStock().getCrop();
            break;
         }

         Stock stock = farm.getStocks().get(type.getName());
         Stock s = new Stock();

         s.setId(stock.getId());
         s.setMax(stock.getMax());
         s.setIncrease(stock.getIncrease());
         s.setSpeed(stock.getSpeed());
         s.setNow(0);
         stocks[index++] = s;
      }

      boolean[] full = new boolean[len];
      double average = 0;
      int fullCount = 0;

      while (remained > 1 && fullCount < len) {
         double a = remained / (len - fullCount);
         index = 0;

         for (Stock s : stocks) {
            if (!full[index]) {
               double limit = calcLimit(s);

               if (a >= limit) {
                  remained -= limit;
                  s.setNow(s.getNow() + limit);
                  full[index] = true;
                  fullCount++;
               } else {
                  remained -= a;
                  s.setNow(s.getNow() + a);
               }
            }

            index++;
         }

         average += a;
      }

      return average;
   }

   double calcLimit(Stock stock) {
      double limit = stock.getMax() - stock.getNow() - m_upHeads * (stock.getIncrease() > 0 ? stock.getIncrease() : 0);

      if (limit > 0) {
         return limit;
      } else {
         return 0;
      }
   }

   double calcDelta(Stock stock, double average) {
      double limit = calcLimit(stock);
      double delta = stock.getNow() - average;

      if (delta + limit < 0) {
         if (limit == 0 && stock.getIncrease() > 0) {
            return m_upHeads * stock.getIncrease() - (stock.getMax() - stock.getNow());
         } else {
            return -limit;
         }
      } else {
         if (stock.getIncrease() < 0) {
            return delta + m_downHeads * stock.getIncrease();
         } else {
            return delta;
         }
      }
   }

   public List<Price> calcDeltas(ThreadContext ctx) {
      Price average = new Price();

      average.setLumber(calcAverage(ctx, ResourceType.LUMBER));
      average.setClay(calcAverage(ctx, ResourceType.CLAY));
      average.setIron(calcAverage(ctx, ResourceType.IRON));
      average.setCrop(calcAverage(ctx, ResourceType.CROP));

      List<Farm> farms = ctx.getModel().getFarms();
      int len = farms.size();
      List<Price> deltas = new ArrayList<Price>(len);

      for (Farm farm : farms) {
         Price delta = new Price();

         for (ResourceType type : ResourceType.values()) {
            Stock stock = farm.getStocks().get(type.getName());

            switch (type) {
            case LUMBER:
               delta.setLumber(calcDelta(stock, average.getLumber()));
               break;
            case CLAY:
               delta.setClay(calcDelta(stock, average.getClay()));
               break;
            case IRON:
               delta.setIron(calcDelta(stock, average.getIron()));
               break;
            case CROP:
               delta.setCrop(calcDelta(stock, average.getCrop()));
               break;
            }
         }

         deltas.add(delta);
      }

      return deltas;
   }

   public double getCapacity(Farm farm) {
      double total = 0;

      for (ResourceType type : ResourceType.values()) {
         Stock stock = farm.getStocks().get(type.getName());

         total += stock.getMax();
      }

      return total;
   }
}
