package seng302.group6.Controller.Content;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;
import seng302.group6.Controller.Window.WindowController;
import seng302.group6.Debug;
import seng302.group6.Model.Command.AddingNewCommand;
import seng302.group6.Model.Command.CommandMessage;
import seng302.group6.Model.Command.Project.AllocateDevTeam;
import seng302.group6.Model.Command.Project.ChangeBacklog;
import seng302.group6.Model.Command.Project.DeallocateDevTeam;
import seng302.group6.Model.ItemClasses.Allocation;
import seng302.group6.Model.ItemClasses.Project;
import seng302.group6.Model.ItemClasses.Team;
import seng302.group6.Model.ItemClasses.Workspace;
import seng302.group6.Model.ItemType;

import java.time.LocalDate;
import java.util.ArrayList;

/**
 * Controller for the Project frame
 * Created by simon on 18/03/15.
 */
public class ProjectFrameController extends ContentController
{
    @FXML TextField projectName;
    @FXML TextArea projectDescription;
    @FXML ComboBox<String> backlogCombo;
    @FXML Label backlogQuestion;

    @FXML Label snAsterisk;

    @FXML TableView<Allocation> allocatedTeamsTable;
    @FXML TableColumn<Allocation, String> allocatedTeamsNameColumn;
    @FXML TableColumn<Allocation, String> allocatedTeamsStartDateColumn;
    @FXML TableColumn<Allocation, String> allocatedTeamsEndDateColumn;

    @FXML TableView<Allocation> teamHistoryTable;
    @FXML TableColumn<Allocation, String> historyTeamsNameColumn;
    @FXML TableColumn<Allocation, String> historyTeamsStartDateColumn;
    @FXML TableColumn<Allocation, String> historyTeamsEndDateColumn;

    // Team allocation
    @FXML Label startDateLabel;
    @FXML DatePicker startDate;
    @FXML Label endDateLabel;
    @FXML DatePicker endDate;
    @FXML ComboBox<String> teamSelector;
    @FXML Button allocateButton;

    @FXML private TabPane projectTabPane;
    @FXML private Tab propertiesTab;
    @FXML private Tab teamsTab;

    private ArrayList<Allocation> allocatedTeams;

    private ObservableList<String> backlogs;

    private boolean allocating = true;
    private boolean preventCombo = false;

    /**
     * Intializes the Project Frame
     */
    public void initialize() {
        allocatedTeamsNameColumn.setCellValueFactory(param -> new ReadOnlyObjectWrapper<String>(param.getValue().getTeamName()));
        allocatedTeamsStartDateColumn.setCellValueFactory(new PropertyValueFactory<Allocation, String>("startDate"));
        allocatedTeamsEndDateColumn.setCellValueFactory(new PropertyValueFactory<Allocation, String>("endDate"));

        historyTeamsNameColumn.setCellValueFactory(param -> new ReadOnlyObjectWrapper<String>(param.getValue().getTeamName()));
        historyTeamsStartDateColumn.setCellValueFactory(new PropertyValueFactory<Allocation, String>("startDate"));
        historyTeamsEndDateColumn.setCellValueFactory(new PropertyValueFactory<Allocation, String>("endDate"));


        FontAwesomeIcon glyph = new FontAwesomeIcon();
        glyph.setIconName("QUESTION_CIRCLE");
        glyph.setSize("1em");
        backlogQuestion.setGraphic(glyph);

        updateAllocatedTeams();

        populateTeamCombo();
        updateTeamsLists();
    }

    /**
     * Fills the backlog combobox and sets that backlog if there is one
     */
    private void updateBacklogs()
    {
        preventCombo = true;
        ArrayList<String> backlogUids = Workspace.getBacklogs();
        ArrayList<String> backlogNames = new ArrayList<>();
        backlogNames.add(" ");

        for (String name : Workspace.getBacklogNames(backlogUids)) {
            backlogNames.add(name);
        }

        backlogs = FXCollections.observableArrayList(
                backlogNames
        );
        backlogCombo.setItems(backlogs);

        Project project = Workspace.getProject(currentItem);
        if (project.getBacklog() != null) {
            String selectedBacklog = Workspace.getBacklogName(project.getBacklog());
            backlogCombo.getSelectionModel().select(selectedBacklog);
        }
        preventCombo = false;
    }

