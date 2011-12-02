package com.site.wdbc.linkedin;

import java.io.IOException;
import java.io.Writer;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.codehaus.plexus.personality.plexus.lifecycle.phase.Initializable;
import org.codehaus.plexus.personality.plexus.lifecycle.phase.InitializationException;

public class DatabaseAccess implements Initializable {
   private DatabaseConnectionFactory m_factory;

   private Connection m_conn;

   public void dumpToCsv(Writer writer) throws SQLException, IOException {
      List<Employee> employees = findAllEmployees();
      Map<Integer, List<Job>> result = findAllJobs();
      StringBuffer sb = new StringBuffer(2048);
      long start = System.currentTimeMillis();
      int nums = 0;

      writer.write("\"ID\", \"First Name\", \"Last Name\", \"Profile\"");
      writer.write(", \"Title\", \"Company\", \"From\", \"To\"");
      writer.write(", \"Title\", \"Company\", \"From\", \"To\"");
      writer.write("\r\n");

      for (Employee e : employees) {
         List<Job> jobs = result.get(e.getEmployeeId());

         nums++;
         sb.setLength(0);
         csv(sb, String.valueOf(e.getEmployeeId()));
         sb.append(", ");
         csv(sb, e.getFirstName());
         sb.append(", ");
         csv(sb, e.getLastName());
         sb.append(", ");
         csv(sb, e.getProfile());

         if (jobs != null) {
            for (Job job : jobs) {
               sb.append(", ");
               csv(sb, job.getTitle());
               sb.append(", ");
               csv(sb, job.getCompany());
               sb.append(", ");
               csv(sb, job.getFromDate());
               sb.append(", ");
               csv(sb, job.getToDate());
            }
         }

         sb.append("\r\n");
         writer.write(sb.toString());
      }

      System.out.println(nums + " records dumped in " + (System.currentTimeMillis() - start) + " ms");
   }

   private void csv(StringBuffer sb, String data) {
      int len = (data == null ? 0 : data.length());

      sb.append('"');

      for (int i = 0; i < len; i++) {
         char ch = data.charAt(i);

         if (ch == '"') {
            sb.append('"'); // add one more quote
         }

         sb.append(ch);
      }

      sb.append('"');
   }

   public List<Employee> findAllEmployees() throws SQLException {
      String sql = "select * from employee";
      PreparedStatement ps = m_conn.prepareStatement(sql);

      ResultSet rs = ps.executeQuery();
      List<Employee> result = new ArrayList<Employee>();

      while (rs.next()) {
         Employee e = new Employee();

         e.setEmployeeId(rs.getInt("employee_id"));
         e.setFirstName(rs.getString("first_name"));
         e.setLastName(rs.getString("last_name"));
         e.setProfile(rs.getString("profile"));
         e.setZipcode(rs.getInt("zipcode"));

         result.add(e);
      }

      rs.close();
      ps.close();
      return result;
   }

   public Map<Integer, List<Job>> findAllJobs() throws SQLException {
      String sql = "select * from job";
      PreparedStatement ps = m_conn.prepareStatement(sql);

      ResultSet rs = ps.executeQuery();
      Map<Integer, List<Job>> result = new HashMap<Integer, List<Job>>(4096);

      while (rs.next()) {
         Job job = new Job();

         job.setEmployeeId(rs.getInt("employee_id"));
         job.setTitle(rs.getString("title"));
         job.setCompany(rs.getString("company"));
         job.setFromDate(rs.getString("from_date"));
         job.setToDate(rs.getString("to_date"));

         int employeeId = job.getEmployeeId();
         List<Job> jobs = result.get(employeeId);

         if (jobs == null) {
            jobs = new ArrayList<Job>(5);
            result.put(employeeId, jobs);
         }

         jobs.add(job);
      }

      rs.close();
      ps.close();

      return result;
   }

   public String[] getZipcodeInPending(String state) throws SQLException {
      String sql = "select zipcode from location where state = ? and status in (0,1) and zipcode > 94000";
      PreparedStatement ps = m_conn.prepareStatement(sql);

      ps.setObject(1, state);
      ResultSet rs = ps.executeQuery();
      List<String> zipcodes = new ArrayList<String>();

      while (rs.next()) {
         String zipcode = rs.getString(1);

         zipcodes.add(zipcode);
      }

      rs.close();
      ps.close();
      return zipcodes.toArray(new String[0]);
   }

   public boolean hasEmployee(String id) {
      PreparedStatement ps = null;
      ResultSet rs = null;

      try {
         String sql = "select 1 from employee where employee_id = ?";
         ps = m_conn.prepareStatement(sql);

         ps.setObject(1, id);
         rs = ps.executeQuery();

         if (rs.next()) {
            int count = rs.getInt(1);

            return count > 0;
         }
      } catch (SQLException e) {
         System.err.println(e.toString());
      } finally {
         try {
            if (rs != null) {
               rs.close();
            }

            if (ps != null) {
               ps.close();
            }
         } catch (SQLException e) {
            // ignore it
         }
      }

      return false;
   }

   public void initialize() throws InitializationException {
      try {
         m_conn = m_factory.getConnection();
      } catch (SQLException e) {
         throw new IllegalArgumentException("Can't get database connection", e);
      }
   }

