package com.example.skytechhub.myaccounts.activities;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.skytechhub.myaccounts.R;

public class EmployeeList extends AppCompatActivity implements View.OnClickListener {

    private RecyclerView recyclerView;
    private TextView totalDisplay;
    private Button btn_activeemp,btn_deactiveemp;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_list);
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

        btn_activeemp =(Button)findViewById(R.id.btn_activeemp);
        btn_deactiveemp=(Button)findViewById(R.id.btn_deactiveemp);
        totalDisplay = (TextView)findViewById(R.id.totalEmployees);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                btn_activeemp.performClick();
            }
        }, 1000);


        btn_activeemp.setOnClickListener(this);
        btn_deactiveemp.setOnClickListener(this);
        recyclerView = (RecyclerView)findViewById(R.id.recyclerview);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(EmployeeList.this,Admin.class);
        startActivity(intent);
        EmployeeList.this.finish();
    }

    @Override
    public void onClick(View v) {

        if (v == btn_activeemp)
        {
            Drawable d = getResources().getDrawable(R.drawable.rectangle);
            btn_deactiveemp.setBackground(d);
            btn_deactiveemp.setTextColor(Color.parseColor("#000000"));

            btn_activeemp.setBackgroundColor(Color.parseColor("#D3D3D3"));
            btn_activeemp.setTextColor(Color.parseColor("#994813"));

            totalDisplay.setText("Total employees : "+Admin.activeList.size());
            recyclerView.setLayoutManager(new GridLayoutManager(EmployeeList.this,1));
            EmpListAdapter mAdapter = new EmpListAdapter(EmployeeList.this, Admin.activeList);
            recyclerView.setAdapter(mAdapter);
            recyclerView.setHasFixedSize(true);
            mAdapter.getItemCount();
        }
        else
        {
            Drawable d = getResources().getDrawable(R.drawable.rectangle);
            btn_activeemp.setBackground(d);
            btn_activeemp.setTextColor(Color.parseColor("#000000"));

            btn_deactiveemp.setBackgroundColor(Color.parseColor("#D3D3D3"));
            btn_deactiveemp.setTextColor(Color.parseColor("#994813"));

            totalDisplay.setText("Total employees : "+Admin.deactiveList.size());
            recyclerView.setLayoutManager(new GridLayoutManager(EmployeeList.this,1));
            EmpListAdapter mAdapter = new EmpListAdapter(EmployeeList.this, Admin.deactiveList);
            recyclerView.setAdapter(mAdapter);
            recyclerView.setHasFixedSize(true);
            mAdapter.getItemCount();
        }
    }
}
