package com.andyhuang.bluff.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import com.andyhuang.bluff.Callback.FirebaseLoginCallback;
import com.andyhuang.bluff.R;
import com.andyhuang.bluff.Util.Constants;
import com.andyhuang.bluff.User.FirebaseCreateAccount;

public class CreateAccountPage extends BaseActivity implements View.OnClickListener{
    private EditText editEmail;
    private EditText editPassword;
    private EditText editName;
    private TextView textCompleted;
    private EditText editConfirmPassword;
    private TextView textError;
    private String emailInput;
    private String passwordInput;
    private String nameInput;
    private String photoAddressInput;
    private String passwordConfirm;
    private FirebaseCreateAccount mFirebaseCreateAccount;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.account_create_login_page);
        editEmail = findViewById(R.id.edit_email_create_account);
        editPassword = findViewById(R.id.edit_password_create_account);
        editName = findViewById(R.id.edit_name_create_account);
        editConfirmPassword = findViewById(R.id.edit_password_confirm);
        textCompleted = findViewById(R.id.text_completed_create_account);
        textError = findViewById(R.id.text_error_message_create_account);
        textCompleted.setOnClickListener(this);

        mFirebaseCreateAccount = new FirebaseCreateAccount(CreateAccountPage.this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.text_completed_create_account:
                if(checkEditError()) {
                    mFirebaseCreateAccount.creatAccount(firebaseCallback(),emailInput,passwordInput,nameInput,photoAddressInput);
                }
                break;
        }
    }

    public boolean checkEditError() {
        emailInput = String.valueOf(editEmail.getText());
        passwordInput = String.valueOf(editPassword.getText());
        nameInput = String.valueOf(editName.getText());
        passwordConfirm = String.valueOf(editConfirmPassword.getText());
        //TODO  let user set photo,get user photoURL
        photoAddressInput= Constants.NODATA;

        if(emailInput.isEmpty()) {
            textError.setText("email不能為空");
            return false;
        } else if (passwordInput.isEmpty()) {
            textError.setText("password不能為空");
            return false;
        } else if (nameInput.isEmpty()) {
            textError.setText("名字不能為空");
            return false;
        } else if(passwordConfirm.isEmpty()){
            textError.setText("密碼確認不能為空");
            return false;
        }else if (!passwordInput.equals(passwordConfirm)) {
            textError.setText("密碼必須相同");
            return false;
        } else {
            return true;
        }
    }

    private FirebaseLoginCallback firebaseCallback() {
        return new FirebaseLoginCallback() {
            @Override
            public void completed() {
                startMainHallActiviry();
            }

            @Override
            public void loginFail() {
                Log.d(Constants.TAG,"firebase login fail");
            }
        };
    }

    public void startMainHallActiviry() {
        //設定切換Activity時所需要的參數
        Intent intent = new Intent();
        intent.setClass(CreateAccountPage.this,MainHallPage.class);
        //切換Activity
        startActivity(intent);
        //關掉activity
        this.finish();
    }
}
