package com.site.wdbc.query.path;

import static com.site.wdbc.query.path.WdbcPathPattern.PATTERN_ALL_NODE;
import static com.site.wdbc.query.path.WdbcPathPattern.PATTERN_ALL_TEXT;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Stack;

public class TagTreeTraverser {
   private WdbcTagTree m_root;

   private List<WdbcTagTree> m_tops = new ArrayList<WdbcTagTree>();

   private List<Stack<WdbcTagTree>> m_lanes = new ArrayList<Stack<WdbcTagTree>>();

   private boolean m_autoCreate;

   private Stack<WdbcTagTree> m_trail = new Stack<WdbcTagTree>();

   public TagTreeTraverser(WdbcTagTree root) {
      m_root = root;
      m_autoCreate = root.isAutoCreate();
   }

   public String getTrailPath() {
      StringBuffer sb = new StringBuffer(256);
      int len = m_trail.size();

      for (int i = 0; i < len; i++) {
         WdbcTagTree tree = m_trail.get(i);

         if (i > 0) {
            sb.append('.');
         }

         sb.append(tree.getName());

         if (tree.getCount() > 1) {
            sb.append('[').append(tree.getCount()).append(']');
         }
      }

      return sb.toString();
   }

   // 0: Not matched, 1: full matched, 2: partial matched
   public int matchesPath(WdbcPathPattern pattern, Map<String, String> attributes) {
      List<String> names = pattern.getSectionNames();
      List<WdbcExpression> expressions = pattern.getSectionExpressions();
      int maxIndex = names.size() - 2; // ignore last section
      String lastName = names.get(names.size() - 1);

      for (Stack<WdbcTagTree> lane : m_lanes) {
         int depth = lane.size();
         int index = 0;
         String sectionName = names.get(index);
         int matched = WdbcPathPattern.NOT_MATCHED;

         for (int i = 0; i < depth; i++) {
            String name = lane.get(i).getName();

            if (sectionName.equals(name)) {
               WdbcExpression expr = expressions.get(index);
               int count = lane.get(i).getCount();

               if (!expr.matches(count, attributes)) {
                  break;
               }

               index++;

               if (index > maxIndex) {
                  if (lastName.equals(PATTERN_ALL_TEXT) || lastName.equals(PATTERN_ALL_NODE)) {
                     if (i == depth - 1) {
                        matched = WdbcPathPattern.FULL_WILD_MATCHED;
                     } else {
                        matched = WdbcPathPattern.PARTIAL_WILD_MATCHED;
                     }
                  } else if (i == depth - 1) {
                     matched = WdbcPathPattern.FULL_MATCHED;
                  }

                  break;
               }

               sectionName = names.get(index);
            }
         }

         if (matched != WdbcPathPattern.NOT_MATCHED) {
            return matched;
         }
      }

      return WdbcPathPattern.NOT_MATCHED;
   }

   public void moveDown(String tagName) {
      int len = m_lanes.size();
      boolean found = false;

      // check all of lanes
      for (int i = 0; i < len; i++) {
         Stack<WdbcTagTree> lane = m_lanes.get(i);
         WdbcTagTree peek = (lane.isEmpty() ? null : lane.peek());
         WdbcTagTree next = (peek == null ? null : peek.getChild(tagName));

         if (next == null && peek != null && tagName.equals(peek.getName())) {
            next = peek.addNode(tagName);
         }

         if (next != null) {
            found = true;
            next.increaseCount();
            lane.push(next);

            if (m_autoCreate && i == 0) {
               m_trail.push(next);
            }
         }
      }

      // check if it's added to lane
      WdbcTagTree top = m_root.getChild(tagName);
      if (top != null && !m_tops.contains(top)) {
         Stack<WdbcTagTree> lane = new Stack<WdbcTagTree>();

         m_tops.add(top);
         m_lanes.add(lane);
         top.increaseCount();
         lane.push(top);

         if (m_autoCreate && !found) {
            m_trail.push(top);
         }
      }
   }

   public void moveUp(String tagName) {
      int len = m_lanes.size();

      // check all of lanes
      for (int i = 0; i < len; i++) {
         Stack<WdbcTagTree> lane = m_lanes.get(i);
         WdbcTagTree peek = (lane.isEmpty() ? null : lane.peek());

         if (peek != null && peek.getName().equals(tagName)) {
            peek.resetChildrenCount();
            lane.pop();

            if (m_autoCreate && i == 0) {
               m_trail.pop();
            }

            if (lane.isEmpty()) {
               m_lanes.remove(i);
               m_tops.remove(peek);
            }
         }
      }
   }
}
