package seng302.group6.Model.ItemClasses;

import com.google.gson.annotations.Expose;
import seng302.group6.Model.CyclicDependencyException;
import seng302.group6.Model.EstimationScale.EstimationScale;
import seng302.group6.Model.EstimationScale.ScaleType;
import seng302.group6.Model.ItemType;
import seng302.group6.Model.Status.StatusType;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * Created by dan on 20/05/15.
 *
 * Model class to represent a story.
 * Contains information about Stories
 */
public class Story extends Item implements Comparable<Story> {

    @Expose String longName;

    @Expose String description;

    @Expose String creatorName;

    @Expose String creatorUID;

    @Expose
    List<AcceptanceCriteria> acceptanceCriterias;

    @Expose private String backlog;

    @Expose
    ScaleType scaleType;
    @Expose
    Integer estimate;
    @Expose
    private String estimateRep;

    @Expose Integer currentPriority;

    @Expose Boolean ready;

    //Stories that this story depends on
    @Expose
    Set<String> dependencies;

    //Stories that depend on this story
    @Expose
    Set<String> dependents;

    @Expose
    List<Task> tasks;

    @Expose
    StatusType state;

    @Expose public boolean inSprint;


    /**
     * Creates a Story item
     * @param shortName The sortname of the story
     * @param longName The long name of the story
     * @param description The description of the story
     * @param creator The UID of the person who created the story
     */
    public Story(String shortName, String longName, String description, String creator)
    {
        this.shortName = shortName;
        this.longName = longName;
        this.description = description;

        this.creatorUID = creator;
        this.creatorName = Workspace.getPersonName(creator);

        this.acceptanceCriterias = new ArrayList<>();
        this.dependencies = new HashSet<>(); // Stories that this story depends on
        this.dependents = new HashSet<>();  //Stories that depend on this story

        this.currentPriority = null;

        this.scaleType = ScaleType.PSEUDOFIBONACCI;
        this.tasks = new ArrayList<>();

        this.state = StatusType.NOT_STARTED;
        this.ready = false;
        this.inSprint = false;

    }

    /**
     * Gets the longname of the story
     * @return the longnameof the story
     */
    public String getLongName() { return longName; }

    /**
     * Sets the longname of the story
     * @param longName longname
     */
    public void setLongName(String longName) { this.longName = longName; }

    /**
     * Gets the description of the story
     * @return the descrition of the story
     */
    public String getDescription() { return description; }

    /**
     * Sets the description of the story
     * @param description description
     */
    public void setDescription(String description) { this.description = description; }

    /**
     * Sets the current priority of the story
     * @param priority priority
     */
    public void setCurrentPriority(Integer priority) { this.currentPriority = priority; }

    /**
     * Gets the current priority of the story
     * @return the current priority of the story
     */
    public Integer getCurrentPriority(){ return currentPriority; }

    /**
     * Check if the story can be readied. Throws an Exception if it cannot.
     */
    public void canBeReadied() {
        if (estimate == null) {
            throw new IllegalArgumentException("Story must be estimated before it can be ready");
        }
        if (!isInBacklog()) {
            throw new IllegalArgumentException("Story must be in backlog before it can be ready");
        }
    }

    /**
     * Sets the "Readiness" of the Story to "Ready"
     */
    public void setReadinessToReady() {
        canBeReadied();
        this.ready = true;
    }

    /**
     * Sets the "Readiness" of the Story to "Not Ready"
     */
    public void setReadinessToNotReady(){
        this.ready = false;
    }

    /**
     * Gets the "Readiness" state of the story
     * @return readiness boolean
     */
    public Boolean getReadiness(){ return ready; }


    /**
     * Set the backlog for the story
     * Determines whether a story is in a backlog
     * @param backlog backlog this story is in
     */
    public void addedToBacklog(String backlog){
        this.backlog = backlog;
    }

    /**
     * Removes the story from the backlog (sets backlog to null)
     */
    public void removedFromBacklog() {
        setReadinessToNotReady();
        this.backlog = null;
    }

    /**
     * Returns inBacklog boolean
     * @return inBacklog - whether the story is in a backlog
     */
    public Boolean isInBacklog(){
        return backlog != null;
    }

    /**
     * Get the creators name based on the creators UID.
     * If the Creator has changed their name it will be updated before returning,
     * If the Creator no longer exists, it will return the last known name.
     * @return Creators Name as a string
     */
    public String getCreatorName()
    {
        if (Workspace.getPerson(creatorUID)!=null) {
            this.creatorName = Workspace.getPersonName(creatorUID);
            return creatorName;
        }
        return creatorName + " (Deleted)";
    }

