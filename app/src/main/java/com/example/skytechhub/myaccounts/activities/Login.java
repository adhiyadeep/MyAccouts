package com.example.skytechhub.myaccounts.activities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.LoaderManager.LoaderCallbacks;
import android.app.ProgressDialog;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.skytechhub.myaccounts.BasicActivity.SkyTechHubActivity;
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

import static android.Manifest.permission.READ_CONTACTS;

/**
 * A login screen that offers login via email/password.
 */
public class Login extends AppCompatActivity implements LoaderCallbacks<Cursor>, Response.ErrorListener, Response.Listener<JSONObject> {

    ProgressDialog loading;
    public static boolean isLogin = false;
    private Button btn_register;

    private static final String LOGIN_URL = "http://myaccounts.skytechhub.com/Login.php";


    /**
     * Id to identity READ_CONTACTS permission request.
     */
    private static final int REQUEST_READ_CONTACTS = 0;

    /**
     * A dummy authentication store containing known user names and passwords.
     * TODO: remove after connecting to a real authentication system.
     */
    private static final String[] DUMMY_CREDENTIALS = new String[]{
            "foo@example.com:hello", "bar@example.com:world"
    };
    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */

    // UI references.
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;
    private TextView forgot_password;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        if (!AppController.preferences.getPreference("isLogin", "").contains("true")) {
            setContentView(R.layout.activity_login);
            // Set up the login form.
            mEmailView = (AutoCompleteTextView) findViewById(R.id.email);
            populateAutoComplete();

            mPasswordView = (EditText) findViewById(R.id.password);
            mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                    if (id == R.id.login || id == EditorInfo.IME_NULL) {
                        attemptLogin();
                        return true;
                    }
                    return false;
                }
            });

            Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
            mEmailSignInButton.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {

                    attemptLogin();
                }
            });

            mLoginFormView = findViewById(R.id.login_form);
            mProgressView = findViewById(R.id.login_progress);
            forgot_password = (TextView)findViewById(R.id.forgotpassword);

            forgot_password.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(Login.this,Forgot_password.class);
                    startActivity(intent);
                    Login.this.finish();

                }
            });
        } else {
            if (AppController.preferences.getPreference("companyname", "").contains("Admin")) {
                Intent intent = new Intent(Login.this, Admin.class);
                startActivity(intent);
                finish();
            } else if (AppController.preferences.getPreference("companyname", "").equals("Sky Tech Hub")) {
                Intent intent = new Intent(Login.this, SkyTechHubActivity.class);
                startActivity(intent);
                finish();
            } else if (AppController.preferences.getPreference("companyname", "").equals("Sawasdee Spa")) {
                Intent intent = new Intent(Login.this, SkyTechHubActivity.class);
                startActivity(intent);
                finish();
            } else if (AppController.preferences.getPreference("companyname", "").equals("Sky Tours India Pvt Ltd")) {
                Intent intent = new Intent(Login.this, SkyTechHubActivity.class);
                startActivity(intent);
                finish();
            } else if (AppController.preferences.getPreference("companyname", "").equals("Serenity Home")) {
                Intent intent = new Intent(Login.this, SkyTechHubActivity.class);
                startActivity(intent);
                finish();
            } else if (AppController.preferences.getPreference("companyname", "").equals("Sanjeev Personal")) {
                Intent intent = new Intent(Login.this, SkyTechHubActivity.class);
                startActivity(intent);
                finish();
            }
        }

    }

    private void populateAutoComplete() {
        if (!mayRequestContacts()) {
            return;
        }

        getLoaderManager().initLoader(0, null, this);
    }

    private boolean mayRequestContacts() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        if (checkSelfPermission(READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        if (shouldShowRequestPermissionRationale(READ_CONTACTS)) {
            Snackbar.make(mEmailView, R.string.permission_rationale, Snackbar.LENGTH_INDEFINITE)
                    .setAction(android.R.string.ok, new View.OnClickListener() {
                        @Override
                        @TargetApi(Build.VERSION_CODES.M)
                        public void onClick(View v) {
                            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
                        }
                    });
        } else {
            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
        }
        return false;
    }

    /**
     * Callback received when a permissions request has been completed.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == REQUEST_READ_CONTACTS) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                populateAutoComplete();
            }
        }
    }


    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {

        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String email = mEmailView.getText().toString();

        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
//false

            showProgress(false);
            login();
        }
    }

    private void login() {

        String email = mEmailView.getText().toString();
        AppController.preferences.savePreference("username", email);
        String password = mPasswordView.getText().toString();

        goLogin(email, password);
    }

    private void goLogin(String email, String password) {
        String urlsuffix = "?username=" + email + "&password=" + password;
        class UserLoginTask extends AsyncTask<String, Void, String> {

            @Override
            protected String doInBackground(String... params) {
                String s = params[0];
                BufferedReader bufferedReader = null;
                // TODO: attempt authentication against a network service.

                try {
                    URL url = new URL(LOGIN_URL + s);
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
                loading = ProgressDialog.show(Login.this, "Please Wait", null, true, true);
            }


            @Override
            protected void onPostExecute(final String success) {
                showProgress(false);
                Log.e("onPostExecute: ", success);
                if (success.equals("Login Successful")) {
                    loading.dismiss();
                    getClientDetails();
                    AppController.preferences.savePreference("isLogin", "true");

                } else {
                    AppController.preferences.savePreference("isLogin", "false");
                    mPasswordView.setError(getString(R.string.error_incorrect_password));
                    mPasswordView.requestFocus();
                    loading.dismiss();
                }
            }

            @Override
            protected void onCancelled() {
                showProgress(false);
            }
        }
        UserLoginTask srf = new UserLoginTask();
        srf.execute(urlsuffix);


    }

    private void getClientDetails() {
        CustomJSONObjectRequest request = new CustomJSONObjectRequest(Request.Method.GET, "http://myaccounts.skytechhub.com/Gettingresponse/getCompanyName.php?username=" + AppController.preferences.getPreference("username", ""), null, Login.this, Login.this);
        Log.e("getClientid OTP: ", String.valueOf(request));
        AppController.requestQueue.add(request);
    }

    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4;
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new CursorLoader(this,
                // Retrieve data rows for the device user's 'profile' contact.
                Uri.withAppendedPath(ContactsContract.Profile.CONTENT_URI,
                        ContactsContract.Contacts.Data.CONTENT_DIRECTORY), ProfileQuery.PROJECTION,

                // Select only email addresses.
                ContactsContract.Contacts.Data.MIMETYPE +
                        " = ?", new String[]{ContactsContract.CommonDataKinds.Email
                .CONTENT_ITEM_TYPE},

                // Show primary email addresses first. Note that there won't be
                // a primary email address if the user hasn't specified one.
                ContactsContract.Contacts.Data.IS_PRIMARY + " DESC");
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        List<String> emails = new ArrayList<>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            emails.add(cursor.getString(ProfileQuery.ADDRESS));
            cursor.moveToNext();
        }

        addEmailsToAutoComplete(emails);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {

    }

    private void addEmailsToAutoComplete(List<String> emailAddressCollection) {
        //Create adapter to tell the AutoCompleteTextView what to show in its dropdown list.
        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(Login.this,
                        android.R.layout.simple_dropdown_item_1line, emailAddressCollection);

        mEmailView.setAdapter(adapter);
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        login();
    }

    @Override
    public void onResponse(JSONObject response) {
        Log.e("Response", String.valueOf(response));
        try {
            JSONArray jsonArray = response.getJSONArray("companyDetails");
            loading.dismiss();
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject object = jsonArray.getJSONObject(i);
                AppController.preferences.savePreference("companyname", object.getString("company_name"));
            }

            if (AppController.preferences.getPreference("companyname", "").equals("Admin")) {
                Intent intent = new Intent(Login.this, Admin.class);
                startActivity(intent);
                finish();
            } else if (AppController.preferences.getPreference("companyname", "").equals("Sky Tech Hub")) {
                Intent intent = new Intent(Login.this, SkyTechHubActivity.class);
                startActivity(intent);
                finish();
            } else if (AppController.preferences.getPreference("companyname", "").equals("Sawasdee Spa")) {
                Intent intent = new Intent(Login.this, SkyTechHubActivity.class);
                startActivity(intent);
                finish();
            } else if (AppController.preferences.getPreference("companyname", "").equals("Sky Tours India Pvt Ltd")) {
                Intent intent = new Intent(Login.this, SkyTechHubActivity.class);
                startActivity(intent);
                finish();
            } else if (AppController.preferences.getPreference("companyname", "").equals("Serenity Home")) {
                Intent intent = new Intent(Login.this, SkyTechHubActivity.class);
                startActivity(intent);
                finish();
            } else if (AppController.preferences.getPreference("companyname", "").equals("Sanjeev Personal")) {
                Intent intent = new Intent(Login.this, SkyTechHubActivity.class);
                startActivity(intent);
                finish();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private interface ProfileQuery {
        String[] PROJECTION = {
                ContactsContract.CommonDataKinds.Email.ADDRESS,
                ContactsContract.CommonDataKinds.Email.IS_PRIMARY,
        };

        int ADDRESS = 0;
        int IS_PRIMARY = 1;
    }

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
}