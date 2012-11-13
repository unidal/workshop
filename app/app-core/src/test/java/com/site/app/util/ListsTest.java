package com.site.app.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import junit.framework.Assert;

import org.junit.Test;

import com.site.app.util.Lists;
import com.site.app.util.Lists.Factor;

public class ListsTest {
   @Test
   public void testIntersection() {
      List<String> list1 = Arrays.asList(new String[] { "1", "2", "3", "4", "5" });
      List<String> list2 = Arrays.asList(new String[] { "2", "4", "6", "8", "10" });
      List<String> result = Lists.intersection(list1, list2, new Factor<String>() {
         public Object getId(String object) {
            return object;
         }

         public String merge(String newItem, String oldItem) {
            return newItem;
         }
      });

      Assert.assertEquals("[2, 4]", result.toString());
   }

   @Test
   public void testSegregate() {
      List<String> list1 = Arrays.asList(new String[] { "1", "2", "3", "4", "5" });
      List<String> list2 = Arrays.asList(new String[] { "2", "4", "6", "8", "10" });
      List<String> insert = new ArrayList<String>();
      List<String> update = new ArrayList<String>();
      List<String> delete = new ArrayList<String>();

      Lists.segregate(list1, list2, insert, update, delete, new Factor<String>() {
         public Object getId(String object) {
            return object;
         }

         public String merge(String newItem, String oldItem) {
            return newItem;
         }
      });

      Assert.assertEquals("[1, 3, 5]", insert.toString());
      Assert.assertEquals("[2, 4]", update.toString());
      Assert.assertEquals("[6, 8, 10]", delete.toString());
   }
}
