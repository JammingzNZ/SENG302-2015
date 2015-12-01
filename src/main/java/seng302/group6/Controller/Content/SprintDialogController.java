package seng302.group6.Controller.Content;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.util.Callback;
import seng302.group6.Controller.Window.SideDisplayListController;
import seng302.group6.Controller.Window.WindowController;
import seng302.group6.Model.ItemClasses.Allocation;
import seng302.group6.Model.ItemClasses.Project;
import seng302.group6.Model.ItemClasses.Workspace;
import seng302.group6.Model.ItemType;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;

/**
 * Created by wheeler on 23/09/15.
 */
public class SprintDialogController {

    @FXML Label projectQuestion;
    @FXML Label releaseQuestion;
    @FXML Label endDateQuestion;
    @FXML Label startDateQuestion;
    @FXML Label teamQuestion;
    @FXML Label teamWarning;
    @FXML ComboBox<String> projectCombo;
    @FXML ComboBox<String> releaseCombo;
    @FXML DatePicker startDatePicker;
    @FXML DatePicker endDatePicker;
    @FXML ComboBox<String> teamCombo;

    @FXML Label releaseComboLabel;

    @FXML Label errorMessageLabel;

    @FXML Button cancelButton;
    @FXML Button createButton;

    boolean preventCombo = false;
    private WindowController windowController;
    private SideDisplayListController sideDisplayListController;
    private ContentController contentController;





    public void initialize(){
        endDatePicker.setEditable(false);
        startDatePicker.setEditable(false);

        // Setup help labels
        addGlyph(projectQuestion);
        addGlyph(releaseQuestion);
        addGlyph(startDateQuestion);
        addGlyph(endDateQuestion);
        addGlyph(teamQuestion);
        FontAwesomeIcon glyph = new FontAwesomeIcon();
        glyph.setIconName("EXCLAMATION_CIRCLE");
        glyph.setSize("1em");
        glyph.setGlyphStyle("-fx-fill: red");
        teamWarning.setGraphic(glyph);

        teamCombo.valueProperty().addListener(
                (observable, oldValue, newValue) -> {
                    if (newValue != null) {
                        showTeamWarning(Workspace.getTeamID(newValue));
                    } else {
                        showTeamWarning(null);
                    }
                }
        );

        startDatePicker.valueProperty().addListener(
                (observable, oldValue, newValue) -> {
                    if (teamCombo.getValue() != null) {
                        showTeamWarning(Workspace.getTeamID(teamCombo.getValue()));
                    }
                }
        );

        endDatePicker.valueProperty().addListener(
                (observable, oldValue, newValue) -> {
                    if (teamCombo.getValue() != null) {
                        showTeamWarning(Workspace.getTeamID(teamCombo.getValue()));
                    }
                }
        );

        releaseCombo.setDisable(true);
        startDatePicker.setDisable(true);
        endDatePicker.setDisable(true);
        teamCombo.setDisable(true);
        teamWarning.setVisible(false);

        updateProjectCombo();
    }

    @FXML
    public void cancelButtonAction(){
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }

    @FXML
    public void createButtonAction() throws Exception{
        String project = projectCombo.getSelectionModel().getSelectedItem();
        String release = releaseCombo.getSelectionModel().getSelectedItem();
        String startDate = null;
        if(startDatePicker.getValue() != null) {
            startDate = startDatePicker.getValue().toString();
        }
        String endDate = null;
        if (endDatePicker.getValue() != null){
            endDate = endDatePicker.getValue().toString();
        }
        String team = teamCombo.getSelectionModel().getSelectedItem();
        if (project == null || release == null || startDate == null || endDate == null || team == null){
            errorMessageLabel.setText("All fields are required to create a sprint");
        } else {
            errorMessageLabel.setText("");
            String uid = Workspace.createSprint(project, release, startDate, endDate, team);
            windowController.setMainView(ItemType.SPRINT, Workspace.getSprintName(uid));
            sideDisplayListController.displayItem(ItemType.SPRINT);
            windowController.updateGUI();
            windowController.selected_mpc.doneEditAction();
            windowController.selected_mpc.addingNew();
            Stage stage = (Stage) createButton.getScene().getWindow();
            stage.close();
        }
    }


    @FXML
    public void projectComboAction(){

        if (!preventCombo) {

            // If a project was selected, enable the release combobox
            if (projectCombo.getValue() != null) {
                releaseCombo.setDisable(false);
            }

            startDatePicker.setDisable(true);
            startDatePicker.setValue(null);
            endDatePicker.setDisable(true);
            endDatePicker.setValue(null);
            teamCombo.setDisable(false);
            teamCombo.setValue(null);
            updateReleaseCombo();
            updateTeamCombo();


        }
    }

    @FXML
    public void releaseComboAction(){
        if (!preventCombo) {
            if(releaseCombo.getValue() != null) {
                // Enable DatePickers
                startDatePicker.setDisable(false);
                endDatePicker.setDisable(false);
                startDatePicker.setValue(null);
                endDatePicker.setValue(null);
                // Restrict the date selection between now and the end of the release.
                LocalDate afterDate = LocalDate.parse(Workspace.getRelease(Workspace.getReleaseID(releaseCombo.getValue())).getReleaseDate());
                disableDates(LocalDate.MIN, afterDate, startDatePicker);
                disableDates(LocalDate.MIN, afterDate, endDatePicker);
            }
        }
    }

