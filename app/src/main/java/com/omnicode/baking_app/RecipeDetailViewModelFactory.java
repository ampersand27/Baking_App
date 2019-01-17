package com.omnicode.baking_app;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import com.omnicode.baking_app.data.RecipeObject;

public class RecipeDetailViewModelFactory extends ViewModelProvider.NewInstanceFactory {
    private final RecipeObject obj;

    public RecipeDetailViewModelFactory(RecipeObject obj) {
        this.obj = obj;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new RecipeDetailViewModel(obj);
    }
}
