package com.site.kernel.dal.db;

import com.site.kernel.dal.DataObjectMetaData;
import com.site.kernel.dal.DataObjectNaming;

public class DataRowMetaData extends DataObjectMetaData {
   private String m_logicalName;

   private DataRowField[] m_hintFields;

   public DataRowMetaData(String logicalName, DataObjectNaming naming, Class doClass, DataRowField[] hintFields) {
      super(naming, doClass);

      m_logicalName = logicalName;
      m_hintFields = hintFields;
   }

   public DataRowField[] getHintFields() {
      return m_hintFields;
   }

   public String getLogicalName() {
      return m_logicalName;
   }
}
