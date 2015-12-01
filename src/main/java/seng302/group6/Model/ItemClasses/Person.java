package seng302.group6.Model.ItemClasses;

import com.google.gson.annotations.Expose;
import seng302.group6.Model.ItemType;

import java.util.ArrayList;

/**
 * Created by simon on 13/03/15.
 * Edited by Jaln on 26/03/15
 *
 * Contains all information about people
 */
public class Person extends Item
{
    @Expose String lastName;
    @Expose String firstName;
    @Expose String userID;
    @Expose ArrayList<String> skills = new ArrayList<>();
    @Expose boolean inTeam;
    @Expose String team;

    /**
     * Creates a new person
     * @param shortName the name this person goes by (e.g. John or John H.)
     * @param lastName the person's last name
     * @param firstName the person's first name
     * @param userID a unique identifier for the person
     */
    public Person(String shortName, String lastName, String firstName, String userID)
    {
        this.shortName = shortName;
        this.lastName = lastName;
        this.firstName = firstName;
        this.userID = userID;
        this.inTeam = false;
    }

    /**
     * Gets the person's last name
     * @return person's last name
     */
    public String getLastName()
    {
        return lastName;
    }

    /**
     * Sets the person's last name
     * @param lastName person's last name
     */
    public void setLastName(String lastName)
    {
        this.lastName = lastName;
    }

    /**
     * Gets the person's first name
     * @return person's first name
     */
    public String getFirstName()
    {
        return firstName;
    }

    /**
     * Sets the person's first name
     * @param firstName person's first name
     */
    public void setFirstName(String firstName)
    {
        this.firstName = firstName;
    }

    /**
     * Gets the person's user ID
     * @return person's user ID
     */
    public String getUserID()
    {
        return userID;
    }

    /**
     * Sets the person's user ID
     * @param userID person's user ID
     */
    public void setUserID(String userID)
    {
        this.userID = userID;
    }

    /**
     * Returns the skill ID list
     * @return the persons skill ID's
     */
    public ArrayList<String> getSkills()
    {
        return skills;
    }

    /**
     * sets the skill ID list: Used for undo/redo
     * @param skills, an arraylist of Skill ID's
     */
    public void setSkills(ArrayList<String> skills)
    {
        this.skills = skills;
    }

    /**
     * Adds a skill to the skills list
     * @param uid is the unique id of the skill to add.
     */
    public void addSkill(String uid)
    {
        this.skills.add(uid);
    }

    /**
     * Removes a skill from the skills array
     * @param uid is the unique id of the skill to remove.
     */
    public void removeSkill(String uid)
    {
        this.skills.remove(uid);
    }

    /**
     * Gets the person's full name
     * @return person's full name
     */
    public String getFullName() {
        return firstName + " " + lastName;
    }

    /**
     * toggle inTeam to true when person joins a team
     */
    public void joinedTeam() { inTeam = true; }

    /**
     * toggle inTeam to false when person leaves a team
     */
    public void leftTeam() { inTeam = false; }

    /**
     * @return returns true if this person is in a team.
     */
    public boolean isInTeam() { return inTeam; }


    @Override
    protected boolean searchAllFields(String searchTerm) {
        return searchField(searchTerm, getFirstName()) ||
                searchField(searchTerm, getLastName()) ||
                searchField(searchTerm, getUserID());
    }

    /**
     * Returns the person item type
     * @return the person item type
     */
    @Override
    public ItemType type()
    {
        return ItemType.PERSON;
    }

    /**
     * Returns a string to be shown in the search pane
     * @return search string
     */
    @Override
    public String searchString()
    {
        return shortName + " (Person)\nFull Name: " +
                (getFullName().length() > 30 ? getFullName().substring(0, 30) + "..." : getFullName())
                + "    ID: " +
                (userID.length() > 30 ? userID.substring(0, 30) + "..." : userID);
    }
}
