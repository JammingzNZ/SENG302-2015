package seng302.group6.Controller.Window;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.MenuBar;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import seng302.group6.Controller.Content.*;
import seng302.group6.Debug;
import seng302.group6.Model.Command.CommandMessage;
import seng302.group6.Model.ItemClasses.Workspace;
import seng302.group6.Model.ItemType;


/**
 * The controller for the main window. This should only contain code related
 * to the window itself, controller code related to the app in general should
 * be in AppController.
 *
 * Created by dan on 4/03/15.
 */
public class WindowController
{
    // ========================================================================
    // The naming of these variables is important. JavaFX uses them to
    // automatically get the controller of fx:include elements
    @FXML private VBox sideDisplayList;
    @FXML private SideDisplayListController sideDisplayListController;

    @FXML private MenuBar menuBar;
    @FXML private MenuBarController menuBarController;

    @FXML private ToolBar toolbar;
    @FXML private ToolBarController toolBarController;

    // ========================================================================

    @FXML public AnchorPane basePane = new AnchorPane();
    @FXML private AnchorPane content;

    public static WindowController instance;

    public FXMLLoader loader;
    public ContentController selected_mpc = null;

    private String lastItem;
    public ItemType lastItemType;

    private boolean searchIsShown = false;

    /**
     * Sets the static instance field to this so it can be referenced from
     * anywhere.
     */
    public WindowController()
    {
        instance = this;
        lastItemType = ItemType.PROJECT;
    }

    /**
     * Returns the controller for the side display list
     * @return the controller for the side display list
     */
    public static SideDisplayListController sideDisplayListController()
    {
        return WindowController.instance.sideDisplayListController;
    }

    /**
     * Returns the controller for the menu bar
     * @return the controller for the menu bar
     */
    public static MenuBarController menuBarController()
    {
        return WindowController.instance.menuBarController;
    }

    /**
     * Returns the controller for the menu bar
     * @return the controller for the menu bar
     */
    public static ToolBarController toolBarController()
    {
        return WindowController.instance.toolBarController;
    }

    /**
     * Shows projects in the side list and shows the first project in the main
     * content area.
     */
    public static void startupDefaultView()
    {
        String uid = Workspace.getCurrentProject();
        if(instance != null) {
            // Show the sidebar after loading
            instance.sideDisplayList.managedProperty().bind(instance.sideDisplayList.visibleProperty());
            instance.sideDisplayList.setVisible(true);
            sideDisplayListController().shouldIDisplay = Boolean.TRUE;

            // Show current project by default
            WindowController.sideDisplayListController().setSideBarCombo(ItemType.PROJECT);
            try {
                if (Workspace.getCurrentProject() != null) {
                    instance.setWindow("projectFrame.fxml");
                    Debug.println(instance.loader.getController().toString());
                    ProjectFrameController projfc = instance.loader.getController();
                    if (uid != null) {
                        projfc.setSelected(uid);
                        instance.sideDisplayListController.displayList.getSelectionModel().select(Workspace.getProjectName(uid));
                    }
                    projfc.setWindowController(instance);
                }
                else {
                    instance.clearWindow();
                }
            } catch (Exception e) {
                Debug.run(e::printStackTrace);
            }
        }
    }

