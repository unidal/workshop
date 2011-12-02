package com.site.bes.queue.memcache.block;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Block {
   private int m_blockId;

   private Date m_expiry;

   private int m_previousId;

   private int m_nextId;

   private List<Integer> m_events = new ArrayList<Integer>();

   public int getBlockId() {
      return m_blockId;
   }

   public int getPreviousId() {
      return m_previousId;
   }

   public void setPreviousId(int previousId) {
      m_previousId = previousId;
   }

   public int getNextId() {
      return m_nextId;
   }

   public void setNextId(int nextId) {
      m_nextId = nextId;
   }

   public Date getExpiry() {
      return m_expiry;
   }

   public void setExpiry(Date expiry) {
      m_expiry = expiry;
   }

   public void setBlockId(int blockId) {
      m_blockId = blockId;
   }

   public List<Integer> getEventIds() {
      return m_events;
   }
}
