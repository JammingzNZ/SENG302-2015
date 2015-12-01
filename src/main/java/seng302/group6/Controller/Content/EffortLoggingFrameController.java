package seng302.group6.Controller.Content;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.util.Callback;
import seng302.group6.Controller.AppController;
import seng302.group6.Model.Command.Story.Tasks.AddTaskEffort;
import seng302.group6.Model.Command.Story.Tasks.DeleteTaskEffort;
import seng302.group6.Model.Command.Story.Tasks.TaskTab;
import seng302.group6.Model.ItemClasses.*;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;


/**
 * Controller for the effort logging frame
 * Created by Michael Wheeler on 17/09/15.
 */
public class EffortLoggingFrameController {

    @FXML RadioButton intervalRadio;
    @FXML RadioButton amountRadio;
    @FXML ComboBox<String> personCombo;
    @FXML DatePicker startDate;
    @FXML TextField startHour;
    @FXML TextField startMinute;
    @FXML DatePicker endDate;
    @FXML TextField endHour;
    @FXML TextField endMinute;
    @FXML TextField effortSpent;
    @FXML TextArea commentTextArea;
    @FXML Label spentLabel;
    @FXML Label endLabel;
    @FXML Button addEffort;
    @FXML Button deleteEffort;
    @FXML Label errorMessageLabel;
    @FXML TableColumn<Effort, String> pastDateColumn;
    @FXML TableColumn<Effort, String> pastPersonColumn;
    @FXML TableColumn<Effort, Integer> pastSpentColumn;
    @FXML TableColumn<Effort, String> pastCommentColumn;
    @FXML TableView<Effort> pastEffortTable;
    @FXML Button doneButton;
    private Boolean intervalMode;
    private Task task;
    private Story story;
    private Sprint sprint;
    private ScrumboardStoryController scrumboardStoryController = null;
    private SprintFrameController sprintFrameController = null;


