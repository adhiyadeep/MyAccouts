package com.example.skytechhub.myaccounts.activities;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.skytechhub.myaccounts.POJOClass.EmployeeListPojo;
import com.example.skytechhub.myaccounts.R;
import com.example.skytechhub.myaccounts.app.AppController;
import com.example.skytechhub.myaccounts.webhandler.CustomJSONObjectRequest;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class Admin extends AppCompatActivity implements View.OnClickListener, Response.ErrorListener, Response.Listener<JSONObject> {

    private Button btn_skytech, btn_sawasdee, btn_skytours, btn_personal, btn_serenity,btn_full_reports,btn_emplist;
    private Dialog confirmDialog,changePassword;
    private Button btn_income, btn_expense,btn_save_password;
    private WebView mWebview;
    private EditText newPassword,confirmPassword;

    public static Boolean exit=false,isEmployeeList = false;
    public static List<EmployeeListPojo> activeList,deactiveList;

    private String CHANGINGPASS_URL = "http://myaccounts.skytechhub.com/ChangePassword.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        getEmpList();
        if (isEmployeeList)
        {
            isEmployeeList = false;
            Intent intent = new Intent(Admin.this,EmployeeList.class);
            startActivity(intent);
            Admin.this.finish();
        }
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(AppController.preferences.getPreference("username", ""));
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        btn_skytech = (Button) findViewById(R.id.btn_skytechhub);
        btn_sawasdee = (Button) findViewById(R.id.btn_sawasdee);
        btn_skytours = (Button) findViewById(R.id.btn_skytours);
        btn_serenity = (Button) findViewById(R.id.btn_serenity);
        btn_personal = (Button) findViewById(R.id.btn_sanjeevpersonal);
        btn_emplist = (Button)findViewById(R.id.btn_empList);
        btn_full_reports = (Button)findViewById(R.id.btn_admin_full_reports);

        btn_skytech.setOnClickListener(this);
        btn_sawasdee.setOnClickListener(this);
        btn_skytours.setOnClickListener(this);
        btn_personal.setOnClickListener(this);
        btn_serenity.setOnClickListener(this);
        btn_emplist.setOnClickListener(this);
        btn_full_reports.setOnClickListener(this);
    }

    private void getEmpList() {
        CustomJSONObjectRequest request = new CustomJSONObjectRequest(Request.Method.GET, "http://myaccounts.skytechhub.com/getEmpList.php", null, Admin.this, Admin.this);
        Log.e("getTherapy: ", String.valueOf(request));
        AppController.requestQueue.add(request);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_admin, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {

            AppController.preferences.savePreference("isLogin", "false");
            AppController.preferences.savePreference("username", "");

            Intent intent = new Intent(Admin.this, Login.class);
            startActivity(intent);
            finish();
        }
        else if (id == R.id.action_changePassword)
        {
            changePassword = new Dialog(Admin.this);
            changePassword.setContentView(R.layout.dialog_changepass);
            changePassword.setCanceledOnTouchOutside(false);
            WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
            Window window = changePassword.getWindow();
            WindowManager.LayoutParams wlp = window.getAttributes();

            wlp.gravity = Gravity.BOTTOM;
            lp.copyFrom(window.getAttributes());
            //This makes the dialog take up the full width
            lp.width = WindowManager.LayoutParams.MATCH_PARENT;
            lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
            window.setAttributes(lp);
            changePassword.show();

            newPassword = (EditText)changePassword.findViewById(R.id.changePassword);
            confirmPassword = (EditText)changePassword.findViewById(R.id.confirmPassword);
            btn_save_password = (Button)changePassword.findViewById(R.id.btn_changePassword);
            btn_save_password.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (newPassword.getText().toString().equals(confirmPassword.getText().toString()))
                    {
                        change();
                    }
                    else
                    {
                        AlertDialog.Builder reorder = new AlertDialog.Builder(Admin.this);
                        reorder.setTitle("Password");
                        reorder.setMessage("Entered password not matched. Please try again.");
                        reorder.setCancelable(true);
                        reorder.setPositiveButton("ok",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        newPassword.setText("");
                                        confirmPassword.setText("");
                                        dialog.dismiss();

                                    }
                                });

                        AlertDialog orderError = reorder.create();
                        orderError.show();
                    }
                }
            });
        }
        else {
            Intent intent = new Intent(Admin.this, Register.class);
            startActivity(intent);
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    private void change() {

        String newPass = newPassword.getText().toString();

        changingPassword(newPass);

    }

    private void changingPassword(String newPass) {

        String urlSuffix = "?username="+ AppController.preferences.getPreference("username","")+"&password="+newPass;
        //String urlSuffix = URLEncoder.encode(urlSuffix1, "UTF-8");

        class addingBusiness extends AsyncTask<String, Void, String> {

            ProgressDialog loading;


            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(Admin.this, "Please Wait...", null, true, true);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();

                if (s.equals("Successfully updated.")) {
                    AlertDialog.Builder reorder = new AlertDialog.Builder(Admin.this);
                    reorder.setTitle("Updated");
                    reorder.setMessage("You successfully updated your login credential. Please login again.");
                    reorder.setCancelable(true);
                    reorder.setPositiveButton("ok",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.dismiss();
                                    AppController.preferences.savePreference("isLogin", "false");
                                    AppController.preferences.savePreference("username", "");
                                    Intent intent = new Intent(Admin.this,Login.class);
                                    startActivity(intent);
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
                    URL url = new URL(CHANGINGPASS_URL + s);
                    Log.e("doInBackground: ", String.valueOf(url));
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));

                    String result;

                    result = bufferedReader.readLine();

                    return result;

                } catch (Exception e) {
                    return new String("Exception: " +e);
                }

            }
        }
        addingBusiness ba = new addingBusiness();
        ba.execute(urlSuffix);
    }


    @Override
    public void onBackPressed() {
        if (exit) {
            finish(); // finish activity
        } else {
            Toast.makeText(this, "Press Back again to Exit.",
                    Toast.LENGTH_SHORT).show();
            exit = true;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    exit = false;
                }
            }, 3 * 1000);

        }
    }

    @Override
    public void onClick(View v) {

        if (v == btn_skytech) {
            String click = "Sky Tech Hub";
            showDialog(click);
        } else if (v == btn_personal) {
            String click = "Sanjeev Personal";
            showDialog(click);
        } else if (v == btn_sawasdee) {
            String click = "Sawasdee Spa";
            showDialog(click);
        } else if (v == btn_serenity) {
            String click = "Serenity Home";
            showDialog(click);
        } else if (v == btn_skytours) {
            String click = "Sky Tours India Pvt Ltd";
            showDialog(click);
        }
        else if (v== btn_full_reports)
        {
            String click = "Report";
            showDialog(click);
        }
        else if (v == btn_emplist)
        {
            Intent intent = new Intent(Admin.this,EmployeeList.class);
            startActivity(intent);
            Admin.this.finish();
        }

    }

    private void showDialog(final String click) {

        confirmDialog = new Dialog(Admin.this);
        confirmDialog.setContentView(R.layout.dialog_income_expense);
        confirmDialog.setCanceledOnTouchOutside(false);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        Window window = confirmDialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        confirmDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        String value = click;
        wlp.gravity = Gravity.CENTER;
        lp.copyFrom(window.getAttributes());
        //This makes the dialog take up the full width
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(lp);
        confirmDialog.show();

        btn_income = (Button) confirmDialog.findViewById(R.id.btn_income);
        btn_expense = (Button) confirmDialog.findViewById(R.id.btn_expense);

        btn_income.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Admin.this, IncomeReport.class);
                Bundle b = new Bundle();
                b.putString("companyname", click); //Your id
                intent.putExtras(b); //Put your id to your next Intent
                startActivity(intent);
                finish();
            }
        });
        btn_expense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Admin.this, ExpenseReport.class);
                Bundle b = new Bundle();
                b.putString("companyname", click); //Your id
                intent.putExtras(b); //Put your id to your next Intent
                startActivity(intent);
                finish();
            }
        });

    }

    @Override
    public void onErrorResponse(VolleyError error) {
        AlertDialog.Builder reorder = new AlertDialog.Builder(Admin.this);
        reorder.setTitle("Employee List");
        reorder.setMessage("Failed to get employee data. Press ok to refresh data or check internet connection");
        reorder.setCancelable(true);
        reorder.setPositiveButton("ok",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                        Intent intent = new Intent(Admin.this,Login.class);
                        startActivity(intent);
                        Admin.this.finish();

                    }
                });

        AlertDialog orderError = reorder.create();
        orderError.show();
    }

    @Override
    public void onResponse(JSONObject response) {
        Log.e("onResponse: ", String.valueOf(response));
        activeList = new ArrayList<>();
        deactiveList = new ArrayList<>();

        try {
            activeList.clear();
            deactiveList.clear();
            JSONArray jsonArray = response.getJSONArray("EmpList");
            for (int i = 0; i < jsonArray.length(); i++) {
                EmployeeListPojo list;
                JSONObject object = jsonArray.getJSONObject(i);
                list = new EmployeeListPojo();
                String name = object.getString("name");
                String cell = object.getString("cell");
                String username = object.getString("username");
                String companyName = object.getString("company_name");
                String discard = object.getString("discard");

                list.setName(name);
                list.setCell(cell);
                list.setUsername(username);
                list.setCompany_name(companyName);
                list.setDiscard(discard);

                if (discard.equals("No"))
                {
                    activeList.add(list);
                }
                else
                {
                    deactiveList.add(list);
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
