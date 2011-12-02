package com.site.game.sanguo.thread.handler.question;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import org.codehaus.plexus.personality.plexus.lifecycle.phase.Initializable;
import org.codehaus.plexus.personality.plexus.lifecycle.phase.InitializationException;

public class MiscAnswerer implements Answerer, Initializable {
   private Map<String, String> m_wordMap = new HashMap<String, String>();

   public int answer(String question, String[] choices) {
      String answer = m_wordMap.get(question);
      int index = 1;

      if (answer != null) {
         for (String choice : choices) {
            if (answer.equals(choice)) {
               return index;
            }

            index++;
         }
      }

      return 0;
   }

   public boolean canAnswer(String question) {
      return true;
   }

   public void initialize() throws InitializationException {
      try {
         InputStream stream = getClass().getResourceAsStream("misc.txt");
         BufferedReader reader = new BufferedReader(new InputStreamReader(stream, "gbk"));

         while (true) {
            String line = reader.readLine();

            if (line == null) {
               break;
            }

            int pos = line.indexOf(")");
            if (pos == 3) {
               String question = line.substring(pos + 1);
               String answer = reader.readLine();

               if (answer == null) {
                  break;
               }

               m_wordMap.put(question.trim(), answer.trim());
            }
         }
      } catch (Exception e) {
         throw new InitializationException("Error when reading file misc.txt.", e);
      }
   }
}
