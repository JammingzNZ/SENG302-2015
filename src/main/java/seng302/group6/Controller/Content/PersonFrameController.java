package seng302.group6.Controller.Content;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import seng302.group6.Controller.Window.WindowController;
import seng302.group6.Debug;
import seng302.group6.Model.Command.AddingNewCommand;
import seng302.group6.Model.Command.CommandMessage;
import seng302.group6.Model.Command.Person.AddSkill;
import seng302.group6.Model.Command.Person.RemoveSkill;
import seng302.group6.Model.ItemClasses.Person;
import seng302.group6.Model.ItemClasses.Workspace;
import seng302.group6.Model.ItemType;

import java.util.ArrayList;


/**
 * Controller for the Person frame
 * Created by wheeler on 18/03/15.
 */
public class PersonFrameController extends ContentController
{

    @FXML TextField personFirstName;
    @FXML TextField personLastName;
    @FXML TextField personID;

    @FXML Label equippedSkillsLabel;
    @FXML Label availableSkillsLabel;
    @FXML ListView<String> availableList;
    @FXML ListView<String> skillsList;
    private ArrayList<String> availableSkills = new ArrayList<>();
    private ArrayList<String> equippedSkills = new ArrayList<>();
    @FXML Button removeButton;
    @FXML Button addButton;

    @FXML private TabPane personTabPane;
    @FXML private Tab propertiesTab;
    @FXML private Tab skillsTab;

    @FXML Label snAsterisk;

    /**
     * Intializes the Person Frame
     */
    @FXML
    public void initialize()
    {
        availableSkills = Workspace.getSkills();
        availableList.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        availableList.setEditable(false);
        skillsList.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        skillsList.setEditable(false);
        updateSkillTables();
    }

    /**
     * Removes the skill from the person
     */
    @FXML
    public void removeSkillAction(){
        String skillName = skillsList.getSelectionModel().getSelectedItem();
        if(skillName != null) {
            String uid = Workspace.getSkillID(skillName);
            if(uid != null) {
                equippedSkills.remove(uid);
                availableSkills.add(uid);
                updateSkillTables();
            }
            try {
                appController.addCommand(new AddingNewCommand(new RemoveSkill(Workspace.getPerson(currentItem), uid),
                        addingNew));
            }
            catch (NullPointerException e)
            {
                Debug.run(() -> e.printStackTrace());
            }
        }
    }

    /**
     * Adds the Skill to the Person
     */
    @FXML
    public void addSkillAction(){
        String skillName = availableList.getSelectionModel().getSelectedItem();
        if(skillName != null) {
            String uid = Workspace.getSkillID(skillName);
            if(uid != null) {
                equippedSkills.add(uid);
                availableSkills.remove(uid);
                updateSkillTables();
            }
            try {
                appController.addCommand(new AddingNewCommand(new AddSkill(Workspace.getPerson(currentItem), uid),
                        addingNew));
            }
            catch (NullPointerException e)
            {
                Debug.run(() -> e.printStackTrace());
            }
        }
    }


    /**
     * populates the map of text fields, keying them so that they may be
     * referred to by the name reported in validation reports.
     */
    protected void populateFieldMap()
    {
        fieldMap.put("firstName", personFirstName);
        fieldMap.put("lastName", personLastName);
        fieldMap.put("shortName", itemShortName);
        fieldMap.put("userID", personID);
    }

    /**
     * Updates teh Skill Tables
     */
    public void updateSkillTables()
    {
        ObservableList<String> aS = FXCollections.observableArrayList(Workspace.getSkillNames(availableSkills));
        aS.sort(String.CASE_INSENSITIVE_ORDER);
        availableList.setItems(aS);
        ObservableList<String> eS = FXCollections.observableArrayList(Workspace.getSkillNames(equippedSkills));
        eS.sort(String.CASE_INSENSITIVE_ORDER);
        skillsList.setItems(eS);

        skillsList.getSelectionModel().clearSelection();
        availableList.getSelectionModel().clearSelection();
    }

    /**
     * Done / Edit Button
     * Clicking 'Done' will save new/editing person object
     * Clicking 'Edit' allows a person objects attributes to be edited
     * Changes between done and edit when clicked
     */
    @FXML
    public void doneEditAction()
    {

        // Done button has been clicked
        if (this.isEditing()) {
            displayView();
            WindowController.instance.updateGUI();
            WindowController.instance.updateList(ItemType.PERSON);
            WindowController.sideDisplayListController().setListItem(Workspace.getPersonName(currentItem), ItemType.PERSON);
            clearErrorNotification();
        }
        // Edit button has been clicked
        else {
            editView();
            // TODO: check if it works or not on Linux
            // Displays the persons skills in the textfield when edit is clicked
            equippedSkills = new ArrayList<>(); // fixes bug where skills are added more than once
            for(String sid : Workspace.getPerson(currentItem).getSkills()){
                equippedSkills.add(sid);
            }
        }
    }

