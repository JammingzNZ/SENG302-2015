package seng302.group6.Model.Report.Elements;

import org.jdom2.Element;
import seng302.group6.Model.ItemClasses.Sprint;
import seng302.group6.Model.ItemClasses.Workspace;

/**
 * Creates a Sprint Element for the XML reports
 * Created by daniel on 02/07/15.
 */
public class SprintElement
{
    public static SprintElement sprint;
    private Sprint sprintItem;
    private Element parent = new Element("Sprint");

    /**
     * Constructor for the Sprint Element
     */
    public SprintElement() {sprint = this; }

    /**
     * Returns the sprint element containing attributes:
     * ShortName
     * FullName
     * Description
     * Creator
     * @param uid is the unique ID of the story to generate an element for
     * @return the element with the story details attached
     */
    public Element getSprint(String uid) {
        this.sprintItem = Workspace.getSprint(uid);
        parent.setAttribute("ShortName", sprintItem.getShortName());
        parent.setAttribute("fullName", sprintItem.getFullName());
        parent.setAttribute("Description", sprintItem.getDescription());
        if (sprintItem.getAssociatedProject() != null) {
            parent.setAttribute("Project", Workspace.getProjectName(sprintItem.getAssociatedProject()));
        }
        if (sprintItem.getAssociatedBacklog() != null) {
            parent.setAttribute("Backlog", Workspace.getBacklogName(sprintItem.getAssociatedBacklog()));
        }
        if (sprintItem.getAssociatedRelease() != null) {
            parent.setAttribute("Release", Workspace.getReleaseName(sprintItem.getAssociatedRelease()));
        }
        if (sprintItem.getStartDate() != null) {
            parent.setAttribute("Start-Date", sprintItem.getStartDate());
        }
        if (sprintItem.getEndDate() != null) {
            parent.setAttribute("End-Date", sprintItem.getEndDate());
        }
        if (sprintItem.getAssociatedTeam() != null) {
            parent.setAttribute("Team", Workspace.getTeamName(sprintItem.getAssociatedTeam()));
        }
        return parent;
    }

}
