package seng302.group6.Controller.Content;

import com.sun.javafx.scene.control.behavior.TextAreaBehavior;
import com.sun.javafx.scene.control.behavior.TextFieldBehavior;
import com.sun.javafx.scene.control.skin.TextAreaSkin;
import com.sun.javafx.scene.control.skin.TextFieldSkin;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import seng302.group6.Controller.AppController;
import seng302.group6.Controller.Window.WindowController;
import seng302.group6.Model.Command.AddingNewCommand;
import seng302.group6.Model.Command.Backlog.ChangeBacklogDescription;
import seng302.group6.Model.Command.Backlog.ChangeBacklogName;
import seng302.group6.Model.Command.ChangeShortName;
import seng302.group6.Model.Command.CommandMessage;
import seng302.group6.Model.Command.Person.ChangeFirstName;
import seng302.group6.Model.Command.Person.ChangeLastName;
import seng302.group6.Model.Command.Person.ChangeUserID;
import seng302.group6.Model.Command.Project.ChangeProjectDescription;
import seng302.group6.Model.Command.Project.ChangeProjectName;
import seng302.group6.Model.Command.Release.ChangeReleaseDescription;
import seng302.group6.Model.Command.Skill.ChangeSkillDescription;
import seng302.group6.Model.Command.Sprint.ChangeSprintDescription;
import seng302.group6.Model.Command.Sprint.ChangeSprintName;
import seng302.group6.Model.Command.Story.ChangeStoryDescription;
import seng302.group6.Model.Command.Story.ChangeStoryName;
import seng302.group6.Model.Command.Team.ChangeTeamDescription;
import seng302.group6.Model.ItemClasses.Workspace;
import seng302.group6.Model.ItemType;

import java.util.Collections;
import java.util.HashMap;

import static javafx.scene.input.KeyCode.ENTER;

/**
 * Provides generic methods for all main pane controllers
 *
 * Created by simon on 23/04/15.
 */
public abstract class ContentController
{
    protected boolean editing = true;
    public String currentItem = null;
    protected boolean addingNew = false;
    protected WindowController windowController;
    protected AppController appController = AppController.instance();
    protected Boolean undoable = true;
    protected String validShortName;

    @FXML Label paneTitle;
    @FXML Button edit_doneButton;
    @FXML Label errorMessageLabel;
    @FXML Button cancelButton;

    @FXML TextField itemShortName;

    @FXML Button deleteButton;

    Control erroredField = null;
    HashMap<String, Control> fieldMap = new HashMap<>();

    public void addingNew(){
        //addingNew = true;
        //cancelButton.setVisible(true);
        editView();
    }

    /**
     *  Called when done/edit button is pressed
     */
    public abstract void doneEditAction();

    /**
     * Cancel Action
     * If creating a item, clicking 'cancel' will cancel creation of item
     */
    @FXML
    public void cancelAction()
    {
        // New person was being added
        if(addingNew){

            while (Workspace.getUndoMessage().getCommandType() != "Create") {
                try {
                    AppController.instance().undoRedo(true);} catch (Exception e) {}
            }
            try {
                AppController.instance().undoRedo(true);} catch (Exception e) {}
        }
    }

    /**
     * populates the map of text fields, keying them so that they may be
     * referred to by the name reported in validation reports.
     *
     * call this function before doing validation.
     */
    protected abstract void populateFieldMap();

    /**
     * clears error states from the display if there are any.
     */
    void clearErrorNotification() {
        if(erroredField != null) {
            ObservableList<String> styleClass = erroredField.getStyleClass();
            styleClass.removeAll(Collections.singleton("error"));
            erroredField = null;
            errorMessageLabel.setText("");
        }
    }

    /**
     * Getter for editing field
     * @return returns true if the pane is in edit mode.
     */
    public boolean isEditing()
    {
        return editing;
    }

    /**
     *
     * @param uid is the unique id of the item to set as current (the one selected in sidebar list for example).
     */
    public void setSelected(String uid)
    {
        currentItem = uid;
        addingNew = false;
        this.isEditing(false);
        this.displayView();
        //TODO Add validation
        validShortName = Workspace.getItemName(uid);
        addUndoableShortName();
        addUndoableTextInput(itemShortName, CommandType.SHORT_NAME);

        editView();
        edit_doneButton.setVisible(false);
    }

    /**
     * Get the item type used by the current concrete subclass
     * @return the item type used by the current concrete subclass
     */
    protected abstract ItemType getItemType();

