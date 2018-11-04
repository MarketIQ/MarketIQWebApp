package com.app.server.http.exceptions;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;


public class APPBadRequestException extends WebApplicationException {

    public APPBadRequestException(int errorCode, String errorMessage) {
        super(Response.status(Status.BAD_REQUEST).entity(new APPExceptionInfo(
                Status.BAD_REQUEST.getStatusCode(),
                Status.BAD_REQUEST.getReasonPhrase(),
                errorCode,
                errorMessage)
        ).type("application/json").build());
    }

    public APPBadRequestException(int errorCode) {
        super(Response.status(Status.BAD_REQUEST).entity(new APPExceptionInfo(
                Status.BAD_REQUEST.getStatusCode(),
                Status.BAD_REQUEST.getReasonPhrase(),
                errorCode)
        ).type("application/json").build());
    }

}