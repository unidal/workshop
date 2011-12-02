package com.site.bes.queue.memcache.sequence;

import com.site.lookup.ComponentTestCase;

public class SequenceTest extends ComponentTestCase {
   public void testEventSequence() throws Exception {
      Sequence seq = lookup(Sequence.class, "event");

      int value = seq.getCurrentValue();

      assertEquals(value + 1, seq.getNextValue());
      assertEquals(value + 1, seq.getCurrentValue());
      assertEquals(value + 2, seq.getNextValue());
   }

   public void testBlockSequence() throws Exception {
      Sequence seq = lookup(Sequence.class, "block");

      int value = seq.getCurrentValue();

      assertEquals(value + 1, seq.getNextValue());
      assertEquals(value + 1, seq.getCurrentValue());
      assertEquals(value + 2, seq.getNextValue());
   }
}
