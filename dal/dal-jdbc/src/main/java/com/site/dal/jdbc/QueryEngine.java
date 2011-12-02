package com.site.dal.jdbc;

import java.util.List;

public interface QueryEngine {
   public <T extends DataObject> void commitTransaction(QueryDef query, T proto) throws DalException;

   public <T extends DataObject> int[] deleteBatch(QueryDef query, T[] protos) throws DalException;

   public <T extends DataObject> int deleteSingle(QueryDef query, T proto) throws DalException;

   public <T extends DataObject> int[] insertBatch(QueryDef query, T[] protos) throws DalException;

   public <T extends DataObject> int insertSingle(QueryDef query, T proto) throws DalException;

   public <T extends DataObject> List<T> queryMultiple(QueryDef query, T proto, Readset<?> readset) throws DalException;

   public <T extends DataObject> T querySingle(QueryDef query, T proto, Readset<?> readset) throws DalException;

   public <T extends DataObject> void rollbackTransaction(QueryDef query, T proto) throws DalException;

   public <T extends DataObject> void startTransaction(QueryDef query, T proto) throws DalException;

   public <T extends DataObject> int[] updateBatch(QueryDef query, T[] protos, Updateset<?> updateset) throws DalException;

   public <T extends DataObject> int updateSingle(QueryDef query, T proto, Updateset<?> updateset) throws DalException;
}