    /**
     * Shows the selected side list item in the main content area
     * @param itemType the type of the item
     * @param selected the short name of selected item
     * @throws Exception an exception
     */
    public void setMainView(ItemType itemType, String selected) throws Exception
    {
        try {
            if(selected != null) {
                sideDisplayListController().setSideBarCombo(itemType);
                switch (itemType) {
                    case SKILL:
                        setWindow("skillFrame.fxml");
                        SkillFrameController skillfc = loader.getController();
                        skillfc.setSelected(Workspace.getSkillID(selected));
                        sideDisplayListController.displayList.getSelectionModel().select(Workspace.getSkillName(Workspace.getSkillID(selected)));
                        selected_mpc = skillfc;
                        break;
                    case PERSON:
                        setWindow("personFrame.fxml");
                        PersonFrameController personfc = loader.getController();
                        personfc.setSelected(Workspace.getPersonID(selected));
                        sideDisplayListController.displayList.getSelectionModel().select(Workspace.getPersonName(Workspace.getPersonID(selected)));
                        selected_mpc = personfc;
                        break;
                    case PROJECT:
                        Workspace.setCurrentProject(Workspace.getProjectID(selected));
                        setWindow("projectFrame.fxml");
                        ProjectFrameController projectfc = loader.getController();
                        projectfc.setSelected(Workspace.getProjectID(selected));
                        sideDisplayListController.displayList.getSelectionModel().select(Workspace.getProjectName(Workspace.getProjectID(selected)));
                        selected_mpc = projectfc;
                        break;
                    case TEAM:
                        setWindow("teamFrame.fxml");
                        TeamFrameController teamfc = loader.getController();
                        teamfc.setSelected(Workspace.getTeamID(selected));
                        sideDisplayListController.displayList.getSelectionModel().select(Workspace.getTeamName(Workspace.getTeamID(selected)));
                        selected_mpc = teamfc;
                        break;
                    case RELEASE:
                        setWindow("releaseFrame.fxml");
                        ReleaseFrameController releasefc = loader.getController();
                        releasefc.setSelected(Workspace.getReleaseID(selected));
                        sideDisplayListController.displayList.getSelectionModel().select(Workspace.getReleaseName(Workspace.getReleaseID(selected)));
                        selected_mpc = releasefc;
                        break;
                    case STORY:
                        setWindow("storyFrame.fxml");
                        StoryFrameController storyfc = loader.getController();
                        storyfc.setSelected(Workspace.getStoryID(selected));
                        sideDisplayListController.displayList.getSelectionModel().select(Workspace.getStoryName(Workspace.getStoryID(selected)));
                        selected_mpc = storyfc;
                        break;
                    case BACKLOG:
                        setWindow("backlogFrame.fxml");
                        BacklogFrameController backlogfc = loader.getController();
                        backlogfc.setSelected(Workspace.getBacklogID(selected));
                        sideDisplayListController.displayList.getSelectionModel().select(Workspace.getBacklogName(Workspace.getBacklogID(selected)));
                        selected_mpc = backlogfc;
                        break;
                    case SPRINT:
                        /*if (Workspace.getSprint(Workspace.getSprintID(selected)).getSettingUp()) {
                            setWindow("sprintSetupFrame.fxml");
                        } else {*/
                            setWindow("sprintFrame.fxml");
                        //}
                        SprintFrameController sprintfc = loader.getController();
                        sprintfc.setSelected(Workspace.getSprintID(selected));
                        sideDisplayListController.displayList.getSelectionModel().select(Workspace.getSprintName(Workspace.getSprintID(selected)));
                        selected_mpc = sprintfc;
                        break;
                }
            }
            selected_mpc.setWindowController(this);
            lastItem = selected;
            lastItemType = itemType;
            searchIsShown = false;
        }
        catch (NullPointerException e)
        {
            Debug.run(e::printStackTrace);
        }
    }

