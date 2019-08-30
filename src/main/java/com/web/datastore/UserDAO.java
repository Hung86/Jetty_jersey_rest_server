package com.web.datastore;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import com.web.entity.JobEntity;
import com.web.entity.UserEntity;

public class UserDAO
{
   private static final String SQLPROC_JOBLIST = "{call SJPS.dbo.P_JobList(?,?)}";
   private static final String SQLPROC_ENROLL = "DECLARE @RetSuccess nvarchar(max)  EXEC sjps.dbo.spEnrollUser_V4 ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, "
         + "@RetSuccess=@RetSuccess OUTPUT SELECT @RetSuccess AS N'@RetSuccess'";
   private static final String SQLPROC_UNENROLL = "DECLARE @return_value int,@RetSuccess nvarchar(max) EXEC    @return_value = [sjps].[dbo].[UnEnrollUser_V3] @cardId = ?, "
         + "@RetSuccess = @RetSuccess OUTPUT SELECT  @RetSuccess as N'@RetSuccess' SELECT    'Return Value' = @return_value";
 
   private static final String SQL_UPDATE_CARD_PASSWORD = "Update T_Enrollment set lab_doublefactor = ? where lab_idCard = ? ";
   
   private static final String LOOKUP_USER_ID = "SELECT lab_domain, lab_login, lab_department, lab_email, lab_fullname, lab_homedirectory, lab_doublefactor, lab_enrollment, GroupOUName, IsOU, DistinguishedName FROM sjps.dbo.T_Enrollment LEFT JOIN sjps.dbo.T_EnrollmentGroupOU ON T_EnrollmentGroupOU.id_User = T_Enrollment.id_User LEFT JOIN sjps.dbo.T_GroupOU ON T_GroupOU.GroupOUID = T_EnrollmentGroupOU.GroupOUID WHERE lab_idCard = ?";

   private JdbcConnection jdbcConnection;
   
   public UserDAO() {
      jdbcConnection = JdbcConnection.getInstance();
   }

   public UserEntity doAuthentication(String id) {
      UserEntity user = null;
      Connection con = jdbcConnection.connect(JdbcConnection.CELIVEO_DB);
      if (con != null) {
         PreparedStatement pstmt = null;
         ResultSet rs = null;
         try
         {
            pstmt = con.prepareStatement(LOOKUP_USER_ID);
            pstmt.setString(1, id);
            rs = pstmt.executeQuery();

            if (rs.next()) {
               List<String> groupMemberOf = new ArrayList<String>();
               List<String> groupDistinggiushedName = new ArrayList<String>();
               List<String> OUName = new ArrayList<String>();
               user  = new UserEntity();
               user.setUserId(id);
               
               do {
                  user.setDomain(rs.getString("lab_domain"));
                  user.setUserName(rs.getString("lab_login"));
                  user.setDepartment(rs.getString("lab_department"));
                  user.setEmail(rs.getString("lab_email"));
                  user.setUserFullName(rs.getString("lab_fullname"));
                  user.setHomeDirectory(rs.getString("lab_homedirectory"));
                  user.setDualFactor(rs.getString("lab_doublefactor"));
                  user.setEnrollment(rs.getString("lab_enrollment"));
                  if(rs.getBoolean("IsOU")) {
                      user.setUserDistinguishedName(rs.getString("DistinguishedName"));
                      OUName.add(rs.getString("GroupOUName"));
                  }else {
                      groupMemberOf.add(rs.getString("GroupOUName"));
                      groupDistinggiushedName.add(rs.getString("DistinguishedName"));
                  }
               } while(rs.next());
            
               user.setOrgUnit(OUName.toArray(new String[0]));
               user.setMemberOf(groupMemberOf.toArray(new String[0]));
               user.setGroupDistinguishedName(groupDistinggiushedName.toArray(new String[0]));
            }
         } catch (SQLException e) {
            e.printStackTrace();
         } catch (Exception e) {
            e.printStackTrace();
         } finally {
            jdbcConnection.close(con, pstmt, rs);
         }
      }
      return user;
   }
   
   public boolean sqlproc_unenroll_execute(String username) {
      boolean isOk = false;
      Connection con = jdbcConnection.connect(JdbcConnection.SJPS_DB);
      if (con != null) {
          PreparedStatement pstmt = null;
          ResultSet rs = null;
          try {
              pstmt = con.prepareStatement(SQLPROC_UNENROLL);
              pstmt.setObject(1, username, Types.VARCHAR);
              System.out.println("sqlproc_unenroll_execute : pstmt = " + pstmt.toString());
              rs = pstmt.executeQuery();
              rs = pstmt.executeQuery();
              while (rs.next()) {
                  if ("1".equals(rs.getString("@RetSuccess"))) {
                      isOk = true;
                  }
              }
          } catch (Exception e) {
             e.printStackTrace();
          } finally {
             jdbcConnection.close(con, pstmt, rs);
          }

      }
      return isOk;
  }
   
