package seng302.group6.Controller.Window;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.util.Callback;
import seng302.group6.Controller.AppController;
import seng302.group6.Controller.SearchController;
import seng302.group6.Debug;
import seng302.group6.Model.ItemClasses.Item;
import seng302.group6.Model.ItemClasses.Workspace;
import seng302.group6.Model.ItemType;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Controller for the side display list
 * Created by jaln on 12/05/15.
 */
public class SideDisplayListController
{
    @FXML Button deleteItem;
    //@FXML Label displayListTitle;
    @FXML ComboBox<String> sideBarCombo;
    @FXML public ListView<String> displayList;
    @FXML private VBox sideDisplayList;

    protected Boolean shouldIDisplay = Boolean.FALSE;

    public ItemType viewType = ItemType.PROJECT;
    public ItemType currentList = null;


    /**
     * Initializes the side display list
     */
    public void initialize()
    {
        populateSideBarCombo();
        displayList.setCellFactory(new Callback<ListView<String>, ListCell<String>>() {
            @Override
            public ListCell<String> call(ListView<String> param) {
                return new ListCell<String>() {
                    @Override
                    public void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        boolean searching = false;
                        SearchController sc = null;

                        if (WindowController.instance.loader.getController().getClass().equals(SearchController.class)) {
                            searching = true;
                            sc = WindowController.instance.loader.getController();
                        }

                        if (searching) {
                            if (empty) {
                                setText(null);
                                setStyle(null);
                            } else if (isSelected()) {
                                setText(item);
                                setStyle(null);
                            } else {
                                boolean highlighted = false;
                                ArrayList<Item> searchResults = sc.getResults();
                                if (searchResults != null) {
                                    ArrayList<String> shortNames = new ArrayList();
                                    for (Item result : searchResults) {
                                        shortNames.add(result.getShortName());
                                    }
                                    highlighted = shortNames.contains(item);
                                }
                                setText(item);
                                if (highlighted) {
                                    setStyle("-fx-background-color: yellow;");
                                } else {
                                    setStyle(null);
                                }
                            }
                        } else {
                            setStyle(null);
                            if (empty) {
                                setText(null);
                            } else {
                                setText(item);
                            }
                        }
                        setGraphic(null);
                    }
                };
            }
        });
    }

    /**
     * Populates the combo box with the Itemtype plurals.
     */
    public void populateSideBarCombo() {
        ArrayList<String> plurals = new ArrayList<>();
        for (ItemType type : ItemType.values()) {
            plurals.add(type.getPlural());
        }
        sideBarCombo.getItems().setAll(plurals);
    }

    /**
     * Shows the item clicked on in the main content area if the user is not
     * editing
     * @throws Exception an exception
     */
    @FXML
    public void handleMouseClick() throws Exception
    {
        Debug.println("clicked on " + displayList.getSelectionModel().getSelectedItem());
        boolean edit = false;
        if(WindowController.instance.selected_mpc != null) {
            /*if (WindowController.instance.selected_mpc.isEditing()) {
                Debug.println("Edit in progress, ignoring.");
                edit = true;
            }*/
        }
        if (!edit) {
            String selected = displayList.getSelectionModel().getSelectedItem();
            WindowController.instance.setMainView(viewType, selected);
        }
        WindowController.instance.updateGUI();
        WindowController.toolBarController().hidePopover();
    }

    /**
     * Displays the create new pane for whatever items are showing in the display list
     * @throws Exception an exception
     */
    @FXML
    public void addSelectedItem() throws Exception{
        try {
            switch (currentList) {
                case SKILL:
                    AppController.instance().newSkill();
                    break;
                case PERSON:
                    AppController.instance().newPerson();
                    break;
                case TEAM:
                    AppController.instance().newTeam();
                    break;
                case PROJECT:
                    AppController.instance().newProject();
                    break;
                case RELEASE:
                    AppController.instance().newRelease();
                    break;
                case STORY:
                    AppController.instance().newStory();
                    break;
                case BACKLOG:
                    AppController.instance().newBacklog();
                    break;
                case SPRINT:
                    AppController.instance().newSprint();
                    break;
            }
        }catch(Exception e){
            Debug.run(() -> e.printStackTrace());
        }

    }

    /**
     * Deletes the selected Item in the display list or provides an appropriate dialog pop up..
     */
    public void deleteSelectedItem()
    {
        String selectedItemName = displayList.getSelectionModel().getSelectedItem();
        // Quick fix to stop a null pointer error occuring.
        if(selectedItemName == null)
        {
            return;
        }
        switch (currentList)
        {
            case SKILL:
                // Sets the skillID
                String skillID = Workspace.getSkillID(selectedItemName);
                if(AppController.instance().peopleWithSkill(skillID).size() > 0) {
                    AppController.instance().deleteSkillPopup(skillID);
                }else{
                    Workspace.deleteSkill(skillID);
                    WindowController.instance.clearWindow();
                }
                break;
            case PERSON:
                String personID = Workspace.getPersonID(selectedItemName);
                Workspace.deletePerson(personID);
                WindowController.instance.clearWindow();
                break;
            case TEAM:
                String teamID = Workspace.getTeamID(selectedItemName);
                if (Workspace.getTeam(teamID).hasSprint()) {
                    AppController.instance().cannotDeleteTeamPopup(teamID);
                } else {
                    if (Workspace.getTeam(teamID).getPeople().size() > 0) {
                        AppController.instance().deleteTeamPopup(teamID);
                    } else { // The team has no-one in the team
                        Workspace.deleteTeamOnly(teamID);
                        WindowController.instance.clearWindow();
                    }
                }
                break;
            case PROJECT:
                String projectID = Workspace.getProjectID(selectedItemName);
                if (Workspace.getProject(projectID).hasSprint()) {
                    AppController.instance().cannotDeleteProjectPopup(projectID);
                } else {
                    AppController.instance().deleteProjectPopup(projectID);
                }
                break;
            case RELEASE:
                String releaseID = Workspace.getReleaseID(selectedItemName);
                if (Workspace.getRelease(releaseID).hasSprint()) {
                    AppController.instance().cannotDeleteReleasePopup(releaseID);
                } else {
                    Workspace.deleteRelease(releaseID);
                    WindowController.instance.clearWindow();
                }
                break;
            case STORY:
                String storyID = Workspace.getStoryID(selectedItemName);
                if (Workspace.getStory(storyID).hasSprint()) {
                    AppController.instance().cannotDeleteStoryPopup(storyID);
                } else {
                    Workspace.deleteStory(storyID);
                    WindowController.instance.clearWindow();
                }
                break;
            case BACKLOG:
                String backlogId = Workspace.getBacklogID(selectedItemName);
                if (Workspace.getBacklog(backlogId).hasSprint()) {
                    AppController.instance().cannotDeleteBacklogPopup(backlogId);
                } else {
                    Workspace.deleteBacklog(backlogId);
                    WindowController.instance.clearWindow();
                }
                break;
            case SPRINT:
                String sprintID = Workspace.getSprintID(selectedItemName);
                Workspace.deleteSprint(sprintID);
                WindowController.instance.clearWindow();
                break;
        }
        //TODO: Create listeners for all these updates so we dont have to keep using them
        WindowController.instance.updateGUI();
        Debug.println("Deleted Item: " + selectedItemName);
    }

    /**
     * Enables or disables the side list
     * @param editable true if the list should be enabled, false if it should
     *                 be disabled
     */
    public void setEnable(boolean editable) {
        displayList.setDisable(!editable);
    }

    /**
     * Shows the sidebar or does nothing if it is already shown
     */
    public void showSidebar()
    {
        if (!this.shouldIDisplay) {
            sideDisplayList.managedProperty().bind(sideDisplayList.visibleProperty());
            sideDisplayList.setVisible(true);
            this.shouldIDisplay = Boolean.TRUE;
        }
    }

    /**
     * Hides the sidebar or does nothing if it is already hidden
     */
    public void hideSidebar()
    {
        if (this.shouldIDisplay) {
            sideDisplayList.managedProperty().bind(sideDisplayList.visibleProperty());
            sideDisplayList.setVisible(false);
            this.shouldIDisplay = Boolean.FALSE;
        }
    }

    /**
     * Highlights an item in the side list with String item
     * @param item short name of item to be selected
     * @param ListType type of the list
     */
    public void setListItem(String item, ItemType ListType)
    {
        if(currentList == ListType) {
            displayList.getSelectionModel().select(item);
        }
    }

    /**
     * Sets the Combo box on the side display list to a given Itemtype; causing an update of the list if needed.
     * @param itemtype the itemtype that the list should be changed/updated to.
     */
    public void setSideBarCombo(ItemType itemtype) {
        sideBarCombo.setValue(itemtype.getPlural());
    }

    /**
     * Combo box action updates/changes the sidedisplay list
     */
    @FXML
    public void sideBarComboAction() {
        String plural = sideBarCombo.getValue();
        for(ItemType type: ItemType.values()) {
            if(plural.equals(type.getPlural())) {
                menuDisplayItemsAction(type);
            }
        }
    }

    /**
     * Populate the display list with a list of items of the given type
     * Also focus on the first item in the list when switching (if there is one)
     * @param itemType Type of items to show
     */
    private void menuDisplayItemsAction(ItemType itemType) {
        ArrayList<String> itemList = Workspace.getItems(itemType);

        Boolean wasItem = viewType == itemType;

        displayItem(itemType);

        //Focus on the first item in the list, or clear the view if there are no items
        if (!wasItem) {
            if (!itemList.isEmpty()) {
                itemList = (ArrayList<String>) itemList.clone();
                itemList.sort(new Comparator<String>() {
                    @Override
                    public int compare(String o1, String o2) {
                        return o1.toLowerCase().compareTo(o2.toLowerCase());
                    }
                });
                try {
                    WindowController.instance.setMainView(itemType, itemList.get(0));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                WindowController.instance.clearWindow();
            }
        }
    }

    /**
     * Displays a list of items in the side display list
     * @param item the selected item
     */
    public void displayItem(ItemType item)
    {
        this.currentList = item;


        ObservableList<String> items = FXCollections.observableArrayList(Workspace.getItems(item));
        boolean reselect = false;
        if (!item.equals(viewType)) {
            displayList.getSelectionModel().clearSelection(); // deselect current selection
        } else {
            reselect = true;
        }
        viewType = item;
        displayList.setItems(items);
        currentList = item;
        showSidebar();

        // Sort items by alphabetical order
        Collections.sort(displayList.getItems(), String.CASE_INSENSITIVE_ORDER);

        if(WindowController.instance.selected_mpc != null && reselect) {
            try {
                displayList.getSelectionModel().select(Workspace.getItemName(WindowController.instance.selected_mpc.currentItem));
            }
            catch (NullPointerException e) {}
        }
        createContextMenu();
    }

    /**
     * Creates a context menu contain create and delete functionality
     */
    public void createContextMenu(){
        final ContextMenu cm = new ContextMenu();
        MenuItem delete = new MenuItem("Delete");
        delete.setOnAction(e -> deleteSelectedItem());
        MenuItem add = new MenuItem("Create " + currentList.toString());
        add.setOnAction(event -> {
            try {
                addSelectedItem();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        cm.getItems().add(add);
        cm.getItems().add(delete);
        displayList.setContextMenu(cm);
    }

}
