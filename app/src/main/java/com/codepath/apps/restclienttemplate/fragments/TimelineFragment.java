package com.codepath.apps.restclienttemplate.fragments;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.codepath.apps.restclienttemplate.R;
import com.codepath.apps.restclienttemplate.TwitterApplication;
import com.codepath.apps.restclienttemplate.TwitterClient;
import com.codepath.apps.restclienttemplate.adapters.TweetsArrayAdapter;
import com.codepath.apps.restclienttemplate.databinding.FragmentTimelineBinding;
import com.codepath.apps.restclienttemplate.listeners.EndlessRecyclerViewScrollListener;
import com.codepath.apps.restclienttemplate.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

/**
 * Created on 11/1/16.
 */

public class TimelineFragment extends Fragment {

    private FragmentTimelineBinding binding;

    private TwitterClient client;
    private TweetsArrayAdapter mAdapter;
    private ArrayList<Tweet> mTweets;
    private long mMaxId;
    private String mTimelineType;
    private long mUserId;
    private String mScreenName;

    public static TimelineFragment newInstance() {
        return new TimelineFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {

        Log.d("LIFECYCLE", "TimelineFragment.onCreate");
        super.onCreate(savedInstanceState);

        // Create the arrayList
        mTweets = new ArrayList<>();

        client = TwitterApplication.getRestClient();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        Log.d("LIFECYCLE", "TimelineFragment.onCreateView");
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_timeline, container, false);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        Log.d("LIFECYCLE", "TimelineFragment.onViewCreated");
        super.onViewCreated(view, savedInstanceState);

        setUpRecyclerView();

        populateTimeline(-1);
    }

    private void setUpRecyclerView() {

        // Construct the adapter from the data source
        mAdapter = new TweetsArrayAdapter(getContext(), mTweets);
        binding.rvTweets.setAdapter(mAdapter);
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        binding.rvTweets.setLayoutManager(linearLayoutManager);

        // IMPLEMENT Endless pagination:
        //  Pass in instance of EndlessRecyclerViewScrollListener and implement onLoadMore which
        //  fires whenever a new page needs to be loaded to fill up the list
        EndlessRecyclerViewScrollListener scrollListener =
                new EndlessRecyclerViewScrollListener(linearLayoutManager) {

                    @Override
                    public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                        view.post(new Runnable() {
                            @Override
                            public void run() {
                                populateTimeline(mMaxId);
                            }
                        });
                    }
                };
        binding.rvTweets.addOnScrollListener(scrollListener);
    }


    // Send an API request to get eth timeline json
    // Fill the ListView by creating the tweet objects from the json
    private void populateTimeline(long maxId) {

        switch (mTimelineType) {
            case "home":
                client.getHomeTimeline(new JsonHttpResponseHandler() {
                    // SUCCESS

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONArray json) {
                        super.onSuccess(statusCode, headers, json);
                        addTweetsToAdapter(json);
                    }

                    // FAILURE
                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable,
                                          JSONObject errorResponse) {
                        super.onFailure(statusCode, headers, throwable, errorResponse);
                        Log.d("DEBUG", errorResponse.toString());
                    }
                }, maxId);
                break;
            case "mentions":

                client.getMentionsTimeline(new JsonHttpResponseHandler() {
                    // SUCCESS

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONArray json) {
                        super.onSuccess(statusCode, headers, json);
                        addTweetsToAdapter(json);
                    }

                    // FAILURE
                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable,
                                          JSONObject errorResponse) {
                        super.onFailure(statusCode, headers, throwable, errorResponse);
                        Log.d("DEBUG", errorResponse.toString());
                    }
                }, maxId);
                break;
            default:
                client.getUserTimeline(new JsonHttpResponseHandler() {
                    // SUCCESS

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONArray json) {
                        super.onSuccess(statusCode, headers, json);
                        addTweetsToAdapter(json);
                    }

                    // FAILURE
                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable,
                                          JSONObject errorResponse) {
                        super.onFailure(statusCode, headers, throwable, errorResponse);
                        Log.d("DEBUG", errorResponse.toString());
                    }
                }, maxId, mUserId, mScreenName);
                break;
        }



    }

    private void addTweetsToAdapter(JSONArray json) {
        Log.d("DEBUG", json.toString());

        // JSON here
        // Deserialize JSON
        // Create models and add them to the adapter
        // Load the model data into ListView

        ArrayList<Tweet> tweets = Tweet.fromJSONArray(json);

        // In the case that there are tweets existing, then we are returning results using
        // maxId, therefore first tweet will be a duplicate of last tweet in mTweets, hence
        // removal of last tweet
        if (mAdapter.getItemCount() > 0) {
            tweets.remove(0);
        }

        mAdapter.addTweets(tweets);
        mAdapter.notifyDataSetChanged();

        // set mMaxId to use for endless pagination
        if (tweets.size() > 1) {
            mMaxId = tweets.get(tweets.size() - 1).getUid();
        }

        Log.d("DEBUG", mAdapter.toString());
    }


    public void addNewTweetToTimeline(Tweet newTweet) {

        mAdapter.addNewTweet(newTweet);
        mAdapter.notifyDataSetChanged();
    }

    public void setTimelineType(String timelineType) {
        mTimelineType = timelineType;
    }

    public void setTimelineType(String timelineType, long userId, String screenName) {
        mTimelineType = timelineType;
        mUserId = userId;
        mScreenName = screenName;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.d("LIFECYCLE", "TimelineFragment.onAttach");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.d("LIFECYCLE", "TimelineFragment.onDestroyView");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("LIFECYCLE", "TimelineFragment.onDestroy");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.d("LIFECYCLE", "TimelineFragment.onDetach");
    }
}