    @FXML
    public void endDateAction(){
        if(!preventCombo) {
            LocalDate beforeDate = LocalDate.MIN;
            // Disable start dates after the new end date - and dates before the current date
            disableDates(beforeDate, endDatePicker.getValue(), startDatePicker);
        }
    }

    @FXML
    public void startDateAction(){
        if(!preventCombo) {
            if(releaseCombo.getValue() != null) {
                LocalDate afterDate = LocalDate.parse(Workspace.getRelease(Workspace.getReleaseID(releaseCombo.getValue())).getReleaseDate());
                // Disable end dates before the new start date - and dates after the release end date
                disableDates(startDatePicker.getValue(), afterDate, endDatePicker);
            }
        }
    }

    @FXML
    public void teamComboAction(){

    }

    /**
     * Disables the dates before and after the given dates for the given DatePicker
     * @param before date to disable before
     * @param after date to disable after
     * @param datePicker datepicker to disable dates on
     */
    public void disableDates(LocalDate before, LocalDate after, DatePicker datePicker){
        final Callback<DatePicker, DateCell> dayCellFactory =
                new Callback<DatePicker, DateCell>() {
                    @Override
                    public DateCell call(final DatePicker datePicker) {
                        return new DateCell() {
                            @Override
                            public void updateItem(LocalDate item, boolean empty) {
                                super.updateItem(item, empty);
                                if(before != null && after != null){
                                    if(item.isBefore(before) || item.isAfter(after)){
                                        setDisable(true);
                                        setStyle("-fx-background-color: #ffc0cb;");
                                    }
                                }
                            }
                        };
                    }
                };
        datePicker.setDayCellFactory(dayCellFactory);
    }

    /**
     * Add a question mark glyph to the given label
     * @param label Label to add glyph to
     */
    private void addGlyph(Label label) {
        FontAwesomeIcon glyph = new FontAwesomeIcon();
        glyph.setIconName("QUESTION_CIRCLE");
        glyph.setSize("1em");
        label.setGraphic(glyph);
    }

    public void updateProjectCombo()
    {
        preventCombo = true;
        ObservableList<String> projects =
                FXCollections.observableArrayList(Workspace.getProjectNames(Workspace.getProjectsWithBacklog()));
        if (projects.size() == 0){
            projectCombo.setPromptText("None Available");
            projectCombo.setDisable(true);
        } else {
            projectCombo.setPromptText("");
            projectCombo.setDisable(false);
            projectCombo.setItems(projects);
        }
        preventCombo = false;
    }

    /**
     * Update Release ComboBox
     * Updates the release ComboBox with the available values. Selects the current items
     * associated release if it has one.
     */
    public void updateReleaseCombo()
    {
        preventCombo = true;
        releaseCombo.setValue(null);
        if (projectCombo.getSelectionModel().getSelectedItem() != null) {
            ObservableList<String> releases = FXCollections.observableArrayList(
                    Workspace.getReleaseNames(Workspace.getProject(Workspace.getProjectID(
                            projectCombo.getSelectionModel().getSelectedItem())).getUnfinishedReleases()));
            if (releases.size() == 0){
                releaseCombo.setDisable(true);
                releaseCombo.setPromptText("None Available");
                teamCombo.setDisable(true);
                teamCombo.setPromptText("");
            } else {
                releaseCombo.setDisable(false);
                releaseCombo.setPromptText("");
                releaseCombo.setItems(releases);
                teamCombo.setDisable(false);
            }
        }
        preventCombo = false;
    }

    /**
     * Update Team ComboBox
     * Updates the team ComboBox with the available values. Selects the current items
     * associated team if it has one.
     */
    public void updateTeamCombo()
    {
        preventCombo = true;

        if (projectCombo.getValue() != null && !teamCombo.isDisable()) {
            HashSet<String> teams = new HashSet<>();
            ArrayList<Allocation> devTeams = Workspace.getProject(Workspace.getProjectID(projectCombo.getSelectionModel().getSelectedItem())).getDevTeams();
            for (Allocation team : devTeams) {
                teams.add(Workspace.getTeamName(team.getTeamUID()));
            }
            ObservableList<String> availableTeams = FXCollections.observableArrayList(teams);
            availableTeams.sort((o1, o2) -> o1.compareTo(o2));
            if (availableTeams.size() == 0){
                teamCombo.setDisable(true);
                teamCombo.setPromptText("None Available");
            } else {
                teamCombo.setDisable(false);
                teamCombo.setPromptText("");
                teamCombo.setItems(availableTeams);
            }
        }
        preventCombo = false;
    }

    /**
     * Make the team warning icon visible or invisible based on whether the team is allocated at the right time
     * @param team ID of the team to test
     */
    private void showTeamWarning(String team) {

        Boolean showWarning = true;
        if (team != null) {
            if (endDatePicker.getValue() != null && startDatePicker.getValue() != null) {
                Project project = Workspace.getProject(Workspace.getProjectID(projectCombo.getValue()));
                for (Allocation allocation : project.getDevTeams()) {
                    if (allocation.getTeamUID().equals(team)) {
                        if (!allocation.getEndDate().isBefore(endDatePicker.getValue()) &&
                                !allocation.getStartDate().isAfter(startDatePicker.getValue())) {
                            showWarning = false;
                        }
                    }
                }
            }
        }
        else {
            showWarning = false;
        }
        teamWarning.setVisible(showWarning);
    }

    public void setWindowController(WindowController windowController) {
        this.windowController = windowController;
        sideDisplayListController = WindowController.sideDisplayListController();
    }

    public void setContentController(ContentController contentController){
        this.contentController = contentController;
    }

}
