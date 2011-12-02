package com.site.app.tracking.analysis;

import javax.servlet.http.HttpServletRequest;

public interface PayloadProvider {
   public Payload getPayload(HttpServletRequest req);
}
