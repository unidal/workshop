package com.site.converter;

import junit.framework.TestSuite;

import com.site.converter.advanced.ConstructorConverterTest;
import com.site.converter.basic.BasicConverterTest;
import com.site.converter.collection.ArrayConverterTest;
import com.site.converter.collection.ListConverterTest;
import com.site.converter.dom.NodeConverterTest;

public class AllTests extends TestSuite {
   public static TestSuite suite() {
      TestSuite suite = new TestSuite();
      
      suite.addTestSuite(TypeUtilTest.class);
      suite.addTestSuite(BasicConverterTest.class);
      suite.addTestSuite(ArrayConverterTest.class);
      suite.addTestSuite(ListConverterTest.class);
      suite.addTestSuite(NodeConverterTest.class);
      suite.addTestSuite(ConstructorConverterTest.class);
      
      return suite;
   }
}
