package com.app.server.http.exceptions;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.WebApplicationException;

/**
 * Create 500
 */
public class APPInternalServerException extends WebApplicationException {
    public APPInternalServerException(int errorCode, String errorMessage) {
        super(Response.status(Status.INTERNAL_SERVER_ERROR).entity(new APPExceptionInfo(
                Status.INTERNAL_SERVER_ERROR.getStatusCode(),
                Status.INTERNAL_SERVER_ERROR.getReasonPhrase(),
                errorCode,
                errorMessage)
        ).type("application/json").build());
    }

    public APPInternalServerException(int errorCode) {
        super(Response.status(Status.INTERNAL_SERVER_ERROR).entity(new APPExceptionInfo(
                Status.INTERNAL_SERVER_ERROR.getStatusCode(),
                Status.INTERNAL_SERVER_ERROR.getReasonPhrase(),
                errorCode)
        ).type("application/json").build());
    }
}