package com.example.fdfda.Model;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.util.Objects;

public class Card_details {

    private String day;
    private String time;
    private String date;

    public Card_details(String day, String time, String date) {
        this.day = day;
        this.time = time;
        this.date = date;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    // this to compare object with object to remove it from list
    // without this method u don't compare object with another
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Card_details that = (Card_details) o;
        return day.equals(that.day) &&
                time.equals(that.time) &&
                date.equals(that.date);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public int hashCode() {
        return Objects.hash(day, time, date);
    }
}
