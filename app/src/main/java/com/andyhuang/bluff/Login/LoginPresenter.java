package com.andyhuang.bluff.Login;

import android.util.Log;
import android.widget.Toast;

import com.andyhuang.bluff.Bluff;
import com.andyhuang.bluff.Callback.FacebookLoginCallback;
import com.andyhuang.bluff.Callback.FirebaseLoginCallback;
import com.andyhuang.bluff.Callback.GetFacebookUserDataCallback;
import com.andyhuang.bluff.R;
import com.andyhuang.bluff.User.FacebookUserData;
import com.andyhuang.bluff.User.FirebaseAccount;
import com.andyhuang.bluff.Util.Constants;
import com.andyhuang.bluff.activities.Login;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;

import static android.content.Context.MODE_PRIVATE;

public class LoginPresenter implements LoginContract.Presenter{
    Login loginView;
    private FirebaseAccount firebaseAccount;
    private CallbackManager callbackManager;
    private AccessToken accessToken;
    private FacebookUserData userDataAPI;
    
    public LoginPresenter(Login loginViewInput) {
        loginView = loginViewInput;
        firebaseAccount = new FirebaseAccount(loginView,this);
        userDataAPI = new FacebookUserData();
        callbackManager = CallbackManager.Factory.create();
        facebookLoginResult(mFacebookLoginCallback);
    }


    @Override
    public void start() {

    }

    //facebook login Result
    @Override
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
                userDataAPI.getUserData(mGetFacebookUserDataCallback,accessToken,loginView);
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
    //檢查email和password不為空
    @Override
    public void checkEditTextInput(String accountEmail, String password) {
        if(accountEmail.isEmpty()||password.isEmpty()) {
            loginView.showToast(loginView.getString(R.string.account_and_password_null_error_message));

        }else {
            firebaseAccount.login(accountEmail,password,firebaseCallback());
        }
    }

    @Override
    public void checkSharedPreference() {
        if( Bluff.getContext().getSharedPreferences(Constants.TAG_FOR_SHAREDPREFREENCE,MODE_PRIVATE)
                .contains(Constants.USER_PASSWORD_SHAREDPREFREENCE)) {
            //it has password in sharedPreference
            String password = Bluff.getContext().getSharedPreferences(Constants.TAG_FOR_SHAREDPREFREENCE,MODE_PRIVATE)
                    .getString(Constants.USER_PASSWORD_SHAREDPREFREENCE,Constants.NODATA);
            String email =  Bluff.getContext().getSharedPreferences(Constants.TAG_FOR_SHAREDPREFREENCE,MODE_PRIVATE)
                    .getString(Constants.USER_EMAIL_SHAREDPREFREENCE,Constants.NODATA);

            if(password.equals(Constants.FACEBOOK_HINT)) {
                //使用者上次是用facebook登入,則清空email和password
                loginView.setEditTextAccountAndPassword("", "");
                loginView.setEditTextHint(Bluff.getContext().getString(R.string.please_input_email),
                                          Bluff.getContext().getString(R.string.please_input_password));

            } else {
                //使用者上次是用帳密登入,設定email和password
                loginView.setEditTextAccountAndPassword(password, email);
            }

        }
    }

    public CallbackManager getCallbackManager() {
        return callbackManager;
    }

    private FirebaseLoginCallback firebaseCallback() {
        return new FirebaseLoginCallback() {
            @Override
            public void completed() {
                loginView.startMainActivity();
            }
            @Override
            public void loginFail() {
                Log.d(Constants.TAG,"firebase login fail");
            }
        };
    }

    private FacebookLoginCallback mFacebookLoginCallback =new FacebookLoginCallback() {
        @Override
        public void loginSuccess() {
            Log.d(Constants.TAG, "facebook login success");
            loginView.startMainActivity();
        }

        @Override
        public void loginFail() {
            {
                Log.d(Constants.TAG, "facebook login fail");
            }
        }
    };

    private GetFacebookUserDataCallback mGetFacebookUserDataCallback = new GetFacebookUserDataCallback() {
        @Override
        public void completed() {
            Log.d(Constants.TAG,"Got facebook data!");
        }
    };


}
