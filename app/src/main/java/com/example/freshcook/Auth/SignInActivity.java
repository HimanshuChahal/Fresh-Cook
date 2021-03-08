package com.example.freshcook.Auth;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.ServerError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.freshcook.HomeDashBoard.HomePageActivity;
import com.example.freshcook.MainActivity;
import com.example.freshcook.R;
import com.example.freshcook.Utilities;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class SignInActivity extends AppCompatActivity {

    boolean logIn;
    Button signInSignUpButton;
    LinearLayout signInLinearLayout;
    LinearLayout signUpLinearLayout;
    Button signInButton;
    Button signUpButton;
    EditText signInEmailEditText;
    EditText signInPasswordEditText;
    EditText signUpPhoneNumberEditText;
    EditText signUpNameEditText;
    EditText signUpEmailIDEditText;
    EditText signUpPasswordEditText;

    public void loginSelectOnClick(View view)
    {
        signInButton=(Button) view;
        underlineButtonText((Button) view, signUpButton);
        signInLinearLayout.setVisibility(View.VISIBLE);
        signUpLinearLayout.setVisibility(View.GONE);
        logIn=true;
    }

    public void signUpSelectOnClick(View view)
    {
        signUpButton=(Button) view;
        underlineButtonText((Button) view, signInButton);
        signInLinearLayout.setVisibility(View.GONE);
        signUpLinearLayout.setVisibility(View.VISIBLE);
        logIn=false;
    }

    public void underlineButtonText(Button button, Button button2)
    {
        SpannableString spannableString=new SpannableString(button.getText().toString());
        signInSignUpButton.setText(button.getText().toString());
        spannableString.setSpan(new UnderlineSpan(), 0, spannableString.length(), SpannableString.SPAN_EXCLUSIVE_INCLUSIVE);
        button.setText(spannableString);
        String buttonText=button2.getText().toString();
        button2.setText(buttonText);
    }

    public void initialise()
    {
        signInSignUpButton=findViewById(R.id.signInSignUpButton);
        signInLinearLayout=findViewById(R.id.signInLinearLayout);
        signUpLinearLayout=findViewById(R.id.signUpLinearLayout);
        signInEmailEditText=findViewById(R.id.signInEmailEditText);
        signInPasswordEditText=findViewById(R.id.signInPasswordEditText);
        signUpPhoneNumberEditText=findViewById(R.id.signUpPhoneNumberEditText);
        signUpNameEditText=findViewById(R.id.signUpNameEditText);
        signUpEmailIDEditText=findViewById(R.id.signUpEmailIDEditText);
        signUpPasswordEditText=findViewById(R.id.signUpPasswordEditText);
        logIn=true;
        signInButton=new Button(SignInActivity.this);
        signUpButton=new Button(SignInActivity.this);

        signInSignUpButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(logIn)
                {

                    if(textIsEntered(signInEmailEditText) && textIsEntered(signInPasswordEditText))
                    {
                        SignIn signIn=new SignIn(SignInActivity.this);
                        signIn.doInBackground(signInEmailEditText.getText().toString().trim(), signInPasswordEditText.getText().toString().trim());
                    } else
                    {
                        Toast.makeText(SignInActivity.this, "Please enter the sign in credentials", Toast.LENGTH_SHORT).show();
                    }

                } else
                {

                    if(textIsEntered(signUpPhoneNumberEditText) && textIsEntered(signUpNameEditText) && textIsEntered(signUpEmailIDEditText) && textIsEntered(signUpPasswordEditText))
                    {
                        Register register=new Register(SignInActivity.this);
                        register.doInBackground(signUpNameEditText.getText().toString().trim(), signUpEmailIDEditText.getText().toString().trim(),
                                signUpPhoneNumberEditText.getText().toString().trim(), signUpPasswordEditText.getText().toString().trim());
                    } else
                    {
                        Toast.makeText(SignInActivity.this, "Please enter sign up credentials", Toast.LENGTH_SHORT).show();
                    }

                }
            }
        });

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        initialise();

    }

    public boolean textIsEntered(EditText editText)
    {
        return !TextUtils.isEmpty(editText.getText().toString().trim());
    }

    @Override
    public void finish() {
        super.finish();

        overridePendingTransition(R.anim.animstart, R.anim.animend);

    }

    static class Register extends AsyncTask<String, Void, Void>
    {

        Context context;
        ProgressDialog loadingProgressDialog;

        public Register(Context context)
        {
            this.context=context;
        }

        @Override
        protected Void doInBackground(final String... strings) {

            loadingProgressDialog=new ProgressDialog(context);
            loadingProgressDialog.setTitle("Registering");
            loadingProgressDialog.setCancelable(false);
            loadingProgressDialog.show();

            StringRequest request=new StringRequest(StringRequest.Method.POST, Utilities.registrationURL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    if(loadingProgressDialog!=null && loadingProgressDialog.isShowing())
                    {
                        loadingProgressDialog.dismiss();
                    }

                    Log.i("Response", response);

                    try {
                        JSONObject jsonObject=new JSONObject(response);

                        Toast.makeText(context, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                    if(loadingProgressDialog!=null && loadingProgressDialog.isShowing())
                    {
                        loadingProgressDialog.dismiss();
                    }

                    NetworkResponse response = error.networkResponse;

                    if (error instanceof ServerError && response != null) {
                        try {
                            String res = new String(response.data,
                                    HttpHeaderParser.parseCharset(response.headers, "utf-8"));

                            JSONObject jsonObject = new JSONObject(res);

                            Toast.makeText(context, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();

                        } catch (UnsupportedEncodingException e1) {
                            // Couldn't properly decode data to string
                            e1.printStackTrace();
                        } catch (JSONException e2) {
                            // returned data is not JSONObject?
                            e2.printStackTrace();
                        }
                    }

                }
            })
            {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {

                    HashMap<String, String> hashMap=new HashMap<>();

                    hashMap.put("name", strings[0]);
                    hashMap.put("email", strings[1]);
                    hashMap.put("mobile", strings[2]);
                    hashMap.put("password", strings[3]);

                    return hashMap;
                }
            };

            RetryPolicy policy=new DefaultRetryPolicy(Utilities.maxTimeOut, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
            request.setRetryPolicy(policy);

            RequestQueue queue= Volley.newRequestQueue(context);
            queue.add(request);

            return null;
        }
    }

    static class SignIn extends AsyncTask<String, Void, Void>
    {

        Context context;
        ProgressDialog loadingProgressDialog;

        public SignIn(Context context)
        {
            this.context=context;
        }

        @Override
        protected Void doInBackground(final String... strings) {

            loadingProgressDialog=new ProgressDialog(context);
            loadingProgressDialog.setTitle("Checking info.....");
            loadingProgressDialog.setCancelable(false);
            loadingProgressDialog.show();

            StringRequest request=new StringRequest(StringRequest.Method.POST, Utilities.signInURL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    if(loadingProgressDialog!=null && loadingProgressDialog.isShowing())
                    {
                        loadingProgressDialog.dismiss();
                    }

                    Log.i("Response", response);

                    try {
                        JSONObject jsonObject=new JSONObject(response);

                        Toast.makeText(context, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();

                        if(jsonObject.getString("message").toLowerCase().equals("Login Successfully".toLowerCase()))
                        {
                            MainActivity.loggedIn.edit().putBoolean("loggedIn", true).apply();
                            MainActivity.loggedIn.edit().putString("id", jsonObject.getJSONObject("payload").getString("id")).apply();
                            MainActivity.loggedIn.edit().putString("email", jsonObject.getJSONObject("payload").getString("email")).apply();
                            MainActivity.loggedIn.edit().putString("userName", jsonObject.getJSONObject("payload").getString("name")).apply();
                            MainActivity.loggedIn.edit().putString("phoneNumber", jsonObject.getJSONObject("payload").getString("contact")).apply();
                            moveToHomePage();
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                    if(loadingProgressDialog!=null && loadingProgressDialog.isShowing())
                    {
                        loadingProgressDialog.dismiss();
                    }

                    NetworkResponse response = error.networkResponse;

                    if (error instanceof ServerError && response != null) {
                        try {
                            String res = new String(response.data,
                                    HttpHeaderParser.parseCharset(response.headers, "utf-8"));

                            JSONObject jsonObject = new JSONObject(res);

                            Toast.makeText(context, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();

                            if(jsonObject.getString("message").toLowerCase().equals("Login Successfully".toLowerCase()))
                            {
                                MainActivity.loggedIn.edit().putBoolean("loggedIn", true).apply();
                                MainActivity.loggedIn.edit().putString("id", jsonObject.getJSONObject("payload").getString("id")).apply();
                                MainActivity.loggedIn.edit().putString("email", jsonObject.getJSONObject("payload").getString("email")).apply();
                                MainActivity.loggedIn.edit().putString("userName", jsonObject.getJSONObject("payload").getString("name")).apply();
                                MainActivity.loggedIn.edit().putString("phoneNumber", jsonObject.getJSONObject("payload").getString("contact")).apply();
                                moveToHomePage();
                            }

                        } catch (UnsupportedEncodingException e1) {
                            // Couldn't properly decode data to string
                            e1.printStackTrace();
                        } catch (JSONException e2) {
                            // returned data is not JSONObject?
                            e2.printStackTrace();
                        }
                    }

                }
            })
            {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {

                    HashMap<String, String> hashMap=new HashMap<>();

                    hashMap.put("email", strings[0]);
                    hashMap.put("password", strings[1]);

                    return hashMap;
                }
            };

            RetryPolicy policy=new DefaultRetryPolicy(Utilities.maxTimeOut, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
            request.setRetryPolicy(policy);

            RequestQueue queue= Volley.newRequestQueue(context);
            queue.add(request);

            return null;
        }

        public void moveToHomePage()
        {
            Intent intent=new Intent(context, HomePageActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            context.startActivity(intent);
            ((Activity) context).overridePendingTransition(R.anim.animstart, R.anim.animend);
        }

    }

}
