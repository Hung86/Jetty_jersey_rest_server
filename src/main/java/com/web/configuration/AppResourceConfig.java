package com.web.configuration;

import org.glassfish.jersey.server.ResourceConfig;

import com.web.provider.AuthenticationFilter;
import com.web.provider.CustomExceptionHandler;

import io.swagger.v3.jaxrs2.SwaggerSerializers;



public class AppResourceConfig extends ResourceConfig  {
   public AppResourceConfig() 
   {
       packages("com.web,io.swagger.v3.jaxrs2.integration.resources");
       register(CustomExceptionHandler.class);
       register(AuthenticationFilter.class);
       register(SwaggerSerializers.class);

   }
   
   public void loadConfigFile() {

   }

}
