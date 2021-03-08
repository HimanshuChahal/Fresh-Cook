package com.example.freshcook.Auth;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.freshcook.HomeDashBoard.HomePageActivity;
import com.example.freshcook.R;
import com.example.freshcook.Utilities;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;
import java.util.Random;

public class LoginActivity extends AppCompatActivity {

    EditText phoneNumberEditText;

    public void backOnClick(View view)
    {
        finish();
    }

    public String createOTP()
    {
        Random random=new Random();

        String OTP="";

        for(int i=0;i<6;i++)
        {
            OTP+=String.valueOf(random.nextInt(10));
        }

        return OTP;
    }

    public void sendOTPonClick(View view)
    {
        if(checkPhoneNumber())
        {

            SendOTP sendOTP=new SendOTP(LoginActivity.this, phoneNumberEditText);
            sendOTP.doInBackground(createOTP(), phoneNumberEditText.getText().toString());

        } else
        {
            Toast.makeText(this, "Please enter valid phone number", Toast.LENGTH_SHORT).show();
        }

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

    }

    public void initialise()
    {
        phoneNumberEditText=findViewById(R.id.phoneNumberEditText);

        phoneNumberEditText.setInputType(InputType.TYPE_NULL);

        View keypadView=LayoutInflater.from(LoginActivity.this).inflate(R.layout.keypad_layout, null, false);

        ((LinearLayout) findViewById(R.id.keypadLinearLayout)).addView(keypadView);

        final ArrayList<Button> keypadButtonsArrayList=new ArrayList<>(Arrays.asList((Button) keypadView.findViewById(R.id.keypad1Button),
                (Button) keypadView.findViewById(R.id.keypad2Button),
                (Button) keypadView.findViewById(R.id.keypad3Button),
                (Button) keypadView.findViewById(R.id.keypad4Button),
                (Button) keypadView.findViewById(R.id.keypad5Button),
                (Button) keypadView.findViewById(R.id.keypad6Button),
                (Button) keypadView.findViewById(R.id.keypad7Button),
                (Button) keypadView.findViewById(R.id.keypad8Button),
                (Button) keypadView.findViewById(R.id.keypad9Button),
                (Button) keypadView.findViewById(R.id.keypad0Button),
                (Button) keypadView.findViewById(R.id.keypadBackspaceButton),
                (Button) keypadView.findViewById(R.id.keypadDoneButton)));

        for(int i=0;i<keypadButtonsArrayList.size();i++)
        {
            keypadButtonsArrayList.get(i).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(v.getTag().toString().equals("b") && phoneNumberEditText.getText().toString().length()>0)
                    {
                        phoneNumberEditText.setText(phoneNumberEditText.getText().toString().substring(0, phoneNumberEditText.getText().toString().length()-1));
                    } else if(v.getTag().toString().equals("d"))
                    {
                        if(phoneNumberEditText.getText().toString().length()==10)
                        {
                            SendOTP sendOTP=new SendOTP(LoginActivity.this, phoneNumberEditText);
                            sendOTP.doInBackground(createOTP(), phoneNumberEditText.getText().toString());
                        } else
                        {
                            Toast.makeText(LoginActivity.this, "Please enter valid phone number", Toast.LENGTH_SHORT).show();
                        }
                    } else
                    {
                        if(phoneNumberEditText.getText().toString().length()<10 && !v.getTag().toString().equals("b")) {
                            if (phoneNumberEditText.getText() != null) {
                                phoneNumberEditText.setText(phoneNumberEditText.getText().toString() + v.getTag().toString());
                            } else {
                                phoneNumberEditText.setText(v.getTag().toString());
                            }
                        }
                    }
                }
            });
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initialise();

    }

    public boolean checkPhoneNumber()
    {
        if(phoneNumberEditText.getText()==null || phoneNumberEditText.getText().toString().length()!=10)
        {
            return false;
        }

        return true;
    }

    @Override
    public void finish() {
        super.finish();

        overridePendingTransition(R.anim.animstart, R.anim.animend);

    }

    static class SendOTP extends AsyncTask<String, Void, Void>
    {

        Context context;
        EditText phoneNumberEditText;
        ProgressDialog progressDialog;

        public SendOTP(Context context, EditText phoneNumberEditText)
        {
            this.context=context;
            this.phoneNumberEditText=phoneNumberEditText;
        }

        @Override
        protected Void doInBackground(final String... strings) {

            progressDialog=new ProgressDialog(context);

            progressDialog.setTitle("Sending OTP.....");

            progressDialog.setCancelable(false);

            progressDialog.show();

            String OTPURL="http://66.70.200.49/rest/services/sendSMS/sendGroupSms?AUTH_KEY=3e377f8fcc852e1ceb8262ea7d82913&message="+strings[0]+"&senderId=FILLIP&routeId=1&mobileNos="+strings[1]+"&smsContentType=unicode";

            StringRequest request=new StringRequest(StringRequest.Method.GET, OTPURL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    if(progressDialog!=null && progressDialog.isShowing()) {
                        progressDialog.dismiss();
                    }

                    Intent intent=new Intent(context, OTPActivity.class);
                    intent.putExtra("phone number", phoneNumberEditText.getText().toString());
                    intent.putExtra("OTP", strings[0]);
                    (context).startActivity(intent);
                    ((Activity) context).overridePendingTransition(R.anim.animstart, R.anim.animend);

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                    Toast.makeText(context, String.valueOf(error), Toast.LENGTH_SHORT).show();

                    progressDialog.dismiss();

                    Intent intent=new Intent(context, OTPActivity.class);
                    intent.putExtra("phone number", phoneNumberEditText.getText().toString());
                    intent.putExtra("OTP", strings[0]);
                    (context).startActivity(intent);
                    ((Activity) context).overridePendingTransition(R.anim.animstart, R.anim.animend);

                }
            });

            RetryPolicy policy=new DefaultRetryPolicy(Utilities.maxTimeOut, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
            request.setRetryPolicy(policy);

            RequestQueue queue= Volley.newRequestQueue(context);
            queue.add(request);

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            if(progressDialog!=null && progressDialog.isShowing())
            {
                progressDialog.dismiss();
            }

        }
    }

}