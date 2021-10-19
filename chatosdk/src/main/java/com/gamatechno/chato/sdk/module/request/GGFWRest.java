package com.gamatechno.chato.sdk.module.request;

import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.os.Build;
import androidx.annotation.RequiresApi;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.gamatechno.chato.sdk.module.core.ChatoBaseApplication;
import com.gamatechno.ggfw.utils.InputStreamVolleyRequest;
import com.gamatechno.ggfw.utils.VolleyMultipartRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.Map;

import static com.gamatechno.ggfw.string.GGFWString.DateInMilis;
import static com.gamatechno.ggfw.utils.GGFWUtil.volleyError;


public class GGFWRest {
    public static void POST(final String URL, final RequestInterface.OnPostRequest onPostRequest){
        /*Log.d("iki opo", new Throwable().getStackTrace()[1].getClassName());*/
//        StringRequest request = new StringRequest(RequestInterface.Method.POST, RESTUtil.URL(new Throwable().getStackTrace()[1].getClass(), new Throwable().getStackTrace()[1].getMethodName()),
        StringRequest request = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Log.d("asdResponse "+URL, "onResponse: "+response);
                            JSONObject jsonObject = new JSONObject(response);
                            onPostRequest.onSuccess(jsonObject);
                        } catch (JSONException e) {
                            onPostRequest.onFailure(e.toString());
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                showVolleyError(context, error);
                Log.d("asdResponseFailure ", "onResponse: "+error.getMessage());
                String errorCode = CODE_NETWORKERROR;
                if (error instanceof NetworkError) {
                    errorCode = CODE_NETWORKERROR;
                } else if (error instanceof ServerError) {
                    errorCode = CODE_SERVERERROR;
                } else if (error instanceof NoConnectionError) {
                    errorCode = CODE_NOCONNECTIONERROR;
                } else if (error instanceof TimeoutError) {
                    errorCode = CODE_TIMEOUTERROR;
                }
                onPostRequest.onFailure(errorCode);
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> param;
                param = onPostRequest.requestParam();
                Log.d("asdParams "+URL, "requestParam: "+param);
                return param;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> param;
                param = onPostRequest.requestHeaders();
                Log.d("asdHeaders "+URL, " requestParam: "+param);
                return param;
            }

