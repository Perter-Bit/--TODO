package com.example.homework.utils;

import java.util.Map;

public class RequestData {
    private Map<String, String> params;
    private String extraParam;

    public RequestData(Map<String, String> params, String extraParam) {
        this.params = params;
        this.extraParam = extraParam;
    }

    public Map<String, String> getParams() {
        return params;
    }

    public String getExtraParam() {
        return extraParam;
    }
}