package com.example.todoapp2;

public class TimeDateFormat {
    String dateTime;
    public TimeDateFormat(long dateTime){
        this.dateTime = String.valueOf(dateTime);
    }

    public String getMinute(){
        return dateTime.substring(10);
    }

    public String getHour(){
        return dateTime.substring(8,10);
    }

    public String getDayOfMonth(){
        return dateTime.substring(6,8);
    }

    public String getMonth(){
        return dateTime.substring(4,6);
    }

    public String getYear(){
        return dateTime.substring(0,4);
    }

}
