package com.codepath.apps.restclienttemplate.models;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created on 10/27/16.
 */

public class User implements Parcelable{
    // list attributes
    private String name;
    private long uid;
    private String screenName;
    private String profileImageUrl;

    private User(Parcel in) {
        name = in.readString();
        uid = in.readLong();
        screenName = in.readString();
        profileImageUrl = in.readString();
    }

    @Override
    public void writeToParcel(Parcel out, int i) {
        out.writeString(name);
        out.writeLong(uid);
        out.writeString(screenName);
        out.writeString(profileImageUrl);
    }

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

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Parcelable.Creator<User> CREATOR
            = new Parcelable.Creator<User>() {

        // This simply calls our new constructor (typically private) and
        // passes along the unmarshalled `Parcel`, and then returns the new object!
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        // We just need to copy this and change the type to match our class.
        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };
}
