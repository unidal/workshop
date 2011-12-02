package com.site.app.tracking.counter;

public interface PayloadProvider {
   public Payload getPayload(String referer, String queryString);
}
