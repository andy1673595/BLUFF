package com.andyhuang.bluff.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.andyhuang.bluff.R;
import com.andyhuang.bluff.User.FirebaseAccount;

public class Login extends BaseActivity implements View.OnClickListener{
    private TextView loginButton;
    private TextView createButton;
    private TextView facebookLoginButton;
    private EditText accountInput;
    private EditText passwordInput;
    FirebaseAccount mFirebaseAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        loginButton = (TextView)findViewById(R.id.text_login_loginpage);
        createButton = (TextView)findViewById(R.id.text_creat_loginpage);
        facebookLoginButton = (TextView)findViewById(R.id.text_fb_login_loginpage);
        accountInput = (EditText)findViewById(R.id.edit_login_account);
        passwordInput = (EditText)findViewById(R.id.edit_login_password);
        mFirebaseAccount = new FirebaseAccount(Login.this);

        loginButton.setOnClickListener(this);
        createButton.setOnClickListener(this);
        facebookLoginButton.setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.text_login_loginpage:
                break;
            case R.id.text_creat_loginpage:
                mFirebaseAccount.creatAccount(String.valueOf(accountInput.getText()),String.valueOf(passwordInput.getText()));
                break;
            case R.id.text_fb_login_loginpage:
                break;
        }


    }
}
