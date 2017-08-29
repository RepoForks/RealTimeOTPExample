package com.md.realtimeotpexample;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class VerifyOTPActivity extends AppCompatActivity {
    SharedPreferences preferences;
String otp;
    EditText etOtp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_otp);
        preferences = getSharedPreferences("OtpPrefs",MODE_PRIVATE);
        etOtp= (EditText) findViewById(R.id.otp);
        otp=preferences.getString("otp",null);

    }
    public void verifyOtp(View view)
    {
        String strEtOtp=etOtp.getText().toString().trim();
        if (strEtOtp.isEmpty())
        {
            etOtp.setError(getString(R.string.empty_field));
            etOtp.requestFocus();
        }
        else if (otp!=null)
        {
            if (!strEtOtp.equals(otp))
            {
                etOtp.setText("");
                etOtp.setError("Invalid OTP");
                etOtp.requestFocus();
            }
            else if (strEtOtp.equals(otp))
            {
                preferences.edit().clear().apply();
                MainActivity.activity.finish();
                finish();
                startActivity(new Intent(this,RegisteredActivity.class));
            }
        }
        else
            Toast.makeText(this, "otp is null", Toast.LENGTH_SHORT).show();
    }
}
