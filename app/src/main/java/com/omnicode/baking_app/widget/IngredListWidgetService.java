package com.omnicode.baking_app.widget;

import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.omnicode.baking_app.R;
import com.omnicode.baking_app.data.RecipeObject;
import com.omnicode.baking_app.utils.WidgetHelper;

import timber.log.Timber;

public class IngredListWidgetService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        Timber.d("XX onGetViewFactory ");
        return new IngredListRemoteViewsFactory(this.getApplicationContext());
    }
}

class IngredListRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {
    Context mContext;
    RecipeObject mRecipeObject;

    public IngredListRemoteViewsFactory(Context applicationContext) {
        mContext = applicationContext;
    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onDataSetChanged() {
        //TODO: get DataObject
        mRecipeObject = WidgetHelper.getRecipeFromPref(mContext);
        Timber.d("XX onDataSetChanged: "+mRecipeObject.getRecipeName());
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        Timber.d("XX getCount: "+mRecipeObject.getIngredients().size());
        return  mRecipeObject.getIngredients().size();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        Timber.d("XX getViewAt");
        RemoteViews views = new RemoteViews(mContext.getPackageName(), R.layout.baking_app_widget);
        views.setTextViewText(R.id.widget_item_text, mRecipeObject.getIngredients().get(position).getIngredient());
        return views;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }
}