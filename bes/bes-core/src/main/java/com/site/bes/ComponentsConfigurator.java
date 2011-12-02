package com.site.bes;

import java.util.ArrayList;
import java.util.List;

import com.site.bes.consumer.DefaultEventConsumerRegistry;
import com.site.bes.consumer.EventConsumerRegistry;
import com.site.bes.engine.DefaultEventDispatcher;
import com.site.bes.engine.DefaultEventEngine;
import com.site.bes.engine.EventDispatcher;
import com.site.bes.engine.EventEngine;
import com.site.bes.queue.DefaultEventQueueManager;
import com.site.bes.queue.DefaultEventTypeRegistry;
import com.site.bes.queue.EventQueue;
import com.site.bes.queue.EventQueueManager;
import com.site.bes.queue.EventTypeRegistry;
import com.site.bes.queue.MemCacheEventQueue;
import com.site.bes.queue.MysqlEventQueue;
import com.site.bes.queue.memcache.Cache;
import com.site.bes.queue.memcache.Formatter;
import com.site.bes.queue.memcache.block.BlockFormatter;
import com.site.bes.queue.memcache.block.BlockManager;
import com.site.bes.queue.memcache.block.DefaultBlockManager;
import com.site.bes.queue.memcache.dashboard.Dashboard;
import com.site.bes.queue.memcache.dashboard.DashboardFormatter;
import com.site.bes.queue.memcache.dashboard.DashboardManager;
import com.site.bes.queue.memcache.dashboard.DefaultDashboard;
import com.site.bes.queue.memcache.dashboard.DefaultDashboardManager;
import com.site.bes.queue.memcache.event.EventFormatter;
import com.site.bes.queue.memcache.lock.DefaultLock;
import com.site.bes.queue.memcache.lock.Lock;
import com.site.bes.queue.memcache.sequence.DefaultSequence;
import com.site.bes.queue.memcache.sequence.Sequence;
import com.site.bes.queue.mysql.EventTransformer;
import com.site.bes.queue.mysql.dal.EventDao;
import com.site.dal.jdbc.QueryEngine;
import com.site.dal.jdbc.mapping.SimpleTableProvider;
import com.site.dal.jdbc.mapping.TableProvider;
import com.site.lookup.configuration.AbstractResourceConfigurator;
import com.site.lookup.configuration.Component;

public class ComponentsConfigurator extends AbstractResourceConfigurator {
   public static void main(String[] args) {
      generatePlexusComponentsXmlFile(new ComponentsConfigurator());
   }

   @Override
   public List<Component> defineComponents() {
      List<Component> all = new ArrayList<Component>();

      all.addAll(getEventComponents());
      all.addAll(getMemcacheComponents());
      all.addAll(getMysqlComponents());

      return all;
   }

   public List<Component> getEventComponents() {
      List<Component> all = new ArrayList<Component>();
      
      all.add(C(EventConsumerRegistry.class, DefaultEventConsumerRegistry.class));
      all.add(C(EventTypeRegistry.class, DefaultEventTypeRegistry.class)
            .config(E("defaultStorageType").value("memcache")));
      all.add(C(EventQueueManager.class, DefaultEventQueueManager.class)
            .req(EventTypeRegistry.class));
      all.add(C(EventEngine.class, DefaultEventEngine.class)
            .req(EventConsumerRegistry.class, EventQueueManager.class));
      all.add(C(EventDispatcher.class, DefaultEventDispatcher.class)
            .is(PER_LOOKUP));
      
      return all;
   }

   public List<Component> getMemcacheComponents() {
      List<Component> all = new ArrayList<Component>();

      all.add(C(EventQueue.class, "memcache", MemCacheEventQueue.class)
               .req(DashboardManager.class, BlockManager.class));
      all.add(C(Cache.class));
      all.add(C(Lock.class, DefaultLock.class).req(Cache.class));

      all.add(C(Sequence.class, "event", DefaultSequence.class).req(Cache.class)
            .config(E("name").value("event")));
      all.add(C(Formatter.class, "event", EventFormatter.class));

      all.add(C(DashboardManager.class, DefaultDashboardManager.class)
            .req(Cache.class));
      all.add(C(Formatter.class, "dashboard", DashboardFormatter.class));
      all.add(C(Dashboard.class, DefaultDashboard.class).is(PER_LOOKUP)
            .req(Formatter.class, "dashboard"));

      all.add(C(BlockManager.class, DefaultBlockManager.class)
            .req(Cache.class, Lock.class)
            .req(Sequence.class, "block", "m_blockSequence")
            .req(Sequence.class, "event", "m_eventSequence")
            .req(Formatter.class, "block", "m_blockFormatter")
            .req(Formatter.class, "event", "m_eventFormatter")
            .is(PER_LOOKUP));
      all.add(C(Sequence.class, "block", DefaultSequence.class)
            .req(Cache.class).config(E("name").value("block")));
      all.add(C(Formatter.class, "block", BlockFormatter.class));

      return all;
   }

   public List<Component> getMysqlComponents() {
      List<Component> all = new ArrayList<Component>();

      all.add(C(EventTransformer.class));

      all.add(C(EventQueue.class, "mysql", MysqlEventQueue.class)
            .req(EventDao.class, EventTransformer.class));
      all.add(C(EventDao.class).req(QueryEngine.class));
      all.add(C(TableProvider.class, "event", SimpleTableProvider.class)
            .config(E("logical-table-name").value("event"),
                  E("physical-table-name").value("event"), 
                  E("data-source-name").value("jdbc-bes")));

      return all;
   }
}
