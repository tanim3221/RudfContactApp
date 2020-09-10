package com.androiddeft.firebasepushnotification;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;

import com.blog.library.UpdateChecker;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import static com.androiddeft.firebasepushnotification.WebviewActivity.isNetworkStatusAvialable;


public class LoginActivity extends AppCompatActivity {

    EditText editTextUsername, editTextPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        final Button checknew = (Button) findViewById(R.id.checkNew);
        checknew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UpdateChecker.checkForDialog(LoginActivity.this);
            }
        });

        if (isNetworkStatusAvialable(getApplicationContext())) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    checknew.performClick();
                }
            }, 1);
        } else {

            Toast.makeText(getApplicationContext(), (getString(R.string.connect_net)), Toast.LENGTH_LONG).cancel();
        }

        if (SharedPrefManager.getInstance(this).isLoggedIn()) {
            finish();
            startActivity(new Intent(this, Home.class));
            return;
        }

        editTextUsername = (EditText) findViewById(R.id.editTextUsername);
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);


        //if user presses on login
        //calling the method login
        findViewById(R.id.buttonLogin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userLogin();
            }
        });

        ProgressBar progressBar = findViewById(R.id.progressBar);

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            Drawable wrapDrawable = DrawableCompat.wrap(progressBar.getIndeterminateDrawable());
            DrawableCompat.setTint(wrapDrawable, ContextCompat.getColor(getApplicationContext(),
                    R.color.colorPrimary));
            progressBar.setIndeterminateDrawable(DrawableCompat.unwrap(wrapDrawable));

        } else {
            progressBar.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary), PorterDuff.Mode.SRC_IN);
        }
    }

    private void userLogin() {
        //first getting the values
        final String username = editTextUsername.getText().toString();
        final String mobile = editTextPassword.getText().toString();

        //validating inputs
        if (TextUtils.isEmpty(username)) {
            editTextUsername.setError("Please enter your email");
            editTextUsername.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(mobile)) {
            editTextPassword.setError("Please enter your mobile number");
            editTextPassword.requestFocus();
            return;
        }

        //if everything is fine

        class UserLogin extends AsyncTask<Void, Void, String> {

            ProgressBar progressBar;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressBar = (ProgressBar) findViewById(R.id.progressBar);
                progressBar.setVisibility(View.VISIBLE);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                progressBar.setVisibility(View.GONE);


                try {
                    //converting response to json object
                    JSONObject obj = new JSONObject(s);

                    //if no error in response
                    if (!obj.getBoolean("error")) {
                        Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();

                        //getting the user from the response
                        JSONObject userJson = obj.getJSONObject("user");

                        //creating a new user object
                        User user = new User(
                                userJson.getInt("id"),
                                userJson.getString("name"),
                                userJson.getString("department"),
                                userJson.getString("session"),
                                userJson.getString("email"),
                                userJson.getString("username"),
                                userJson.getString("rudf_position")

                        );

                        //storing the user in shared preferences
                        SharedPrefManager.getInstance(getApplicationContext()).userLogin(user);

                        //starting the profile activity
                        finish();
                        startActivity(new Intent(getApplicationContext(), Home.class));
                    } else {
                        Toast.makeText(getApplicationContext(), "Invalid username or mobile", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            protected String doInBackground(Void... voids) {
                //creating request handler object
                RequestHandler requestHandler = new RequestHandler();

                //creating request parameters
                HashMap<String, String> params = new HashMap<>();
                params.put("username", username);
                params.put("mobile", mobile);

                //returing the response
                return requestHandler.sendPostRequest(URLs.URL_LOGIN, params);
            }
        }

        UserLogin ul = new UserLogin();
        ul.execute();
    }
}