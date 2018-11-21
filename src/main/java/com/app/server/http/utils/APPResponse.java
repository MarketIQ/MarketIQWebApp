package com.app.server.http.utils;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class APPResponse {
    public boolean success = true;
    public Object data;
    public int httpStatusCode = 200;
    public APPResponse(Object dataParam) {
        this.data = dataParam;
    }

    public APPResponse() {
    }

    public APPResponse(int httpStatusCode) {

        this.httpStatusCode = httpStatusCode;
    }
}