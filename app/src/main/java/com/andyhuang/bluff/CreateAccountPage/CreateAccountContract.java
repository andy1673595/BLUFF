package com.andyhuang.bluff.CreateAccountPage;

import com.andyhuang.bluff.BasePresenter;
import com.andyhuang.bluff.BaseView;

public interface CreateAccountContract {
    interface View extends BaseView {
        void backToLoginAndStartMainActivity();
        void setErrorText(String errorMessage);
    }

    interface Presenter extends BasePresenter {
        void checkEditTextIsLegal(String emailInput, String passwordInput, String nameInput, String passwordConfirm);
    }

}
