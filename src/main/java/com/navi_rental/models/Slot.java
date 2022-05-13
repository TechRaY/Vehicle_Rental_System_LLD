package com.navi_rental.models;

public class Slot{
    int startTime;
    int endTime;
    int bookedPrice;

    public Slot(int startTime, int endTime, int bookedPrice) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.bookedPrice = bookedPrice;
    }
}
