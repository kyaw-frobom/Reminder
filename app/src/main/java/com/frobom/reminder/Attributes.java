package com.frobom.reminder;

/**
 * Created by Lenovo on 6/29/2017.
 */

public class Attributes {
    private int id;
    private String title;
    private String description;
    private String alarmTime;
    private String alarmDate;
    private String alarmPath;
    private String enabled;

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

    public String getAlarmTime() {
        return alarmTime;
    }

    public void setAlarmTime(String alarmTime) {
        this.alarmTime = alarmTime;
    }

    public String getAlarmDate() {
        return alarmDate;
    }

    public void setAlarmDate(String alarmDate) {
        this.alarmDate = alarmDate;
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
        return "Attributes{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", alarmTime='" + alarmTime + '\'' +
                ", alarmDate='" + alarmDate + '\'' +
                ", alarmPath='" + alarmPath + '\'' +
                ", enabled=" + enabled +
                '}';
    }
}
