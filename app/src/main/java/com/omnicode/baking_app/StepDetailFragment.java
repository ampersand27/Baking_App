package com.omnicode.baking_app;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.VideoView;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.omnicode.baking_app.data.StepObject;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import timber.log.Timber;

public class StepDetailFragment extends Fragment {
    Unbinder mBinder;
    @BindView(R.id.tv_instruct) TextView tvInstruct;
    @BindView(R.id.exo_player_view) SimpleExoPlayerView mExoPlayerView;
    @BindView(R.id.step_thumbnail_image) ImageView ivThumb;
    //@BindView(R.id.instruct_container) ScrollView mInstructContainer;

    private StepObject mStepObj;
    private int mClickedItem;

    private boolean mReady = true;
    private long mPlayerPosition = 0;

    private SimpleExoPlayer mExoPlayer;

    public StepDetailFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        Timber.d("XX OnCreate");
        super.onCreate(savedInstanceState);
        if (getArguments() != null && getArguments().containsKey(getString(R.string.BUNDLE_KEY_STEP_OBJ))) {
            mStepObj = getArguments().getParcelable(getString(R.string.BUNDLE_KEY_STEP_OBJ));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Timber.d("XX OnCreateView");
        View rootView = inflater.inflate(R.layout.fragment_step_detail, container, false);
        if (savedInstanceState != null && savedInstanceState.containsKey(getString(R.string.PLAYER_KEY_POSITION))) {
            mReady = savedInstanceState.getBoolean(getString(R.string.PLAYER_KEY_READY));
            mPlayerPosition = savedInstanceState.getLong(getString(R.string.PLAYER_KEY_POSITION));
        }

        bindViews(rootView);
        populateUI();
        // Show thumbnail if url exists
        if (!mStepObj.getThumbUrl().isEmpty()) {
            Picasso.with(getContext())
                    .load(mStepObj.getThumbUrl())
                    .placeholder(R.drawable.ic_launcher_background)
                    .into(ivThumb);
            ivThumb.setVisibility(View.VISIBLE);
        }
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!TextUtils.isEmpty(mStepObj.getVideoUrl()))
            initPlayer(Uri.parse(mStepObj.getVideoUrl()));
        else {
            //mInstructContainer.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        removePlayer();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mBinder.unbind();
        Timber.d("XX OnDestroyView");
    }

    private void populateUI() {
        // recipeDetailFragmentAdapter = new RecipeDetailFragmentAdapter(getActivity(), mStepObj, this);
        tvInstruct.setText(mStepObj.getDesc());
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putLong(getString(R.string.PLAYER_KEY_POSITION), mPlayerPosition);
        outState.putBoolean(getString(R.string.PLAYER_KEY_READY), mReady);
    }

    private void bindViews(View rootView) {
        mBinder = ButterKnife.bind(this, rootView);
    }

    private void initPlayer(Uri mediaUri) {
        if (mExoPlayer == null) {
            // Create a default TrackSelector
            DefaultBandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
            TrackSelection.Factory videoTrackSelectionFactory = new AdaptiveTrackSelection.Factory(bandwidthMeter);
            TrackSelector trackSelector = new DefaultTrackSelector(videoTrackSelectionFactory);

            // Create the player
            mExoPlayer = ExoPlayerFactory.newSimpleInstance(getContext(), trackSelector);
            // Bind player to view.
            mExoPlayerView.setPlayer(mExoPlayer);
            // Measures bandwidth during playback. Can be null if not required.
            // Produces DataSource instances through which media data is loaded.
            DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(getContext(), Util.getUserAgent(getContext(), getString(R.string.app_name)), bandwidthMeter);
            // This is the MediaSource representing the media to be played.
            MediaSource videoSource = new ExtractorMediaSource.Factory(dataSourceFactory).createMediaSource(mediaUri);
            // Prepare the player with the source.
            mExoPlayer.prepare(videoSource);

            // onRestore
            if (mPlayerPosition != 0)
                mExoPlayer.seekTo(mPlayerPosition);

            mExoPlayer.setPlayWhenReady(mReady);
            mExoPlayerView.setVisibility(View.VISIBLE);
        }
    }

    private void removePlayer() {
        if (mExoPlayer != null) {
            mReady = mExoPlayer.getPlayWhenReady();
            mPlayerPosition = mExoPlayer.getCurrentPosition();

            mExoPlayer.stop();
            mExoPlayer.release();
            mExoPlayer = null;
        }
    }

}
