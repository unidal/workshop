package com.site.dal.xml;

import javax.xml.transform.Result;

import org.xml.sax.InputSource;

import com.site.dal.xml.builder.XmlBuilder;
import com.site.dal.xml.parser.XmlParser;
import com.site.lookup.ContainerHolder;

public class DefaultXmlAdapter extends ContainerHolder implements XmlAdapter {
   public <T extends Object> void marshal(Result result, T object) throws XmlException {
      XmlBuilder builder = lookup(XmlBuilder.class);

      builder.build(result, object);
      release(builder);
   }

   @SuppressWarnings("unchecked")
   public <T extends Object> T unmarshal(InputSource source) throws XmlException {
      XmlParser parser = lookup(XmlParser.class);

      Object result = parser.parse(source);
      release(parser);
      return (T) result;
   }
}
