package com.site.kernel.dal.db.helpers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.site.kernel.dal.DalRuntimeException;
import com.site.kernel.dal.db.helpers.token.EndTagToken;
import com.site.kernel.dal.db.helpers.token.ParameterToken;
import com.site.kernel.dal.db.helpers.token.SimpleTagToken;
import com.site.kernel.dal.db.helpers.token.StartTagToken;
import com.site.kernel.dal.db.helpers.token.StringToken;

/**
 * @author qwu
 */
public class Statement {
   private List<Token> m_tokens;

   public Statement(String sqlStatement) {
      m_tokens = parse(sqlStatement);
   }

   public static void main(String[] argv) {
      new Statement("SELECT <FIELDS/> FROM <TABLE/> WHERE 1 = 1 <IF type='NOT_ZERO' param='state'> AND state = ${state} </IF> <IF type='NOT_ZERO' param='airline-id'> AND airline_id = ${airline-id} </IF> <IF type='NOT_NULL' param='carrier-code'> AND carrier_code like concat('%', ${carrier-code}, '%') </IF> <IF type='NOT_NULL' param='class-type'> AND class_type like concat('%', ${class-type}, '%') </IF> <IF type='NOT_NULL' param='class-code'> AND class_code like concat('%', ${class-code}, '%') </IF> <IF type='NOT_ZERO' param='airfare-status'> AND airfare_status = ${airfare-status} </IF> <IF type='NOT_NULL' param='last-modified-by'> AND last_modified_by like concat('%', ${last-modified-by}, '%') </IF> <IF type='NOT_NULL' param='comments'> AND comments like concat('%', ${comments}, '%') </IF> ORDER BY airfare_id ASC LIMIT ${start}, ${count}");
   }
   
