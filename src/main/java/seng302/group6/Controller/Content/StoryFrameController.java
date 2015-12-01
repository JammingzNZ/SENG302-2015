package seng302.group6.Controller.Content;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.util.Callback;
import seng302.group6.Controller.Window.WindowController;
import seng302.group6.CustomDoubleConverter;
import seng302.group6.Debug;
import seng302.group6.Model.Command.AddingNewCommand;
import seng302.group6.Model.Command.CommandMessage;
import seng302.group6.Model.Command.Story.AcceptanceCriteria.AddAcceptanceCriteria;
import seng302.group6.Model.Command.Story.AcceptanceCriteria.ChangeAcceptanceCriteria;
import seng302.group6.Model.Command.Story.AcceptanceCriteria.MoveAcceptanceCriteria;
import seng302.group6.Model.Command.Story.AcceptanceCriteria.RemoveAcceptanceCriteria;
import seng302.group6.Model.Command.Story.ChangeStoryEstimate;
import seng302.group6.Model.Command.Story.ChangeStoryReadiness;
import seng302.group6.Model.Command.Story.ChangeStoryScale;
import seng302.group6.Model.Command.Story.Dependencies.AddDependency;
import seng302.group6.Model.Command.Story.Dependencies.RemoveDependency;
import seng302.group6.Model.Command.Story.Tasks.*;
import seng302.group6.Model.EstimationScale.ScaleType;
import seng302.group6.Model.ItemClasses.*;
import seng302.group6.Model.ItemType;
import seng302.group6.View.DragAndDropCell;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static javafx.scene.input.KeyCode.ENTER;

/**
 * Main pane controller for Story pane
 * Created by simon on 21/05/15.
 */
public class StoryFrameController extends ContentController
{
    @FXML TextField storyLongName;
    @FXML TextArea storyDescription;
    @FXML Label shortNameLabel;
    @FXML Label storyCreator;
    @FXML ListView<AcceptanceCriteria> storyACList;
    @FXML TextArea storyACText;
    @FXML VBox ACControls;
    @FXML Button storyACAdd;
    @FXML Button deleteACButton;
    @FXML Button cancelACButton;
    @FXML ComboBox estimateCombo;
    @FXML ComboBox scaleCombo;
    @FXML CheckBox readyCheck;
    @FXML Label scaleLabel;
    @FXML Label estimateLabel;
    @FXML Label readyLabel;
    @FXML ListView<String> dependenciesList;
    @FXML ListView<String> availableStoriesList;
    @FXML Button addDependencyButton;
    @FXML Button removeDependencyButton;
    @FXML Label availableStoriesLabel;
    @FXML Label availableStoriesQuestion;
    @FXML TableView<Task> taskTable;
    @FXML Button addTaskButton;
    @FXML Button deleteTaskButton;
    @FXML TextField taskName;
    @FXML TextField taskDescription;
    @FXML Label addTaskLabel;
    @FXML VBox storyVbox;
    @FXML TableColumn<Task, String> taskTableShortNameColumn;
    @FXML TableColumn<Task, String> taskTableDescriptionColumn;
    @FXML TableColumn<Task, Double> taskTableEstimateColumn;

    @FXML TextField taskEstimate;

    @FXML Label snAsterisk;

    @FXML TabPane storyTabPane;
    @FXML Tab propertiesTab;
    @FXML Tab acTab;
    @FXML Tab dependenciesTab;
    @FXML Tab tasksTab;

    ObservableList<AcceptanceCriteria> acceptanceCriteria;

    boolean preventCombo = false;

    private ArrayList<String> availableStories = new ArrayList<>();
    private ArrayList<String> dependentStories = new ArrayList<>();

    private EventHandler<ActionEvent> action;

