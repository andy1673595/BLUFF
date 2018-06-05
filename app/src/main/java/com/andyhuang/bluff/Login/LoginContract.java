package com.andyhuang.bluff.Login;

import com.andyhuang.bluff.BasePresenter;
import com.andyhuang.bluff.BaseView;
import com.andyhuang.bluff.Callback.FacebookLoginCallback;

public interface LoginContract {
    interface View extends BaseView {
        void startMainActivity();
        void startCreateAccountActivity();
        void setEditTextAccountAndPassword(String password, String email);
        void setEditTextHint(String passwordHint, String emailHint);
        void showToast(String message);
    }
    interface Presenter extends BasePresenter{
        void facebookLoginResult(final FacebookLoginCallback callback);
        void checkEditTextInput(String accountEmail, String password);
        void checkSharedPreference();
    }

}