    /**
     * Sets allocated teams from model
     */
    private void updateAllocatedTeams() {
        String projectUid = Workspace.getCurrentProject();
        Project project = Workspace.getProject(projectUid);
        if (project.getDevTeams() != null) {
            allocatedTeams = project.getDevTeams();
        } else {
            allocatedTeams = new ArrayList<Allocation>();
        }

        updateTeamsLists();
    }

    /**
     * populates the map of text fields, keying them so that they may be
     * referred to by the name reported in validation reports.
     */
    protected void populateFieldMap()
    {
        fieldMap.put("shortName", itemShortName);
        fieldMap.put("longName", projectName);
        fieldMap.put("description", projectDescription);
    }

    /**
     * Backlog ComboBox
     * Updates the allocated backlog on comboBox selection change
     */
    @FXML
    public void backlogSelected()
    {
        if (!preventCombo) {
            try {
                appController.addCommand(new AddingNewCommand(new ChangeBacklog(
                        Workspace.getProject(currentItem),
                        Workspace.getBacklogID(backlogCombo.getSelectionModel().getSelectedItem())),
                        addingNew
                ));
            } catch (NullPointerException e) {
                    appController.addCommand(new AddingNewCommand(new ChangeBacklog(
                            Workspace.getProject(currentItem),
                            null),
                            addingNew
                    ));
            }
        }
    }

    /**
     * Create Button
     * Clicking 'Create' will create a new project object
     */
    @FXML
    public void doneEditAction()
    {
        populateFieldMap();
        clearErrorNotification();

        // The done button has been clicked
        if (this.isEditing()) {
            displayView();
            WindowController.instance.updateGUI();
            WindowController.instance.updateList(ItemType.PROJECT);
            WindowController.sideDisplayListController().setListItem(Workspace.getProjectName(currentItem), ItemType.PROJECT);
            clearErrorNotification();
        } else { // Edit button has been clicked
            editView();
        }
    }

    /**
     * Give more generic methods access to the item type
     * @return The PROJECT item type
     */
    protected ItemType getItemType() {
        return ItemType.PROJECT;
    }

    /**
     * Makes elements uneditable and changes button to say 'edit'
     */
    protected void displayView() {
        super.displayView();

        projectName.getStyleClass().remove("edit-view");
        itemShortName.getStyleClass().remove("edit-view");
        projectDescription.getStyleClass().remove("edit-view");
        projectName.getStyleClass().add("display-view");
        itemShortName.getStyleClass().add("display-view");
        projectDescription.getStyleClass().add("display-view");
        projectDescription.getStyleClass().add("text-area-display-view");

        projectName.setDisable(true);
        itemShortName.setDisable(true);
        projectDescription.setDisable(true);
        backlogCombo.setDisable(true);
        backlogQuestion.setVisible(false);

        startDateLabel.setVisible(false);
        startDate.setVisible(false);
        endDateLabel.setVisible(false);
        endDate.setVisible(false);
        teamSelector.setVisible(false);
        allocateButton.setVisible(false);

        // Hide asterisks
        snAsterisk.setVisible(false);

        updateAllocatedTeams();
        updateBacklogs();
    }

