package seng302.group6.Controller.Window;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.controlsfx.control.ButtonBar;
import org.controlsfx.control.action.Action;
import org.controlsfx.dialog.Dialog;
import org.controlsfx.dialog.DialogAction;
import org.controlsfx.dialog.Dialogs;
import seng302.group6.Controller.AppController;
import seng302.group6.Controller.WorkspaceFileChooser;
import seng302.group6.Model.Command.CommandMessage;
import seng302.group6.Model.ItemClasses.Workspace;
import seng302.group6.Model.ItemType;
import seng302.group6.WorkspaceLoadException;
import seng302.group6.WorkspaceVersionMismatchException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import static java.lang.System.out;

/**
 * Controller for the menu bar
 * Created by jaln on 12/05/15.
 */
public class MenuBarController
{
    @FXML protected MenuItem undoItem;
    @FXML protected MenuItem redoItem;

    @FXML protected MenuItem saveItem;
    @FXML protected MenuItem saveAsItem;

    /**
     * Close Menu Item
     * Clicking 'Close' will close the whole program
     */
    @FXML
    public void menuCloseAction()
    {
        if(AppController.instance().unsavedWarning()){
            Platform.exit();
        }
    }

    /**
     * Open Menu Item
     * Clicking 'Open' will show File Chooser
     * @throws Exception an exception
     */
    @FXML
    public void menuLoadAction() throws Exception
    {
        if (AppController.instance().unsavedWarning()) {
            WorkspaceFileChooser fileChooser = new WorkspaceFileChooser("Open Workspace", "open", "", AppController.BRAND_PROJECT_FILE_DESC, Workspace.appdata.getWorkspaceFileExtension());
            File file = fileChooser.getFile();

            if (file != null) {
                try {
                    Workspace.loadData(file);
                } catch (FileNotFoundException | WorkspaceLoadException | WorkspaceVersionMismatchException e) {
                    AppController.instance().showErrorPopup("Open Workspace Error", e.getMessage());
                    return;
                }

                WindowController.instance.updateGUI();
                WindowController.startupDefaultView();
            }
        }
    }

    /**
     * Toggle displaylist Menu Item
     * Click 'Toggle' will open or close the displaylist
     */
    @FXML
    public void menuToggleAction()
    {
        if (WindowController.sideDisplayListController().shouldIDisplay == Boolean.TRUE) {
            // Hides the sidePanel
            WindowController.sideDisplayListController().hideSidebar();
        } else {
            // Shows the sidePanel
            WindowController.sideDisplayListController().showSidebar();
        }
    }

    /**
     * Display People Menu ItemController.instance.displayItem
     * Click 'People' will populate the display list with all people
     */
    @FXML
    public void menuDisplayPeopleAction()
    {
        WindowController.sideDisplayListController().setSideBarCombo(ItemType.PERSON);
    }

    /**
     * Display Projects Menu Item
     * Click 'Projects' will populate the display list with all projects
     */
    @FXML
    public void menuDisplayProjectsAction()
    {
        WindowController.sideDisplayListController().setSideBarCombo(ItemType.PROJECT);
    }

    /**
     * Display Skills
     * Populate the list with skills
     */
    @FXML
    public void menuDisplaySkillsAction()
    {
        WindowController.sideDisplayListController().setSideBarCombo(ItemType.SKILL);
    }

    /**
     * Display Teams
     * Populate the list with teams
     */
    @FXML
    public void menuDisplayTeamsAction()
    {
        WindowController.sideDisplayListController().setSideBarCombo(ItemType.TEAM);
    }

    /**
     * Display Releases
     * Populates the list with releases
     */
    @FXML
    public void menuDisplayReleasesAction()
    {
        WindowController.sideDisplayListController().setSideBarCombo(ItemType.RELEASE);
    }

    /**
     * Display Stories
     * Populates the list with stories
     */
    @FXML
    public void menuDisplayStoriesAction()
    {
        WindowController.sideDisplayListController().setSideBarCombo(ItemType.STORY);
    }

    /**
     * Display Backlogs
     * Populates the list with backlogs
     */
    public void menuDisplayBacklogsAction()
    {
        WindowController.sideDisplayListController().setSideBarCombo(ItemType.BACKLOG);
    }