    /**
     * Shows the undone/redone item in the main content area and highlights the
     * field that was changed.
     * @param itemType the type of the item
     * @param selected short name of the item
     * @param commandMessage Message to use for detecting changes
     * @param addingNew whether we are adding a new item or editing and old one
     * @throws Exception an exception
     */
    public void setMainViewForChanges(ItemType itemType, String selected, CommandMessage commandMessage, Boolean addingNew) throws Exception
    {

            if(selected != null) {
                sideDisplayListController().setSideBarCombo(itemType);
                switch (itemType) {
                    case SKILL:
                        setWindow("skillFrame.fxml");
                        SkillFrameController skillfc = loader.getController();
                        skillfc.setSelected(Workspace.getSkillID(selected));
                        sideDisplayListController.displayList.getSelectionModel().select(Workspace.getSkillName(Workspace.getSkillID(selected)));
                        skillfc.setFocus(commandMessage);
                        selected_mpc = skillfc;
                        break;
                    case PERSON:
                        setWindow("personFrame.fxml");
                        PersonFrameController personfc = loader.getController();
                        personfc.setSelected(Workspace.getPersonID(selected));
                        sideDisplayListController.displayList.getSelectionModel().select(Workspace.getPersonName(Workspace.getPersonID(selected)));
                        personfc.setFocus(commandMessage);
                        selected_mpc = personfc;
                        break;
                    case PROJECT:
                        Workspace.setCurrentProject(Workspace.getProjectID(selected));
                        setWindow("projectFrame.fxml");
                        ProjectFrameController projectfc = loader.getController();
                        projectfc.setSelected(Workspace.getProjectID(selected));
                        sideDisplayListController.displayList.getSelectionModel().select(Workspace.getProjectName(Workspace.getProjectID(selected)));
                        projectfc.setFocus(commandMessage);
                        selected_mpc = projectfc;
                        break;
                    case TEAM:
                        setWindow("teamFrame.fxml");
                        TeamFrameController teamfc = loader.getController();
                        teamfc.setSelected(Workspace.getTeamID(selected));
                        sideDisplayListController.displayList.getSelectionModel().select(Workspace.getTeamName(Workspace.getTeamID(selected)));
                        teamfc.setFocus(commandMessage);
                        selected_mpc = teamfc;
                        break;
                    case RELEASE:
                        setWindow("releaseFrame.fxml");
                        ReleaseFrameController releasefc = loader.getController();
                        releasefc.setSelected(Workspace.getReleaseID(selected));
                        sideDisplayListController.displayList.getSelectionModel().select(Workspace.getReleaseName(Workspace.getReleaseID(selected)));
                        releasefc.setFocus(commandMessage);
                        selected_mpc = releasefc;
                        break;
                    case STORY:
                        setWindow("storyFrame.fxml");
                        StoryFrameController storyfc = loader.getController();
                        storyfc.setSelected(Workspace.getStoryID(selected));
                        sideDisplayListController.displayList.getSelectionModel().select(Workspace.getStoryName(Workspace.getStoryID(selected)));
                        storyfc.setFocus(commandMessage);
                        selected_mpc = storyfc;
                        break;
                    case BACKLOG:
                        setWindow("backlogFrame.fxml");
                        BacklogFrameController backlogfc = loader.getController();
                        backlogfc.setSelected(Workspace.getBacklogID(selected));
                        sideDisplayListController.displayList.getSelectionModel().select(Workspace.getBacklogName(Workspace.getBacklogID(selected)));
                        backlogfc.setFocus(commandMessage);
                        selected_mpc = backlogfc;
                        break;
                    case SPRINT:
                        setWindow("sprintFrame.fxml");
                        SprintFrameController sprintfc = loader.getController();
                        sprintfc.setSelected(Workspace.getSprintID(selected));
                        sideDisplayListController.displayList.getSelectionModel().select(Workspace.getSprintName(Workspace.getSprintID(selected)));
                        sprintfc.setFocus(commandMessage);
                        selected_mpc = sprintfc;
                }
            }
            selected_mpc.setWindowController(this);
            if (addingNew) {
                selected_mpc.addingNew();
            }

            lastItem = selected;
            lastItemType = itemType;
    }

    /**
     * Replace the content pane with the search pane
     */
    public void showSearch()
    {
        String li = lastItem;
        ItemType lit = lastItemType;
        if (!searchIsShown) {
            try {
                setWindow("search.fxml");
            } catch (Exception e) {
                e.printStackTrace();
            }
            searchIsShown = true;
            lastItem = li;
            lastItemType = lit;
        } else {
            showLast();
        }
        updateGUI();
    }

