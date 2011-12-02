package com.ebay.eunit.testfwk.spi.event;

import com.ebay.eunit.testfwk.spi.IClassContext;

public interface IEventListener {
   public void onEvent(IClassContext classContext, Event event);
}
