package org.unidal.ezsell.view;

import java.text.MessageFormat;

public enum ReportAction {
   CUSTOMS_DECLARATION_REPORT("/jsp/ebay/report/customs_declaration_report.jsp", "customs-declaration-{0}.pdf"),

   SHIPPING_LEVEL_REPORT("/jsp/ebay/report/shipping_label_report.jsp", "shipping-label-{0}.pdf"),

   TRANSACTIONS_REPORT("/jsp/ebay/report/transactions_report.jsp", "transactions-{0}.pdf"),
   
   ;

   private String m_path;

   private MessageFormat m_fileNameFormat;

   private ReportAction(String path, String fileNamePattern) {
      m_path = path;
      m_fileNameFormat = new MessageFormat(fileNamePattern);
   }

   public String getPath() {
      return m_path;
   }

   public String getFileName(Object id) {
      return m_fileNameFormat.format(new Object[] { id });
   }
}
