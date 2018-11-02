package com.app.server.http.exceptions;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;


public class APPUnauthorizedException extends WebApplicationException {

    public APPUnauthorizedException(int errorCode, String errorMessage) {
        super(Response.status(Status.UNAUTHORIZED).entity(new APPExceptionInfo(
                Status.UNAUTHORIZED.getStatusCode(),
                Status.UNAUTHORIZED.getReasonPhrase(),
                errorCode,
                errorMessage)
        ).type("application/json").build());
    }

    public APPUnauthorizedException(int errorCode){
        super(Response.status(Status.UNAUTHORIZED).entity(new APPExceptionInfo(
                Status.UNAUTHORIZED.getStatusCode(),
                Status.UNAUTHORIZED.getReasonPhrase(),
                errorCode)
        ).type("application/json").build());
    }
}