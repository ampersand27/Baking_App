package com.omnicode.baking_app.data;

import android.os.Parcel;
        import android.os.Parcelable;

public class IngredientObject implements Parcelable {
    private int quantity;
    private String measure;
    private String ingredient;

    public IngredientObject() {}

    public IngredientObject(int quantity, String measure, String ingredient) {
        this.quantity = quantity;
        this.measure = measure;
        this.ingredient = ingredient;
    }

    public IngredientObject(Parcel in) {
        quantity = in.readInt();
        measure = in.readString();
        ingredient = in.readString();
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
        dest.writeInt(quantity);
        dest.writeString(measure);
        dest.writeString(ingredient);
    }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getMeasure() {return measure; }
    public void setMeasure(String measure) {
        this.measure = measure;
    }

    public String getIngredient() {return ingredient; }
    public void setIngredient(String ingredient) {
        this.ingredient = ingredient;
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<IngredientObject> CREATOR = new Parcelable.Creator<IngredientObject>() {
        @Override
        public IngredientObject createFromParcel(Parcel in) {
            return new IngredientObject(in);
        }

        @Override
        public IngredientObject[] newArray(int size) {
            return new IngredientObject[size];
        }
    };

}
