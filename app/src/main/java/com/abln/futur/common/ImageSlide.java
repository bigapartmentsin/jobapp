package com.abln.futur.common;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Lincoln on 04/04/16.
 */
public class ImageSlide implements Parcelable {
    public static final Creator<ImageSlide> CREATOR = new Creator<ImageSlide>() {
        @Override
        public ImageSlide createFromParcel(Parcel source) {
            return new ImageSlide(source);
        }

        @Override
        public ImageSlide[] newArray(int size) {
            return new ImageSlide[size];
        }
    };
    private String name;
    private String small, medium, large;
    private String timestamp;
    private String id;

    public ImageSlide() {
    }

    public ImageSlide(String name, String small, String medium, String large, String timestamp) {
        this.name = name;
        this.small = small;
        this.medium = medium;
        this.large = large;
        this.timestamp = timestamp;
    }

    protected ImageSlide(Parcel in) {
        this.name = in.readString();
        this.small = in.readString();
        this.medium = in.readString();
        this.large = in.readString();
        this.timestamp = in.readString();
        this.id = in.readString();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSmall() {
        return small;
    }

    public void setSmall(String small) {
        this.small = small;
    }

    public String getMedium() {
        return medium;
    }

    public void setMedium(String medium) {
        this.medium = medium;
    }

    public String getLarge() {
        return large;
    }

    public void setLarge(String large) {
        this.large = large;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeString(this.small);
        dest.writeString(this.medium);
        dest.writeString(this.large);
        dest.writeString(this.timestamp);
        dest.writeString(this.id);
    }
}
