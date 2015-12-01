package seng302.group6.Model.ItemClasses;

import org.joda.time.DateTime;
import com.google.gson.annotations.Expose;
import seng302.group6.Model.ItemType;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Model class to represent a Sprint
 * Created by dan on 20/07/15.
 */
public class Sprint extends Item {

    @Expose private String description;
    @Expose private String fullName;
    @Expose private String associatedBacklog;
    @Expose private String associatedTeam;
    @Expose private String associatedProject;
    @Expose private String associatedRelease;
    @Expose private ArrayList<String> stories;
    @Expose private String startDate;
    @Expose private String endDate;
    @Expose private boolean settingUp;

    /**
     * Sprint Object
     * @param shortName shortName of the sprint
     * @param fullName fullName of the sprint
     * @param description description of the sprint
     */
    public Sprint(String shortName, String fullName, String description)
    {
        this.shortName = shortName;
        this.description = description;
        this.fullName = fullName;
        this.associatedBacklog = null;
        this.associatedProject = null;
        this.associatedRelease = null;
        this.stories = new ArrayList<>();
        this.startDate = null;
        this.endDate = null;
        this.setSettingUpOn();
    }

    public Sprint(String shortName, String fullName, String description, String project,
                  String release, String startDate, String endDate, String team){
        this.shortName = shortName;
        this.description = description;
        this.fullName = fullName;
        this.associatedProject = Workspace.getProjectID(project);
        this.associatedRelease = Workspace.getReleaseID(release);
        this.associatedBacklog = null;
        this.stories = new ArrayList<>();
        this.associatedTeam = Workspace.getTeamID(team);
        this.startDate = startDate;
        this.endDate = endDate;
        this.setSettingUpOn();
    }

    /**
     * Returns the description of this Sprint
     * @return Description as a String
     */
    public String getDescription() { return description; }

    /**
     * Sets the Description for this Sprint
     * @param description Description as a String
     */
    public void setDescription(String description)
    {
        this.description = description;
    }

    /**
     * Returns the FullName of the Sprint
     * @return the full name as a String
     */
    public String getFullName() { return fullName; }

    /**
     * Sets the Fullname of the Sprint
     * @param fullName String containing the full name
     */
    public void setFullName(String fullName)
    {
        this.fullName = fullName;
    }

    /**
     * Returns the Associated Backlog of this Sprint
     * @return the UID of the backlog
     */
    public String getAssociatedBacklog() { return associatedBacklog; }

    /**
     * Sets the Backlog that this Sprint is associated to.
     * @param associatedBacklog the uid of the backlog this sprint is associated to
     */
    public void setAssociatedBacklog(String associatedBacklog)
    {
        this.associatedBacklog = associatedBacklog;
    }

    /**
     * Returns the Team associated to this Sprint
     * @return the UID of the team associated to this sprint
     */
    public String getAssociatedTeam() { return associatedTeam; }

    /**
     * Sets the Team associated with this Sprint
     * @param associatedTeam the UID of the team
     */
    public void setAssociatedTeam(String associatedTeam)
    {
        this.associatedTeam = associatedTeam;
    }

    /**
     * Returns the project associated with this Sprint
     * @return the UID of the project
     */
    public String getAssociatedProject() { return associatedProject; }

    /**
     * Sets the project associated with this Sprint
     * @param associatedProject the UID of the project
     */
    public void setAssociatedProject(String associatedProject)
    {
        this.associatedProject = associatedProject;
    }

    /**
     * Returns the Release associated with this Sprint
     * @return the UID of the Release
     */
    public String getAssociatedRelease() { return associatedRelease; }

    /**
     * Sets the Release associated with this Sprint
     * @param associatedRelease the UID of the Release
     */
    public void setAssociatedRelease(String associatedRelease)
    {
        this.associatedRelease = associatedRelease;
    }

    /**
     * Returns Stories in this Sprint
     * @return ArrayList containing UID Strings of the stories in the Sprint
     */
    public ArrayList<String> getStories() { return stories; }

    /**
     * Adds a story to the current sprints stories list
     * First checks if the uid is a valid story, then adds it to the arrayList
     * @param uid of the story to be added to the sprint
     */
    public void addStory(String uid)
    {
        Story story = Workspace.getStory(uid);
        if (story != null) {
            stories.add(uid);
        }
        story.inSprint = true;

    }

    /**
     * Removes a story from the current sprints stories list, and deletes all tasks in the story
     * First checks if the uid is a valid story, then removes it from the arrayList
     * @param uid of the story to be removed from the sprint
     */
    public void removeStory(String uid)
    {
        Story story = Workspace.getStory(uid);
        if (story != null) {
            stories.remove(uid);
        }

        story.inSprint = false;
    }

    /**
     * Returns the Start date for the Sprint
     * @return start date as a String
     */
    public String getStartDate() { return startDate; }

    /**
     * Sets the Start date for this Sprint
     * @param startDate start date as a String
     */
    public void setStartDate(String startDate)
    {
        this.startDate = startDate;
    }

    /**
     * Returns the End date for the Sprint
     * @return end date as a String
     */
    public String getEndDate() { return endDate; }