    /**
     * Add acceptance criteria.
     * @param AC AcceptanceCriteria to add
     */
    public void addAcceptanceCriteria(AcceptanceCriteria AC) {
        // Stops bug where it tries to add an item to a null List.
        // Think it's to do with List not being initialized when loading to
        // @Exposed properties
        if (acceptanceCriterias == null)
            acceptanceCriterias = new ArrayList<>();

        acceptanceCriterias.add(AC);
    }

    /**
     * Delete a given acceptance criteria.
     * @param AC AcceptanceCriteria to delete
     */
    public void deleteAcceptanceCriteria(AcceptanceCriteria AC) {
        acceptanceCriterias.remove(AC);
        if (acceptanceCriterias.isEmpty()) {
            setEstimate(null);
        }
    }

    /**
     * Move an acceptance criteria to a different place in the AC list.
     * @param index Index to move the AC to
     * @param AC AcceptanceCriteria to add
     */
    public void moveAcceptanceCriteria(int index, AcceptanceCriteria AC) {
        acceptanceCriterias.remove(AC);
        acceptanceCriterias.add(index, AC);
    }

    /**
     * Get the acceptance criteria at a given place in the AC list
     * @param index Index to get the AC from
     * @return AcceptanceCriteria at the specified index
     */
    public AcceptanceCriteria getAcceptanceCriteria(int index) {
        return acceptanceCriterias.get(index);
    }

    /**
     * Get the list of all acceptance criteria
     * @return ArrayList containing all ACs
     */
    public List<AcceptanceCriteria> getAllAcceptanceCriteria() {
        return acceptanceCriterias;
    }

    /**
     * Get the representation of te estimate based on the current scale
     * @return String indicating the estimate
     */
    public String getEstimateRep() {
        return scaleType.getScale().getRepresentation(estimate);
    }

    /**
     * Get the integer value for the estimate
     * @return Integer value for the estimate
     */
    public Integer getEstimate() {
        return estimate;
    }

    /**
     * Set the integer value for the estimate
     * @param estimate Value to set the estimate to
     */
    public void setEstimate(Integer estimate) {
        if (estimate == null || estimate == -1) {
            setReadinessToNotReady();
            this.estimate = null;
        }
        else {
            if (acceptanceCriterias.isEmpty()) {
                throw new IllegalArgumentException("A Story must have at least one AC to be estimated");
            }
            if (estimate >= 0 && estimate <= 11) {
                this.estimate = estimate;
            } else {
                throw new IllegalArgumentException("Value must be between 0 and 11");
            }
        }
        estimateRep = getEstimateRep();
    }

    /**
     * Set the type of estimation scale to use for this story
     * @param scaleType ScaleType to use
     */
    public void setScaleType(ScaleType scaleType) {
        this.scaleType = scaleType;
        estimateRep = getEstimateRep();
    }

    /**
     * Get the scale type for this story
     * @return ScaleType for this story
     */
    public ScaleType getScaleType() {
        return scaleType;
    }

    /**
     * Get the EstimationScale object that corresponds to this story's scale type
     * @return EstimationScale object for the story
     */
    public EstimationScale getEstimationScale() {
        return scaleType.getScale();
    }

    /**
     * Get the backlog the story is in
     * @return String representing the ID of the story's backlog, null if it isn't in one
     */
    public String getBacklog() {
        return backlog;
    }

    /**
     * Add a story as a dependency
     * @param dep Story to make dependent
     * @throws seng302.group6.Model.CyclicDependencyException if adding this dependency would create a cyclic dependency
     */
    public void addDependency(String dep) throws CyclicDependencyException {
        //Adds this story to the dependents set of it's dependency
        Story s = Workspace.getStory(dep);

        if (checkCycle(s.uid())) {
            throw new CyclicDependencyException();
        }
        else {
            this.dependencies.add(dep);
            s.addDependent(this.uid);
        }
    }

    /**
     * removes a story from the dependency set.
     *
     * @param dep - the uid of the story to remove.
     */
    public void removeDependency(String dep) {
        this.dependencies.remove(dep);
        Workspace.getStory(dep).removeDependent(this.uid);
    }

    /**
     * Adds a story as a dependent
     * @param uid The uid of the Story being added to the dependents
     */
    public void addDependent(String uid) {
        this.dependents.add(uid);
    }

    /**
     * Removes the story as a dependent
     * @param uid The uid of the story that is being removed as a dependent
     */
    public void removeDependent(String uid) {
        this.dependents.remove(uid);
    }

    /**
     *
     * @return the set of Dependent stories on this story
     */
    public Set<String> getDependents() {
        return dependents;
    }
    /**
     * tests that the story can be added as a dependency
     * based on its priority and the priority of this story.
     *
     * to be a dependency, neither story may have a null priority, and the dependency must have a higher priority than this.
     *
     * @param dep - the story to check.
     * @return returns true if the dependency has a suitable priority setting.
     */
    public Boolean dependencyHasSuitablePriority(Story dep) {
        return this.currentPriority != null && dep.getCurrentPriority() != null && dep.getCurrentPriority() > this.currentPriority;
    }

