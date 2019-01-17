package com.omnicode.baking_app.data;

import android.os.Parcel;
import android.os.Parcelable;

public class StepsObject implements Parcelable {
    private int stepsId;
    private String shortDesc;
    private String desc;
    private String videoUrl;
    private String thumbUrl;

    public StepsObject() {}

    public StepsObject(int stepsId, String shortDesc, String desc, String videoUrl, String thumbUrl) {
        this.stepsId = stepsId;
        this.shortDesc = shortDesc;
        this.desc = desc;
        this.videoUrl = videoUrl;
        this.thumbUrl = thumbUrl;
    }

    public StepsObject(Parcel in) {
        stepsId = in.readInt();
        shortDesc = in.readString();
        desc = in.readString();
        videoUrl = in.readString();
        thumbUrl = in.readString();
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
        dest.writeInt(stepsId);
        dest.writeString(shortDesc);
        dest.writeString(desc);
        dest.writeString(videoUrl);
        dest.writeString(thumbUrl);
    }

    public int getStepsId() { return stepsId; }
    public void setStepsId(int stepsId) {
        this.stepsId = stepsId;
    }

    public String getShortDesc() {return shortDesc; }
    public void setShortDesc(String shortDesc) {
        this.shortDesc = shortDesc;
    }

    public String getDesc() {return desc; }
    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getVideoUrl() {return videoUrl; }
    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public String getThumbUrl() {return thumbUrl; }
    public void setThumbUrl(String thumbUrl) {
        this.thumbUrl = thumbUrl;
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<StepsObject> CREATOR = new Parcelable.Creator<StepsObject>() {
        @Override
        public StepsObject createFromParcel(Parcel in) {
            return new StepsObject(in);
        }

        @Override
        public StepsObject[] newArray(int size) {
            return new StepsObject[size];
        }
    };

}
