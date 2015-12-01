package seng302.group6.Controller.Content;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import seng302.group6.Controller.Window.WindowController;
import seng302.group6.Model.Command.AddingNewCommand;
import seng302.group6.Model.Command.CommandMessage;
import seng302.group6.Model.Command.Team.*;
import seng302.group6.Model.ItemClasses.Allocation;
import seng302.group6.Model.ItemClasses.Person;
import seng302.group6.Model.ItemClasses.Team;
import seng302.group6.Model.ItemClasses.Workspace;
import seng302.group6.Model.ItemType;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.stream.Collectors;

/**
 * Controller for the Team frame
 * Created by dan on 26/03/15.
 */
public class TeamFrameController extends ContentController
{
    @FXML Button addButton;
    @FXML Button removeButton;
    @FXML Label availablePeople;
    @FXML TextArea teamDescription;
    @FXML ListView<String> teamList;
    @FXML ListView<String> availableList;

    @FXML Label availableMembersLabel;
    @FXML ListView<String> developerList;
    @FXML ListView<String> availableMembersList;
    @FXML Button addDeveloperButton;
    @FXML Button removeDeveloperButton;

    @FXML ComboBox<String> poComboBox;
    @FXML ComboBox<String> smComboBox;
    @FXML Label poLabel;
    @FXML Label smLabel;

    @FXML Label snAsterisk;

    @FXML Label allocatedProjectLabel;
    @FXML Label allocatedDateLabel;

    @FXML TableView<Allocation> projectHistoryTable;
    @FXML TableColumn<Allocation, String> projectHistoryNameColumn;
    @FXML TableColumn<Allocation, String> projectHistoryStartDateColumn;
    @FXML TableColumn<Allocation, String> projectHistoryEndDateColumn;

    @FXML private TabPane teamTabPane;
    @FXML private Tab propertiesTab;
    @FXML private Tab peopleTab;
    @FXML private Tab projectsTab;

    private ArrayList<String> availableMembers = new ArrayList<>();
    private ArrayList<String> newTeamMembers = new ArrayList<>();

    private ArrayList<String> newDevelopers = new ArrayList<>();

    private boolean preventCombo = false;

    /**
     * Intializes the Team Frame
     */
    @FXML
    public void initialize(){
        // These three lines cause the error for some reason?

        projectHistoryNameColumn.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(Workspace.getProject(param.getValue().getProjectUID()).getShortName()));
        projectHistoryStartDateColumn.setCellValueFactory(new PropertyValueFactory<>("startDate"));
        projectHistoryEndDateColumn.setCellValueFactory(new PropertyValueFactory<>("endDate"));

        availableMembers = Workspace.getPeopleWithoutTeam();

        availableList.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        availableList.setEditable(false);

        teamList.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        teamList.setEditable(false);

        developerList.setEditable(false);

