package com.web.provider;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import com.web.entity.ErrorEntity;
import com.web.exception.WebApiRuntimeException;

@Provider
public class CustomExceptionHandler implements ExceptionMapper<WebApiRuntimeException>
{

   @Override
   public Response toResponse(WebApiRuntimeException exception)
   {
      ErrorEntity error = exception.getErrorInfo();
      return Response.status(error.getHttpCode()).entity(error)
            .type(MediaType.APPLICATION_JSON).build();
   }

}
