package com.site.service.pdf.tag;

import java.util.Stack;

import org.xml.sax.Attributes;

import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;

public interface GenericContainer {
   public Element getElement() throws DocumentException;

   public void loadAttributes(Attributes attrs);

   public void addChild(GenericContainer generic) throws DocumentException;

   public void setReady(Stack parents);
}
