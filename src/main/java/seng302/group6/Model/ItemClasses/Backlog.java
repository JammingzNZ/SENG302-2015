package seng302.group6.Model.ItemClasses;

import com.google.gson.annotations.Expose;
import seng302.group6.Model.EstimationScale.EstimationScale;
import seng302.group6.Model.EstimationScale.ScaleType;
import seng302.group6.Model.ItemType;

import java.util.ArrayList;
import java.util.HashSet;


/**
 * Model class to represent a Backlog.
 *
 */
public class Backlog extends Item
{
    @Expose String fullName;
    @Expose String description;
    @Expose String productOwner;
    @Expose ArrayList<String> stories = new ArrayList<>();
    @Expose private ScaleType scaleType;
    @Expose private HashSet<String> projects;

    /**
     * Creates a new Backlog
     *
     * @param shortName the short name of the backlog.
     * @param fullName the full name of the backlog.
     * @param description the description of the backlog.
     * @param productOwner the product owner of the backlog.
     */
    public Backlog(String shortName, String fullName, String description, String productOwner)
    {
        this.shortName = shortName;
        this.fullName = fullName;
        this.description = description;
        this.productOwner = productOwner;
        this.projects = new HashSet<>();

        this.scaleType = ScaleType.PSEUDOFIBONACCI;
    }

    /**
     * Backlog full name getter
     *
     * @return the full name of the backlog.
     */
    public String getFullName()
    {
        return fullName;
    }

    /**
     * Backlog full name setter
     *
     * @param fullName the full name of the backlog.
     */
    public void setFullName(String fullName)
    {
        this.fullName = fullName;
    }

    /**
     * Backlog description getter
     *
     * @return the description of the backlog.
     */
    public String getDescription()
    {
        return description;
    }

    /**
     * Backlog description setter
     *
     * @param description is the description of the backlog.
     */
    public void setDescription(String description)
    {
        this.description = description;
    }

    /**
     *Backlog product owner getter
     *
     * @return the product owner of the backlog.
     */
    public String getProductOwner()
    {
        return productOwner;
    }

    /**
     * Backlog product owner setter
     *
     * @param productOwner is the product owner of the backlog.
     */
    public void setProductOwner(String productOwner)
    {
        this.productOwner = productOwner;
    }

    /**
     * Backlog story getter
     *
     * @return the unique ids of the stories of the backlog, ordered by priority.
     */
    public ArrayList<String> getStories() {return stories; }

    /**
     * Adds a story to the backlog.
     *
     * @param uid is the unique id of the story to add.
     * @param priority is the priority of the story, and may be null, or 0+.
     */
    public void addStory(String uid, Integer priority)
    {
        Story story = Workspace.getStory(uid);
        story.setCurrentPriority(priority);
        this.stories.add(uid);
        story.addedToBacklog(this.uid);
        story.setScaleType(this.scaleType);
    }


    /**
     * Removes a story from the backlog.
     *
     * @param uid is the unique id of the story to remove.
     * @throws java.lang.AssertionError - asserts that the story uid is present in this.stories.
     */
    public void removeStory(String uid){
        Workspace.getStory(uid).setCurrentPriority(null);
        this.stories.remove(uid);
        Workspace.getStory(uid).removedFromBacklog();
    }

    /**
     * gets the priority of a story by its uid.
     *
     * @param uid is the unique id of the story to get the priority of.
     * @return returns the priority of the specified story.
     * @throws java.lang.AssertionError - asserts that the story uid is present in this.stories.
     */
    public Integer getStoryPriority(String uid) {
        assert stories.contains(uid);
        return Workspace.getStory(uid).getCurrentPriority();
    }

    /**
     * sets the priority of a story by its uid.
     *
     * @param uid is the unique id of the story to get the priority of.
     * @param priority is the priority of the story, and may be null, or 0+.
     * @throws java.lang.AssertionError - asserts that the story uid is present in this.stories.
     */
    public void setStoryPriority(String uid, Integer priority) {
        assert stories.contains(uid);
        Workspace.getStory(uid).setCurrentPriority(priority);
    }

    /**
     * Set the type of estimation scale to use for this backlog
     * @param scaleType ScaleType to use
     */
    public void setScaleType(ScaleType scaleType) {
        this.scaleType = scaleType;
        for (String storyID : this.getStories()) {
            Story story = Workspace.getStory(storyID);
            story.setScaleType(this.scaleType);
        }
    }

    /**
     * Get the scale type for this backlog
     * @return ScaleType for this backlog
     */
    public ScaleType getScaleType() {
        return scaleType;
    }

    /**
     * Get the EstimationScale object that corresponds to this backlog's scale type
     * @return EstimationScale object for the backlog
     */
    public EstimationScale getEstimationScale() {
        return scaleType.getScale();
    }

    /**
     * Adds a project to the list of projects that this backlog is allocated to
     * @param project the uid of the project
     */
    public void allocateToProject(String project)
    {
        if (projects == null) {
            this.projects = new HashSet<>();
        }

        projects.add(project);
    }

    /**
     * Removes a project from the list of projects that this backlog is
     * allocated to
     * @param project the uid of the project
     */
    public void forgetProject(String project)
    {
        if (projects != null) {
            projects.remove(project);
        }
    }

    /**
     * Returns a list of project that this backlog is allocated to
     * @return list of project uids
     */
    public HashSet<String> getProjects()
    {
        return projects;
    }


    @Override
    protected boolean searchAllFields(String searchTerm) {
        return searchField(searchTerm, getDescription()) ||
                searchField(searchTerm, getFullName());
    }

    /**
     * Returns the backlog item type
     * @return the backlog item type
     */
    @Override
    public ItemType type()
    {
        return ItemType.BACKLOG;
    }

    /**
     * Returns a string to be shown in the search pane
     * @return search string
     */
    @Override
    public String searchString()
    {
        return shortName + " (Backlog)\nFull Name: " +
                (fullName.length() > 30 ? fullName.substring(0, 30) + "..." : fullName)
                + "    Description: " +
                (description.length() > 30 ? description.substring(0, 30) + "..." : description);
    }

    /**
     * Returns whether or not there are sprints for this backlog
     * @return true if a sprint uses this backlog, false otherwise
     */
    public Boolean hasSprint()
    {
        ArrayList<String> sprintUids = Workspace.getSprints();

        for (String sprintUid : sprintUids) {
            if (Workspace.getSprint(sprintUid).getAssociatedProject() != null) {
                Project project = Workspace.getProject(
                        Workspace.getSprint(sprintUid).getAssociatedProject()
                );
                if (project.getBacklog().equals(uid())) {
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * Returns the uids of all sprints that are associated with this backlog.
     * @return list of sprint uids
     */
    public ArrayList<String> associatedSprints()
    {
        ArrayList<String> sprintUids = Workspace.getSprints();
        ArrayList<String> associatedSprints = new ArrayList<>();

        for (String sprintUid : sprintUids) {
            if (Workspace.getSprint(sprintUid).getAssociatedProject() != null) {
                Project project = Workspace.getProject(
                        Workspace.getSprint(sprintUid).getAssociatedProject()
                );
                if (project.getBacklog().equals(uid())) {
                    associatedSprints.add(sprintUid);
                }
            }
        }

        return associatedSprints;
    }
}
