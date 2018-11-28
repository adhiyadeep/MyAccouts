package com.example.skytechhub.myaccounts.activities;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.skytechhub.myaccounts.R;
import com.example.skytechhub.myaccounts.URLclass.URLClass;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class Register extends AppCompatActivity implements OnItemSelectedListener {

    private EditText edt_reg_name,edt_reg_username, edt_reg_password, edt_reg_confrimpass, edt_reg_officeid, edt_reg_email, edt_reg_phone;
    private Button btn_reg;
    private Spinner spinner_company;
    ProgressDialog loading;
    ProgressDialog progress1;
    private List <String> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        edt_reg_name =(EditText)findViewById(R.id.edt_reg_name);
        edt_reg_phone =(EditText)findViewById(R.id.edt_reg_phone);
        edt_reg_username=  (EditText)findViewById(R.id.edt_reg_username);
        edt_reg_password=  (EditText)findViewById(R.id.edt_reg_password);
        edt_reg_confrimpass=  (EditText)findViewById(R.id.edt_reg_confrimpass);
        spinner_company =(Spinner)findViewById(R.id.spinner_company);
        // Spinner Drop down elements

        list.add("Sky Tech Hub");
        list.add("Sawasdee Spa");
        list.add("Sky Tours India Pvt Ltd");
        list.add("Serenity Home");
        list.add("Sanjeev Personal");

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(Register.this, android.R.layout.simple_spinner_item, list);
        spinner_company.setAdapter(adapter);

        btn_reg=(Button)findViewById(R.id.btn_reg);
        btn_reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edt_reg_password.getText().toString().equals(edt_reg_confrimpass.getText().toString()))
                {
                    register();
                }
               else
                {
                    Toast.makeText(Register.this, "Password MissMatch", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void register() {

        String name = edt_reg_name.getText().toString().trim();
        String phone = edt_reg_phone.getText().toString().trim();
        String username = edt_reg_username.getText().toString().trim();
        String pass = edt_reg_password.getText().toString().trim();
        String c_name = spinner_company.getSelectedItem().toString();

       sendToServer(name,phone,username,pass,c_name);

    }

    private void sendToServer(String reg_Name, String phone, String username, String pass, String cname) {

        String urlSuffix = "?name=" + reg_Name.replace(" ", "%20")+"&phone="+phone+"&username="+username.replace(" ", "%20")+"&pass="+pass+"&company_name="+cname.replace(" ", "%20");
        class bookingAppointment_membership extends AsyncTask<String, Void, String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(Register.this, "Please Wait...", null, true, true);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                Toast.makeText(Register.this, s, Toast.LENGTH_SHORT).show();
                if (s.equals("Success")) {
                    AlertDialog.Builder reorder = new AlertDialog.Builder(Register.this);
                    reorder.setMessage("Registration form submitted.");
                    reorder.setCancelable(true);
                    reorder.setPositiveButton("ok",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {

                                    dialog.dismiss();
                                    Intent intent = new Intent(Register.this,Admin.class);
                                    startActivity(intent);
                                    finish();
                                }
                            });

                    AlertDialog orderError = reorder.create();
                    orderError.show();

                }
                else
                {
                    AlertDialog.Builder reorder = new AlertDialog.Builder(Register.this);
                    reorder.setMessage("Registeration failed. Please try again/after sometime... ");
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

            @Override
            protected String doInBackground(String... params) {
                String s = params[0];
                BufferedReader bufferedReader = null;
                try {
                    URL url = new URL(URLClass.REGISTER_URL + s);
                    Log.e("doInBackground: ", String.valueOf(url));
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));

                    String result;

                    result = bufferedReader.readLine();

                    return result;

                } catch (Exception e) {
                    return String.valueOf(e);
                }
            }
        }
        bookingAppointment_membership ba = new bookingAppointment_membership();
        ba.execute(urlSuffix);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent= new Intent(Register.this,Admin.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String item = parent.getItemAtPosition(position).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