    /**
     * Intilizes the EffortLogging Frame
     */
    public void initialize(){
        // Bind radio buttons
        final ToggleGroup radioButtons = new ToggleGroup();
        intervalRadio.setToggleGroup(radioButtons);
        amountRadio.setToggleGroup(radioButtons);
        // Set interval as default
        intervalRadio.setSelected(true);
        intervalAddMode();

        // Initialize table columns
        pastDateColumn.setCellValueFactory(new PropertyValueFactory<>("startDate"));
        pastSpentColumn.setCellValueFactory(new PropertyValueFactory<>("minutes"));
        pastPersonColumn.setCellValueFactory(new PropertyValueFactory<>("person"));
        pastCommentColumn.setCellValueFactory(new PropertyValueFactory<>("comment"));

        startHour.textProperty().addListener(makeTimeListener(startHour, 23, 2));
        startMinute.textProperty().addListener(makeTimeListener(startMinute, 59, 2));
        endHour.textProperty().addListener(makeTimeListener(endHour, 23, 2));
        endMinute.textProperty().addListener(makeTimeListener(endMinute, 59, 2));
        effortSpent.textProperty().addListener(makeTimeListener(effortSpent, Integer.MAX_VALUE, 9));

        pastEffortTable.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Effort>() {
            @Override
            public void changed(ObservableValue<? extends Effort> observable, Effort oldValue, Effort newValue) {
                deleteEffort.setDisable(newValue == null);
            }
        });
    }


    /**
     * Returns ChangeListener that allows only numbers under a given value to be typed into the given text field
     * @param timeField TextField to add listener to
     * @param maxTime Max number
     * @param maxDigits Max digits
     * @return ChangeListener
     */
    private ChangeListener<String> makeTimeListener(TextField timeField, Integer maxTime, Integer maxDigits) {
        ChangeListener<String> timeListener = (ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            if (!newValue.isEmpty()) {
                if (!newValue.matches("\\d*") || newValue.length() > maxDigits) {
                    timeField.setText(oldValue);
                } else {
                    Integer time = Integer.parseInt(newValue);
                    if (time > maxTime || time < 0) {
                        timeField.setText(oldValue);
                    }
                }
            }
        };
        return timeListener;
    }

    /**
     * Amount mode radio button
     * Switches window to spent amount mode
     */
    @FXML
    public void amountRadioAction(){ spentAmountAddMode(); }

    /**
     * Interval mode radio button
     * Switches window to interval mode
     */
    @FXML
    public void intervalRadioAction(){ intervalAddMode(); }


    @FXML
    public void personComboAction(){

    }

    /**
     * Adds effort to Task
     */
    @FXML
    public void addEffortAction(){

        Effort newEffort;

        Integer startH = 0;
        Integer startM = 0;
        Integer endH = 0;
        Integer endM = 0;
        Integer spent = 0;
        DecimalFormat df = new DecimalFormat("#.##");

        if (!startHour.getText().isEmpty()) {
            startH = Integer.parseInt(startHour.getText());
        }
        if (!startMinute.getText().isEmpty()) {
            startM = Integer.parseInt(startMinute.getText());
        }
        if (!endHour.getText().isEmpty()) {
            endH = Integer.parseInt(endHour.getText());
        }
        if (!endMinute.getText().isEmpty()) {
            endM = Integer.parseInt(endMinute.getText());
        }
        if (!effortSpent.getText().isEmpty()) {
            spent = Integer.parseInt(effortSpent.getText());
        }

        LocalDate selectedStartDate = startDate.getValue();
        LocalDateTime startDateTime = LocalDateTime.of(selectedStartDate, LocalTime.of(startH, startM));

        LocalDate selectedEndDate = endDate.getValue();
        LocalDateTime endDateTime = LocalDateTime.of(selectedEndDate, LocalTime.of(endH, endM));

        //Validation
        if (startDateTime.isAfter(endDateTime) && intervalMode) {
            errorMessageLabel.setText("Start date & time cannot be after end date & time");
        }
        else if (personCombo.getValue() == null) {
            errorMessageLabel.setText("A person must be specified");
        }
        else {
            errorMessageLabel.setText("");

            if (intervalMode) {
                newEffort = new Effort(personCombo.getSelectionModel().getSelectedItem(),
                        commentTextArea.getText(), startDateTime, endDateTime);
            } else {
                newEffort = new Effort(personCombo.getSelectionModel().getSelectedItem(),
                        commentTextArea.getText(), startDateTime, spent);
            }

            Double effortLeft = task.getEffortLeft() - (newEffort.getMinutes() / 60.0);
            effortLeft = Double.parseDouble(df.format(effortLeft));
            if (effortLeft < 0) effortLeft = 0.0;

            if (scrumboardStoryController != null) {
                AppController.instance().addCommand(new AddTaskEffort(sprint, task, newEffort, story, effortLeft, TaskTab.SCRUM_BOARD));
            } else {
                AppController.instance().addCommand(new AddTaskEffort(sprint, task, newEffort, story, effortLeft, TaskTab.ALL_TASKS));
            }
            populatePastEntriesTable();

            setDefaultDates();
            effortSpent.clear();
            commentTextArea.clear();
            personCombo.setValue(null);
            if (sprintFrameController != null){
                sprintFrameController.populateAllTasksTable();
            }

            if (scrumboardStoryController != null) {
                scrumboardStoryController.populateTaskTable();
                scrumboardStoryController.updateProgressBars();
            }
        }
    }

    /**
     * Delete Effort button action
     * Deletes the selected effort in the effort table
     */
    @FXML
    protected void deleteEffortAction()
    {
        ObservableList<Effort> effort = pastEffortTable.getSelectionModel().getSelectedItems();
        for(Effort effort1 : effort){
            if (scrumboardStoryController != null) {
                AppController.instance().addCommand(new DeleteTaskEffort(sprint, task, effort1, story, TaskTab.SCRUM_BOARD));
            } else {
                AppController.instance().addCommand(new DeleteTaskEffort(sprint, task, effort1, story, TaskTab.ALL_TASKS));
            }
        }
        populatePastEntriesTable();
        if (scrumboardStoryController != null) {
            scrumboardStoryController.populateTaskTable();
            scrumboardStoryController.updateProgressBars();
        }
        if (sprintFrameController != null){
            sprintFrameController.populateAllTasksTable();
        }
    }

    /**
     * Changes the window into interval mode
     * (Adding effort using date interval)
     */
    private void intervalAddMode(){
        intervalMode = true;
        endDate.setDisable(false);
        endMinute.setDisable(false);
        endHour.setDisable(false);
        endLabel.setDisable(false);
        effortSpent.setDisable(true);
        spentLabel.setDisable(true);
    }

    /**
     * Changes the window into spent amount mode
     * (Adding effort using start date and amount of time)
     */
    private void spentAmountAddMode(){
        intervalMode = false;
        endLabel.setDisable(true);
        endDate.setDisable(true);
        endHour.setDisable(true);
        endMinute.setDisable(true);
        effortSpent.setDisable(false);
        spentLabel.setDisable(false);
    }

    /**
     * Used by ScrumboardStoryController to pass the task to add/view effort.
     * @param task Task to add/view effort
     */
    public void setTask(Task task){
        this.task = task;
        populatePastEntriesTable();
    }

    /**
     * Used by ScrumboardStoryController to pass the parent story
     * @param story story
     */
    public void setStory(Story story) {
        this.story = story;
    }

    /**
     * Used by ScrumboardStoryController to pass the parent sprint
     * @param sprint sprint
     */
    public void setSprint(Sprint sprint){
        this.sprint = sprint;

        populatePersonCombobox();
        setDefaultDates();

        LocalDate sprintStartDate = LocalDate.parse(sprint.getStartDate());
        LocalDate sprintEndDate = LocalDate.parse(sprint.getEndDate());

        final Callback<DatePicker, DateCell> dayCellFactory =
                new Callback<DatePicker, DateCell>() {
                    @Override
                    public DateCell call(final DatePicker datePicker) {
                        return new DateCell() {
                            @Override
                            public void updateItem(LocalDate item, boolean empty) {
                                super.updateItem(item, empty);
                                if (item.isBefore(sprintStartDate) || item.isAfter(sprintEndDate)) {
                                    setDisable(true);
                                    setStyle("-fx-background-color: #ffc0cb;");
                                }
                            }
                        };
                    }
                };

        startDate.setDayCellFactory(dayCellFactory);
        endDate.setDayCellFactory(dayCellFactory);
    }

    /**
     * Sets the Parent FXML to the effort logging frame
     * @param scrumboardStoryController scrumboard story controller
     */
    public void setParent(ScrumboardStoryController scrumboardStoryController) {
        this.scrumboardStoryController = scrumboardStoryController;
    }

    /**
     * Populates person ComboBox with members of the team assigned to the
     * sprint that the task is in
     */
    public void populatePersonCombobox(){
        Team sprintTeam = Workspace.getTeam(sprint.getAssociatedTeam());
        ArrayList<String> people = new ArrayList<>();
        for(String person : sprintTeam.getPeople()){
            if (Workspace.getPerson(person) != null) {
                people.add(Workspace.getPerson(person).getShortName());
            }
        }
        personCombo.setItems(FXCollections.observableList(people));
    }

    /**
     * Populates the past entries table with the Task's past effort entries
     */
    public void populatePastEntriesTable(){
        List<Effort> pastEffort = task.getEffortList();
        if(pastEffort != null){
            pastEffortTable.setItems(FXCollections.observableList(pastEffort));
        }
        pastEffortTable.getSelectionModel().clearSelection();
    }

    /**
     * Give the datepickers default values based on the current date and the sprint dates
     */
    public void setDefaultDates() {
        LocalDateTime defaultDate = LocalDateTime.now();
        LocalDateTime sprintStartDate = LocalDateTime.of(LocalDate.parse(sprint.getStartDate()), LocalTime.of(0, 0));
        LocalDateTime sprintEndDate = LocalDateTime.of(LocalDate.parse(sprint.getEndDate()), LocalTime.of(23, 59));

        if (defaultDate.isBefore(sprintStartDate)) {
            defaultDate = sprintStartDate;
        }
        else if (defaultDate.isAfter(sprintEndDate)) {
            defaultDate = sprintEndDate;
        }

        startDate.setValue(defaultDate.toLocalDate());
        startHour.setText(Integer.toString(defaultDate.getHour()));
        startMinute.setText(Integer.toString(defaultDate.getMinute()));
        endDate.setValue(defaultDate.toLocalDate());
        endHour.setText(Integer.toString(defaultDate.getHour()));
        endMinute.setText(Integer.toString(defaultDate.getMinute()));
    }

    /**
     * Closes the dialog
     */
    @FXML
    public void doneButtonAction(){
        Stage dialog = (Stage) doneButton.getScene().getWindow();
        dialog.close();
    }

    public void setParentSprintFC(SprintFrameController sprintFrameController){
        this.sprintFrameController = sprintFrameController;
    }


}
