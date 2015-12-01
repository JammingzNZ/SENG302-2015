package seng302.group6.Model.ItemClasses;

import com.google.gson.annotations.Expose;
import seng302.group6.Model.Status.StatusType;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

/**
 * Model class to represent a task
 * Created by Michael Wheeler on 24/07/15.
 */
public class Task {

    @Expose String shortName;

    @Expose String description;

    @Expose StatusType status;

    @Expose Double estimation;

    @Expose Double effortLeft;

    @Expose Double effort;

    @Expose ArrayList<Effort> effortList;

    @Expose ArrayList<EffortLeft> effortLeftList;

    @Expose ArrayList<String> people;

    /**
     * Creates a new Task for a story
     * @param shortName the short name of the task
     * @param description the description of the task
     * @param estimation the estimation for the task
     */
    public Task(String shortName, String description, Double estimation){
        if(shortName == null){
            throw new IllegalArgumentException("Short Name required");
        }
        this.shortName = shortName;
        this.description = description;
        this.status = StatusType.NOT_STARTED;
        this.estimation = estimation;
        this.effort = 0.0;
        this.effortLeft = estimation;
        this.effortList = new ArrayList<>();
        this.effortLeftList = new ArrayList<>();
        this.people = new ArrayList<>();
    }

    /**
     * Returns the short name of the task
     * @return short name
     */
    public String getShortName() {
        return shortName;
    }

    /**
     * Sets the short name of the task
     * @param shortName new short name
     */
    public void setShortName(String shortName) {
        if(shortName == null){
            throw new IllegalArgumentException("Short Name required");
        }else{
            this.shortName = shortName;
        }
    }

    /**
     * Returns the description of the task
     * @return description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the description of the task
     * @param description new description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Returns the task's current status
     * @return status
     */
    public StatusType getStatus() {
        return status;
    }

    /**
     * Sets the task status
     * @param status status to be set
     */
    public void setStatus(StatusType status) {
        this.status = status;
    }

    /**
     * Returns the task estimation
     * @return estimation
     */
    public Double getEstimation() {
        return estimation;
    }

    /**
     * Sets the task estimation
     * @param estimation estimation
     */
    public void setEstimation(Double estimation) {
        this.estimation = estimation;
    }

    /**
     * Set the tasks's effort left - NOT THE SAME AS ESTIMATION
     * @param effortLeft effort left
     */
    public void setEffortLeft(Double effortLeft) {
        this.effortLeft = effortLeft;
    }

    /**
     * Get the tasks's effort left - NOT THE SAME AS ESTIMATION
     * @return effort left
     */
    public Double getEffortLeft() {
        return effortLeft;
    }

    /**
     * Gets the effort logged on the task
     * @return effort as a Double
     */
    public Double getEffort()
    {
        return effort;
    }

    /**
     * Return the list of effort logs for this task
     * @return the list of effort logs for this task
     */
    public ArrayList<Effort> getEffortList() {
       if (effortList == null) {
            effortList = new ArrayList<Effort>();
        }
        return effortList;
    }

    /**
     * Clear effort
     * sets the effort to 0.0
     */
    public void clearEffort() {
        effortList.clear();
        effort = 0.0;
        effortLeftList.clear();
        effortLeft = estimation;
    }

    /**
     * Add a new effort log to the effort list
     * @param effort Effort log to add
     */
    public void addEffort(Effort effort) {
        effortList.add(effort);
        this.effort += (double)Math.round((effort.getMinutes() / 60.0) * 100d) / 100d;
    }

    /**
     * Remove an effort log from the effort list
     * @param effort Effort log to remove
     */
    public void removeEffort(Effort effort)
    {
        effortList.remove(effort);
        this.effort -= (double) Math.round((effort.getMinutes() / 60.0) * 100d) / 100d;
    }

    /**
     * Add a new effortLeft log to the effortLeft list
     * @param effortLeft EffortLeft log to add
     */
    public void addEffortLeft(EffortLeft effortLeft) {
        effortLeftList.add(effortLeft);
    }

    /**
     * Remove an effortLeft log from the effortLeft list
     * @param effortLeft EffortLeft log to remove
     */
    public void removeEffortLeft(EffortLeft effortLeft)
    {
        effortLeftList.remove(effortLeft);
    }

    public ArrayList<EffortLeft> getEffortLeftList()
    {
        if (effortLeftList == null) {
            effortLeftList = new ArrayList<EffortLeft>();
        }
        return effortLeftList;
    }

    /**
     * Return a list of the effort left, using only the most recent effort left for each day.
     * @return hashmap of effort left per day
     */
    public HashMap<LocalDate, EffortLeft> getEffortLeftPerDay() {
        HashMap<LocalDate, EffortLeft> effortLeftMap = new HashMap<>();

        for (EffortLeft effortLeft : getEffortLeftList()) {
            LocalDateTime dateTime = LocalDateTime.parse(effortLeft.getDate());
            EffortLeft compLeft = effortLeftMap.get(dateTime.toLocalDate());
            if (compLeft != null) {
                LocalTime compTime = LocalDateTime.parse(compLeft.getDate()).toLocalTime();
                if (dateTime.toLocalTime().isAfter(compTime)) {
                    effortLeftMap.put(dateTime.toLocalDate(), effortLeft);
                }
            }
            else {
                effortLeftMap.put(dateTime.toLocalDate(), effortLeft);
            }
        }

        return effortLeftMap;
    }

    /**
     * Get the people allocated to this task
     * @return an ArrayList of the people stored as UID's
     */
    public ArrayList<String> getPeople() {
        return people;
    }

    /**
     * Set the people allocated to this task
     * @param people an ArrayList of people to set to this task
     */
    public void setPeople(ArrayList<String> people) {
        this.people = people;
    }

    /**
     * Add Person to this task
     * @param person uid of the person to add
     */
    public void addPerson(String person) {
        // Stops bug where it tries to add an item to a null List.
        // Think it's to do with List not being initialized when loading to
        // @Exposed properties
        if (people == null)
            people = new ArrayList<>();
        people.add(person);
    }

    /**
     * Move a person to a different place in the people list.
     * @param index Index to move the person to
     * @param person person to move
     */
    public void moveTask(int index, String person) {
        people.remove(person);
        people.add(index, person);
    }

    /**
     * Delete a given Person.
     * @param person Person uid to remove
     */
    public void removePerson(String person) {
        people.remove(person);
    }

    /**
     * Returns the associated story
     * @return the associated story
     */
    public Story getStory()
    {
        ArrayList<String> storyUids = Workspace.getStories();

        Story result = null;
        for (String uid: storyUids) {
            Story story = Workspace.getStory(uid);
            if (story.getAllTasks().contains(this)) {
                result = story;
                break;
            }
        }

        return result;
    }

}
