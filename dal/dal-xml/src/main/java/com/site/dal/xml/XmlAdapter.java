package com.site.dal.xml;

import javax.xml.transform.Result;

import org.xml.sax.InputSource;

public interface XmlAdapter {
   public <T> void marshal(Result result, T object) throws XmlException;

   public <T> T unmarshal(InputSource source) throws XmlException;
}