   private List<Token> parse(String statement) {
      List<Token> tokens = new ArrayList<Token>();
      int len = statement.length();
      StringBuffer sb = new StringBuffer(256);
      StringBuffer attrName = new StringBuffer(32);
      StringBuffer attrValue = new StringBuffer(64);
      int numTags = 0; // number of <...>
      int numBrackets = 0; // number of {...}
      boolean hasWhiteSpace = false; // ' ', '\t', '\r', '\n'
      boolean hasStartSlash = false; // '/' of </...>
      boolean hasEndSlash = false; // '/' of <.../>
      boolean hasDollarSign = false; // '$', used for IN parameter
      boolean hasNumberSign = false; // '#', used for OUT parameter
      boolean inTag = false;
      boolean inAttrName = false;
      boolean inAttrValue = false;
      Map<String, String> attrs = new HashMap<String, String>(3);

      for (int i = 0; i < len; i++) {
         char ch = statement.charAt(i);

         switch (ch) {
         case ' ':
         case '\t':
         case '\r':
         case '\n':
            if (inTag) {
               if (sb.length() == 0) {
                  sb.append('<');
                  sb.append(' ');
                  inTag = false;
               } else {
                  inTag = false;
                  inAttrName = true;
               }
            } else if (!hasWhiteSpace) { // only one white space is
               // counted
               sb.append(' ');
            }

            hasWhiteSpace = true;
            break;
         case '/':
            if (numTags > 0) {
               if (sb.length() == 0) { // </...>
                  hasStartSlash = true;
               } else { // <.../>
                  hasEndSlash = true;
               }
            } else {
               sb.append(ch);
            }

            hasWhiteSpace = false;
            break;
         case '>':
            if (numTags > 0) {
               if (hasStartSlash || hasEndSlash) {
                  numTags--;
               }

               if (sb.length() > 0) {
                  if (hasStartSlash) {
                     tokens.add(new EndTagToken(sb.toString()));
                  } else if (hasEndSlash) {
                     tokens.add(new SimpleTagToken(sb.toString(), attrs));
                  } else {
                     tokens.add(new StartTagToken(sb.toString(), attrs));
                  }

                  sb.setLength(0);
                  attrs.clear();
                  hasStartSlash = false;
                  hasEndSlash = false;
               } else {
                  throw new DalRuntimeException("Illegal TAG usage, parsed tokens: " + tokens + ". Statement: " + statement);
               }
            } else {
               sb.append(ch);
            }

            inAttrName = false;
            inAttrValue = false;
            inTag = false;
            hasWhiteSpace = false;
            break;
         case '$':
            hasDollarSign = true;
            hasWhiteSpace = false;
            break;
         case '#':
            hasNumberSign = true;
            hasWhiteSpace = false;
            break;
         case '{':
            if ((hasDollarSign || hasNumberSign) && numBrackets == 0) {
               int size = sb.length();

               if (size > 0) {
                  tokens.add(new StringToken(sb.substring(0, size)));

                  sb.setLength(0);
                  numBrackets++;
                  hasWhiteSpace = false;
                  continue;
               }
            }

            sb.append(ch);
            hasWhiteSpace = false;
            break;
         case '}':
            if (numBrackets > 0) {
               tokens.add(new ParameterToken(sb.toString(), hasDollarSign, hasNumberSign));
               sb.setLength(0);
               numBrackets--;
            } else {
               sb.append(ch);
            }

            hasDollarSign = false;
            hasNumberSign = false;
            hasWhiteSpace = false;
            break;
         case '=':
            if (inAttrName) {
               inAttrName = false;
               inAttrValue = true;
            } else {
               sb.append(ch);
            }

            hasWhiteSpace = false;
            break;
         case '\'':
            if (inAttrValue) {
               while (i + 1 < len) {
                  ch = statement.charAt(++i);

                  if (ch == '\'') {
                     break;
                  } else {
                     attrValue.append(ch);
                  }
               }

               attrs.put(attrName.toString(), attrValue.toString());
               attrName.setLength(0);
               attrValue.setLength(0);
               inTag = true;
               inAttrValue = false;
            } else {
               sb.append(ch);

               while (i + 1 < len) {
                  ch = statement.charAt(++i);
                  sb.append(ch);

                  if (ch == '\'') {
                     break;
                  }
               }

               if (ch != '\'') {
                  throw new DalRuntimeException("single quote is expected. Statement: " + statement);
               }
            }

            hasWhiteSpace = false;
            break;
         case '<':
            boolean followByLetter = false;

            if (i + 1 < len) {
               char ch2 = statement.charAt(i + 1);

               if (Character.isLetter(ch2)) {
                  followByLetter = true;
               } else if (ch2=='/') {
                  if (sb.length() > 0) {
                     tokens.add(new StringToken(sb.toString()));
                     sb.setLength(0);
                  }
                  
                  hasStartSlash = true;
                  break;
               }
            }

            if (followByLetter) {
               if (sb.length() > 0) {
                  tokens.add(new StringToken(sb.toString()));
                  sb.setLength(0);
               }

               numTags++;
               inTag = true;
               hasWhiteSpace = false;
               break;
            }
         default:
            if (inTag) {
               if (hasWhiteSpace) {
                  inTag = false;
                  inAttrName = true;
               } else {
                  sb.append(ch);
               }
            } else if (inAttrName) {
               attrName.append(ch);
            } else if (inAttrValue) {
               attrValue.append(ch);
            } else {
               if (numBrackets == 0) {
                  if (hasDollarSign) {
                     sb.append('$');
                     hasDollarSign = false;
                  }

                  if (hasNumberSign) {
                     sb.append('#');
                     hasNumberSign = false;
                  }
               }

               sb.append(ch);
            }

            hasWhiteSpace = false;
            break;
         }
      }

      if (sb.length() > 0) {
         tokens.add(new StringToken(sb.toString()));
      }

      return tokens;
   }

   public String getSqlStatement(QueryContext ctx) {
      StringBuffer sql = new StringBuffer(1024);
      int size = m_tokens.size();

      for (int i = 0; i < size; i++) {
         String token = m_tokens.get(i).getToken(ctx);

         if (!ctx.isNoOutput()) {
            sql.append(token);
         }
      }

      return sql.toString();
   }

   public String toString() {
      StringBuffer sb = new StringBuffer(256);
      int len = m_tokens.size();

      for (int i = 0; i < len; i++) {
         Token token = m_tokens.get(i);

         sb.append(token);
      }

      return sb.toString();
   }
}
