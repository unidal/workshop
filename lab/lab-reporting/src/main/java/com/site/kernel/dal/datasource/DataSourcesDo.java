package com.site.kernel.dal.datasource;

import static com.site.kernel.dal.Cardinality.ONE_TO_MANY;
import static com.site.kernel.dal.model.NodeType.MODEL;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import org.xml.sax.Attributes;

import com.site.kernel.dal.model.ValidationException;
import com.site.kernel.dal.model.XmlModel;
import com.site.kernel.dal.model.XmlModelField;

public class DataSourcesDo extends XmlModel {
   public static final XmlModelField DATA_SOURCE = new XmlModelField("data-source", MODEL, ONE_TO_MANY, DataSourceDo.class);

   private List m_dataSources = new ArrayList(3);

   static {
      init();
   }

   protected static void init() {
      initialize(null);
   }

   public DataSourcesDo() {
      super(null, "data-sources");
   }

   protected void addDataSourceDo(DataSourceDo dataSource) {
      super.addChild(DATA_SOURCE, dataSource);
   }

   public boolean checkConstraint() throws ValidationException {
      return true;
   }

   public void destroy() {
      for (int i = 0; i != m_dataSources.size(); i++) {
         DataSourceDo dataSource = (DataSourceDo) m_dataSources.get(i);

         dataSource.destroy();
      }
   }

   protected void doValidate(Stack<XmlModel> parents) {
      if (m_dataSources.size() == 0) {
         throw new ValidationException("At least one data-source should be defined under " + this);
      }

      for (int i = 0; i != m_dataSources.size(); i++) {
         DataSourceDo dataSource = (DataSourceDo) m_dataSources.get(i);

         dataSource.validate(parents);
      }
   }

   protected List getDataSourceDos() {
      return m_dataSources;
   }

   public void loadAttributes(Attributes attrs) {
   }

}
