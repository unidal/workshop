package com.site.dal.jdbc.query.token;

import java.util.List;
import java.util.regex.Pattern;

import com.site.lookup.ComponentTestCase;

public class TokenParserTest extends ComponentTestCase {
   public void testParseSimple() throws Exception {
      TokenParser parser = lookup(TokenParser.class);
      assertNotNull(parser);

      String pattern = "SELECT <FIELDS/> FROM <TABLE/> WHERE 1 = 1 <IF type='NOT_ZERO' param='state'> AND <FIELD name='state'/> = ${state} </IF> <IF type='NOT_ZERO' param='airline-id'> AND <FIELD name='airline-id'/> = ${airline-id} </IF> <IF type='NOT_NULL' param='carrier-code'> AND carrier_code like concat('%', ${carrier-code}, '%') </IF> <IF type='NOT_NULL' param='class-type'> AND class_type like concat('%', ${class-type}, '%') </IF> <IF type='NOT_NULL' param='class-code'> AND class_code like concat('%', ${class-code}, '%') </IF> <IF type='NOT_ZERO' param='airfare-status'> AND airfare_status = ${airfare-status} </IF> <IF type='NOT_NULL' param='last-modified-by'> AND last_modified_by like concat('%', ${last-modified-by}, '%') </IF> <IF type='NOT_NULL' param='comments'> AND comments like concat('%', ${comments}, '%') </IF> ORDER BY airfare_id ASC LIMIT ${start}, ${count}";
      List<Token> tokens = parser.parse(pattern);
      StringBuilder sb = new StringBuilder(pattern.length());

      for (Token token : tokens) {
         sb.append(token);
      }

      assertEquals(pattern, sb.toString());
   }

   public void testParseJoins() throws Exception {
      TokenParser parser = lookup(TokenParser.class);
      assertNotNull(parser);

      String pattern = "SELECT <FIELDS/> FROM <TABLES/> WHERE <JOINS/> AND <IF type='NOT_ZERO' param='state'> AND state = ${state} </IF> <IF type='NOT_ZERO' param='airline-id'> AND airline_id = ${airline-id} </IF> <IF type='NOT_NULL' param='carrier-code'> AND carrier_code like concat('%', ${carrier-code}, '%') </IF> <IF type='NOT_NULL' param='class-type'> AND class_type like concat('%', ${class-type}, '%') </IF> <IF type='NOT_NULL' param='class-code'> AND class_code like concat('%', ${class-code}, '%') </IF> <IF type='NOT_ZERO' param='airfare-status'> AND airfare_status = ${airfare-status} </IF> <IF type='NOT_NULL' param='last-modified-by'> AND last_modified_by like concat('%', ${last-modified-by}, '%') </IF> <IF type='NOT_NULL' param='comments'> AND comments like concat('%', ${comments}, '%') </IF> ORDER BY airfare_id ASC LIMIT ${start}, ${count}";
      List<Token> tokens = parser.parse(pattern);
      StringBuilder sb = new StringBuilder(pattern.length());

      for (Token token : tokens) {
         sb.append(token);
      }

      assertEquals(pattern, sb.toString());
   }

   public void testParseEscaping() throws Exception {
      TokenParser parser = lookup(TokenParser.class);
      assertNotNull(parser);

      String pattern = "SELECT <FIELDS/> FROM <TABLE/> WHERE <IF type='NOT_ZERO' param='state'> AND state \\>= ${state} </IF> <IF type='NOT_ZERO' param='airline-id'> AND airline_id = ${airline-id} </IF> <IF type='NOT_NULL' param='carrier-code'> AND carrier_code like concat('%', ${carrier-code}, '%') </IF> <IF type='NOT_NULL' param='class-type'> AND class_type like concat('%', ${class-type}, '%') </IF> <IF type='NOT_NULL' param='class-code'> AND class_code like concat('%', ${class-code}, '%') </IF> <IF type='NOT_ZERO' param='airfare-status'> AND airfare_status = ${airfare-status} </IF> <IF type='NOT_NULL' param='last-modified-by'> AND last_modified_by like concat('%', ${last-modified-by}, '%') </IF> <IF type='NOT_NULL' param='comments'> AND comments like concat('%', ${comments}, '%') </IF> ORDER BY airfare_id ASC LIMIT ${start}, ${count}";
      List<Token> tokens = parser.parse(pattern);
      StringBuilder sb = new StringBuilder(pattern.length());

      for (Token token : tokens) {
         sb.append(token);
      }

      assertEquals(pattern.replaceAll(Pattern.quote("\\>"), ">"), sb.toString());
   }
}
