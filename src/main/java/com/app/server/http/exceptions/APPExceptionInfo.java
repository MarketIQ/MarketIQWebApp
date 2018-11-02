package com.app.server.http.exceptions;


public class APPExceptionInfo {

    private int httpStatusCode;
    private String httpStatusMessage;
    private int errorCode;
    private String errorMessage;

    public APPExceptionInfo(int hsc, String hsm, int ec, String em) {
        this.httpStatusCode=hsc;
        this.httpStatusMessage=hsm;
        this.errorCode=ec;
        this.errorMessage = em;
    }
    public APPExceptionInfo(int hsc, String hsm, int ec) {
        this.httpStatusCode=hsc;
        this.httpStatusMessage=hsm;
        this.errorCode=ec;
        this.errorMessage = "No message";
    }

    public int getHttpStatusCode() { return httpStatusCode; }
    public String getHttpStatusMessage() { return httpStatusMessage; }
    public int getErrorCode() { return errorCode; }
    public String getErrorMessage() {return errorMessage; }
}
