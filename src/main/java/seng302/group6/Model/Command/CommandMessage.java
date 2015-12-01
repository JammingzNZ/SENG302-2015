package seng302.group6.Model.Command;

import seng302.group6.Model.ItemClasses.Item;
import seng302.group6.Model.ItemType;

import java.util.ArrayList;

/**
 * A message that is used to determing what a command is. This is used for allowing the user to know what each
 * undo/redo command may do and allows the app to know what the command is changing.
 * Created by Josh on 29/06/2015.
 */
public class CommandMessage {

    private String commandType;
    private ItemType itemType;
    private Item item;
    private String itemProperty;
    private boolean addingNew = false;
    private String tab;
    private ArrayList<Object> specialCaseList;

    /**
     * Constructor for the Command message
     * @param commandType the type of command
     * @param itemType the itemtype involved in the command
     * @param item the item involved in the command
     */
    public CommandMessage(String commandType, ItemType itemType, Item item) {
        this.commandType = commandType;
        this.item = item;
        this.itemType = itemType;
    }

    /**
     * Constructor for the Command message
     * @param commandType the type of command
     * @param itemType the itemtype involved in the command
     * @param item the item involved in the command
     * @param itemProperty item properties
     */
    public CommandMessage(String commandType, ItemType itemType, Item item, String itemProperty) {
        this(commandType, itemType, item);
        this.itemProperty = itemProperty;
    }

    /**
     * Constructor for the Command message
     * @param commandType the type of command
     * @param itemType the itemtype involved in the command
     * @param item the item involved in the command
     * @param itemProperty item property
     * @param addingNew adding new item
     */
    public CommandMessage(String commandType, ItemType itemType, Item item, String itemProperty, Boolean addingNew) {
        this(commandType, itemType, item, itemProperty);
        this.addingNew = addingNew;
    }

    /**
     * Gets the command type of the command message
     * @return the command type
     */
    public String getCommandType() {
        return commandType;
    }

    /**
     * gets the item type of the command message
     * @return the itemtype involved
     */
    public ItemType getItemType() {
        return itemType;
    }

    /**
     * Gets the item of the command message
     * @return the item involved
     */
    public Item getItem() {
        return item;
    }

    /**
     * Gets the item UID involved in the command message
     * @return the uid of the item
     */
    public String getItemID() {
        return item.uid();
    }

    /**
     * Gets the Item name involved in the cmmand message
     * @return the short name of the item
     */
    public String getItemName() {
        return item.getShortName();
    }

    /**
     * get the property of the item involved in the command message
     * @return the item property
     */
    public String getItemProperty() {
        return itemProperty;
    }

    /**
     * Gets the boolean "addingNew"
     * @return if the command is adding a new command
     */
    public Boolean getAddingNew() {
        return addingNew;
    }

    /**
     * Sets the boolean "addingNew"
     * @param addingNew adding new state
     */
    public void setAddingNew(Boolean addingNew) {
        this.addingNew = addingNew;
    }

    /**
     * Get the tab to switch to upon undo/redo
     * @return Name of the tab
     */
    public String getTab() {
        return tab;
    }

    /**
     * Specify the name of the tab to switch through upon undo/redo
     * @param tab Name of the tab
     */
    public void setTab(String tab) {
        this.tab = tab;
    }

    /**
     * Get a list of objects for special cases
     * @return List of objects for special cases
     */
    public ArrayList<Object> getSpecialCaseList() {
        return specialCaseList;
    }

    /**
     * Set some objects to use in special case
     * @param specialCaseList Set objects to use in special cases
     */
    public void setSpecialCaseList(ArrayList<Object> specialCaseList) {
        this.specialCaseList = specialCaseList;
    }

    @Override
    public String toString() {
        return commandType + " " + itemType + " \"" + getItemName() + "\"";
                //String.format("% % \"%\"", commandType, itemType, item.getShortName());
    }
}
