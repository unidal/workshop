package com.site.game.sanguo.thread.handler.question;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import org.codehaus.plexus.personality.plexus.lifecycle.phase.Initializable;
import org.codehaus.plexus.personality.plexus.lifecycle.phase.InitializationException;

public class NameAnswerer implements Answerer, Initializable {
   private Map<String, String> m_nameToZiMap = new HashMap<String, String>();

   public int answer(String question, String[] choices) {
      String[] parts = question.split(" ");

      if (parts.length > 0) {
         String name = parts[0];
         String zi = m_nameToZiMap.get(name.trim());
         int index = 1;

         for (String choice : choices) {
            if (choice.equals(zi)) {
               return index;
            } else {
               index++;
            }
         }
      }

      return 0;
   }

   public boolean canAnswer(String question) {
      return question.contains("µÄ×ÖÊÇ");
   }

   public void initialize() throws InitializationException {
      try {
         InputStream stream = getClass().getResourceAsStream("names.txt");
         BufferedReader reader = new BufferedReader(new InputStreamReader(stream, "gbk"));

         while (true) {
            String line = reader.readLine();

            if (line == null) {
               break;
            }

            int pos = line.indexOf("×Ö");
            if (pos > 0) {
               String name = line.substring(0, pos);
               String zi = line.substring(pos);

               m_nameToZiMap.put(name, zi);
            }
         }
      } catch (Exception e) {
         throw new InitializationException("Error when reading file names.txt.", e);
      }
   }
}
