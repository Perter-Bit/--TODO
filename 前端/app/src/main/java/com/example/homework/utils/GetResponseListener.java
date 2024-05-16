package com.example.homework.utils;

import okhttp3.Response;

public interface GetResponseListener
{
    void onGetResponse(Response response);
    void onGetResponseError(Exception e);
}
