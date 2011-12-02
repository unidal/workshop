package com.site.bes.queue.memcache.dashboard;

public interface DashboardManager {
   public Dashboard getDashboard(String eventType, String consumerType);
}
