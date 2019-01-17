package com.omnicode.baking_app;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.omnicode.baking_app.data.IngredientObject;
import com.omnicode.baking_app.data.RecipeObject;
import com.omnicode.baking_app.data.StepObject;
import com.omnicode.baking_app.utils.NetworkUtils;
import com.omnicode.baking_app.utils.WidgetHelper;
import com.omnicode.baking_app.widget.BakingAppWidgetProvider;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

//(c) Dirk Weber 2018-11-15
// Baking App
// NOT Submitted to Udacity

public class MainActivity extends AppCompatActivity implements RecipeAdapter.RecipeItemClickListener {

    private static final String INSTANCE_SORT_ORDER = "instanceSortOrder";

    @BindView(R.id.rv_recipes) RecyclerView rvRecipes;
    @BindView(R.id.tv_error_message_display) TextView mErrorMessage;
    @BindView(R.id.pb_loading_indicator) ProgressBar mLoadingIndicator;

    private RecipeAdapter recipeAdapter;
    static List<RecipeObject> recipeList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bindViews();
        setupTimber();
        Timber.d("XX onCreate");

        readSavedInstance(savedInstanceState);

        GridLayoutManager layoutManager = new GridLayoutManager(this, getResources().getInteger(R.integer.spanCount));
        rvRecipes.setLayoutManager(layoutManager);
        rvRecipes.setHasFixedSize(true);

        loadData();

