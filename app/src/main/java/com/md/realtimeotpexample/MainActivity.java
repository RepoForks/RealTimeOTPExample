package com.md.realtimeotpexample;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.md.phlex.Phlex;

import java.util.Random;

public class MainActivity extends AppCompatActivity {
    EditText etFullName, etEmailId;
    String strFullName, strEmailId;
    SharedPreferences preferences;
    public static AppCompatActivity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        activity = this;
        etFullName = (EditText) findViewById(R.id.etFullName);
        etEmailId = (EditText) findViewById(R.id.etEmailId);
        preferences = getSharedPreferences("OtpPrefs",MODE_PRIVATE);
    }

    public void verifyData(View view) {
        if (dataValid()) {
            new SendMailTask().execute();
        }
    }

    private boolean dataValid() {
        boolean dataValid = false;
        strFullName = etFullName.getText().toString().trim();
        strEmailId = etEmailId.getText().toString().trim();
        if (strFullName.isEmpty()) {
            etFullName.setError(getString(R.string.empty_field));
            etFullName.requestFocus();
        }
        else if (strEmailId.isEmpty()) {
            etEmailId.setError(getString(R.string.empty_field));
            etEmailId.requestFocus();
        }
        else if (!Phlex.isEmailIdValid(strEmailId)) {
            etEmailId.setError(getString(R.string.invalid_email_id));
            etEmailId.requestFocus();
        }
        else dataValid = true;
        return dataValid;
    }

    public String randomNumber() {
        return String.valueOf(100000 + (int) (Math.random() * ((999999 - 100000) + 1)));
    }

    public class SendMailTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Toast.makeText(MainActivity.this, "Just a sec...", Toast.LENGTH_LONG).show();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Toast.makeText(MainActivity.this, "Check your email for the OTP", Toast.LENGTH_LONG).show();
            startActivity(new Intent(MainActivity.this, VerifyOTPActivity.class)
                    .putExtra("full_name", strFullName)
                    .putExtra("email_id", strEmailId));
        }

        @Override
        protected Void doInBackground(Void... voids) {
            String otp = randomNumber();
            preferences.edit().putString("otp", otp).apply();
            GMailSender mailSender = new GMailSender(getString(R.string.admin_email_id), getString(R.string.admin_password));
            try {
                mailSender.sendMail(strFullName + " Resgistration", "OTP: "+otp, getString(R.string.admin_email_id), strEmailId);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}
