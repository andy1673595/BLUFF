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
    private EditText editPhoto_address;
    private TextView textCompleted;
    private TextView textError;
    private String emailInput;
    private String passwordInput;
    private String nameInput;
    private String photoAddressInput;
    private FirebaseCreateAccount mFirebaseCreateAccount;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.account_create_login_page);
        editEmail = findViewById(R.id.edit_email_create_account);
        editPassword = findViewById(R.id.edit_password_create_account);
        editName = findViewById(R.id.edit_name_create_account);
        editPhoto_address = findViewById(R.id.edit_photo_create_account);
        textCompleted = findViewById(R.id.text_completed_create_account);
        textError = findViewById(R.id.text_error_message_create_account);
        textCompleted.setOnClickListener(this);

        mFirebaseCreateAccount = new FirebaseCreateAccount(CreateAccountPage.this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.text_completed_create_account:
                if(checkEditNull()) {
                    mFirebaseCreateAccount.creatAccount(firebaseCallback(),emailInput,passwordInput,nameInput,photoAddressInput);
                }
                break;
        }
    }

    public boolean checkEditNull() {
        emailInput = String.valueOf(editEmail.getText());
        passwordInput = String.valueOf(editPassword.getText());
        nameInput = String.valueOf(editName.getText());
        photoAddressInput = String.valueOf(editPhoto_address.getText());
        if(photoAddressInput.isEmpty()) {
            photoAddressInput= Constants.NODATA;
        }

        if(emailInput.isEmpty()) {
            textError.setText("email不能為空");
            return false;
        } else if (passwordInput.isEmpty()) {
            textError.setText("password不能為空");
            return false;
        } else if (nameInput.isEmpty()) {
            textError.setText("名字不能為空");
            return false;
        } else return true;
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
