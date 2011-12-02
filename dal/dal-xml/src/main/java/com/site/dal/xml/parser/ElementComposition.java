package com.site.dal.xml.parser;

import com.site.dal.xml.XmlException;

public interface ElementComposition {
   public Object compose(ElementNode node) throws XmlException;
}