    /**
     * Makes elements editable and changes button to say 'done'
     */
    protected void editView()
    {
        super.editView();

        projectName.getStyleClass().remove("display-view");
        itemShortName.getStyleClass().remove("display-view");
        projectDescription.getStyleClass().remove("display-view");
        projectName.getStyleClass().add("edit-view");
        itemShortName.getStyleClass().add("edit-view");
        projectDescription.getStyleClass().add("edit-view");

        projectName.setDisable(false);
        itemShortName.setDisable(false);
        projectDescription.setDisable(false);

        Project project = Workspace.getProject(currentItem);
        if (!project.hasSprint()) {
            backlogCombo.setDisable(false);
            backlogQuestion.setVisible(false);
        } else {
            backlogQuestion.setVisible(true);
        }

        startDateLabel.setVisible(true);
        startDate.setVisible(true);
        endDateLabel.setVisible(true);
        endDate.setVisible(true);
        teamSelector.setVisible(true);
        allocateButton.setVisible(true);

        setDatePickerTimes();

        // Show asterisks
        snAsterisk.setVisible(true);

        updateAllocatedTeams();
        allocateTeamMode();
        updateBacklogs();
    }

    /**
     * Sets default times for date pickers.
     * Start date is today, and end date is one week later.
     * Also sets DayCellFactory for endDate to make sure dates earlier than
     * the start date are disabled.
     */
    private void setDatePickerTimes() {
        //TODO: make sure times don't coincide with current allocations
        LocalDate today = LocalDate.now();
        LocalDate oneWeek = today.plusWeeks(1);
        startDate.setValue(today);
        endDate.setValue(oneWeek);

        // Make sure end date can't be earlier than start date
        final Callback<DatePicker, DateCell> dayCellFactory =
                new Callback<DatePicker, DateCell>() {
                    @Override
                    public DateCell call(final DatePicker datePicker) {
                        return new DateCell() {
                            @Override
                            public void updateItem(LocalDate item, boolean empty) {
                                super.updateItem(item, empty);
                                if (item.isBefore(startDate.getValue())) {
                                    setDisable(true);
                                    setStyle("-fx-background-color: #ffc0cb;");
                                }
                            }
                        };
                    }
                };
        endDate.setDayCellFactory(dayCellFactory);
    }

    /**
     * Called when the value of the startDate or endDate is changed.
     * Makes sure that endDate is not before startDate.
     */
    @FXML
    public void dateAction() {
        if (startDate.getValue() != null && endDate.getValue() != null) {
            if (endDate.getValue().isBefore(startDate.getValue())) {
                endDate.setValue(startDate.getValue());
            }
        }
        populateTeamCombo();
    }

    /**
     * Sets the current project selected from the list
     *
     * @param uid currently selected project from list
     */
    public void setSelected(String uid) {
        super.setSelected(uid);
        // Set text-field values
        setFields(uid);
        addUndoableTextInput(projectName, CommandType.PROJECT_NAME);
        addUndoableTextInput(projectDescription, CommandType.PROJECT_DESCRIPTION);
    }

    /**
     * Set the fields to attributes from a given project.
     *
     * @param uid is the unique id of the Project to get attributes from
     */
    private void setFields(String uid) {
        Project p = Workspace.getProject(uid);
        projectName.setText(p.getLongName());
        itemShortName.setText(p.getShortName());
        projectDescription.setText(p.getDescription());
    }

    /**
     * Populates the Combo box depending on the dates selected.
     */
    private void populateTeamCombo()
    {
        //Gets the dates from date pickers
        LocalDate selectedStart = startDate.getValue();
        LocalDate selectedEnd = endDate.getValue();
        // Finds the dtaes without projects inside the available dates
        ArrayList<String> teamsWithoutProject = new ArrayList<>();
        for (String teamUid : Workspace.getTeams()) {
            if (Workspace.getTeam(teamUid).canAllocateForDates(
                    selectedStart, selectedEnd)) {
                teamsWithoutProject.add(Workspace.getTeamName(teamUid));
            }
        }
        teamSelector.setItems(FXCollections.observableList(teamsWithoutProject));

    }

    /**
     * Sets the teamSelector combo box to contain all teams which aren't
     * allocated to a project
     */
    private void updateTeamsLists() {


        ArrayList<Allocation> teamHistory = new ArrayList<>();
        ArrayList<Allocation> teamCurrent = new ArrayList<>();

        for (Allocation allocation : allocatedTeams) {

            LocalDate end = allocation.getEndDate();
            if (end.isBefore(LocalDate.now())) {
                // Currently allocated team
                teamHistory.add(allocation);
            } else {
                // Previously allocated team
                teamCurrent.add(allocation);
            }
        }
        allocatedTeamsTable.setItems(FXCollections.observableList(teamCurrent));
        teamHistoryTable.setItems(FXCollections.observableList(teamHistory));

    }

