package com.omnicode.baking_app;

import android.arch.lifecycle.ViewModel;

import com.omnicode.baking_app.data.RecipeObject;

/*  You should use the AndroidViewModel only if you need the context inside your ViewModel.
    For example, if you need to do some database reads or writes from your ViewModel, using an AndroidViewModel is a good idea*/
public class RecipeDetailViewModel extends ViewModel {
    private RecipeObject recipeObj;

    public RecipeDetailViewModel(RecipeObject obj) {
        this.recipeObj = obj;
    }

    public RecipeObject getRecipeObj() { return recipeObj; }
}
