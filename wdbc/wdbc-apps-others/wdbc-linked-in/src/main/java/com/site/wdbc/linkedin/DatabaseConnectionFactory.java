package com.site.wdbc.linkedin;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnectionFactory {
   private String driver = "com.mysql.jdbc.Driver";

   private String url = "jdbc:mysql://localhost:3306/linkedin";

   private String user = "root";

   private String password = "Iamfrankie";

   public Connection getConnection() throws SQLException {
      try {
         Class.forName(driver);
      } catch (ClassNotFoundException e) {
         throw new SQLException("JDBC driver not found: " + driver);
      }

      Connection conn = DriverManager.getConnection(url, user, password);

      return conn;
   }
}
