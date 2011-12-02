package com.site.service.pdf.tag;

import java.awt.Color;
import java.lang.reflect.Field;
import java.util.Stack;

import com.lowagie.text.Chunk;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.pdf.BaseFont;

public class FontBo extends FontDo implements GenericContainer {
   private Font m_font;
   private GenericContainer m_parent;

   private static int DEFAULT_FONT_SIZE = 9;
   private static int DEFAULT_FONT_STYLE = Font.NORMAL;
   private static Color DEFAULT_FONT_COLOR = Color.BLACK;

   static {
      init();
   }

   public Element getElement() throws DocumentException {
      return new Chunk("");
   }

   public void addChild(GenericContainer generic) throws DocumentException {
      m_parent.addChild(generic);
   }

   public Font getFont() {
      return m_font;
   }

   public void normalize(Stack parentFonts) {
      BaseFont baseFont = FontManager.getInstance().getBaseFont(getName());
      int size = getFontSize(parentFonts);
      int style = getFontStyle(parentFonts);
      Color color = getFontColor(parentFonts);

      m_font = new Font(baseFont, size, style, color);
   }

   private int getFontSize(Stack parentFonts) {
      if (getSize() > 0) {
         return getSize();
      } else if (parentFonts.isEmpty()) {
         return DEFAULT_FONT_SIZE;
      } else {
         FontBo parent = (FontBo) parentFonts.peek();

         return (int) parent.getFont().size();
      }
   }

   private int getFontStyle(Stack parentFonts) {
      if (getStyle() > 0) {
         return getStyle();
      } else if (parentFonts.isEmpty()) {
         return DEFAULT_FONT_STYLE;
      } else {
         FontBo parent = (FontBo) parentFonts.peek();

         return parent.getFont().style();
      }
   }

   private Color getFontColor(Stack parentFonts) {
      String color = getColor();

      if (!isEmpty(color)) {
         try {
            if (!color.startsWith("#")) {
               Color fontColor = getFontColorByName(color);

               if (fontColor != null) {
                  return fontColor;
               }

               return Color.decode("#" + color);
            } else {
               return Color.decode(color);
            }
         } catch (NumberFormatException e) {
            throw new RuntimeException("Invalid font color:" + color + ", error:" + e);
         }

      } else if (parentFonts.isEmpty()) {
         return DEFAULT_FONT_COLOR;
      } else {
         FontBo parent = (FontBo) parentFonts.peek();

         return parent.getFont().color();
      }
   }

   private Color getFontColorByName(String name) {
      try {
         Field field = Color.class.getField(name);

         return (Color) field.get(null);
      } catch (Exception e) {
         return null;
      }
   }

   public void setReady(Stack parents) {
      for (int i = parents.size() - 1; i >= 0; i--) {
         Object obj = parents.get(i);

         if (obj instanceof GenericContainer) {
            GenericContainer g = (GenericContainer) obj;

            if (!(g instanceof FontBo)) {
               m_parent = g;
               break;
            }
         } else if (obj instanceof GenericHelper) {
            GenericContainer g = ((GenericHelper) obj).getGenericContainer();

            if (!(g instanceof FontBo)) {
               m_parent = g;
               break;
            }
         }
      }
   }

}
