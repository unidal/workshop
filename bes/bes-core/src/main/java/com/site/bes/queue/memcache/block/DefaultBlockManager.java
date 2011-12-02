package com.site.bes.queue.memcache.block;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.codehaus.plexus.logging.LogEnabled;
import org.codehaus.plexus.logging.Logger;
import org.codehaus.plexus.personality.plexus.lifecycle.phase.Initializable;
import org.codehaus.plexus.personality.plexus.lifecycle.phase.InitializationException;

import com.site.bes.Event;
import com.site.bes.queue.memcache.Cache;
import com.site.bes.queue.memcache.Formatter;
import com.site.bes.queue.memcache.lock.Lock;
import com.site.bes.queue.memcache.sequence.Sequence;
import com.site.lookup.ContainerHolder;

public class DefaultBlockManager extends ContainerHolder implements BlockManager, Initializable, LogEnabled {
   private Cache m_cache;

   private Lock m_lock;

   private Sequence m_blockSequence;

   private Sequence m_eventSequence;

   private Formatter<Block, Integer> m_blockFormatter;

   private Formatter<Event, Integer> m_eventFormatter;

   private Block m_head;

   private Block m_tail;

   private Map<Integer, Block> m_blocks = new HashMap<Integer, Block>(256);

   private Logger m_logger;

   private long m_timeout = 100;

   private int m_blockSize = 20;

   public void add(Event event) {
      Date expiry = getExpiry(event);

      if (m_lock.lock(m_timeout)) {
         try {
            Block block = getNextAvailableBlock();
            int eventId = m_eventSequence.getNextValue();

            event.setId(eventId);
            block.getEventIds().add(eventId);

            if (block.getExpiry() == null || block.getExpiry().before(expiry)) {
               block.setExpiry(expiry);
            }

            storeBlock(block);
            storeEvent(event);
         } finally {
            m_lock.unlock();
         }
      } else {
         m_logger.warn(event + " is not added to MemCached.");
      }
   }

   public void enableLogging(Logger logger) {
      m_logger = logger;
   }

   public List<Event> getEvents(int lastEventId) {
      Block block = m_head;

      while (true) {
         if (block.getNextId() > 0) {// not tail
            List<Integer> eventIds = block.getEventIds();
            int len = eventIds.size();

            if (len > 0 && eventIds.get(len - 1) < lastEventId) {
               block = m_blocks.get(block.getNextId());

               if (block == null) {
                  throw new RuntimeException("Internal error happened");
               }

               m_head = block;
               continue;
            }
         }

         if (block.getNextId() == 0) { // tail
            block = loadBlock(block.getBlockId());

            m_head = block;
            m_tail = block;
         }

         List<Event> events = new ArrayList<Event>(m_blockSize);

         for (Integer eventId : block.getEventIds()) {
            if (eventId.intValue() > lastEventId) {
               events.add(loadEvent(eventId.intValue()));
            }
         }

         return events;
      }
   }

   public Date getExpiry(Event event) {
      Calendar cal = Calendar.getInstance();

      if (event.getScheduleDate() != null) {
         cal.setTime(event.getScheduleDate());
      }

      cal.add(Calendar.MINUTE, 30); // 30 minutes timeout
      return cal.getTime();
   }

   private Block getNextAvailableBlock() {
      int blockId = m_tail.getBlockId();
      Block last = null;

      // found the last block node
      while (true) {
         Block block = loadBlock(blockId);

         if (block.getNextId() == 0) {
            last = block;
            break;
         }

         blockId = block.getNextId();
      }

      // reach maximum capacity
      if (last != null && last.getEventIds().size() >= m_blockSize) {
         Block block = new Block();

         block.setBlockId(m_blockSequence.getNextValue());
         m_blocks.put(block.getBlockId(), block);

         // link them
         block.setPreviousId(last.getBlockId());
         last.setNextId(block.getBlockId());

         // persistence
         storeBlock(last);
         last = block;
      }

      return last;
   }

   public void initialize() throws InitializationException {
      if (m_lock.lock(m_timeout)) {
         try {
            Block current = loadBlock(0);
            int blockId;

            m_head = current;
            m_tail = current;

            while ((blockId = current.getNextId()) > 0) {
               Block next = loadBlock(blockId);

               m_blocks.put(blockId, next);
               m_tail = next;
               current = next;
            }
         } finally {
            m_lock.unlock();
         }
      }
   }

   private Block loadBlock(int blockId) {
      String value = m_cache.get(m_blockFormatter.getId(blockId));

      if (value != null) {
         return m_blockFormatter.parseValue(value);
      } else {
         return new Block();
      }
   }

   private Event loadEvent(int eventId) {
      String value = m_cache.get(m_eventFormatter.getId(eventId));

      if (value != null) {
         return m_eventFormatter.parseValue(value);
      } else {
         return new Event();
      }
   }

   public void setBlockSize(int blockSize) {
      m_blockSize = blockSize;
   }

   public void setTimeout(long timeout) {
      m_timeout = timeout;
   }

   private boolean storeBlock(Block block) {
      return m_cache.set(m_blockFormatter.getId(block.getBlockId()), m_blockFormatter.getValue(block));
   }

   private boolean storeEvent(Event event) {
      return m_cache.set(m_eventFormatter.getId(event.getId()), m_eventFormatter.getValue(event));
   }
}
