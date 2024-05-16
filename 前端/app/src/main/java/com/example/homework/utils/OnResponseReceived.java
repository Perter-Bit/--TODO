package com.example.homework.utils;

import okhttp3.Response;

public interface OnResponseReceived {
    void onPostResponse(Response response);
    void onPostResponseError(Exception e);
}
