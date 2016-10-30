package com.codepath.apps.restclienttemplate.models;

import android.os.Parcel;
import android.os.Parcelable;
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
public class Tweet implements Parcelable {
    // list out the attributes
    private String body;
    private long uid; // unique id for the tweet
    private User user;
    private String createdAt;

    private Tweet(Parcel in) {
        body = in.readString();
        uid = in.readLong();
        user = in.readParcelable(User.class.getClassLoader());
        createdAt = in.readString();
    }

    @Override
    public void writeToParcel(Parcel out, int i) {
        out.writeString(body);
        out.writeLong(uid);
        out.writeParcelable(user, i);
        out.writeString(createdAt);
    }

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

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Parcelable.Creator<Tweet> CREATOR
            = new Parcelable.Creator<Tweet>() {

        // This simply calls our new constructor (typically private) and
        // passes along the unmarshalled `Parcel`, and then returns the new object!
        @Override
        public Tweet createFromParcel(Parcel in) {
            return new Tweet(in);
        }

        // We just need to copy this and change the type to match our class.
        @Override
        public Tweet[] newArray(int size) {
            return new Tweet[size];
        }
    };

}