    /**
     * Display Sprints
     * Populate the list with sprints
     */
    @FXML
    public void menuDisplaySprintsAction() {
        WindowController.sideDisplayListController().setSideBarCombo(ItemType.SPRINT);
    }

    /**
     * File New Person Menu Item
     * Click 'Person' will initiate user to create a new Person
     * @throws Exception an exception
     */
    @FXML
    public void menuNewTeamAction() throws Exception
    {
        AppController.instance().newTeam();
    }

    /**
     * File New Release Menu Item
     * Click 'Release' will initiate user to create a new Release
     * @throws Exception an exception
     */
    @FXML
    public void menuNewReleaseAction() throws Exception
    {
        AppController.instance().newRelease();
    }

    /**
     * File New Person Menu Item
     * Click 'Person' will initiate user to create a new Person
     * @throws Exception an exception
     */
    @FXML
    public void menuNewSkillAction() throws Exception
    {
        AppController.instance().newSkill();
    }


    /**
     * File New Story Menu Item
     * Click 'Story' will initiate user to create a new Story
     * @throws Exception an Exception
     */
    @FXML
    public void menuNewStoryAction() throws Exception
    {
        AppController.instance().newStory();
    }

    /**
     * File New Backlog Menu Item
     * Clicking 'Backlog' will initiate user to create a new Backlog
     * @throws Exception an exception
     */
    @FXML
    public void menuNewBacklogAction() throws Exception
    {
        AppController.instance().newBacklog();
    }

    /**
     * File New Sprint Menu Item
     * Clicking 'Sprint' will initiate user to create a new Sprint
     * @throws Exception an Exception
     */
    @FXML
    public void menuNewSprintAction() throws Exception
    {
        AppController.instance().newSprint();
    }

    /**
     * Allows the user to select a destination for a report to be written to
     * default location of file set to project save location (appdata stored)
     * default name of file is "Status-Report_dd-MM-yy_projectName.xml"
     * @throws Exception an exception
     */
    @FXML
    public void menuExportStatusReport() throws Exception{
        Parent root = FXMLLoader.load((getClass().getClassLoader().getResource("reportFrame.fxml")));
        Stage reportStage = new Stage();
        reportStage.setScene(new Scene(root, 450, 470));
        reportStage.setResizable(false);
        reportStage.initModality(Modality.APPLICATION_MODAL);
        reportStage.show();
    }

    /**
     * File Save Menu Item
     * Click save will save the current project to specified directory
     */
    @FXML
    public void menuSaveAction()
    {
        boolean destinationSet = false;
        try {
            destinationSet = Workspace.saveData();
        }
        catch(IOException e) {
            AppController.instance().showErrorPopup("Error Saving Workspace", e.getMessage());
            return;
        }

        if (destinationSet) {
            WindowController.instance.updateSaved();
        }
        else {
            menuSaveAsAction();
        }
    }

    /**
     * Allows the user to select a new save destination and saves to it
     */
    @FXML
    public void menuSaveAsAction() {
        AppController.instance().saveWorkspace();
    }

    /**
     * File Revert Menu Item
     * Return to the last save state
     */
    @FXML
    public void menuRevertAction() {
        Action revertButton = new DialogAction("Revert", ButtonBar.ButtonType.OTHER);
        Action response = Dialogs.create()
                .owner(WindowController.instance.basePane.getScene().getWindow())
                .title("Warning")
                .masthead("Are you sure you want to revert? All changes since your last save will be lost.")
                .actions(revertButton, Dialog.ACTION_CANCEL)
                .showConfirm();
        if (response == revertButton) {
            if (WindowController.instance.selected_mpc != null) {
                WindowController.instance.selected_mpc.forceTextInputCommand();
            }
            Workspace.revert();
            WindowController.instance.updateGUI();
            WindowController.instance.clearWindow();
        }
        //}
    }

    /**
     * File New Person Menu Item
     * Click 'Person' will initiate user to create a new Person
     * @throws Exception an exception
     */
    @FXML
    public void menuNewPersonAction() throws Exception
    {
        AppController.instance().newPerson();
    }

    /**
     * File New Project Menu Item
     * Click 'Project' will initiate user to create a new Project
     * @throws Exception an exception
     */
    @FXML
    public void menuNewProjectAction() throws Exception
    {
        AppController.instance().newProject();
    }

