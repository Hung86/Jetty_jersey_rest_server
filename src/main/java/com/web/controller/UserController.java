package com.web.controller;

import java.util.List;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.web.datastore.UserDAO;
import com.web.entity.JobEntity;
import com.web.entity.UserEntity;
import com.web.exception.EnrollmentException;
import com.web.exception.UnenrollmentException;
import com.web.exception.UserNotFoundException;

@Path("/users")
public class UserController
{
   @RolesAllowed("Authentication")
   @GET
   @Path("/{id}")
   @Produces(MediaType.APPLICATION_JSON)
   public Response authentication(@PathParam("id") String id) 
   {
      System.out.println("-------------use gson");

      UserDAO userDao = new UserDAO();
      UserEntity user = userDao.doAuthentication(id);
      if (user == null)
      {
         throw new UserNotFoundException();
      }

      return Response.status(200).entity(user).build();
   }
   
   @RolesAllowed("Authentication")
   @POST
   @Consumes(MediaType.APPLICATION_JSON)
   @Produces(MediaType.APPLICATION_JSON)
   public Response enrollment(UserEntity user) 
   {
      System.out.println("-------------enroll : " + user);

      UserDAO userDao = new UserDAO();
      if (userDao.sqlproc_enroll_execute(user.getUserId(), user)) {
         System.out.println("-------------enroll success");
      } else {
         System.out.println("-------------enroll failure");
         throw new EnrollmentException();
      }
      return Response.ok().status(201).build();
   }
   
   @RolesAllowed("Authentication")
   @DELETE
   @Path("/{username}")
   @Produces(MediaType.APPLICATION_JSON)
   public Response unenrollment(@PathParam("username") String username) 
   {
      System.out.println("-------------unenrollment : " + username);
      UserDAO userDao = new UserDAO();
      if (userDao.sqlproc_unenroll_execute(username)) {
         System.out.println("-------------un-enroll success");
      } else {
         System.out.println("-------------un-enroll failure");
         throw new UnenrollmentException();
      }
      return Response.ok().status(200).build();
   }
   
   @RolesAllowed("Authentication")
   @GET
   @Path("/{username}/{department}/jobs")
   @Produces(MediaType.APPLICATION_JSON)
   public Response getJobList(@PathParam("username") String username, @PathParam("department") String department) 
   {
      System.out.println("--------v4-----getJobList : " + username);
      UserDAO userDao = new UserDAO();
      List<JobEntity> jobList  = userDao.getJobList(username, department);
      GenericEntity<List<JobEntity>> genericEntity = new GenericEntity<List<JobEntity>>(jobList){};
      return Response.status(200).entity(genericEntity).build();
   }
}