    /**
     * Give more generic methods access to the item type
     * @return The PERSON item type
     */
    protected ItemType getItemType() {
        return ItemType.PERSON;
    }

    /**
     * Makes elements uneditable and changes button to say 'edit'
     */
    protected void displayView()
    {
        super.displayView();

        // Make text-fields look unselectable ==========================
        personFirstName.setDisable(true);
        personLastName.setDisable(true);
        itemShortName.setDisable(true);
        personID.setDisable(true);

        personFirstName.getStyleClass().remove("edit-view");
        personLastName.getStyleClass().remove("edit-view");
        itemShortName.getStyleClass().remove("edit-view");
        personID.getStyleClass().remove("edit-view");

        personFirstName.getStyleClass().add("display-view");
        personLastName.getStyleClass().add("display-view");
        itemShortName.getStyleClass().add("display-view");
        personID.getStyleClass().add("display-view");

        availableList.setVisible(false); // Hide ListView of available skills
        // Hide add and remove buttons for adding skills to person
        addButton.setVisible(false);
        removeButton.setVisible(false);
        availableSkillsLabel.setVisible(false); // Hide available skills label

        skillsList.getStyleClass().remove("edit-view-listview");
        skillsList.getStyleClass().add("display-view-listview");
        skillsList.getStyleClass().remove("edit-view");
        skillsList.getStyleClass().add("display-view");

        // Hide asterisks
        snAsterisk.setVisible(false);
    }

    /**
     * Makes elements editable and changes button to say 'done'
     */
    protected void editView()
    {
        super.editView();

        // Make text-fields look like text-fields again =======================
        personFirstName.setDisable(false);
        personLastName.setDisable(false);
        itemShortName.setDisable(false);
        personID.setDisable(false);

        personFirstName.getStyleClass().remove("display-view");
        personLastName.getStyleClass().remove("display-view");
        itemShortName.getStyleClass().remove("display-view");
        personID.getStyleClass().remove("display-view");

        personFirstName.getStyleClass().add("edit-view");
        personLastName.getStyleClass().add("edit-view");
        itemShortName.getStyleClass().add("edit-view");
        personID.getStyleClass().add("edit-view");

        availableList.setVisible(true); // Show available skills in ListView
        // Show add and remove buttons for adding people to team.
        addButton.setVisible(true);
        removeButton.setVisible(true);
        availableSkillsLabel.setVisible(true); // Make available label appear.

        skillsList.getStyleClass().remove("display-view-listview");
        skillsList.getStyleClass().add("edit-view-listview");
        skillsList.getStyleClass().remove("display-view");
        skillsList.getStyleClass().add("edit-view");

        // ====================================================================

        // Show asterisks
        snAsterisk.setVisible(true);
    }

    /**
     * Called from main WindowController to tell this windowController who the currently
     * selected person from the list is
     * @param uid currently selected person from list
     */
    public void setSelected(String uid)
    {
        super.setSelected(uid);
        Person person = Workspace.getPerson(uid);
        addUndoableTextInput(personLastName, CommandType.LAST_NAME);
        addUndoableTextInput(personFirstName, CommandType.FIRST_NAME);
        addUndoableTextInput(personID, CommandType.USER_ID);

//        person.updateSkillShortNames();

        // Set text-fields with values from person
        personFirstName.setText(person.getFirstName());
        personLastName.setText(person.getLastName());
        itemShortName.setText(person.getShortName());
        personID.setText(person.getUserID());

        equippedSkills = new ArrayList<>();
        for(String skillid : person.getSkills()){
            availableSkills.remove(skillid); // Remove person's skills from the available list
            equippedSkills.add(skillid);
        }

        updateSkillTables();
    }


    /**
     * Sets the focus on the Control with the given fxID
     * @param commandMessage Command Message to use to know what to update
     */
    public void setFocus(CommandMessage commandMessage)
    {
        String fxID = commandMessage.getItemProperty();
        String tab = commandMessage.getTab();

        editView();

        if ("Skills".equals(tab)) {
            personTabPane.getSelectionModel().select(skillsTab);
        }
        else {
            personTabPane.getSelectionModel().select(propertiesTab);
        }

        switch(fxID) {
            case("itemShortName"):
                itemShortName.requestFocus();
                break;
            case("personFirstName"):
                personFirstName.requestFocus();
                break;
            case("personLastName"):
                personLastName.requestFocus();
                break;
            case("personID"):
                personID.requestFocus();
                break;
            case("availableList"):
                availableList.requestFocus();
                break;
            case("skillsList"):
                skillsList.requestFocus();
                break;
        }
    }
}
