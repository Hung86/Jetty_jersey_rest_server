package com.web.datastore;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.web.exception.DBConnectedException;
import com.web.exception.UserNotFoundException;

public class JdbcConnection
{
   public static String CELIVEO_DB = "CeliveoDB";
   public static String SJPS_DB = "SJPS";
   public static String PRINTMANAGER90_DB = "PrintManager90";
   
   private static JdbcConnection jdbcConnection;
   private String username ="CeliveoDB";
   private String password = "7740c942295dfd1f105dfb0137b077fcdb21a16a4630247688be438564876fe4";
   private String driverName = "net.sourceforge.jtds.jdbc.Driver";
   private String serverAddress = "192.168.12.199";
   
   public static synchronized JdbcConnection getInstance() {
      if (jdbcConnection == null) {
         jdbcConnection = new JdbcConnection();
      }
      return jdbcConnection;
   }
   
   private JdbcConnection() {
      try
      {
         Class.forName(driverName);

      }
      catch (ClassNotFoundException e)
      {
         e.printStackTrace();
      }

   }
   
   public Connection connect(String databaseName) {
      Connection con = null;
      try
      {
         StringBuilder urlConnection = new StringBuilder();
         urlConnection.append("jdbc:jtds:sqlserver://").append(serverAddress).append(";").
                                 append("databaseName=").append(databaseName).append(";").
                                             append("user=").append(username).append(";").
                                         append("password=").append(password).append(";");
                                         
         con = DriverManager.getConnection(urlConnection.toString());
      }
      catch (SQLException e)
      {
         e.printStackTrace();
      } catch (Exception e2)
      {
         e2.printStackTrace();
      }
      
      if (con == null) {
         throw new DBConnectedException();
      }
      
      return con;
   }
   
   public void close(Connection con) {
      try
      {
         if ((con != null) && (!con.isClosed())) {
            con.close();
         }
      }
      catch (SQLException e)
      {
         e.printStackTrace();
      }

   }
   
   public void close(Connection con, PreparedStatement pstmt, ResultSet rs) {
      try
      {
         if (rs != null) {
            rs.close();
         }
         
         if (pstmt != null) {
            pstmt.close();
         }
         
         if ((con != null) && (!con.isClosed())) {
            con.close();
         }
      }
      catch (SQLException e)
      {
         e.printStackTrace();
      }

   }
}
