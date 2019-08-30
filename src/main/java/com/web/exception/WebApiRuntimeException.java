package com.web.exception;

import com.web.entity.ErrorEntity;

public class WebApiRuntimeException extends RuntimeException
{

   protected ErrorEntity errorInfo;
   /**
    * 
    */
   private static final long serialVersionUID = 1L;
   
   public ErrorEntity getErrorInfo() {
      return errorInfo;
   }
}
