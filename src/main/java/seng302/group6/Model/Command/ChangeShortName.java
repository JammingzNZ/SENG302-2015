package seng302.group6.Model.Command;

import seng302.group6.Model.ItemClasses.Item;
import seng302.group6.Model.ItemType;

/**
 * Command class which allows undo/redo functionality for changing the shortname of an item
 * Created by Josh on 1/05/2015.
 */
public class ChangeShortName implements Command {

    private Item item;
    private String oldShortName;
    private String newShortName;
    private ItemType itemType;

    /**
     * Create a ChangeDescription command for changing the first name of a skill
     * @param item Item to change the first name of
     * @param newShortName String to change the first name to
     */
    public ChangeShortName(Item item, String newShortName) {
        this.item = item;
        this.newShortName = newShortName;
        this.oldShortName = item.getShortName();

        String[] itemParts = item.getClass().toString().split("\\.");
        String itemStr = itemParts[itemParts.length-1];
        itemType = ItemType.valueOf(itemStr.toUpperCase());
    }

    /**
     * Undo the action, setting the first name to the old first name
     */
    @Override
    public void undo() {
        item.setShortName(oldShortName);
    }

    /**
     * Redo the action, setting the first name to the new first name
     */
    @Override
    public void redo() {
        item.setShortName(newShortName);
    }

    /**
     * Return a message indicating properties of the command
     * @return a message indicating properties of the command
     */
    @Override
    public CommandMessage getMessage() {
        CommandMessage message = new CommandMessage("Change", itemType, item, "itemShortName");

        return message;
    }
}
