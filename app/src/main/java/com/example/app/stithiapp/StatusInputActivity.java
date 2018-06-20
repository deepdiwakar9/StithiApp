package com.example.app.stithiapp;

import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class StatusInputActivity extends AppCompatActivity {

    Toolbar statusToolbar;
    TextInputLayout statusInput;
    String status, userID;
    Button submitBtn;

    DatabaseReference statusDatabse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status_input);

        statusToolbar = findViewById(R.id.status_input_toolbar);
        statusInput = findViewById(R.id.status_input);
        submitBtn = findViewById(R.id.statusSubmitBtn);

        setSupportActionBar(statusToolbar);
        getSupportActionBar().setTitle("Set Status");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        userID = getIntent().getStringExtra("USERID");
        status = getIntent().getStringExtra("STATUS");
        statusInput.getEditText().setText(status);

        statusDatabse = FirebaseDatabase.getInstance().getReference().child("Users").child(userID).child("status");

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                statusDatabse.setValue(statusInput.getEditText().getText().toString());
                finish();
            }
        });
    }
}
