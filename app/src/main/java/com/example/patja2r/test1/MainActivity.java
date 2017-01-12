package com.example.patja2r.test1;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.pushbots.push.Pushbots;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;


public class MainActivity extends AppCompatActivity {

    private EditText enterEmail;
    private EditText enterPhoneNumber;
    private Button btStartSurvey;
    private TextView tvClickInstructions;
    private int clicks = 0;
    private static final String TAG = "MainActivity";

    String emailAddress = "jay@gmail.com";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //binding the text fields
        enterEmail = (EditText) findViewById(R.id.enterEmail);
        enterPhoneNumber = (EditText) findViewById(R.id.enterPhoneNumber);

        emailAddress = enterEmail.getText().toString();

        //binding textView
        tvClickInstructions = (TextView) findViewById(R.id.clickInstructions);

        //initialize push notifications
        Pushbots.sharedInstance().init(this);


        //binding button
        btStartSurvey = (Button) findViewById(R.id.btStartSurvey);

        //set listeners
        enterEmail.addTextChangedListener(textWatcher);
        enterPhoneNumber.addTextChangedListener(textWatcher);

        //run once to disable it if empty
        checkFieldsForEmptyValues();

        // check if you are connected or not

        //setting up listeners on Textview for Instructions pop up
        tvClickInstructions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, PopupWindowActivity.class);
                startActivity(intent);
                clicks++;
            }
        });


        btStartSurvey.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, SurveyActivity.class);
                startActivity(intent);
                new PostAsync().execute();
                new GetAsync().execute();


            }
        });

    }



    private TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            checkFieldsForEmptyValues();
        }

        @Override
        public void afterTextChanged(Editable s) {
            if (clicks < 0) {
                Intent intent = new Intent(MainActivity.this, PopupWindowActivity.class);
                startActivity(intent);
                clicks++;
            }
        }
    };


    private void showJSON(String response) throws JSONException {

        ArrayList<String> questions = new ArrayList<String>();
        JSONObject obj = new JSONObject(response);
        Iterator iterator = obj.keys();
        while (iterator.hasNext()){
            String key = (String)iterator.next();
            questions.add(obj.getString(key));
        }

        for (String question:questions){
            Log.i("Question is ", question);
        }

    }

    public String getPhoneNumber(){

        TelephonyManager tMgr = (TelephonyManager)this.getSystemService(Context.TELEPHONY_SERVICE);
        return tMgr.getLine1Number();
    }

    private void checkFieldsForEmptyValues() {

        String sEmail = enterEmail.getText().toString().trim();
        String sPhoneNumber = enterPhoneNumber.getText().toString().trim();

        btStartSurvey = (Button) findViewById(R.id.btStartSurvey);

        if ((sEmail.length() > 0 && sPhoneNumber.length() > 0)) {
            btStartSurvey.setEnabled(true);
        } else {
            btStartSurvey.setEnabled(false);
        }

    }

    }

class PostAsync extends AsyncTask<String, String, JSONObject> {

    JSONParser jsonParser = new JSONParser();

    private ProgressDialog pDialog;

    private static final String SIGN_IN_URL = "http://108.20.122.99/survey/v1/signin/";

    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";

    final String mdn = "754353453";
    final String deviceId = "45435435534";
    final String email = "jay@gmail.com";
    final String pushId = "5646";
    final String manufacturer = "Google";
    final String model = "65.5";
    final String osVersion = "8.8.8";
    final String product = "some";

    @Override
    protected void onPreExecute() {
        //pDialog = new ProgressDialog(MainActivity.this);
        //pDialog.setMessage("Attempting login...");
        //pDialog.setIndeterminate(false);
        //pDialog.setCancelable(true);
        //pDialog.show();
    }

