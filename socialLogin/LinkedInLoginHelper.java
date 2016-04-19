package com.levo.android.Library.socialLogin;

import android.app.Activity;
import android.content.Intent;
import com.linkedin.platform.APIHelper;
import com.linkedin.platform.LISessionManager;
import com.linkedin.platform.errors.LIApiError;
import com.linkedin.platform.errors.LIAuthError;
import com.linkedin.platform.listeners.ApiListener;
import com.linkedin.platform.listeners.ApiResponse;
import com.linkedin.platform.listeners.AuthListener;
import com.linkedin.platform.utils.Scope;

/**
 * Created by Jaison on 13/03/16.
 */
public class LinkedInLoginHelper {

    private Activity activity;
    private SocialLoginHelperListener socialLoginHelperListener;

    public LinkedInLoginHelper(Activity activity,SocialLoginHelperListener socialLoginHelperListener) {
        this.activity = activity;
        this.socialLoginHelperListener = socialLoginHelperListener;
    }

    public LinkedInLoginHelper login() {
        LISessionManager.getInstance(activity).init(activity, buildScope(), authListener, true);
        return this;
    }

    private AuthListener authListener = new AuthListener() {
        @Override
        public void onAuthSuccess() {
            String url = "https://api.linkedin.com/v1/people/~";
            APIHelper apiHelper = APIHelper.getInstance(activity);
            apiHelper.getRequest(activity, url, new ApiListener() {
                @Override
                public void onApiSuccess(ApiResponse apiResponse) {
                    //TODO
                }

                @Override
                public void onApiError(LIApiError liApiError) {

                }
            });
        }

        @Override
        public void onAuthError(LIAuthError error) {

        }
    };

    private static Scope buildScope() {
        return Scope.build(Scope.R_BASICPROFILE, Scope.W_SHARE);
    }

    public void onActivityResult(int requestCode, int responseCode, Intent intent) {
        LISessionManager.getInstance(activity).onActivityResult(activity, requestCode, responseCode, intent);
    }
}
