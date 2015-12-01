package seng302.group6.Model.ItemClasses;

import com.google.gson.annotations.Expose;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Model class to represent the amount of effort left on a task
 * Created by simon on 22/09/15.
 */
public class EffortLeft
{
    @Expose
    private String date;
    @Expose
    private Double effortLeft;

    /**
     * Create a log of effort left on a task by specifying the date the effort
     * left was changed and the new effort left
     * @param date when effort left was changed
     * @param effortLeft new effort left
     */
    public EffortLeft(LocalDateTime date, Double effortLeft)
    {
        this.date = date.toString();
        this.effortLeft = effortLeft;
    }

    /**
     * Get the date the effort was last changed
     * @return date string
     */
    public String getDate()
    {
        return date;
    }

    /**
     * The amount of effort left to complete the task
     * @return amount of effort left in minutes
     */
    public Double getEffortLeft()
    {
        return effortLeft;
    }
}
