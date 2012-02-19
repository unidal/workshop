package com.site.wdbc.query;

import java.util.Collections;
import java.util.Map;
import java.util.Stack;

import org.codehaus.plexus.configuration.PlexusConfiguration;
import org.codehaus.plexus.configuration.xml.XmlPlexusConfiguration;

import com.site.wdbc.query.path.TagTreeTraverser;
import com.site.wdbc.query.path.WdbcPathPattern;
import com.site.wdbc.query.path.WdbcTagTree;
import com.site.wdbc.query.path.WdbcWordPattern;

public class DefaultWdbcContext implements WdbcContext {
   private Stack<String> m_tagNames = new Stack<String>();

   private Map<String, String> m_attributes;

   private String m_text;

   private String m_comment;

   private Stack<StringBuffer> m_allTexts = new Stack<StringBuffer>();

   private Stack<Stack<XmlPlexusConfiguration>> m_allChildren = new Stack<Stack<XmlPlexusConfiguration>>();

   private TagTreeTraverser m_traverser;

   public void endAllChildren() {
      Stack<XmlPlexusConfiguration> top = m_allChildren.pop();

      if (!m_allChildren.isEmpty()) {
         m_allChildren.peek().pop(); // it's a fix
         m_allChildren.peek().push(top.peek());
      }
   }

   public void endAllText() {
      StringBuffer sb = m_allTexts.pop();

      if (!m_allTexts.isEmpty()) {
         m_allTexts.peek().append(sb);
      }
   }

   public PlexusConfiguration getAllChildren() {
      return m_allChildren.peek().peek();
   }

   public String getAllText() {
      return m_allTexts.peek().toString();
   }

   public String getAttribute(String name) {
      if (m_attributes != null) {
         return m_attributes.get(name);
      } else {
         return null;
      }
   }

   public Map<String, String> getAttributes() {
      if (m_attributes == null) {
         return Collections.emptyMap();
      } else {
         return m_attributes;
      }
   }

   public String getComment() {
      return m_comment;
   }

   public String getTagName() {
      return m_tagNames.peek();
   }

   public String getText() {
      return m_text;
   }

   public String getTrailPath(WdbcWordPattern pattern) {
      StringBuffer sb = new StringBuffer(256);

      sb.append(m_traverser.getTrailPath());
      String str = pattern.toString();

      if (!str.equals("#text")) {
         sb.append('.').append(str);
      }

      return sb.toString();
   }

   public int matchesPath(WdbcPathPattern pattern) {
      return matchesPath(pattern, null);
   }

   public int matchesPath(WdbcPathPattern pattern, Map<String, String> attributes) {
      return m_traverser.matchesPath(pattern, attributes);
   }

   public void pop(String tagName) {
      m_traverser.moveUp(tagName);
      m_tagNames.pop();
      m_attributes = null;
      m_text = null;
      m_comment = null;

      if (!m_allChildren.isEmpty()) {
         if (!m_allChildren.peek().isEmpty()) {
            XmlPlexusConfiguration child = m_allChildren.peek().pop();

            m_allChildren.peek().peek().addChild(child);
         }
      }
   }

   public void push(String tagName, Map<String, String> attributes) {
      m_tagNames.push(tagName);
      m_traverser.moveDown(tagName);
      m_attributes = attributes;

      if (!m_allChildren.isEmpty()) {
         XmlPlexusConfiguration child = new XmlPlexusConfiguration(tagName);

         for (Map.Entry<String, String> e : attributes.entrySet()) {
            child.setAttribute(e.getKey().toLowerCase(), e.getValue());
         }

         m_allChildren.peek().push(child);
      } else if (!m_allTexts.isEmpty() && tagName.equals("br")) {
         m_allTexts.peek().append("\r\n");
      }
   }

   public void setComment(String comment) {
      m_comment = comment;

      if (!m_allChildren.isEmpty() && comment.trim().length() > 0) {
         XmlPlexusConfiguration child = m_allChildren.peek().peek();
         XmlPlexusConfiguration data = new XmlPlexusConfiguration("#comment");

         data.setValue(comment);
         child.addChild(data);
      }
   }

   public void setTagTree(WdbcTagTree root) {
      m_traverser = new TagTreeTraverser(root);
   }

   public void setText(String text) {
      m_text = text;

      if (!m_allTexts.isEmpty()) {
         m_allTexts.peek().append(text);
      }

      if (!m_allChildren.isEmpty() && text.trim().length() > 0) {
         XmlPlexusConfiguration child = m_allChildren.peek().peek();
         XmlPlexusConfiguration data = new XmlPlexusConfiguration("#text");

         data.setValue(text);
         child.addChild(data);
      }
   }

   public void startAllChildren() {
      Stack<XmlPlexusConfiguration> top = new Stack<XmlPlexusConfiguration>();
      XmlPlexusConfiguration child = new XmlPlexusConfiguration(m_tagNames.peek());

      for (Map.Entry<String, String> e : m_attributes.entrySet()) {
         child.setAttribute(e.getKey(), e.getValue());
      }

      top.push(child);
      m_allChildren.push(top);
   }

   public void startAllText() {
      m_allTexts.push(new StringBuffer(2048));
   }
}
