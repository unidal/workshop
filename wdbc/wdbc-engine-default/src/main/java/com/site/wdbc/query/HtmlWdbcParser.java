package com.site.wdbc.query;

import javax.swing.text.html.parser.ParserDelegator;

import com.site.wdbc.WdbcException;
import com.site.wdbc.WdbcResult;
import com.site.wdbc.WdbcSource;

public class HtmlWdbcParser implements WdbcParser {
   public WdbcResult parse(WdbcHandler handler, WdbcSource source)
         throws WdbcException {
      HtmlParserCallback cb = new HtmlParserCallback(handler);

      try {
         new ParserDelegator().parse(source.getReader(), cb, true);

         WdbcResult result = handler.getResult();

         return result;
      } catch (Exception e) {
         throw new WdbcException("Error while parsing HTML, message: " + e, e);
      }
   }
}
