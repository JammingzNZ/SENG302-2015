package seng302.group6.Model.ItemClasses;

import seng302.group6.Model.ItemType;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Model class to represent the searcher
 * Created by Josh on 27/07/2015.
 */
public class Searcher {

    /**
     * Search for items that contain a search term among one item type.
     * @param searchTerm Term to look for in each item's fields
     * @param labelOnly Whether to only look for matches based on item labels
     * @param itemType The type of item to search for
     * @return A list of all items of the given type that match the search term
     */
    public ArrayList<Item> searchSingle(String searchTerm, Boolean labelOnly, ItemType itemType) {
        ArrayList<String> toSearch = new ArrayList<>();

        switch (itemType) {
            case PROJECT:
                toSearch = Workspace.getProjects();
                break;
            case PERSON:
                toSearch = Workspace.getPeople();
                break;
            case SKILL:
                toSearch = Workspace.getSkills();
                break;
            case TEAM:
                toSearch = Workspace.getTeams();
                break;
            case RELEASE:
                toSearch = Workspace.getReleases();
                break;
            case STORY:
                toSearch = Workspace.getStories();
                break;
            case BACKLOG:
                toSearch = Workspace.getBacklogs();
                break;
            case SPRINT:
                toSearch = Workspace.getSprints();
        }

        ArrayList<Item> searchResults = new ArrayList<>();

        for (String searchID : toSearch) {
            Item searchItem = Workspace.getItem(searchID);
            if (searchItem.search(searchTerm, labelOnly)) {
                searchResults.add(searchItem);
            }
        }

        return searchResults;
    }


    /**
     * Search for items that contain a search term among all items
     * @param searchTerm Term to look for in each item's fields
     * @param labelOnly Whether to only look for matches based on item labels
     * @return results of the search as a hashMap
     */
    public HashMap<ItemType, ArrayList<Item>> searchAll(String searchTerm, Boolean labelOnly) {
        HashMap<ItemType, ArrayList<Item>> searchResults = new HashMap<>();

        for (ItemType itemType : ItemType.values()) {
            searchResults.put(itemType, searchSingle(searchTerm, labelOnly, itemType));
        }

        return searchResults;
    }
}
