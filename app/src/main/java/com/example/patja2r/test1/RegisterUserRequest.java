package com.example.patja2r.test1;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by patja2r on 6/3/2016.
 */
public class RegisterUserRequest extends StringRequest {

    private static final String SIGN_IN_URL = "http://108.20.122.99/survey/v1/signin/";
    private Map<String, String> params;
    private JSONObject jsonObject;


    public RegisterUserRequest(String mdn,String deviceId,String emailAddress,String pushId,String manufacturer,String model,String osVersion,String product,Response.Listener<String> responseListener) {
        super(Request.Method.POST, SIGN_IN_URL, responseListener,null);

        params = new HashMap<>();
        Map<String, String> params = new HashMap<String, String>();
        params.put("MDN",mdn);
        params.put("deviceId",deviceId);
        params.put("emailAddress",emailAddress);
        params.put("pushId",pushId);
        params.put("Manufacturer",manufacturer);
        params.put("Model",model);
        params.put("OSVersion",osVersion);
        params.put("Product",product);

        JSONObject jsonObject = new JSONObject(params);

    }

    public Map <String, String> getHeaders() throws AuthFailureError {

        Map<String, String> params = new HashMap<String,String>();
        params.put("Content-Type","application/json");
        params.put("Accept","application/json");
        params.put("Authentication","surveyapp");



        return params;
    }

    /*@Override
    protected Response<String> parseNetworkResponse(NetworkResponse response){

        try{

            String json = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
            //return Response.success(json,);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }*/
}
