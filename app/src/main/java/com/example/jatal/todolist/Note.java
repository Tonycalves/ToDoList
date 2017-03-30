package com.example.jatal.todolist;

import android.os.Bundle;

public final class Note {
    private int id;
    private String name;
    private String content;
    private boolean remind;
    private int day;
    private int month;
    private int year;
    private int hour;
    private int min;
    private int state;

    public Note() {
        this.remind = false;
        this.id = -1;
        this.state = -1;
    }

    public Note(String name, String content, int id, int state) {
        this.remind = false;
        this.name = name;
        this.content = content;
        this.id = id;
        this.state = state;
    }

    public Note(String name, String content, int day, int month, int year, int hour, int min, int id, int state) {
        this.name = name;
        this.content = content;
        this.remind = true;
        this.day = day;
        this.month = month;
        this.year = year;
        this.hour = hour;
        this.min = min;
        this.id = id;
        this.state = state;
    }

    public void addState(int a) {
        while (a > 0) {
            a--;
            state++;
            if (state >= 2)
                state = -1;
        }

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public boolean isRemind() {
        return remind;
    }

    public void setRemind(boolean remind) {
        this.remind = remind;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public int getMin() {
        return min;
    }

    public void setMin(int min) {
        this.min = min;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public boolean getRemind() {
        return remind;
    }

    public String toString() {
        return ("Nom:" + name + "\nContent" + content);
    }

    public Bundle getNValue()
    {
        Bundle extras = new Bundle();
        extras.putInt("EXTRA_NOTE", 1);
        extras.putInt("EXTRA_ID", id);
        extras.putString("EXTRA_NAME",name);
        extras.putString("EXTRA_CONTENT",content);
        extras.putInt("EXTRA_TODO", state);
        if (remind == true) {
            extras.putInt("EXTRA_REMIND", 1);
            extras.putInt("EXTRA_DAY", day);
            extras.putInt("EXTRA_MONTH", month);
            extras.putInt("EXTRA_YEAR", year);
            extras.putInt("EXTRA_HOUR", hour);
            extras.putInt("EXTRA_MIN", min);
        }
        else {
            extras.putInt("EXTRA_REMIND", 0);
        }
        return (extras);
    }
}