        recipeAdapter = new RecipeAdapter(this);
        rvRecipes.setAdapter(recipeAdapter);

    }

    private void loadData() {
        Timber.d("XX loadData() ");
        if (!NetworkUtils.InternetAvailable(this)) { showErrorMessage(true); return;}
        URL APIUrl = NetworkUtils.buildUrlLoadRecipes();
        //Toast.makeText(MainActivity.this, "API URL "+APIUrl.toString(), Toast.LENGTH_LONG).show();
        new MovieAsyncTask(this).execute(APIUrl);
    }

    //retrieves data from TheMovieDB
    /*To prevent leaks, you can make the the inner class static. The problem with that, though, is that you no
    longer have access to the Activity's UI views or member variables. You can pass in a reference to the
    Context but then you run the same risk of a memory leak. (Android can't garbage collect the Activity after
    it closes if the AsyncTask class has a strong reference to it.) The solution is to make a weak reference
    to the Activity (or whatever Context you need).*/
    public static class MovieAsyncTask extends AsyncTask<URL, Void, List<RecipeObject>> {

        private WeakReference<MainActivity> activityRef;

        MovieAsyncTask(MainActivity context) {
            activityRef = new WeakReference<>(context);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            MainActivity activity = activityRef.get();
            if (activity == null || activity.isFinishing()) return;
            activity.mLoadingIndicator.setVisibility(View.VISIBLE);
        }

        @Override
        protected List<RecipeObject> doInBackground(URL... params) {
            MainActivity activity = activityRef.get();
            if (activity == null || activity.isFinishing()) return null;
            URL searchUrl = params[0];
            String searchResults;
            try {
                if (!NetworkUtils.InternetAvailable(activity)) return null;
                searchResults = NetworkUtils.getJSONDataFromUrl(searchUrl);
                return activity.ConvertJsonToObjectList(searchResults);
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(List<RecipeObject> searchResults) {
            MainActivity activity = activityRef.get();
            if (activity == null || activity.isFinishing()) return;
            activity.mLoadingIndicator.setVisibility(View.INVISIBLE);
            if (searchResults != null && searchResults.size() > 0) {
                activity.recipeList = searchResults;
                activity.showErrorMessage(false);
                activity.recipeAdapter.setRecipeData(activity.recipeList);
                //mTestResults.setText(searchResults);
            }
            else {
                activity.showErrorMessage(true);
            }
        }
    }

    /*
[
  {
    "id": 1,
    "name": "Nutella Pie",
    "ingredients": [
      {
        "quantity": 2,
        "measure": "CUP",
        "ingredient": "Graham Cracker crumbs"
      },
      {
        "quantity": 6,
        "measure": "TBLSP",
        "ingredient": "unsalted butter, melted"
      },
    ],
    "steps": [
      {
        "id": 0,
        "shortDescription": "Recipe Introduction",
        "description": "Recipe Introduction",
        "videoURL": "https://d17h27t6h515a5.cloudfront.net/topher/2017/April/58ffd974_-intro-creampie/-intro-creampie.mp4",
        "thumbnailURL": ""
      },
      {
        "id": 1,
        "shortDescription": "Starting prep",
        "description": "1. Preheat the oven to 350\u00b0F. Butter a 9\" deep dish pie pan.",
        "videoURL": "",
        "thumbnailURL": ""
      },
      {
        "id": 5,
        "shortDescription": "Finish filling prep",
        "description": "5. Beat the cream cheese and 50 grams (1/4 cup) of sugar on medium speed in a stand mixer or high speed with a hand mixer for 3 minutes. Decrease the speed to medium-low and gradually add in the cold cream. Add in 2 teaspoons of vanilla and beat until stiff peaks form.",
        "videoURL": "",
        "thumbnailURL": "https://d17h27t6h515a5.cloudfront.net/topher/2017/April/58ffda20_7-add-cream-mix-creampie/7-add-cream-mix-creampie.mp4"
      },
    ],
    "servings": 8,
    "image": ""
  },
    */
    private List<RecipeObject> ConvertJsonToObjectList(String searchResults) {
        //DONE: JSON Konverter anpassen
        List<RecipeObject> recipeList;
        try {
            // read JSON
            JSONArray allRecipes = new JSONArray(searchResults);
            //JSONArray allRecipes = json.getJSONArray(""); //Outer Array has no name
            int len = allRecipes.length();
            recipeList = new ArrayList<RecipeObject>();

            //loop all recipes
            for (int j = 0; j < len; j++) {
                JSONObject recipeItem = allRecipes.getJSONObject(j);
                int recipeId = recipeItem.optInt(getString(R.string.JSON_KEY_ID));
                String recipeName =  recipeItem.optString(getString(R.string.JSON_KEY_NAME), getString(R.string.JSON_KEY_NAME_FALLBACK));

                RecipeObject recipeObj = new RecipeObject();
                recipeObj.setRecipeId(recipeId);
                recipeObj.setRecipeName(recipeName);

                //ingredients loop
                ArrayList<IngredientObject> ingredientList = new ArrayList<IngredientObject>();
                JSONArray allIngredients = recipeItem.optJSONArray(getString(R.string.JSON_KEY_INGREDIENTS)); //inner Array ingredients

                if (allIngredients!= null) {
                   int lenIngr = allIngredients.length();
                   //loop all ingredients
                   for (int k = 0; k < lenIngr; k++) {
                       JSONObject ingredientItem = allIngredients.getJSONObject(k);
                       int quantity = ingredientItem.optInt(getString(R.string.JSON_KEY_IG_QUANTITY));
                       String measure = ingredientItem.optString(getString(R.string.JSON_KEY_IG_MEASURE));
                       String ingredient = ingredientItem.optString(getString(R.string.JSON_KEY_IG_INGREDIENT));
                       IngredientObject ingrObj = new IngredientObject(quantity, measure, ingredient);
                       ingredientList.add(ingrObj);
                   }
                }
                recipeObj.setIngredients(ingredientList);

                //steps loop
                ArrayList<StepObject> stepList = new ArrayList<StepObject>();
                JSONArray allSteps = recipeItem.optJSONArray(getString(R.string.JSON_KEY_STEPS)); //inner Array ingredients

                if (allSteps!= null) {
                    int lenSteps = allSteps.length();
                    //loop all ingredients
                    for (int l = 0; l < lenSteps; l++) {
                        JSONObject stepItem = allSteps.getJSONObject(l);
                        int id = stepItem.optInt(getString(R.string.JSON_KEY_STEP_ID));
                        String shortDesc = stepItem.optString(getString(R.string.JSON_KEY_STEP_SHORTDESC));
                        String desc = stepItem.optString(getString(R.string.JSON_KEY_STEP_DESC));
                        String videoUrl = stepItem.optString(getString(R.string.JSON_KEY_STEP_VIDEO_URL));
                        String thumbUrl = stepItem.optString(getString(R.string.JSON_KEY_STEP_THUMB_URL));
                        StepObject stepObj = new StepObject(id, shortDesc, desc, videoUrl, thumbUrl);
                        stepList.add(stepObj);
                    }
                }
                recipeObj.setSteps(stepList);

                Timber.d("XX Recipe: "+ recipeName+" Ingr: "+ingredientList.size()+" Steps: "+stepList.size());

                recipeList.add(recipeObj);
            }
        }
        catch (JSONException e) {
            Log.e("log_tag", "Error parsing data "+e.toString());
            return null;
        }
        return recipeList;
    }

    private void showErrorMessage(boolean visible) {
        if (visible) {
            mErrorMessage.setVisibility(View.VISIBLE);
            rvRecipes.setVisibility(View.INVISIBLE);
        }
        else {
            mErrorMessage.setVisibility(View.INVISIBLE);
            rvRecipes.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onRecipeItemClickListener(int clickedItemIndex) {
        Timber.d("XX onRecipeItemClickListener() "+clickedItemIndex);

        RecipeObject obj;
        obj = recipeList.get(clickedItemIndex);
         if (obj != null ) {
            //DONE: Save recipeObj to SharedPreferences for Widget
            WidgetHelper.saveRecipeToPref(this, obj);
            //DONE: hier muss das updaten der Widgets angestoßen werden
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
            int[] appWidgetIds =appWidgetManager.getAppWidgetIds(new ComponentName(this, BakingAppWidgetProvider.class));
            appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.widget_list_ingredients);
            BakingAppWidgetProvider.updateIngredWidgets(this, appWidgetManager, appWidgetIds);

            Intent intent = new Intent(getApplicationContext(), RecipeDetail.class);
            intent.putExtra(getString(R.string.BUNDLE_KEY_RECIPE_OBJ), obj);
            startActivity(intent);
        }
    }

    public static RecipeObject getCurrentRecipeObject(int pos) {
        RecipeObject obj = recipeList.get(pos);
        return obj;
    }

    // *** SaveInstanceState ***

    private void readSavedInstance(Bundle savedInstanceState) {
        if (savedInstanceState != null ) {
            if (savedInstanceState.containsKey(INSTANCE_SORT_ORDER)) {
                //currentSortOrder = savedInstanceState.getString(INSTANCE_SORT_ORDER);
                Timber.d("found savedInstanceState of INSTANCE_SORT_ORDER - reading variable ");
            } else {
                //currentSortOrder = getString(R.string.POPULAR_URL);
                Timber.d( "no savedInstanceState of INSTANCE_SORT_ORDER- set variable to default ");
            }
        }
        else {
            Timber.d( "savedInstanceState = null");
            //currentSortOrder = getString(R.string.POPULAR_URL);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        Timber.d("XX onSaveInstanceState() ");
        //outState.putString(INSTANCE_SORT_ORDER, currentSortOrder);
        super.onSaveInstanceState(outState);
    }


    // *** libraries ***

    private void bindViews() {
        // Bind the views
        ButterKnife.bind(this);
    }

    private void setupTimber() {
        // Set up Timber
        Timber.plant(new Timber.DebugTree());
    }
}