            @Override
            public String getBodyContentType() {
//                return super.getBodyContentType();
                return "application/x-www-form-urlencoded";
            }
        };
        ChatoBaseApplication.Companion.getInstance().addToChatoRequestQueue(request, DateInMilis()+URL);
        onPostRequest.onPreExecuted();
    }

    public static void GET(final String URL, final RequestInterface.OnGetRequest onGetRequest){
        /*final JsonObjectRequest request = new JsonObjectRequest(RequestInterface.Method.GET,
                URL+GGFWString.ParamConversion(onGetRequest.requestParam()), "", new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                onGetRequest.onSuccess(response);
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                showVolleyError(context, error);
                onGetRequest.onFailure(error.toString());
            }
        }){

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return onGetRequest.requestHeaders();
            }

        };*/
        StringRequest request = new StringRequest(Request.Method.GET, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Log.d("asdResponse ", URL+" onResponse: "+response);
                            JSONObject jsonObject = new JSONObject(response);
                            onGetRequest.onSuccess(jsonObject);
                        } catch (JSONException e) {
                            onGetRequest.onFailure(e.toString());
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                showVolleyError(context, error);
                String errorCode = CODE_NETWORKERROR;
                if (error instanceof NetworkError) {
                    errorCode = CODE_NETWORKERROR;
                } else if (error instanceof ServerError) {
                    errorCode = CODE_SERVERERROR;
                } else if (error instanceof NoConnectionError) {
                    errorCode = CODE_NOCONNECTIONERROR;
                } else if (error instanceof TimeoutError) {
                    errorCode = CODE_TIMEOUTERROR;
                }
                onGetRequest.onFailure(errorCode);
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> param;
                param = onGetRequest.requestParam();
                Log.d("asdParams", URL+" requestParam: "+param);
                return param;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> param;
                param = onGetRequest.requestHeaders();
                Log.d("asdHeaders ", URL+" requestParam: "+param);
                return param;
            }

            @Override
            public String getBodyContentType() {
//                return super.getBodyContentType();
                return "application/x-www-form-urlencoded";
            }
        };
        request.setShouldCache(false);
        ChatoBaseApplication.Companion.getInstance().addToChatoRequestQueue(request, DateInMilis()+URL);
        onGetRequest.onPreExecuted();
    }

    public static void POSTAuth(final String URL, final Context context, final RequestInterface.OnAuthPostRequest onPostRequest){
        /*Log.d("iki opo", new Throwable().getStackTrace()[1].getClassName());*/
//        StringRequest request = new StringRequest(RequestInterface.Method.POST, RESTUtil.URL(new Throwable().getStackTrace()[1].getClass(), new Throwable().getStackTrace()[1].getMethodName()),
        StringRequest request = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Log.d("asdResponse "+URL, "onResponse: "+response);
                            JSONObject jsonObject = new JSONObject(response);
                            onPostRequest.onSuccess(jsonObject);
                        } catch (JSONException e) {
                            onPostRequest.onFailure(e.toString());
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                showVolleyError(context, error);
                String errorCode = CODE_NETWORKERROR;
                if (error instanceof NetworkError) {
                    errorCode = CODE_NETWORKERROR;
                } else if (error instanceof ServerError) {
                    errorCode = CODE_SERVERERROR;
                } else if (error instanceof NoConnectionError) {
                    errorCode = CODE_NOCONNECTIONERROR;
                } else if (error instanceof TimeoutError) {
                    errorCode = CODE_TIMEOUTERROR;
                }
                onPostRequest.onFailure(errorCode);
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> param;
                param = onPostRequest.requestParam();
                Log.d("asdParams "+URL, "requestParam: "+param);
                return param;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> param;
                param = onPostRequest.requestHeaders();
                Log.d("asdHeaders "+URL, "requestParam: "+param);
                return param;
            }

            @Override
            public String getBodyContentType() {
//                return super.getBodyContentType();
                return "application/x-www-form-urlencoded";
            }
        };
        ChatoBaseApplication.Companion.getInstance().addToChatoRequestQueue(request, DateInMilis()+URL);
        onPostRequest.onPreExecuted();
    }

    public static void GETAuth(final String URL, final Context context, final RequestInterface.OnAuthGetRequest onGetRequest){
        /*final JsonObjectRequest request = new JsonObjectRequest(RequestInterface.Method.GET,
                URL+GGFWString.ParamConversion(onGetRequest.requestParam()), "", new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                onGetRequest.onSuccess(response);
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                showVolleyError(context, error);
                onGetRequest.onFailure(error.toString());
            }
        }){

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return onGetRequest.requestHeaders();
            }

        };*/
        StringRequest request = new StringRequest(Request.Method.GET, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Log.d("asdResponse ", URL+" onResponse: "+response);
                            JSONObject jsonObject = new JSONObject(response);
                            onGetRequest.onSuccess(jsonObject);
                        } catch (JSONException e) {
                            onGetRequest.onFailure(e.toString());
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                showVolleyError(context, error);
                Log.d("asdResponseFailure ", "onResponse: "+error.getMessage());
                String errorCode = CODE_NETWORKERROR;
                if (error instanceof AuthFailureError) {
                    onGetRequest.onUnauthorized(volleyError(context, error));
                } else {
                    if (error instanceof NetworkError) {
                        errorCode = CODE_NETWORKERROR;
                    } else if (error instanceof ServerError) {
                        errorCode = CODE_SERVERERROR;
                    } else if (error instanceof NoConnectionError) {
                        errorCode = CODE_NOCONNECTIONERROR;
                    } else if (error instanceof TimeoutError) {
                        errorCode = CODE_TIMEOUTERROR;
                    }
                    onGetRequest.onFailure(errorCode);
                }
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> param;
                param = onGetRequest.requestParam();
                Log.d("asdParams", URL+" requestParam: "+param);
                return param;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> param;
                param = onGetRequest.requestHeaders();
                Log.d("asdHeaders ", URL+"requestParam: "+param);
                return param;
            }

            @Override
            public String getBodyContentType() {
//                return super.getBodyContentType();
                return "application/x-www-form-urlencoded";
            }
        };
        request.setShouldCache(false);
        ChatoBaseApplication.Companion.getInstance().addToChatoRequestQueue(request, DateInMilis()+URL);
        onGetRequest.onPreExecuted();
    }

    public static void POSTMultipart(final String URL, final Context context, final RequestInterface.OnMultipartRequest onPostRequest){
        VolleyMultipartRequest multipartRequest = new VolleyMultipartRequest(Request.Method.POST, URL, new Response.Listener<NetworkResponse>() {
            @Override
            public void onResponse(NetworkResponse response) {
                String resultResponse = new String(response.data);
                try {
                    JSONObject result = new JSONObject(resultResponse);
                    onPostRequest.onSuccess(result);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                showVolleyError(context, error);
                NetworkResponse networkResponse = error.networkResponse;
                Log.d("asdResponseFailure ", "onResponse: "+URL);
                String errorCode = CODE_NETWORKERROR;
                if (error instanceof NetworkError) {
                    errorCode = CODE_NETWORKERROR;
                } else if (error instanceof ServerError) {
                    errorCode = CODE_SERVERERROR;
                } else if (error instanceof NoConnectionError) {
                    errorCode = CODE_NOCONNECTIONERROR;
                } else if (error instanceof TimeoutError) {
                    errorCode = CODE_TIMEOUTERROR;
                }
                onPostRequest.onFailure(errorCode);
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> param;
                param = onPostRequest.requestHeaders();
                Log.d("asdHeaders ", URL+"requestParam: "+param);
                return param;
            }

            @Override
            protected Map<String, String> getParams() {
                return onPostRequest.requestParam();
            }

            @Override
            protected Map<String, VolleyMultipartRequest.DataPart> getByteData() {
                return onPostRequest.requestData();
            }
        };
//        multipartRequest.setRetryPolicy(new DefaultRetryPolicy( 30000, 1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        multipartRequest.setShouldCache(false);
        ChatoBaseApplication.Companion.getInstance().addToChatoRequestQueue(multipartRequest, DateInMilis()+URL);
        onPostRequest.onPreExecuted();
    }

    public static InputStreamVolleyRequest request;
    public static void POSTDownload(String URL, final Context context, final RequestInterface.OnDownloadRequest onPostRequest){

        request = new InputStreamVolleyRequest(Request.Method.POST, URL,
                new Response.Listener<byte[]>() {
                    @Override
                    public void onResponse(byte[] response) {
                        try {
                            onPostRequest.onSuccess(response, request);
                        }catch (NullPointerException e){

                        }
                    }
                } ,new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                // TODO handle the error
                error.printStackTrace();
                onPostRequest.onFailure(error.toString());
            }
        }, onPostRequest.requestParam());
        RequestQueue mRequestQueue = Volley.newRequestQueue(context, new HurlStack());
        mRequestQueue.add(request);
        onPostRequest.onPreExecuted();
    }

    @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
    public static boolean DownloadFile(Context context, Uri uri, String title, File file, String fileDir){
        DownloadManager downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        DownloadManager.Request req = new DownloadManager.Request(uri);
        if(!file.exists()){
            req.setDestinationInExternalFilesDir(context, fileDir,title);
            downloadManager.enqueue(req);
            return true;
        } else{
            return false;
        }
    }

//    public final static String CODE_SERVERERROR = "503";
    public final static String CODE_SERVERERROR = "Telah terjadi kesalahan pada sistem. Silahkan coba lagi";
//    public final static String CODE_NETWORKERROR = "1012";
    public final static String CODE_NETWORKERROR = "Periksa kembali koneksi internet atau wifi anda dan silahkan coba lagi";
//    public final static String CODE_NOCONNECTIONERROR = "1019";
    public final static String CODE_NOCONNECTIONERROR = "Periksa kembali koneksi internet atau wifi anda dan silahkan coba lagi.";
//    public final static String CODE_TIMEOUTERROR = "522";
    public final static String CODE_TIMEOUTERROR = "Jaringan terlalu lama merespon";


}