    /**
     * Enables the display view
     */
    protected void displayView()
    {
        forceTextInputCommand();

        WindowController.sideDisplayListController().setEnable(true);
        errorMessageLabel.setText("");
        this.isEditing(false);
        paneTitle.setText("" + getItemType() + ": \"" + Workspace.getItemName(currentItem) + "\"");

        edit_doneButton.setText("Edit"); // Change main button text
        cancelButton.setVisible(false); // Remove cancel button
    }

    /**
     * Enables the edit view
     */
    protected void editView()
    {
        //WindowController.sideDisplayListController().setEnable(false);
        errorMessageLabel.setText("");
        this.isEditing(true);
        paneTitle.setText("" + getItemType() + ": \"" + Workspace.getItemName(currentItem) + "\"");


        edit_doneButton.setText("Done"); // Change main button text
        cancelButton.setVisible(false); // Show cancel button
    }

    /**
     * check that in this pane, an item is known to be selected.
     * this implies that we are editing an existing item.
     *
     * @return returns true if an item is selected.
     */
    protected boolean anItemIsSelected()
    {
        return currentItem != null;
    }

    /**
     * Setter for editing field
     * @param  edit sets the editing field value.
     */
    protected void isEditing(boolean edit)
    {
         editing = edit;
    }

    /**
     * Handles a key press in a TextArea
     * If tab is pressed, it will traverse to the next node
     * If enter is pressed, changes will be saved
     * If alt+tab is pressed, a tab character is inserted
     * if alt+enter is pressed, a new line is inserted
     * @param keyEvent the KeyEvent
     */
    @FXML
    protected void handleTextAreaKeyPress(KeyEvent keyEvent)
    {
        //if (this.isEditing()) {
            // Need these for traversing/inserting characters
            TextArea sender = (TextArea) keyEvent.getSource();
            TextAreaSkin skin = (TextAreaSkin) sender.getSkin();
            TextAreaBehavior behavior = skin.getBehavior();

            switch (keyEvent.getCode()) {

                case TAB:

                    // Insert a tab if alt is pressed
                    if (keyEvent.isAltDown()) {
                        behavior.callAction("InsertTab");
                    } else if (keyEvent.isShiftDown()) {
                        behavior.callAction("TraversePrevious");
                    } else {
                        behavior.callAction("TraverseNext");
                    }
                    keyEvent.consume();

                    break;

                case ENTER:


                    behavior.callAction("InsertNewLine");
                    keyEvent.consume();

                    break;

                case Z:

                    //Override text area undo
                    if (keyEvent.isControlDown()) {
                        try {
                            AppController.instance().undoRedo(true);
                        }
                        catch (Exception e) {}
                    }
                    keyEvent.consume();
                    break;

                case Y:

                    //Override text area redo
                    if (keyEvent.isControlDown()) {
                        try {
                            AppController.instance().undoRedo(false);
                        }
                        catch (Exception e) {}
                    }
                    keyEvent.consume();
                    break;
            }
        //}
    }

    /**
     * Handles a key press in a TextField
     * If enter is pressed, changes will be saved
     * if alt+enter is pressed, a new line is inserted
     * @param keyEvent key that has been pressed
     */
    @FXML
    protected void handleTextFieldKeyPress(KeyEvent keyEvent)
    {
        if (this.isEditing()) {
            // Need these for inserting characters
            TextField sender = (TextField) keyEvent.getSource();
            TextFieldSkin skin = (TextFieldSkin) sender.getSkin();
            TextFieldBehavior behavior = skin.getBehavior();

            switch (keyEvent.getCode()) {

                case ENTER:

                    behavior.callAction("InsertNewLine");
                    keyEvent.consume();

                    break;

                case Z:

                    //Override text area undo
                    if (keyEvent.isControlDown()) {
                        try {
                            AppController.instance().undoRedo(true);
                        }
                        catch (Exception e) {}
                    }
                    keyEvent.consume();
                    break;

                case Y:

                    //Override text area redo
                    if (keyEvent.isControlDown()) {
                        try {
                            AppController.instance().undoRedo(false);
                        }
                        catch (Exception e) {}
                    }
                    keyEvent.consume();
                    break;
            }
        }
    }

    /**
     * Cancels if enter is pressed while cancel button is selected
     * @param keyEvent the KeyEvent
     */
    @FXML
    protected void handleCancelKeyPress(KeyEvent keyEvent)
    {
        if (keyEvent.getCode() == ENTER) {
            cancelAction();
            keyEvent.consume();
        }
    }

    /**
     * Saves changes if enter is pressed while done button is selected
     * @param keyEvent the KeyEvent
     */
    @FXML
    protected void handleDoneKeyPress(KeyEvent keyEvent)
    {
        if (keyEvent.getCode() == ENTER) {
            doneEditAction();
            keyEvent.consume();
        }
    }

