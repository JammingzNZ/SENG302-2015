package seng302.group6.Controller.Content;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.util.Callback;
import seng302.group6.Controller.Window.WindowController;
import seng302.group6.Model.Command.AddingNewCommand;
import seng302.group6.Model.Command.CommandMessage;
import seng302.group6.Model.Command.Release.ChangeAssociatedProject;
import seng302.group6.Model.Command.Release.ChangeReleaseDate;
import seng302.group6.Model.ItemClasses.Release;
import seng302.group6.Model.ItemClasses.Workspace;
import seng302.group6.Model.ItemType;

import java.time.LocalDate;

/**
 * Controller for the Release Frame
 * Created by Michael Wheeler on 3/05/15.
 */
public class ReleaseFrameController extends ContentController
{

    @FXML TextArea releaseDescription;
    @FXML ComboBox<String> releaseProjectComboBox;
    @FXML DatePicker releaseEstimatedDate;

    private String oldReleaseName;
    private String oldReleaseDescription;
    private String oldReleaseDate;
    private String oldAssociatedProject;

    @FXML Label snAsterisk;
    @FXML Label apAsterisk;

    Boolean preventCombo = false;


    /**
     * Intializes the Release Frame
     */
    public void initialize(){
        preventCombo = true;
        releaseProjectComboBox.getItems().addAll(Workspace.getProjectNames(Workspace.getProjects()));
        releaseEstimatedDate.setEditable(false);
        updateDate();
        preventCombo = false;
    }

    /**
    * populates the map of text fields, keying them so that they may be
    * referred to by the name reported in validation reports.
    */
    protected void populateFieldMap()
    {
        fieldMap.put("shortName", itemShortName);
        fieldMap.put("description", releaseDescription);
        fieldMap.put("releaseDate", releaseEstimatedDate);
        fieldMap.put("associatedProject", releaseProjectComboBox);
    }

    /**
     * Switches between the Done and edit modes
     */
    @FXML
    public void doneEditAction(){
        if (this.isEditing()) {
            displayView();
            WindowController.instance.updateGUI();
            WindowController.instance.updateList(ItemType.RELEASE);
            WindowController.sideDisplayListController().setListItem(Workspace.getReleaseName(currentItem), ItemType.RELEASE);
            clearErrorNotification();
        } else {
            editView();
        }
    }

    /**
     * Give more generic methods access to the item type
     * @return The RELEASE item type
     */
    protected ItemType getItemType() {
        return ItemType.RELEASE;
    }

    /**
     * Switches to the edit mode for the frame
     */
    protected void editView()
    {        
        super.editView();

        itemShortName.getStyleClass().remove("display-view");
        releaseDescription.getStyleClass().remove("display-view");

        itemShortName.getStyleClass().add("edit-view");
        releaseDescription.getStyleClass().add("edit-view");

        Release release = Workspace.getRelease(currentItem);
        if (!release.hasSprint()) {
            releaseProjectComboBox.setDisable(false);
            releaseEstimatedDate.setDisable(false);
        }
        itemShortName.setDisable(false);
        releaseDescription.setDisable(false);

        // Show asterisks
        snAsterisk.setVisible(true);
        apAsterisk.setVisible(true);
    }

    /**
     * Switches to teh display view for the frame
     */
    protected void displayView()
    {
        super.displayView();

        itemShortName.getStyleClass().remove("edit-view");
        releaseDescription.getStyleClass().remove("edit-view");

        itemShortName.getStyleClass().add("display-view");
        releaseDescription.getStyleClass().add("display-view");
        releaseDescription.getStyleClass().add("text-area-display-view");

        itemShortName.setDisable(true);
        releaseDescription.setDisable(true);
        releaseEstimatedDate.setDisable(true);
        releaseProjectComboBox.setDisable(true);
        releaseProjectComboBox.setStyle("-fx-opacity: 1");
        releaseEstimatedDate.setStyle("-fx-opacity: 1");

        // Hide asterisks
        snAsterisk.setVisible(false);
        apAsterisk.setVisible(false);
    }

    /**
     * Associated Project combo box action
     */
    @FXML
    public void associatedProjectAction(){
        if (!preventCombo) {
            try {
                appController.addCommand(new AddingNewCommand(new ChangeAssociatedProject(Workspace.getRelease(currentItem),
                        Workspace.getProjectID(releaseProjectComboBox.getValue())),
                        addingNew));
            } catch (Exception e) {
            }
        }
    }

    /**
     * Estimated Release date action
     */
    @FXML
    public void releaseDateAction(){
        if (!preventCombo) {
            try {
                appController.addCommand(new AddingNewCommand(new ChangeReleaseDate(Workspace.getRelease(currentItem),
                        releaseEstimatedDate.getValue().toString()),
                        addingNew
                ));
            } catch (NullPointerException e) {
            }
        }
    }


    /**
     * Makes all days before today disabled in the datepicker
     */
    public void updateDate(){
        preventCombo = true;
        LocalDate today = LocalDate.now();
        final Callback<DatePicker, DateCell> dayCellFactory =
                new Callback<DatePicker, DateCell>() {
                    @Override
                    public DateCell call(final DatePicker datePicker) {
                        return new DateCell() {
                            @Override
                            public void updateItem(LocalDate item, boolean empty) {
                                super.updateItem(item, empty);
                                if (item.isBefore(today)) {
                                    setDisable(true);
                                    setStyle("-fx-background-color: #ffc0cb;");
                                }
                            }
                        };
                    }
                };
        releaseEstimatedDate.setDayCellFactory(dayCellFactory);
        preventCombo = false;
    }

    /**
     * Sets the fields when displaying a release
     * @param uid is the unique id of the item to set as current (the one selected in sidebar list for example).
     */
    public void setSelected(String uid)
    {
        super.setSelected(uid);
        preventCombo = true;
        addUndoableTextInput(releaseDescription, CommandType.RELEASE_DESCRIPTION);
        Release release = Workspace.getRelease(uid);
        // Set text-field values
        itemShortName.setText(release.getShortName());
        releaseDescription.setText(release.getDescription());
        releaseProjectComboBox.getSelectionModel().select(Workspace.getProjectName(release.getAssociatedProject()));
        if(release.getReleaseDate() != null && !release.getReleaseDate().equals("")){
            LocalDate estimatedDate = LocalDate.parse(release.getReleaseDate());
            releaseEstimatedDate.setValue(estimatedDate);
        }
        preventCombo = false;
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
            case("releaseEstimatedDate"):
                releaseEstimatedDate.requestFocus();
                break;
            case("releaseProjectComboBox"):
                releaseProjectComboBox.requestFocus();
                break;
            case("releaseDescription"):
                releaseDescription.requestFocus();
                break;
        }
    }
}