    /**
     * File New Workspace Menu Item
     * Click 'Project' will initiate user to create a new Project
     * @throws Exception an exception
     */
    @FXML
    public void menuNewWorkspaceAction() throws Exception
    {
        if (AppController.instance().unsavedWarning()) {
            WorkspaceFileChooser fileChooser = new WorkspaceFileChooser("New Workspace", "save", "",
                    AppController.BRAND_PROJECT_FILE_DESC,
                    Workspace.appdata.getWorkspaceFileExtension());
            File file = fileChooser.getFile();

            if (file != null) {
                Workspace.reset();
                try {
                    Workspace.saveData(file);
                } catch (IOException e) {
                    AppController.instance().showErrorPopup("Error Creating Workspace", e.getMessage());
                }
                WindowController.instance.updateGUI();
                WindowController.startupDefaultView();
            }
        }
    }

    /**
     * Undo the last action.
     * @throws Exception an Exception
     */
    @FXML
    public void menuEditUndoAction() throws Exception{
        AppController.instance().undoRedo(true);
    }


    /**
     * Redo the last undone action.
     * @throws Exception an Exception
     */
    @FXML
    public void menuEditRedoAction() throws Exception{
        AppController.instance().undoRedo(false);
    }

    /**
     * About menu item action
     * Creates an About (our software) pop up
     */
    public void menuAboutAction(){
        Dialog about = new Dialog(WindowController.instance.basePane.getScene().getWindow(), "About Scrum Machine");
        try{
            Image softserve = new Image(getClass().getClassLoader().getResource("softServeLogo.png").openStream());
            Label logo = new Label();
            logo.setGraphic(new ImageView(softserve));
            about.setMasthead(logo);
        }catch (IOException e) { out.println(e); }

        Label details = new Label("" +
                "Scrum Machine\n" +
                "Version 0.6\n" +
                "© 2015 soft.serve Corporation - All rights Reserved.\n " +
                "\n" +
                "External Library Acknowledgement: \n \n" +
                "Controlsfx/OpenJFX Dialogs \nJens Deters FontAwesomeFX\n");
        Label license = new Label("License: \n" +
                "Copyright (c) 2014, Oracle and/or its affiliates. All rights reserved.\n" +
                "DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.\n" +
                "\n" +
                "This code is free software; you can redistribute it and/or modify it\n" +
                "under the terms of the GNU General Public License version 2 only, as\n" +
                "published by the Free Software Foundation.  Oracle designates this\n" +
                "particular file as subject to the \"Classpath\" exception as provided\n" +
                "by Oracle in the LICENSE file that accompanied this code.\n" +
                "\n" +
                "This code is distributed in the hope that it will be useful, but WITHOUT\n" +
                "ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or\n" +
                "FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License\n" +
                "version 2 for more details (a copy is included in the LICENSE file that\n" +
                "accompanied this code).\n" +
                "\n" +
                "You should have received a copy of the GNU General Public License version\n" +
                "2 along with this work; if not, write to the Free Software Foundation,\n" +
                "Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.\n" +
                "\n" +
                "Please contact Oracle, 500 Oracle Parkway, Redwood Shores, CA 94065 USA\n" +
                "or visit www.oracle.com if you need additional information or have any\n" +
                "questions.\n ");

        license.setStyle("-fx-font-size: 10px");
        GridPane content = new GridPane();
        content.add(details, 0, 0);
        content.add(license, 0, 1);
        about.setContent(content);
        about.show();
    }

    /**
     * Enables or disables undo and redo menu items depending on whether there
     * are any changes to undo or redo
     * @param undoMessage message to show for undo
     * @param redoMessage message to show for redo
     */
    protected void updateUndoRedo(CommandMessage undoMessage, CommandMessage redoMessage) {
        String undoString = "Undo";
        if (undoMessage == null) {
            undoItem.setDisable(true);
        }
        else {
            undoItem.setDisable(false);
            undoString += " " + undoMessage;
        }
        undoItem.setText(undoString);

        String redoString = "Redo";
        if (redoMessage == null) {
            redoItem.setDisable(true);
        }
        else {
            redoItem.setDisable(false);
            redoString += " " + redoMessage;
        }
        redoItem.setText(redoString);
    }

    /**
     * Shows the search pane
     */
    @FXML
    public void menuSearchAction()
    {
        WindowController.instance.showSearch();
    }
}
