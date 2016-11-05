package com.codepath.apps.restclienttemplate.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.View;

import com.codepath.apps.restclienttemplate.R;
import com.codepath.apps.restclienttemplate.TwitterApplication;
import com.codepath.apps.restclienttemplate.TwitterClient;
import com.codepath.apps.restclienttemplate.models.User;
import com.codepath.apps.restclienttemplate.utils.AppConstants;
import com.codepath.oauth.OAuthLoginActionBarActivity;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class LoginActivity extends OAuthLoginActionBarActivity<TwitterClient> {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
	}


	// Inflate the menu; this adds items to the action bar if it is present.
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.login, menu);
		return true;
	}

	// OAuth authenticated successfully, launch primary authenticated activity
	// i.e Display application "homepage"
	@Override
	public void onLoginSuccess() {

        setUpUserCredentials();

        Intent i = new Intent(this, TimelineActivity.class);
        startActivity(i);
	}

    private void setUpUserCredentials() {
        final SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        String imageUrl = sharedPref.getString(AppConstants.USER_PROFILE_IMAGE_URL_KEY, null);
        String screenName = sharedPref.getString(AppConstants.USER_PROFILE_SCREEN_NAME_KEY, null);
        if (imageUrl == null || screenName == null) {
            TwitterApplication.getRestClient().getUserCredentials(new JsonHttpResponseHandler() {

                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    super.onSuccess(statusCode, headers, response);
                    Log.d("DEBUG", response.toString());

                    User user = User.fromJSON(response);
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putString(AppConstants.USER_PROFILE_IMAGE_URL_KEY,
                            user.getProfileImageUrl());
                    editor.putString(AppConstants.USER_PROFILE_SCREEN_NAME_KEY,
                            user.getScreenName());
                    editor.putLong(AppConstants.USER_PROFILE_UID_KEY,
                            user.getUid());
                    editor.apply();
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable,
                                      JSONObject errorResponse) {
                    super.onFailure(statusCode, headers, throwable, errorResponse);
                    Log.d("DEBUG", errorResponse.toString());
                }

            });
        }
    }

    // OAuth authentication flow failed, handle the error
	// i.e Display an error dialog or toast
	@Override
	public void onLoginFailure(Exception e) {
		e.printStackTrace();
	}

	// Click handler method for the button used to start OAuth flow
	// Uses the client to initiate OAuth authorization
	// This should be tied to a button used to login
	public void loginToRest(View view) {
		getClient().connect();
	}

}
