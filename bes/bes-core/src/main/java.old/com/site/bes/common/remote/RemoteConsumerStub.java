package com.site.bes.common.remote;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.Date;

import com.site.bes.Event;
import com.site.bes.EventBatch;
import com.site.bes.EventConsumer;
import com.site.bes.EventRetryPolicy;
import com.site.bes.EventState;
import com.site.bes.common.helpers.DefaultEventRetryPolicy;

public class RemoteConsumerStub implements EventConsumer {
   private String m_remoteUrl;

   public EventRetryPolicy getRetryPolicy() {
      return new DefaultEventRetryPolicy(15 * 60);
   }

   public void processEvents(EventBatch batch) {
      if (m_remoteUrl == null) {
         throw new RuntimeException("Parameter(remote-url) is not configured");
      }

      try {
         URLConnection uc = new URL(m_remoteUrl).openConnection();

         if (uc instanceof HttpURLConnection) {
            ((HttpURLConnection) uc).setRequestMethod("POST");
         }

         uc.setDoOutput(true);

         OutputStreamWriter writer = new OutputStreamWriter(uc.getOutputStream(), "UTF-8");
         writer.write(formatEventBatch(batch));
         writer.close();

         BufferedReader reader = new BufferedReader(new InputStreamReader(uc.getInputStream(), "UTF-8"));
         parseResult(batch, reader);
         reader.close();
      } catch (IOException e) {
         e.printStackTrace();
      }
   }

   private void parseResult(EventBatch batch, Reader reader) throws IOException {
      StringBuffer sb = new StringBuffer();
      int eventId = 0;
      int state = 0;
      long nextScheduleDate = 0;
      int part = 0;

      synchronized (sb) {
         while (true) {
            char ch = (char) reader.read();

            switch (ch) {
            case '\t':
               if (sb.length() > 0) {
                  if (part == 0) {
                     eventId = Integer.parseInt(sb.toString());
                  } else if (part == 1) {
                     state = Integer.parseInt(sb.toString());
                  } else if (part == 2) {
                     nextScheduleDate = Long.parseLong(sb.toString());
                  }
               }

               part++;
               sb.setLength(0);
               break;
            case '\n':
               Event event = findEvent(batch, eventId);

               if (nextScheduleDate > 0) {
                  event.setNextScheduleDate(new Date(nextScheduleDate));
               }

               event.setEventState(EventState.getById(state));
               part = 0;
               break;
            default:
               sb.append(ch);
               break;
            }
         }
      }
   }

   private Event findEvent(EventBatch batch, int eventId) {
      int len = batch.length();

      for (int i = 0; i < len; i++) {
         Event event = batch.get(i);

         if (event.getEventId() == eventId) {
            return event;
         }
      }

      return null;
   }

   private String formatEventBatch(EventBatch batch) {
      StringBuffer sb = new StringBuffer(1024);

      synchronized (sb) {
         int len = batch.length();

         for (int i = 0; i < len; i++) {
            Event event = batch.get(i);

            sb.append(event.getEventId()).append('\t');
            sb.append(event.getEventType()).append('\t');
            sb.append(event.getMaxRetryTimes()).append('\t');
            sb.append(event.getRetriedTimes()).append('\t');
            sb.append(escape(event.getRefId())).append('\t');
            sb.append(escape(event.getProducerId())).append('\t');
            sb.append(escape(event.getProducerType())).append('\t');
            sb.append(event.getCreationDate().getTime()).append('\t');
            sb.append(escape(event.getStringPayload())).append('\t');
            sb.append('\n');
         }
      }

      return sb.toString();
   }

   private String escape(String str) {
      int len = str == null ? 0 : str.length();
      StringBuffer sb = new StringBuffer(2 * len);

      synchronized (sb) {
         for (int i = 0; i < len; i++) {
            char ch = str.charAt(i);

            if (ch == '\t') {
               sb.append("\\t");
            } else if (ch == '\n') {
               sb.append("\\n");
            } else if (ch == '\\') {
               sb.append("\\\\");
            } else {
               sb.append(ch);
            }
         }
      }

      return sb.toString();
   }

   public void setRemoteUrl(String remoteUrl) {
      m_remoteUrl = remoteUrl;
   }
}
