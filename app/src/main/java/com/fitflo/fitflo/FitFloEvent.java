package com.fitflo.fitflo;

/**
 * Created by cj on 2/28/16.
 * needs to be updated. no longer dealing with events, dealing with coaches
 * this was so an individual could search an event
 */
public class FitFloEvent {
    String eventID;
    String instructorName;
    String title;
    double price;
    public FitFloEvent(String eventID, String instructorName,String title,double price) {
        this.eventID = eventID;
        this.instructorName = instructorName;
        this.title = title;
        this.price = price;
    }

    @Override
    public String toString() {
        return title + ", " + instructorName + ", " + price;
    }
}