    /**
     * Switches to deallocate mode if an allocated team was selected
     */
    @FXML
    public void teamClicked() {
        if (allocatedTeamsTable.getSelectionModel().getSelectedItem() != null) {
            deallocateTeamMode();
        }
    }

    /**
     * Switches to allocate mode if a team was selected in the team selector
     */
    @FXML
    public void teamSelected()
    {
        if (teamSelector.getSelectionModel().getSelectedItem() != null) {
            allocateTeamMode();
        }
    }

    /**
     * Enables date pickers and sets button text to "Allocate"
     */
    private void allocateTeamMode()
    {
        startDate.setDisable(false);
        endDate.setDisable(false);
        allocateButton.setText("Allocate");
        allocating = true;
    }

    /**
     * Disables date pickers and sets button text to "Deallocate"
     */
    private void deallocateTeamMode()
    {
        startDate.setDisable(true);
        endDate.setDisable(true);
        teamSelector.getSelectionModel().select(null);
        allocateButton.setText("Deallocate");
        allocating = false;
    }

    /**
     * Allocates a team to a project, i.e. adds the selected team to
     * allocatedTeams
     */
    @FXML
    public void allocateTeam()
    {
        if (allocating) { // Allocate button was pressed
            String teamName = teamSelector.getSelectionModel().getSelectedItem();
            LocalDate start = startDate.getValue();
            LocalDate end = endDate.getValue();

            if (teamName != null) {
                String uid = Workspace.getTeamID(teamName);
                if (uid != null && start != null && end != null) {
                    appController.addCommand(new AddingNewCommand(new AllocateDevTeam(
                            Workspace.getProject(currentItem), uid,
                            start, end),
                            addingNew
                    ));
                    updateTeamsLists();
                    Workspace.getProject(currentItem).updateTeams();
                }
            }
        } else { // Deallocate button was pressed
            String teamName = allocatedTeamsTable.getSelectionModel().getSelectedItem().getTeamName();
            if (teamName != null) {
                String uid = Workspace.getTeamID(teamName);
                if (uid != null) {
                    Team team = Workspace.getTeam(uid);
                    if (team.hasSprint()) {
                        errorMessageLabel.setText("This team cannot be deallocated because it is in a sprint.");
                    }
                    else {
                        Allocation selectedAllocation = allocatedTeamsTable.getSelectionModel().getSelectedItem();
                        appController.addCommand(new AddingNewCommand(new DeallocateDevTeam(
                                Workspace.getProject(currentItem), uid,
                                selectedAllocation.getStartDate(),
                                selectedAllocation.getEndDate()),
                                addingNew
                        ));
                        updateTeamsLists();
                    }
                }
                allocateTeamMode();
            }
        }
        populateTeamCombo();
    }



    /**
     * Sets the focus on the Control with the given fxID
     * @param commandMessage Command Message etc.
     */
    public void setFocus(CommandMessage commandMessage)
    {
        String fxID = commandMessage.getItemProperty();
        String tab = commandMessage.getTab();

        if ("Teams".equals(tab)) {
            projectTabPane.getSelectionModel().select(teamsTab);
        }
        else {
            projectTabPane.getSelectionModel().select(propertiesTab);
        }

        editView();
        switch(fxID) {
            case("itemShortName"):
                itemShortName.requestFocus();
                break;
            case("projectName"):
                projectName.requestFocus();
                break;
            case("projectDescription"):
                projectDescription.requestFocus();
                break;
            case("previousTeamsList"):
                teamHistoryTable.requestFocus();
                break;
            case("availableList"):
                allocatedTeamsTable.requestFocus();
                break;
            case ("backlogCombo"):
                backlogCombo.requestFocus();
                break;
        }
    }
}

