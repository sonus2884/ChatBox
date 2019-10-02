package com.example.user.chatbox.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.user.chatbox.R;
import com.example.user.chatbox.Class.UserDetail;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText emailText;
    private EditText passwordText;
    private EditText rePasswordText;
    private Button signUpButton;
    private RelativeLayout backgroundRelativeLayout;
    private String password;
    private FirebaseAuth mAuth;
//    private ProgressBar progressBar;
    private ProgressDialog progressDialog;
    private EditText nameText;
    private DatabaseReference mDatabaseRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        TextInputLayout emailTextWrapper = findViewById(R.id.emailTextWrapper);
        TextInputLayout passwordTextWrapper = findViewById(R.id.passwordTextWrapper);
        TextInputLayout rePasswordTextWrapper = findViewById(R.id.rePasswordTextWrapper);
        TextInputLayout nameTextWrapper = findViewById(R.id.nameTextWrapper);

        emailTextWrapper.setHint("Email");
        passwordTextWrapper.setHint("Enter password");
        rePasswordTextWrapper.setHint("Re enter password");



        emailText = findViewById(R.id.emailText);
        passwordText = findViewById(R.id.passwordText);
        rePasswordText = findViewById(R.id.rePasswordText);
        nameText = findViewById(R.id.nameText1);

        signUpButton = findViewById(R.id.signUpButton);
//        progressBar = findViewById(R.id.spinner);
        backgroundRelativeLayout = findViewById(R.id.backgroundRelativeLayout);
        backgroundRelativeLayout.setOnClickListener(this);

        android.support.v7.widget.Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Sign up");

        mAuth = FirebaseAuth.getInstance();
    }

    public void signUp(View view) {

        String email = emailText.getText().toString().trim();
        final String name = nameText.getText().toString().trim();
        password = passwordText.getText().toString().trim();
        String rePassword = rePasswordText.getText().toString().trim();
        if (TextUtils.isEmpty(email)) {

            // Toast.makeText(SignUpActivity.this, "Please Enter email.", Toast.LENGTH_SHORT).show();
            emailText.setError("Please Enter email.");
            return;
        }
        if (TextUtils.isEmpty(password)) {

            //  Toast.makeText(SignUpActivity.this,"Please Password",Toast.LENGTH_SHORT).show();
            passwordText.setError("Please Password");
            return;
        }
        if (TextUtils.isEmpty(rePassword)) {

            Toast.makeText(SignUpActivity.this, "Please re-enter password", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!password.equals(rePassword)) {

            Toast.makeText(SignUpActivity.this, "Password doesn't match", Toast.LENGTH_SHORT).show();
            return;
        }

        rePasswordText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (!password.equals(s.toString())) {

                    rePasswordText.setError("Password does't match");

                    return;

                } else {

                    rePasswordText.setError(null);

                    return;
                }

            }

            @Override
            public void afterTextChanged(Editable s) {


            }
        });
        if (TextUtils.isEmpty(name)) {

            nameText.setError("Please enter name");
            return;
        }

//        progressBar.setVisibility(View.VISIBLE);
        progressDialog = new ProgressDialog(SignUpActivity.this);
        progressDialog.setMessage("Sign up..."); // Setting Message
        progressDialog.setTitle("ChatApp SignUp"); // Setting Title
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER); // Progress Dialog Style Spinner
        progressDialog.show(); // Display Progress Dialog
        progressDialog.setCancelable(false);

        Log.i("_Email", email);
        Log.i("_Password", password);
        Log.i("_RePassword", rePassword);
       Log.i("_Name", name);

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {

//                            progressBar.setVisibility(View.INVISIBLE);
                            progressDialog.dismiss();

                            mDatabaseRef = FirebaseDatabase.getInstance().getReference("User details")
                                    .child(mAuth.getUid());
                            UserDetail userDetail = new UserDetail();
                            userDetail.setName(name);
                            userDetail.setAbout("");
                            userDetail.setImageUri("");
                            userDetail.setReceiverUid(mAuth.getUid());
                            mDatabaseRef.setValue(userDetail);

                            Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
//                            progressBar.setVisibility(View.INVISIBLE);
                            progressDialog.dismiss();
                            Toast.makeText(SignUpActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }

                    }
                })
                .addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
//                        progressBar.setVisibility(View.INVISIBLE);
                        progressDialog.dismiss();

                        if (e != null) {

                            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                        }

                    }
                });


    }

    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.backgroundRelativeLayout) {

            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }
}
