package seng302.group6.Model.Report.Elements;

import org.jdom2.Element;
import seng302.group6.Model.ItemClasses.Release;
import seng302.group6.Model.ItemClasses.Workspace;

/**
 * Creates a Release Element for the XML reports
 * Created by dan on 27/05/15.
 */
public class ReleaseElement
{
    public static ReleaseElement release;
    private Release releaseItem;
    private Element parent = new Element("Release");

    /**
     * Constructor for the Release Element
     */
    public ReleaseElement() {release = this; }

    /**
     * Returns the release element containing attributes:
     * ShortName
     * Description
     * Release Date
     * @param uid is the unique ID of the release to generate an element for
     * @return the element with the release details attached
     */
    public Element getRelease(String uid) {
        this.releaseItem = Workspace.getRelease(uid);
        parent.setAttribute("ShortName", releaseItem.getShortName());
        parent.setAttribute("Description", releaseItem.getDescription());
        parent.setAttribute("Release-Date", releaseItem.getReleaseDate());
        return parent;
    }

}