package com.codepath.apps.restclienttemplate.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.bumptech.glide.Glide;
import com.codepath.apps.restclienttemplate.R;
import com.codepath.apps.restclienttemplate.TwitterApplication;
import com.codepath.apps.restclienttemplate.TwitterClient;
import com.codepath.apps.restclienttemplate.adapters.MainFragmentPagerAdapter;
import com.codepath.apps.restclienttemplate.adapters.TweetsArrayAdapter;
import com.codepath.apps.restclienttemplate.databinding.ActivityTimelineBinding;
import com.codepath.apps.restclienttemplate.fragments.TimelineFragment;
import com.codepath.apps.restclienttemplate.models.Tweet;
import com.codepath.apps.restclienttemplate.models.User;
import com.codepath.apps.restclienttemplate.utils.AppConstants;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;
import jp.wasabeef.glide.transformations.CropCircleTransformation;

import static com.codepath.apps.restclienttemplate.utils.AppConstants.COMPOSE_TWEET_RESULT_CODE;

public class TimelineActivity extends AppCompatActivity {

    private ActivityTimelineBinding binding;

    private TwitterClient client;
    private ArrayList<Tweet> mTweets;
    private TweetsArrayAdapter mAdapter;
    private long mMaxId;

    private MainFragmentPagerAdapter mPagerAdapter;
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_timeline);

        // Create the arrayList
        mTweets = new ArrayList<>();

        setUpToolbar();

        setUpFab();

        setUpTabLayout();
    }

    private void setUpTabLayout() {

        // Get the ViewPager and set it's PagerAdapter so that it can display items
        mViewPager = binding.viewPagerTimeline;

        mPagerAdapter =
                new MainFragmentPagerAdapter(getSupportFragmentManager(),TimelineActivity.this);

        mViewPager.setAdapter(mPagerAdapter);

        // Give the TabLayout the ViewPager
        TabLayout tabLayout = binding.tabLayoutTimeline;
        tabLayout.setupWithViewPager(mViewPager);
    }

    private void setUpToolbar() {
        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        String imageUrl = sharedPref.getString(AppConstants.USER_PROFILE_IMAGE_URL_KEY, null);

        if (imageUrl != null) {
            imageUrl = imageUrl.replace("_normal", "");
        }


        Glide.with(this)
                .load(imageUrl)
                .bitmapTransform(new CropCircleTransformation(this))
                .into(binding.ivToolbarUserImage);
    }

    public void startUserProfileActivity(View view) {

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        String screenName = sharedPref.getString(AppConstants.USER_PROFILE_SCREEN_NAME_KEY, null);

        TwitterApplication.getRestClient().getUserLookup(new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);

                User user = User.fromJSON(response);

                Intent intent = new Intent(TimelineActivity.this, UserProfileActivity.class);
                intent.putExtra("user", user);
                TimelineActivity.this.startActivity(intent);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                super.onSuccess(statusCode, headers, response);

                User user = null;
                try {
                    user = User.fromJSON(response.getJSONObject(0));
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                Intent intent = new Intent(TimelineActivity.this, UserProfileActivity.class);
                intent.putExtra("user", user);
                TimelineActivity.this.startActivity(intent);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable,
                                  JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                Log.d("DEBUG", errorResponse.toString());
            }
        }, screenName);

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

            TimelineFragment fragment = (TimelineFragment)
                    mPagerAdapter.getRegisteredFragment(mViewPager.getCurrentItem());

            fragment.addNewTweetToTimeline(tweet);
//
//            mAdapter.addNewTweet(tweet);
//            mAdapter.notifyDataSetChanged();
        }
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
