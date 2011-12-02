package org.unidal.education;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class PaperPrinter {
   private Set<String> m_questions = new LinkedHashSet<String>();
   private List<String> m_answers = new ArrayList<String>();

   public static void main(String[] args) {
      PaperPrinter printer = new PaperPrinter();

      for (int i = 0; i < 30; i++) {
         printer.reset();
         printer.prepare(Type.ADD_OR_SUB, 20, 20, 20, 100);
         printer.print(5, false);
         printer.print(5, true);
      }
   }

   private void prepare(Type type, int a, int b, int maxResult, int total) {
      m_questions.clear();

      while (m_questions.size() < total) {
         int x = (int) (Math.random() * (a - 1)) + 1;
         int y = (int) (Math.random() * (b - 1)) + 1;

         switch (type) {
         case ADD:
            if (x + y < maxResult) {
               if (m_questions.add(String.format("%2d + %2d = ", x, y))) {
                  m_answers.add(String.format("%2d", x + y));
               }
            }
            break;
         case ADD_OR_SUB:
            int z = (int) (Math.random() * 2);

            if (z == 0) {
               if (x + y < maxResult) {
                  if (m_questions.add(String.format("%2d + %2d = ", x, y))) {
                     m_answers.add(String.format("%2d", x + y));
                  }
               }
            } else {
               if (x - y >= 0 && x - y < maxResult) {
                  if (m_questions.add(String.format("%2d - %2d = ", x, y))) {
                     m_answers.add(String.format("%2d", x - y));
                  }
               }
            }
         }
      }
   }

   private void print(int columns, boolean isAnswer) {
      int size = m_questions.size();
      int columnSize = size / columns;
      String[][] matrix = new String[columnSize][columns];
      int index = 0;

      if (isAnswer) {
         for (String item : m_answers) {
            matrix[index / columns][index % columns] = item;
            index++;
         }
      } else {
         for (String item : m_questions) {
            matrix[index / columns][index % columns] = item;
            index++;
         }
      }

      String indent = "     ";

      for (String[] row : matrix) {
         StringBuilder sb = new StringBuilder(256);
         boolean first = true;

         for (String cell : row) {
            if (first) {
               first = false;
            } else {
               sb.append(indent);
            }

            sb.append(cell);
         }

         System.out.println(sb.toString());
      }
   }
   private void reset() {
      m_questions.clear();
      m_answers.clear();
   }

   enum Type {
      ADD,

      ADD_OR_SUB;
   }
}
