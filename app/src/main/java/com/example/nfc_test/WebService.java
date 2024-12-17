package com.example.nfc_test;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;


class WebService {
    public String URL = "";
    private RequestQueue mRequestQueue;
    private StringRequest mStringRequest;
    public String responseData = "";

    protected String sendWebRequest(Context context, String methodName, int methodType, Map<String, String> params, final VolleyCallback callback) {
        try {
            MyVariables.trustAllHttpsCertificates();
        } catch (Exception e) {
            e.printStackTrace();
        }

        //RequestQueue initialized
        mRequestQueue = Volley.newRequestQueue(context);
        //Request.Method.GET
        //String Request initialized
        String oFinalURl = (URL.isEmpty() ? (MyVariables.SCHOOL_WEB_URL + MyVariables.SCHOOL_GROUP_CODE) : URL) + methodName;
        if (!MyVariables.IsProduction) {
            Log.v("OfinalUsrl", oFinalURl);
        }
        mStringRequest = new StringRequest(methodType, oFinalURl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                responseData = response.toString();
                callback.onSuccess(response.toString());

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                if (!MyVariables.IsProduction) {
                    Log.i("Msg", "Error :" + error.toString());
                }
                Toast.makeText(context, error.toString(), Toast.LENGTH_SHORT).show();
                callback.onSuccess("");
                error.printStackTrace();

            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> paramsList = new HashMap<String, String>();

                if (params != null && params.size() > 0) {
                    if (!MyVariables.IsProduction) {
                        Log.e("Parameters", "->" + params.toString());
                    }
                    paramsList.putAll(params);
                }
                return paramsList;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> paramsList = new HashMap<String, String>();
                paramsList.put("Content-Type", "application/x-www-form-urlencoded");
                paramsList.put("Accept", "application/json");

                if (!MyVariables.SCHOOL_WEB_URL.isEmpty()) {
                    if (!MyVariables.MCAMPUS_TOKENID.isEmpty()) {
                        paramsList.put("MCampusTokenID", MyVariables.MCAMPUS_TOKENID);
                        paramsList.put("IMR", "1");
                        paramsList.put("Cookies", "MCampusTokenID=" + MyVariables.MCAMPUS_TOKENID + ";IMR=1;");
                    }
                }
                if (!MyVariables.IsProduction) {
                    Log.e("HEaders", "->" + paramsList.toString());
                }
                return paramsList;
            }
        };

        mStringRequest.setRetryPolicy(new RetryPolicy() {
            @Override
            public int getCurrentTimeout() {
                return 50000;
            }

            @Override
            public int getCurrentRetryCount() {
                return 50000;
            }

            @Override
            public void retry(VolleyError error) throws VolleyError {
                if (!MyVariables.IsProduction) {
                    Log.e("Retry", error.toString());
                }
                error.printStackTrace();
            }
        });