        updateTeamTables();
        //updateAllocations();
    }

    /**
     * populates the map of text fields, keying them so that they may be
     * referred to by the name reported in validation reports.
     */
    protected void populateFieldMap()
    {
        fieldMap.put("shortName", itemShortName);
        fieldMap.put("description", teamDescription);
    }

    /**
     * Sets the currently allocated project and project history
     */
    private void updateAllocations()
    {
        Team thisTeam = Workspace.getTeam(currentItem);

        // Update currently allocated project
        if (thisTeam.isAllocated()) {

            // Set project name
            allocatedProjectLabel.getStyleClass().remove("label");
            String allocatedProjectUID = thisTeam.getAllocatedProject();
            String projectName = Workspace.getProjectName(allocatedProjectUID);
            allocatedProjectLabel.setText(projectName);

            // Set dates
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

            LocalDate startDate = thisTeam.getAllocation().getStartDate();
            LocalDate endDate = thisTeam.getAllocation().getEndDate();

            Date start = Date.from(startDate.atStartOfDay(ZoneId
                    .systemDefault()).toInstant());
            Date end = Date.from(endDate.atStartOfDay(ZoneId
                    .systemDefault()).toInstant());

            String dateString = dateFormat.format(start) + " - " +
                    dateFormat.format(end);

            allocatedDateLabel.setStyle("-fx-text-fill: #555555;" +
                                        "-fx-font-size: 9pt;");
            allocatedDateLabel.setText(dateString);

        } else {
            allocatedProjectLabel.setText("None ");
            allocatedDateLabel.setText("");
        }

        // Update project history
        ArrayList<Allocation> history = thisTeam.getProjectHistory();
        projectHistoryTable.setItems(FXCollections.observableList(history));
    }

    /**
     * Updates new team members and available people Jlists
     */
    private void updateTeamTables() {
        ObservableList<String> availablePeople = FXCollections.observableArrayList(Workspace.getPeopleNames(availableMembers));
        availableList.setItems(availablePeople);
        ObservableList<String> teamPeople = FXCollections.observableArrayList(Workspace.getPeopleNames(newTeamMembers));
        teamList.setItems(teamPeople);

        ObservableList<String> nd = FXCollections.observableArrayList(Workspace.getPeopleNames(newDevelopers));
        Collections.sort(nd, String.CASE_INSENSITIVE_ORDER);
        developerList.setItems(nd);
        checkIfPo();
        checkIfSm();
        checkIfNotPoOrSm();

        // fixed bug (as far as I know) where some people couldn't be added or removed
        teamList.getSelectionModel().clearSelection();
        availableList.getSelectionModel().clearSelection();
    }

    /**
     * Checks if each person in the new team members list is a Product Owner
     * If the person is a product owner, they are added to the product owner combo box
     */
    private void checkIfPo()
    {
        ObservableList<String> productOwners = FXCollections.observableArrayList();
        productOwners.add("None ");
        for(String uid : newTeamMembers){
            Person temp = Workspace.getPerson(uid);
            String poid = Workspace.getSkillID("Product Owner");
            if(temp.getSkills().contains(poid) && !newDevelopers.contains(uid)){
                productOwners.add(temp.getShortName());
            }
        }
        preventCombo = true;
        poComboBox.setItems(productOwners);
        preventCombo = false;
    }

    /**
     * Checks to see if each person is not a PO or SM and adds them to the available members list
     */
    private void checkIfNotPoOrSm() {
        ArrayList<String> members = new ArrayList<>();
        for (String uid : newTeamMembers) {
            boolean poCheck = false;
            boolean smCheck = false;
            if (Workspace.getPersonID(poComboBox.getSelectionModel().getSelectedItem()) != null) {
                poCheck = Workspace.getPersonID(poComboBox.getSelectionModel().getSelectedItem()).equals(uid);
            }
            if (Workspace.getPersonID(smComboBox.getSelectionModel().getSelectedItem()) != null) {
                smCheck = Workspace.getPersonID(smComboBox.getSelectionModel().getSelectedItem()).equals(uid);
            }
            if (!poCheck
             && !smCheck
             && !newDevelopers.contains(uid)) {
                members.add(uid);
                //availableDevelopers.add(shortName);
            }
        }
        ObservableList<String> am = FXCollections.observableArrayList(Workspace.getPeopleNames(members));
        availableMembersList.setItems(am);
    }


    /**
     * Checks if each person in the new team members list is a Scrum Master
     * If the person is a scrum master, they are added to the scrum master combo box
     */
    private void checkIfSm(){
        ObservableList<String> scrumMasters = FXCollections.observableArrayList();
        scrumMasters.add("None ");
        for(String uid : newTeamMembers){
            Person temp = Workspace.getPerson(uid);
            String smid = Workspace.getSkillID("Scrum Master");
            if(temp.getSkills().contains(smid) && !newDevelopers.contains(uid)){
                scrumMasters.add(temp.getShortName());
            }
        }
        preventCombo = true;
        smComboBox.setItems(scrumMasters);
        preventCombo = false;
    }


    /**
     * Add Button Action
     * Adds selected person into new team member list from available people
     */
    @FXML
    public void addTeamMember() {
        String movingPerson = availableList.getSelectionModel().getSelectedItem();
        if(movingPerson != null) {
            String uid =  Workspace.getPersonID(movingPerson);
            if(uid != null) {
                newTeamMembers.add(uid);
                availableMembers.remove(uid);
                updateTeamTables();
                //try {
                    appController.addCommand(new AddingNewCommand(new AddMember(Workspace.getTeam(currentItem), uid),
                            addingNew));
                //}
                //catch(NullPointerException e) {}
            }
        }
    }


    /**
     * Remove Button Action
     * Removes team member from new team member list back into available people
     */
    @FXML
    public void removeTeamMember() {
        String movingPerson = teamList.getSelectionModel().getSelectedItem();
        if(movingPerson != null) {
            String uid =  Workspace.getPersonID(movingPerson);
            if(uid != null) {
                newTeamMembers.remove(uid);
                newDevelopers.remove(uid);
                availableMembers.add(uid);
                updateTeamTables();
                //try {
                    appController.addCommand(new AddingNewCommand(new RemoveMember(Workspace.getTeam(currentItem), uid),
                            addingNew));
                //}
                //catch(NullPointerException e) {}
            }
        }
    }

    /**
     * Adds selected team member to developers list
     */
    @FXML
    public void addDeveloper() {
        String movingPerson = Workspace.getPersonID(availableMembersList.getSelectionModel().getSelectedItem());
        if (movingPerson != null) {
            newDevelopers.add(movingPerson);
            //availableDevelopers.remove(movingPerson);
            updateTeamTables();
            appController.addCommand(new AddingNewCommand(new AddDeveloper(Workspace.getTeam(currentItem), movingPerson),
                    addingNew));
        }
    }

    /**
     * Removes selected developer from developers list
     */
    @FXML
    public void removeDeveloper() {
        String movingPerson = Workspace.getPersonID(developerList.getSelectionModel().getSelectedItem());
        if (movingPerson != null) {
            newDevelopers.remove(movingPerson);
            //availableDevelopers.remove(movingPerson);
            updateTeamTables();
            appController.addCommand(new AddingNewCommand(new RemoveDeveloper(Workspace.getTeam(currentItem), movingPerson),
                    addingNew));
        }
    }

    /**
     * Product Owner Combo boxes Action
     * Clears Scrum Master's selection if it is equal to the selected Product Owner.
     * (So long as it is not the blank selection)
     */
    @FXML
    public void poComboBoxAction(){
        if (!preventCombo) {
        if ((smComboBox.getSelectionModel().getSelectedItem() == poComboBox.getSelectionModel().getSelectedItem())
                // Ensures that the blank selection isnt the duplicate
                && (smComboBox.getSelectionModel().getSelectedItem() != "None ")) {
            preventCombo = true;
            smComboBox.getSelectionModel().clearSelection();
            preventCombo = false;
            errorMessageLabel.setText("Team member can only be either a Product Owner or a Scrum Master"); // error errorMessageLabel
        }else{
            errorMessageLabel.setText(""); // Clears error errorMessageLabel when sm & po combo box selection is different
        }
        checkIfNotPoOrSm();
            appController.addCommand(new AddingNewCommand(new ChangeProductOwner(Workspace.getTeam(currentItem),
                    Workspace.getPersonID(poComboBox.getValue())),
                    addingNew));
        }
    }

    /**
     * Scrum Master Combo boxes Action
     * Clears Product Owner's selection if it is equal to the selected Scrum Master.
     * (So long as it is not the blank selection)
     */
    @FXML
    public void smComboBoxAction(){
        if (!preventCombo) {
        if ((poComboBox.getSelectionModel().getSelectedItem() == smComboBox.getSelectionModel().getSelectedItem()
                // Ensures that the blank selection isnt the duplicate
                && (smComboBox.getSelectionModel().getSelectedItem() != "None ")) ) {
            preventCombo = true;
            poComboBox.getSelectionModel().clearSelection();
            preventCombo = false;
            errorMessageLabel.setText("Team member can only be either a Scrum Master or a Product Owner"); //error errorMessageLabel
        }else{
            errorMessageLabel.setText(""); // Clears error errorMessageLabel when sm & po combo box selection is different
        }
        checkIfNotPoOrSm();
            appController.addCommand(new AddingNewCommand(new ChangeScrumMaster(Workspace.getTeam(currentItem),
                    Workspace.getPersonID(smComboBox.getValue())),
                    addingNew));
        }
    }


    /**
     * "Create" and "Edit" button
     * Creates a new team or edits the current team.
     */
    @FXML
    public void doneEditAction()
    {
        // Done button has been clicked
        if (this.isEditing()) {
            displayView();
            WindowController.instance.updateGUI();
            WindowController.instance.updateList(ItemType.TEAM);
            WindowController.sideDisplayListController().setListItem(Workspace.getTeamName(currentItem), ItemType.TEAM);
            clearErrorNotification();
        }
        else{
            editView();
        }
    }

    /**
     * Displays the given team's information in the Team FXML view.
     * @param uid is the unique id of the team to set the Team view to.
     */
    public void setSelected(String uid)
    {
        super.setSelected(uid);
        Team team = Workspace.getTeam(uid);

        // Set text-field values
        itemShortName.setText(team.getShortName());
        teamDescription.setText(team.getDescription());

        newTeamMembers = new ArrayList<>();
        newTeamMembers.addAll(team.getPeople().stream().collect(Collectors.toList()));

        newDevelopers = new ArrayList<>();
        newDevelopers.addAll(team.getDevelopers().stream().collect(Collectors.toList()));

        ObservableList<String> teamMembersList = FXCollections.observableArrayList(Workspace.getPeopleNames(newTeamMembers));
        teamList.setItems(teamMembersList);

        // Set Combo boxes
        preventCombo = true;
        if(team.getProductOwner() != null) {
            poComboBox.getItems().add(Workspace.getPersonName(team.getProductOwner()));
            poComboBox.getSelectionModel().select(Workspace.getPersonName(team.getProductOwner()));
        }
        if(team.getScrumMaster() != null) {
            smComboBox.getItems().add(Workspace.getPersonName(team.getScrumMaster()));
            smComboBox.getSelectionModel().select(Workspace.getPersonName(team.getScrumMaster()));
        }
        preventCombo = false;

        updateTeamTables();

        addUndoableTextInput(teamDescription, CommandType.TEAM_DESCRIPTION);
        updateAllocations();

        // Stop the scroll bar form going to the bottom of the page
        WindowController.instance.setFocusToPanel();
        //mainScrollPane.setVvalue(0);
    }

    /**
     * Makes elements uneditable and changes button to say 'Edit'.
     */
    protected void displayView()
    {
        super.displayView();

        // Make text-fields look unselectable =============================
        itemShortName.getStyleClass().remove("edit-view");
        teamDescription.getStyleClass().remove("edit-view");

        poComboBox.getStyleClass().add("main-uneditable-combobox");
        smComboBox.getStyleClass().add("main-uneditable-combobox");

        itemShortName.getStyleClass().add("display-view");
        teamDescription.getStyleClass().add("display-view");
        teamDescription.getStyleClass().add("text-area-display-view");

        itemShortName.setDisable(true);
        teamDescription.setDisable(true);
        // ====================================================================

        availableMembersList.setVisible(false);
        addDeveloperButton.setVisible(false);
        removeDeveloperButton.setVisible(false);
        availableMembersLabel.setVisible(false);

        // Make combo boxes unselectable
        poComboBox.setDisable(true);
        smComboBox.setDisable(true);

        poComboBox.setStyle("-fx-opacity: 1");
        smComboBox.setStyle("-fx-opacity: 1");

        availablePeople.setVisible(false); // Hide listview of available people
        // Hide add and remove buttons for adding people to team
        addButton.setVisible(false);
        removeButton.setVisible(false);
        availableList.setVisible(false); // Hide available people label

        teamList.getStyleClass().remove("edit-view-listview");
        teamList.getStyleClass().add("display-view-listview");
        teamList.getStyleClass().remove("edit-view");
        teamList.getStyleClass().add("display-view");

        developerList.getStyleClass().remove("edit-view-listview");
        developerList.getStyleClass().add("display-view-listview");
        developerList.getStyleClass().remove("edit-view");
        developerList.getStyleClass().add("display-view");

        // Hide asterisks
        snAsterisk.setVisible(false);

        //mainScrollPane.setVvalue(1);
    }

    /**
     * Give more generic methods access to the item type
     * @return The TEAM item type
     */
    protected ItemType getItemType() {
        return ItemType.TEAM;
    }

    /**
     * Makes elements editable and changes the button to say 'Done'.
     */
    protected void editView()
    {
        super.editView();

        poComboBox.getStyleClass().remove("main-uneditable-combobox");
        smComboBox.getStyleClass().remove("main-uneditable-combobox");

        // Make text-fields look like text-fields =============================
        itemShortName.getStyleClass().remove("display-view");
        teamDescription.getStyleClass().remove("display-view");

        itemShortName.getStyleClass().add("edit-view");
        teamDescription.getStyleClass().add("edit-view");


        // ====================================================================

        itemShortName.setDisable(false);
        teamDescription.setDisable(false);

        //Make combo boxes selectable
        poComboBox.setDisable(false);
        smComboBox.setDisable(false);


        availablePeople.setVisible(true); // Show available people in listview
        // Show add and remove buttons for adding people to team.
        addButton.setVisible(true);
        removeButton.setVisible(true);
        availableList.setVisible(true); // Make available label appear.

        availableMembersList.setVisible(true);
        addDeveloperButton.setVisible(true);
        removeDeveloperButton.setVisible(true);
        availableMembersLabel.setVisible(true);

        teamList.getStyleClass().remove("display-view-listview");
        teamList.getStyleClass().add("edit-view-listview");
        teamList.getStyleClass().remove("display-view");
        teamList.getStyleClass().add("edit-view");

        developerList.getStyleClass().remove("display-view-listview");
        developerList.getStyleClass().add("edit-view-listview");
        developerList.getStyleClass().remove("display-view");
        developerList.getStyleClass().add("edit-view");

        // Show asterisks
        snAsterisk.setVisible(true);
    }

    /**
     * Sets the focus on the Control with the given fxID
     * @param commandMessage CommandMessage to use for knowing what to update
     */
    public void setFocus(CommandMessage commandMessage)
    {
        String fxID = commandMessage.getItemProperty();
        String tab = commandMessage.getTab();

        if ("People".equals(tab)) {
            teamTabPane.getSelectionModel().select(peopleTab);
        }
        else if ("Projects".equals(tab)) {
            teamTabPane.getSelectionModel().select(projectsTab);
        }
        else {
            teamTabPane.getSelectionModel().select(propertiesTab);
        }

        editView();
        switch(fxID) {
            case("itemShortName"):
                itemShortName.requestFocus();
                break;
            case("developerList"):
                developerList.requestFocus();
                break;
            case("availableList"):
                availableList.requestFocus();
                break;
            case("teamDescription"):
                teamDescription.requestFocus();
                break;
            case("teamList"):
                teamList.requestFocus();
                break;
            case("availableMembersList"):
                availableMembersList.requestFocus();
                break;
            case("poComboBox"):
                poComboBox.requestFocus();
                break;
            case("smComboBox"):
                smComboBox.requestFocus();
                break;
        }
    }
}
