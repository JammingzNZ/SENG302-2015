package seng302.group6.Model.ItemClasses;

import com.google.gson.annotations.Expose;
import seng302.group6.Model.ItemType;

/**
 * Model class to represent a skill
 * Created by simon on 17/03/15.
 * Edited by Jaln on 26/03/15
 */
public class Skill extends Item
{
    @Expose String description;

    /**
     * Constructor for a skill
     * @param shortName the shortname of the skill
     * @param description the description of the skill
     */
    public Skill(String shortName, String description)
    {
        this.shortName = shortName;
        this.description = description;
    }

    /**
     * Get the description of the skill
     * @return the skill description
     */
    public String getDescription()
    {
        return description;
    }

    /**
     * Sets the description of the skill
     * @param description description string
     */
    public void setDescription(String description)
    {
        this.description = description;
    }

    /**
     * A skill which allows the person to be a SM
     * @return the SM skill
     */
    public static Skill scrumMaster()
    {
        Skill sm = new Skill("Scrum Master", "Can be the scrum master of a scrum team.");
        return sm;
    }

    /**
     * A skill which allows the person to be a PO
     * @return the PO skill
     */
    public static Skill productOwner()
    {
        Skill po = new Skill("Product Owner", "Can be the product owner of a scrum team.");
        return po;
    }


    @Override
    protected boolean searchAllFields(String searchTerm) {
        return searchField(searchTerm, getDescription());
    }

    /**
     * Returns the skill item type
     * @return the skill item type
     */
    @Override
    public ItemType type()
    {
        return ItemType.SKILL;
    }

    /**
     * Returns a string to be shown in the search pane
     * @return search string
     */
    @Override
    public String searchString()
    {
        return shortName + " (Skill)\nDescription: " +
                (description.length() > 80 ? description.substring(0, 80) + "..." : description);
    }
}