        /*JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, URL + methodName, null,
                new Response.Listener<JSONObject>()
                {
                    @Override
                    public void onResponse(JSONObject response)
                    {
                        Log.e(" result",(String.valueOf(response)));
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }
        ){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();

                return params;
            }
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String> params = new HashMap<String, String>();
                params.put("Content-Type","application/x-www-form-urlencoded");
                params.put("MCampusTokenID","0c06586e-c8fb-44ba-946d-323db5c9e78a");
                params.put("Cookies","MCampusTokenID=0c06586e-c8fb-44ba-946d-323db5c9e78a");
                return params;
            }
        };
        getRequest.setRetryPolicy(new RetryPolicy() {
            @Override
            public int getCurrentTimeout() {
                return 50000;
            }

            @Override
            public int getCurrentRetryCount() {
                return 50000;
            }

            @Override
            public void retry(VolleyError error) throws VolleyError {

            }
        });

        mRequestQueue.add(getRequest);*/
        mRequestQueue.add(mStringRequest);
        return responseData;
    }

    protected String sendWebPostRequest(Context context, String methodName, Map<String, String> params, final VolleyCallback callback) {
        try {
            MyVariables.trustAllHttpsCertificates();
        } catch (Exception e) {
            e.printStackTrace();
        }
        /*JSONObject jsonBody = new JSONObject();
        try {

            if(params != null && params.size() > 0){
                Log.e("Parameters","->"+params.toString());
                for (Map.Entry<String,String> entry : params.entrySet())
                    jsonBody.put(entry.getKey(), entry.getValue());

                Log.e("Parameters2","->"+jsonBody.toString());
            }
        }catch (Exception ex){

        }*/

        //RequestQueue initialized
        mRequestQueue = Volley.newRequestQueue(context);
        //Request.Method.GET
        //String Request initialized
        String oFinalUrl = (URL.isEmpty() ? (MyVariables.SCHOOL_WEB_URL + "/" + MyVariables.SCHOOL_GROUP_CODE) : URL) + methodName;
        mStringRequest = new StringRequest(Request.Method.POST, oFinalUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                responseData = response.toString();
                callback.onSuccess(response.toString());
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Gson gson = new Gson();
                if (!MyVariables.IsProduction) {
                    Log.i("Msg", "Method Name :" + methodName);
                    Log.e("Calling Error:", gson.toJson(error));
                    Log.i("Msg", "Error :" + error.toString());
                }
                error.printStackTrace();
            }

        }) {
            @Override
            protected void deliverResponse(String response) {
                super.deliverResponse(response);
            }

            @Override
            protected Map<String, String> getParams() {
                // below line we are creating a map for
                // storing our values in key and value pair.
                Map<String, String> paramsList = new HashMap<String, String>();

                // on below line we are passing our key
                // and value pair to our parameters.
                paramsList.putAll(params);

                // at last we are
                // returning our params.
                return params;
            }

            /*@Override
            protected Map<String,String> getParams() {
                Map<String,String> paramsList = new HashMap<String, String>();
                paramsList.putAll(params);
                return paramsList;
            }*/

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> paramsList = new HashMap<String, String>();
                paramsList.put("Content-Type", "application/x-www-form-urlencoded");
                paramsList.put("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9");
                paramsList.put("Connection", "Keep-Alive");
                paramsList.put("Accept-Encoding", "gzip");

                if (!MyVariables.SCHOOL_WEB_URL.isEmpty()) {
                    if (!MyVariables.MCAMPUS_TOKENID.isEmpty()) {
                        paramsList.put("MCampusTokenID", MyVariables.MCAMPUS_TOKENID);
                        paramsList.put("IMR", "1");
                        paramsList.put("Cookies", "MCampusTokenID=" + MyVariables.MCAMPUS_TOKENID + ";IMR=1;");
                        paramsList.put("EdusprintPlus", "1");
                    }
                }
                return paramsList;
            }

            @Override
            public byte[] getBody() throws AuthFailureError {
                super.getBody();
                JSONObject jsonBody = new JSONObject();
                try {
                    if (params != null && params.size() > 0) {
                        Log.e("Parameters", "->" + params.toString());
                        for (Map.Entry<String, String> entry : params.entrySet())
                            jsonBody.put(entry.getKey(), entry.getValue());

                        Log.e("Parameters2", "->" + jsonBody.toString());

                    }
                    String requestBody = jsonBody.toString();
                    return requestBody == null ? null : requestBody.getBytes("utf-8");
                } catch (Exception ex) {
                    Log.e("Parameters2", "->" + ex.toString());
                    return null;
                }
            }

