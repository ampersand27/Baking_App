package com.omnicode.baking_app.data;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Base64;

import java.util.ArrayList;
import java.util.logging.Logger;

import timber.log.Timber;

public class RecipeObject implements Parcelable {
    private int recipeId;
    private String recipeName;
    private ArrayList<IngredientObject> ingredients;
    private ArrayList<StepObject> steps;

    public RecipeObject() {
        ingredients = new ArrayList<IngredientObject>();
        steps = new ArrayList<StepObject>();
    }

    public RecipeObject(int recipeId, String recipeName, ArrayList<IngredientObject> ingredients, ArrayList<StepObject> steps) {
        this.recipeId = recipeId;
        this.recipeName = recipeName;
        this.ingredients = ingredients;
        this.steps = steps;
    }

    protected RecipeObject(Parcel in) {
        recipeId = in.readInt();
        recipeName = in.readString();
        ingredients = in.createTypedArrayList(IngredientObject.CREATOR);
        //ingredients = in.readArrayList(IngredientObject.class.getClassLoader()); // geht nicht
        steps = in.createTypedArrayList(StepObject.CREATOR);

    }

    @Override
    public int describeContents() {
        return 0;
    }

    /**
     * Actual object serialization happens here, Write object content
     * to parcel, reading should be done according to this write order
     * param dest - parcel
     * param flags - Additional flags about how the object should be written
     */
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(recipeId);
        dest.writeString(recipeName);
        dest.writeTypedList(ingredients);
        dest.writeTypedList(steps);
    }

    public int getRecipeId() { return recipeId; }
    public void setRecipeId(int recipeId) {
        this.recipeId = recipeId;
    }

    public String getRecipeName() {return recipeName; }
    public void setRecipeName(String recipeName) {
        this.recipeName = recipeName;
    }

    public ArrayList<IngredientObject> getIngredients() {return ingredients; }
    public void setIngredients(ArrayList<IngredientObject> ingredients) {
        this.ingredients = ingredients;
    }

    public String getIngredientsAsString() {
        //String result = TextUtils.join("\n", ingredients.)
        StringBuilder sb = new StringBuilder();
        for (int i=0; i<ingredients.size(); i++) {
            String quantity = String.valueOf(ingredients.get(i).getQuantity());
            String measure = ingredients.get(i).getMeasure();
            String ingred = ingredients.get(i).getIngredient();
            sb.append(quantity+" "+measure + "\t\t\t " + ingred +"\n");
        }
        return sb.toString();
    }

    public ArrayList<StepObject> getSteps() {return steps; }
    public void setSteps(ArrayList<StepObject> steps) {
        this.steps = steps;
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<RecipeObject> CREATOR = new Parcelable.Creator<RecipeObject>() {
        @Override
        public RecipeObject createFromParcel(Parcel in) {
            return new RecipeObject(in);
        }

        @Override
        public RecipeObject[] newArray(int size) {
            return new RecipeObject[size];
        }
    };

}
