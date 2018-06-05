package com.andyhuang.bluff.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.andyhuang.bluff.Bluff;
import com.andyhuang.bluff.Callback.FacebookLoginCallback;
import com.andyhuang.bluff.Callback.FirebaseLoginCallback;
import com.andyhuang.bluff.Callback.GetFacebookUserDataCallback;
import com.andyhuang.bluff.Login.LoginContract;
import com.andyhuang.bluff.Login.LoginPresenter;
import com.andyhuang.bluff.R;
import com.andyhuang.bluff.User.FacebookUserData;
import com.andyhuang.bluff.User.FirebaseAccount;
import com.andyhuang.bluff.User.UserManager;
import com.andyhuang.bluff.Util.Constants;
import com.andyhuang.bluff.helper.ImageUtils;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import java.util.Arrays;

public class Login extends BaseActivity implements View.OnClickListener,LoginContract.View{
    //layout的變數宣告
    private ImageView imageLoginButton;
    private ImageView imageFBLoginIcon;
    private TextView textCreateAccountButton;
    private EditText editTextEmailAccountInput;
    private EditText editTextPasswordInput;
    private ConstraintLayout layoutFBLoginButton;
    //其他變數宣告
    private LoginPresenter mLoginPresenter;


    @Override
    protected void onCreate(Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        initView();
        setButtonListener();
        //登入按鈕切圓角
        Bitmap bitmapIconFBLogin = BitmapFactory.decodeResource(Bluff.getContext().getResources(),
                R.drawable.facebook_icon);
        imageFBLoginIcon.setImageBitmap(ImageUtils.toRoundCorner(bitmapIconFBLogin,50,5));
        //檢查sharedPrefrence是否有帳號密碼,有的話直接從裡面拿出來不用讓使用者重新輸入
        mLoginPresenter = new LoginPresenter(this);
        mLoginPresenter.checkSharedPreference();
    }

    //把layout的變數找到對應的id
    private void initView() {
        imageLoginButton =findViewById(R.id.image_login_button);
        imageFBLoginIcon = findViewById(R.id.image_fb_icon);
        textCreateAccountButton = findViewById(R.id.text_create_account);
        editTextEmailAccountInput = findViewById(R.id.edit_login_account);
        editTextPasswordInput = findViewById(R.id.edit_login_password);
        layoutFBLoginButton = findViewById(R.id.constraintLayout_fb_login);
    }

    private void setButtonListener() {
        imageLoginButton.setOnClickListener(this);
        textCreateAccountButton.setOnClickListener(this);
        layoutFBLoginButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.image_login_button:
                //登入按鈕, 檢查帳號密碼不為空之後登入
                UserManager.getInstance().reset();
                String accountEmail = String.valueOf(editTextEmailAccountInput.getText());
                String password  = String.valueOf(editTextPasswordInput.getText());
                mLoginPresenter.checkEditTextInput(accountEmail,password);
                break;
            case R.id.text_create_account:
                //創建帳號
                UserManager.getInstance().reset();
                startCreateAccountActivity();
                break;
            case R.id.constraintLayout_fb_login:
                //fb登入按鈕
                UserManager.getInstance().reset();
                LoginManager.getInstance().logInWithReadPermissions(this,
                        Arrays.asList("public_profile", "user_friends","email"));
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
            super.onActivityResult(requestCode, resultCode, data);
            mLoginPresenter.getCallbackManager().onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onResume() {
        boolean sholdStartMainActivity = this.getIntent().getBooleanExtra("closeActivity",false);
        //come from create account page, completed sign up and start main activity
        if(sholdStartMainActivity) startMainActivity();
        super.onResume();
    }


    @Override
    public void startMainActivity() {
        //設定切換Activity時所需要的參數
        Intent intent = new Intent();
        intent.setClass(Login.this,BluffMainActivity.class);
        //切換Activity
        startActivity(intent);
        //關掉activity
        this.finish();
    }

    //開啟創建帳號的豔面
    public void startCreateAccountActivity() {
        Intent intent = new Intent();
        intent.setClass(Login.this,CreateAccountPage.class);
        //切換Activity
        startActivity(intent);
    }

    @Override
    public void setEditTextAccountAndPassword(String password, String email) {
        editTextEmailAccountInput.setText(email);
        editTextPasswordInput.setText(password);
    }

    @Override
    public void setEditTextHint(String passwordHint, String emailHint) {
        editTextEmailAccountInput.setHint(passwordHint);
        editTextPasswordInput.setHint(emailHint);
    }

    @Override
    public void showToast(String message) {
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void setPresenter(Object presenter) {

    }
}
