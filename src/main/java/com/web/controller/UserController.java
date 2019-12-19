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
import com.web.entity.ErrorEntity;
import com.web.entity.JobEntity;
import com.web.entity.UserEntity;
import com.web.exception.EnrollmentException;
import com.web.exception.UnenrollmentException;
import com.web.exception.UserNotFoundException;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.media.ArraySchema;





@Tag(name = "UserController", description ="User authentication, Enrollment, Joblist")
@OpenAPIDefinition(
	     security = {@SecurityRequirement(name = "basicAuth")})
//@OpenAPIDefinition(
//	     security = {@SecurityRequirement(name = "basicAuth")},
//	     info = @Info ( title = "Ser", description = "", version = "1.0-SNAPSHOT"))
@SecurityScheme(name="basicAuth", type = SecuritySchemeType.HTTP, scheme = "basic")
@Path("/users")
public class UserController
{
    @Operation(operationId = "doAuthentication")
    @ApiResponses(
            value = {
            		@ApiResponse(
                    responseCode = "404",
                    description = "User ID not found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorEntity.class))),
            		@ApiResponse(
                    responseCode = "200",
                    description = "User ID is valid",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserEntity.class)))})
   @RolesAllowed("Authentication")
   @GET
   @Path("/{id}")
   @Produces(MediaType.APPLICATION_JSON)
   public Response authentication(@Parameter(description  = "User Id", required = true, schema = @Schema(implementation = String.class))
   								 @PathParam("id") String id) 
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
   
   @Operation(operationId = "doEnrollment")
   @ApiResponses(
           value = {
           		@ApiResponse(
                   responseCode = "201",
                   description = "User's enrollment is successful"),
           		@ApiResponse(
                   responseCode = "500",
                   description = "User's enrollment is failed",
                   content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorEntity.class)))})
   @RolesAllowed("Authentication")
   @POST
   @Consumes(MediaType.APPLICATION_JSON)
   @Produces(MediaType.APPLICATION_JSON)
   public Response enrollment(@RequestBody(description = "User Entity", required = true,
           								content = @Content( schema = @Schema(implementation = UserEntity.class)))
   										UserEntity user) 
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
   
   
   @Operation(operationId = "doUnenrollment")
   @ApiResponses(
           value = {
           		@ApiResponse(
                   responseCode = "200",
                   description = "User's un-enrollment is successful"),
           		@ApiResponse(
                   responseCode = "500",
                   description = "User's un-enrollment is failed",
                   content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorEntity.class)))})
   @RolesAllowed("Authentication")
   @DELETE
   @Path("/{username}")
   @Produces(MediaType.APPLICATION_JSON)
   public Response unenrollment(@Parameter(description  = "User Name", required = true, schema = @Schema(implementation = String.class)) 
   											@PathParam("username") String username) 
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
   
   
   
   
   @Operation(operationId = "getJobList")
   @ApiResponses(
           value = {
           		@ApiResponse(
                   responseCode = "200",
                   description = "Return Job Entity List by User",
                   content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = UserEntity.class)))),
           		@ApiResponse(
                   responseCode = "500",
                   description = "Getting Job List is failed",
                   content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorEntity.class)))})
   @RolesAllowed("Authentication")
   @GET
   @Path("/{username}/{department}/jobs")
   @Produces(MediaType.APPLICATION_JSON)
   public Response getJobList(@Parameter(description  = "User Name", required = true, schema = @Schema(implementation = String.class)) 
   							 	@PathParam("username") String username, 
   							  @Parameter(description  = "User Department", required = true, schema = @Schema(implementation = String.class)) 
   							  	@PathParam("department") String department) 
   {
      System.out.println("--------v4-----getJobList : " + username);
      UserDAO userDao = new UserDAO();
      List<JobEntity> jobList  = userDao.getJobList(username, department);
      GenericEntity<List<JobEntity>> genericEntity = new GenericEntity<List<JobEntity>>(jobList){};
      return Response.status(200).entity(genericEntity).build();
   }
}
