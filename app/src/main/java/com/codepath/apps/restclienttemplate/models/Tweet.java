package com.codepath.apps.restclienttemplate.models;

import android.text.format.DateUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

/**
 * Created on 10/27/16.
 */

// Parse the JSON + Store the data, encapsulate state logic or display logic
@org.parceler.Parcel
public class Tweet {
    // list out the attributes
    String body;
    long uid; // unique id for the tweet
    User user;
    String createdAt;

    public Tweet() {

    }

    // Deserialize the JSON and build Tweet objects
    // Tweet.fromJSON("{...}") => <Tweet>
    public static Tweet fromJSON(JSONObject jsonObject) {
        Tweet tweet = new Tweet();;

        try {
            tweet.body = jsonObject.getString("text");
            tweet.uid = jsonObject.getLong("id");
            tweet.createdAt = jsonObject.getString("created_at");
            tweet.user = User.fromJSON(jsonObject.getJSONObject("user"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return tweet;
    }

    // Tweet.fromJSONArray([ {...}, {...} ] => List<Tweet>
    public static ArrayList<Tweet> fromJSONArray(JSONArray jsonArray) {
        ArrayList<Tweet> tweets = new ArrayList<>();

        // Iterate the json array and create tweets
        for (int i = 0; i < jsonArray.length(); i++) {
            try{
                JSONObject tweetJson = jsonArray.getJSONObject(i);
                Tweet tweet = Tweet.fromJSON(tweetJson);
                if (tweet != null) {
                    tweets.add(tweet);
                }
            } catch (JSONException e) {
                e.printStackTrace();
                continue;
            }
        }

        return tweets;
    }

    // getRelativeTimeAgo("Mon Apr 01 21:16:23 +0000 2014");
    public String getRelativeTimeAgo() {
        String twitterFormat = "EEE MMM dd HH:mm:ss ZZZZZ yyyy";
        SimpleDateFormat sf = new SimpleDateFormat(twitterFormat, Locale.ENGLISH);
        sf.setLenient(true);

        String relativeDate = "";
        try {
            long dateMillis = sf.parse(createdAt).getTime();
            relativeDate = DateUtils.getRelativeTimeSpanString(dateMillis,
                    System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS).toString();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if (relativeDate.startsWith("In")) {
            relativeDate = "now";
        }

        return relativeDate
                .replace(" minutes ago", "m")
                .replace(" minute ago", "m")
                .replace(" hour ago", "h")
                .replace(" hours ago", "h")
                .replace(" seconds ago", "s")
                .replace(" second ago", "s")
                ;
    }

    public String getBody() {
        return body;
    }

    public long getUid() {
        return uid;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public User getUser() {
        return user;
    }

}