    /**
     * Sets the End Date for this Sprint
     * @param endDate end date as a String
     */
    public void setEndDate(String endDate)
    {
        this.endDate = endDate;
    }

    /**
     * get the settingUp mode
     * @return true for settingUp, false for not settingUp
     */
    public boolean getSettingUp() { return this.settingUp; }

    /**
     * Set setup mode to off
     */
    public void setSettingUpOff() { this.settingUp = false; }

    /**
     * Set setup mode to on
     */
    public void setSettingUpOn() {
        for (String story : (ArrayList<String>) getStories().clone()) {
            removeStory(story);
        }

        this.settingUp = true;
    }

    /**
     * Gets all of the effort logged for this sprint
     * @return a list containing all effort
     */
    public HashMap<String, Double> getEffortList() {
        ArrayList<Effort> sprintEffort = new ArrayList<>();
        for (String storyUid : this.getStories()) {
            Story story = Workspace.getStory(storyUid);
            for (Effort effort : story.getEffortList()) {
                sprintEffort.add(effort);
            }
        }
        return createDailyEffortList(sprintEffort);
    }

    /**
     * Goes through the effort given to it in an Arraylist and sorts the effort by day, and then accumulates the data
     * as the duration of the sprint goes on.
     * @param sprintEffort an arrayList containing the effort within the sprint
     * @return a HashMap with keys from sprint start date to end date, with the accumulated effort time so far in hours
     * for each day. key = "YYYY-MM-dd", value = double(effort in hours)
     */
    public HashMap<String, Double> createDailyEffortList(ArrayList<Effort> sprintEffort)
    {
        HashMap<String, Double> dateEffortMap = new HashMap<>();

        for (Effort effort : sprintEffort) {
            String date = LocalDateTime.parse(effort.getStartDate()).toLocalDate().toString();
            if (!dateEffortMap.containsKey(date)) {
                dateEffortMap.put(date, 0.0);
            }
            dateEffortMap.put(date, dateEffortMap.get(date) + effort.getMinutes());
        }

        HashMap<String, Double> effortByDay = new HashMap<>();

        LocalDate iterateDate = LocalDate.parse(this.startDate);
        LocalDate lastDay = LocalDate.parse(this.endDate);

        Double totalTime = 0.0;

        while (iterateDate.compareTo(lastDay) <= 0) {
            String stringDate = iterateDate.toString();
            if (dateEffortMap.containsKey(stringDate)) {
                totalTime += dateEffortMap.get(stringDate);
            }
            effortByDay.put(stringDate, totalTime);
            iterateDate = iterateDate.plusDays(1);
        }

        for (Map.Entry<String, Double> entry : effortByDay.entrySet()) {
            effortByDay.put(entry.getKey(), (entry.getValue()/60));
        }

        return effortByDay;
    }


    public HashMap<String, Double> getEffortLeftList() {
        HashMap<String, Double> effortLeftList = new HashMap<>();

        for (String storyID : getStories()) {
            Story story = Workspace.getStory(storyID);
            HashMap<LocalDate, Double> storyLeft = story.getEffortLeftList();
            for (LocalDate date : storyLeft.keySet()) {
                String stringDate = date.toString();
                if (effortLeftList.containsKey(stringDate)) {
                    effortLeftList.put(stringDate, effortLeftList.get(stringDate) + storyLeft.get(date));
                }
                else {
                    effortLeftList.put(stringDate, storyLeft.get(date));
                }
            }
        }

        HashMap<String, Double> effortLeftByDay = new HashMap<>();

        LocalDate iterateDate = LocalDate.parse(this.startDate);
        LocalDate lastDay = LocalDate.parse(this.endDate);

        Double totalTime = 0.0;

        while (iterateDate.compareTo(lastDay) <= 0) {
            String stringDate = iterateDate.toString();
            if (effortLeftList.containsKey(stringDate)) {
                totalTime = effortLeftList.get(stringDate);
            }
            effortLeftByDay.put(stringDate, totalTime);
            iterateDate = iterateDate.plusDays(1);
        }

        return effortLeftByDay;
    }


    @Override
    protected boolean searchAllFields(String searchTerm) {
        return searchField(searchTerm, getDescription()) ||
                searchField(searchTerm, getFullName());
    }

    /**
     * Returns the sprint item type
     * @return the sprint item type
     */
    @Override
    public ItemType type()
    {
        return ItemType.SPRINT;
    }

    /**
     * Returns a string to be shown in the search pane
     * @return search string
     */
    @Override
    public String searchString()
    {
        return shortName + " (Sprint)\nFull Name: " +
                (fullName.length() > 30 ? fullName.substring(0, 30) + "..." : fullName)
                + "    Description: " +
                (description.length() > 30 ? description.substring(0, 30) + "..." : description);
    }

    /**
     * Get the the total estimated time from all stories in the sprint
     * @return Total effort as a double
     */
    public Double getEstimation() {
        Double out = 0.0;
        for (String story : stories) {
            out += Workspace.getStory(story).getTaskEstimation();
        }
        return out;
    }

}
