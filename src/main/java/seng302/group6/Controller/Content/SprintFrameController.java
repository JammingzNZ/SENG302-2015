package seng302.group6.Controller.Content;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Callback;
import org.controlsfx.control.action.Action;
import org.controlsfx.dialog.*;
import seng302.group6.Controller.AppController;
import seng302.group6.Controller.Window.WindowController;
import seng302.group6.CustomDoubleConverter;
import seng302.group6.Debug;
import seng302.group6.Model.Command.AddingNewCommand;
import seng302.group6.Model.Command.CommandMessage;
import seng302.group6.Model.Command.Sprint.*;
import seng302.group6.Model.Command.Story.Tasks.*;
import seng302.group6.Model.Command.Story.Tasks.TaskTab;
import seng302.group6.Model.ItemClasses.*;
import seng302.group6.Model.ItemType;
import seng302.group6.Model.Status.StatusType;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

/**
 * Controller for the Sprint frame
 * Created by dan on 27/07/15.
 */
public class SprintFrameController extends ContentController {

    @FXML TextField sprintShortName;
    @FXML Label snAsterisk;

    @FXML TextField sprintFullName;
    @FXML TextArea sprintDescription;

    @FXML TableView<Story> sprintStories;
    @FXML TableColumn<Story, String> storyNameColumn;

    @FXML VBox storyControls;
    @FXML ComboBox<String> storyCombo;
    @FXML Button addStoryButton;
    @FXML Button deleteStoryButton;
    @FXML Button editStoryButton;

    @FXML Label storyQuestion;
    @FXML Label addStoryLabel;

    @FXML Tab propertiesTab;
    @FXML private Tab storiesTab;
    @FXML TabPane sprintTabPane;

    @FXML Button lockButton;
    @FXML Tooltip lockButtonToolTip;

    @FXML Label projectLabel;
    @FXML Label releaseLabel;
    @FXML Label startDateLabel;
    @FXML Label endDateLabel;
    @FXML Label teamLabel;
    @FXML Label backlogLabel;

    private SprintFrameController thisController = this;

    // Used to get controller for a Node
    private HashMap<TitledPane, FXMLLoader> loaders = new HashMap<>();

    // Scrum Board

    @FXML Tab scrumBoardTab;
    @FXML Accordion scrumBoardStories;

    // All Tasks Tab
    @FXML Tab allTasksTab;

    @FXML Label tableTitle;
    @FXML RadioButton unassignedTasksRadio;
    @FXML RadioButton allTasksRadio;

    @FXML TableView<Task> allTasksTable;
    @FXML TableColumn<Task, String> taskTableStoryColumn;
    @FXML TableColumn<Task, String> taskTableShortNameColumn;
    @FXML TableColumn<Task, Double> taskTableEstimateColumn;
    @FXML TableColumn<Task, Double> taskTableEffortColumn;
    @FXML TableColumn<Task, StatusType> taskTableStatusColumn;

    ///////// Burn down ////////
    @FXML Tab burndownTab;
    @FXML LineChart burndownChart;

    private ObservableList<Story> stories = FXCollections.observableArrayList();

    boolean preventCombo = false;
    
