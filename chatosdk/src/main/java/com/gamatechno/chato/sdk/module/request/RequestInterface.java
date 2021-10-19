package com.gamatechno.chato.sdk.module.request;



import com.gamatechno.ggfw.utils.InputStreamVolleyRequest;
import com.gamatechno.ggfw.utils.VolleyMultipartRequest;

import org.json.JSONObject;

import java.util.Map;

public interface RequestInterface {

    public interface OnPostRequest{
        void onPreExecuted();

        void onSuccess(JSONObject response);

        void onFailure(String error);

        Map<String, String> requestParam();

        Map<String, String> requestHeaders();
    }

    public interface OnAuthPostRequest{
        void onPreExecuted();

        void onSuccess(JSONObject response);

        void onFailure(String error);

        void onUnauthorized(String error);

        Map<String, String> requestParam();

        Map<String, String> requestHeaders();
    }

    public interface OnDownloadRequest{
        void onPreExecuted();

        void onSuccess(byte[] response, InputStreamVolleyRequest i);

        void onFailure(String error);

        Map<String, String> requestParam();

        Map<String, String> requestHeaders();
    }

    public interface OnGetRequest{
        void onPreExecuted();

        void onSuccess(JSONObject response);

        void onFailure(String error);

        Map<String, String> requestParam();

        Map<String, String> requestHeaders();
    }

    public interface OnAuthGetRequest{
        void onPreExecuted();

        void onSuccess(JSONObject response);

        void onFailure(String error);

        void onUnauthorized(String error);

        Map<String, String> requestParam();

        Map<String, String> requestHeaders();
    }

    public interface OnMultipartRequest{
        Map<String, VolleyMultipartRequest.DataPart> requestData();

        void onPreExecuted();

        void onSuccess(JSONObject response);

        void onFailure(String error);

        Map<String, String> requestParam();

        Map<String, String> requestHeaders();

    }

}