    /**
     * Tests whether any of this story's dependencies have an invalid priority when compared with this story
     * @return True if all dependencies have a suitable priority, false otherwise
     */
    public Boolean dependenciesHaveSuitablePriority() {
        for (String storyID : this.getDependencies()) {
            if (!dependencyHasSuitablePriority(Workspace.getStory(storyID))) {
                return false;
            }
        }
        return true;
    }

    /**
     * Tests whether any of this story's dependencies have an invalid priority
     * when compared with this story
     * @return A list of all dependencies with priority conflicts
     */
    public ArrayList<String> priorityConflicts()
    {
        ArrayList<String> conflictingStories = new ArrayList<>();
        for (String storyID : this.getDependencies()) {
            if (!dependencyHasSuitablePriority(Workspace.getStory(storyID))) {
                conflictingStories.add(storyID);
            }
        }
        return conflictingStories;
    }

    /**
     * dependency set getter.
     * @return the set of dependencies
     */
    public Set<String> getDependencies(){
        return this.dependencies;
    }

    public StatusType getState() {
        return state;
    }

    public void setState(StatusType state) {
        this.state = state;
    }

    /**
     * Test whether any cyclic dependencies would exist if a given story was added
     * @param story Story (as an ID) to check for cycle causing
     * @return Boolean indicating whether there would be any cycles
     */
    public boolean checkCycle(String story) {
        //There should be no cycle if the story is already a dependency
        if (dependencies.contains(story)) {
            return false;
        }

        dependencies.add(story);

        //Depth First Search
        Stack<ArrayList<Story>> storyStack = new Stack<>();
        ArrayList<Story> startList = new ArrayList<>();
        startList.add(this);
        storyStack.add(startList);

        while (!storyStack.isEmpty()) {
            ArrayList<Story> currentPath = storyStack.pop();
            Story currentStory = currentPath.get(currentPath.size() - 1);

            for (String storyID : currentStory.getDependencies()) {
                Story thisStory = Workspace.getStory(storyID);
                if (currentPath.contains(thisStory)) {
                    //Cycle detected
                    dependencies.remove(story);
                    return true;
                }
                else {
                    ArrayList<Story> newPath = (ArrayList) currentPath.clone();
                    newPath.add(thisStory);
                    storyStack.add(newPath);
                }
            }
        }

        //No cycle detected
        dependencies.remove(story);
        return false;
    }

    /**
     * Add task
     * @param task task to add
     */
    public void addTask(Task task) {
        // Stops bug where it tries to add an item to a null List.
        // Think it's to do with List not being initialized when loading to
        // @Exposed properties
        if (tasks == null)
            tasks = new ArrayList<>();
        tasks.add(task);
    }

    /**
     * Delete a given task.
     * @param task Task to delete
     */
    public void deleteTask(Task task) {
        tasks.remove(task);
    }

    /**
     * Move an task to a different place in the tasks list.
     * @param index Index to move the task to
     * @param task Task to move
     */
    public void moveTask(int index, Task task) {
        tasks.remove(task);
        tasks.add(index, task);
    }

    /**
     * Get the task at a given place in the task list
     * @param index Index to get the task from
     * @return Task at the specified index
     */
    public Task getTask(int index) {
        return tasks.get(index);
    }

    /**
     * Get the list of all tasks
     * @return ArrayList containing all tasks
     */
    public List<Task> getAllTasks() {
        return tasks;
    }

    /**
     * Get the the total estimated time from all tasks in the story
     * @return Total effort as a double
     */
    public Double getTaskEstimation() {
        Double out = 0.0;
        for (Task task : tasks) {
            out += task.getEstimation();
        }
        return out;
    }

    @Override
    protected boolean searchAllFields(String searchTerm) {
        return searchField(searchTerm, getDescription()) ||
                searchField(searchTerm, getLongName());
    }

    /**
     * Returns the story item type
     * @return the story item type
     */
    @Override
    public ItemType type()
    {
        return ItemType.STORY;
    }

    /**
     * Returns a string to be shown in the search pane
     * @return search string
     */
    @Override
    public String searchString()
    {
        return shortName + " (Story)\nLong Name: " +
                (longName.length() > 30 ? longName.substring(0, 30) + "..." : longName)
                + "    Description: " +
                (description.length() > 30 ? description.substring(0, 30) + "..." : description);
    }

