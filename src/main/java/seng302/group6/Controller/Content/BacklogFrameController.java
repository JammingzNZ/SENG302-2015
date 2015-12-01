package seng302.group6.Controller.Content;


import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import javafx.util.Callback;
import seng302.group6.Controller.Window.WindowController;
import seng302.group6.CustomStringConverter;
import seng302.group6.Debug;
import seng302.group6.Model.Command.AddingNewCommand;
import seng302.group6.Model.Command.Backlog.AddBacklogStory;
import seng302.group6.Model.Command.Backlog.ChangeBacklogProductOwner;
import seng302.group6.Model.Command.Backlog.ChangeBacklogScale;
import seng302.group6.Model.Command.Backlog.RemoveBacklogStory;
import seng302.group6.Model.Command.CommandMessage;
import seng302.group6.Model.Command.Story.ChangeStoryPriority;
import seng302.group6.Model.EstimationScale.ScaleType;
import seng302.group6.Model.ItemClasses.Backlog;
import seng302.group6.Model.ItemClasses.Person;
import seng302.group6.Model.ItemClasses.Story;
import seng302.group6.Model.ItemClasses.Workspace;
import seng302.group6.Model.ItemType;

import java.util.ArrayList;

import static javafx.scene.input.KeyCode.ENTER;

/**
 * Controller for the backlog frame
 * Created by Michael Wheeler on 2/07/15.
 */
public class BacklogFrameController extends ContentController
{


    @FXML TextField backlogName;
    @FXML TextArea backlogDescription;
    @FXML ComboBox<String> backlogPoCombobox;

    @FXML ComboBox<String> storyCombobox;
    @FXML Button addStoryButton;

    @FXML Button deleteStoryButton;

    @FXML Label storyLabel;
    @FXML Label valueLabel;

    @FXML Label snAsterisk;

    @FXML ToggleButton highlightToggle;
    @FXML VBox key;
    @FXML Label readyKey;
    @FXML Label canEstimateKey;
    @FXML Label priorityConflictKey;
    @FXML TableView<Story> backlogStories;

    @FXML TableColumn<Story, Integer> priorityColumn;
    @FXML TableColumn<Story, String> storyNameColumn;

    @FXML TextField storyPriority;

    @FXML ComboBox scaleCombo;

    @FXML TabPane backlogTabPane;
    @FXML Tab propertiesTab;
    @FXML Tab storiesTab;

    private ObservableList<Story> stories = FXCollections.observableArrayList();
    private boolean preventCombo = false;


    /**
     * Intializes the Backlog Frame
     */
    @FXML
    public void initialize(){

        // Map columns to story properties
        priorityColumn.setCellValueFactory(new PropertyValueFactory<Story, Integer>("currentPriority"));
        storyNameColumn.setCellValueFactory(new PropertyValueFactory<Story, String>("shortName"));

        // On edit of priority column action
        priorityColumn.setOnEditCommit(
                new EventHandler<TableColumn.CellEditEvent<Story, Integer>>() {
                    @Override
                    public void handle(TableColumn.CellEditEvent<Story, Integer> t) {

                            appController.addCommand(new AddingNewCommand(new ChangeStoryPriority(
                                    backlogStories.getSelectionModel().getSelectedItem(),
                                    Workspace.getBacklog(currentItem),
                                    t.getNewValue()),
                                    addingNew));

                        backlogStories.getSortOrder().add(priorityColumn);
                    }
                }
        );

        // On priority value textfield action
        storyPriority.textProperty().addListener(new ChangeListener<String>()
        {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue)
            {
                if (!newValue.matches("\\d*")) {
                    storyPriority.clear();
                    errorMessageLabel.setText("Please enter a positive number for the story priority");
                } else {
                    errorMessageLabel.setText("");
                }
            }
        });

