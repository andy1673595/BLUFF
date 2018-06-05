package com.andyhuang.bluff.CreateAccountPage;
import android.util.Log;

import com.andyhuang.bluff.Callback.FirebaseLoginCallback;
import com.andyhuang.bluff.User.FirebaseCreateAccount;
import com.andyhuang.bluff.Util.Constants;
import com.andyhuang.bluff.activities.CreateAccountPage;

public class CreateAcccountPresenter implements CreateAccountContract.Presenter{
    private CreateAccountPage createAccountPageView;
    private FirebaseCreateAccount firebaseCreateAccount;

    public CreateAcccountPresenter(CreateAccountPage createAccountPage) {
        createAccountPageView = createAccountPage;
        firebaseCreateAccount = new FirebaseCreateAccount(createAccountPageView);
    }

    @Override
    public void start() {}

    @Override
    public void checkEditTextIsLegal(String emailInput, String passwordInput, String nameInput, String passwordConfirm) {
        if(isEditTextCheckedLegal(emailInput, passwordInput, nameInput, passwordConfirm)) {
            //It's a legal input, start to create account
            firebaseCreateAccount.creatAccount(firebaseCallback(),emailInput,passwordInput,nameInput);
        }
    }

    private boolean isEditTextCheckedLegal(String emailInput, String passwordInput, String nameInput, String passwordConfirm) {
        //check the EditText is legal or not
        if(emailInput.equals(Constants.NODATA)) {
            createAccountPageView.setErrorText("email 不能為空");
            return false;
        } else if (passwordInput.equals(Constants.NODATA)) {
            createAccountPageView.setErrorText("password 不能為空");
            return false;
        } else if (nameInput.equals(Constants.NODATA)) {
            createAccountPageView.setErrorText("名字不能為空");
            return false;
        } else if(passwordConfirm.equals(Constants.NODATA)){
            createAccountPageView.setErrorText("密碼確認不能為空");
            return false;
        }else if (!passwordInput.equals(passwordConfirm)) {
            createAccountPageView.setErrorText("密碼必須相同");
            return false;
        }else if(passwordInput.length()<6) {
            createAccountPageView.setErrorText("密碼至少要6個字");
            return false;
        }else {
            return true;
        }

    }

    private FirebaseLoginCallback firebaseCallback() {
        return new FirebaseLoginCallback() {
            @Override
            public void completed() {
                createAccountPageView.backToLoginAndStartMainActivity();
            }
            @Override
            public void loginFail() {
                Log.d(Constants.TAG,"firebase login fail");
            }
        };
    }
}
