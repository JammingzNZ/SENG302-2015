package seng302.group6.Model.Report.Elements;

import org.jdom2.Element;
import seng302.group6.Model.ItemClasses.Person;
import seng302.group6.Model.ItemClasses.Workspace;

/**
 * Creates a Person Element for the XML reports
 * Created by dan on 27/05/15.
 */
public class PersonElement
{
    public static PersonElement person;
    private Person personItem;
    private Element parent = new Element("Person");

    /**
     * Constructor for the PersonElement
     */
    public PersonElement() {person = this; }

    /**
     * Returns the person element containing attributes:
     * ShortName
     * FullName
     * UserID
     * @param uid is the unique ID of the person to generate an element for
     * @return the element with the persons details attached
     */
    public Element getPerson(String uid) {
        this.personItem = Workspace.getPerson(uid);
        parent.setAttribute("ShortName", personItem.getShortName());
        parent.setAttribute("FullName", personItem.getFullName());
        parent.setAttribute("UserID", personItem.getUserID());
        return parent;
    }

}
