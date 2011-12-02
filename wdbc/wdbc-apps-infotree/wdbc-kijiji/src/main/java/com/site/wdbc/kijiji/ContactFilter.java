package com.site.wdbc.kijiji;

import java.io.BufferedReader;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.MessageFormat;

import com.site.wdbc.WdbcResult;
import com.site.wdbc.query.DefaultWdbcFilter;

public class ContactFilter extends DefaultWdbcFilter {
   private MessageFormat m_format = new MessageFormat("{0}new SWFObject(\"{1}id={2}&v={3}\"{4}");

   private String[] magics = { "SDCXZDEWFSDAF", "CSAXCWFDSFASD", "FEWFDSAKJH", "FCZWEFGFSD", "OIJKWFSDAL",
         "FDSAFKPPONSDF", "IOWEQFKDSJKFKL", "NSDLAKNDSKFJKK", "WVCSJOFIUIKL", "FSDCXCVOIWEFDS", };

   @Override
   protected boolean shouldRemoveRow(WdbcResult result, int row) {
      String text = (String) result.getCell(row, "text");

      try {
         Object[] parts = m_format.parse(text);
         int id = Integer.parseInt((String) parts[2]);
         String encodedContact = (String) parts[3];
         String contact = decode(id, encodedContact);

         result.setValue(row, "text", contact);
         parseAndStore(contact, result, row);
         guessAndAdjust(result, row);
      } catch (Exception e) {
         System.out.println(e);
         return true;
      }

      return false;
   }

   private void guessAndAdjust(WdbcResult result, int row) {
      String mobilephone = (String) result.getCell(row, "mobilephone");
      String telephone = (String) result.getCell(row, "telephone");
      int offset = -1;

      if (mobilephone != null) {
         if (mobilephone.length() == 11) {
            char ch = mobilephone.charAt(0);

            // first number must be '1'
            if (ch >= '0' && ch <= '9') {
               offset = ch - '1';
//            } else if (ch >= '£°' && ch <= '£¹') {
//               offset = ch - '£±';
            }
         } else if (mobilephone.length() >= 12) {
            // starts with '021'
            offset = (mobilephone.charAt(0) - '0');
         }
      } else if (telephone != null) {
         if (telephone.length() == 8) {
            // TODO starts with '5' or '6'?
            offset = (telephone.charAt(0) - '6');
         } else if (telephone.length() >= 12) {
            // starts with '021'
            offset = (telephone.charAt(0) - '0');
         }
      }

      doAdjust(result, row, "mobilephone", offset, true);
      doAdjust(result, row, "telephone", offset, true);
      doAdjust(result, row, "qq", offset, true);
      doAdjust(result, row, "text", offset, false);
   }

   private void doAdjust(WdbcResult result, int row, String colName, int offset, boolean numberOnly) {
      String value = (String) result.getCell(row, colName);
      int len = (value == null ? 0 : value.length());
      StringBuffer sb = new StringBuffer(len);

      for (int i = 0; i < len; i++) {
         char ch = value.charAt(i);

         if (ch >= '0' && ch <= '9') {
            sb.append((ch - '0' + 10 - offset) % 10);
//         } else if (ch >= '�0„5¡ã' && ch <= '�0„5�0†1') {
//            sb.append((ch - '�0„5¡ã' + 10 - offset) % 10);
         } else if (numberOnly) {
            if (ch == '-'/* || ch == '�0„5�0…2'*/) {
               sb.append('-');
            } else {
               String remain = value.substring(i).trim();

               if (remain.length() > 0) {
                  result.setValue(row, "contact", remain);
               }

               break;
            }
         } else {
            sb.append(ch);
         }
      }

      result.setValue(row, colName, sb.toString());
   }

   private void parseAndStore(String contact, WdbcResult result, int row) {
      BufferedReader reader = new BufferedReader(new StringReader(contact));

      try {
         while (true) {
            String line = reader.readLine();

            if (line == null) {
               break;
            }

            if (line.startsWith("�0†9�0„9�0ˆ3�0…8�0‡6�0‡9�0„5�0†2")) {
               result.setValue(row, "contact", line.substring("�0†9�0„9�0ˆ3�0…8�0‡6�0‡9�0„5�0†2".length()).trim());
            } else if (line.startsWith("�0‡8�0‰0�0†3¨²�0„5�0†2")) {
               result.setValue(row, "mobilephone", line.substring("�0‡8�0‰0�0†3¨²�0„5�0†2".length()).trim());
            } else if (line.startsWith("�0…8�0Š4�0†3¡ã�0„5�0†2")) {
               result.setValue(row, "telephone", line.substring("�0…8�0Š4�0†3¡ã�0„5�0†2".length()).trim());
            } else if (line.startsWith("QQ�0„5�0†2")) {
               result.setValue(row, "qq", line.substring("QQ�0„5�0†2".length()).trim());
            } else if (line.startsWith("MSN�0„5�0†2")) {
               result.setValue(row, "msn", line.substring("MSN�0„5�0†2".length()).trim());
            } else if (line.startsWith("EMAIL�0„5�0†2")) {
               result.setValue(row, "email", line.substring("EMAIL�0„5�0†2".length()).trim());
            } else if (line.startsWith("�0ˆ1�0‹3�0ˆ9�0†6�0„5�0†2")) {
               result.setValue(row, "url", line.substring("�0ˆ1�0‹3�0ˆ9�0†6�0„5�0†2".length()).trim());
            } else if (line.startsWith("�0‡4�0Š1�0‡9¨¹�0„5�0†2")) {
               result.setValue(row, "other", line.substring("�0‡4�0Š1�0‡9¨¹�0„5�0†2".length()).trim());
            } else {
               result.setValue(row, "other", line.trim());
            }
         }
      } catch (Exception e) {
         e.printStackTrace();
      }
   }

   private String decode(int id, String encoded) throws UnsupportedEncodingException {
      String decoded = URLDecoder.decode(URLDecoder.decode(encoded, "utf-8"), "utf-8");

      for (int i = 0; i < magics.length; i++) {
         decoded = decoded.replaceAll(magics[i], String.valueOf(i));
      }

      return decoded;
   }
}
