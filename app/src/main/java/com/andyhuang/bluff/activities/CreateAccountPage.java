package com.andyhuang.bluff.activities;

import android.content.Intent;
import android.net.wifi.aware.AttachCallback;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.andyhuang.bluff.Callback.CloseLoginPageCallback;
import com.andyhuang.bluff.Callback.FirebaseLoginCallback;
import com.andyhuang.bluff.R;
import com.andyhuang.bluff.Util.Constants;
import com.andyhuang.bluff.User.FirebaseCreateAccount;

public class CreateAccountPage extends BaseActivity implements View.OnClickListener{
    //layout的變數
    private TextView textError;
    private EditText editEmail;
    private EditText editPassword;
    private EditText editName;
    private EditText editConfirmPassword;
    private ConstraintLayout layouOKButton;
    //區域變數
    private String emailInput;
    private String passwordInput;
    private String nameInput;
    private String passwordConfirm;
    private FirebaseCreateAccount mFirebaseCreateAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.account_create_login_page);
        initView();
        layouOKButton.setOnClickListener(this);
        mFirebaseCreateAccount = new FirebaseCreateAccount(CreateAccountPage.this);
    }

    public void initView(){
        textError = findViewById(R.id.text_error_message_create_account);
        editEmail = findViewById(R.id.edit_email_create_account);
        editPassword = findViewById(R.id.edit_password_create_account);
        editName = findViewById(R.id.edit_name_create_account);
        editConfirmPassword = findViewById(R.id.edit_password_confirm);
        layouOKButton = findViewById(R.id.layout_completed_create_account);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.layout_completed_create_account:
                if(isEditTextCheckedLegal()) {
                    mFirebaseCreateAccount.creatAccount(firebaseCallback(),emailInput,passwordInput,nameInput);
                }
                break;
        }
    }
    //檢查輸入是否正確
    public boolean isEditTextCheckedLegal() {
        emailInput = String.valueOf(editEmail.getText());
        passwordInput = String.valueOf(editPassword.getText());
        nameInput = String.valueOf(editName.getText());
        passwordConfirm = String.valueOf(editConfirmPassword.getText());

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
        }else if(passwordInput.length()<6) {
            textError.setText("密碼至少要6個字");
            return false;
        }else {
            return true;
        }
    }

    public void backToLoginAndStartMainActivity() {
        //回到LoginActivity並且關掉LoginActivity前往MainActivity
        Intent intent=new Intent();
        intent.setClass(CreateAccountPage.this,Login.class);
        //告訴LoginActivity要前往MainActivity且關掉自己
        intent.putExtra("closeActivity",true);
        //切換Activity
        startActivity(intent);
        //關掉activity
        this.finish();
    }

    private FirebaseLoginCallback firebaseCallback() {
        return new FirebaseLoginCallback() {
            @Override
            public void completed() {
                backToLoginAndStartMainActivity();
            }

            @Override
            public void loginFail() {
                Log.d(Constants.TAG,"firebase login fail");
            }
        };
    }


}