    /**
     * Returns whether or not there are sprints for this story
     * @return true if a sprint uses this story, false otherwise
     */
    public Boolean hasSprint()
    {
        ArrayList<String> sprintUids = Workspace.getSprints();

        for (String sprintUid : sprintUids) {
            if (Workspace.getSprint(sprintUid).getStories() != null) {
                if (Workspace.getSprint(sprintUid).getStories().contains(uid())) {
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * Returns the uids of all sprints that are associated with this story.
     * @return list of sprint uids
     */
    public ArrayList<String> associatedSprints()
    {
        ArrayList<String> sprintUids = Workspace.getSprints();
        ArrayList<String> associatedSprints = new ArrayList<>();

        for (String sprintUid : sprintUids) {
            if (Workspace.getSprint(sprintUid).getStories() != null) {
                if (Workspace.getSprint(sprintUid).getStories().contains(uid())) {
                    associatedSprints.add(sprintUid);
                }
            }
        }

        return associatedSprints;
    }

    /**
     * Returns a list containing all effort from all tasks within this story
     * @return list of all effort on this story
     */
    public ArrayList<Effort> getEffortList()
    {
        ArrayList<Effort> storyEffort = new ArrayList<>();
        for (Task task : this.getAllTasks()) {
            for (Effort effort : task.getEffortList()) {
                storyEffort.add(effort);
            }
        }
        return storyEffort;
    }

    /**
     * Returns a list containing all effort left from all tasks within this story
     * @return list of all effort left on this story
     */
    public HashMap<LocalDate, Double> getEffortLeftList() {
        HashMap<LocalDate, Double> storyEffortLeft = new HashMap<>();

        for (Task task : this.getAllTasks()) {
            HashMap<LocalDate, EffortLeft> taskEffortLeft = task.getEffortLeftPerDay();
            for (LocalDate date : taskEffortLeft.keySet()) {
                EffortLeft effortLeft = taskEffortLeft.get(date);
                if (storyEffortLeft.containsKey(date)) {
                    storyEffortLeft.put(date, storyEffortLeft.get(date) + effortLeft.getEffortLeft());
                }
                else {
                    storyEffortLeft.put(date, effortLeft.getEffortLeft());
                }
            }
        }

        return storyEffortLeft;
    }

    /**
     * Get the current total effort left from all tasks in this story
     * @return Double of the total effort left
     */
    public Double getEffortLeft()
    {
        Double effortLeft = 0.0;
        for (Task task : this.getAllTasks()) {
            effortLeft += task.getEffortLeft();
        }

        return effortLeft;
    }

    /**
     * Gets the total effort spent on this story from all tasks
     * @return effort spend double
     */
    public Double getEffortSpent()
    {
        Double effortSpent = 0.0;
        for (Effort effort : getEffortList()) {
            effortSpent += effort.getMinutes();
        }
        return effortSpent;
    }

    /**
     * Get the current progress of this story as a decimal
     * Based off a ratio of time left : Time spent
     * 1.0 = 100%
     * 0.0 = 0%
     * 0.5 = 50%
     * @return a double of the progress of this story
     */
    public Double getProgress() {
        Double effortLeft = this.getEffortLeft() * 60;

        if ((effortLeft + this.getEffortSpent()) == 0.0) {
            return 0.0;
        }

        Double progress = ((this.getEffortSpent()) / (effortLeft + this.getEffortSpent()));
        return progress;
    }

    /**
     * Get the tooltip string based on this stories progress
     * format:
     * Progress: XX%
     * Effort Spent: X Hour(s) and X Minute(s)
     * Effort Left: X Hour(s) and X Minute(s)
     * @return the string containing the stories progress details
     */
    public String getProgressTooltipString()
    {
        Double effortLeft = this.getEffortLeft() * 60;
        DecimalFormat df = new DecimalFormat("#.##");
        long spentHours = TimeUnit.MINUTES.toHours(this.getEffortSpent().intValue());
        long spentMinutes = this.getEffortSpent().intValue() - TimeUnit.HOURS.toMinutes(spentHours);
        long leftHours = TimeUnit.MINUTES.toHours(effortLeft.intValue());
        long leftMinutes = effortLeft.intValue() - TimeUnit.HOURS.toMinutes(leftHours);

        return "Progress: " + df.format(this.getProgress() * 100) + "%\n" +
                "Effort Spent: " + spentHours + " hour(s) and " + spentMinutes + " minute(s)\n" +
                "Effort Left: " + leftHours + " hour(s) and " + leftMinutes + " minute(s)\n";
    }

    @Override
    public int compareTo(Story other) {
        Integer thisPriority = getCurrentPriority();
        Integer otherPriority = other.getCurrentPriority();

        if (thisPriority == otherPriority) {
            return getShortName().compareTo(other.getShortName());
        }
        else if (thisPriority == null) {
            return -1;
        }
        else if (otherPriority == null) {
            return 1;
        }

        return thisPriority.compareTo(otherPriority);
    }
}
