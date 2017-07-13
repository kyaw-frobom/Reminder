package com.frobom.reminder;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Lenovo on 6/29/2017.
 */

public class Attributes implements Parcelable{
    private int id;
    private String title;
    private String description;
    private String alarmTime;
    private String alarmDate;
    private String alarmPath;
    private String enabled;


    protected Attributes(Parcel in) {
        id = in.readInt();
        title = in.readString();
        description = in.readString();
        alarmTime = in.readString();
        alarmDate = in.readString();
        alarmPath = in.readString();
        enabled = in.readString();
    }

    public static final Creator<Attributes> CREATOR = new Creator<Attributes>() {
        @Override
        public Attributes createFromParcel(Parcel in) {
            return new Attributes(in);
        }

        @Override
        public Attributes[] newArray(int size) {
            return new Attributes[size];
        }
    };

    protected Attributes() {
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

    public String getAlarmDate() {
        return alarmDate;
    }

    public void setAlarmDate(String alarmDate) {
        this.alarmDate = alarmDate;
    }

    public String getAlarmTime() {
        return alarmTime;
    }

    public void setAlarmTime(String alarmTime) {
        this.alarmTime = alarmTime;
    }

    public String getAlarmPath() {
        return alarmPath;
    }

    public void setAlarmPath(String alarmPath) {
        this.alarmPath = alarmPath;
    }

    public String isEnabled() {
        return enabled;
    }

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
        parcel.writeString(alarmTime);
        parcel.writeString(alarmDate);
        parcel.writeString(alarmPath);
        parcel.writeString(enabled);
    }
}
