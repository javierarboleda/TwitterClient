package com.codepath.apps.restclienttemplate.adapters;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.codepath.apps.restclienttemplate.R;
import com.codepath.apps.restclienttemplate.databinding.ItemTweetBinding;
import com.codepath.apps.restclienttemplate.models.Tweet;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import static com.raizlabs.android.dbflow.config.FlowManager.getContext;

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

    //@Override
//    public View getView(int position, View convertView, ViewGroup parent) {
//        // 1. Get the tweet
//        Tweet tweet = getItem(position);
//        // 2. Find or inflate the template
//        if (convertView == null) {
//            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_tweet, parent, false);
//        }
//        // 3. Find the subviews to fill with data in the template
//        ImageView ivProfileImage = (ImageView) convertView.findViewById(R.id.ivProfilePic);
//        TextView tvUserName = (TextView) convertView.findViewById(R.id.tvUserName);
//        TextView tvBody = (TextView) convertView.findViewById(R.id.tvBody);
//        // 4. Populate data into the subviews
//        tvUserName.setText(tweet.getUser().getScreenName());
//        tvBody.setText(tweet.getBody());
//        ivProfileImage.setImageResource(android.R.color.transparent); // clear out old image
//        Picasso.with(getContext()).load(tweet.getUser().getProfileImageUrl()).into(ivProfileImage);
//        // 5. Return the view to be inserted into the list
//        return convertView;
//    }

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
        Tweet tweet = mTweets.get(position);
        
        holder.binding.tvBody.setText(tweet.getBody());
        holder.binding.tvUserName.setText(tweet.getUser().getName());
        String screenName = "@" + tweet.getUser().getScreenName();
        holder.binding.tvScreenName.setText(screenName);
        holder.binding.tvRelativeTime.setText(tweet.getRelativeTimeAgo());

        Picasso.with(getContext())
                .load(tweet.getUser().getProfileImageUrl())
                .into(holder.binding.ivProfilePic);
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
