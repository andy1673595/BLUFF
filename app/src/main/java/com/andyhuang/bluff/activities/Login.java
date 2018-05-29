package com.andyhuang.bluff.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Parcel;
import android.support.constraint.ConstraintLayout;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.andyhuang.bluff.Bluff;
import com.andyhuang.bluff.Callback.CloseLoginPageCallback;
import com.andyhuang.bluff.Callback.FacebookLoginCallback;
import com.andyhuang.bluff.Callback.FirebaseLoginCallback;
import com.andyhuang.bluff.Callback.GetFacebookUserDataCallback;
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

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Login extends BaseActivity implements View.OnClickListener{
    private ImageView imageLoginButton;
    private TextView textCreateButton;
    private EditText accountInput;
    private EditText passwordInput;
    private FirebaseAccount firebaseAccount;
    private CallbackManager callbackManager;
    private AccessToken accessToken;
    private FacebookLoginCallback callback = loginCallback();
    private FacebookUserData userDataAPI;
    private ConstraintLayout layoutFBLogin;
    private ImageView imageFBLoginIcon;


    @Override
    protected void onCreate(Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        layoutFBLogin = findViewById(R.id.constraintLayout_fb_login);
        imageLoginButton =findViewById(R.id.image_login_button);
        textCreateButton = findViewById(R.id.text_create_account);
        accountInput = (EditText)findViewById(R.id.edit_login_account);
        passwordInput = (EditText)findViewById(R.id.edit_login_password);
        imageFBLoginIcon = (ImageView)findViewById(R.id.image_fb_icon);
        Bitmap bitmapIconFBLogin = BitmapFactory.decodeResource(Bluff.getContext().getResources(),
                R.drawable.facebook_icon);
        //set round corner
        imageFBLoginIcon.setImageBitmap(ImageUtils.toRoundCorner(bitmapIconFBLogin,50,5));
        checkSharedPrefrence();
        imageLoginButton.setOnClickListener(this);
        textCreateButton.setOnClickListener(this);
        layoutFBLogin.setOnClickListener(this);

        firebaseAccount = new FirebaseAccount(Login.this);
        userDataAPI = new FacebookUserData();

        callbackManager = CallbackManager.Factory.create();
        login(callback);
    }

    private void checkSharedPrefrence() {
        if( Bluff.getContext().getSharedPreferences(Constants.TAG_FOR_SHAREDPREFREENCE,MODE_PRIVATE)
                .contains(Constants.USER_PASSWORD_SHAREDPREFREENCE)) {
            //it has password in sharedprefrence
            String password = Bluff.getContext().getSharedPreferences(Constants.TAG_FOR_SHAREDPREFREENCE,MODE_PRIVATE)
                    .getString(Constants.USER_PASSWORD_SHAREDPREFREENCE,Constants.NODATA);
            String email =  Bluff.getContext().getSharedPreferences(Constants.TAG_FOR_SHAREDPREFREENCE,MODE_PRIVATE)
                    .getString(Constants.USER_EMAIL_SHAREDPREFREENCE,Constants.NODATA);

            if(password.equals(Constants.FACEBOOK_HINT)) {
                //this is facebook account,don't show
                accountInput.setText("");
                passwordInput.setText("");
                accountInput.setHint("請輸入信箱登入");
                passwordInput.setHint("請輸入密碼登入");
            } else {
                //email account , show sharedprefrence
                accountInput.setText(email);
                passwordInput.setText(password);
            }

        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.image_login_button:
                UserManager.getInstance().reset();
                String accountEmail = String.valueOf(accountInput.getText());
                String password  = String.valueOf(passwordInput.getText());
                if(accountEmail.isEmpty()||password.isEmpty()) {
                    Toast.makeText(this,"帳號或密碼不得為空白",Toast.LENGTH_SHORT).show();
                }else {
                    firebaseAccount.login(accountEmail,password,firebaseCallback());
                }

                break;
            case R.id.text_create_account:
                UserManager.getInstance().reset();
                startCreateAccountActivity();
                break;
            case R.id.constraintLayout_fb_login:
                UserManager.getInstance().reset();
                LoginManager.getInstance().logInWithReadPermissions(this,
                        Arrays.asList("public_profile", "user_friends","email"));
                break;
        }


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
        if(requestCode == Constants.CLOSE_ACTIVITY) {
            //close login page
            this.finish();
        }
    }

    //facebook login
    public void login(final FacebookLoginCallback callback) {
        callbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {

            @Override
            public void onSuccess(LoginResult loginResult) {
                //拿到accessToken
                accessToken = loginResult.getAccessToken();
                if (accessToken == null) {
                    Log.d(Constants.TAG, "token null");
                }
                userDataAPI.getUserData(userDataCallback(),accessToken,Login.this);
                firebaseAccount.facebookLogin(accessToken,callback);
            }

            @Override
            public void onCancel() {
                callback.loginFail();
            }

            @Override
            public void onError(FacebookException error) {
                Log.d(Constants.TAG,"facebook login error: "+error.getMessage());
                callback.loginFail();
            }
        });
    }

    private FacebookLoginCallback loginCallback() {
        return new FacebookLoginCallback() {
            @Override
            public void loginSuccess() {
                Log.d(Constants.TAG,"facebook login success");
                startMainHallActiviry();
            }

            @Override
            public void loginFail() {
                Log.d(Constants.TAG,"facebook login fail");
            }
        };
    }

    private GetFacebookUserDataCallback userDataCallback(){
        return new GetFacebookUserDataCallback() {
            @Override
            public void completed() {
               Log.d(Constants.TAG,"Got facebook data!");
            }
        };
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
        intent.setClass(Login.this,MainHallPage.class);
        //切換Activity
        startActivity(intent);
        //關掉activity
        this.finish();
    }

    public void startCreateAccountActivity() {
        Intent intent = new Intent();
        intent.setClass(Login.this,CreateAccountPage.class);
        //切換Activity
        startActivity(intent);
    }

}
