package com.androiddeft.firebasepushnotification;

import android.content.Intent;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

public class ProfileActivity extends AppCompatActivity {

    TextView name, department, session, email, position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        //if the user is not logged in
        //starting the login activity
        if (!SharedPrefManager.getInstance(this).isLoggedIn()) {
            finish();
            startActivity(new Intent(this, LoginActivity.class));
        }

        name = (TextView) findViewById(R.id.name);
        department = (TextView) findViewById(R.id.department);
        session = (TextView) findViewById(R.id.session);
        /*  mobile = (TextView) findViewById(R.id.mobile);*/
        email = (TextView) findViewById(R.id.email);
        position = (TextView) findViewById(R.id.position);


        //getting the current user
        User user = SharedPrefManager.getInstance(this).getUser();

        //setting the values to the textviews
        name.setText(user.getName());
        department.setText(user.getDepartment());
        session.setText(user.getSession());
        email.setText(user.getEmail());
        position.setText(user.getRudf_position());

        //when the user presses logout button
        //calling the logout method
        findViewById(R.id.buttonLogout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                SharedPrefManager.getInstance(getApplicationContext()).logout();
            }
        });
    }

}