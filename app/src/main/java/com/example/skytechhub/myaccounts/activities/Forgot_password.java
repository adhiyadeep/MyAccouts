package com.example.skytechhub.myaccounts.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.skytechhub.myaccounts.R;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Dhaval on 7/19/2017.
 */

public class Forgot_password extends Activity implements View.OnClickListener {

    private EditText username;
    private Button btn_submit;
    private static final String CHECKUSERNAME_URL = "http://myaccounts.skytechhub.com/checkUsername.php";
    private ProgressDialog pd;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forgot_password);

        username = (EditText)findViewById(R.id.forgot_username);
        btn_submit = (Button)findViewById(R.id.btn_submit);
        btn_submit.setOnClickListener(this);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(Forgot_password.this,Login.class);
        startActivity(intent);
        Forgot_password.this.finish();
    }

    @Override
    public void onClick(View v) {
        if (v == btn_submit)
        {
            String uname = username.getText().toString();
            checkData(uname);
        }
    }

    private void checkData(String uname) {
        String urlsuffix = "?username=" + uname;
        class UserLoginTask extends AsyncTask<String, Void, String> {

            @Override
            protected String doInBackground(String... params) {
                String s = params[0];
                BufferedReader bufferedReader = null;
                // TODO: attempt authentication against a network service.

                try {
                    URL url = new URL(CHECKUSERNAME_URL + s);
                    Log.e("doInBackground: ", String.valueOf(url));
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));

                    String result;

                    result = bufferedReader.readLine();

                    return result;
                    // Simulate network access.
                } catch (Exception e) {
                    return "Exception: " + e.getMessage();
                }
            }

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                pd = ProgressDialog.show(Forgot_password.this, "Please Wait", null, true, true);
            }


            @Override
            protected void onPostExecute(final String success) {
                Log.e("onPostExecute: ", success);
                if (success.equals("Success")) {
                    pd.dismiss();
                    AlertDialog.Builder reorder = new AlertDialog.Builder(Forgot_password.this);
                    reorder.setTitle("Password");
                    reorder.setMessage("Successfully send new password to your registered mobile number.");
                    reorder.setCancelable(true);
                    reorder.setPositiveButton("ok",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.dismiss();
                                    Intent intent = new Intent(Forgot_password.this,Login.class);
                                    startActivity(intent);
                                    Forgot_password.this.finish();

                                }
                            });

                    AlertDialog orderError = reorder.create();
                    orderError.show();
                }
                else {
                    pd.dismiss();
                    AlertDialog.Builder reorder = new AlertDialog.Builder(Forgot_password.this);
                    reorder.setTitle("Password");
                    reorder.setMessage("Something went wrong please try again after sometime.");
                    reorder.setCancelable(true);
                    reorder.setPositiveButton("ok",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.dismiss();
                                }
                            });

                    AlertDialog orderError = reorder.create();
                    orderError.show();
                }
            }
        }
        UserLoginTask srf = new UserLoginTask();
        srf.execute(urlsuffix);
    }
}
