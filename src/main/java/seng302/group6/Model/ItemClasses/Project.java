package seng302.group6.Model.ItemClasses;

import com.google.gson.annotations.Expose;
import seng302.group6.Model.ItemType;

import java.time.LocalDate;
import java.util.ArrayList;

/**
 * Created by simon on 13/03/15.
 *
 * Model class to represent a project
 * Contains all information about a project
 */
public class Project extends Item
{
    @Expose private String longName;
    @Expose private String description;
    @Expose private String backlog;
    @Expose private ArrayList<String> releases;

    // Each allocated development team is stored as a hash map
    @Expose private ArrayList<Allocation> devTeams;

    /**
     * Creates new project with parameters provided
     * @param shortName unique identifier for the project
     * @param longName name of the project
     * @param description description of the project
     */
    public Project(String shortName, String longName, String description)
    {
        this.shortName = shortName;
        this.longName = longName;
        this.description = description;
        this.releases = new ArrayList<>();
        this.devTeams = new ArrayList<>();
    }

    /**
     * Gets the project description
     * @return project description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the project description
     * @param description project description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Gets the uid of the project's backlog
     * @return the uid of the project's backlog
     */
    public String getBacklog()
    {
        return backlog;
    }

    /**
     * Sets the uid of the project's backlog
     * @param backlog the uid of the project's backlog
     */
    public void setBacklog(String backlog)
    {
        this.backlog = backlog;
    }

    /**
     * Gets the long name of the project
     * @return project long name
     */
    public String getLongName() {
        return longName;
    }

    /**
     * Sets the long name of the project
     * @param longName project long name
     */
    public void setLongName(String longName) {
        this.longName = longName;
    }

    /**
     * Gets the Releases involved on the project
     * @return project releases
     */
    public ArrayList<String> getReleases() {
        return releases;
    }

    /**
     * Gets the releases that are not yet finished
     * @return a list of unfinished releases
     */
    public ArrayList<String> getUnfinishedReleases()
    {
        ArrayList<String> unfinishedReleases = new ArrayList<>();
        for (String release : releases) {
            Release r = Workspace.getRelease(release);
            LocalDate releaseDate = LocalDate.parse(r.getReleaseDate());
            if (releaseDate.isAfter(LocalDate.now())) {
                unfinishedReleases.add(release);
            }

        }
        return unfinishedReleases;
    }

    /**
     * Adds a release to the Project
     * @param uid is the unique id of the release
     */
    public void addRelease(String uid)
    {
        this.releases.add(uid);
    }

    /**
     * Removes a release from the Project
     * @param uid is the unique id of the release
     */
    public void removeRelease(String uid) {
        this.releases.remove(uid);
    }

    /**
     * Allocates a development team to the project
     * @param uid unique id of the team
     * @param startDate date to start the allocation
     * @param endDate date to end the allocation
     */
    public void addDevTeam(String uid, LocalDate startDate, LocalDate endDate)
    {
        Team team = Workspace.getTeam(uid);

        if (team != null) {
            Allocation allocation = new Allocation(uid, startDate, endDate,
                    Workspace.getProjectID(shortName));
            this.devTeams.add(allocation);
            team.setAllocatedProject(Workspace.getProjectID(shortName), allocation);
        }
    }

    /**
     * Deallocates a development team from the project
     * @param uid unique id of the team
     * @param start date the allocation starts
     * @param end date the allocation ends
     */
    public void removeDevTeam(String uid, LocalDate start, LocalDate end)
    {
        Team team = Workspace.getTeam(uid);
        if (team != null) {
            // Find the right Allocation

            Allocation removeMe = null;

            for (Allocation allocation: devTeams) {
                if (allocation.getTeamUID().equals(uid) &&
                        allocation.getStartDate().equals(start) &&
                        allocation.getEndDate().equals(end)) {
                    removeMe = allocation;
                    break;
                }
            }

            if (removeMe != null) {
                devTeams.remove(removeMe);
            }

            team.setAllocatedProject(null, null);
        }
    }

