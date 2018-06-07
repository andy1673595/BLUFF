package com.andyhuang.bluff.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.andyhuang.bluff.CreateAccountPage.CreateAcccountPresenter;
import com.andyhuang.bluff.CreateAccountPage.CreateAccountContract;
import com.andyhuang.bluff.R;
import com.andyhuang.bluff.Constant.Constants;

public class CreateAccountPage extends BaseActivity implements View.OnClickListener,CreateAccountContract.View{
    //layout的變數
    private TextView textError;
    private EditText editEmail;
    private EditText editPassword;
    private EditText editName;
    private EditText editConfirmPassword;
    private ConstraintLayout layouOKButton;
    //區域變數
    private String emailInput = Constants.NODATA;
    private String passwordInput = Constants.NODATA;
    private String nameInput =Constants.NODATA;
    private String passwordConfirm =Constants.NODATA;

    //presenter
    private CreateAcccountPresenter mCreateAcccountPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.account_create_login_page);
        mCreateAcccountPresenter = new CreateAcccountPresenter(this);
        initView();
        layouOKButton.setOnClickListener(this);

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
                emailInput = String.valueOf(editEmail.getText());
                passwordInput = String.valueOf(editPassword.getText());
                nameInput = String.valueOf(editName.getText());
                passwordConfirm = String.valueOf(editConfirmPassword.getText());
                //檢查輸入是否正確,如果正確就執行創建帳號
                mCreateAcccountPresenter.checkEditTextIsLegal(emailInput,passwordInput,nameInput,passwordConfirm);
                break;
        }
    }


    @Override
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

    @Override
    public void setErrorText(String errorMessage) {
        textError.setText(errorMessage);
    }


    @Override
    public void setPresenter(Object presenter) {

    }
}
