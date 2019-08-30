package com.web.configuration;

import org.glassfish.jersey.server.ResourceConfig;

import com.web.provider.AuthenticationFilter;
import com.web.provider.CustomExceptionHandler;


public class AppResourceConfig extends ResourceConfig 
{
   public AppResourceConfig() 
   {
       packages("com.web");
       register(CustomExceptionHandler.class);
       register(AuthenticationFilter.class);
   }
   
   public void loadConfigFile() {
      
   }

}
