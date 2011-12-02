package com.site.kernel.util;

public class LockMonitor {
   private int lock;

   public LockMonitor() {
      lock = 0;
   }

   public synchronized void getSharedLock() {
      while (lock < 0) {
         try {
            wait();
         }
         catch (InterruptedException ie) {
         }
      }

      lock++;
   }

   public synchronized void getExclusiveLock() {
      while (lock != 0) {
         try {
            wait();
         }
         catch (InterruptedException ie) {
         }
      }

      lock = -1;
   }

   public synchronized void releaseLock() {
      if (lock > 0)
         lock--;
      else if (lock < 0)
         lock++;

      if (lock == 0)
         notifyAll();
   }
}