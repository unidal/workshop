package com.site.dal.xml.formatter;

import com.site.dal.xml.XmlException;

public interface Formatter<T> {
   String format(String format, T object) throws XmlException;

   T parse(String format, String text) throws XmlException;
}
