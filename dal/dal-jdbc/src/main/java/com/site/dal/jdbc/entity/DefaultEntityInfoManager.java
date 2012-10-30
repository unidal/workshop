package com.site.dal.jdbc.entity;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.codehaus.plexus.logging.LogEnabled;
import org.codehaus.plexus.logging.Logger;

import com.site.dal.jdbc.DalRuntimeException;
import com.site.dal.jdbc.DataField;
import com.site.dal.jdbc.Readset;
import com.site.dal.jdbc.annotation.Attribute;
import com.site.dal.jdbc.annotation.Entity;
import com.site.dal.jdbc.annotation.Relation;
import com.site.dal.jdbc.annotation.SubObjects;
import com.site.dal.jdbc.annotation.Variable;

public class DefaultEntityInfoManager implements EntityInfoManager, LogEnabled {
   private Map<String, Class<?>> m_logicalNameToEntityClass = new HashMap<String, Class<?>>();

   private Map<Class<?>, EntityInfo> m_entityClassToEntityInfo = new HashMap<Class<?>, EntityInfo>();

   private Logger m_logger;

   public void enableLogging(Logger logger) {
      m_logger = logger;
   }

   public synchronized void register(Class<?> entityClass) {
      if (m_entityClassToEntityInfo.containsKey(entityClass)) {
         m_logger.debug(entityClass + " is already initialized yet");
         return;
      }

      Entity entity = (Entity) entityClass.getAnnotation(Entity.class);

      if (entity == null) {
         throw new DalRuntimeException(entityClass + " should be annotated by Entity");
      }

      Map<DataField, Relation> relations = new HashMap<DataField, Relation>();
      Map<DataField, Attribute> attributes = new LinkedHashMap<DataField, Attribute>();
      Map<DataField, Variable> variables = new HashMap<DataField, Variable>();
      Map<Readset<?>, SubObjects> subobjects = new HashMap<Readset<?>, SubObjects>();
      Field[] fields = entityClass.getFields();
      int index = 0;

      for (Field field : fields) {
         Class<?> type = field.getType();

         if (type == DataField.class) {
            if (!Modifier.isStatic(field.getModifiers())) {
               throw new DalRuntimeException("Field " + field.getName() + " of " + entityClass
                        + " should be modified as static");
            }

            Relation relation = field.getAnnotation(Relation.class);
            Attribute attribute = field.getAnnotation(Attribute.class);
            Variable variable = field.getAnnotation(Variable.class);
            DataField dataField;

            try {
               dataField = (DataField) field.get(null);
            } catch (Exception e) {
               throw new DalRuntimeException("Can't get value of Field " + field.getName() + " of " + entityClass);
            }

            if (attribute != null) {
               attributes.put(dataField, attribute);
            } else if (variable != null) {
               variables.put(dataField, variable);
            } else if (relation != null) {
               relations.put(dataField, relation);
            } else {
               m_logger.warn("Field " + field.getName() + " of " + entityClass + " should be annotated by "
                        + "Attribute or Relation");
            }

            if (dataField != null) {
               dataField.setEntityClass(entityClass);
               dataField.setIndex(index);
            }

            index++;
         } else if (type == Readset.class) {
            if (!Modifier.isStatic(field.getModifiers())) {
               throw new DalRuntimeException("Readset " + field.getName() + " of " + entityClass
                        + " should be modified as static");
            }

            SubObjects subobject = field.getAnnotation(SubObjects.class);
            Readset<?> readset;

            try {
               readset = (Readset<?>) field.get(null);
            } catch (Exception e) {
               throw new DalRuntimeException("Can't get value of Field " + field.getName() + " of " + entityClass);
            }

            if (subobject != null) {
               subobjects.put(readset, subobject);
            }
         }
      }

      if (attributes.size() == 0) {
         m_logger.warn("No fields defined with type DataField in " + entityClass);
      }

      Class<?> otherClass = m_logicalNameToEntityClass.get(entity.logicalName());

      if (otherClass != null) {
         throw new DalRuntimeException("Logical name(" + entity.logicalName() + ") has been used by " + otherClass
                  + ", can't use it in " + entityClass);
      } else {
         m_logicalNameToEntityClass.put(entity.logicalName(), entityClass);
      }

      EntityInfo info = new EntityInfo(entity, relations, attributes, variables, subobjects);

      m_entityClassToEntityInfo.put(entityClass, info);
   }

   public EntityInfo getEntityInfo(Class<?> entityClass) {
      EntityInfo info = m_entityClassToEntityInfo.get(entityClass);

      if (info == null) {
         throw new IllegalStateException(entityClass + " is not registered yet");
      } else {
         return info;
      }
   }

   public EntityInfo getEntityInfo(String logicalName) {
      Class<?> entityClass = m_logicalNameToEntityClass.get(logicalName);
      EntityInfo info = m_entityClassToEntityInfo.get(entityClass);

      if (info == null) {
         throw new IllegalStateException("No Entity is registered with logical name(" + logicalName + ")");
      } else {
         return info;
      }
   }
}
