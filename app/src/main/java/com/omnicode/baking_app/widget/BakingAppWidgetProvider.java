package com.omnicode.baking_app.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.omnicode.baking_app.MainActivity;
import com.omnicode.baking_app.R;
import com.omnicode.baking_app.data.RecipeObject;
import com.omnicode.baking_app.utils.WidgetHelper;

import timber.log.Timber;

/**
 * Implementation of App Widget functionality.
 */
public class BakingAppWidgetProvider extends AppWidgetProvider {

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {
        //TODO: Hier weiter arbeiten
        //RecipeObject mRecipeObject = WidgetHelper.getRecipeFromPref(context);
        Timber.d("XX updateAppWidget");
        RecipeObject recipeObject = WidgetHelper.getRecipeFromPref(context);

        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.baking_app_widget);
        views.setTextViewText(R.id.widget_title, recipeObject.getRecipeName());

        //create ClickListener in widget
        Intent intent = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
        views.setOnClickPendingIntent(R.id.baking_app_widget_frame, pendingIntent);

        //init listview and adapter
        Intent intentWidget = new Intent(context, BakingAppWidgetService.class);
        intentWidget.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        //intentWidget.setData()
        views.setRemoteAdapter(R.id.widget_list_ingredients, intent);

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetId, R.id.widget_list_ingredients);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        Timber.d("XX onUpdate");
        /*for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }*/
        //Start the intent service update widget action, the service takes care of updating the widgets UI
        BakingAppWidgetService.startActionUpdateWidgets(context);
    }

    public static void updateIngredWidgets(Context context, AppWidgetManager appWidgetManager,
                                           int[] appWidgetIds) {
        Timber.d("XX updateIngredWidgets");
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    /*private static RemoteViews getIngredListRemoteView(Context context) {
        RemoteViews views  = new RemoteViews(context.getPackageName(), R.layout.baking_app_widget);
        return views;
    }*/
}

