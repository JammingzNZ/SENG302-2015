package seng302.group6.Controller.Content;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import seng302.group6.Controller.Window.WindowController;
import seng302.group6.Model.Command.CommandMessage;
import seng302.group6.Model.ItemClasses.Skill;
import seng302.group6.Model.ItemClasses.Workspace;
import seng302.group6.Model.ItemType;

/**
 * Controller for the Skill frame
 * Created by dan on 19/03/15.
 */
public class SkillFrameController extends ContentController
{
    
    @FXML private TextArea skillDescription;

    private String oldShortName;
    private String oldDescription;

    @FXML Label snAsterisk;

    /**
     * populates the map of text fields, keying them so that they may be
     * referred to by the name reported in validation reports.
     */
    protected void populateFieldMap()
    {
        fieldMap.put("shortName", itemShortName);
        fieldMap.put("description", skillDescription);
    }

    /**
     * Create skill Button
     * Clicking 'Create' will create a new skill object and add it to the current project
     */
    @FXML
    public void doneEditAction()
    {
        Skill skill = Workspace.getSkill(currentItem);
        if (!skill.getShortName().equals("Product Owner") && !skill.getShortName().equals("Scrum Master")) {
            if (this.isEditing()) {
                displayView();
                WindowController.instance.updateGUI();
                WindowController.instance.updateList(ItemType.SKILL);
                WindowController.sideDisplayListController().setListItem(Workspace.getSkillName(currentItem), ItemType.SKILL);
                clearErrorNotification();
            } else {
                editView();
            }
        }
    }

    /**
     * Changes to the display mode for the Skill Frame
     */
    protected void displayView()
    {
        super.displayView();

        itemShortName.getStyleClass().remove("edit-view");
        skillDescription.getStyleClass().remove("edit-view");

        itemShortName.getStyleClass().add("display-view");
        skillDescription.getStyleClass().add("display-view");
        skillDescription.getStyleClass().add("text-area-display-view");

        itemShortName.setDisable(true);
        skillDescription.setDisable(true);

        // Hide asterisks
        snAsterisk.setVisible(false);
    }

    /**
     * Give more generic methods access to the item type
     * @return The SKILL item type
     */
    protected ItemType getItemType() {
        return ItemType.SKILL;
    }

    /**
     * Switches to the edit mode for the skill frame
     */
    protected void editView()
    {        
        super.editView();

        itemShortName.getStyleClass().remove("display-view");
        skillDescription.getStyleClass().remove("display-view");

        itemShortName.getStyleClass().add("edit-view");
        skillDescription.getStyleClass().add("edit-view");


        itemShortName.setDisable(false);
        skillDescription.setDisable(false);

        // Show asterisks
        snAsterisk.setVisible(true);
    }

    /**
     *
     * @param uid is the unique id of the item to set as current (the one selected in sidebar list for example).
     */
    public void setSelected(String uid)
    {
        super.setSelected(uid);
        setFields(uid);
        addUndoableTextInput(skillDescription, CommandType.SKILL_DESCRIPTION);

        Skill skill = Workspace.getSkill(uid);
        if (skill.getShortName().equals("Product Owner") || skill.getShortName().equals("Scrum Master")) {
            displayView();
            errorMessageLabel.setText("This is a default skill. It can't be modified");
        }
    }

    /**
     * Set the fields to attributes from a given skill.
     * @param uid is the unique id of the Skill to get attributes from
     */
    private void setFields(String uid) {
        Skill skill = Workspace.getSkill(uid);
        itemShortName.setText(skill.getShortName());
        skillDescription.setText(skill.getDescription());
    }

    /**
     * Sets the focus on the Control with the given fxID
     * @param commandMessage Command Message to use for knowing what to update
     */
    public void setFocus(CommandMessage commandMessage)
    {
        String fxID = commandMessage.getItemProperty();
        String tab = commandMessage.getTab();

        editView();
        switch(fxID) {
            case("itemShortName"):
                itemShortName.requestFocus();
                break;
            case("skillDescription"):
                skillDescription.requestFocus();
                break;
        }
    }
}
