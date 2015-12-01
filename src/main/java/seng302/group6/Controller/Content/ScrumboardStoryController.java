package seng302.group6.Controller.Content;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import seng302.group6.Controller.AppController;
import seng302.group6.CustomDoubleConverter;
import seng302.group6.Model.Command.Story.ChangeStoryState;
import seng302.group6.Model.Command.Story.Tasks.ChangeTaskEffortLeft;
import seng302.group6.Model.Command.Story.Tasks.ChangeTaskStatus;
import seng302.group6.Model.Command.Story.Tasks.TaskTab;
import seng302.group6.Model.ItemClasses.EffortLeft;
import seng302.group6.Model.ItemClasses.Story;
import seng302.group6.Model.ItemClasses.Task;
import seng302.group6.Model.ItemClasses.Workspace;
import seng302.group6.Model.Status.StatusType;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Controller for the Scrumboard frame
 * Created by simon on 15/09/15.
 */
public class ScrumboardStoryController
{
    private String storyUid;
    private Story story;
    private String sprintUid;
    public ScrumboardStoryController thisController = this;

    private Boolean preventCombo = false;

    @FXML TitledPane storyPane;
    @FXML Label storyPaneLabel;
    @FXML ProgressBar storyPaneProgress;
    @FXML VBox storyPaneVBox;

    @FXML TableView<Task> taskTable;
    @FXML TableColumn<Task, String> taskTableShortNameColumn;
    @FXML TableColumn<Task, Double> taskTableEstimateColumn;
    @FXML TableColumn<Task, Double> taskTableEffortColumn;
    @FXML TableColumn<Task, StatusType> taskTableStatusColumn;

    @FXML ComboBox<StatusType> stateCombo;

    @FXML Tooltip storyToolTip;

