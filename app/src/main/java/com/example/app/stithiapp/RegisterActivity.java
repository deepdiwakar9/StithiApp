package com.example.app.stithiapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {

    private TextInputLayout regName, regEmail, regPwd, regCnfrmPwd;
    private Button createAccountBtn;
    private Toolbar registerToolbar;

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    private ProgressDialog mRegProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //Set up Toolbar
        registerToolbar = findViewById(R.id.register_toolbar);
        setSupportActionBar(registerToolbar);
        getSupportActionBar().setTitle("Create Account");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mRegProgressDialog = new ProgressDialog(this);

        mAuth = FirebaseAuth.getInstance();
        regName = findViewById(R.id.register_name);
        regEmail = findViewById(R.id.register_email);
        regPwd = findViewById(R.id.register_password);
        regCnfrmPwd = findViewById(R.id.register_confirm_password);
        createAccountBtn = findViewById(R.id.createAcc);


        createAccountBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String stringName = regName.getEditText().getText().toString();
                String stringEmail = regEmail.getEditText().getText().toString();
                String stringPwd = regPwd.getEditText().getText().toString();
                String stringCnfrmPwd = regCnfrmPwd.getEditText().getText().toString();
                Log.d("DATA", stringName);

                if(stringName.isEmpty() || stringEmail.isEmpty() || stringCnfrmPwd.isEmpty() || stringPwd.isEmpty()){
                    Toast.makeText(getApplicationContext(), "Please fill all the details", Toast.LENGTH_SHORT).show();
                }else{
                    if(!stringCnfrmPwd.equals(stringPwd)){
                        Toast.makeText(getApplicationContext(), "Password and Confirm Password aren't same", Toast.LENGTH_SHORT).show();
                    }
                    else {

                        mRegProgressDialog.setTitle("Registering User");
                        mRegProgressDialog.setMessage("Please wait while we create your account!");
                        mRegProgressDialog.setCanceledOnTouchOutside(false);
                        mRegProgressDialog.show();
                        registerUser(stringEmail, stringPwd, stringName);
                    }
                }
            }
        });

    }

    private void registerUser(String stringEmail, String stringPwd, final String stringName) {
        mAuth.createUserWithEmailAndPassword(stringEmail, stringPwd).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){

                    FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                    String currentUid = currentUser.getUid().toString();
                    mDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUid);

                    Log.d("Current User", currentUid + stringName);

                    //Adding User Details in Hashmap
                    HashMap<String, String> userDetails = new HashMap<>();
                    userDetails.put("name", stringName);
                    userDetails.put("status", "Hey, There!!!");
                    userDetails.put("image", "default, There!!!");

                    mDatabase.setValue(userDetails).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){

                                mRegProgressDialog.dismiss();
                                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        }
                    });

                }else {
                    mRegProgressDialog.hide();
                    Toast.makeText(getApplicationContext(), "Can't Sign in, please check the form and try again", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
