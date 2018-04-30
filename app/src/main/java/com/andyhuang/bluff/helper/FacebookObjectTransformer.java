package com.andyhuang.bluff.helper;

import org.json.JSONException;
import org.json.JSONObject;

public class FacebookObjectTransformer {
    private String id;
    private String name;
    public FacebookObjectTransformer(JSONObject jsonInput) throws JSONException {
        id = jsonInput.getString("id");
        name = jsonInput.getString("first_name");

    }
}
