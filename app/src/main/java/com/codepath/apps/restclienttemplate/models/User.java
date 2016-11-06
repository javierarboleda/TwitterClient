package com.codepath.apps.restclienttemplate.models;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created on 10/27/16.
 */

@org.parceler.Parcel
public class User{
    // list attributes
    String name;
    long uid;
    String screenName;
    String profileImageUrl;
    String profileBackgroundImageUrl;
    long followingCount;
    long followersCount;
    String description;

    public User() {

    }

    // deserialize the user JSON => User
    public static User fromJSON(JSONObject jsonObject) {

        User user = new User();

        try {
            user.name = jsonObject.getString("name");
            user.uid = jsonObject.getLong("id");
            user.screenName = jsonObject.getString("screen_name");
            user.profileImageUrl = jsonObject.getString("profile_image_url");
            user.followingCount = jsonObject.getLong("friends_count");
            user.followersCount = jsonObject.getLong("followers_count");
            user.description = jsonObject.getString("description");
            user.profileBackgroundImageUrl = jsonObject.getString("profile_banner_url");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return user;
    }

    public String getName() {
        return name;
    }

    public long getUid() {
        return uid;
    }

    public String getScreenName() {
        return screenName;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public String getProfileBackgroundImageUrl() {
        return profileBackgroundImageUrl;
    }

    public long getFollowingCount() {
        return followingCount;
    }

    public long getFollowersCount() {
        return followersCount;
    }

    public String getDescription() {
        return description;
    }
    
}
