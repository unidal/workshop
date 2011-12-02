package com.site.game.sanguo.thread.handler.question;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.codehaus.plexus.personality.plexus.lifecycle.phase.Initializable;
import org.codehaus.plexus.personality.plexus.lifecycle.phase.InitializationException;

public class WordAnswerer implements Answerer, Initializable {
   private Map<String, List<String>> m_wordMap = new HashMap<String, List<String>>();

   public int answer(String question, String[] choices) {
      String[] parts = question.split(" ");

      if (parts.length > 1) {
         String lastWord = parts[1];
         List<String> nextWords = m_wordMap.get(lastWord);
         int index;

         for (String nextWord : nextWords) {
            index=1;
            
            for (String choice : choices) {
               if (nextWord.equals(choice)) {
                  return index;
               }

               index++;
            }
         }
      }

      return 0;
   }

   public boolean canAnswer(String question) {
      return question.startsWith("ÐªºóÓï");
   }

   public void initialize() throws InitializationException {
      try {
         InputStream stream = getClass().getResourceAsStream("words.txt");
         BufferedReader reader = new BufferedReader(new InputStreamReader(stream, "gbk"));

         while (true) {
            String line = reader.readLine();

            if (line == null) {
               break;
            }

            int pos = line.indexOf(":");
            if (pos > 0) {
               String lastWord = line.substring(0, pos);
               String nextWord = line.substring(pos + 1);

               List<String> nextWords = m_wordMap.get(lastWord);

               if (nextWords == null) {
                  nextWords = new ArrayList<String>(3);
                  m_wordMap.put(lastWord, nextWords);
               }

               nextWords.add(nextWord);
            }
         }
      } catch (Exception e) {
         throw new InitializationException("Error when reading file words.txt.", e);
      }
   }
}
