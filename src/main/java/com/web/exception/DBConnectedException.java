package com.web.exception;

import java.util.Date;

import com.web.entity.ErrorEntity;

public class DBConnectedException extends WebApiRuntimeException
{

   /**
    * 
    */
   private static final long serialVersionUID = 4703051566421807736L;

   public DBConnectedException()
   {
      errorInfo = new ErrorEntity();
      errorInfo.setHttpCode(500);
      errorInfo.setErrorCode("0x4000001A");
      errorInfo.setErrorMessage("Unable to connect to SQL Server");
      errorInfo.setDate(new Date().toString());
   }
}