/*@Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }*/

            /*@Override
            public byte[] getBody() throws AuthFailureError {
                try {
                    String requestBody = jsonBody.toString();
                    return requestBody == null ? null : requestBody.getBytes("utf-8");
                } catch (UnsupportedEncodingException uee) {
                   // VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", requestBody, "utf-8");
                    return null;
                }
            }*/

            /*@Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                String responseString = "";
                if (response != null) {
                    responseString = String.valueOf(response.data);
                    // can get more details such as response.headers
                    Log.e("ResponseString",response.headers.toString());
                    Log.e("statusCode",String.valueOf(response.statusCode));
                    Log.e("statusCode",response.data.toString());
                }
                return Response.success(responseString, HttpHeaderParser.parseCacheHeaders(response));
            }*/
        };

        mStringRequest.setRetryPolicy(new RetryPolicy() {
            @Override
            public int getCurrentTimeout() {
                return 50000;
            }

            @Override
            public int getCurrentRetryCount() {
                return 50000;
            }

            @Override
            public void retry(VolleyError error) throws VolleyError {
                Log.e("Retry", error.toString());
                error.printStackTrace();
            }
        });
        Log.e("URL", mStringRequest.getUrl());
        mRequestQueue.add(mStringRequest);


        Log.d("Data", responseData);
        return responseData;
    }


    protected String sendPostRequest(String methodName, Map<String, String> params, final VolleyCallback callback) {

        HttpURLConnection connection;
        try {
            URL url = new URL((URL.isEmpty() ? (MyVariables.SCHOOL_WEB_URL + MyVariables.SCHOOL_GROUP_CODE) : URL) + methodName);

            connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            String urlParameters = "";
            int counter = 0;

            if (!params.isEmpty()) {
                for (Map.Entry<String, String> entry : params.entrySet()) {
                    counter++;
                    if (params.size() != counter)
                        urlParameters += (entry.getKey() + "=" + entry.getValue() + "&");
                    else
                        urlParameters += (entry.getKey() + "=" + entry.getValue());
                }
            }


            connection.setRequestMethod("POST");
            connection.setRequestProperty("USER-AGENT", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/101.0.4951.67 Safari/537.36");
            connection.setRequestProperty("ACCEPT-LANGUAGE", "en-US,en;q=0.9");
            connection.setRequestProperty("MCampusTokenID", MyVariables.MCAMPUS_TOKENID);
            connection.setRequestProperty("IMR", "1");
            connection.setRequestProperty("Cookies", "MCampusTokenID=" + MyVariables.MCAMPUS_TOKENID + ";IMR=1;");

            if (!MyVariables.IsProduction) {
                Log.v("Params", connection.getRequestProperties().toString());
            }


            DataOutputStream dataOutputStream = new DataOutputStream(connection.getOutputStream());
            //Log.e("urlParameters",urlParameters);
            dataOutputStream.writeBytes(urlParameters);
            dataOutputStream.flush();
            dataOutputStream.close();

            //connection.connect();

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder data = new StringBuilder();
            String line = "";

            while ((line = bufferedReader.readLine()) != null) {
                data.append(line);
            }
            bufferedReader.close();

            //Log.e("Apioutput",data.toString());
            callback.onSuccess(data.toString());
            responseData = data.toString();

            return responseData;
        } catch (FileNotFoundException ex) {
            if (!MyVariables.IsProduction) {
                Log.e("FileNotFoundException", ex.toString());
                ex.printStackTrace();
            }

            callback.onSuccess(responseData);
            return responseData;
        } catch (Exception ex) {
            if (!MyVariables.IsProduction) {
                Log.e("Ex", ex.toString());
                ex.printStackTrace();
            }

            responseData = ex.toString();
            callback.onSuccess(responseData);
            return responseData;
            //sendPostRequest(methodName,params,callback);
        }

    }

    protected String sendGetRequest(String methodName, Map<String, String> params, final VolleyCallback callback) {

        HttpURLConnection connection;
        try {
            URL url = new URL((URL.isEmpty() ? (MyVariables.SCHOOL_WEB_URL + "/" + MyVariables.SCHOOL_GROUP_CODE) : URL) + methodName);

            connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            String urlParameters = "";
            int counter = 0;

            for (Map.Entry<String, String> entry : params.entrySet()) {
                counter++;
                if (params.size() != counter)
                    urlParameters += (entry.getKey() + "=" + entry.getValue() + "&");
                else
                    urlParameters += (entry.getKey() + "=" + entry.getValue());
            }


            connection.setRequestMethod("GET");
            connection.setRequestProperty("USER-AGENT", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/101.0.4951.67 Safari/537.36");
            connection.setRequestProperty("ACCEPT-LANGUAGE", "en-US,en;q=0.9");

            if (!MyVariables.SCHOOL_WEB_URL.isEmpty()) {
                if (!MyVariables.MCAMPUS_TOKENID.isEmpty()) {
                    connection.setRequestProperty("MCampusTokenID", MyVariables.MCAMPUS_TOKENID);
                    connection.setRequestProperty("IMR", "1");
                    connection.setRequestProperty("Cookies", "MCampusTokenID=" + MyVariables.MCAMPUS_TOKENID + ";IMR=1;");
                }
            }

            //Log.v("Params",connection.getRequestProperties().toString());

            DataOutputStream dataOutputStream = new DataOutputStream(connection.getOutputStream());
            //Log.e("urlParameters",urlParameters);
            dataOutputStream.writeBytes(urlParameters);
            dataOutputStream.flush();
            dataOutputStream.close();

            //connection.connect();

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder data = new StringBuilder();
            String line = "";

            while ((line = bufferedReader.readLine()) != null) {
                data.append(line);
            }
            bufferedReader.close();

            //Log.e("Apioutput",data.toString());
            callback.onSuccess(data.toString());
            responseData = data.toString();

            return responseData;
        } catch (FileNotFoundException ex) {
            Log.e("FileNotFoundException", ex.toString());
            ex.printStackTrace();
            callback.onSuccess(responseData);
            return responseData;
        } catch (Exception ex) {
            Log.e("Ex", ex.toString());
            responseData = ex.toString();
            ex.printStackTrace();
            callback.onSuccess(responseData);
            return responseData;
            //sendPostRequest(methodName,params,callback);
        }

    }
}

interface VolleyCallback {
    void onSuccess(String result);
}
