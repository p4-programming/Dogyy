package com.aks.doggydoo.videocall;

public interface WebResponse
{
    void onWebResponse(String response, int callCode);
    void onWebResponseError(String error, int callCode);
}
