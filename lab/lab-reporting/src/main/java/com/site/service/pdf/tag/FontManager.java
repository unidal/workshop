package com.site.service.pdf.tag;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

import com.lowagie.text.Font;
import com.lowagie.text.pdf.BaseFont;

public class FontManager {
   private static final FontManager s_instance = new FontManager();

   private Map m_baseFonts = new HashMap();
   private Stack m_fonts = new Stack();

   private FontManager() {
   }

   public static FontManager getInstance() {
      return s_instance;
   }

   public void pushFontBo(FontBo font) {
      font.normalize(m_fonts);

      m_fonts.push(font);
   }

   public FontBo popFontBo() {
      return (FontBo) m_fonts.pop();
   }

   public void registerBaseFont(String alias, BaseFont baseFont) {
      m_baseFonts.put(alias, baseFont);
   }

   public Font getFont() {
      if (m_fonts.isEmpty()) {
         return new Font();
      } else {
         FontBo font = (FontBo) m_fonts.peek();

         return font.getFont();
      }
   }

   public BaseFont getBaseFont(String alias) {
      return (BaseFont) m_baseFonts.get(alias);
   }
}
