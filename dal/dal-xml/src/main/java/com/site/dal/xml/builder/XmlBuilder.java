package com.site.dal.xml.builder;

import javax.xml.transform.Result;

import com.site.dal.xml.XmlException;

public interface XmlBuilder {
   public void build(Result result, Object object) throws XmlException;
}
