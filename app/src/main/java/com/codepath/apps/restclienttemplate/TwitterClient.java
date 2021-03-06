package com.codepath.apps.restclienttemplate;

import android.content.Context;

import com.codepath.oauth.OAuthBaseClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.scribe.builder.api.Api;
import org.scribe.builder.api.TwitterApi;

/*
 * 
 * This is the object responsible for communicating with a REST API. 
 * Specify the constants below to change the API being communicated with.
 * See a full list of supported API classes: 
 *   https://github.com/fernandezpablo85/scribe-java/tree/master/src/main/java/org/scribe/builder/api
 * Key and Secret are provided by the developer site for the given API i.e dev.twitter.com
 * Add methods for each relevant endpoint in the API.
 * 
 * NOTE: You may want to rename this object based on the service i.e TwitterClient or FlickrClient
 * 
 */
public class TwitterClient extends OAuthBaseClient {
	public static final Class<? extends Api> REST_API_CLASS = TwitterApi.class; // Change this
	public static final String REST_URL = "https://api.twitter.com/1.1"; // Change this, base API URL
	public static final String REST_CONSUMER_KEY = "psOM85ctl6LNKWlhxWVKi1O3l"; // Change this
	public static final String REST_CONSUMER_SECRET = "br13lo9U3e66a3EY8oOpSZG4NIOlOvzDkUFTxn5S6lUwSTK1Xw"; // Change this
	public static final String REST_CALLBACK_URL = "oauth://cpsimpletweets"; // Change this (here and in manifest)

	public TwitterClient(Context context) {
		super(context, REST_API_CLASS, REST_URL, REST_CONSUMER_KEY, REST_CONSUMER_SECRET, REST_CALLBACK_URL);
	}

	// Method = endpoint

	// HomeTimeLine endpoint
    // GET: statuses/home_timeline.json
    //    count=25
    //    since_id=1
    public void getHomeTimeline(AsyncHttpResponseHandler handler, long maxId) {

        String apiUrl = getApiUrl("statuses/home_timeline.json");

        // Specify the params
        RequestParams params = new RequestParams();
        params.put("count", 25);
        // show us tweets from the beginning
        params.put("since_id", 1);
        // return results with an ID less than (i.e., older than( or equal to given ID
        if (maxId != -1) {
            params.put("max_id", maxId);
        }

        // Execute the request
        getClient().get(apiUrl, params, handler);
    }

    public void getMentionsTimeline(AsyncHttpResponseHandler handler, long maxId) {

        String apiUrl = getApiUrl("statuses/mentions_timeline.json");

        // Specify the params
        RequestParams params = new RequestParams();
        params.put("count", 25);
        // show us tweets from the beginning
        params.put("since_id", 1);
        // return results with an ID less than (i.e., older than( or equal to given ID
        if (maxId != -1) {
            params.put("max_id", maxId);
        }

        // Execute the request
        getClient().get(apiUrl, params, handler);
    }

	public void getUserTimeline(AsyncHttpResponseHandler handler, long maxId, long userId,
                                String screenName) {

		String apiUrl = getApiUrl("statuses/user_timeline.json");

		// Specify the params
		RequestParams params = new RequestParams();
		params.put("count", 25);
        params.put("user_id", userId);
        params.put("screen_name", screenName);
        // show us tweets from the beginning
		params.put("since_id", 1);
		// return results with an ID less than (i.e., older than( or equal to given ID
		if (maxId != -1) {
			params.put("max_id", maxId);
		}

		// Execute the request
		getClient().get(apiUrl, params, handler);
	}

	public void getUserLookup(AsyncHttpResponseHandler handler, String screenName) {

		String apiUrl = getApiUrl("users/lookup.json");

		// Specify the params
		RequestParams params = new RequestParams();
		params.put("screen_name", screenName);
		// Execute the request
		getClient().get(apiUrl, params, handler);
	}

	// GET: account/verify_credentials.json
	//   get user credentials
	public void getUserCredentials(AsyncHttpResponseHandler handler) {
		String apiUrl = getApiUrl("account/verify_credentials.json");

		getClient().get(apiUrl, handler);
	}

	// COMPOSE TWEET endpoint
	public void postStatusUpdate(AsyncHttpResponseHandler handler, String tweetString) {

		String apiUrl = getApiUrl("statuses/update.json");

		// Specify the params
		RequestParams params = new RequestParams();
		params.put("status", tweetString);

		// Execute the POST request
		getClient().post(apiUrl, params, handler);
	}

	/* 1. Define the endpoint URL with getApiUrl and pass a relative path to the endpoint
	 * 	  i.e getApiUrl("statuses/home_timeline.json");
	 * 2. Define the parameters to pass to the request (query or body)
	 *    i.e RequestParams params = new RequestParams("foo", "bar");
	 * 3. Define the request method and make a call to the client
	 *    i.e client.get(apiUrl, params, handler);
	 *    i.e client.post(apiUrl, params, handler);
	 */
}