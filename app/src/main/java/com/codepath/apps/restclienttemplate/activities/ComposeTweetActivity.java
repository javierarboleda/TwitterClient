package com.codepath.apps.restclienttemplate.activities;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
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
import org.parceler.Parcels;

import cz.msebera.android.httpclient.Header;

public class ComposeTweetActivity extends AppCompatActivity {

    private ActivityComposeTweetBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_compose_tweet);

//        binding.tvCharsLeft.setText("100");

        binding.etTweetInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                int charsLeft = 140 - editable.length();
                String charsLeftString = "" + charsLeft;
                binding.tvCharsLeft.setText(charsLeftString);

                if (charsLeft < 0) {
                    binding.tvCharsLeft.setTextColor(getResources().getColor(R.color.redText));
                    binding.btnTweet.setEnabled(false);
                    binding.btnTweet.setBackground(getResources()
                            .getDrawable(R.drawable.shape_round_button_disabled));
                } else {
                    binding.tvCharsLeft.setTextColor(getResources().getColor(R.color.blackText));
                    binding.btnTweet.setEnabled(true);
                    binding.btnTweet.setBackground(getResources()
                            .getDrawable(R.drawable.shape_round_button));
                }
            }
        });
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
                data.putExtra(AppConstants.NEW_TWEET, Parcels.wrap(newTweet));

                setResult(RESULT_OK, data);
                finish();
            }

            // FAILURE
            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable,
                                  JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                if (errorResponse != null) {
                    Log.d("DEBUG", errorResponse.toString());
                }
            }
        }, tweetString);
    }
}