   public boolean sqlproc_enroll_execute (String enrollId, UserEntity user) {
      boolean isOk = false;
      if ((user == null) ||(user.getUserId().equals("")) ||(user.getUserName().equals(""))) {
         return isOk;
      }
      
      Connection con = jdbcConnection.connect(JdbcConnection.SJPS_DB);
      if (con != null) {
          PreparedStatement pstmt = null;
          ResultSet rs = null;
          int col = 1;
          try {
              pstmt = con.prepareStatement(SQLPROC_ENROLL);
              pstmt.setString(col++, enrollId);
              pstmt.setString(col++, user.getDomain());
              pstmt.setString(col++, user.getUserName());
              pstmt.setString(col++, user.getDepartment());
              pstmt.setString(col++, user.getEmail());
              pstmt.setString(col++, user.getUserFullName());
              pstmt.setString(col++, user.getHomeDirectory());
              pstmt.setString(col++, user.getDualFactor());
              pstmt.setString(col++, user.getUserRight());
              pstmt.setString(col++, user.getEnrollment());
              
              String ouName  = (user.getOrgUnit().length > 0) ? user.getOrgUnit()[0] : "";
              pstmt.setString(col++, ouName);

              pstmt.setString(col++, user.getUserDistinguishedName());

              pstmt.setString(col++, user.generateGroupDistinguishedNameInXML());

              pstmt.setString(col++, user.generateGroupMemeberOfInXML());

              rs = pstmt.executeQuery();
              
              while (rs.next()) {
                  if ("1".equals(rs.getString("@RetSuccess"))) {
                      isOk = true;
                  }
              }
          } catch (Exception e) {
             e.printStackTrace();
          } finally {
             jdbcConnection.close(con, pstmt, rs);
          }
      }
      return isOk;
   }
   
   public List<JobEntity> getJobList(String username, String deparment) {
      List<JobEntity> jobList = new ArrayList<JobEntity>();
      Connection con = jdbcConnection.connect(JdbcConnection.SJPS_DB);
      if (con != null) {
         PreparedStatement pstmt = null;
         ResultSet rs = null;
         int col = 1;
         try {
            pstmt = con.prepareCall(SQLPROC_JOBLIST);
            pstmt.setString(col++, username);
            pstmt.setString(col++, deparment);
            pstmt.execute();
            rs = pstmt.getResultSet();
            JobEntity jobEntity = null;
            while (rs.next()) {
               jobEntity = new JobEntity();
               String formattedserverId = String.format("%09d", Integer.parseInt(rs.getString("id_server")));
               String formattedJobId = String.format("%09d", Integer.parseInt(rs.getString("id_PrintJob")));
               jobEntity.setJobId("D-" + formattedserverId + "-" + formattedJobId);
               jobEntity.setJobServerName(rs.getString("lab_ServerName"));
               jobEntity.setJobServerIpAddress(rs.getString("lab_IpAddress"));
               jobEntity.setJobServerPort(rs.getInt("nb_Port"));
               jobEntity.setJobDate(rs.getString("lab_date"));
               jobEntity.setJobName(rs.getString("lab_name"));
               jobEntity.setJobTotalPages(rs.getInt("lab_JobTotalPages"));
               jobEntity.setJobNbColorPages(rs.getInt("lab_JobNbColorPages"));
               jobEntity.setJobNbCopy(rs.getInt("lab_JobNbCopy"));
               jobEntity.setJobSize(rs.getString("lab_JobSize"));
               jobEntity.setJobAllowed((int) rs.getLong("lab_JobAllowed"));
               jobEntity.setJobCompatibilityStatus(rs.getString("lab_ApplicationType"));
               jobEntity.setJobPaperSize(rs.getString("lab_JobPaperSize"));
               jobEntity.setJobCost(rs.getFloat("lab_JobCost"));
               jobEntity.setJobDateLimit(rs.getString("lab_DateLimit"));
               jobEntity.setJobDuplex(rs.getBoolean("lab_JobDuplex"));
               jobEntity.setJobVoid(rs.getString("lab_UniqueId"));
               jobEntity.setDeptJob(rs.getString("lab_Type").equalsIgnoreCase("DEPT") ? true : false);
               jobList.add(jobEntity);
            }
         } catch (Exception e) {
            e.printStackTrace();
         } finally {
            jdbcConnection.close(con, pstmt, rs);
         }
      }
      return jobList;
   }
}
