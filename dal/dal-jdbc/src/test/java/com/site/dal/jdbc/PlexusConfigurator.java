package com.site.dal.jdbc;

import java.util.ArrayList;
import java.util.List;

import com.site.dal.jdbc.configuration.AbstractJdbcResourceConfigurator;
import com.site.dal.jdbc.datasource.DataSource;
import com.site.dal.jdbc.datasource.JndiDataSource;
import com.site.dal.jdbc.mapping.SimpleTableProvider;
import com.site.dal.jdbc.mapping.TableProvider;
import com.site.lookup.configuration.Component;
import com.site.test.user.address.dal.UserAddressDao;
import com.site.test.user.dal.UserDao;

public class PlexusConfigurator extends AbstractJdbcResourceConfigurator {

   @Override
   public List<Component> defineComponents() {
      List<Component> all = new ArrayList<Component>();
      String datasourceFile = "datasource/datasource.xml";

      all.add(defineJdbcDataSourceConfigurationManagerComponent(getClass().getResource(datasourceFile).getFile().toString()));
      
      all.add(defineJdbcDataSourceComponent("jdbc-dal", "com.mysql.jdbc.Driver", "jdbc:mysql://localhost:3306/dal",
            "root", "Iamfrankie", "<![CDATA[useUnicode=true&autoReconnect=true]]>"));

      all.add(C(DataSource.class, "jndi-dal", JndiDataSource.class).config(
               E("jndi-name").value("java:comp/env/jdbc/dal")));

      all.add(C(TableProvider.class, "user", SimpleTableProvider.class).config(
               E("data-source-name").value("jdbc-dal"),
               E("logical-table-name").value("user")
               ));

      all.add(C(TableProvider.class, "user2", SimpleTableProvider.class).config(
               E("data-source-name").value("jdbc-dal"),
               E("logical-table-name").value("user2")
               ));
      
      all.add(C(TableProvider.class, "user-address", SimpleTableProvider.class).config(
               E("data-source-name").value("jdbc-dal"),
               E("logical-table-name").value("user-address"),
               E("physical-table-name").value("user_address")
               ));
      
      all.add(C(UserDao.class).req(QueryEngine.class));
      all.add(C(UserAddressDao.class).req(QueryEngine.class));

      return all;
   }

   @Override
   protected boolean isTestConfiguration() {
      return true;
   }

   public static void main(String[] args) {
      generatePlexusComponentsXmlFile(new PlexusConfigurator());
   }
}
