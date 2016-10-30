package com.codepath.apps.restclienttemplate.activities;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.codepath.apps.restclienttemplate.R;
import com.codepath.apps.restclienttemplate.TwitterApplication;
import com.codepath.apps.restclienttemplate.TwitterClient;
import com.codepath.apps.restclienttemplate.adapters.TweetsArrayAdapter;
import com.codepath.apps.restclienttemplate.databinding.ActivityTimelineBinding;
import com.codepath.apps.restclienttemplate.models.Tweet;
import com.codepath.apps.restclienttemplate.utils.AppConstants;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

import static com.codepath.apps.restclienttemplate.utils.AppConstants.COMPOSE_TWEET_RESULT_CODE;

public class TimelineActivity extends AppCompatActivity {

    private ActivityTimelineBinding binding;

    private TwitterClient client;
    private ArrayList<Tweet> tweets;
    private TweetsArrayAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_timeline);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_timeline);

        // find the ListView
        //lvTweets = (ListView) findViewById(R.id.lvTweets);

        // Create the arrayList
        tweets = new ArrayList<>();

        // Connect adapter to ListView
        //lvTweets.setAdapter(mAdapter);

        setUpRecyclerView();

        setUpFab();

        client = TwitterApplication.getRestClient();
        populateTimeline();
    }

    private void setUpFab() {

        binding.fabNewTweet.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                Intent intent = new Intent(TimelineActivity.this, ComposeTweetActivity.class);
                startActivityForResult(intent, COMPOSE_TWEET_RESULT_CODE);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        // REQUEST_CODE is defined above
        if (resultCode == RESULT_OK && requestCode == AppConstants.COMPOSE_TWEET_RESULT_CODE) {

            Tweet tweet = data.getParcelableExtra(AppConstants.NEW_TWEET);
            mAdapter.addNewTweet(tweet);
            mAdapter.notifyDataSetChanged();
        }
    }

    private void setUpRecyclerView() {

        // Construct the adapter from the data source
        mAdapter = new TweetsArrayAdapter(this, tweets);
        binding.rvTweets.setAdapter(mAdapter);
        binding.rvTweets.setLayoutManager(new LinearLayoutManager(this));
    }

    // Send an API request to get eth timeline json
    // Fill the ListView by creating the tweet objects from the json
    private void populateTimeline() {

        client.getHomeTimeline(new JsonHttpResponseHandler() {
            // SUCCESS

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray json) {
                super.onSuccess(statusCode, headers, json);
                Log.d("DEBUG", json.toString());

                // JSON here
                // Deserialize JSON
                // Create models and add them to the adapter
                // Load the model data into ListView


                mAdapter.setTweets(Tweet.fromJSONArray(json));
                mAdapter.notifyDataSetChanged();

                Log.d("DEBUG", mAdapter.toString());
            }

            // FAILURE
            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable,
                                  JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                Log.d("DEBUG", errorResponse.toString());
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

}
