package com.andyhuang.bluff.User;

import android.os.Bundle;

import com.andyhuang.bluff.Callback.GetFacebookUserDataCallback;
import com.andyhuang.bluff.activities.Login;
import com.andyhuang.bluff.helper.FacebookObjectTransformer;
import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;

import org.json.JSONException;
import org.json.JSONObject;

public class FacebookUserData {
    public void getUserData(final GetFacebookUserDataCallback callback, AccessToken accessToken, final Login login) {
        GraphRequest request = GraphRequest.newMeRequest(
                accessToken,
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(
                            JSONObject object,
                            GraphResponse response) {
                        try {
                            FacebookObjectTransformer transformer = new FacebookObjectTransformer(object,login,callback);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,name,link");
        request.setParameters(parameters);
        request.executeAsync();
    }

}
