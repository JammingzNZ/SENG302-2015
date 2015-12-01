package seng302.group6.Model.Report.Elements;

import org.jdom2.Element;
import seng302.group6.Model.ItemClasses.Task;

/**
 * 
 * Creates a Task Element for the XML Reports
 * Created by jaln on 30/07/15.
 */
public class TaskElement {

    public static TaskElement taskElement;

    private Element parent = new Element("Task");

    /**
     * Constructor for the Task Element
     */
    public TaskElement() {taskElement = this; }

    /**
     * Returns the task element containing attributes:
     * Shortname
     * Description
     * Status
     * Estimation
     * @param task the task to generate the element for
     * @return the element with the task details attached
     */
    public Element getTask(Task task) {
        parent.setAttribute("ShortName", task.getShortName());
        parent.setAttribute("Description", task.getDescription());
        parent.setAttribute("Estimation", task.getEstimation().toString());
        parent.setAttribute("Status", task.getStatus().toString());
        return parent;
    }

    /**
     * Returns the sprint task element containing attributes:
     * Shortname
     * Estimation
     * Effort Spent
     * Status
     * @param task the task to generate the element for
     * @return the element with the task details attached
     */
    public Element getSprintTask(Task task) {
        parent.setAttribute("ShortName", task.getShortName());
        parent.setAttribute("Estimation", task.getEstimation().toString());
        parent.setAttribute("Effort-Spent", task.getEffort().toString() + " Hours");
        parent.setAttribute("Status", task.getStatus().toString());
        return parent;
    }
}
