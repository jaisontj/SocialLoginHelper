package com.levo.android.Library.socialLogin;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
/**
 * Created by Jaison on 13/03/16.
 *
 * USAGE :
 * 1.Initialize with a
 */
public class SocialLoginManager {

    public enum SocialLoginType {
        FACEBOOK, GOOGLE_PLUS, LINKED_IN
    }

    private AppCompatActivity activity;
    private SocialLoginType socialLoginType;
    private FacebookLoginHelper facebookLoginHelper;
    private GooglePlusLoginHelper googlePlusLoginHelper;
    private LinkedInLoginHelper linkedInLoginHelper;
    private SocialLoginHelperListener socialLoginHelperListener;

    public SocialLoginManager(AppCompatActivity activity,SocialLoginHelperListener socialLoginHelperListener) {
        this.activity = activity;
        this.socialLoginHelperListener = socialLoginHelperListener;
    }

    public void login(SocialLoginType socialLoginType) {
        this.socialLoginType = socialLoginType;
        switch (socialLoginType) {
            case FACEBOOK:
                facebookLoginHelper = new FacebookLoginHelper(activity,socialLoginHelperListener).login();
                break;
            case GOOGLE_PLUS:
                googlePlusLoginHelper = new GooglePlusLoginHelper(activity,socialLoginHelperListener).login();
                break;
            case LINKED_IN:
                linkedInLoginHelper = new LinkedInLoginHelper(activity,socialLoginHelperListener).login();
                break;
        }
    }

    public void handleActivityResult(int requestCode, int responseCode, Intent intent) {
        switch (socialLoginType) {
            case FACEBOOK:
                facebookLoginHelper.onActivityResult(requestCode,responseCode,intent);
                break;
            case GOOGLE_PLUS:
                googlePlusLoginHelper.onActivityResult(requestCode,responseCode,intent);
                break;
            case LINKED_IN:
                linkedInLoginHelper.onActivityResult(requestCode, responseCode, intent);
                break;
        }
    }
}
