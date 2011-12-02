package com.site.service.pdf.tag;

import java.util.Stack;

import org.xml.sax.Attributes;

import com.lowagie.text.Chunk;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;

public class TextBo implements GenericContainer {
   private char[] m_ch;

   public TextBo(char[] ch, int start, int length) {
      m_ch = new char[length];

      System.arraycopy(ch, start, m_ch, 0, length);
   }

   public Element getElement() throws DocumentException {
      String text = getDecodedText();

      try {
         if (text.trim().length() > 0) {
            return new Chunk(text, FontManager.getInstance().getFont());
         } else {
            return null;
         }
      } catch (Exception e) {
         return new Chunk(text);
      }
   }

   private String getDecodedText() {
      StringBuffer sb = new StringBuffer(m_ch.length + 16);
      int len = m_ch.length;

      for (int i = 0; i < len; i++) {
         char ch = m_ch[i];

         switch (ch) {
            case '\\':
               if (i + 1 < len) {
                  char ch2 = m_ch[i + 1];

                  switch (ch2) {
                     case 't':
                        sb.append('\t');
                        break;
                     case 'n':
                        sb.append('\n');
                        break;
                     case 'r':
                        sb.append('\r');
                        break;
                     case '\\':
                        sb.append(ch);
                        break;
                     default:
                        sb.append(ch);
                        sb.append(ch2);
                        break;
                  }

                  i++;
                  break;
               }
            default:
               sb.append(ch);
               break;
         }
      }

      return sb.toString();
   }

   public void loadAttributes(Attributes attrs) {
      // Nothing here
   }

   public void addChild(GenericContainer generic) throws DocumentException {
      // Should not have child
   }

   public void setReady(Stack parents) {
      // do nothing
   }

   public String toString() {
      StringBuffer sb = new StringBuffer(m_ch.length + 8);

      sb.append("TextBo[").append(m_ch).append("]");

      return sb.toString();
   }
}
