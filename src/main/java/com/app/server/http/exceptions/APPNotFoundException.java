package com.app.server.http.exceptions;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

/**
 * Create 500
 */
public class APPNotFoundException extends WebApplicationException {
    public APPNotFoundException(int errorCode, String errorMessage) {
        super(Response.status(Status.NOT_FOUND).entity(new APPExceptionInfo(
                Status.NOT_FOUND.getStatusCode(),
                Status.NOT_FOUND.getReasonPhrase(),
                errorCode,
                errorMessage)
        ).type("application/json").build());
    }

    public APPNotFoundException(int errorCode) {
        super(Response.status(Status.NOT_FOUND).entity(new APPExceptionInfo(
                Status.NOT_FOUND.getStatusCode(),
                Status.NOT_FOUND.getReasonPhrase(),
                errorCode)
        ).type("application/json").build());
    }
}

