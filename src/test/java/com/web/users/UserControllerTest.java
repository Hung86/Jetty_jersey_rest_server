package com.web.users;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.web.datastore.UserDAO;
import com.web.entity.JobEntity;
import com.web.entity.UserEntity;

public class UserControllerTest
{

   
   @Test
   public void testAuthenticationAndReturnUserEntity()
   {
      UserDAO userDao = new UserDAO();
      UserEntity user = userDao.doAuthentication("9888");
      if (user == null) {
         System.out.println("[AuthenticationTest] user not found ");
      } else {
         System.out.println("[AuthenticationTest] Found . UserInfo " + user);
      }
   }

   @Test
   public void testGetJobList()
   {
      UserDAO userDao = new UserDAO();
      List<JobEntity> jobEntity = userDao.getJobList("user01", "dev");
      if (jobEntity == null) {
         System.out.println("[testGetJobList] Job is not found ");
      } else {
         System.out.println("[testGetJobList] Found . JobList: " + jobEntity);
      }
   }
   
   @Before
   public void testUserEnrollment()
   {
      UserEntity dumpUser = new UserEntity();
      dumpUser.setUserId("9888");
      dumpUser.setDomain("DC=JETMOBILEDEMO,DC=COM");
      dumpUser.setUserName("ASWANTH");
      dumpUser.setDepartment("FPE-Celiveo");
      dumpUser.setEmail("aswanth.k.muthu@celiveo.com");
      dumpUser.setUserFullName("Aswanth Muthukrishnan");
      dumpUser.setHomeDirectory("UNKNOWN");
      dumpUser.setDualFactor("");
      dumpUser.setEnrollment("aswanth");
      
      String[] OU = {"Testing"};
      dumpUser.setOrgUnit(OU);
      dumpUser.setUserDistinguishedName("OU=TESTING,OU=SUPERUSERS,OU=OUTEST,DC=JETMOBILEDEMO,DC=COM");
     
      String[] groupDistinguishedName = {"CN=COPYBWOK,CN=USERS,DC=JETMOBILEDEMO,DC=COM", "CN=TESTCOLORCOPY,CN=USERS,DC=JETMOBILEDEMO,DC=COM", "CN=COLORCOPYOK,OU=OU3,OU=OUTEST,DC=JETMOBILEDEMO,DC=COM", "CN=DOMAIN USERS,CN=USERS,DC=JETMOBILEDEMO,DC=COM" };
      String[] memberOf = {"CopyBWOK", "testcolorcopy", "ColorCopyOK", "Domain Users" };
      
      dumpUser.setGroupDistinguishedName(groupDistinguishedName);
      dumpUser.setMemberOf(memberOf);
      dumpUser.setUserRight("FFFFFFFF");
            
      UserDAO userDao = new UserDAO();
      boolean isOK= userDao.sqlproc_enroll_execute(dumpUser.getUserId(), dumpUser);
      
      if (isOK) {
         System.out.println("[testUserEnrollment] enrollment success ");
      } else {
         System.out.println("[testUserEnrollment] enrollment failure ");
      }
   }
   
   @After
   public void testUserUnEnrollment()
   {
      UserDAO userDao = new UserDAO();
      boolean isOK= userDao.sqlproc_unenroll_execute("ASWANTH");
      if (isOK) {
         System.out.println("[testUserUnEnrollment] un-enrollment success ");
      } else {
         System.out.println("[testUserUnEnrollment] un-enrollment failure ");
      }
   }
}
