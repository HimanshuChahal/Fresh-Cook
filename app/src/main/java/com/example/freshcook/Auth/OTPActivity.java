package com.example.freshcook.Auth;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.TextViewCompat;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.freshcook.HomeDashBoard.HomePageActivity;
import com.example.freshcook.MainActivity;
import com.example.freshcook.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

public class OTPActivity extends AppCompatActivity {

    ArrayList<TextView> otpTextViewsArrayList;
    String OTP;
    CountDownTimer countDownTimer;
    StringBuilder otpStringBuilder;

    public void backOnClick(View view)
    {
        finish();
    }

    public void verifyOnClick(View view)
    {
        if(otpStringBuilder.length()==6)
        {

            if(otpStringBuilder.toString().equals(OTP))
            {
                MainActivity.otpVerifiedSharedPreferences.edit().putBoolean("OTPVerified", true).apply();
                startActivity(new Intent(OTPActivity.this, WelcomeSlidesActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK));
                overridePendingTransition(R.anim.animstart, R.anim.animend);
                finish();
            } else
            {
                Toast.makeText(OTPActivity.this, "Incorrect OTP. Please try again", Toast.LENGTH_SHORT).show();
            }

        } else
        {
            Toast.makeText(OTPActivity.this, "Please enter the OTP", Toast.LENGTH_SHORT).show();
        }
    }

    public void initialise()
    {
        otpTextViewsArrayList=new ArrayList<>(Arrays.asList((TextView) findViewById(R.id.otp1TextView),
                (TextView) findViewById(R.id.otp2TextView),
                (TextView) findViewById(R.id.otp3TextView),
                (TextView) findViewById(R.id.otp4TextView),
                (TextView) findViewById(R.id.otp5TextView),
                (TextView) findViewById(R.id.otp6TextView)));

        OTP=getIntent().getStringExtra("OTP");

        otpStringBuilder=new StringBuilder();

        View keypadView= LayoutInflater.from(OTPActivity.this).inflate(R.layout.keypad_layout, null, false);

        ((LinearLayout) findViewById(R.id.otpKeypadLinearLayout)).addView(keypadView);

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
                    if(v.getTag().toString().equals("b") && otpStringBuilder.toString().length()>0)
                    {
                        otpStringBuilder.deleteCharAt(otpStringBuilder.length()-1);

                        TextViewCompat.setCompoundDrawableTintList(otpTextViewsArrayList.get(otpStringBuilder.length()), ColorStateList.valueOf(Color.GRAY));

                    } else if(v.getTag().toString().equals("d"))
                    {
                        if(otpStringBuilder.toString().length()==6)
                        {
                            if(otpStringBuilder.toString().equals(OTP) || true)
                            {
                                MainActivity.otpVerifiedSharedPreferences.edit().putBoolean("OTPVerified", true).apply();
                                countDownTimer.cancel();
                                startActivity(new Intent(OTPActivity.this, WelcomeSlidesActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK));
                                overridePendingTransition(R.anim.animstart, R.anim.animend);
                                finish();
                            } else
                            {
                                Toast.makeText(OTPActivity.this, "Incorrect OTP. Please try again", Toast.LENGTH_SHORT).show();
                            }
                        } else
                        {
                            Toast.makeText(OTPActivity.this, "Please enter the OTP", Toast.LENGTH_SHORT).show();
                        }
                    } else
                    {
                        if(otpStringBuilder.toString().length()<6 && !v.getTag().toString().equals("b")) {
                            otpStringBuilder.append(v.getTag().toString());
                            TextViewCompat.setCompoundDrawableTintList(otpTextViewsArrayList.get(otpStringBuilder.length()-1), ColorStateList.valueOf(Color.BLACK));
                        }
                    }
                }
            });
        }

        countDownTimer=new CountDownTimer(90000, 1000)
        {

            @Override
            public void onTick(long millisUntilFinished) {



            }

            @Override
            public void onFinish() {

                Toast.makeText(OTPActivity.this, "Please try again", Toast.LENGTH_SHORT).show();

                finish();

            }
        }.start();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_o_t_p);

        initialise();

    }

    public void moveToHomePage()
    {
        new CountDownTimer(1000, 1000)
        {

            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {

                Intent intent=new Intent(OTPActivity.this, HomePageActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                overridePendingTransition(R.anim.animstart, R.anim.animend);

            }
        }.start();
    }

    @Override
    public void finish() {
        super.finish();

        countDownTimer.cancel();

        overridePendingTransition(R.anim.animstart, R.anim.animend);

    }
}