package com.levo.android.Library.socialLogin;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.levo.android.Library.models.SocialLoginDetails;

/**
 * Created by Jaison on 13/03/16.
 */
public class GooglePlusLoginHelper {

    private AppCompatActivity activity;
    private GoogleApiClient mGoogleApiClient;
    private SocialLoginHelperListener socialLoginHelperListener;
    private static final int RC_SIGN_IN = 9001;

    public GooglePlusLoginHelper(AppCompatActivity activity,SocialLoginHelperListener socialLoginHelperListener) {
        this.activity = activity;
        this.socialLoginHelperListener = socialLoginHelperListener;
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestIdToken("266467904528-ocrsqbgibg3vrs850sf6jc0k7u9qk5u9.apps.googleusercontent.com")
                .build();
        mGoogleApiClient = new GoogleApiClient.Builder(activity)
                .enableAutoManage(activity /* FragmentActivity */, onConnectionFailedListener /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
    }

    public GooglePlusLoginHelper login() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        activity.startActivityForResult(signInIntent, RC_SIGN_IN);
        return this;
    }

    private GoogleApiClient.OnConnectionFailedListener onConnectionFailedListener = new GoogleApiClient.OnConnectionFailedListener() {
        @Override
        public void onConnectionFailed(ConnectionResult connectionResult) {
            //TODO: HANDLE THIS
        }
    };


    public void onActivityResult(int requestCode, int responseCode, Intent intent) {
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(intent);
            if (result.isSuccess()) {
                handleSignInResult(result);
            }
            else {
                // TODO: This is getting executed. The sign-in is not successful.
                Log.d("{{HASURA::AUTH}}", result.toString());
                Log.d("{{HASURA::AUTH}}", result.getStatus().toString());
                Log.d("{{HASURA::AUTH}}", result.getStatus().getStatusMessage());
            }
        }
    }

    private void handleSignInResult(GoogleSignInResult result) {
        if (result.isSuccess()) {
            GoogleSignInAccount acct = result.getSignInAccount();
            SocialLoginDetails loginDetails = new SocialLoginDetails();
            loginDetails.setLoginType("google");
            loginDetails.setUid(acct.getId());
            loginDetails.setEmail(acct.getEmail());
            loginDetails.setFirstname(acct.getDisplayName());
            loginDetails.setAccessToken(acct.getIdToken());
            socialLoginHelperListener.onSocialLoginSuccessful(loginDetails);
        } else {
            // TODO: HANDLE THIS
            // Signed out, show unauthenticated UI.
            System.out.println("RESULT ERROR MESSAGE:" + result.getStatus());
            socialLoginHelperListener.onSocialLoginFailed();
        }
    }
}
