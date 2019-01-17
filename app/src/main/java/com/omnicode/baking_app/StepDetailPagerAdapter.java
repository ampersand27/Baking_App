package com.omnicode.baking_app;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v7.app.AppCompatActivity;

import com.omnicode.baking_app.data.RecipeObject;
import com.omnicode.baking_app.R;

public class StepDetailPagerAdapter extends FragmentPagerAdapter {
    private Context mContext;
    private RecipeObject mRecipeObj;

    public StepDetailPagerAdapter(Context context, RecipeObject recipeObject, FragmentManager fragMan) {
        super(fragMan);
        this.mContext = context;
        this.mRecipeObj = recipeObject;
    }

    @Override
    public Fragment getItem(int clickedStep) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(mContext.getString(R.string.BUNDLE_KEY_STEP_OBJ), mRecipeObj.getSteps().get(clickedStep));
        //bundle.putInt(mContext.getString(R.string.BUNDLE_KEY_CLICKED_STEP), clickedStep);

        StepDetailFragment fragment = new StepDetailFragment();
        fragment.setArguments(bundle);

        return fragment;
    }
    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return String.format(mContext.getString(R.string.PAGER_TITLE), position);
    }

    @Override
    public int getCount() {
        return mRecipeObj.getSteps().size();
    }
}
