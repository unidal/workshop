package org.unidal.ezsell.view;

import java.text.MessageFormat;

public enum FileAction {
   TRANSACTIONS_CSV("/jsp/ebay/file/transactions_csv.jsp", "text/csv", "transactions-{0}.csv"),

   ;

   private String m_path;

   private String m_mimeType;
   
   private MessageFormat m_fileNameFormat;

   private FileAction(String path, String mimeType, String fileNamePattern) {
      m_path = path;
      m_mimeType = mimeType;
      m_fileNameFormat = new MessageFormat(fileNamePattern);
   }

   public String getFileName(Object id) {
      return m_fileNameFormat.format(new Object[] { id });
   }

   public String getMimeType() {
      return m_mimeType;
   }

   public String getPath() {
      return m_path;
   }
}