   public void insertEmployee(String id, String firstName, String lastName, String profile, String zipcode)
         throws SQLException {
      String sql = "insert into employee (employee_id,first_name,last_name,profile,zipcode,creation_date) values (?,?,?,?,?,now())";
      PreparedStatement ps = m_conn.prepareStatement(sql);

      ps.setObject(1, id);
      ps.setObject(2, firstName);
      ps.setObject(3, lastName);
      ps.setObject(4, profile);
      ps.setObject(5, zipcode);
      ps.execute();
      ps.close();
   }

   public void insertJob(String id, String title, String company, String fromDate, String toDate) throws SQLException {
      String sql = "insert into job (employee_id,title,company,from_date,to_date) values (?,?,?,?,?)";
      PreparedStatement ps = m_conn.prepareStatement(sql);

      ps.setObject(1, id);
      ps.setObject(2, title);
      ps.setObject(3, company);
      ps.setObject(4, fromDate);
      ps.setObject(5, toDate);
      ps.execute();
      ps.close();
   }

   public void insertLocation(String zipcode, String state) throws SQLException {
      String sql = "insert into location (zipcode,state,status) values (?,?,0)";
      PreparedStatement ps = m_conn.prepareStatement(sql);

      ps.setObject(1, zipcode);
      ps.setObject(2, state);
      ps.execute();
      ps.close();
   }

   public void insertProgress(String level1, String level2) throws SQLException {
      String sql = "insert into progress (L1, L2) values (?,?)";
      PreparedStatement ps = m_conn.prepareStatement(sql);

      ps.setObject(1, level1);
      ps.setObject(2, level2);
      ps.execute();
      ps.close();
   }

   public boolean isDone(String level1, String level2) {
      PreparedStatement ps = null;
      ResultSet rs = null;

      try {
         String sql = "select 1 from progress where L1 = ? and L2 = ?";
         ps = m_conn.prepareStatement(sql);

         ps.setObject(1, level1);
         ps.setObject(2, level2);
         rs = ps.executeQuery();

         if (rs.next()) {
            int count = rs.getInt(1);

            return count > 0;
         }
      } catch (SQLException e) {
         System.err.println(e.toString());
      } finally {
         try {
            if (rs != null) {
               rs.close();
            }

            if (ps != null) {
               ps.close();
            }
         } catch (SQLException e) {
            // ignore it
         }
      }

      return false;
   }

   public void removeEmployee(String id) throws SQLException {
      String sql = "delete from employee where employee_id = ?";
      PreparedStatement ps = m_conn.prepareStatement(sql);

      ps.setObject(1, id);
      ps.execute();
      ps.close();
   }

   public void removeJob(String id) throws SQLException {
      String sql = "delete from job where employee_id = ?";
      PreparedStatement ps = m_conn.prepareStatement(sql);

      ps.setObject(1, id);
      ps.execute();
      ps.close();
   }

   public void removeLocation(String zipcode) throws SQLException {
      String sql = "delete from location where zipcode = ?";
      PreparedStatement ps = m_conn.prepareStatement(sql);

      ps.setObject(1, zipcode);
      ps.execute();
      ps.close();
   }

   public void updateLocation(String zipcode, int status, int nums) throws SQLException {
      String sql = "update location set status = ?, jobs = jobs + ? where zipcode = ?";
      PreparedStatement ps = m_conn.prepareStatement(sql);

      ps.setObject(1, status);
      ps.setObject(2, nums);
      ps.setObject(3, zipcode);
      ps.execute();
      ps.close();
   }

   private static final class Employee {
      private int m_employeeId;

      private String m_firstName;

      private String m_lastName;

      private String m_profile;

      private int m_zipcode;

      public int getEmployeeId() {
         return m_employeeId;
      }

      public String getFirstName() {
         return m_firstName;
      }

      public String getLastName() {
         return m_lastName;
      }

      public String getProfile() {
         return m_profile;
      }

      public int getZipcode() {
         return m_zipcode;
      }

      public void setEmployeeId(int employeeId) {
         m_employeeId = employeeId;
      }

      public void setFirstName(String firstName) {
         m_firstName = firstName;
      }

      public void setLastName(String lastName) {
         m_lastName = lastName;
      }

      public void setProfile(String profile) {
         m_profile = profile;
      }

      public void setZipcode(int zipcode) {
         m_zipcode = zipcode;
      }
   }

   private static final class Job {
      private int m_employeeId;

      private String m_title;

      private String m_company;

      private String m_fromDate;

      private String m_toDate;

      public String getCompany() {
         return m_company;
      }

      public int getEmployeeId() {
         return m_employeeId;
      }

      public String getFromDate() {
         return m_fromDate;
      }

      public String getTitle() {
         return m_title;
      }

      public String getToDate() {
         return m_toDate;
      }

      public void setCompany(String company) {
         m_company = company;
      }

      public void setEmployeeId(int employeeId) {
         m_employeeId = employeeId;
      }

      public void setFromDate(String fromDate) {
         m_fromDate = fromDate;
      }

      public void setTitle(String title) {
         m_title = title;
      }

      public void setToDate(String toDate) {
         m_toDate = toDate;
      }
   }
}
