package com.web.exception;

import java.util.Date;

import com.web.entity.ErrorEntity;

public class UserNotFoundException extends WebApiRuntimeException
{
   /**
    * 
    */
   private static final long serialVersionUID = -2686403834683724544L;

   public UserNotFoundException()
   {
      errorInfo = new ErrorEntity();
      errorInfo.setHttpCode(404);
      errorInfo.setErrorCode("0x40000022");
      errorInfo.setErrorMessage("User Authentication failed. Invalid ID on SQL Server");
      errorInfo.setDate(new Date().toString());
   }

}
