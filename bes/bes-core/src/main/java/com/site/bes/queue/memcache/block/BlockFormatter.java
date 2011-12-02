package com.site.bes.queue.memcache.block;

import java.util.Date;
import java.util.List;

import com.site.bes.queue.memcache.Formatter;
import com.site.bes.queue.memcache.FormatterException;
import com.site.lookup.ContainerHolder;

public class BlockFormatter extends ContainerHolder implements Formatter<Block, Integer> {
   public String getId(Integer blockId) throws FormatterException {
      return "b-" + blockId;
   }

   public String getValue(Block block) throws FormatterException {
      StringBuilder sb = new StringBuilder(4096);

      sb.append(block.getBlockId()).append(' ');
      sb.append(block.getPreviousId()).append(' ');
      sb.append(block.getNextId()).append(' ');
      sb.append(block.getExpiry() == null ? 0 : block.getExpiry().getTime()).append(' ');

      for (Integer eventId : block.getEventIds()) {
         sb.append(eventId).append(' ');
      }

      return sb.toString();
   }

   public Block parseValue(String value) throws FormatterException {
      Block block = new Block();
      List<Integer> eventIds = block.getEventIds();
      String[] parts = value.split(" ");
      int index = 0;

      try {
         block.setBlockId(Integer.parseInt(parts[index++]));
         block.setPreviousId(Integer.parseInt(parts[index++]));
         block.setNextId(Integer.parseInt(parts[index++]));

         long expiry = Long.parseLong(parts[index++]);

         block.setExpiry(expiry == 0 ? null : new Date(expiry));

         while (index < parts.length) {
            eventIds.add(Integer.parseInt(parts[index++]));
         }
      } catch (Exception e) {
         throw new FormatterException("Error when parsing block: " + value, e);
      }

      return block;
   }
}
