package seng302.group6.Model.ItemClasses;

import com.google.gson.annotations.Expose;
import seng302.group6.Debug;
import seng302.group6.Model.ItemType;

import java.time.LocalDate;
import java.util.ArrayList;

/**
 * Model class to represent a team
 * Created by dan on 25/03/15.
 * Edited by Jaln on 26/03/15
 */
public class Team extends Item {
    @Expose private String description;
    @Expose private ArrayList<String> people;
    @Expose private ArrayList<String> developers;
    @Expose private String teamSm;
    @Expose private String teamPo;

    private String allocatedProject;
    private Allocation allocation;

    /**
     *
     * @param shortName shortName of the team
     * @param description description of the team
     */
    public Team(String shortName, String description)
    {
        this.shortName = shortName;
        this.description = description;
        this.people = new ArrayList<>();
        this.developers = new ArrayList<>();
        this.teamSm = null;
        this.teamPo = null;
        this.allocatedProject = null;
        this.allocation = null;
    }

    /**
     * Add a person to the team
     * @param uid is the unique id of the person to add.
     */
    public void addPerson(String uid) {
        Person person = Workspace.getPerson(uid);
        if(person != null) {
            this.people.add(uid);
            person.joinedTeam();
        }
    }

    /**
     * Set a team product owner
     * @param uid is the unique id of the person to make the product owner.
     */
    public void setProductOwner(String uid) {
        this.teamPo = uid;
        if (teamSm != null) {
            if (teamSm.equals(uid)) {
                teamSm = null;
            }
        }
        if (developers.contains(uid)) {
            removeDeveloper(uid);
        }
    }

    /**
     * Get team product owner
     * @return the teams PO uid
     */
    public String getProductOwner() {
        return teamPo;
    }

    /**
     * Get team Scrum Master
     * @return the teams SM uid
     */
    public String getScrumMaster() {
        return teamSm;
    }



    /**
     * Set a team scrum master
     * @param uid is the unique id of the person to make the scrum master.
     */
    public void setScrumMaster(String uid) {
        this.teamSm = uid;
        if (teamPo != null) {
            if (teamPo.equals(uid)) {
                teamPo = null;
            }
        }
        if (developers.contains(uid)) {
            removeDeveloper(uid);
        }
    }

    /**
     * Remove a person from the team
     * @param uid is the unique id of the person to remove.
     */
    public void removePerson(String uid) {
        Person person = Workspace.getPerson(uid);
        if(person != null) {
            person.leftTeam();
            people.remove(uid);
        }
        if(developers.contains(uid)){
            removeDeveloper(uid);
        }
        try {
            if (this.getScrumMaster().equals(uid)) {
                this.setScrumMaster(null);
            }
        }
        catch (NullPointerException e)
        {
            Debug.run(() -> e.printStackTrace());
        }
        try {
            if (this.getProductOwner().equals(uid)) {
                this.setProductOwner(null);
            }
        }
        catch (NullPointerException e)
        {
            Debug.run(() -> e.printStackTrace());
        }
    }

    /**
     * Gets the People in the team
     * @return returns the unique ids of all people in the team.
     */
    public ArrayList<String> getPeople() {
        return people;
    }

    /**
     * Sets the People in the team: Used for Undo/redo functionality
     * @param people is an arraylist of unique ids of people.
     */
    public void setPeople(ArrayList<String> people) {
        this.people = people;
    }

    /**
     * get Description
     * @return string containing the description
     */
    public String getDescription() { return description; }

    /**
     * Set Description
     * @param description string of the description to be set
     */
    public void setDescription(String description) { this.description = description; }


    /**
     * Get all developers in team
     * @return developers
     */
    public ArrayList<String> getDevelopers() {
        return developers;
    }

    /**
     * Set teams developers
     * @param developers all developers
     */
    public void setDevelopers(ArrayList<String> developers) {
        this.developers = developers;
    }

    /**
     * Adds a person to the developer team
     * @param uid is the unique id of the person to be added
     */
    public void addDeveloper(String uid) throws IllegalArgumentException {
        if (people.contains(uid)) {
            developers.add(uid);
            if (teamSm != null) {
                if (teamSm.equals(uid)) {
                    teamSm = null;
                }
            }
            if (teamPo != null) {
                if (teamPo.equals(uid)) {
                    teamPo = null;
                }
            }
        } else {
            throw new IllegalArgumentException(
                    "Person cannot be a developer " +
                    "if they are not in the team");
        }
    }

    /**
     * Removes a person from the developer team
     * @param uid is the unique id of the person to be removed
     */
    public void removeDeveloper(String uid) {
        developers.remove(uid);
    }

