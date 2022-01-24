package com.example.newapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText signUpEmailEditText, signUpPasswordEditText, signUpConfirmPasswordEditText, signUpNameEditText;
    private Button registerButton, loginButton;
    private FirebaseAuth fAuth;
    private FirebaseFirestore fStore;
    String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        this.setTitle("Signup Page");

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        signUpNameEditText = findViewById(R.id.userNameEditTextId);
        signUpEmailEditText = findViewById(R.id.signUpEmailEditTextId);
        signUpPasswordEditText = findViewById(R.id.signUpPasswordEditTextId);
        signUpConfirmPasswordEditText = findViewById(R.id.signUpConfirmPasswordEditTextId);

        registerButton = findViewById(R.id.registerButtonId);
        loginButton = findViewById(R.id.loginButtonId);

        registerButton.setOnClickListener(this);
        loginButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.registerButtonId:
                userRegister();
                break;

            case R.id.loginButtonId:
                startActivity(new Intent(getApplicationContext(), SignInActivity.class));
                break;
        }
    }

    private void userRegister() {

        String name = signUpNameEditText.getText().toString().trim();
        String email = signUpEmailEditText.getText().toString().trim();
        String password = signUpPasswordEditText.getText().toString().trim();
        String confirmPassword = signUpConfirmPasswordEditText.getText().toString().trim();

        if (name.isEmpty()) {
            signUpNameEditText.setError("Name");
            signUpNameEditText.requestFocus();
            return;
        }

        //checking the validity of the email
        if (email.isEmpty()) {
            signUpEmailEditText.setError("Email Address");
            signUpEmailEditText.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            signUpEmailEditText.setError("Enter Valid Email");
            signUpEmailEditText.requestFocus();
            return;
        }

        //checking the validity of the password
        if (password.isEmpty()) {
            signUpPasswordEditText.setError("Enter a password");
            signUpPasswordEditText.requestFocus();
            return;
        }

        if (password.length() < 6) {
            signUpEmailEditText.setError("Minimum length of a password should be 6");
            signUpEmailEditText.requestFocus();
            return;
        }

        if (confirmPassword.isEmpty()) {
            signUpConfirmPasswordEditText.setError("Enter an password address");
            signUpConfirmPasswordEditText.requestFocus();
            return;
        }

        if (!password.equals(confirmPassword)) {
            signUpConfirmPasswordEditText.setError("Password Do not Match.");
            return;
        }

        fAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {

                    userID = fAuth.getCurrentUser().getUid();
                    DocumentReference documentReference = fStore.collection("users").document(userID);
                    Map<String, Object> user = new HashMap<>();

                    user.put("name", name);
                    user.put("email", email);
                    user.put("phone", "");
                    user.put("department", "");
                    documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                        }
                    });

                    Toast.makeText(getApplicationContext(), "Register is Successful.", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    finish();
                }
                else {
                    if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                        Toast.makeText(getApplicationContext(), "User is already Registered.", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "Error : " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
}