    /**
     * Give this a link to the main windowController.
     * @param windowController Main windowController of the program
     */
    public void setWindowController(WindowController windowController) {
        this.windowController = windowController;
    }

    public enum CommandType {
        //Item
        SHORT_NAME,
        //Skill
        SKILL_DESCRIPTION,
        //Person
        LAST_NAME,
        FIRST_NAME,
        USER_ID,
        //Project
        PROJECT_DESCRIPTION,
        PROJECT_NAME,
        //Team
        TEAM_DESCRIPTION,
        //Release
        RELEASE_DESCRIPTION,
        //Story
        STORY_LONG_NAME,
        STORY_DESCRIPTION,
        //Backlog
        BACKLOG_NAME,
        BACKLOG_DESCRIPTION,
        //Sprint
        SPRINT_NAME,
        SPRINT_DESCRIPTION
    }

    /**
     * Adds the Undo Text
     * @param text text
     * @param commandType The command type of the command
     */
    protected void addUndoableTextInput(TextInputControl text, CommandType commandType) {

        text.focusedProperty().addListener(new ChangeListener<Boolean>()
        {
            @Override
            public void changed(ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue, Boolean newPropertyValue)
            {
                if (!newPropertyValue && Workspace.getItem(currentItem) != null && undoable)
                {
                    text.setText(text.getText().trim());
                    //Item
                    if (commandType == CommandType.SHORT_NAME) {
                        if (validateShortName() == null) {
                            if(!Workspace.getItem(currentItem).getShortName().equals(text.getText())) {
                                appController.addCommand(new AddingNewCommand(
                                        new ChangeShortName(Workspace.getItem(currentItem), text.getText()),
                                        addingNew
                                ));
                                paneTitle.setText("" + getItemType() + ": \"" + Workspace.getItemName(currentItem) + "\"");
                            }
                            validShortName = text.getText();
                        }
                        else {
                            text.setText(validShortName);
                            clearErrorNotification();
                        }
                    }
                    //Skill
                    if (commandType == CommandType.SKILL_DESCRIPTION) {
                        //Checks to see if there is a difference
                        if(!Workspace.getSkill(currentItem).getDescription().equals(text.getText())) {
                            appController.addCommand(new AddingNewCommand(
                                    new ChangeSkillDescription(Workspace.getSkill(currentItem), text.getText()),
                                    addingNew
                            ));
                        }
                    }
                    //Person
                    if (commandType == CommandType.LAST_NAME) {
                        //Checks to see if there is a difference
                        if(!Workspace.getPerson(currentItem).getLastName().equals(text.getText())) {
                            appController.addCommand(new AddingNewCommand(
                                    new ChangeLastName(Workspace.getPerson(currentItem), text.getText()),
                                    addingNew
                            ));
                        }
                    }
                    if (commandType == CommandType.FIRST_NAME) {
                        //Checks to see if there is a difference
                        if(!Workspace.getPerson(currentItem).getFirstName().equals(text.getText())) {
                            appController.addCommand(new AddingNewCommand(
                                    new ChangeFirstName(Workspace.getPerson(currentItem), text.getText()),
                                    addingNew
                            ));
                        }
                    }
                    if (commandType == CommandType.USER_ID) {
                        //Checks to see if there is a difference
                        if(!Workspace.getPerson(currentItem).getUserID().equals(text.getText())) {
                            appController.addCommand(new AddingNewCommand(
                                    new ChangeUserID(Workspace.getPerson(currentItem), text.getText()),
                                    addingNew
                            ));
                        }
                    }
                    //Project
                    if (commandType == CommandType.PROJECT_DESCRIPTION) {
                        //Checks to see if there is a difference
                        if(!Workspace.getProject(currentItem).getDescription().equals(text.getText())) {
                            appController.addCommand(new AddingNewCommand(
                                    new ChangeProjectDescription(Workspace.getProject(currentItem), text.getText()),
                                    addingNew
                            ));
                        }
                    }
                    if (commandType == CommandType.PROJECT_NAME) {
                        //Checks to see if there is a difference
                        if(!Workspace.getProject(currentItem).getLongName().equals(text.getText())) {
                            appController.addCommand(new AddingNewCommand(
                                    new ChangeProjectName(Workspace.getProject(currentItem), text.getText()),
                                    addingNew
                            ));
                        }
                    }
                    //Team
                    if (commandType == CommandType.TEAM_DESCRIPTION) {
                        //Checks to see if there is a difference
                        if(!Workspace.getTeam(currentItem).getDescription().equals(text.getText())) {
                            appController.addCommand(new AddingNewCommand(
                                    new ChangeTeamDescription(Workspace.getTeam(currentItem), text.getText()),
                                    addingNew
                            ));
                        }
                    }
                    //Release
                    if (commandType == CommandType.RELEASE_DESCRIPTION){
                        if(!Workspace.getRelease(currentItem).getDescription().equals(text.getText())) {
                            appController.addCommand(new AddingNewCommand(
                                    new ChangeReleaseDescription(Workspace.getRelease(currentItem), text.getText()),
                                    addingNew
                            ));
                        }
                    }
                    //Story
                    if (commandType == CommandType.STORY_LONG_NAME) {
                        if(!Workspace.getStory(currentItem).getLongName().equals(text.getText())) {
                            appController.addCommand(new AddingNewCommand(
                                    new ChangeStoryName(Workspace.getStory(currentItem), text.getText()),
                                    addingNew
                            ));
                        }
                    }
                    if (commandType == CommandType.STORY_DESCRIPTION) {
                        if(!Workspace.getStory(currentItem).getDescription().equals(text.getText())) {
                            appController.addCommand(new AddingNewCommand(
                                    new ChangeStoryDescription(Workspace.getStory(currentItem), text.getText()),
                                    addingNew
                            ));
                        }
                    }
                    //Backlog
                    if (commandType == CommandType.BACKLOG_NAME) {
                        if(!Workspace.getBacklog(currentItem).getFullName().equals(text.getText())){
                            appController.addCommand(new AddingNewCommand(
                                    new ChangeBacklogName(Workspace.getBacklog(currentItem), text.getText()),
                                    addingNew
                            ));
                        }
                    }
                    if (commandType == CommandType.BACKLOG_DESCRIPTION) {
                        if(!Workspace.getBacklog(currentItem).getDescription().equals(text.getText())){
                            appController.addCommand(new AddingNewCommand(
                                    new ChangeBacklogDescription(Workspace.getBacklog(currentItem), text.getText()),
                                    addingNew
                            ));
                        }
                    }
                    //Sprint
                    if (commandType == CommandType.SPRINT_NAME) {
                        if(!Workspace.getSprint(currentItem).getFullName().equals(text.getText())){
                            appController.addCommand(new AddingNewCommand(
                                    new ChangeSprintName(Workspace.getSprint(currentItem), text.getText()),
                                    addingNew
                            ));
                        }
                    }
                    if (commandType == CommandType.SPRINT_DESCRIPTION) {
                        if(!Workspace.getSprint(currentItem).getDescription().equals(text.getText())){
                            appController.addCommand(new AddingNewCommand(
                                    new ChangeSprintDescription(Workspace.getSprint(currentItem), text.getText()),
                                    addingNew
                            ));
                        }
                    }
                }
            }
        });
    }

