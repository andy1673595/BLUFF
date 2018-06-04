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

public class Login extends BaseActivity implements View.OnClickListener{
    //layout的變數宣告
    private ImageView imageLoginButton;
    private ImageView imageFBLoginIcon;
    private TextView textCreateAccountButton;
    private EditText editTextEmailAccountInput;
    private EditText editTextPasswordInput;
    private ConstraintLayout layoutFBLoginButton;
    //其他變數宣告
    private FirebaseAccount firebaseAccount;
    private CallbackManager callbackManager;
    private AccessToken accessToken;
    private FacebookLoginCallback callback = loginCallback();
    private FacebookUserData userDataAPI;

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
        checkSharedPreference();
        firebaseAccount = new FirebaseAccount(Login.this);
        userDataAPI = new FacebookUserData();
        callbackManager = CallbackManager.Factory.create();
        facebookLoginResult(callback);
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
                UserManager.getInstance().reset();
                String accountEmail = String.valueOf(editTextEmailAccountInput.getText());
                String password  = String.valueOf(editTextPasswordInput.getText());
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
    }

    @Override
    protected void onResume() {
        boolean sholdStartMainActivity = this.getIntent().getBooleanExtra("closeActivity",false);
        //come from create account page, completed sign up and start main activity
        if(sholdStartMainActivity) startMainActiviry();
        super.onResume();
    }


    public void startMainActiviry() {
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

    //檢查sharedPreference是否有資料存在
    private void checkSharedPreference() {
        if( Bluff.getContext().getSharedPreferences(Constants.TAG_FOR_SHAREDPREFREENCE,MODE_PRIVATE)
                .contains(Constants.USER_PASSWORD_SHAREDPREFREENCE)) {
            //it has password in sharedPreference
            String password = Bluff.getContext().getSharedPreferences(Constants.TAG_FOR_SHAREDPREFREENCE,MODE_PRIVATE)
                    .getString(Constants.USER_PASSWORD_SHAREDPREFREENCE,Constants.NODATA);
            String email =  Bluff.getContext().getSharedPreferences(Constants.TAG_FOR_SHAREDPREFREENCE,MODE_PRIVATE)
                    .getString(Constants.USER_EMAIL_SHAREDPREFREENCE,Constants.NODATA);

            if(password.equals(Constants.FACEBOOK_HINT)) {
                //使用者上次是用facebook登入,則清空email和password
                setEditTextAccountAndPassword("", "");
                editTextEmailAccountInput.setHint("請輸入信箱登入");
                editTextPasswordInput.setHint("請輸入密碼登入");
            } else {
                //使用者上次是用帳密登入,設定email和password
                setEditTextAccountAndPassword(password, email);
            }

        }
    }

    private void setEditTextAccountAndPassword(String password, String email) {
        editTextEmailAccountInput.setText(email);
        editTextPasswordInput.setText(password);
    }

    //facebook login Result
    public void facebookLoginResult(final FacebookLoginCallback callback) {
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
                startMainActiviry();
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
                startMainActiviry();
            }
            @Override
            public void loginFail() {
                Log.d(Constants.TAG,"firebase login fail");
            }
        };
    }

}
