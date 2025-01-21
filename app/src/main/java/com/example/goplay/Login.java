package com.example.goplay;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseUser;

public class Login extends AppCompatActivity implements FBAuthHelper.FBReply {
    private Button btnLogin;
    private Button btnGoToSignUp;
    private FBAuthHelper fbAuthHelper;
    private EditText etEmail;
    private EditText etPwd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        etEmail = findViewById(R.id.emailEditText);
        etPwd = findViewById(R.id.passwordEditText);

        fbAuthHelper = new FBAuthHelper(this);
        if(fbAuthHelper.getCurrentUser() != null)
            startActivity(new Intent(this, MainActivity.class));
        findViewById(R.id.btnLogin).setOnClickListener(v -> {
            if(     checkEmailValidity(etEmail.getText().toString()) &&
                    checkPasswordValidity(etPwd.getText().toString()) )

                fbAuthHelper.login(
                        etEmail.getText().toString(),
                        etPwd.getText().toString());

        });

        btnGoToSignUp= findViewById(R.id.btnGoToSignUp);
        btnGoToSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this,SignUp.class);
                startActivity(intent);
            }
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

    }

    @Override
    public void loginSuccess(FirebaseUser user) {
        if (etEmail.equals("Admin@gmail.com")){

        }
        startActivity(new Intent(this, MainActivity.class));
    }
}