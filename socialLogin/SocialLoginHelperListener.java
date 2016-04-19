package com.levo.android.Library.socialLogin;

import com.levo.android.Library.models.SocialLoginDetails;

/**
 * Created by Jaison on 13/03/16.
 */
public interface SocialLoginHelperListener {

    void onSocialLoginSuccessful(SocialLoginDetails loginDetails);
    void onSocialLoginFailed();
    void onSocialLoginCancelled();
}