    /**
     * Intializes the Sprint Frame controller
     */
    public void initialize() {
        storyNameColumn.setCellValueFactory(new PropertyValueFactory<>("shortName"));

        // ===========

        // All Tasks Table

        // ==========

        TableColumn<Task, Task> peopleCol = new TableColumn<>("People");
        peopleCol.setMinWidth(100);
        peopleCol.setPrefWidth(100);
        peopleCol.setMaxWidth(100);
        peopleCol.setCellValueFactory(features -> new ReadOnlyObjectWrapper<>(features.getValue()));

        peopleCol.setCellFactory(new Callback<TableColumn<Task, Task>, TableCell<Task, Task>>() {
            @Override
            public TableCell<Task, Task> call(TableColumn<Task, Task> btnCol) {
                return new TableCell<Task, Task>() {
                    final Label label = new Label();

                    @Override
                    public void updateItem(final Task task, boolean empty) {
                        super.updateItem(task, empty);
                        if (task != null) {

                            setOnMouseClicked(event -> {

                                Debug.println(task.getShortName());

                                try {
                                    FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("assignPeople.fxml"));

                                    Stage assignPeople = new Stage();
                                    assignPeople.setTitle("Assign or unassign people");
                                    assignPeople.setScene(new Scene(loader.load()));

                                    assignPeople.setResizable(false);
                                    assignPeople.initModality(Modality.APPLICATION_MODAL);

                                    AssignPeopleController apc = loader.<AssignPeopleController>getController();
                                    apc.setSprintParent(thisController);
                                    apc.setTaskTableIndex(getIndex());
                                    apc.setTask(allTasksTable.getSelectionModel().getTableView().getItems().get(getIndex()));
                                    apc.setSprint(Workspace.getSprint(currentItem));
                                    apc.setStory(allTasksTable.getSelectionModel().getTableView().getItems().get(getIndex()).getStory());
                                    apc.update();

                                    assignPeople.show();

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                            });

                            ArrayList<String> people = Workspace.getPeopleNames(task.getPeople());
                            String peopleString = "";

                            for (String person : people) {
                                peopleString += person + ", ";
                            }

                            if (peopleString.length() > 0) {
                                peopleString = peopleString.substring(0, peopleString.length() - 2);
                            }

                            label.setText(peopleString);
                            label.setStyle("-fx-font-weight: normal; ");

                            setGraphic(label);
                        } else {
                            setGraphic(null);
                            setOnMouseClicked(null);
                        }
                    }
                };
            }
        });

        allTasksTable.getColumns().add(peopleCol);

        TableColumn<Task, Task> logCol = new TableColumn<>("Log");
        logCol.setMinWidth(50);
        logCol.setPrefWidth(50);
        logCol.setMaxWidth(50);
        logCol.setCellValueFactory(features -> new ReadOnlyObjectWrapper<>(features.getValue()));

        logCol.setCellFactory(new Callback<TableColumn<Task, Task>, TableCell<Task, Task>>() {
            @Override
            public TableCell<Task, Task> call(TableColumn<Task, Task> btnCol) {
                return new TableCell<Task, Task>() {
                    final Button button = new Button();
                    private boolean popOverShown = false;

                    @Override
                    public void updateItem(final Task Task, boolean empty) {
                        super.updateItem(Task, empty);
                        if (Task != null) {
                            FontAwesomeIcon glyph = new FontAwesomeIcon();
                            glyph.setIconName("PENCIL");
                            glyph.setSize("1em");
                            button.setGraphic(glyph);

                            button.setPadding(new Insets(2, 5, 2, 5));
                            setPadding(new Insets(6, 0, 0, 14));

                            button.setOnAction(event -> {

                                try {
                                    FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("logEffort.fxml"));

                                    Stage effortLogging = new Stage();
                                    effortLogging.setTitle("Effort Logging");
                                    effortLogging.setScene(new Scene(loader.load(), 690, 528));

                                    effortLogging.setResizable(false);
                                    effortLogging.initModality(Modality.APPLICATION_MODAL);

                                    EffortLoggingFrameController elfc = loader.<EffortLoggingFrameController>getController();
                                    elfc.setTask(allTasksTable.getSelectionModel().getTableView().getItems().get(getIndex()));
                                    elfc.setStory(allTasksTable.getSelectionModel().getTableView().getItems().get(getIndex()).getStory());
                                    elfc.setParentSprintFC(thisController);
                                    elfc.setSprint(Workspace.getSprint(currentItem));

                                    effortLogging.show();

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                            });
                            setGraphic(button);
                        } else {
                            setGraphic(null);
                        }
                    }
                };
            }
        });

        allTasksTable.getColumns().add(logCol);

        taskTableStoryColumn.setCellValueFactory(new PropertyValueFactory<Task, String>("story"));

        taskTableShortNameColumn.setCellValueFactory(new PropertyValueFactory<>("shortName"));

        taskTableEstimateColumn.setCellValueFactory(new PropertyValueFactory<>("effortLeft"));
        taskTableEstimateColumn.setCellFactory(TextFieldTableCell.<Task, Double>forTableColumn(new CustomDoubleConverter(allTasksTable)));

        taskTableEffortColumn.setCellValueFactory(new PropertyValueFactory<>("effort"));

        taskTableStatusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
        taskTableStatusColumn.setCellFactory(ComboBoxTableCell.forTableColumn(FXCollections.observableArrayList(StatusType.values())));

        taskTableEstimateColumn.setOnEditCommit(event -> {
            Task task = allTasksTable.getSelectionModel().getSelectedItem();
            AppController.instance().addCommand(new ChangeTaskEffortLeft(allTasksTable.getSelectionModel().getSelectedItem().getStory(), task, event.getNewValue(),
                    Workspace.getSprint(currentItem), new EffortLeft(LocalDateTime.now(), event.getNewValue()), TaskTab.ALL_TASKS));
        });

        taskTableStatusColumn.setOnEditCommit(event -> {
                    AppController.instance().addCommand(new ChangeTaskStatus(
                            allTasksTable.getSelectionModel().getSelectedItem().getStory(),
                            allTasksTable.getSelectionModel().getSelectedItem(),
                            event.getNewValue(),
                            Workspace.getSprint(currentItem),
                            TaskTab.ALL_TASKS
                    ));
                    highlight();
                }
        );

        sprintStories.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> {
                    editStoryButton.setDisable(newValue == null);
                    deleteStoryButton.setDisable(newValue == null);
                }
        );