    /**
     * Intializes the Scrumboard Story Frame
     */
    public void initialize()
    {
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

                                try {
                                    FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("assignPeople.fxml"));

                                    Stage assignPeople = new Stage();
                                    assignPeople.setTitle("Assign or unassign people");
                                    assignPeople.setScene(new Scene(loader.load()));

                                    assignPeople.setResizable(false);
                                    assignPeople.initModality(Modality.APPLICATION_MODAL);

                                    AssignPeopleController apc = loader.<AssignPeopleController>getController();
                                    apc.setTask(taskTable.getSelectionModel().getTableView().getItems().get(getIndex()));
                                    apc.setSprint(Workspace.getSprint(sprintUid));
                                    apc.setStory(story);
                                    apc.setParent(thisController);
                                    apc.update();

                                    assignPeople.show();

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                            });

                            ArrayList<String> people = Workspace.getPeopleNames(task.getPeople());
                            String peopleString = "";

                            for (String person: people) {
                                peopleString += person + ", ";
                            }

                            if (peopleString.length() > 0) {
                                peopleString = peopleString.substring(0, peopleString.length() - 2);
                            }

                            label.setText(peopleString);
                            label.setStyle("-fx-font-weight: normal; ");

                            setGraphic(label);
                        }
                    }
                };
            }
        });

        taskTable.getColumns().add(peopleCol);

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
                                        elfc.setTask(taskTable.getSelectionModel().getTableView().getItems().get(getIndex()));
                                        elfc.setStory(story);
                                        elfc.setSprint(Workspace.getSprint(sprintUid));
                                        elfc.setParent(thisController);


                                        effortLogging.show();

                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }

                            });
                            setGraphic(button);
                        }
                    }
                };
            }
        });

        storyPane.widthProperty().addListener(new ChangeListener<Number>() {
            @Override public void changed(ObservableValue<? extends Number> observableValue, Number oldStoryWidth,
                                          Number newStoryWidth) {
                resizeStoryLabels();
            }
        });
        taskTable.getColumns().add(logCol);

    }

    /**
     * Sets the StoryUid and the SprintUid for the scrumboard
     * @param storyUid story uid
     * @param sprintUid sprint uid
     */
    public void setStoryAndSprint(String storyUid, String sprintUid)
    {
        this.storyUid = storyUid;
        this.sprintUid = sprintUid;
        story = Workspace.getStory(this.storyUid);

        storyPaneLabel.setText(story.getShortName());
        updateProgressBars();

        preventCombo = true;
        stateCombo.setItems(FXCollections.observableArrayList(StatusType.values()));
        stateCombo.setValue(story.getState());
        preventCombo = false;

        taskTableShortNameColumn.setCellValueFactory(new PropertyValueFactory<>("shortName"));

        taskTableEstimateColumn.setCellValueFactory(new PropertyValueFactory<>("effortLeft"));
        taskTableEstimateColumn.setCellFactory(TextFieldTableCell.<Task, Double>forTableColumn(new CustomDoubleConverter(taskTable)));

        taskTableEffortColumn.setCellValueFactory(new PropertyValueFactory<>("effort"));

        taskTableStatusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
        taskTableStatusColumn.setCellFactory(ComboBoxTableCell.forTableColumn(FXCollections.observableArrayList(StatusType.values())));

        taskTableEstimateColumn.setOnEditCommit(event -> {
            Task task = taskTable.getSelectionModel().getSelectedItem();
            AppController.instance().addCommand(new ChangeTaskEffortLeft(story, task, event.getNewValue(),
                    Workspace.getSprint(sprintUid), new EffortLeft(LocalDateTime.now(), event.getNewValue()), TaskTab.SCRUM_BOARD));
            updateProgressBars();
        });

        taskTableStatusColumn.setOnEditCommit(event ->
                        AppController.instance().addCommand(new ChangeTaskStatus(
                                story,
                                taskTable.getSelectionModel().getSelectedItem(),
                                event.getNewValue(),
                                Workspace.getSprint(sprintUid),
                                TaskTab.SCRUM_BOARD
                        ))
        );

        populateTaskTable();

        if (story.getState() != null) {
            stateCombo.getSelectionModel().select(story.getState());
        } else {
            stateCombo.getSelectionModel().select(StatusType.NOT_STARTED);
        }

    }

    /**
     * Fills the task table with all the tasks of the story.
     */
    public void populateTaskTable()
    {
        taskTable.getSelectionModel().clearSelection();

        List<Task> tasks = story.getAllTasks();

        if(tasks != null) {
            taskTable.setItems(null);
            taskTable.layout();
            taskTable.setItems(FXCollections.observableList(tasks));
        }

        taskTable.setFixedCellSize(35);
        if (taskTable.getItems().size() > 0) {
            taskTable.minHeightProperty().bind(Bindings.size(taskTable.getItems()).add(1).multiply(taskTable.getFixedCellSize()).add(25));
            taskTable.prefHeightProperty().bind(Bindings.size(taskTable.getItems()).add(1).multiply(taskTable.getFixedCellSize()).add(25));
        }
        else {
            taskTable.minHeightProperty().bind(Bindings.size(taskTable.getItems()).multiply(taskTable.getFixedCellSize()).add(60));
            taskTable.prefHeightProperty().bind(Bindings.size(taskTable.getItems()).multiply(taskTable.getFixedCellSize()).add(60));
        }
    }

    /**
     * Change the stories state when the state combobox is used.
     */
    @FXML
    private void stateComboAction() {
        if (!preventCombo) {
            AppController.instance().addCommand(new ChangeStoryState(
                    story,
                    stateCombo.getValue(),
                    Workspace.getSprint(sprintUid)
            ));
            updateProgressBars();
        }
    }

    /**
     * BUGFIX:
     * Resizes the story labels
     */
    private void resizeStoryLabels()
    {
        storyPaneVBox.setPrefWidth(storyPane.getLayoutBounds().getWidth() - 40);
    }

    /**
     * Sets the progress bar value and color
     */
    public void updateProgressBars()
    {
        storyPaneProgress.setProgress(story.getProgress());

        String colorStyle = "-fx-accent: ";

        switch (story.getState()) {
            case NOT_STARTED:
                colorStyle += "lightgray;";
                break;
            case READY:
                colorStyle += "lightgreen;";
                break;
            case IN_PROGRESS:
                colorStyle += "gold;";
                break;
            case PENDING:
                colorStyle += "lightblue;";
                break;
            case BLOCKED:
                colorStyle += "indianred;";
                break;
            case DONE:
                colorStyle += "limegreen;";
                break;
            case DEFERRED:
                colorStyle += "plum;";
                break;
        }

        storyPaneProgress.setStyle(colorStyle);
        storyToolTip.setText(story.getProgressTooltipString());
    }

    /**
     * Toggles the accordion expanded property when the progress bar is clicked
     */
    @FXML private void progressBarClickAction() {
        storyPane.setExpanded(!storyPane.isExpanded());
    }

    /**
     * Selects a task in the table
     * @param task task to be selected
     */
    public void selectTask(Task task)
    {
        taskTable.getSelectionModel().select(task);
    }

    public Story getStory()
    {
        return story;
    }
}