    /**
     * Sets the project which the team is allocated to
     * @param uid unique id of the project
     * @param allocation the allocation this team uses
     */
    public void setAllocatedProject(String uid, Allocation allocation)
    {
        this.allocation = allocation;
        this.allocatedProject = uid;
    }

    /**
     * Returns the project which the team is allocated to
     * @return uid of the allocated project
     */
    public String getAllocatedProject()
    {
        if (!allocation.getEndDate().isBefore(LocalDate.now())) {
            return allocatedProject;
        } else {
            return null;
        }
    }

    /**
     * Tells whether team is currently allocated to project
     * @return true if team is allocated to project, false otherwise
     */
    public boolean isAllocated()
    {
        if (allocation != null) {
            if (!allocation.getEndDate().isBefore(LocalDate.now())) {
                return allocatedProject != null ? true : false;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    /**
     * Gets all projects this team has been previously allocated to
     * @return list of project uids
     */
    public ArrayList<Allocation> getProjectHistory()
    {
        ArrayList<Allocation> history = new ArrayList<>();
        ArrayList<String> projectUids = Workspace.getProjects();

        // Loop through all projects in workspace
        for (String uid: projectUids) {

            Project project = Workspace.getProject(uid);

            // Loop through all of the projects allocated dev teams (including
            // past ones)
            for (Allocation allocation: project.getDevTeams()) {
                // Check that this is the team in the current allocation and
                // that it is a past project
                if (allocation.getTeamUID().equals(uid()) &&
                        allocation.isPast()) {
                    // Add project to history
                    history.add(allocation);
                }
            }
        }

        return history;
    }

    /**
     * Returns the Allocation object for the currently allocated project
     * @return current Allocation
     */
    public Allocation getAllocation()
    {
        return allocation;
    }

    /**
     * Checks if the team is able to be allocated to a project for the give
     * start and end
     * @param start start date of allocation
     * @param end end date of allocation
     * @return true if the team can be allocated for these dates, false
     * otherwise
     */
    public boolean canAllocateForDates(LocalDate start, LocalDate end)
    {
        if (start != null && end != null) {
            ArrayList<String> projectUids = Workspace.getProjects();

            // Loop through all projects in workspace
            for (String uid : projectUids) {

                Project project = Workspace.getProject(uid);

                // Loop through all of the projects allocated dev teams (including
                // past ones)
                for (Allocation allocation : project.getDevTeams()) {
                    // Check that this is the team in the current allocation
                    if (allocation.getTeamUID().equals(uid())) {
                        LocalDate aStart = allocation.getStartDate();
                        LocalDate aEnd = allocation.getEndDate();
                        // Check that dates don't overlap, can't allocate if they
                        // do
                        if (!((start.isBefore(aStart) && end.isBefore(aStart)) ||
                                (start.isAfter(aEnd) && end.isAfter(aEnd)))) {
                            return false;
                        }
                    }
                }
            }
            return true;

        } else {
            return false;
        }
    }


    @Override
    protected boolean searchAllFields(String searchTerm) {
        return searchField(searchTerm, getDescription());
    }

    /**
     * Returns the team item type
     * @return the team item type
     */
    @Override
    public ItemType type()
    {
        return ItemType.TEAM;
    }

    /**
     * Returns a string to be shown in the search pane
     * @return search string
     */
    @Override
    public String searchString()
    {
        return shortName + " (Backlog)\nDescription: " +
                (description.length() > 30 ? description.substring(0, 30) + "..." : description);
    }

    /**
     * Returns whether or not there are sprints for this team
     * @return true if a sprint uses this team, false otherwise
     */
    public Boolean hasSprint()
    {
        ArrayList<String> sprintUids = Workspace.getSprints();

        for (String sprintUid : sprintUids) {
            if (Workspace.getSprint(sprintUid).getAssociatedTeam() != null) {
                if (Workspace.getSprint(sprintUid).getAssociatedTeam().equals(uid())) {
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * Returns the uids of all sprints that are associated with this team.
     * @return list of sprint uids
     */
    public ArrayList<String> associatedSprints()
    {
        ArrayList<String> sprintUids = Workspace.getSprints();
        ArrayList<String> associatedSprints = new ArrayList<>();

        for (String sprintUid : sprintUids) {
            if (Workspace.getSprint(sprintUid).getAssociatedTeam() != null) {
                if (Workspace.getSprint(sprintUid).getAssociatedTeam().equals(uid())) {
                    associatedSprints.add(sprintUid);
                }
            }
        }

        return associatedSprints;
    }
}
