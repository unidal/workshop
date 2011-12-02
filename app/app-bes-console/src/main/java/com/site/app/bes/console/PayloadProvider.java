package com.site.app.bes.console;

import javax.servlet.http.HttpServletRequest;

public interface PayloadProvider {
   public Payload getPayload(HttpServletRequest req);
}
