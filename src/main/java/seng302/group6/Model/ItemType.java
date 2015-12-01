package seng302.group6.Model;

/**
 * Model class to represent the different ItemTypes
 * Created by Josh on 11/07/2015.
 */
public enum ItemType {
    PROJECT("Project"),
    TEAM("Team"),
    PERSON("Person", "People"),
    SKILL("Skill"),
    RELEASE("Release"),
    STORY("Story", "Stories"),
    BACKLOG("Backlog"),
    SPRINT("Sprint");

    private String single;
    private String plural;

    /**
     * Constructor for Itemtpe
     * @param single The Itemtype (singular)
     */
    ItemType(String single) {
        this.single = single;
        this.plural = single + "s";
    }

    /**
     * Constructor for ItemType
     * @param single The itemType (singular)
     * @param plural The itemType (plural)
     */
    ItemType(String single, String plural) {
        this.single = single;
        this.plural = plural;
    }

    /**
     * Gets the itemtype (singular)
     * @return the itemtype
     */
    public String getSingle() {
        return single;
    }

    /**
     * Gets the itemtype (plural)
     * @return the itemtype
     */
    public String getPlural() {
        return plural;
    }

    /**
     * The to string method for Itemtype
     * @return the itemtype (singular)
     */
    @Override
    public String toString() {
        return getSingle();
    }
}
