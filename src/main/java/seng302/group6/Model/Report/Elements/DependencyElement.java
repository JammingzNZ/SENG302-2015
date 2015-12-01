package seng302.group6.Model.Report.Elements;

import org.jdom2.Element;
import seng302.group6.Model.ItemClasses.Workspace;

/**
 * Creates a Dependency Element for the XML reports
 * Created by dan on 28/07/15.
 */
public class DependencyElement
{
    public static DependencyElement dependency;
    private Element parent = new Element("Story");

    /**
     * Constructor for the dependency element
     */
    public DependencyElement() {dependency = this; }

    /**
     * Get the dependency elements of the story
     * @param dependentStory the dependent story
     * @return the dependecies of the given story
     */
    public Element getDependency(String dependentStory) {
        parent.setAttribute("ShortName", Workspace.getStoryName(dependentStory));
        return parent;
    }
}
