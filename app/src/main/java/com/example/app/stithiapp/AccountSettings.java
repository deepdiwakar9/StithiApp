package com.example.app.stithiapp;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AccountSettings extends AppCompatActivity {

    private TextView mName, mStatus;
    private Button statusButton;

    private String currentUser;

    private DatabaseReference userDatabse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_settings);

        mName = findViewById(R.id.settings_display_name);
        mStatus = findViewById(R.id.settings_status);
        statusButton = findViewById(R.id.settings_status_btn);

        currentUser = getIntent().getStringExtra("USERID");
        Log.d("NAME", currentUser);

        userDatabse = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUser);

        userDatabse.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String name = dataSnapshot.child("name").getValue().toString();
                String status = dataSnapshot.child("status").getValue().toString();
                Log.d("NAME", name);

                mName.setText(name);
                mStatus.setText(status);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("ERROR", databaseError.getDetails());
            }
        });

        statusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent statusIntent = new Intent(AccountSettings.this, StatusInputActivity.class);
                statusIntent.putExtra("STATUS", mStatus.getText());
                statusIntent.putExtra("USERID", currentUser);
                startActivity(statusIntent);
            }
        });
    }
}
