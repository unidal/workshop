package com.site.bes.queue.memcache.block;

import java.util.Date;

import com.site.bes.queue.memcache.Formatter;
import com.site.lookup.ComponentTestCase;

public class BlockFormatterTest extends ComponentTestCase {
   @SuppressWarnings("unchecked")
   public void testMarshal() throws Exception {
      Formatter<Block, Integer> formatter = lookup(Formatter.class, "block");
      Block block = new Block();

      block.setPreviousId(12344);
      block.setBlockId(12345);
      block.setNextId(12346);
      block.setExpiry(new Date(12346));
      block.getEventIds().add(123);
      block.getEventIds().add(124);
      block.getEventIds().add(125);

      assertEquals("b-12345", formatter.getId(block.getBlockId()));
      assertEquals("12345 12344 12346 12346 123 124 125 ", formatter.getValue(block));
   }

   @SuppressWarnings("unchecked")
   public void testUnmarshal() throws Exception {
      Formatter<Block, Integer> formatter = lookup(Formatter.class, "block");
      Block block = formatter.parseValue("12345 12344 12346 12346 123 124 125 ");

      assertEquals(new Integer(123), block.getEventIds().get(0));
   }
}