    /**
     * Get the uids of all dev teams for the project
     * @return ArrayList of uids
     */
    public ArrayList<Allocation> getDevTeams()
    {
        return devTeams;
    }

    /**
     * Returns a list of teams allocated to the project whose end dates are
     * after today.
     * @return list of Allocations
     */
    public ArrayList<Allocation> getCurrentDevTeams()
    {
        // TODO: make sure future allocations aren't included
        ArrayList<Allocation> currentTeams = new ArrayList<>();
        for (Allocation allocation: devTeams) {
            if (Workspace.getTeam(allocation.getTeamUID()).isAllocated()) {
                boolean add = true;
                for (Allocation a: currentTeams) {
                    if (allocation.getTeamUID().equals(a.getTeamUID())) {
                        add = false;
                        break;
                    }
                }
                if (add) {
                    currentTeams.add(allocation);
                }
            }
        }
        return currentTeams;
    }

    public void setDevTeams(ArrayList<Allocation> devTeams)
    {
        // I have commented out the below code because it didn't seem to be
        // doing anything other than causing a bug. I'll leave it here in case
        // it was there for a good reason.

        // Remove allocated project from old teams
        /*for (String teamUid: Workspace.getTeams()) {
            Team team = Workspace.getTeam(teamUid);
            if (team.isAllocated()) {
                team.setAllocatedProject(null, null);
            }
        }*/

        this.devTeams = devTeams;

        // Add allocated project to new dev teams
        if (this.devTeams != null) {
            for (Allocation allocation : this.devTeams) {
                Workspace.getTeam(allocation.getTeamUID()).setAllocatedProject(
                        Workspace.getProjectID(shortName), allocation);
                allocation.setProjectUID(uid());
            }
        }

    }

    /**
     * Makes sure each team has the correct allocation as its current one
     */
    public void updateTeams()
    {
        for (Allocation allocation: this.devTeams) {
            Team team = Workspace.getTeam(allocation.getTeamUID());

            if (!allocation.getEndDate().isBefore(LocalDate.now())) {
                team.setAllocatedProject(uid(), allocation);
            }
        }
    }


    @Override
    protected boolean searchAllFields(String searchTerm) {
        return searchField(searchTerm, getDescription()) ||
                searchField(searchTerm, getLongName());
    }

    /**
     * Returns the project item type
     * @return the project item type
     */
    @Override
    public ItemType type()
    {
        return ItemType.PROJECT;
    }

    /**
     * Returns a string to be shown in the search pane
     * @return search string
     */
    @Override
    public String searchString()
    {
        return shortName + " (Project)\nLong Name: " +
                (longName.length() > 30 ? longName.substring(0, 30) + "..." : longName)
                + "    Description: " +
                (description.length() > 30 ? description.substring(0, 30) + "..." : description);
    }

    /**
     * Returns whether or not there are sprints for this project
     * @return true if a sprint uses this project, false otherwise
     */
    public Boolean hasSprint()
    {
        ArrayList<String> sprintUids = Workspace.getSprints();

        for (String sprintUid : sprintUids) {
            if (Workspace.getSprint(sprintUid).getAssociatedProject() != null) {
                if (Workspace.getSprint(sprintUid).getAssociatedProject().equals(uid())) {
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * Returns the uids of all sprints that are associated with this project.
     * @return list of sprint uids
     */
    public ArrayList<String> associatedSprints()
    {
        ArrayList<String> sprintUids = Workspace.getSprints();
        ArrayList<String> associatedSprints = new ArrayList<>();

        for (String sprintUid : sprintUids) {
            if (Workspace.getSprint(sprintUid).getAssociatedProject() != null) {
                if (Workspace.getSprint(sprintUid).getAssociatedProject().equals(uid())) {
                    associatedSprints.add(sprintUid);
                }
            }
        }

        return associatedSprints;
    }
}
