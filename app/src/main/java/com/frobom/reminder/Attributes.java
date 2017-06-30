package com.frobom.reminder;

/**
 * Created by Lenovo on 6/29/2017.
 */

public class Attributes {
    private int id;
    private String title;
    private String description;
    private String time;
    private String date;
    private String alarm_path;

    public Attributes(){

    }
    public int getId(){
        return id;
    }
    public void setId(int id){
        this.id=id;
    }
    public String getTitle(){
        return title;
    }
    public void setTitle(String title){
        this.title=title;
    }
    public String getDescription(){
        return description;
    }
    public void setDescription(String description){
        this.description=description;
    }
    public String getTime(){
        return time;
    }
    public void setTime(String time){
        this.time = time;
    }
    public String getDate(){
        return  date;
    }
    public void setDate(String date){
        this.date = date;
    }
    public String getAlarm_path(){
        return  alarm_path;
    }
    public void setRemindUri(String alarm_path){
        this.alarm_path = alarm_path;
    }
    // Will be used by the ArrayAdapter in the ListView
    @Override
    public String toString() {
        return title;
    }
}