    @Override
    protected JSONObject doInBackground(String... args) {

        try {

            HashMap<String, String> postParam= new HashMap<String, String>();
            postParam.put("MDN",mdn);
            postParam.put("deviceId",deviceId);
            postParam.put("emailAddress",email);
            postParam.put("pushId",pushId);
            postParam.put("Manufacturer",manufacturer);
            postParam.put("Model",model);
            postParam.put("OSVersion",osVersion);
            postParam.put("Product",product);
            Log.d("request", "starting");

            JSONObject json = jsonParser.makeHttpRequest(
                    SIGN_IN_URL, "POST", postParam);

            if (json != null) {
                Log.d("JSON result", json.toString());

                return json;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    protected void onPostExecute(JSONObject json) {

        int success = 0;
        String message = "";

        if (json != null) {

            try {
                success = json.getInt(TAG_SUCCESS);
                message = json.getString(TAG_MESSAGE);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        

        if (success == 1) {
            Log.d("Success!", message);
        }else{
            Log.d("Failure", message);
        }
    }

}


class GetAsync extends AsyncTask<String, String, JSONObject> {

    JSONParser jsonParser = new JSONParser();

    private ProgressDialog pDialog;

    private static final String RETRIEVE_URL = "http://108.20.122.99/survey/v1/retrieveSurvey/";
    private static final String TAG_SUCCESS = "surveyDate";
    private static final String TAG_MESSAGE = "message";

    @Override
    protected void onPreExecute() {
        //pDialog = new ProgressDialog(MainActivity.this);
        //pDialog.setMessage("Attempting login...");
        //pDialog.setIndeterminate(false);
        //pDialog.setCancelable(true);
        //pDialog.show();
    }

    @Override
    protected JSONObject doInBackground(String... args) {

        try {

            HashMap<String, String> params = new HashMap<>();
            params.put("name", args[0]);
            params.put("password", args[1]);

            Log.d("request", "starting");

            JSONObject json = jsonParser.makeHttpRequest(
                    RETRIEVE_URL, "POST", params);

            if (json != null) {
                Log.d("JSON result", json.toString());

                return json;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    protected void onPostExecute(JSONObject json) {
        super.onPostExecute(json);

        int success = 0;
        String message = "";

        if (json != null) {

            try {
                success = json.getInt(TAG_SUCCESS);
                message = json.getString(TAG_MESSAGE);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        if (success == 1) {
            Log.d("Success!", message);
        }else{
            Log.d("Failure", message);
        }

        /*try {

            ArrayList<String> arr = new ArrayList<String>();
            if (json != null) {

                Iterator iter = json.keys();
                while (iter.hasNext()) {
                    String key = (String) iter.next();
                    arr.add(json.getString(key));
                    System.out.println(json.getString(key));
                }
            }


        } catch (Exception e) {
            e.printStackTrace();
        }*/


    }

}


















    /*class SendDataToServer extends AsyncTask<String,String,String> {

        private static final String TAG = "MainActivity";

        @Override
        protected String doInBackground(String... params) {

            String JsonResponse = null;
            String JsonDATA = params[0];
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            try {
                URL url = new URL("");
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setDoOutput(true);
                // is output buffer writer
                urlConnection.setRequestMethod("POST");
                urlConnection.setRequestProperty("Content-Type", "application/json");
                urlConnection.setRequestProperty("Accept", "application/json");
                urlConnection.setRequestProperty("Authentication", "surveyapp");
                //set headers and method
                Writer writer = new BufferedWriter(new OutputStreamWriter(urlConnection.getOutputStream(), "UTF-8"));
                writer.write(JsonDATA);
                // json data
                writer.close();
                InputStream inputStream = urlConnection.getInputStream();
                //input stream
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    // Nothing to do.
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String inputLine;
                while ((inputLine = reader.readLine()) != null)
                    buffer.append(inputLine + "\n");
                if (buffer.length() == 0) {
                    // Stream was empty. No point in parsing.
                    return null;
                }
                JsonResponse = buffer.toString();
                //response data
                Log.i(TAG,JsonResponse);
                //send to post execute
                return JsonResponse;

            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e(TAG, "Error closing stream", e);
                    }
                }
            }
            return null;

        }


public void sendDataToServer(View v) {
        //function in the activity that corresponds to the layout button

        final String mdn = "1111111";
        final String deviceId = "2222222";
        final String email = emailAddress;
        final String pushId = "4444444";
        final String manufacturer = "Google";
        final String model = "6.6";
        final String osVersion = "7.7.7";
        final String product = "droid";

        JSONObject postJson = new JSONObject();

        try {
            postJson.put("mdn", mdn);
            postJson.put("deviceId", deviceId);
            postJson.put("emailAddress", emailAddress);
            postJson.put("pushId", pushId);
            postJson.put("Manufacturer", manufacturer);
            postJson.put("Model", model);
            postJson.put("OSVersion", osVersion);
            postJson.put("Product", product);


        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (postJson.length() > 0) {
            new SendDataToServer().execute(String.valueOf(postJson));

        }

        Toast.makeText(getBaseContext(),"User Registered Successfully", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(MainActivity.this, SurveyActivity.class);
        startActivity(intent);
    }



        @Override
        protected void onPostExecute(String s) {
        }


        public void registerUser(){

        final String mdn = "1111111";
        final String deviceId = "2222222";
        final String email = emailAddress;
        final String pushId = "4444444";
        final String manufacturer = "Google";
        final String model = "6.6";
        final String osVersion ="7.7.7";
        final String product = "droid";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, SIGN_IN_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(MainActivity.this, response,Toast.LENGTH_SHORT).show();

            }
        },
                new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse (VolleyError error){
                        Toast.makeText(MainActivity.this,error.toString(),Toast.LENGTH_SHORT).show();
                    }
                }){
            @Override
            protected Map<String, String> getParams(){

                Map<String, String> params = new HashMap<String, String>();
                params.put(KEY_MDN,mdn);
                params.put(KEY_DEVICEID,deviceId);
                params.put(KEY_EMAIL,email);
                params.put(KEY_PUSHID,pushId);
                params.put(KEY_MANUFACTURER,manufacturer);
                params.put(KEY_MODEL,model);
                params.put(KEY_OSVERSION,osVersion);
                params.put(KEY_PRODUCT,product);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }


    public void onClick (View v){

        if (v==btStartSurvey){
            registerUser();
        }
    }


*/

/*
* btStartSurvey.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                final String mdn = "1111111";
                final String deviceId = "2222222";
                final String email = "jay@gmail.com";
                final String pushId = "4444444";
                final String manufacturer = "Google";
                final String model = "6.6";
                final String osVersion ="7.7.7";
                final String product = "droid";

                Response.Listener<String> responseListener = new Response.Listener<String>(){

                    @Override
                    public void onResponse(String response) {
                        JSONObject jsonObject = null;

                        try {

                            jsonObject = new JSONObject(response);
                                jsonObject.put(mdn,"111111");
                                jsonObject.put(deviceId,"222222");
                                jsonObject.put(email,emailAddress);
                                jsonObject.put(pushId,"fhffsgfs");
                                jsonObject.put(manufacturer,"Google");
                                jsonObject.put(model,"6.6");
                                jsonObject.put(osVersion,"7.7.7");
                                jsonObject.put(product,"droid");
                                Toast.makeText(getBaseContext(),"User Registered Successfully", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(MainActivity.this, SurveyActivity.class);
                                startActivity(intent);
                                sendRequest();


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                };

                RegisterUserRequest registerUserRequest = new RegisterUserRequest(mdn,deviceId,email,pushId, manufacturer,model,osVersion,product, responseListener);
                RequestQueue queue = Volley.newRequestQueue(MainActivity.this);
                queue.add(registerUserRequest);

                }
        });



        private void makeJsonObjectRequest() {

        Map<String, String> postParam= new HashMap<String, String>();
        postParam.put("MDN",mdn);
        postParam.put("deviceId",deviceId);
        postParam.put("emailAddress",emailAddress);
        postParam.put("pushId",pushId);
        postParam.put("Manufacturer",manufacturer);
        postParam.put("Model",model);
        postParam.put("OSVersion",osVersion);
        postParam.put("Product",product);


        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                SIGN_IN_URL, new JSONObject(postParam),
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, response.toString());
                        Intent intent = new Intent(MainActivity.this, SurveyActivity.class);
                        startActivity(intent);
                        Toast.makeText(getBaseContext(),"User Registered Successfully", Toast.LENGTH_SHORT).show();

                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
            }
        }) {

            /**
             * Passing some request headers
             * */
/*@Override
public Map<String, String> getHeaders() throws AuthFailureError {
    HashMap<String, String> headers = new HashMap<String, String>();
    headers.put("Content-Type", "application/json");
    headers.put("Accept", "application/json");
    headers.put("Authentication", "surveyapp");
    return headers;
}



};

// Adding request to request queue


// Cancelling request
// ApplicationController.getInstance().getRequestQueue().cancelAll(tag_json_obj);
}

*
*
* */