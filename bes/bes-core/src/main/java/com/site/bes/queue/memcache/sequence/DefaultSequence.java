package com.site.bes.queue.memcache.sequence;

import org.codehaus.plexus.personality.plexus.lifecycle.phase.Initializable;
import org.codehaus.plexus.personality.plexus.lifecycle.phase.InitializationException;

import com.site.bes.queue.memcache.Cache;

public class DefaultSequence implements Sequence, Initializable {
   private Cache m_client;

   private String m_name;

   private String m_id;

   public void setName(String name) {
      m_name = name;
      m_id = "s-" + name;
   }

   public int getCurrentValue() {
      return m_client.getCounter(m_id);
   }

   public String getId() {
      return m_id;
   }

   public String getName() {
      return m_name;
   }

   public int getNextValue() {
      return m_client.addOrIncr(m_id, 1);
   }

   public String getValue() {
      throw new UnsupportedOperationException();
   }

   public void initialize() throws InitializationException {
      m_client.addOrIncr(m_id, 0);
   }
}
