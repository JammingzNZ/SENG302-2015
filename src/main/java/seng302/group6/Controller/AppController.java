package seng302.group6.Controller;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Tooltip;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.controlsfx.control.ButtonBar;
import org.controlsfx.control.action.Action;
import org.controlsfx.dialog.Dialog;
import org.controlsfx.dialog.DialogAction;
import org.controlsfx.dialog.Dialogs;
import seng302.group6.Controller.Content.EffortLoggingFrameController;
import seng302.group6.Controller.Content.SprintDialogController;
import seng302.group6.Controller.Content.SprintFrameController;
import seng302.group6.Controller.Window.SideDisplayListController;
import seng302.group6.Controller.Window.WindowController;
import seng302.group6.Model.Command.Command;
import seng302.group6.Model.Command.CommandMessage;
import seng302.group6.Model.ItemClasses.*;
import seng302.group6.Model.ItemType;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Optional;

/**
 * A singleton class for general app controlling code
 * Created by simon on 28/07/15.
 */
public class AppController
{
    private static AppController instance = null;

    private WindowController windowController;
    private SideDisplayListController sideDisplayListController;

    public static final String BRAND_PROJECT_FILE_DESC = "Soft Serve project files";

    /**
     * Returns the singleton instance of the class
     * @return the app controller
     */
    public static AppController instance()
    {
        if (instance == null) {
            instance = new AppController();
        }

        return instance;
    }

    /**
     * Private constructor that only gets called once. Creates references to
     * the window controller and side display list controller.
     */
    private AppController()
    {
        windowController = WindowController.instance;
        sideDisplayListController = WindowController.sideDisplayListController();
        setTooltipDisplayTime(50);
    }

