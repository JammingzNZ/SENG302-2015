package seng302.group6.Model.Report.Elements;

import org.jdom2.Element;
import seng302.group6.Model.ItemClasses.Backlog;
import seng302.group6.Model.ItemClasses.Workspace;

/**
 * Creates a Backlog Element for the XML reports
 * Created by simon on 8/07/15.
 */
public class BacklogElement
{
    public static BacklogElement backlog;
    private Backlog backlogItem;
    private Element parent = new Element("Backlog");

    /**
     * Constructor for the backlog element class
     */
    public BacklogElement() {backlog = this; }

    /**
     * Returns the backlog element containing attributes:
     * ShortName
     * FullName
     * Description
     * ProductOwner
     * @param uid is the unique ID of the backlog to generate an element for
     * @return the element with the backlog details attached
     */
    public Element getBacklog(String uid) {
        this.backlogItem = Workspace.getBacklog(uid);
        parent.setAttribute("ShortName", backlogItem.getShortName());
        parent.setAttribute("FullName", backlogItem.getFullName());
        parent.setAttribute("Description", backlogItem.getDescription());
        if (Workspace.getPersonName(backlogItem.getProductOwner()) != null) {
            parent.setAttribute("ProductOwner", Workspace.getPersonName(backlogItem.getProductOwner()));
        } else {
            parent.setAttribute("ProductOwner", "");
        }
        parent.setAttribute("EstimationScale", backlogItem.getScaleType().toString());
        return parent;
    }
}
