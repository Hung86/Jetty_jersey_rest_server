package com.web.exception;

import java.util.Date;

import com.web.entity.ErrorEntity;

public class EnrollmentException extends WebApiRuntimeException
{
   /**
    * 
    */
   private static final long serialVersionUID = 5447543167179278165L;

   public EnrollmentException()
   {
      errorInfo = new ErrorEntity();
      errorInfo.setHttpCode(500);
      errorInfo.setErrorCode("0xxxxxxxxx");
      errorInfo.setErrorMessage("Enrollment is unsuccessful");
      errorInfo.setDate(new Date().toString());
   }
}
