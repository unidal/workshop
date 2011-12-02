package com.site.wdbc.query.path;

import static com.site.wdbc.query.path.WdbcPathPattern.PATTERN_ALL_NODE;
import static com.site.wdbc.query.path.WdbcPathPattern.PATTERN_ALL_TEXT;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TagTree implements WdbcTagTree {
   private WdbcTagTree m_parent;

   private String m_name;

   private int m_count;

   public Map<String, TagTree> m_children;

   public boolean m_autoCreate;

   private TagTree(WdbcTagTree parent, String name, boolean autoCreate) {
      m_parent = parent;
      m_name = name;
      m_children = new HashMap<String, TagTree>();
      m_autoCreate = autoCreate;
   }

   public static WdbcTagTree buildTree(List<PathPattern> patterns, boolean autoCreate) {
      TagTree root = new TagTree(null, null, autoCreate);

      if (patterns != null) {
         for (PathPattern pattern : patterns) {
            List<String> names = pattern.getSectionNames();
            int size = names.size();
            TagTree parent = root;
            String lastName = names.get(size - 1);

            // Do not include last section
            for (int i = 0; i < size - 1; i++) {
               String name = names.get(i);

               parent = parent.addNode(name);

               if (i == size - 2 && (lastName.equals(PATTERN_ALL_TEXT) || lastName.equals(PATTERN_ALL_NODE))) {
                  parent.m_autoCreate = true;
               }
            }
         }
      }

      return root;
   }

   public TagTree addNode(String name) {
      TagTree node = m_children.get(name);

      if (node == null) {
         node = new TagTree(this, name, m_autoCreate);

         m_children.put(name, node);
      }

      return node;
   }

   public int getCount() {
      return m_count;
   }

   public String getName() {
      return m_name;
   }

   public WdbcTagTree getParent() {
      return m_parent;
   }

   @Override
   public String toString() {
      StringBuffer sb = new StringBuffer(1024);

      toString(sb, 0);

      return sb.toString();
   }

   private void toString(StringBuffer sb, int level) {
      sb.append("TagTree[");
      sb.append("name=").append(m_name);
      sb.append(",count=").append(m_count);
      sb.append("]\r\n");

      for (TagTree t : m_children.values()) {
         for (int i = 0; i <= level; i++) {
            sb.append("   ");
         }

         t.toString(sb, level + 1);
      }
   }

   // For test only
   public Map<String, TagTree> getChildren() {
      return m_children;
   }

   public TagTree getChild(String name) {
      TagTree child = m_children.get(name);

      if (child == null && m_autoCreate) {
         child = addNode(name);
      }

      return child;
   }

   public void increaseCount() {
      m_count++;
   }

   public void resetCount() {
      m_count = 0;
   }

   public void resetChildrenCount() {
      for (WdbcTagTree child : m_children.values()) {
         child.resetCount();
      }
   }

   public boolean isAutoCreate() {
      return m_autoCreate;
   }
}
