package com.codepath.apps.restclienttemplate.activities;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.codepath.apps.restclienttemplate.R;
import com.codepath.apps.restclienttemplate.databinding.ActivityUserProfileBinding;
import com.codepath.apps.restclienttemplate.fragments.TimelineFragment;
import com.codepath.apps.restclienttemplate.models.User;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

public class UserProfileActivity extends AppCompatActivity {

    private ActivityUserProfileBinding binding;
    private User mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_user_profile);

        mUser = getIntent().getParcelableExtra("user");

        setUpToolbar();

        setUpFragment();
    }

    private void setUpFragment() {

        // Begin the transaction
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        // Replace the contents of the container with the new fragment

        TimelineFragment fragment = TimelineFragment.newInstance();
        fragment.setTimelineType("user", mUser.getUid(), mUser.getScreenName());

        ft.replace(R.id.fragmentPlaceholder, fragment);
        // or ft.add(R.id.your_placeholder, new FooFragment());
        // Complete the changes added above
        ft.commit();
    }

    private void setUpToolbar() {

        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        String followingCount = mUser.getFollowingCount() + "";
        String followersCount = mUser.getFollowersCount() + "";

        Glide.with(this)
                .load(mUser.getProfileBackgroundImageUrl())
                .into(binding.ivBackgroundImage);

        Glide.with(this)
                .load(mUser.getProfileImageUrl().replace("_normal", ""))
                .bitmapTransform(new RoundedCornersTransformation(this, 30, 5))
                .into(binding.ivUserProfileImage);

        binding.tvDescription.setText(mUser.getDescription());

        binding.tvFollowingCount.setText(followingCount);
        binding.tvFollowersCount.setText(followersCount);

        binding.tvUserName.setText(mUser.getName());
        binding.tvScreenName.setText("@" + mUser.getScreenName());

        binding.tvUserNameMain.setText(mUser.getName());
        binding.tvScreenNameMain.setText("@" + mUser.getScreenName());

        // code borrowed from http://stackoverflow.com/a/31602120/3850772
        //
        // This allows the toolbar alpha to animate in and out on toolbar collapse
        final CollapsingToolbarLayout collapsingToolbar = binding.collapsingToolbar;
        AppBarLayout.OnOffsetChangedListener listener = new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if(collapsingToolbar.getHeight() +
                        verticalOffset < 2 * ViewCompat.getMinimumHeight(collapsingToolbar)) {
                    binding.toolbar.animate().alpha(1).setDuration(300);
                } else {
                    binding.toolbar.animate().alpha(0).setDuration(300);
                }
            }
        };
        binding.appBar.addOnOffsetChangedListener(listener);
    }
}
