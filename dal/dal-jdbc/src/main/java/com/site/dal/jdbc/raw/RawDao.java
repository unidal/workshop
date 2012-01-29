package com.site.dal.jdbc.raw;

import java.util.List;

import com.site.dal.jdbc.AbstractDao;
import com.site.dal.jdbc.DalException;
import com.site.dal.jdbc.QueryDef;
import com.site.dal.jdbc.QueryType;
import com.site.dal.jdbc.mapping.RawTableProvider;

public class RawDao extends AbstractDao {
   @Override
   protected Class<?>[] getEntityClasses() {
      return new Class<?>[0];
   }

   public List<RawDataObject> executeQuery(String dataSource, String sql) throws DalException {
      RawDataObject proto = new RawDataObject();
      QueryDef query = new QueryDef("raw", RawEntity.class, QueryType.SELECT, sql);

      RawTableProvider.setDataSourceName(dataSource);

      try {
         List<RawDataObject> list = getQueryEngine().queryMultiple(query, proto, RawEntity.READSET_FULL);

         return list;
      } finally {
         RawTableProvider.reset();
      }
   }
}
