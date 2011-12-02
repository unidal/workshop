package org.unidal.ezsell.api.ebay.trading;

import org.codehaus.plexus.util.IOUtil;

import com.site.lookup.ComponentTestCase;

public abstract class AbstractTradingTest extends ComponentTestCase {
   protected String getResponse(String resourceName) throws Exception {
      String content = IOUtil.toString(getClass().getResourceAsStream(resourceName));

      assertNotNull("No resource found for " + resourceName, content);

      return content;
   }
}
