package com.app.server.http.utils;

public class APPResponse {
    public boolean success = true;
    public Object data;
    public int httpStatusCode = 200;
    public APPResponse(Object dataParam) {
        this.data = dataParam;
    }

    public APPResponse() {
    }
}