        addGlyph(storyQuestion);

        //burndown
        initBurndownChart();
        burndownTab.setOnSelectionChanged(event -> initBurndownChart());

        ToggleGroup allTasksGroup = new ToggleGroup();

        allTasksRadio.setToggleGroup(allTasksGroup);
        unassignedTasksRadio.setToggleGroup(allTasksGroup);

        allTasksRadio.setSelected(true);

        // When the story selection changes in the backlogStories table whilst in editing mode and with
        // highlighting enabled, highlightToggled() is called to change the background colours appropriately.
        allTasksTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            highlight();
        });

        sprintTabPane.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Tab>() {
            @Override
            public void changed(ObservableValue<? extends Tab> observable, Tab oldValue, Tab newValue) {
                populateAllTasksTable();
                updateSBStories();
            }
        });

        highlight();
    }

    public void setSelectedTask(int index){
        allTasksTable.getSelectionModel().select(index);
    }

    public void highlight(){
        allTasksTable.setRowFactory(new Callback<TableView<Task>, TableRow<Task>>()
        {
            @Override
            public TableRow<Task> call(TableView<Task> param)
            {
                final TableRow<Task> row = new TableRow<Task>()
                {
                    @Override
                    protected void updateItem(Task task, boolean empty)
                    {
                        String colour = "white";
                        super.updateItem(task, empty);
                        if (!empty) {
                            if(task.getPeople().size() > 0){
                                colour = getStatusColour(task.getStatus());
                            }
                            setStyle("-fx-background-color: " + colour);
                            if(this.getIndex() == allTasksTable.getSelectionModel().getSelectedIndex()){
                                setStyle("-fx-background-color: linear-gradient(to bottom, dodgerblue, " + colour +")");
                            }
                        } else {
                            setStyle("-fx-background-color: " + colour);
                        }
                    }
                };
                return row;
            }
        });
    }

    public String getStatusColour(StatusType statusType){
        String colour = null; // Unassigned
        switch (statusType){
            case DONE:
                colour = "limegreen";
                break;
            case READY:
                colour = "lightgreen";
                break;
            case DEFERRED:
                colour = "plum";
                break;
            case BLOCKED:
                colour = "indianred";
                break;
            case PENDING:
                colour = "lightblue";
                break;
            case IN_PROGRESS:
                colour = "gold";
                break;
            case NOT_STARTED:
                colour = "lightgray";
                break;
        }
        return colour;
    }

    /**
     * Show only unassigned tasks in the table
     */
    @FXML
    public void unassignedTasksAction()
    {
        tableTitle.setText("Unassigned Tasks");

        ArrayList<Task> allTasks = new ArrayList<>();

        ArrayList<String> storyUids = Workspace.getSprint(currentItem).getStories();

        // Get all of the tasks
        for (String uid: storyUids) {
            Story story = Workspace.getStory(uid);

            for (Task task: story.getAllTasks()) {
                if (task.getPeople().isEmpty()) {
                    allTasks.add(task);
                } 
            }
        }

        if(allTasks != null) {
            allTasksTable.setItems(null);
            allTasksTable.layout();
            allTasksTable.setItems(FXCollections.observableList(allTasks));
        }

        allTasksTable.setFixedCellSize(35);
        if (allTasksTable.getItems().size() > 0) {
            allTasksTable.prefHeightProperty().bind(Bindings.size(allTasksTable.getItems()).multiply(allTasksTable.getFixedCellSize()).add(25));
        }
        else {
            allTasksTable.prefHeightProperty().bind(Bindings.size(allTasksTable.getItems()).multiply(allTasksTable.getFixedCellSize()).add(60));
        }
    }

    /**
     * Show all of the sprint's tasks in the table
     */
    @FXML
    public void allTasksAction()
    {
        tableTitle.setText("All Tasks");

        ArrayList<Task> allTasks = new ArrayList<>();

        ArrayList<String> storyUids = Workspace.getSprint(currentItem).getStories();

        // Get all of the tasks
        for (String uid: storyUids) {
            Story story = Workspace.getStory(uid);
            allTasks.addAll(story.getAllTasks());
        }

        if(allTasks != null) {
            allTasksTable.setItems(null);
            allTasksTable.layout();
            allTasksTable.setItems(FXCollections.observableList(allTasks));
        }

        allTasksTable.setFixedCellSize(35);
        if (allTasksTable.getItems().size() > 0) {
            allTasksTable.prefHeightProperty().bind(Bindings.size(allTasksTable.getItems()).multiply(allTasksTable.getFixedCellSize()).add(25));
        }
        else {
            allTasksTable.prefHeightProperty().bind(Bindings.size(allTasksTable.getItems()).multiply(allTasksTable.getFixedCellSize()).add(60));
        }
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

    /**
     * Gets all the stories from the selected projects backlog
     */
    public void updateStoryCombo(){
        ArrayList<String> stories = new ArrayList<>();
        String project = Workspace.getSprint(currentItem).getAssociatedProject();
        if(project != null) {
            String backlog = Workspace.getProject(project).getBacklog();
            for (String uid : Workspace.getBacklog(backlog).getStories()) {
                // If the current Sprint does not already contain this story...
                if(!Workspace.getSprint(currentItem).getStories().contains(uid)) {
                    Story story = Workspace.getStory(uid);
                    if (!story.inSprint) {
                        stories.add(story.getShortName());
                    }
                }
            }
            ObservableList<String> s = FXCollections.observableArrayList(stories);
            storyCombo.setItems(s);
        }
    }

    /**
     * Adds the story in the story ComboBox to the Sprint story table
     */
    @FXML
    public void addStoryAction(){
        if(storyCombo.getSelectionModel().getSelectedItem() != null) {

            appController.addCommand(new AddingNewCommand(new AddStoryToSprint(Workspace.getSprint(currentItem),
                    Workspace.getStoryID(storyCombo.getSelectionModel().getSelectedItem())), addingNew));

            stories.add(Workspace.getStory(Workspace.getStoryID(storyCombo.getSelectionModel().getSelectedItem())));
            updateStoryCombo();
            updateStoryTable();
            updateSBStories();
            populateAllTasksTable();
        }
    }

    /**
     * Deletes the selected story in the Sprint story table
     */
    @FXML
    public void deleteStoryAction(){
        Story selectedStory = sprintStories.getSelectionModel().getSelectedItem();

        if(selectedStory != null){

            appController.addCommand(new AddingNewCommand(new RemoveStoryFromSprint(Workspace.getSprint(currentItem),
                    selectedStory.uid()),
                    addingNew
            ));

            stories.remove(selectedStory);

            updateStoryTable();
            updateStoryCombo();
            updateSBStories();
            populateAllTasksTable();
            //clearTaskTable();
        }
    }

    /**
     * Change the frame to the edit view in the selected story
     */
    @FXML
    private void editStoryButtonAction() {
        Story story = sprintStories.getSelectionModel().getSelectedItem();
        if (story != null) {
            try {
                WindowController.instance.setMainView(ItemType.STORY, story.getShortName());
                WindowController.instance.selected_mpc.editView();
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void updateStoryTable()
    {
        sprintStories.getSelectionModel().clearSelection();
        sprintStories.setItems(stories);
        sprintStories.sort();
        updateSBStories();
    }


    /**
     * This method is unused and does nothing.
     * Only here because it has to be.
     */
    protected void populateFieldMap()
    {
        //unused now
    }

    /**
     * Give more generic methods access to the item type
     * @return The SPRINT item type
     */
    protected ItemType getItemType() {
        return ItemType.SPRINT;
    }

    /**
     * Makes elements uneditable and changes button to say 'edit'
     */
    protected void editView() {
        super.editView();

        if (sprintTabPane.getSelectionModel().getSelectedItem() != propertiesTab &&
                sprintTabPane.getSelectionModel().getSelectedItem() != storiesTab) {
            sprintTabPane.getSelectionModel().select(propertiesTab);
        }

        itemShortName.getStyleClass().remove("display-view");
        sprintFullName.getStyleClass().remove("display-view");
        sprintDescription.getStyleClass().remove("display-view");

        itemShortName.getStyleClass().add("edit-view");
        sprintFullName.getStyleClass().add("edit-view");
        sprintDescription.getStyleClass().add("edit-view");

        itemShortName.setDisable(false);
        sprintFullName.setDisable(false);
        sprintFullName.setDisable(false);
        sprintDescription.setDisable(false);

        storyCombo.setVisible(true);
        addStoryButton.setVisible(true);
        deleteStoryButton.setVisible(true);

        addStoryLabel.setVisible(true);

        snAsterisk.setVisible(true);
    }

    /**
     * Makes elements editable and changes button to say 'done'
     */
    protected void displayView(){
        super.displayView();

        itemShortName.getStyleClass().remove("edit-view");
        sprintFullName.getStyleClass().remove("edit-view");
        sprintDescription.getStyleClass().remove("edit-view");

        itemShortName.getStyleClass().add("display-view");
        sprintFullName.getStyleClass().add("display-view");
        sprintDescription.getStyleClass().add("display-view");
        sprintDescription.getStyleClass().add("text-area-display-view");

        itemShortName.setDisable(true);
        sprintFullName.setDisable(true);
        sprintDescription.setDisable(true);

        storyCombo.setVisible(false);
        addStoryButton.setVisible(false);
        deleteStoryButton.setVisible(false);

        addStoryLabel.setVisible(false);

        // Hide asterisks
        snAsterisk.setVisible(false);
    }

    /**
     * Swaps between the Done edit modes
     */
    @FXML
    public void doneEditAction()
    {
        if (this.isEditing()) {
            displayView();
            WindowController.instance.updateGUI();
            WindowController.instance.updateList(ItemType.SPRINT);
            WindowController.sideDisplayListController().setListItem(Workspace.getSprintName(currentItem), ItemType.SPRINT);
            clearErrorNotification();
        } else {
            editView();
        }
    }


    /**
     * Changes the focus onto something in the Sprint frame
     * @param commandMessage Message to use for knowing what to update
     */
    public void setFocus(CommandMessage commandMessage)
    {
        String fxID = commandMessage.getItemProperty();
        String tab = commandMessage.getTab();

        if ("Scrum Board".equals(tab)) {
            sprintTabPane.getSelectionModel().select(scrumBoardTab);
            ArrayList<Object> scl = commandMessage.getSpecialCaseList();
            //sbStoryTable.getSelectionModel().select((Story) scl.get(0));

            if (scl.size() > 1) {
                Task task = (Task) scl.get(1);
                ObservableList<TitledPane> panes = scrumBoardStories.getPanes();

                for (TitledPane pane: panes) {
                    FXMLLoader loader = loaders.get(pane);
                    ScrumboardStoryController ssc = loader.getController();

                    if (ssc.getStory().getAllTasks().contains(task)) {
                        ssc.selectTask(task);
                        scrumBoardStories.setExpandedPane(ssc.storyPane);
                    }
                }
            }
        } else if ("All Tasks".equals(tab)) {
            sprintTabPane.getSelectionModel().select(allTasksTab);
            ArrayList<Object> scl = commandMessage.getSpecialCaseList();

            if (scl.size() > 1) {
                Task task = (Task) scl.get(1);
                allTasksTable.getSelectionModel().select(task);
            }
        } else {

            if ("Stories".equals(tab)) {
                sprintTabPane.getSelectionModel().select(storiesTab);
            }
            else {
                sprintTabPane.getSelectionModel().select(propertiesTab);
            }

            editView();
            switch(fxID) {
                case ("itemShortName"):
                    itemShortName.requestFocus();
                    break;
                case ("sprintFullName"):
                    sprintFullName.requestFocus();
                    break;
                case ("sprintDescription"):
                    sprintDescription.requestFocus();
                    break;
                case ("sprintStories"):
                    sprintStories.requestFocus();
                    break;
                case ("storyCombo"):
                    storyCombo.requestFocus();
                    break;
                case ("sbStoryStatusCombo"):
                    //sbStoryTable.requestFocus();
                    break;
                case ("taskTable"):
                    //taskTable.requestFocus();
                    break;

            }
        }
    }

    /**
     * @param uid is the unique id of the item to set as current (the one selected in sidebar list for example).
     */
    public void setSelected(String uid) {
        super.setSelected(uid);

        addUndoableTextInput(sprintFullName, CommandType.SPRINT_NAME);
        addUndoableTextInput(sprintDescription, CommandType.SPRINT_DESCRIPTION);

        Sprint sprint = Workspace.getSprint(uid);

        itemShortName.setText(sprint.getShortName());
        sprintFullName.setText(sprint.getFullName());
        sprintDescription.setText(sprint.getDescription());

        for(String story : sprint.getStories()){
            stories.add(Workspace.getStory(story));
        }
        updateStoryTable();


        if (sprint.getAssociatedProject() != null) {
            String backlog = Workspace.getBacklog(Workspace.getProject(sprint.getAssociatedProject()).getBacklog()).getShortName();
            if (backlog != null) {
                backlogLabel.setText(backlog);
            }
        }

        projectLabel.setText(Workspace.getProject(sprint.getAssociatedProject()).getShortName());
        releaseLabel.setText(Workspace.getRelease(sprint.getAssociatedRelease()).getShortName());
        startDateLabel.setText(sprint.getStartDate());
        endDateLabel.setText(sprint.getEndDate());
        teamLabel.setText(Workspace.getTeam(sprint.getAssociatedTeam()).getShortName());


        updateSBStories();
        populateAllTasksTable();

        updateStoryCombo();

    }

    // SCRUM BOARD TAB

    /**
     * Updates the SB Stories
     */
    public void updateSBStories()
    {
        if (currentItem != null) {
            scrumBoardStories.getPanes().clear();

            ArrayList<String> storyIds = Workspace.getSprint(currentItem).getStories();
            ArrayList<Story> stories = new ArrayList<>();

            for (String id : storyIds) {
                Story story = Workspace.getStory(id);
                stories.add(story);
            }

            stories.sort(new Comparator<Story>() {
                @Override
                public int compare(Story o1, Story o2) {
                    return o1.compareTo(o2);
                }
            });

            for (Story story : stories) {
                FXMLLoader loader = new FXMLLoader();
                try {
                    TitledPane storyPane = loader.load(
                            getClass().getClassLoader().getResource("scrumboardStory.fxml").openStream()
                    );
                    storyPane.graphicTextGapProperty().bind(storyPane.widthProperty().subtract(300));
                    ScrumboardStoryController controller = loader.getController();
                    controller.setStoryAndSprint(story.uid(), currentItem);

                    scrumBoardStories.getPanes().add(storyPane);
                    loaders.put(storyPane, loader);

                } catch (Exception e) {
                    //Debug.run(() -> e.printStackTrace());
                    e.printStackTrace();
                }
            }
            //sbStoryTable.setItems(FXCollections.observableArrayList(stories));
        }
    }

    // All Tasks tab
    public void populateAllTasksTable()
    {
        if (allTasksRadio.isSelected()) {
            allTasksAction();
        } else {
            unassignedTasksAction();
        }
    }

    // Burn Down

    /**
     * Initializes burndown chart
     */
    public void initBurndownChart()
    {
        burndownChart.setTitle("Burndown Chart: " + Workspace.getSprintName(currentItem));

        ObservableList<XYChart.Series<String, Integer>> effortData = FXCollections.observableArrayList();
        XYChart.Series<String, Integer> effortSeries = new XYChart.Series<>();
        XYChart.Series<String, Integer> projectedSeries = new XYChart.Series<>();
        XYChart.Series<String, Integer> burndownSeries = new XYChart.Series<>();
        effortSeries.setName("Effort Spent");
        projectedSeries.setName("Projected Velocity");
        burndownSeries.setName("Effort Left");

        if (currentItem != null) {
            Sprint sprint = Workspace.getSprint(currentItem);

            LocalDate startDate = LocalDate.parse(sprint.getStartDate());
            LocalDate endDate = LocalDate.parse(sprint.getEndDate());

            Double estimation = sprint.getEstimation();
            double days = (double) ChronoUnit.DAYS.between(startDate, endDate);
            double currentdays = days;

            for (Map.Entry<String, Double> effort : Workspace.getSprint(currentItem).getEffortList().entrySet()) {
                effortSeries.getData().add(new XYChart.Data(effort.getKey().toString(), effort.getValue()));
                //burndownSeries.getData().add(new XYChart.Data(effort.getKey().toString(), estimation - effort.getValue()));
            }

            for (Map.Entry<String, Double> effort : Workspace.getSprint(currentItem).getEffortLeftList().entrySet()) {
                burndownSeries.getData().add(new XYChart.Data(effort.getKey().toString(), effort.getValue()));
            }


            while (startDate.compareTo(endDate) <= 0) {
                if (currentdays > 0) {
                    projectedSeries.getData().add(new XYChart.Data(startDate.toString(), estimation * (currentdays/days)));
                }
                else {
                    projectedSeries.getData().add(new XYChart.Data(startDate.toString(), 0));
                }
                currentdays -= 1;
                startDate = startDate.plusDays(1);
            }

            burndownChart.setCreateSymbols(false);
            effortData.add(projectedSeries);
            effortData.add(burndownSeries);
            effortData.add(effortSeries);
            burndownChart.setData(effortData);
        }
    }

    public void scrumboardTabAction(){
               System.out.println("yiii");
    }

    public void alltaskTabAction(){

    }
}
