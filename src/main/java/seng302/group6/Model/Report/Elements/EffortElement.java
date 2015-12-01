package seng302.group6.Model.Report.Elements;

import org.jdom2.Element;
import seng302.group6.Model.ItemClasses.Effort;

/**
 * Creates a Task Element for the XML Reports
 */
public class EffortElement {

    public static EffortElement taskElement;

    private Element parent = new Element("Effort");

    /**
     * Constructor for the Task Element
     */
    public EffortElement() {taskElement = this; }

    /**
     * Returns the task element containing attributes:
     * Person
     * Comment
     * Start date
     * Minutes spent
     * @param effort the effort to generate the element for
     * @return the element with the effort details attached
     */
    public Element getEffortLogged(Effort effort) {
        parent.setAttribute("Person", effort.getPerson());
        parent.setAttribute("Comment", effort.getComment());
        parent.setAttribute("Start-Date", effort.getStartDate());
        parent.setAttribute("Effort-Spent", String.valueOf(effort.getMinutes()) + " minutes");
        return parent;
    }
}