    /**
     * Set the short name to detect errors upon changes
     */
    private void addUndoableShortName() {
        itemShortName.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(final ObservableValue<? extends String> observable, final String oldValue, final String newValue) {
                if (isEditing()) {
                    String errMessage = validateShortName();
                    if (errMessage != null) {
                        erroredField = itemShortName;
                        errorMessageLabel.setText(errMessage);
                        ObservableList<String> styleClass = erroredField.getStyleClass();
                        if (! styleClass.contains("error")) {
                            styleClass.add("error");
                        }
                    }
                    else {
                        clearErrorNotification();
                    }
                }
            }
        });
    }

    /**
     * Check if the short name is valid
     * @return A relevent error message, or null if there are no errors
     */
    private String validateShortName() {
        String error = null;
        String newValue = itemShortName.getText().trim();
        if (newValue.length() > 24) {
            error = "Short name must be less than 25 characters";
        }
        else if (newValue.length() < 1) {
            error = "Short name cannot be null";
        }
        else if (!newValue.equals(Workspace.getItemName(currentItem)) &&
                Workspace.shortNameNotUnique(newValue, Workspace.getItem(currentItem))) {
            error = "Short name must be unique";
        }
        return error;
    }




    /**
     * Set whether undo/redo commands will be recorded.
     * @param undoable Whether commands will be recorded.
     */
    public void setUndoable(boolean undoable) {
        this.undoable = undoable;
    }


    /**
     * Shift the focus to force any selected text input to save a command
     */
    public void forceTextInputCommand() {
        edit_doneButton.setVisible(true);
        edit_doneButton.requestFocus();
        edit_doneButton.setVisible(false);
    }

    /**
     * Set the focus to the GUI element with the given fxID
     * @param commandMessage Message to use for knowing what to update
     */
    public abstract void setFocus(CommandMessage commandMessage);

    /**
     * Deletes the item in the current pane
     */
    @FXML
    public void deleteButtonAction(){
        windowController.sideDisplayListController().deleteSelectedItem();
    }
}
