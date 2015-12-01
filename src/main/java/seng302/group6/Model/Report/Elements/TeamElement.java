package seng302.group6.Model.Report.Elements;

import org.jdom2.Element;
import seng302.group6.Model.ItemClasses.Team;
import seng302.group6.Model.ItemClasses.Workspace;

/**
 * Creates a Team Element for the XML reports
 * Created by dan on 27/05/15.
 */
public class TeamElement
{
    public static TeamElement team;
    private Team teamItem;
    private Element parent = new Element("Team");

    /**
     * Constructor for the Team Element
     */
    public TeamElement() {team = this; }

    /**
     * Returns the team element containing attributes:
     * ShortName
     * Description
     * Product Owner
     * Scrum Master
     * @param uid is the unique ID of the team to generate an element for
     * @return the element with the teams details attached
     */
    public Element getTeam(String uid) {
        this.teamItem = Workspace.getTeam(uid);
        parent.setAttribute("ShortName", teamItem.getShortName());
        parent.setAttribute("Description", teamItem.getDescription());

        if (teamItem.getProductOwner() != null) {
            parent.setAttribute("Product-Owner", Workspace.getPersonName(teamItem.getProductOwner()));
        } else {
            parent.setAttribute("Product-Owner", "");
        }
        if (teamItem.getScrumMaster() != null) {
            parent.setAttribute("Scrum-Master", Workspace.getPersonName(teamItem.getScrumMaster()));
        } else {
            parent.setAttribute("Scrum-Master", "");
        }

        return parent;
    }

}