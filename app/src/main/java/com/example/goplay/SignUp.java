package com.example.goplay;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseUser;

public class SignUp extends AppCompatActivity implements FBAuthHelper.FBReply{

    private FBAuthHelper fbAuthHelper;
    private EditText etEmail;
    private EditText etPwd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sign_up);

        etEmail = findViewById(R.id.emailEditText);
        etPwd = findViewById(R.id.passwordEditText);

        fbAuthHelper = new FBAuthHelper(this);
        if(fbAuthHelper.getCurrentUser() != null)
            startActivity(new Intent(this, MainActivity.class));

        findViewById(R.id.signUpButton).setOnClickListener(v -> {
            if( checkEmailValidity(etEmail.getText().toString()) &&
                    checkPasswordValidity(etPwd.getText().toString()) )

                fbAuthHelper.createUser(
                        etEmail.getText().toString(),
                        etPwd.getText().toString());
        });


    }
    private boolean checkPasswordValidity(String password) {
        if (password.length() >= 6) {
            // Password is valid
            return true;
        } else {
            // Password is invalid, show an error message
            etPwd.setError("Password must be at least 6 characters long");
            return false;
        }
    }

    private boolean checkEmailValidity(String email) {
        if (android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            // Email is valid
            return true;
        } else {
            // Email is invalid, show an error message
            etEmail.setError("Invalid email address");
            return false;
        }
    }

    @Override
    public void createUserSuccess(FirebaseUser user) {
        Toast.makeText(this, "Welcome " + user.getEmail(), Toast.LENGTH_SHORT).show();
        startActivity(new Intent(this, MainActivity.class));
    }

    @Override
    public void loginSuccess(FirebaseUser user) {

    }
}