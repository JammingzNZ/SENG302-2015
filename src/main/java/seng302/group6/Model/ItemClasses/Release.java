package seng302.group6.Model.ItemClasses;

import com.google.gson.annotations.Expose;
import seng302.group6.Model.ItemType;

import java.util.ArrayList;


/**
 * Created by Michael Wheeler on 3/05/15.
 *
 *  Contains all information about releases
 */
public class Release extends Item {

    @Expose String description;
    @Expose String releaseDate;
    //TODO Use Project UID instead of short name
    @Expose String associatedProject;


    /**
     * Creates a new release
     * @param shortName the name this release goes by (e.g. Release 1)
     * @param description the description for the release
     * @param releaseDate the estimated release date
     * @param associatedProject project linked to this release
     */
    public Release(String shortName, String description, String associatedProject, String releaseDate)
    {
        this.shortName = shortName;
        this.description = description;
        this.releaseDate = releaseDate;
        this.associatedProject = associatedProject;
    }
    /**
     * Gets the releases description
     * @return releases description
     */
    public String getDescription()
    {
        return description;
    }

    /**
     * Sets the releases description
     * @param description releases description
     */
    public void setDescription(String description)
    {
        this.description = description;
    }

    /**
     * Gets the releases estimated release date
     * @return estimated release date
     */
    public String getReleaseDate()
    {
        return releaseDate;
    }

    /**
     * Sets the releases estimated release date
     * @param releaseDate estimated release date
     */
    public void setReleaseDate(String releaseDate)
    {
        this.releaseDate = releaseDate;
    }

    /**
     * Sets the associated project
     * @param project associated project
     */
    public void setAssociatedProject(String project)
    {
        Project oldProject = Workspace.getProject(associatedProject);
        if (oldProject != null) {
            oldProject.removeRelease(this.uid);
        }

        this.associatedProject = project;

        Project newProject = Workspace.getProject(associatedProject);
        if (newProject != null) {
            newProject.addRelease(this.uid);
        }
    }

    /**
     * Returns the associated project
     * @return project
     */
    public String getAssociatedProject()
    {
        return associatedProject;
    }


    @Override
    protected boolean searchAllFields(String searchTerm) {
        return searchField(searchTerm, getDescription());
    }

    /**
     * Returns the release item type
     * @return the release item type
     */
    @Override
    public ItemType type()
    {
        return ItemType.RELEASE;
    }

    /**
     * Returns a string to be shown in the search pane
     * @return search string
     */
    @Override
    public String searchString()
    {
        return shortName + " (Release)\nRelease Date: " +
                (releaseDate.length() > 30 ? releaseDate.substring(0, 30) + "..." : releaseDate)
                + "    Description: " +
                (description.length() > 30 ? description.substring(0, 30) + "..." : description);
    }



    /**
     * Returns whether or not there are sprints for this release
     * @return true if a sprint uses this release, false otherwise
     */
    public Boolean hasSprint()
    {
        ArrayList<String> sprintUids = Workspace.getSprints();

        for (String sprintUid : sprintUids) {
            if (Workspace.getSprint(sprintUid).getAssociatedRelease() != null) {
                if (Workspace.getSprint(sprintUid).getAssociatedRelease().equals(uid())) {
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * Returns the uids of all sprints that are associated with this release.
     * @return list of sprint uids
     */
    public ArrayList<String> associatedSprints()
    {
        ArrayList<String> sprintUids = Workspace.getSprints();
        ArrayList<String> associatedSprints = new ArrayList<>();

        for (String sprintUid : sprintUids) {
            if (Workspace.getSprint(sprintUid).getAssociatedRelease() != null) {
                if (Workspace.getSprint(sprintUid).getAssociatedRelease().equals(uid())) {
                    associatedSprints.add(sprintUid);
                }
            }
        }

        return associatedSprints;
    }
}
