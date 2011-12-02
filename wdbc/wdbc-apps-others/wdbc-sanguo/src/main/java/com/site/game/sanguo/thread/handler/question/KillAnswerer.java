package com.site.game.sanguo.thread.handler.question;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import org.codehaus.plexus.personality.plexus.lifecycle.phase.Initializable;
import org.codehaus.plexus.personality.plexus.lifecycle.phase.InitializationException;

public class KillAnswerer implements Answerer, Initializable {
   private Map<String, String> m_nameToKilledByMap = new HashMap<String, String>();

   public int answer(String question, String[] choices) {
      String[] parts = question.split(" ");

      if (parts.length > 0) {
         String name = parts[0];
         String killedBy = m_nameToKilledByMap.get(name);
         int index = 1;

         for (String choice : choices) {
            if (choice.equals(killedBy)) {
               return index;
            } else {
               index++;
            }
         }
      }

      return 0;
   }

   public boolean canAnswer(String question) {
      return question.contains("被何人所杀") || question.contains("被何人所擒");
   }

   public void initialize() throws InitializationException {
      try {
         InputStream stream = getClass().getResourceAsStream("kills.txt");
         BufferedReader reader = new BufferedReader(new InputStreamReader(stream, "gbk"));
         String killedBy = null;

         while (true) {
            String line = reader.readLine();

            if (line == null) {
               break;
            }

            if (line.length() == 0) {
               killedBy = null;
            } else if (killedBy == null) {
               killedBy = line;
            } else {
               int pos = line.indexOf(":");

               if (pos > 0) {
                  String[] names = line.substring(pos + 1).split(" ");

                  for (String name : names) {
                     m_nameToKilledByMap.put(name, killedBy);
                  }
               }
            }
         }
      } catch (Exception e) {
         throw new InitializationException("Error when reading file kills.txt.", e);
      }
   }
}
