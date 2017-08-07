package com.frobom.reminder;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Lenovo on 6/29/2017.
 */

public class LocationAttributes implements Parcelable{
    private int id;
    private String title;
    private String description;
    private String alarmLocation;
    private String latitude;
    private String longitude;
    private int radius;
    private String alarmPath;
    private String enabled;


    protected LocationAttributes(Parcel in) {
        id = in.readInt();
        title = in.readString();
        description = in.readString();
        alarmLocation = in.readString();
        latitude = in.readString();
        longitude = in.readString();
        radius = in.readInt();
        alarmPath = in.readString();
        enabled = in.readString();
    }

    public static final Creator<LocationAttributes> CREATOR = new Creator<LocationAttributes>() {
        @Override
        public LocationAttributes createFromParcel(Parcel in) {
            return new LocationAttributes(in);
        }

        @Override
        public LocationAttributes[] newArray(int size) {
            return new LocationAttributes[size];
        }
    };

    protected LocationAttributes() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAlarmLocation() {
        return alarmLocation;
    }

    public void setAlarmLocation(String alarmLocation) {
        this.alarmLocation = alarmLocation;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public void setRadius(int radius){ this.radius = radius; }

    public int getRadius() { return radius; }

    public String getAlarmPath() { return  alarmPath; }

    public void setAlarmPath(String alarmPath) { this.alarmPath = alarmPath; }

    public String isEnabled() { return enabled; }

    public void setEnabled(String enabled) {
        this.enabled = enabled;
    }

    // Will be used by the ArrayAdapter in the ListView
    @Override
    public String toString() {
        return title ;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {

        parcel.writeInt(id);
        parcel.writeString(title);
        parcel.writeString(description);
        parcel.writeString(alarmLocation);
        parcel.writeString(latitude);
        parcel.writeString(longitude);
        parcel.writeInt(radius);
        parcel.writeString(alarmPath);
        parcel.writeString(enabled);
    }
}
