package com.site.dal.xml.parser;

import org.xml.sax.InputSource;

import com.site.dal.xml.XmlException;

public interface XmlParser {
   public Object parse(InputSource source) throws XmlException;
}
