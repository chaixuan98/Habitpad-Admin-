package com.example.habitpadadmin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;

public class AdminLoginActivity extends AppCompatActivity {
    TextInputLayout aEmail, aPassword;
    MaterialButton aSignIn;

    SharedPreferences sharedPreferences;

    public static final String fileName = "adminLogin";
    public static final String AEmail = "adminEmail";
    public static final String APassword = "adminPassword";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_login);

        // Getting UI views from our xml file
        aEmail = findViewById(R.id.adminemaillayout);
        aPassword = findViewById(R.id.adminpasswordlayout);
        aSignIn = findViewById(R.id.admin_signinbtn);

        sharedPreferences = getSharedPreferences(fileName, Context.MODE_PRIVATE);
        if(sharedPreferences.contains(AEmail)){
            Toast.makeText(AdminLoginActivity.this,"Admin login successfully",Toast.LENGTH_SHORT).show();
            // Finish
            finish();
            // Start activity dashboard
            startActivity(new Intent(AdminLoginActivity.this,MainActivity.class));
        }

        aSignIn.setOnClickListener(v -> {

            if(!validateEmail() | !validatePassword()){
                return;
            }

            if (aEmail.getEditText().getText().toString().equals("admin@admin.com") &&  aPassword.getEditText().getText().toString().equals("admin147")) {

                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(AEmail,aEmail.getEditText().getText().toString());
                editor.putString(APassword,aPassword.getEditText().getText().toString());
                editor.commit();

                Toast.makeText(AdminLoginActivity.this,"Admin login successfully",Toast.LENGTH_SHORT).show();
                // Finish
                finish();
                // Start activity dashboard
                startActivity(new Intent(AdminLoginActivity.this,MainActivity.class));
            }
            else{
                Toast.makeText(AdminLoginActivity.this,"Invalid Admin Details",Toast.LENGTH_SHORT).show();
            }

        });

    }

    private  boolean validateEmail(){
        String val = aEmail.getEditText().getText().toString().trim();
        String checkEmail = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

        if(val.isEmpty()){
            aEmail.setError("Field cannot be empty");
            return false;
        } else if(!val.matches(checkEmail)){
            aEmail.setError("Invalid Email!");
            return false;
        } else{
            aEmail.setError(null);
            aEmail.setErrorEnabled(false);
            return true;
        }
    }

    private  boolean validatePassword(){
        String val = aPassword.getEditText().getText().toString().trim();

        if(val.isEmpty()){
            aPassword.setError("Field cannot be empty");
            return false;
        } else{
            aPassword.setError(null);
            aPassword.setErrorEnabled(false);
            return true;
        }
    }

}