package seng302.group6.Model.ItemClasses;

import com.google.gson.annotations.Expose;

import java.time.Duration;
import java.time.LocalDateTime;

/**
 * Model class to represent Effort spent on a task.
 * Created by Josh on 10/09/2015.
 */
public class Effort {

    @Expose
    private String person;
    @Expose
    private String comment;
    @Expose
    private String startDate;
    @Expose
    private int minutes;

    /**
     * Create an effort log by specifying a start date/time and duration
     * @param person UID the peron logging
     * @param comment Comment about the logged effort
     * @param startDate Date/time the effort began
     * @param minutes Duration of the effort
     */
    public Effort(String person, String comment, LocalDateTime startDate, int minutes) {
        this.person = person;
        this.comment = comment;
        this.startDate = startDate.toString();
        this.minutes = minutes;
    }

    /**
     * Create an effort log by specifying a start date/time and an end date/time
     * @param person UID the peron logging
     * @param comment Comment about the logged effort
     * @param startDate Date/time the effort began
     * @param endDate Date/time the effort ceased
     */
    public Effort(String person, String comment, LocalDateTime startDate, LocalDateTime endDate) {
        this.person = person;
        this.comment = comment;
        this.startDate = startDate.toString();
        this.minutes = (int) Duration.between(startDate, endDate).toMinutes();
    }

    /**
     * Get the person who performed the effort
     * @return the person
     */
    public String getPerson() {
        return person;
    }

    /**
     * Get the effort comment
     * @return the effort comment
     */
    public String getComment() {
        return comment;
    }

    /**
     * Get teh start date of the effort
     * @return the startdate
     */
    public String getStartDate() {
        return startDate;
    }

    /**
     * The duration of the effort in minutes
     * @return the effort duration in minutes
     */
    public int getMinutes() {
        return minutes;
    }

}
