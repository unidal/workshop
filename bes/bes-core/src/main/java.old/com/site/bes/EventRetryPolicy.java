package com.site.bes;

import java.util.Date;

public interface EventRetryPolicy {
   public Date getNextScheduleDate(Event<?> event);
}
