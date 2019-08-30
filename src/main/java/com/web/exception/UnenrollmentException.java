package com.web.exception;

import java.util.Date;

import com.web.entity.ErrorEntity;

public class UnenrollmentException extends WebApiRuntimeException
{

   /**
    * 
    */
   private static final long serialVersionUID = -3709679040966681908L;
   public UnenrollmentException()
   {
      errorInfo = new ErrorEntity();
      errorInfo.setHttpCode(500);
      errorInfo.setErrorCode("0xxxxxxxxx");
      errorInfo.setErrorMessage("Un-Enrollment is unsuccessful");
      errorInfo.setDate(new Date().toString());
   }
}
