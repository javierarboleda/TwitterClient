package com.codepath.apps.restclienttemplate.activities;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.codepath.apps.restclienttemplate.R;
import com.codepath.apps.restclienttemplate.TwitterApplication;
import com.codepath.apps.restclienttemplate.TwitterClient;
import com.codepath.apps.restclienttemplate.databinding.ActivityComposeTweetBinding;
import com.codepath.apps.restclienttemplate.models.Tweet;
import com.codepath.apps.restclienttemplate.utils.AppConstants;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class ComposeTweetActivity extends AppCompatActivity {

    private ActivityComposeTweetBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_compose_tweet);
    }

    public void sendTweet(View view) {

        String tweetString = binding.etTweetInput.getText().toString();

        TwitterClient client = TwitterApplication.getRestClient();
        client.postStatusUpdate(new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject json) {
                super.onSuccess(statusCode, headers, json);
                Log.d("DEBUG", json.toString());

                Tweet newTweet = Tweet.fromJSON(json);

                Intent data = new Intent();
                data.putExtra(AppConstants.NEW_TWEET, newTweet);

                setResult(RESULT_OK, data);
                finish();
            }

            // FAILURE
            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable,
                                  JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                Log.d("DEBUG", errorResponse.toString());
            }
        }, tweetString);
    }
}
