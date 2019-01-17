package com.omnicode.baking_app;

import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.omnicode.baking_app.data.RecipeObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

//StepDetail activity is only used, when app is in one-pane mode
public class StepDetail extends AppCompatActivity {
    @BindView(R.id.step_detail_tab_layout) TabLayout mTabStepDetail;
    @BindView(R.id.step_detail_viewpager) ViewPager mPagerStepDetail;
    private RecipeObject mRecipeObj;
    private int mClickedStep;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.step_detail);

        bindViews();
        Timber.d("XX onCreate");


        Bundle extras = getIntent().getExtras();
        if (extras != null && extras.containsKey(getString(R.string.BUNDLE_KEY_RECIPE_OBJ)) && extras.containsKey(getString(R.string.BUNDLE_KEY_CLICKED_STEP))) {
            Timber.d("XX Bundle found ");
            mRecipeObj = extras.getParcelable(getString(R.string.BUNDLE_KEY_RECIPE_OBJ));
            mClickedStep = extras.getInt(getString(R.string.BUNDLE_KEY_CLICKED_STEP));
            if (mClickedStep < 0) mClickedStep = 0;
        } else {
            Timber.d("XX Bundle empty");
            return;
        }

        /*Toolbar toolbar = findViewById(R.id.detail_toolbar);
        setSupportActionBar(toolbar);*/
        final ActionBar actionBar = this.getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(mRecipeObj.getRecipeName());
        }

        /*if (savedInstanceState == null) {
            openFragment(mRecipeObj, mClickedStep);
        }*/
        Timber.d("XX Begin Create PagerAdapter");

        StepDetailPagerAdapter pagerAdapter = new StepDetailPagerAdapter(getApplicationContext(), mRecipeObj, getSupportFragmentManager());
        mPagerStepDetail.setAdapter(pagerAdapter);
        mTabStepDetail.setupWithViewPager(mPagerStepDetail);
        mPagerStepDetail.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                if (actionBar != null) {
                    actionBar.setTitle(mRecipeObj.getSteps().get(position).getShortDesc());
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
        mPagerStepDetail.setCurrentItem(mClickedStep);
    }

    private void openFragment(RecipeObject recipeObj, int clickedStep) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(getString(R.string.BUNDLE_KEY_RECIPE_OBJ), recipeObj);
        bundle.putInt(getString(R.string.BUNDLE_KEY_CLICKED_STEP), clickedStep);

        StepDetailFragment fragment = new StepDetailFragment();
        fragment.setArguments(bundle);

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .add(R.id.step_detail_container, fragment)
                //.addToBackStack(null)
                .commit();
    }

    // *** libraries ***

    private void bindViews() {
        // Bind the views
        ButterKnife.bind(this);
    }
}
