package com.omnicode.baking_app.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.omnicode.baking_app.MainActivity;
import com.omnicode.baking_app.R;
import com.omnicode.baking_app.data.RecipeObject;

import java.util.concurrent.CopyOnWriteArrayList;

import timber.log.Timber;

public class WidgetHelper {

    public static void saveRecipeToPref(Context context, RecipeObject recipeObject) {
        SharedPreferences.Editor prefs = context.getSharedPreferences(context.getString(R.string.BAKING_APP_PREFS_FILE), Context.MODE_PRIVATE).edit();
        //prefs.putString(context.getString(R.string.PREFS_KEY_WIDGET_OBJ), RecipeObject.toBase64String(recipe));
        prefs.putInt(context.getString(R.string.PREFS_KEY_WIDGET_OBJ), recipeObject.getRecipeId());

        prefs.apply();
        Timber.d("XX save RecipeObject: "+recipeObject.getRecipeName());
    }
    public static RecipeObject getRecipeFromPref(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(context.getString(R.string.BAKING_APP_PREFS_FILE), Context.MODE_PRIVATE);
        //String recipeKey = prefs.getString(context.getString(R.string.PREFS_KEY_WIDGET_OBJ), "");
        int recipeId = prefs.getInt(context.getString(R.string.PREFS_KEY_WIDGET_OBJ), 0);
        //TODO TEstobject
        RecipeObject recipeObject = MainActivity.getCurrentRecipeObject(recipeId-1);
        //RecipeObject recipeObject = new RecipeObject(99, "Test recipe", null, null);
       /* if (recipeKey.equals(""))
            return null;
        else
            return RecipeObject.getFromPrefs(recipeKey);*/
        Timber.d("XX loaded RecipeObject: "+recipeObject.getRecipeName()+" "+recipeObject.getIngredients().size());
       return recipeObject;
    }
}
