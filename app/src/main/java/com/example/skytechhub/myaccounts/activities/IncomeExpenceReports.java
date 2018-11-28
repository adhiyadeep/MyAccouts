package com.example.skytechhub.myaccounts.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.example.skytechhub.myaccounts.BasicActivity.SkyTechHubActivity;
import com.example.skytechhub.myaccounts.R;
import com.example.skytechhub.myaccounts.app.AppController;

public class IncomeExpenceReports extends AppCompatActivity{

private WebView mWebview;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_income_expence_reports);
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

        mWebview = (WebView) findViewById(R.id.webview);
        WebViewClient wb = new WebViewClient();
        mWebview.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView view, int progress)
            {
                //Make the bar disappear after URL is loaded, and changes string to Loading...
                setTitle("Loading...");
                setProgress(progress * 100); //Make the bar disappear after URL is loaded

                // Return the app name after finish loading
                if(progress == 100)
                    setTitle(R.string.title_activity_income_expence_reports);
            }
        });
        mWebview.setWebViewClient(wb);
        String data = "<html><body><h1>Reports</h1></body></html>";
        mWebview.loadData(data, "text/html", "UTF-8");
        WebSettings webSettings = mWebview.getSettings();
        webSettings.setJavaScriptEnabled(true);
        if (AppController.preferences.getPreference("companyname", "").equals("Sky Tech Hub")) {
            mWebview.loadUrl("http://myaccounts.skytechhub.com/index.php/Welcome/skytechhuser");

        } else if (AppController.preferences.getPreference("companyname", "").equals("Sawasdee Spa")) {
            mWebview.loadUrl("http://myaccounts.skytechhub.com/index.php/Welcome/sawasdeeuser");

        } else if (AppController.preferences.getPreference("companyname", "").equals("Sky Tours India Pvt Ltd")) {
            mWebview.loadUrl("http://myaccounts.skytechhub.com/index.php/Welcome/skytouruser");

        } else if (AppController.preferences.getPreference("companyname", "").equals("Serenity Home")) {
            mWebview.loadUrl("http://myaccounts.skytechhub.com/index.php/Welcome/homeuser");

        } else if (AppController.preferences.getPreference("companyname", "").equals("Sanjeev Personal")) {
            mWebview.loadUrl("http://myaccounts.skytechhub.com/index.php/Welcome/personaluser");
        }



    }

    @Override
    public void onBackPressed() {
        Intent intent= new Intent(IncomeExpenceReports.this,SkyTechHubActivity.class);
        startActivity(intent);
        finish();
    }
}
