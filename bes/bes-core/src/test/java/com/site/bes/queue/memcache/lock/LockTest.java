package com.site.bes.queue.memcache.lock;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import com.site.lookup.ComponentTestCase;

public class LockTest extends ComponentTestCase {
   @Override
   protected void setUp() throws Exception {
      super.setUp();

      // clear lock for any failure case
      Lock lock = lookup(Lock.class);

      lock.unlock();
   }

   public void testSingleThread() throws Exception {
      final Lock lock = lookup(Lock.class);

      assertTrue(!lock.isLocked());
      assertTrue(lock.lock(100));
      assertTrue(lock.isLocked());
      assertTrue(lock.unlock());
      assertTrue(!lock.isLocked());
   }

   public void testMultiThread() throws Exception {
      final Lock lock = lookup(Lock.class);
      final int threads = 10;
      final long timeout = 50;
      ExecutorService service = Executors.newFixedThreadPool(threads);
      final StringBuilder out = new StringBuilder(1024);

      for (int i = 0; i < threads; i++) {
         final int index = i;

         service.submit(new Runnable() {
            public void run() {
               long start = System.currentTimeMillis();

               lock.lock(threads * timeout);

               try {
                  Thread.sleep(timeout);
               } catch (Exception e) {
               }

               lock.unlock();

               out.append("Thread ").append(index).append(": ");
               out.append(System.currentTimeMillis() - start).append(" ms\r\n");
            }
         });
      }

      service.shutdown();

      boolean isTimeout = !service.awaitTermination(threads * timeout + 100, TimeUnit.MILLISECONDS);

      System.out.println(out);

      if (isTimeout) {
         fail("Timeout happened\r\n" + out);
      }
   }
}
