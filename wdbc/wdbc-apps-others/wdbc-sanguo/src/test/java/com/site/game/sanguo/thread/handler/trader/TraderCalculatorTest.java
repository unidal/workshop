package com.site.game.sanguo.thread.handler.trader;

import com.site.game.sanguo.model.Stock;
import com.site.lookup.ComponentTestCase;

public class TraderCalculatorTest extends ComponentTestCase {
   public void testCalcLimit() throws Exception {
      TraderCalculator c = lookup(TraderCalculator.class);

      assertEquals(5000d, c.calcLimit(newStock(5000, 0, 0)));
      assertEquals(3000d, c.calcLimit(newStock(5000, 0, 500)));
      assertEquals(1000d, c.calcLimit(newStock(5000, 2000, 500)));
      assertEquals(4000d, c.calcLimit(newStock(5000, 1000, -500)));
      assertEquals(1000d, c.calcLimit(newStock(5000, 2000, 500)));
      assertEquals(0d, c.calcLimit(newStock(5000, 4600, 500)));
      assertEquals(400d, c.calcLimit(newStock(5000, 4600, -500)));
      assertEquals(0d, c.calcLimit(newStock(5000, 5000, 500)));
      assertEquals(0d, c.calcLimit(newStock(5000, 4000, 500)));
   }

   public void testCalcDelta() throws Exception {
      TraderCalculator c = lookup(TraderCalculator.class);

      assertEquals(-3000d, c.calcDelta(newStock(5000, 0, 0), 3000));
      assertEquals(-3000d, c.calcDelta(newStock(5000, 0, 500), 3000));
      assertEquals(-1000d, c.calcDelta(newStock(5000, 2000, 500), 3000));
      assertEquals(-2500d, c.calcDelta(newStock(5000, 1000, -500), 3000));
      assertEquals(-1000d, c.calcDelta(newStock(5000, 2000, 500), 3000));
      assertEquals(1600d, c.calcDelta(newStock(5000, 4600, 500), 3000));
      assertEquals(1100d, c.calcDelta(newStock(5000, 4600, -500), 3000));
      assertEquals(2000d, c.calcDelta(newStock(5000, 5000, 500), 3000));
      assertEquals(1000d, c.calcDelta(newStock(5000, 4000, 500), 8000));
   }

   private Stock newStock(double max, double now, double increase) {
      Stock stock = new Stock();

      stock.setMax(max);
      stock.setNow(now);
      stock.setIncrease(increase);

      return stock;
   }
}
