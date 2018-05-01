package com.andyhuang.bluff.helper;

import com.andyhuang.bluff.Bluff;
import com.andyhuang.bluff.Callback.GetFacebookUserDataCallback;
import com.andyhuang.bluff.User.UserManager;
import com.andyhuang.bluff.Util.Constants;
import com.andyhuang.bluff.activities.Login;

import org.json.JSONException;
import org.json.JSONObject;

public class FacebookObjectTransformer {
    private String id;
    private String name;
    private String userPhotoURL;
    public FacebookObjectTransformer(JSONObject jsonInput,Login login,GetFacebookUserDataCallback callback) throws JSONException {
        id = jsonInput.getString("id");
        name = jsonInput.getString("name");
        userPhotoURL = "https://graph.facebook.com/" + id + "/picture?type=large";

        UserManager.getInstance().setUserName(name);
        UserManager.getInstance().setFacebookID(id);
        UserManager.getInstance().setUserPhotoUrl(userPhotoURL);

        Bluff.getContext().getSharedPreferences(Constants.TAG_FOR_SHAREDPREFREENCE,login.MODE_PRIVATE).edit()
                .putString(Constants.USER_NAME_SHAREDPREFREENCE,name)
                .putString(Constants.FACEBOOKID_SHAREDPREFREENCE,id)
                .putString(Constants.FACEBOOK_PHOTO_URL_SHAREDPREFREENCE,userPhotoURL)
                .commit();
        callback.completed();
    }
}
