package com.omnicode.baking_app;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;

import com.omnicode.baking_app.data.RecipeObject;

/* RecipeDetail öffnet statisches Fragment direkt über Layout Datei recipe_detail.xml,
    statt direktem Aufruf
 */
public class RecipeDetail extends AppCompatActivity  {

    private boolean mTwoPane;
    private RecipeObject mRecipeObj;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle extras = getIntent().getExtras();
        if (extras == null) { return; }

        mRecipeObj = extras.getParcelable(getString(R.string.BUNDLE_KEY_RECIPE_OBJ));
        if (mRecipeObj   == null)  return;

        //Resource value unterscheidet zwischen handset und tablet (sw600dp)
        mTwoPane = getResources().getBoolean(R.bool.twoPaneMode);

        //done: Daten von RecipeDetail an Fragment übergeben
        RecipeDetailViewModelFactory factory = new RecipeDetailViewModelFactory(mRecipeObj);
        RecipeDetailViewModel viewModel = ViewModelProviders.of(this, factory).get(RecipeDetailViewModel.class);
        openFragment(mRecipeObj);

        setContentView(R.layout.recipe_detail);

        ActionBar actionBar = this.getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(mRecipeObj.getRecipeName());
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        // bei one-pane wird das Fragment automatisch angezeigt durch Verbindung in Layout.xml
        if (mTwoPane) {
            if (savedInstanceState == null && !mRecipeObj.getSteps().isEmpty()) {
                openStepDetail(mRecipeObj, 0);
            }

        }
        else {

        }

        //DONE: an Fragment muss das aktuelle RecipeObject übergeben werden.
        //DONE: aktuelles Objekt muss von MainActivity an REcipeDEtail übergeben werdne.
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public boolean onSupportNavigateUp() {
//        return super.onSupportNavigateUp();
        onBackPressed();
        return true;
    }
    private void openFragment(RecipeObject recipeObj) {
        RecipeDetailFragment fragment = new RecipeDetailFragment();
        //fragment.setArguments(bundle);

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .add(R.id.recipe_detail_fragment_container, fragment)
                //.addToBackStack(null) führt zu Fehler wenn Home gedrückt wird
                .commit();
    }

    public void openStepDetail(RecipeObject obj, int clickedStep) {
        if (mTwoPane) {
            Bundle extras = new Bundle();
            extras.putParcelable(getString(R.string.BUNDLE_KEY_STEP_OBJ), obj.getSteps().get(clickedStep));
            //extras.putInt(getString(R.string.BUNDLE_KEY_CLICKED_STEP), clickedStep);

            StepDetailFragment fragment = new StepDetailFragment();
            fragment.setArguments(extras);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.step_detail_container, fragment)
                    .commit();
        }
        else {
            Intent intent = new Intent(getApplicationContext(), StepDetail.class);
            intent.putExtra(getString(R.string.BUNDLE_KEY_RECIPE_OBJ), obj);
            intent.putExtra(getString(R.string.BUNDLE_KEY_CLICKED_STEP), clickedStep);
            startActivity(intent);
        }
    }

}
