package com.site.game.sanguo.thread.handler.question;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import org.codehaus.plexus.personality.plexus.lifecycle.phase.Initializable;
import org.codehaus.plexus.personality.plexus.lifecycle.phase.InitializationException;

public class ChapterAnswerer implements Answerer, Initializable {
   private Map<String, String> m_chaptersByName = new HashMap<String, String>();

   public int answer(String question, String[] choices) {
      String[] parts = question.split(" ");

      if (parts.length > 1) {
         String name = parts[0] + ' ' + parts[1];
         String id = m_chaptersByName.get(name);

         if (id == null) {
            id = getIdByWrongChapterName(parts[0], parts[1]);
         }

         if (id != null) {
            int index = 1;

            for (String choice : choices) {
               if (id.equals(choice)) {
                  return index;
               }

               index++;
            }
         }
      }

      return 0;
   }

   public boolean canAnswer(String question) {
      return question.contains("是三国演义第几回");
   }

   private String getIdByWrongChapterName(String part1, String part2) {
      for (Map.Entry<String, String> e : m_chaptersByName.entrySet()) {
         String chapter = e.getKey();

         if (chapter.contains(part1) || chapter.contains(part2)) {
            return e.getValue();
         }
      }

      return null;
   }

   public void initialize() throws InitializationException {
      try {
         InputStream stream = getClass().getResourceAsStream("chapters.txt");
         BufferedReader reader = new BufferedReader(new InputStreamReader(stream, "gbk"));

         while (true) {
            String line = reader.readLine();

            if (line == null) {
               break;
            }

            int pos = line.indexOf(" ");
            if (pos > 0) {
               String id = line.substring(0, pos);
               String name = line.substring(pos).trim();

               m_chaptersByName.put(name, id);
            }
         }
      } catch (Exception e) {
         throw new InitializationException("Error when reading file chapters.txt.", e);
      }
   }
}