        // When the story selection changes in the backlogStories table whilst in editing mode and with
        // highlighting enabled, highlightToggled() is called to change the background colours appropriately.
        backlogStories.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (highlightToggle.isSelected() && editing) {
                highlightToggled();
            }
        });

        // Populate TableView and ComboBoxes
        populateStoriesTableView();
        populatePoCombobox();
        populateStoryCombobox();

        // Hide the key
        key.setVisible(false);

        FontAwesomeIcon glyph1 = new FontAwesomeIcon();
        glyph1.setIconName("QUESTION_CIRCLE");
        glyph1.setSize("1em");
        readyKey.setGraphic(glyph1);
        readyKey.setTooltip(new Tooltip("These stories have been marked as Ready."));

        FontAwesomeIcon glyph2 = new FontAwesomeIcon();
        glyph2.setIconName("QUESTION_CIRCLE");
        glyph2.setSize("1em");
        canEstimateKey.setGraphic(glyph2);
        canEstimateKey.setTooltip(new Tooltip("These stories have not been estimated yet, but are able to be\ni.e. " +
                "they have acceptance criteria."));

        FontAwesomeIcon glyph3 = new FontAwesomeIcon();
        glyph3.setIconName("QUESTION_CIRCLE");
        glyph3.setSize("1em");
        priorityConflictKey.setGraphic(glyph3);
        priorityConflictKey.setTooltip(new Tooltip("These stories rely on one or more stories which have a lower " +
                "(or un-set) priority.\nHover over a story with a conflict to see what is causing it."));
    }


    /**
     * Populates the backlog stories TableView and orders by the current
     * selected order
     */

    public void populateStoriesTableView(){
        backlogStories.setItems(stories);
        backlogStories.sort();
    }


    /**
     * Gets short names of all product owners in workspace and adds to
     * product owner combo box
     */
    public void populatePoCombobox(){
        ArrayList<String> allProductOwners = new ArrayList<>();
        for(String uid : Workspace.getPeople()){
            Person person = Workspace.getPerson(uid);
            String poUid = Workspace.getSkillID("Product Owner");
            if(person.getSkills().contains(poUid)) {
                allProductOwners.add(person.getShortName());
            }
        }
        ObservableList<String> productOwners = FXCollections.observableArrayList(allProductOwners);
        productOwners.add("None ");
        preventCombo = true;
        backlogPoCombobox.setItems(productOwners);
        preventCombo = false;
    }

    /**
     * Gets all the stories in the workspace that are not already
     * part of a backlog
     */
    public void populateStoryCombobox(){
        ArrayList<String> allStories = new ArrayList<>();
        for(String uid : Workspace.getStories()){
            Story story = Workspace.getStory(uid);
            if(!story.isInBacklog()){
                allStories.add(story.getShortName());
            }
        }
        ObservableList<String> stories = FXCollections.observableArrayList(allStories);
        storyCombobox.setItems(stories);
    }

    /**
     * Add Button Action
     * Adds a story with a priority to the backlog stories table
     */
    public void addStoryAction(){
        if(storyCombobox.getSelectionModel().getSelectedItem() != null &&
                !storyPriority.getText().isEmpty()){

            errorMessageLabel.setText("");

            int storyVal = Integer.parseInt(storyPriority.getText());

            appController.addCommand(new AddingNewCommand(new AddBacklogStory(Workspace.getBacklog(currentItem),
                    Workspace.getStoryID(storyCombobox.getSelectionModel().getSelectedItem()), storyVal), addingNew));

            stories.add(Workspace.getStory(Workspace.getStoryID(storyCombobox.getSelectionModel().getSelectedItem())));
            populateStoriesTableView();

            populateStoryCombobox();
            storyPriority.clear();

        }else{
            errorMessageLabel.setText("Please select a story and enter a priority");
        }
    }

    /**
     * Delete Button Action
     * Deletes the selected story from the backlog stories table
     */
    public void deleteStoryAction(){
        Story selectedStory = backlogStories.getSelectionModel().getSelectedItem();

        if(selectedStory != null){

            appController.addCommand(new AddingNewCommand(new RemoveBacklogStory(Workspace.getBacklog(currentItem),
                    selectedStory.uid(),
                    selectedStory.getCurrentPriority()),
                    addingNew
            ));

            stories.remove(selectedStory);

            populateStoriesTableView();
            populateStoryCombobox();
        }
    }


    /**
     * populates the map of text fields, keying them so that they may be
     * referred to by the name reported in validation reports.
     */
    protected void populateFieldMap()
    {
        fieldMap.put("fullName", backlogName);
        fieldMap.put("shortName", itemShortName);
    }

    /**
     * Turns highlighting in backlog stories table on or off
     */
    @FXML
    private void highlightToggled()
    {
        Debug.println(String.valueOf(highlightToggle.isSelected()));
        if (highlightToggle.isSelected()) {
            backlogStories.setRowFactory(new Callback<TableView<Story>, TableRow<Story>>()
            {
                @Override
                public TableRow<Story> call(TableView<Story> param)
                {
                    final TableRow<Story> row = new TableRow<Story>()
                    {
                        @Override
                        protected void updateItem(Story story, boolean empty)
                        {
                            super.updateItem(story, empty);
                            if (!empty) {
                                String colour = "white";
                                if (!story.dependenciesHaveSuitablePriority()) {
                                    colour = "pink";
                                    setStyle("-fx-background-color: " + colour);
                                    setTooltip(new Tooltip(getPriorityTooltip(story)));
                                }
                                else {
                                    setTooltip(null);
                                    if (!story.getAllAcceptanceCriteria().isEmpty() && story.getEstimate() == null) {
                                        colour = "orange";
                                        setStyle("-fx-background-color: " + colour);
                                    }
                                    else if (story.getReadiness()) {
                                        colour = "palegreen";
                                        setStyle("-fx-background-color: " + colour);
                                    }
                                    else {
                                        setStyle(null);
                                    }
                                }
                                if(editing && this.getIndex() == backlogStories.getSelectionModel().getSelectedIndex()){
                                    setStyle("-fx-background-color: linear-gradient(to bottom, dodgerblue, " + colour +")");
                                }

                            }
                        }

                    };
                    return row;
                }
            });

            // Show the key
            key.setVisible(true);
        } else {
            backlogStories.setRowFactory(null);

            // Hide the key
            key.setVisible(false);
        }
        if(isEditing()) {
            // Allow editing of priority column
            priorityColumn.setCellFactory(TextFieldTableCell.<Story, Integer>forTableColumn(new CustomStringConverter(backlogStories)));
        }else {
            // Don't allow editing of priority column
            disallowPriorityEditing();
        }
    }

    /**
     * Generates a string to use for a tooltip for stories with priority conflicts that lists the conflicting
     * dependencies
     * @param story Story to get conflicting dependencies of
     * @return String for tooltip to use
     */
    private String getPriorityTooltip(Story story) {
        String out = "";
        out += "This story depends on the following stories with a lower (or un-set) priority:";

        for (String storyID : story.priorityConflicts()) {
            Story conflictStory = Workspace.getStory(storyID);
            out += "\n\t" + conflictStory;
            if (conflictStory.getCurrentPriority() == null) {
                out += " (No set priority)";
            }
            else {
                out += " (Priority: " + conflictStory.getCurrentPriority() + ")";
            }
        }

        return out;
    }

    /**
     * Handles key press when priority text field has focus
     * If enter is pressed, it will add the story
     * @param keyEvent key that is pressed
     */
    @FXML
    public void priorityKeyPress(KeyEvent keyEvent)
    {
        if (keyEvent.getCode() == ENTER) {
            addStoryAction();
            keyEvent.consume();
        }
    }

    /**
     * Handles key press when add button for stories has focus
     * If enter is pressed, it will add the story
     * @param keyEvent key that is pressed
     */
    @FXML
    public void addStoryKeyPress(KeyEvent keyEvent)
    {
        if (keyEvent.getCode() == ENTER) {
            addStoryAction();
            keyEvent.consume();
        }
    }

    /**
     * Handles key press when delete button for stories has focus
     * If enter is pressed, it will delete the story
     * @param keyEvent key that is pressed
     */
    @FXML
    public void deleteStoryKeyPress(KeyEvent keyEvent)
    {
        if (keyEvent.getCode() == ENTER) {
            deleteStoryAction();
            keyEvent.consume();
        }
    }

    /**
     * Updates the Combo box in Backlog Frame
     */
    private void updateComboBox() {
        preventCombo = true;
        Backlog backlog = Workspace.getBacklog(currentItem);

        ObservableList<ScaleType> scaleItems =
                FXCollections.observableArrayList(ScaleType.values());
        scaleCombo.setItems(scaleItems);
        scaleCombo.setValue(backlog.getScaleType());
        preventCombo = false;
    }


    @FXML
    public void scaleComboAction() {
        if (!preventCombo) {
            try {
                appController.addCommand(new AddingNewCommand(new ChangeBacklogScale(
                        Workspace.getBacklog(currentItem),
                        (ScaleType) scaleCombo.getValue()),
                        addingNew
                ));
                updateComboBox();
                backlogStories.getColumns().get(0).setVisible(false);
                backlogStories.getColumns().get(0).setVisible(true);
            } catch (NullPointerException e) {
            }
        }
    }

    /**
     * Switches between the done and edit modes.
     */
    @FXML
    public void doneEditAction(){
        // Done button has been clicked
        if (this.isEditing()) {
            displayView();
            WindowController.instance.updateGUI();
            WindowController.instance.updateList(ItemType.BACKLOG);
            WindowController.sideDisplayListController().setListItem(Workspace.getBacklogName(currentItem), ItemType.BACKLOG);
            clearErrorNotification();
        }
        // Edit button has been clicked
        else {
            editView();
        }
    }

    /**
     * Resets the cellFactory back to a regular uneditable cell
     */
    private void disallowPriorityEditing(){
        priorityColumn.setCellFactory(column -> {
            return new TableCell<Story, Integer>() {
                @Override
                protected void updateItem(Integer item, boolean empty) {
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
     * Give more generic methods access to the item type
     * @return The BACKLOG item type
     */
    protected ItemType getItemType() {
        return ItemType.BACKLOG;
    }


    /**
     * Makes elements uneditable and changes button to say 'edit'
     */
    protected void displayView() {
        super.displayView();
        disallowPriorityEditing();

        // TextFields
        backlogName.getStyleClass().remove("edit-view");
        itemShortName.getStyleClass().remove("edit-view");
        backlogDescription.getStyleClass().remove("edit-view");

        backlogName.getStyleClass().add("display-view");
        itemShortName.getStyleClass().add("display-view");
        backlogDescription.getStyleClass().add("display-view");
        backlogDescription.getStyleClass().add("text-area-display-view");

        backlogName.setDisable(true);
        itemShortName.setDisable(true);
        backlogDescription.setDisable(true);

        // Disable Product Owner ComboBox
        backlogPoCombobox.getStyleClass().add("main-uneditable-combobox");
        backlogPoCombobox.setDisable(true);
        backlogPoCombobox.setStyle("-fx-opacity: 1");

        // Disable Scale ComboBox
        scaleCombo.getStyleClass().add("main-uneditable-combobox");
        scaleCombo.setDisable(true);
        scaleCombo.setStyle("-fx-opacity: 1");

        // Hide story adding section
        storyLabel.setVisible(false);
        valueLabel.setVisible(false);
        storyPriority.setVisible(false);
        deleteStoryButton.setVisible(false);
        storyCombobox.setVisible(false);
        addStoryButton.setVisible(false);

        // Make table unselectable
        backlogStories.getStyleClass().add("table-display");

        backlogStories.getSelectionModel().clearSelection();


        // Hide asterisks
        snAsterisk.setVisible(false);
    }



    /**
     * Makes elements editable and changes button to say 'done'
     */
    protected void editView() {
        super.editView();

        // Allows Editing of story priority
        priorityColumn.setCellFactory(TextFieldTableCell.<Story, Integer>forTableColumn(new CustomStringConverter(backlogStories)));

        // TextFields
        backlogName.getStyleClass().remove("display-view");
        itemShortName.getStyleClass().remove("display-view");
        backlogDescription.getStyleClass().remove("display-view");

        backlogName.getStyleClass().add("edit-view");
        itemShortName.getStyleClass().add("edit-view");
        backlogDescription.getStyleClass().add("edit-view");

        backlogName.setDisable(false);
        itemShortName.setDisable(false);
        backlogDescription.setDisable(false);

        // Enable Product Owner ComboBox
        backlogPoCombobox.getStyleClass().remove("main-uneditable-combobox");
        backlogPoCombobox.setDisable(false);

        // Enable Scale ComboBox
        scaleCombo.getStyleClass().remove("main-uneditable-combobox");
        scaleCombo.setDisable(false);

        // Show story addition section
        storyLabel.setVisible(true);
        valueLabel.setVisible(true);
        storyPriority.setVisible(true);
        deleteStoryButton.setVisible(true);
        storyCombobox.setVisible(true);
        addStoryButton.setVisible(true);

        // Make table selectable
        backlogStories.getStyleClass().remove("table-display");

        backlogStories.getSelectionModel().clearSelection();



        // Show asterisks
        snAsterisk.setVisible(true);
    }

    /**
     * Product Owner ComboBox action
     */
    public void poComboboxAction() {
        if (!preventCombo) {
            try {
                appController.addCommand(new AddingNewCommand(new ChangeBacklogProductOwner(Workspace.getBacklog(currentItem),
                        Workspace.getPersonID(backlogPoCombobox.getValue())), addingNew));
            } catch (NullPointerException e) {
            }
        }
    }


    /**
     * Called from main WindowController to tell this windowController what the currently
     * selected backlog from the list is
     * @param uid currently selected backlog from list
     */
    public void setSelected(String uid) {
        super.setSelected(uid);
        Backlog backlog = Workspace.getBacklog(uid);

        addUndoableTextInput(backlogName, CommandType.BACKLOG_NAME);

        addUndoableTextInput(backlogDescription, CommandType.BACKLOG_DESCRIPTION);

        itemShortName.setText(backlog.getShortName());
        backlogName.setText(backlog.getFullName());
        backlogDescription.setText(backlog.getDescription());

        preventCombo = true;
        if(backlog.getProductOwner() != null){
            backlogPoCombobox.getSelectionModel().select(Workspace.getPersonName(backlog.getProductOwner()));
        }
        preventCombo = false;

        for(String story : backlog.getStories()){
            stories.add(Workspace.getStory(story));
        }

        // By default order by descending priority
        priorityColumn.setSortType(TableColumn.SortType.DESCENDING);
        populateStoriesTableView();
        backlogStories.getSortOrder().add(priorityColumn);

        updateComboBox();
    }

    /**
     * Set the focus based on a message
     * @param commandMessage Message to use for knowing what to update
     */
    public void setFocus(CommandMessage commandMessage)
    {
        String fxID = commandMessage.getItemProperty();
        String tab = commandMessage.getTab();

        if ("Stories".equals(tab)) {
            backlogTabPane.getSelectionModel().select(storiesTab);
        }
        else {
            backlogTabPane.getSelectionModel().select(propertiesTab);
        }

        editView();
        switch(fxID) {
            case ("itemShortName"):
                itemShortName.requestFocus();
                break;
            case ("backlogName"):
                backlogName.requestFocus();
                break;
            case ("backlogPoCombobox"):
                backlogPoCombobox.requestFocus();
                break;
            case ("backlogDescription"):
                backlogDescription.requestFocus();
                break;
            case ("storyCombobox"):
                storyCombobox.requestFocus();
                break;
            case ("backlogStories"):
                backlogStories.requestFocus();
                break;
            case ("scaleCombo"):
                scaleCombo.requestFocus();
                break;
        }
    }
}
