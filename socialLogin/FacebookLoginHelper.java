package com.levo.android.Library.socialLogin;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;

import com.levo.android.Hasura;
import com.levo.android.Library.models.SocialLoginDetails;
import com.levo.android.db.tables.records.UserRecord;


import org.json.JSONObject;
import android.util.Log;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import io.hasura.auth.AuthException;
import io.hasura.auth.SocialLoginRequest;
import io.hasura.auth.SocialLoginResponse;
import io.hasura.core.Callback;
import io.hasura.db.DBException;
import io.hasura.db.InsertQuery;

import static com.levo.android.db.Tables.*;


/**
 * Created by Jaison on 13/03/16.
 */
public class FacebookLoginHelper {

    private CallbackManager callbackManager;
    private Activity activity;
    private SocialLoginHelperListener socialLoginHelperListener;

    private static String READ_PARAMETER_KEY_DEFAULT = "fields";
    private static String READ_PARAMETER_VALUE_DEFAULT = "id,name,email,gender,birthday";

    public FacebookLoginHelper(Activity activity,SocialLoginHelperListener socialLoginHelperListener) {
        this.activity = activity;
        this.socialLoginHelperListener = socialLoginHelperListener;
        callbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().registerCallback(callbackManager, facebookLoginCallbackListener);
    }

    public FacebookLoginHelper login() {
        LoginManager.getInstance().logInWithReadPermissions(activity, getReadPermission());
        return this;
    }

    private List<String> getReadPermission() {
        return Arrays.asList("public_profile", "email", "user_friends");
    }

    private FacebookCallback<LoginResult> facebookLoginCallbackListener = new FacebookCallback<LoginResult>() {
        @Override
        public void onSuccess(LoginResult loginResult) {
            // App code
            final String token = loginResult.getAccessToken().getToken();
            GraphRequest request = GraphRequest.newMeRequest(
                    loginResult.getAccessToken(),
                    new GraphRequest.GraphJSONObjectCallback() {
                        @Override
                        public void onCompleted(JSONObject object, GraphResponse response) {
                            getFacebookDetails(object, token);
                        }
                    });
            Bundle parameters = new Bundle();
            parameters.putString(getReadParameterStringKey(), getReadParameterStringValue());
            request.setParameters(parameters);
            request.executeAsync();
        }

        @Override
        public void onCancel() {
            socialLoginHelperListener.onSocialLoginCancelled();
        }

        @Override
        public void onError(FacebookException exception) {
            // TODO: HANDLE THIS
            System.out.println("Error: " + exception.getMessage());
            socialLoginHelperListener.onSocialLoginFailed();
        }
    };

    private String getReadParameterStringKey() {
        return READ_PARAMETER_KEY_DEFAULT;
    }

    private String getReadParameterStringValue() {
        return READ_PARAMETER_VALUE_DEFAULT;
    }

    private void getFacebookDetails(JSONObject object, String token) {
        SocialLoginDetails loginDetails = new SocialLoginDetails();
        loginDetails.setLoginType("facebook");
        loginDetails.setEmail(object.optString("email", ""));
        loginDetails.setFirstname(object.optString("name", ""));
        loginDetails.setUid(object.optString("id", ""));
        loginDetails.setAccessToken(token);

        socialLoginHelperListener.onSocialLoginSuccessful(loginDetails);
    }

    public void onActivityResult(int requestCode, int responseCode,
                                 Intent intent) {
        callbackManager.onActivityResult(requestCode, responseCode, intent);
    }
}
