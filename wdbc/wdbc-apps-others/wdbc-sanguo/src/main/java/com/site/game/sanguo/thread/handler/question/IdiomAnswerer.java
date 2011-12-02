package com.site.game.sanguo.thread.handler.question;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.MessageFormat;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.codehaus.plexus.logging.LogEnabled;
import org.codehaus.plexus.logging.Logger;
import org.codehaus.plexus.personality.plexus.lifecycle.phase.Initializable;
import org.codehaus.plexus.personality.plexus.lifecycle.phase.InitializationException;

public class IdiomAnswerer implements Answerer, LogEnabled, Initializable {
   private Map<String, List<String>> m_idiomToNamesMap = new HashMap<String, List<String>>();

   private Logger m_logger;

   public int answer(String question, String[] choices) {
      String[] parts = question.split(" ");

      if (parts.length > 0) {
         String idiom = parts[0];
         List<String> names = m_idiomToNamesMap.get(idiom);
         int index = 1;

         for (String choice : choices) {
            boolean found = true;

            for (String name : names) {
               if (!choice.contains(name)) {
                  found = false;
                  break;
               }
            }

            if (found) {
               return index;
            } else {
               index++;
            }
         }
      }

      return 0;
   }

   public boolean canAnswer(String question) {
      return question.contains("≥…”Ô");
   }

   public void initialize() throws InitializationException {
      try {
         InputStream stream = getClass().getResourceAsStream("idioms.txt");
         BufferedReader reader = new BufferedReader(new InputStreamReader(stream, "gbk"));
         MessageFormat format = new MessageFormat("{0}({1}){2}");

         while (true) {
            String line = reader.readLine();

            if (line == null) {
               break;
            }

            try {
               Object[] parts = format.parse(line);
               String[] idioms = ((String) parts[0]).split(" ");
               List<String> names = Arrays.asList(((String) parts[1]).split(" "));

               for (String idiom : idioms) {
                  m_idiomToNamesMap.put(idiom, names);
               }
            } catch (Exception e) {
               m_logger.warn("Unparsable idiom: " + line);
            }
         }
      } catch (Exception e) {
         throw new InitializationException("Error when reading file idioms.txt.", e);
      }
   }

   public void enableLogging(Logger logger) {
      m_logger = logger;
   }
}
