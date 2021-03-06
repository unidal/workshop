package com.site.dal.jdbc.entity;

import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.site.dal.jdbc.DalRuntimeException;
import com.site.dal.jdbc.DataField;
import com.site.dal.jdbc.DataObject;
import com.site.dal.jdbc.engine.QueryContext;
import com.site.dal.jdbc.raw.RawEntity;

public class DefaultDataObjectAssembly implements DataObjectAssembly {
   private DataObjectAccessor m_accessor;

   private DataObjectNaming m_naming;

   private Map<Class<?>, Map<String, Method>> m_subObjectsMap = new HashMap<Class<?>, Map<String, Method>>();

   private Map<Method, DataObject> m_cache = new HashMap<Method, DataObject>();

   @SuppressWarnings("unchecked")
   public <T extends DataObject> T newRow(T proto, List<String> subObjectNames, List<DataObject> result) {
      Class<? extends DataObject> masterClass = proto.getClass();
      Map<String, Method> subObjects = m_subObjectsMap.get(masterClass);

      if (subObjects == null) {
         subObjects = new LinkedHashMap<String, Method>();
         m_subObjectsMap.put(masterClass, subObjects);
      }

      T row = (T) m_accessor.newInstance(masterClass);
      m_cache.clear();

      for (String subObjectName : subObjectNames) {
         if (subObjectName != null) {
            Method setMethod = subObjects.get(subObjectName);

            if (setMethod == null) {
               setMethod = m_naming.getSetMethod(masterClass, subObjectName);
               subObjects.put(subObjectName, setMethod);
            }

            DataObject subObject = m_cache.get(setMethod);

            if (subObject == null) {
               Class<DataObject> subObjectType = (Class<DataObject>) setMethod.getParameterTypes()[0];

               subObject = m_accessor.newInstance(subObjectType);

               try {
                  setMethod.invoke(row, new Object[] { subObject });
               } catch (Exception e) {
                  throw new DalRuntimeException("Error when setting SubObject(" + subObject + " to DataObject(" + row
                           + ")");
               }

               m_cache.put(setMethod, subObject);
            }

            result.add(subObject);
         } else {
            result.add(row);
         }
      }

      return row;
   }

   private void prepareOutFields(QueryContext ctx, ResultSet rs) throws SQLException {
      ResultSetMetaData metadata = rs.getMetaData();
      List<DataField> outFields = ctx.getOutFields();
      int count = metadata.getColumnCount();

      for (int i = 1; i <= count; i++) {
         String name = metadata.getColumnName(i);
         DataField field = new DataField(name);

         field.setEntityClass(RawEntity.class);
         field.setIndex(i - 1);
         outFields.add(field);
      }
   }

   @Override
   @SuppressWarnings("unchecked")
   public <T extends DataObject> List<T> assemble(QueryContext ctx, ResultSet rs) throws SQLException {
      List<DataField> outFields = ctx.getOutFields();

      if (ctx.getQuery().isRaw()) {
         prepareOutFields(ctx, rs);
      }

      List<String> outSubObjectNames = ctx.getOutSubObjectNames();
      List<DataObject> subObjects = new ArrayList<DataObject>();
      T proto = (T) ctx.getProto();
      List<T> rows = new ArrayList<T>();

      while (rs.next()) {
         subObjects.clear();

         T row = newRow(proto, outSubObjectNames, subObjects);
         int len = outFields.size();

         for (int i = 0; i < len; i++) {
            DataField field = outFields.get(i);

            if (ctx.getQuery().isRaw()) {
               m_accessor.setFieldValue(row, field, rs.getObject(i + 1));
            } else {
               DataObject subObject = subObjects.get(i);

               m_accessor.setFieldValue(subObject, field, rs.getObject(i + 1));
            }
         }

         // call afterLoad() to do some custom data manipulation
         row.afterLoad();
         rows.add(row);
      }

      rs.close();
      return rows;
   }
}
