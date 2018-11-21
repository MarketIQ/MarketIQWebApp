package com.app.server.http.exceptions;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

public class APPConflictException extends WebApplicationException {

    public APPConflictException(int errorCode, String errorMessage) {
        super(Response.status(Response.Status.CONFLICT).entity(new APPExceptionInfo(
                Response.Status.CONFLICT.getStatusCode(),
                Response.Status.CONFLICT.getReasonPhrase(),
                errorCode,
                errorMessage)
        ).type("application/json").build());
    }

    public APPConflictException(int errorCode) {
        super(Response.status(Response.Status.CONFLICT).entity(new APPExceptionInfo(
                Response.Status.CONFLICT.getStatusCode(),
                Response.Status.CONFLICT.getReasonPhrase(),
                errorCode)
        ).type("application/json").build());
    }
}
