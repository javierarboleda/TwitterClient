package com.codepath.apps.restclienttemplate.adapters;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.codepath.apps.restclienttemplate.R;
import com.codepath.apps.restclienttemplate.activities.UserProfileActivity;
import com.codepath.apps.restclienttemplate.databinding.ItemTweetBinding;
import com.codepath.apps.restclienttemplate.models.Tweet;

import java.util.ArrayList;
import java.util.List;

/**
 * Created on 10/27/16.
 */

// Taking the Tweet objects and turning them into Views displayed in the list
public class TweetsArrayAdapter extends RecyclerView.Adapter<TweetsArrayAdapter.ViewHolder> {

    List<Tweet> mTweets;
    Context mContext;

    public TweetsArrayAdapter(Context context, List<Tweet> tweets) {
        mContext = context;
        mTweets = tweets;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        ItemTweetBinding binding =
                DataBindingUtil.inflate(
                    LayoutInflater.from(parent.getContext()),
                    R.layout.item_tweet,
                    parent,
                    false
                );

        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Tweet tweet = mTweets.get(position);
        
        holder.binding.tvBody.setText(tweet.getBody());
        holder.binding.tvUserName.setText(tweet.getUser().getName());
        String screenName = "@" + tweet.getUser().getScreenName();
        holder.binding.tvScreenName.setText(screenName);
        holder.binding.tvRelativeTime.setText(tweet.getRelativeTimeAgo());

        String imageUrl = tweet.getUser().getProfileImageUrl().replace("_normal", "");

        Glide.with(mContext)
                .load(imageUrl)
                .into(holder.binding.ivProfilePic);

        holder.binding.ivProfilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, UserProfileActivity.class);
                intent.putExtra("user", tweet.getUser());
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mTweets.size();
    }

    public void addTweets(ArrayList<Tweet> tweets) {
        mTweets.addAll(tweets);
    }

    public void addNewTweet(Tweet tweet) {
        mTweets.add(0, tweet);
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        ItemTweetBinding binding;

        ViewHolder(ItemTweetBinding itemTweetBinding) {
            super(itemTweetBinding.getRoot());
            binding = itemTweetBinding;
        }

    }
}
