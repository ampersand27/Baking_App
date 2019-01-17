package com.omnicode.baking_app;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;

import com.omnicode.baking_app.data.RecipeObject;

public class RecipeStepDetail extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActionBar actionBar = this.getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        Bundle extras = getIntent().getExtras();
        if (extras == null) {
            return;
        }

        RecipeObject extraRecipeObj = extras.getParcelable(getString(R.string.BUNDLE_KEY_RECIPE_OBJ));
        if (extraRecipeObj == null) return;

        setContentView(R.layout.recipe_step_detail);

    }
}
