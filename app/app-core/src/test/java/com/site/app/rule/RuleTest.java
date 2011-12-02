package com.site.app.rule;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.site.app.DataProvider;
import com.site.app.FieldId;
import com.site.app.error.ErrorObject;
import com.site.app.rule.facet.MinMaxLengthFacet;
import com.site.app.rule.facet.MinMaxValueFacet;
import com.site.lookup.ComponentTestCase;

public class RuleTest extends ComponentTestCase {
   public enum UserFieldId implements FieldId {
      USER_ID, USER_NAME, CREATION_DATE_FROM, CREATION_DATE_TO;
      
      public String getName() {
         return name();
      }
   }

   public static interface Rules {
      Rule<UserFieldId, Integer> USER_ID = new IntRule<UserFieldId>(UserFieldId.USER_ID)
            .facet(new MinMaxValueFacet<Integer>(1000, 2000));

      Rule<UserFieldId, String> USER_NAME = new StringRule<UserFieldId>(UserFieldId.USER_NAME)
            .facet(new MinMaxLengthFacet<String>(4, 20));

      Rule<UserFieldId, Date> CREATION_DATE_FROM = new DateRule<UserFieldId>(UserFieldId.CREATION_DATE_FROM, new Date())
            .format("yyyy-MM-dd")
            .facet(new MinMaxValueFacet<Date>(null, new Date()));

   }

   public void test() {
      DataProvider<UserFieldId> dataProvider = new DataProvider<UserFieldId>() {
         public Object getValue(UserFieldId fieldId) {
            switch (fieldId) {
            case USER_ID:
               return "1234";
            case USER_NAME:
               return "abcd";
            case CREATION_DATE_FROM:
               return "2008-03-06";
            default:
               return null;
            }
         }
      };

      List<ErrorObject> errors = new ArrayList<ErrorObject>();
      Integer value1 = Rules.USER_ID.evaluate(dataProvider, errors);
      String value2 = Rules.USER_NAME.evaluate(dataProvider, errors);
      Date value3 = Rules.CREATION_DATE_FROM.evaluate(dataProvider, errors);

      assertNotNull(value1);
      assertNotNull(value2);
      assertNotNull(value3);
      assertTrue(errors.isEmpty());
   }
}
