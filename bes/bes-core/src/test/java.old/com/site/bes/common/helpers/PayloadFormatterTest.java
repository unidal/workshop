package com.site.bes.common.helpers;

import java.util.Date;
import java.util.Stack;

import com.site.bes.EventPayloadFormat;
import com.site.bes.Module;
import com.site.kernel.SystemRegistry;
import com.site.kernel.common.BaseTestCase;
import com.site.kernel.initialization.BaseModule;
import com.site.kernel.initialization.ModuleContext;

public class PayloadFormatterTest extends BaseTestCase {
   private static final boolean DEBUG = false;

   @Override
   protected void setUp() throws Exception {
      Module.FULL.doInitialization(new ModuleContext(), new Stack<BaseModule>(), false);
   }

   public void testPropertiesPayloadFormatter() {
      if (DEBUG) {
         printMethodHeader();
      }

      SamplePayload payload = createSamplePayload();
      EventPayloadFormatter formatter = getFormatter(EventPayloadFormat.PROPERTIES);
      String result = formatter.format(payload);

      if (DEBUG) {
         out(result);
      }

      SamplePayload another = new SamplePayload();

      formatter.parse(another, result);
      compareSamplePayloads(payload, another);
   }

   public void testXmlPayloadFormatter() {
      if (DEBUG) {
         printMethodHeader();
      }

      SamplePayload payload = createSamplePayload();
      EventPayloadFormatter formatter = getFormatter(EventPayloadFormat.XML);
      String result = formatter.format(payload);

      if (DEBUG) {
         out(result);
      }

      SamplePayload another = new SamplePayload();

      formatter.parse(another, result);
      compareSamplePayloads(payload, another);
   }

   public void testUrlPayloadFormatter() {
      if (DEBUG) {
         printMethodHeader();
      }

      SamplePayload payload = createSamplePayload();
      EventPayloadFormatter formatter = getFormatter(EventPayloadFormat.URL);
      String result = formatter.format(payload);

      if (DEBUG) {
         out(result);
      }

      SamplePayload another = new SamplePayload();

      formatter.parse(another, result);
      compareSamplePayloads(payload, another);
   }

   private void compareSamplePayloads(SamplePayload op, SamplePayload np) {
      assertEquals(op.getId(), np.getId());
      assertEquals(op.getArea(), np.getArea());
      assertEquals(op.getContent(), np.getContent());
      assertEquals(op.getExpiry(), np.getExpiry());
      assertEquals(op.getEmail(), np.getEmail());
      assertEquals(op.getTitle(), np.getTitle());
      assertEquals(op.getUserId(), np.getUserId());
   }

   private SamplePayload createSamplePayload() {
      SamplePayload payload = new SamplePayload();

      payload.setId(21);
      payload.setArea("上海");
      payload.setContent("it's content");
      payload.setExpiry(new Date());
      payload.setEmail("test@ab.com");
      payload.setTitle("Engineer & Manager");
      payload.setUserId(12345);

      return payload;
   }

   private EventPayloadFormatter getFormatter(EventPayloadFormat format) {
      return (EventPayloadFormatter) SystemRegistry.newInstance(EventPayloadFormatter.class, format);
   }
}