    /**
     * Set how long it takes for tooltips to appear.
     * Taken from http://stackoverflow.com/questions/26854301/control-javafx-tooltip-delay
     * @param time Time in milliseconds
     */
    private void setTooltipDisplayTime(int time) {
        try {
            Tooltip tooltip = new Tooltip();
            Field fieldBehavior = tooltip.getClass().getDeclaredField("BEHAVIOR");
            fieldBehavior.setAccessible(true);
            Object objBehavior = fieldBehavior.get(tooltip);

            Field fieldTimer = objBehavior.getClass().getDeclaredField("activationTimer");
            fieldTimer.setAccessible(true);
            Timeline objTimer = (Timeline) fieldTimer.get(objBehavior);

            objTimer.getKeyFrames().clear();
            objTimer.getKeyFrames().add(new KeyFrame(new Duration(time)));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Displays appropriate message for unsaved changes.
     * @return true to continue action (exit program) or false to cancel
     */
    public boolean unsavedWarning(){
        if(!Workspace.getSaved()) {
            Action saveButton = new DialogAction("Save", ButtonBar.ButtonType.OTHER);
            Action dontSaveButton = new DialogAction("Don't Save", ButtonBar.ButtonType.OTHER);
            Action response = Dialogs.create()
                    .owner(windowController.basePane.getScene().getWindow())
                    .title("Warning")
                    .masthead("You have unsaved changes")
                    .actions(saveButton, dontSaveButton, Dialog.ACTION_CANCEL)
                    .showConfirm();
            if (response == saveButton) {
                saveWorkspace();
                return true;
            } else if (response == dontSaveButton) {
                return true;
            } else {
                return false;
            }
        } else {
            return true;
        }
    }

    /**
     * Attempts to save everything in the workspace
     */
    public void saveWorkspace() {
        String ext = Workspace.appdata.getWorkspaceFileExtension();
        WorkspaceFileChooser fileChooser = new WorkspaceFileChooser("Save Workspace As...", "save", "", BRAND_PROJECT_FILE_DESC, ext);
        File fileToSave = fileChooser.getFile();
        if (fileToSave != null) {
            try {
                Workspace.saveData(fileToSave);
            }
            catch(IOException e) {
                showErrorPopup("Error Saving Workspace", e.getMessage());
                return;
            }
        }
        windowController.updateSaved();
    }

    /**
     * Generic popup window for Errors
     * @param title String to set the title text to
     * @param text String to set the header text to
     */
    public void showErrorPopup(String title, String text)
    {

        Dialogs.create()
                .owner(windowController.basePane.getScene().getWindow())
                .title(title)
                .masthead(text)
                .showError();
    }

    /**
     * Generic popup window for information
     * @param title String to set the title text to
     * @param text String to set the header text to
     */
    public void showInfoPopup(String title, String text)
    {

        Dialogs.create()
                .owner(windowController.basePane.getScene().getWindow())
                .title(title)
                .masthead(text)
                .showInformation();
    }

    /**
     * Creates the pop up for deletion of a team. Also handles the logic of the options.
     * @param teamID uid of the team that is being deleted
     */
    public void deleteTeamPopup(String teamID){

        Action deleteTeam = new DialogAction("Team Only", ButtonBar.ButtonType.OTHER);
        Action deleteTeamAndMembers = new DialogAction("Team & Members", ButtonBar.ButtonType.OTHER);
        Action response = Dialogs.create()
                .owner(windowController.basePane.getScene().getWindow())
                .title("Delete Options")
                .masthead("How would you like to delete the team?")
                .actions(deleteTeam, deleteTeamAndMembers, Dialog.ACTION_CANCEL)
                .showWarning();

        if (response == deleteTeam){
            Workspace.deleteTeamOnly(teamID);
            windowController.clearWindow();
        } else if (response == deleteTeamAndMembers){
            Workspace.deleteTeamAndPeople(teamID);
            windowController.clearWindow();
        }
    }

    /**
     * Returns an array of people with the given skill
     * @param skill skill that people have
     * @return Array of people with given skill
     */
    public ArrayList<String> peopleWithSkill(String skill){
        ArrayList<String> skillHolders = new ArrayList<>();
        for(String person : Workspace.getPeople()){
            Person temp = Workspace.getPerson(person);
            if(temp.getSkills().contains(skill)){
                skillHolders.add(temp.getShortName());
            }
        }
        return skillHolders;
    }

    /**
     * Builds the string for the skill dialogue message
     * @param skillID skill the message is being built for
     * @return String message
     */
    public String buildSkillMessage(String skillID){
        ArrayList<String> skillHolders = peopleWithSkill(skillID);
        StringBuilder skillMessage = new StringBuilder();
        int numberOfSkillHolders = skillHolders.size();
        if(numberOfSkillHolders > 1) {
            // Adds all the names for every situation
            for (int i = 0; i < numberOfSkillHolders; i++) {
                Person temp = Workspace.getPerson(Workspace.getPersonID(skillHolders.get(i)));
                if (i == 0) {
                    skillMessage.append(temp.getShortName());
                } else if (i == numberOfSkillHolders - 1 && numberOfSkillHolders < 6) {
                    skillMessage.append(" and " + temp.getShortName());
                } else if (i < 5) {
                    skillMessage.append(", " + temp.getShortName());
                }
            }
            // Finishes message
            int leftOver = numberOfSkillHolders - 5;
            if (numberOfSkillHolders == 6) skillMessage.append(" and " + leftOver + " other currently have this skill. Delete anyway?");
            if (numberOfSkillHolders > 6) skillMessage.append(" and " + leftOver + " others currently have this skill. Delete anyway?");
            if (numberOfSkillHolders < 6) skillMessage.append(" currently have this skill. Delete anyway?");
        }else{
            skillMessage.append(skillHolders.get(0) + " currently has this skill. Delete anyway?");
        }
        return skillMessage.toString();
    }


    /**
     * Creates the pop up for deletion of a skill.
     * @param skillID uid of the skill being deleted
     */
    public void deleteSkillPopup(String skillID)
    {
        Skill skill = Workspace.getSkill(skillID);

        if (skill.getShortName().equals("Product Owner") || skill.getShortName().equals("Scrum Master")) {
            showErrorPopup("Cannot Delete", "This is a default skill. It can't be deleted");
        }
        else {
            String peopleString = buildSkillMessage(skillID); //The string of people who have the skill
            String deleteString = "You are deleting \"" + Workspace.getSkillName(skillID) + "\"";

            Action deleteSkill = new DialogAction("Delete this Skill", ButtonBar.ButtonType.OTHER);
            Action response = Dialogs.create()
                    .owner(windowController.basePane.getScene().getWindow())
                    .title("Warning")
                    .masthead(deleteString)
                    .message(peopleString)
                    .actions(deleteSkill, Dialog.ACTION_CANCEL)
                    .showWarning();
            if (response == deleteSkill) {
                Workspace.deleteSkill(skillID);
                windowController.clearWindow();
            }
        }
    }

    /**
     * Creates the pop up for deletion of a project.
     * @param projectID is the ID of the project that is being deleted.
     */
    public void deleteProjectPopup(String projectID)
    {
        String projectName = Workspace.getProjectName(projectID);
        String deleteString = "Are you sure you want to delete \"" + projectName + "\"?";
        String deleteMessage = "Any allocated teams and people will be deallocated from this Project. " +
                "All associated releases will be deleted.";
        Action deleteProject = new DialogAction("Delete", ButtonBar.ButtonType.OTHER);
        Action response = Dialogs.create()
                .owner(windowController.basePane.getScene().getWindow())
                .title("Warning")
                .masthead(deleteString)
                .message(deleteMessage)
                .actions(deleteProject, Dialog.ACTION_CANCEL)
                .showWarning();
        if (response == deleteProject){
            Workspace.deleteProject(projectID);
            windowController.clearWindow();
        }
    }

    /**
     * Shows a dialog informing the user that the can't delete the project
     * because it has associated sprints.
     * @param projectID The UID of the project
     */
    public void cannotDeleteProjectPopup(String projectID)
    {
        Project project = Workspace.getProject(projectID);
        cannotDeleteItemPopup(ItemType.PROJECT, project.associatedSprints());
    }

    /**
     * Shows a dialog informing the user that the can't delete the backlog
     * because it has associated sprints.
     * @param backlogID The UID of the backlog
     */
    public void cannotDeleteBacklogPopup(String backlogID)
    {
        Backlog backlog = Workspace.getBacklog(backlogID);
        cannotDeleteItemPopup(ItemType.BACKLOG, backlog.associatedSprints());
    }

    /**
     * Shows a dialog informing the user that the can't delete the release
     * because it has associated sprints.
     * @param releaseID The UID of the release
     */
    public void cannotDeleteReleasePopup(String releaseID)
    {
        Release release = Workspace.getRelease(releaseID);
        cannotDeleteItemPopup(ItemType.RELEASE, release.associatedSprints());
    }

    /**
     * Shows a dialog informing the user that the can't delete the team
     * because it has associated sprints.
     * @param teamID The UID of the team
     */
    public void cannotDeleteTeamPopup(String teamID)
    {
        Team team = Workspace.getTeam(teamID);
        cannotDeleteItemPopup(ItemType.TEAM, team.associatedSprints());
    }

    /**
     * Shows a dialog informing the user that the can't delete the story
     * because it has associated sprints.
     * @param storyID The UID of the story
     */
    public void cannotDeleteStoryPopup(String storyID)
    {
        Story story = Workspace.getStory(storyID);
        cannotDeleteItemPopup(ItemType.STORY, story.associatedSprints());
    }

    /**
     * Shows a dialog informing the user that the can't delete the item
     * because it has associated sprints.
     * @param itemType Type of item being deleted
     * @param sprints List of sprint IDs the item belongs to
     */
    public void cannotDeleteItemPopup(ItemType itemType, ArrayList<String> sprints) {
        String message = "You cannot delete this " + itemType.toString().toLowerCase() +
                " because it is associated with the following sprints:\n";

        for (String sprintID : sprints) {
            Sprint sprint = Workspace.getSprint(sprintID);
            message += "\n\t" + sprint.getShortName();
        }

        Dialogs.create()
                .owner(windowController.basePane.getScene().getWindow())
                .title("Cannot Delete " + itemType)
                .masthead("You must remove this " + itemType.toString().toLowerCase() +
                        " from associated sprints before deletion")
                .message(message)
                .actions(Dialog.ACTION_OK)
                .showError();
    }

    /**
     * Creates a new project and displays it in the main content area
     * @throws Exception an Exception
     */
    public void newProject() throws Exception
    {
        if (windowController.selected_mpc != null) {
            windowController.selected_mpc.forceTextInputCommand();
        }
        String uid = Workspace.createProject();
        windowController.setMainView(ItemType.PROJECT, Workspace.getProjectName(uid));
        sideDisplayListController.displayItem(ItemType.PROJECT);
        windowController.updateGUI();
        windowController.selected_mpc.doneEditAction();
        windowController.selected_mpc.addingNew();
    }

    /**
     * Creates a new person and displays it in the main content area
     * @throws Exception an Exception
     */
    public void newPerson() throws Exception
    {
        //setWindow("personFrame.fxml");
        if (windowController.selected_mpc != null) {
            windowController.selected_mpc.forceTextInputCommand();
        }
        String uid = Workspace.createPerson();
        windowController.setMainView(ItemType.PERSON, Workspace.getPersonName(uid));
        sideDisplayListController.displayItem(ItemType.PERSON);
        windowController.updateGUI();
        windowController.selected_mpc.doneEditAction();
        windowController.selected_mpc.addingNew();
    }

    /**
     * Creates a new skill and displays it in the main content area
     * @throws Exception an Exception
     */
    public void newSkill() throws Exception
    {
        //setWindow("skillFrame.fxml");
        if (windowController.selected_mpc != null) {
            windowController.selected_mpc.forceTextInputCommand();
        }
        String uid = Workspace.createSkill();
        windowController.setMainView(ItemType.SKILL, Workspace.getSkillName(uid));
        sideDisplayListController.displayItem(ItemType.SKILL);
        windowController.updateGUI();
        windowController.selected_mpc.doneEditAction();
        windowController.selected_mpc.addingNew();
    }

    /**
     * Creates a new team and displays it in the main content area
     * @throws Exception an Exception
     */
    public void newTeam() throws Exception
    {
        //setWindow("teamFrame.fxml");
        if (windowController.selected_mpc != null) {
            windowController.selected_mpc.forceTextInputCommand();
        }
        String uid = Workspace.createTeam();
        windowController.setMainView(ItemType.TEAM, Workspace.getTeamName(uid));
        sideDisplayListController.displayItem(ItemType.TEAM);
        windowController.updateGUI();
        windowController.selected_mpc.doneEditAction();
        windowController.selected_mpc.addingNew();
    }

    /**
     * Creates a new release and displays it in the main content area
     * @throws Exception an Exception
     */
    public void newRelease() throws Exception
    {
        if(Workspace.getCurrentProject() != null) {
            //setWindow("releaseFrame.fxml");
            if (windowController.selected_mpc != null) {
                windowController.selected_mpc.forceTextInputCommand();
            }
            String uid = Workspace.createRelease();
            windowController.setMainView(ItemType.RELEASE, Workspace.getReleaseName(uid));
            sideDisplayListController.displayItem(ItemType.RELEASE);
            windowController.updateGUI();
            windowController.selected_mpc.doneEditAction();
            windowController.selected_mpc.addingNew();
        }
        else {
            String title = "Error Creating Release";
            String error = "A project is required before a release can be created.";
            showErrorPopup(title, error);
        }
    }

    /**
     * Creates a new story and displays it in the main content area
     * @throws Exception an Exception
     */
    public void newStory() throws Exception
    {
        ArrayList<String> peopleNames = Workspace.getPeopleNames(Workspace.getPeople());
        Collections.sort(peopleNames);
        if (!peopleNames.isEmpty()) {
            // Get creator uid
            Optional<String> response = Dialogs.create()
                    .owner(windowController.basePane.getScene().getWindow())
                    .title("Select Creator")
                    .masthead("Select a creator for this story. This cannot be changed.")
                    .message("Choose creator:")
                    .showChoices(peopleNames);

            if (response.isPresent()) {
                if (windowController.selected_mpc != null) {
                    windowController.selected_mpc.forceTextInputCommand();
                }

                String uid = Workspace.createStory(Workspace.getPersonID(response.get()));
                windowController.setMainView(ItemType.STORY, Workspace.getStoryName(uid));
                sideDisplayListController.displayItem(ItemType.STORY);
                windowController.updateGUI();
                windowController.selected_mpc.doneEditAction();
                windowController.selected_mpc.addingNew();

            }
        } else {
            showErrorPopup("Error Creating Story", "There must be a person in the workspace" +
                    " to assign as the story creator.");
        }
    }

    /**
     * Creates a new backlog and displays it in the main content area
     * @throws Exception an Exception
     */
    public void newBacklog() throws Exception
    {
        if (windowController.selected_mpc != null) {
            windowController.selected_mpc.forceTextInputCommand();
        }
        String uid = Workspace.createBacklog();
        windowController.setMainView(ItemType.BACKLOG, Workspace.getBacklogName(uid));
        sideDisplayListController.displayItem(ItemType.BACKLOG);
        windowController.updateGUI();
        windowController.selected_mpc.doneEditAction();
        windowController.selected_mpc.addingNew();
    }

    /**
     * Creates a new sprint and displays it in the main content area
     * @throws Exception an Exception
     */
    public void newSprint() throws Exception
    {
        if (windowController.selected_mpc != null) {
            windowController.selected_mpc.forceTextInputCommand();
        }
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("sprintDialog.fxml"));
        Stage sprintDialog = new Stage();
        sprintDialog.setTitle("Select Sprint properties");
        sprintDialog.setScene(new Scene(loader.load(), 420, 285));
        sprintDialog.setResizable(false);
        sprintDialog.initModality(Modality.APPLICATION_MODAL);
        SprintDialogController sdc = loader.<SprintDialogController>getController();
        sdc.setWindowController(windowController);
        sdc.setContentController(windowController.selected_mpc);
        sprintDialog.show();
    }

    /**
     * Undo or redo an action, and update the GUI accordingly
     * @param doUndo Whether to undo (true) or redo (false)
     * @throws Exception an Exception
     */
    public void undoRedo(boolean doUndo) throws Exception{
        if (windowController.selected_mpc != null) {
            windowController.selected_mpc.forceTextInputCommand();
        }

        if (doUndo) {
            Workspace.undo();
        }
        else {
            Workspace.redo();
        }

        CommandMessage undoMessage;
        if (doUndo) {
            undoMessage = Workspace.getRedoMessage();
        }
        else {
            undoMessage = Workspace.getUndoMessage();
        }
        String undoAction = undoMessage.getCommandType();
        ItemType undoType = undoMessage.getItemType();
        String undoName = undoMessage.getItemName();

        sideDisplayListController.currentList = undoType;
        windowController.updateList(sideDisplayListController.currentList);

        if (undoAction == "Create") {
            if (doUndo) {
                windowController.clearWindow();
            }
            else {
                sideDisplayListController.displayList.getSelectionModel().select(undoName);
                windowController.setMainView(undoType, undoName);
                windowController.selected_mpc.doneEditAction();
                windowController.selected_mpc.addingNew();
            }
        } else if (undoAction == "Edit") {
            sideDisplayListController.displayList.getSelectionModel().select(undoName);
            windowController.setMainView(undoType, undoName);
        } else if (undoAction == "Delete") {
            if (doUndo) {
                sideDisplayListController.displayList.getSelectionModel().select(undoName);
                windowController.setMainView(undoType, undoName);
            }
            else {
                windowController.clearWindow();
            }
        }
        else {
            String fxID = undoMessage.getItemProperty();
            sideDisplayListController.displayList.getSelectionModel().select(undoName);
            Boolean addingNew = undoMessage.getAddingNew();
            String tab = undoMessage.getTab();
            windowController.setMainViewForChanges(undoType, undoName, undoMessage, addingNew);
        }

        windowController.updateGUI();
    }

    /**
     * Manually add an undoable/redoable command to the workspace's command manager.
     * @param command Command to add and execute
     */
    public void addCommand(Command command) {
        Workspace.addCommand(command);
        windowController.updateGUI();
    }
}
