package com.example.skytechhub.myaccounts.activities;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.skytechhub.myaccounts.POJOClass.EmployeeListPojo;
import com.example.skytechhub.myaccounts.R;
import com.example.skytechhub.myaccounts.URLclass.URLClass;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

/**
 * Created by Dhaval on 7/19/2017.
 */

public class EmpListAdapter extends RecyclerView.Adapter {

    private TextView emp_name,emp_number,emp_username,emp_companyName;
    private LayoutInflater inflater;
    private List<EmployeeListPojo> numberList;
    private ImageButton img_emp_cancle,img_emp_active;
    private ProgressDialog loading;

    public EmpListAdapter(Context context, List<EmployeeListPojo> emplist) {

        this.inflater = LayoutInflater.from(context);
        this.numberList = emplist;

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.list_employee, parent, false);
        return new EmpListAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        final EmployeeListPojo employeeListPojo = numberList.get(position);
        emp_companyName.setText("Company Name : "+employeeListPojo.getCompany_name());
        emp_username.setText("Username            : "+employeeListPojo.getUsername());
        emp_name.setText("Name                    : "+employeeListPojo.getName());
        emp_number.setText("Number                : "+employeeListPojo.getCell());

        if (employeeListPojo.getDiscard().equals("No"))
        {
            img_emp_cancle.setVisibility(View.VISIBLE);
            img_emp_active.setVisibility(View.GONE);
        }
        else
        {
            img_emp_cancle.setVisibility(View.GONE);
            img_emp_active.setVisibility(View.VISIBLE);
        }

        img_emp_cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogInterface.OnClickListener dgClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case DialogInterface.BUTTON_POSITIVE:
                                //Yes button clicked
                                String id = employeeListPojo.getUsername();
                                sendData(id);
                                break;

                            case DialogInterface.BUTTON_NEGATIVE:
                                //No button clicked

                                break;
                        }
                    }
                };

                AlertDialog.Builder builder = new AlertDialog.Builder(inflater.getContext());
                builder.setTitle("Deactivate Employee..").setMessage("Are you sure ?").setPositiveButton("Yes", dgClickListener)
                        .setNegativeButton("No", dgClickListener).show();

            }
        });
        img_emp_active.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogInterface.OnClickListener dgClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case DialogInterface.BUTTON_POSITIVE:
                                //Yes button clicked
                                String id = employeeListPojo.getUsername();
                                sendData(id);
                                break;

                            case DialogInterface.BUTTON_NEGATIVE:
                                //No button clicked

                                break;
                        }
                    }
                };

                AlertDialog.Builder builder = new AlertDialog.Builder(inflater.getContext());
                builder.setTitle("Activate Employee..").setMessage("Are you sure ?").setPositiveButton("Yes", dgClickListener)
                        .setNegativeButton("No", dgClickListener).show();
            }
        });
    }

    private void sendData(String id) {


        String urlSuffix = "?username=" + id;
        class submit_income extends AsyncTask<String, Void, String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(inflater.getContext(), "Please Wait...", null, true, true);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                Log.e("onPostExecute: ", s);
                if (s.equals("Success")) {
                    AlertDialog.Builder reorder = new AlertDialog.Builder(inflater.getContext());
                    reorder.setMessage("Successfully done.");
                    reorder.setCancelable(true);
                    reorder.setPositiveButton("ok",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.dismiss();

                                    Admin.isEmployeeList = true;
                                    Intent intent = new Intent(inflater.getContext(),Admin.class);
                                    inflater.getContext().startActivity(intent);
                                }
                            });
                    AlertDialog orderError = reorder.create();
                    orderError.show();

                }
                else
                {
                    AlertDialog.Builder reorder = new AlertDialog.Builder(inflater.getContext());
                    reorder.setMessage("Failed. Please try again/after sometime... ");
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
                    URL url = new URL(URLClass.DEACTIVE_URL + s);
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
        submit_income ba = new submit_income();
        ba.execute(urlSuffix);
    }

    @Override
    public int getItemCount() {
        return numberList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        public ViewHolder(View itemView) {
            super(itemView);
            emp_name = (TextView)itemView.findViewById(R.id.emp_name);
            emp_number = (TextView)itemView.findViewById(R.id.emp_number);
            emp_username = (TextView)itemView.findViewById(R.id.emp_username);
            emp_companyName = (TextView)itemView.findViewById(R.id.emp_compamyname);

            img_emp_cancle   =(ImageButton)itemView.findViewById(R.id.img_emp_cancle);
            img_emp_active =(ImageButton)itemView.findViewById(R.id.img_emp_active);
        }
    }
}
