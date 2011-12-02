package com.site.codegen.meta;

import java.io.Reader;

import org.jdom.Element;

public interface ModelMeta {
   public Element getMeta(Reader reader);
}
