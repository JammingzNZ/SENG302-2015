package seng302.group6.Model.ItemClasses;


import com.google.gson.annotations.Expose;
import seng302.group6.Model.ItemType;

import java.util.UUID;


/**
 * Abstract model class to represent an item
 * Created by Josh on 19/03/2015.
 */
public abstract class Item {
    @Expose
    String shortName;
    @Expose
    String uid = null;

    /**
     * Constructor to create an item
     */
    public Item()
    {
        uid = UUID.randomUUID().toString();
    }

    /**
     * returns the short name of the item.
     */
    @Override
    public String toString() {
        return this.shortName;
    }

    /**
     * Gets the items's short name
     * @return items's short name
     */
    public String getShortName()
    {
        return this.shortName;
    }

    /**
     * @return returns the unique id of this object.
     */
    public String uid()
    {
        return uid;
    }

    /**
     * Sets the item's short name
     * @param shortName item's short name
     */
    public void setShortName(String shortName) throws NullPointerException
    {
        this.shortName = shortName;
    }

    /**
     * Indicate whether this item should be included in the results of a search.
     * @param searchTerm String to search for
     * @param labelOnly Whether to compare the short name only or all fields
     * @return Whether the item should be a result
     */
    public boolean search(String searchTerm, Boolean labelOnly) {
        if (labelOnly) {
            return searchField(searchTerm, getShortName());
        }
        else {
            return searchField(searchTerm, getShortName()) || searchAllFields(searchTerm);
        }
    }


    /**
     * Indicate whether a field matches a search term
     * @param searchTerm String to search for
     * @param fieldContents Contents of a field to be searched
     * @return Whether the field matches
     */
    protected boolean searchField(String searchTerm, String fieldContents) {
        return fieldContents.toLowerCase().contains(searchTerm.toLowerCase());
    }


    /**
     * Indicate whether this item should be returned by a search among all fields
     * @param searchTerm String to search for
     * @return Whether the item should be a result
     */
    protected abstract boolean searchAllFields(String searchTerm);

    /**
     * Returns the type of the item
     * @return the type of the item
     */
    public abstract ItemType type();

    /**
     * Returns a string to be shown in the search pane
     * @return search string
     */
    public abstract String searchString();
}
