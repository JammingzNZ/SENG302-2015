package seng302.group6.Model.Report.Elements;

import org.jdom2.Element;
import seng302.group6.Model.ItemClasses.Project;
import seng302.group6.Model.ItemClasses.Workspace;

/**
 * Creates a Project Element for the XML reports
 * Created by dan on 23/05/15.
 */
public class ProjectElement
{
    public static ProjectElement project;
    private Project projectItem;
    private Element parent = new Element("Project");

    /**
     * Constructor for the ProjectElement
     */
    public ProjectElement() {project = this; }

    /**
     * Returns the project element containing attributes:
     * ShortName
     * LongName
     * Description
     * @param uid is the unique ID of the project to generate an element for
     * @return the element with the projects details attached
     */
    public Element getProject(String uid) {
        this.projectItem = Workspace.getProject(uid);
        parent.setAttribute("ShortName", projectItem.getShortName());
        parent.setAttribute("LongName", projectItem.getLongName());
        parent.setAttribute("Description", projectItem.getDescription());
        if (projectItem.getBacklog() != null) {
            parent.setAttribute("Backlog", Workspace.getItemName(projectItem.getBacklog()));
        }
        return parent;
    }

}