    /**
     * Perform initial setup after object has been created
     */
    public void initialize()
    {
        availableStories = Workspace.getStories();
        availableStoriesList.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        availableStoriesList.setEditable(false);
        dependenciesList.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        dependenciesList.setEditable(false);

        storyACList.setCellFactory(param -> new DragAndDropCell(this));
        storyACList.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>(){
            @Override
            public void changed(ObservableValue<? extends Number> arg0, Number oldPropertyValue,
                                Number newPropertyValue) {
                if (newPropertyValue == Integer.valueOf(-1) || newPropertyValue == null) {
                    deleteACButton.setDisable(true);
                    cancelACButton.setDisable(true);
                    storyACAdd.setText("Add Criteria");
                }
                else {
                    deleteACButton.setDisable(false);
                    cancelACButton.setDisable(false);
                    storyACAdd.setText("Save Changes");
                    storyACText.setText(storyACList.getSelectionModel().getSelectedItem().getText());
                }
            }
        });

        scaleCombo.disabledProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if (newValue) {
                    scaleCombo.getStyleClass().add("main-uneditable-combobox");
                }
                else {
                    scaleCombo.getStyleClass().remove("main-uneditable-combobox");
                }
            }
        });

        estimateCombo.disabledProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if (newValue) {
                    estimateCombo.getStyleClass().add("main-uneditable-combobox");
                }
                else {
                    estimateCombo.getStyleClass().remove("main-uneditable-combobox");
                }
            }
        });

        // make the contents of the task table scale nicely
        taskTable.widthProperty().addListener((observableValue, oldSceneWidth, newSceneWidth) -> {
            taskDescriptionViewMode();
        });

        // Maps the task table columns to task properties
        taskTableShortNameColumn.setCellValueFactory(new PropertyValueFactory<Task, String>("shortName"));
        taskTableDescriptionColumn.setCellValueFactory(new PropertyValueFactory<Task, String>("description"));
        taskTableEstimateColumn.setCellValueFactory(new PropertyValueFactory<Task, Double>("estimation"));

        taskTableShortNameColumn.setCellFactory(TextFieldTableCell.<Task>forTableColumn());


        // Adds command for when editing task name in table
        taskTableShortNameColumn.setOnEditCommit(
                t -> {
                    appController.addCommand(new AddingNewCommand(new ChangeTaskName(
                            Workspace.getStory(currentItem),
                            taskTable.getSelectionModel().getSelectedItem(),
                            t.getNewValue()),
                            addingNew));
                    taskTable.requestFocus();
                }
        );

        // Adds command for when editing task description in table
        taskTableDescriptionColumn.setOnEditCommit(
                t -> {
                    appController.addCommand(new AddingNewCommand(new ChangeTaskDescription(
                            Workspace.getStory(currentItem),
                            taskTable.getSelectionModel().getSelectedItem(),
                            t.getNewValue()),
                            addingNew));
                    taskTable.requestFocus();
                }
        );

        // Adds command for when editing estimation name in table
        taskTableEstimateColumn.setOnEditCommit(event -> {
            appController.addCommand(new AddingNewCommand(new ChangeTaskEstimation(
                    Workspace.getStory(currentItem),
                    taskTable.getSelectionModel().getSelectedItem(),
                    event.getNewValue(), null, new EffortLeft(LocalDateTime.now(), event.getNewValue()), TaskTab.STORY_TASKS),
                    addingNew));
            taskTable.requestFocus();
        });

         // Validation for task estimate TextField
        taskEstimate.setOnKeyTyped(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                String text = taskEstimate.getText();
                Boolean hasDot = text.contains(".");
                if (!"0123456789.".contains(event.getCharacter())) {
                    event.consume();
                } else if (event.getCharacter().equals(".") && hasDot) {
                    event.consume();
                }
            }
        });

        // Adds the new task when enter is hit
        taskEstimate.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if (event.getCode() == ENTER) {
                    addTaskAction();
                    event.consume();
                    taskName.requestFocus();
                }
            }
        });

        FontAwesomeIcon glyph = new FontAwesomeIcon();
        glyph.setIconName("QUESTION_CIRCLE");
        glyph.setSize("1em");
        availableStoriesQuestion.setGraphic(glyph);
    }

    /**
     * Remove a Story dependency from the story
     */
    @FXML
    public void removeDependency(){
        String storyName = dependenciesList.getSelectionModel().getSelectedItem();
        if(storyName != null) {
            String uid = Workspace.getStoryID(storyName);
            if(uid != null) {
                dependentStories.remove(uid);
                availableStories.add(uid);
                updateDependencyTables();
            }
            try {
                appController.addCommand(new AddingNewCommand(new RemoveDependency(Workspace.getStory(currentItem), uid),
                        addingNew));
            }
            catch (NullPointerException e)
            {
                Debug.run(() -> e.printStackTrace());
            }
        }
    }

    /**
     * Adds a story dependency to teh story
     */
    @FXML
    public void addDependency(){
        String storyName = availableStoriesList.getSelectionModel().getSelectedItem();
        if(storyName != null) {
            String uid = Workspace.getStoryID(storyName);
            try {
                if (uid != null) {
                    appController.addCommand(new AddingNewCommand(new AddDependency(Workspace.getStory(currentItem), uid),
                            addingNew));
                    dependentStories.add(uid);
                    availableStories.remove(uid);
                    updateDependencyTables();
                }
            }
            catch (IllegalArgumentException e) {
                if (e.getMessage() == "Cyclic Dependency") {
                    errorMessageLabel.setText("This cannot be added as a dependency because it would cause a cycle");
                }
            }
        }
    }

    /**
     * Called when the done or edit button is pressed
     * This will make the enable or disable the main elements and update side
     * list as appropriate
     */
    @FXML
    public void doneEditAction()
    {
            if (this.isEditing()) {
                Story story = Workspace.getStory(currentItem);
                //Return to sprint if the story is in one
                if (story.hasSprint()) {
                    Sprint sprint = Workspace.getSprint(story.associatedSprints().get(0));
                    try {
                        WindowController.instance.setMainView(ItemType.SPRINT, sprint.getShortName());
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                else {
                    displayView();
                    WindowController.instance.updateGUI();
                    WindowController.instance.updateList(ItemType.STORY);
                    WindowController.sideDisplayListController().setListItem(Workspace.getStoryName(currentItem), ItemType.STORY);
                    clearErrorNotification();
                }
            } else {
                editView();
            }

    }

    /**
     * Called when the Add button is pressed
     * This will create a command for adding an acceptance criteria to the
     * story. The text value of the acceptance criteria is obtained from the
     * value in the text area.
     */
    @FXML
    public void addAC()
    {
        String text = storyACText.getText();
        if(!text.isEmpty() && !text.trim().isEmpty()) {
            storyACText.clear();

            Story currentStory = Workspace.getStory(currentItem);

            if (storyACList.getSelectionModel().getSelectedIndex() >= 0) {
                AcceptanceCriteria ac =
                        currentStory.getAcceptanceCriteria(storyACList.getSelectionModel().getSelectedIndex());
                appController.addCommand(new AddingNewCommand(new ChangeAcceptanceCriteria(currentStory, ac, text),
                        addingNew));
            } else {
                AcceptanceCriteria ac = new AcceptanceCriteria(text);
                appController.addCommand(new AddingNewCommand(new AddAcceptanceCriteria(currentStory, ac), addingNew));
            }
            storyACList.getSelectionModel().clearSelection();
            updateAcceptanceCriteria();
            updateComboBoxes();
        }
    }

    /**
     * Called when the Delete button is pressed
     * This will create a command for deleting an acceptance criteria from the
     * story. The acceptance criteria that gets deleted is the one selected in
     * the list.
     */
    @FXML
    public void deleteAC()
    {
        AcceptanceCriteria selected = storyACList.getSelectionModel().getSelectedItem();
        storyACList.getSelectionModel().clearSelection();
        storyACText.setText("");

        if (selected != null) {
            appController.addCommand(new AddingNewCommand(new RemoveAcceptanceCriteria(
                    Workspace.getStory(currentItem),
                    selected),
                    addingNew
            ));

            updateAcceptanceCriteria();
            updateComboBoxes();
            updateReady();
        }
    }

    /**
     * Called when the Cancel button is pressed
     * Cancels any changes made to the selected acceptance criteria and clears
     * the list selection.
     */
    @FXML
    public void cancelAC()
    {
        storyACList.getSelectionModel().clearSelection();
        storyACText.setText("");
    }

    /**
     * Confirms changes/adds new AC if enter was pressed, otherwise calls the
     * super method for handling key presses.
     * @param keyEvent key that has been pressed
     */
    @FXML
    protected void acTextAreaKeyPress(KeyEvent keyEvent)
    {
        if (keyEvent.getCode() == KeyCode.ENTER) {
            // Call default key handler if alt is pressed
            if (keyEvent.isAltDown()) {
                super.handleTextAreaKeyPress(keyEvent);
            } else {
                addAC(); // Saves changes
                keyEvent.consume();
            }
        } else {
            super.handleTextAreaKeyPress(keyEvent);
        }
    }

    /**
     * Fires cancel event if enter is pressed while cancel button is selected
     * @param keyEvent key that has been pressed
     */
    @FXML
    protected void cancelACKeyPress(KeyEvent keyEvent)
    {
        if (keyEvent.getCode() == ENTER) {
            cancelAC();
            keyEvent.consume();
        }
    }

    /**
     * Fires delete event if enter is pressed while delete button is selected
     * @param keyEvent key that has been pressed
     */
    @FXML
    protected void deleteACKeyPress(KeyEvent keyEvent)
    {
        if (keyEvent.getCode() == ENTER) {
            deleteAC();
            keyEvent.consume();
        }
    }

    /**
     * Fires add event if enter is pressed while add button is selected
     * @param keyEvent key that has been pressed
     */
    @FXML
    protected void addACKeyPress(KeyEvent keyEvent)
    {
        if (keyEvent.getCode() == ENTER) {
            addAC();
            keyEvent.consume();
        }
    }

    /**
     * Adds a task if enter is pressed while entering a task name
     * @param keyEvent key that has been pressed
     */
    @FXML
    protected void taskNameKeyPress(KeyEvent keyEvent)
    {
        if (keyEvent.getCode() == ENTER) {
            addTaskAction();
            keyEvent.consume();
        }
    }

    /**
     * Adds a task if enter is pressed while entering a task description
     * @param keyEvent key that has been pressed
     */
    @FXML
    protected void taskDescriptionKeyPress(KeyEvent keyEvent)
    {
        if (keyEvent.getCode() == ENTER) {
            addTaskAction();
            keyEvent.consume();
            taskName.requestFocus();
        }
    }

    /**
     * Adds a task if enter is pressed while the add task button is selected
     * @param keyEvent key that has been pressed
     */
    @FXML
    protected void addTaskKeyPress(KeyEvent keyEvent)
    {
        if (keyEvent.getCode() == ENTER) {
            addTaskAction();
            keyEvent.consume();
            taskName.requestFocus();
        }
    }

    /**
     * Deletes selected task if enter is pressed while the delete task button
     * is selected
     * @param keyEvent key that has been pressed
     */
    @FXML
    protected void deleteTaskKeyPress(KeyEvent keyEvent)
    {
        if (keyEvent.getCode() == ENTER) {
            deleteTaskAction();
            keyEvent.consume();
        }
    }

    /**
     * Changes the state of the story "Readiness"
     */
    @FXML
    private void readyCheckAction() {
        appController.addCommand(new AddingNewCommand(new ChangeStoryReadiness(Workspace.getStory(currentItem)),
                addingNew));
    }

    /**
     * Updates the acceptance criteria list with items from the Workspace.
     */
    private void updateAcceptanceCriteria()
    {
        acceptanceCriteria = FXCollections.observableArrayList(
                Workspace.getStory(currentItem).getAllAcceptanceCriteria()
        );

        storyACList.setItems(null);
        storyACList.setItems(acceptanceCriteria);
    }

    /**
     * Updates the combo boxes in the story frame
     */
    private void updateComboBoxes() {
        preventCombo = true;
        Story story = Workspace.getStory(currentItem);

        ObservableList<ScaleType> scaleItems =
                FXCollections.observableArrayList(ScaleType.values());
        scaleCombo.setItems(scaleItems);
        scaleCombo.setValue(story.getScaleType());

        ObservableList<String> estimateItems =
                FXCollections.observableArrayList(story.getEstimationScale().getRepresentations());
        estimateCombo.setItems(estimateItems);
        estimateCombo.setValue(story.getEstimateRep());
        if (story.getAllAcceptanceCriteria().isEmpty() || !isEditing()) {
            estimateCombo.setDisable(true);
        }
        else {
            estimateCombo.setDisable(false);
        }
        preventCombo = false;
    }

    /**
     * Updates teh Ready checkbox
     */
    private void updateReady() {
        Story story = Workspace.getStory(currentItem);
        readyCheck.setSelected(story.getReadiness());

        try {
            story.canBeReadied();
            readyCheck.setDisable(!isEditing());
        }
        catch (IllegalArgumentException e) {
            readyCheck.setDisable(true);
        }
    }

    /**
     * CHanges the Story Scale. (FIb, Dog breeds, etc.)
     */
    @FXML
    public void scaleComboAction() {
        if (!preventCombo) {
            try {
                appController.addCommand(new AddingNewCommand(new ChangeStoryScale(
                        Workspace.getStory(currentItem),
                        (ScaleType) scaleCombo.getValue()),
                        addingNew));
                updateComboBoxes();
            } catch (NullPointerException e) {
            }
        }
    }

    /**
     * CHanges the story estimate
     */
    @FXML
    public void estimateComboAction() {
        if (!preventCombo) {
            try {
                appController.addCommand(new AddingNewCommand(new ChangeStoryEstimate(
                        Workspace.getStory(currentItem),
                        estimateCombo.getSelectionModel().getSelectedIndex()),
                        addingNew
                ));
                updateReady();
            } catch (NullPointerException e) {
                Debug.run(() -> e.printStackTrace());
            }
        }
    }

    /**
     * Creates a command for moving an acceptance criteria in the list
     * This method is called when a list item is dragged on top of another list
     * item.
     * @param oldIndex position in the list of the item being dragged
     * @param newIndex position in the list of the item being dragged onto
     */
    public void moveAcceptanceCriteria(int oldIndex, int newIndex)
    {
        appController.addCommand(
                new AddingNewCommand(new MoveAcceptanceCriteria(
                        Workspace.getStory(currentItem),
                        storyACList.getItems().get(oldIndex),
                        newIndex),
                        addingNew
                ));

        updateAcceptanceCriteria();
        storyACList.getSelectionModel().select(newIndex);
    }

    /**
     * populates the map of text fields, keying them so that they may be
     * referred to by the name reported in validation reports.
     */
    protected void populateFieldMap()
    {
        fieldMap.put("shortName", itemShortName);
        fieldMap.put("longName", storyLongName);
        fieldMap.put("description", storyDescription);
        fieldMap.put("creator", storyCreator);
    }

    /**
     * Updates the dependency tables
     */
    public void updateDependencyTables()
    {
        ArrayList<String> available = availableStories;
        available.removeAll(getCyclicStories());
        ObservableList<String> aS = FXCollections.observableArrayList(Workspace.getStoryNames(available));
        aS.sort(String.CASE_INSENSITIVE_ORDER);
        availableStoriesList.setItems(aS);
        ObservableList<String> eS = FXCollections.observableArrayList(Workspace.getStoryNames(dependentStories));
        eS.sort(String.CASE_INSENSITIVE_ORDER);
        dependenciesList.setItems(eS);

        dependenciesList.getSelectionModel().clearSelection();
        availableStoriesList.getSelectionModel().clearSelection();
    }

    /**
     * Add Task button action
     * Adds the new task to the task table
     */
    @FXML
    protected void addTaskAction()
    {
        if (taskName.getText().isEmpty()){
            errorMessageLabel.setText("A task must have a name");
        }
        else {
            errorMessageLabel.setText("");
            Task newTask;
            if(taskEstimate.getText().equals(".") || taskEstimate.getText().isEmpty()){
                newTask = new Task(taskName.getText(), taskDescription.getText(), 0.0);
            } else{
                newTask = new Task(taskName.getText(), taskDescription.getText(), Double.parseDouble(taskEstimate.getText()));
            }
            appController.addCommand(new AddingNewCommand(new AddTask(Workspace.getStory(currentItem),newTask),addingNew));
            populateTaskTable();
            taskName.clear();
            taskDescription.clear();
            taskEstimate.clear();
        }
    }

    /**
     * Delete Task button action
     * Deletes the selected task in the task table
     */
    @FXML
    protected void deleteTaskAction()
    {
        ObservableList<Task> tasks = taskTable.getSelectionModel().getSelectedItems();
        for(Task task : tasks){
            appController.addCommand(new AddingNewCommand(new DeleteTask(Workspace.getStory(currentItem),task), addingNew));
        }
        populateTaskTable();
    }

    /**
     * populates the task table from the set of tasks associated with the current selected story item.
     */
    private void populateTaskTable()
    {
        List<Task> tasks = Workspace.getStory(currentItem).getAllTasks();
        if(tasks != null) {
            taskTable.setItems(null);
            taskTable.layout();
            taskTable.setItems(FXCollections.observableList(tasks));
        }
    }

    /**
     * Give more generic methods access to the item type
     * @return The STORY item type
     */
    protected ItemType getItemType() {
        return ItemType.STORY;
    }


    /**
     * Resets the cellFactory of the table columns cell (back to a regular uneditable cell)
     */
    private void disallowTableRowEditing(){
        taskTableShortNameColumn.setCellFactory(column -> {
            return new TableCell<Task, String>() {
                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    if (item == null || empty) {
                        setText(null);
                    } else {
                        setText(item.toString());
                    }
                }
            };
        });

        taskTableDescriptionColumn.setCellFactory(column -> {
            return new TableCell<Task, String>() {
                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    if (item == null || empty) {
                        setText(null);
                    } else {
                        setText(item.toString());
                    }
                }
            };
        });
}


    /**
     * Makes elements editable
     */
    protected void editView()
    {
        super.editView();

        itemShortName.getStyleClass().remove("display-view");
        storyLongName.getStyleClass().remove("display-view");
        storyDescription.getStyleClass().remove("display-view");

        itemShortName.getStyleClass().add("edit-view");
        storyLongName.getStyleClass().add("edit-view");
        storyDescription.getStyleClass().add("edit-view");
        storyDescription.getStyleClass().add("text-area-display-view");

        itemShortName.setDisable(false);
        storyLongName.setDisable(false);
        storyDescription.setDisable(false);

        // Enable Scale ComboBox
        scaleCombo.getStyleClass().remove("main-uneditable-combobox");
        scaleCombo.setDisable(Workspace.getStory(currentItem).isInBacklog());
        scaleLabel.setTooltip(new Tooltip("Scale cannot be changed while Story is in a Backlog"));

        // Enable Estimate ComboBox
        estimateCombo.getStyleClass().remove("main-uneditable-combobox");
        estimateCombo.setDisable(false);
        estimateLabel.setTooltip(new Tooltip("Story cannot be estimated without Acceptance Criteria"));

        updateComboBoxes();

        // Show controls for ACs
        storyACText.setVisible(true);
        storyACAdd.setVisible(true);
        deleteACButton.setVisible(true);
        storyACList.setDisable(false);
        deleteACButton.setDisable(true);
        cancelACButton.setDisable(true);
        cancelACButton.setVisible(true);
/*
        ((AnchorPane)storyACText.getParent()).setMaxHeight(1000);
        ((AnchorPane)storyACText.getParent()).setPrefHeight(60);
*/
        ACControls.setVisible(true);
        ACControls.setMinHeight(Region.USE_COMPUTED_SIZE);
        ACControls.setMaxHeight(Region.USE_COMPUTED_SIZE);
        ACControls.setPrefHeight(Region.USE_COMPUTED_SIZE);

        // Show asterisks
        snAsterisk.setVisible(true);

        updateReady();
        readyLabel.setTooltip(new Tooltip("Story cannot be readied unless it is estimated and in a Backlog"));

        addDependencyButton.setVisible(true);
        removeDependencyButton.setVisible(true);
        availableStoriesList.setVisible(true);
        availableStoriesLabel.setVisible(true);
        availableStoriesQuestion.setVisible(true);

        dependenciesList.getStyleClass().remove("display-view-listview");
        dependenciesList.getStyleClass().add("edit-view-listview");
        dependenciesList.getStyleClass().remove("display-view");
        dependenciesList.getStyleClass().add("edit-view");

        // add this here instead of at init time since we need to make the cell
        // editable in edit mode and wrappable in view mode
        taskTableDescriptionColumn.setCellFactory(TextFieldTableCell.<Task>forTableColumn());
        taskTableShortNameColumn.setCellFactory(TextFieldTableCell.<Task>forTableColumn());
        taskTableEstimateColumn.setCellFactory(TextFieldTableCell.<Task, Double>forTableColumn(new CustomDoubleConverter(taskTable)));

        addTaskButton.setVisible(true);
        deleteTaskButton.setVisible(true);
        addTaskLabel.setVisible(true);
        taskName.setVisible(true);
        taskDescription.setVisible(true);
        taskEstimate.setVisible(true);


        taskTable.setEditable(true);
        storyVbox.setPrefHeight(900);
        taskTable.getStyleClass().remove("table-display");

        edit_doneButton.setStyle(null);
        edit_doneButton.setOnAction(action);
    }

    /**
     * Makes elements uneditable
     */
    protected void displayView()
    {
        super.displayView();




        disallowTableRowEditing();
        itemShortName.getStyleClass().remove("edit-view");
        storyLongName.getStyleClass().remove("edit-view");
        storyDescription.getStyleClass().remove("edit-view");

        itemShortName.getStyleClass().add("display-view");
        storyLongName.getStyleClass().add("display-view");
        storyDescription.getStyleClass().add("display-view");
        storyDescription.getStyleClass().add("text-area-display-view");

        itemShortName.setDisable(true);
        storyLongName.setDisable(true);
        storyDescription.setDisable(true);

        storyCreator.setStyle("-fx-opacity: 1");
        scaleLabel.setStyle("-fx-opacity: 1");
        estimateLabel.setStyle("-fx-opacity: 1");
        readyLabel.setStyle("-fx-opacity: 1");

        // Disable Scale ComboBox
        scaleCombo.getStyleClass().add("main-uneditable-combobox");
        scaleCombo.setDisable(true);
        scaleCombo.setStyle("-fx-opacity: 1");
        scaleLabel.setTooltip(null);

        // Disable Estimate ComboBox
        estimateCombo.getStyleClass().add("main-uneditable-combobox");
        estimateCombo.setDisable(true);
        estimateCombo.setStyle("-fx-opacity: 1");
        estimateLabel.setTooltip(null);

        // Hide controls for ACs
        storyACText.setVisible(false);
        storyACAdd.setVisible(false);
        deleteACButton.setVisible(false);
        storyACList.setDisable(true);
        cancelACButton.setVisible(false);
/*
        ((AnchorPane)storyACText.getParent()).setMaxHeight(0);
        ((AnchorPane)storyACText.getParent()).setMinHeight(0);
        ((AnchorPane)storyACText.getParent()).setPrefHeight(0);
*/
        ACControls.setVisible(false);
        ACControls.setMaxHeight(0);
        ACControls.setMinHeight(0);
        ACControls.setPrefHeight(0);
        // Hide asterisks
        snAsterisk.setVisible(false);

        readyCheck.setDisable(true);
        readyLabel.setTooltip(null);

        addDependencyButton.setVisible(false);
        removeDependencyButton.setVisible(false);
        availableStoriesList.setVisible(false);
        availableStoriesLabel.setVisible(false);
        availableStoriesQuestion.setVisible(false);

        dependenciesList.getStyleClass().remove("edit-view-listview");
        dependenciesList.getStyleClass().add("display-view-listview");
        dependenciesList.getStyleClass().remove("edit-view");
        dependenciesList.getStyleClass().add("display-view");

        addTaskButton.setVisible(false);
        deleteTaskButton.setVisible(false);
        addTaskLabel.setVisible(false);
        taskName.setVisible(false);
        taskDescription.setVisible(false);
        taskEstimate.setVisible(false);

        populateTaskTable();
        taskTable.setEditable(false);
        taskTable.getStyleClass().add("table-display");

        storyVbox.setPrefHeight(800);

        //Prevent story from being edited if it is in a sprint



    }

    /**
     * adjusts the width of the task table description column when the window to take up allowed space and word wrap correctly.
     */
    void taskDescriptionViewMode()
    {
        if(!this.isEditing()) {
            // add this here instead of at init time since we need to make the cell
            // editable in edit mode and wrappable in view mode
            taskTableDescriptionColumn.setCellFactory(new Callback<TableColumn<Task, String>, TableCell<Task, String>>() {
                @Override
                public TableCell<Task, String> call(TableColumn<Task, String> param) {
                    final TableCell<Task, String> cell = new TableCell<Task, String>() {
                        private Text text;

                        @Override
                        public void updateItem(String item, boolean empty) {
                            super.updateItem(item, empty);
                            if (!isEmpty()) {
                                text = new Text(item.toString());
                                setGraphic(text);
                            }
                        }
                    };
                    return cell;
                }
            });
        }
    }

    /**
     * Get a list of all available stories which would cause a cyclic dependency
     * @return List of cyclic story IDs
     */
    private ArrayList<String> getCyclicStories() {
        Story story = Workspace.getStory(currentItem);

        ArrayList<String> cycleStories = new ArrayList<>();

        for (String storyid : availableStories) {
            if (story.checkCycle(storyid)) {
                cycleStories.add(storyid);
            }
        }

        return cycleStories;
    }

    /**
     * Sets the focus on the Control with the given fxID
     * @param commandMessage CommandMessage to use
     */
    public void setFocus(CommandMessage commandMessage)
    {
        String fxID = commandMessage.getItemProperty();
        String tab = commandMessage.getTab();

        if ("AC".equals(tab)) {
            storyTabPane.getSelectionModel().select(acTab);
        }
        else if ("Dependencies".equals(tab)) {
            storyTabPane.getSelectionModel().select(dependenciesTab);
        }
        else if ("Tasks".equals(tab)) {
            storyTabPane.getSelectionModel().select(tasksTab);
        }
        else {
            storyTabPane.getSelectionModel().select(propertiesTab);
        }

        editView();
        switch(fxID) {
            case("itemShortName"):
                itemShortName.requestFocus();
                break;
            case("storyLongName"):
                storyLongName.requestFocus();
                break;
            case("storyDescription"):
                storyDescription.requestFocus();
                break;
            case("storyCreator"):
                storyCreator.requestFocus();
                break;
            case("storyACList"):
                storyACList.requestFocus();
                break;
            case("storyACText"):
                storyACText.requestFocus();
                break;
            case("scaleCombo"):
                scaleCombo.requestFocus();
                break;
            case("estimateCombo"):
                estimateCombo.requestFocus();
                break;
            case("readyCheck"):
                readyCheck.requestFocus();
                break;
            case("availableStoriesList"):
                availableStoriesList.requestFocus();
                break;
        }
    }

    /**
     * Sets the fields when displaying a story
     * @param uid is the unique id of the item to set as current (the one selected in sidebar list for example).
     */
    public void setSelected(String uid)
    {
        super.setSelected(uid);
        addUndoableTextInput(storyLongName, CommandType.STORY_LONG_NAME);
        addUndoableTextInput(storyDescription, CommandType.STORY_DESCRIPTION);
        Story story = Workspace.getStory(uid);
        // Set text-field values
        itemShortName.setText(story.getShortName());
        storyLongName.setText(story.getLongName());
        storyDescription.setText(story.getDescription());
        storyCreator.setText(story.getCreatorName());

        dependentStories = new ArrayList<>();
        for(String storyid : story.getDependencies()){
            availableStories.remove(storyid); // Remove story's dependencies from the available list
            dependentStories.add(storyid);
        }
        availableStories.remove(currentItem);

        updateDependencyTables();
        updateAcceptanceCriteria();
        updateComboBoxes();

        populateTaskTable();

        updateReady();

        action = edit_doneButton.getOnAction();

        if (story.inSprint) {

            //edit_doneButton.setDisable(true);
            // Hack for disabling edit button - disabling it was requesting the focus and requesting

            edit_doneButton.setStyle("-fx-opacity: 0; -fx-faint-focus-color: transparent;\n" +
                    "    -fx-focus-color: -fx-text-box-border");
            edit_doneButton.setOnAction(event -> event.consume());

            displayView();
            errorMessageLabel.setText("This story is in a sprint. It can't be modified from outside the sprint.");
        }
    }
}
