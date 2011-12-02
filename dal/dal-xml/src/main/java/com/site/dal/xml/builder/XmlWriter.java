package com.site.dal.xml.builder;

import javax.xml.transform.Result;

import org.xml.sax.ContentHandler;

public interface XmlWriter extends ContentHandler {
   public void setResult(Result result);
}
