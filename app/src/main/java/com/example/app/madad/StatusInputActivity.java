package com.example.app.madad;

import android.app.ProgressDialog;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class StatusInputActivity extends AppCompatActivity {

    ProgressDialog statusDialog;
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
                statusDialog = new ProgressDialog(StatusInputActivity.this);
                statusDialog.setTitle("Saving Changes!!");
                statusDialog.setMessage("Please wait while we save the changes");
                statusDialog.show();

                statusDatabse.setValue(statusInput.getEditText().getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            statusDialog.dismiss();
                        }else{
                            Toast.makeText(getApplicationContext(),"You got some Errors", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }
}
