package seng302.group6.Model.Report.Elements;

import org.jdom2.Element;
import seng302.group6.Model.ItemClasses.Skill;
import seng302.group6.Model.ItemClasses.Workspace;

/**
 * Created by dan on 27/05/15.
 */
public class SkillElement {

    public static SkillElement skill;
    private Skill skillItem;
    private Element parent = new Element("Skill");

    /**
     * Constructor for the Skill Element
     */
    public SkillElement() {skill = this; }

    /**
     * Returns the skill element containing attributes:
     * ShortName
     * Description
     * @param uid is the unique ID of the skill to generate an element for
     * @return the element with the skills details attached
     */
    public Element getSkill(String uid) {
        this.skillItem = Workspace.getSkill(uid);
        parent.setAttribute("ShortName", skillItem.getShortName());
        parent.setAttribute("Description", skillItem.getDescription());
        return parent;
    }

}