    /**
     * Shows the last item that was shown
     */
    public void showLast()
    {
        if (lastItem != null && lastItemType != null) {
            try {
                setMainView(lastItemType, lastItem);
            } catch (Exception e) {
                Debug.run(e::printStackTrace);
            }
        }
        else {
            clearWindow();
        }
        searchIsShown = false;
    }

    /**
     * Clears the main window frame of content
     */
    public void clearWindow()
    {
        content.getChildren().clear();
        selected_mpc = null;
        lastItem = null;
        lastItemType = null;
        sideDisplayListController().setEnable(true);
        sideDisplayListController.displayList.getSelectionModel().clearSelection();
    }

    /**
     * Sets the content of the main window frame
     * @param resource the FXML filename to inject into the main pane window
     * @throws Exception an exception
     */
    public void setWindow(String resource) throws Exception
    {
        Object subView;
        clearWindow();
        //content.getChildren().add(FXMLLoader.load(getClass().getClassLoader().getResource(resource)));
        //content.getChildren().add(FXMLLoader.load(getClass().getClassLoader().getResource(resource)));
        loader = new FXMLLoader();
        subView = loader.load(getClass().getClassLoader().getResource(resource).openStream());
        content.getChildren().add((Node) subView); // Adds the new frame to the window

        Node newView = content.getChildren().get(0); // Newly added view/frame/thing
        // Make the new view resize with the main window
        AnchorPane.setTopAnchor(newView, 0.0);
        AnchorPane.setBottomAnchor(newView, 0.0);
        AnchorPane.setLeftAnchor(newView, 0.0);
        AnchorPane.setRightAnchor(newView, 0.0);
    }

    /**
     * When updateList is called it checks to see the current list contents and re-populates and displays the list
     * @param listType is a String of the current loaded content name
     */
    public void updateList(ItemType listType)
    {
        if (sideDisplayListController.currentList == listType) {
            sideDisplayListController.displayItem(listType);
            Debug.println("updated sidelist : " + listType.getPlural());
            lastItemType = listType;
        }
    }

    /**
     * Set the title of the window.
     * @param title String to set the title to.
     */
    private void setTitle(String title) {
        Stage primaryStage = (Stage) basePane.getScene().getWindow();
        primaryStage.setTitle(title);
    }

    /**
     * Update the window's title based on whether there are saved changes or not.
     */
    public void updateSaved() {
        boolean saved = Workspace.getSaved();
        menuBarController.saveItem.setDisable(saved);
        String titleString = "Scrum Machine";
        String fileString = Workspace.getFileString();
        if (fileString != null) {
            titleString += " - " + fileString;
        }
        if (!saved) {
            titleString += "*";
        }
        setTitle(titleString);

        boolean savedAs = (false);
        menuBarController.saveAsItem.setDisable(savedAs);

        toolBarController.updateSaved(saved);
    }

    /**
     * Enables or disables undo and redo menu items depending on whether there
     * are any changes to undo or redo
     */
    public void updateUndoRedo() {
        CommandMessage undoMessage = Workspace.getUndoMessage();
        CommandMessage redoMessage = Workspace.getRedoMessage();
        menuBarController.updateUndoRedo(undoMessage, redoMessage);
        toolBarController.updateUndoRedo(undoMessage, redoMessage);
    }

    /**
     * Updates the GUI by calling all the update methods.
     */
    public void updateGUI()
    {
        updateUndoRedo();
        if(sideDisplayListController.currentList != null) {
            updateList(sideDisplayListController.currentList);
        }
        updateSaved();
    }

    /**
     * Sets the side display list to be the focused element
     */
    public void setFocusToPanel() {
        sideDisplayList.requestFocus();
    }

    /**
     * Handles a mouse click somewhere in the window.
     */
    @FXML
    public void handleMouseClick()
    {
        toolBarController.hidePopover();
    }
}