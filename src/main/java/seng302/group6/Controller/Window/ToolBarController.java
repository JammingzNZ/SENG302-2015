package seng302.group6.Controller.Window;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.layout.GridPane;
import org.controlsfx.control.PopOver;
import seng302.group6.Controller.AppController;
import seng302.group6.Model.Command.CommandMessage;


/**
 * Controller for the Toolbar of the app
 * Created by Michael Wheeler on 21/07/15.
 */
public class ToolBarController {

    @FXML Button newItemButton;
    @FXML private Button undoButton;
    @FXML private Button redoButton;
    @FXML private Button saveButton;

    private PopOver newItem = new PopOver();

    MenuBarController menuBar = WindowController.menuBarController();
    WindowController windowController = WindowController.instance;

    /**
     * Intializes the Toolbar
     */
    @FXML
    public void initialize(){

        GridPane content = new GridPane();

        Button person = createGlyphButton("Person", "USER");
        Button skill = createGlyphButton("Skill", "CODE");
        Button story = createGlyphButton("Story", "BOOK");
        Button team = createGlyphButton("Team", "USERS");
        Button backlog = createGlyphButton("Backlog", "TASKS");
        Button project = createGlyphButton("Project", "COGS");
        Button release = createGlyphButton("Release", "STAR");
        Button sprint = createGlyphButton("Sprint", "FLAG");

        person.setPrefSize(72.0, 72.0);
        skill.setPrefSize(72.0, 72.0);
        story.setPrefSize(72.0, 72.0);
        team.setPrefSize(72.0, 72.0);
        backlog.setPrefSize(72.0, 72.0);
        project.setPrefSize(72.0, 72.0);
        release.setPrefSize(72.0, 72.0);
        sprint.setPrefSize(72.0, 72.0);


        person.setOnAction(e -> {
            try {
                AppController.instance().newPerson();
                newItem.hide();
            }catch(Exception ex){
                System.out.println(ex);
            }
        });

        skill.setOnAction(e -> {
            try {
                AppController.instance().newSkill();
                newItem.hide();
            }catch(Exception ex){
                System.out.println(ex);
            }
        });

        story.setOnAction(e -> {
            try {
                AppController.instance().newStory();
                newItem.hide();
            } catch (Exception ex) {
                System.out.println(ex);
            }
        });

        team.setOnAction(e -> {
            try {
                AppController.instance().newTeam();
                newItem.hide();
            }catch(Exception ex){
                System.out.println(ex);
            }
        });

        backlog.setOnAction(e -> {
            try {
                AppController.instance().newBacklog();
                newItem.hide();
            }catch(Exception ex){
                System.out.println(ex);
            }
        });

        project.setOnAction(e -> {
            try {
                AppController.instance().newProject();
                newItem.hide();
            }catch(Exception ex){
                System.out.println(ex);
            }
        });

        release.setOnAction(e -> {
            try {
                AppController.instance().newRelease();
                newItem.hide();
            }catch(Exception ex){
                System.out.println(ex);
            }
        });

        sprint.setOnAction(e -> {
            try {
                AppController.instance().newSprint();
                newItem.hide();
            }catch(Exception ex){
                System.out.println(ex);
            }
        });

        content.add(project,0,0);
        content.add(person,1,0);
        content.add(skill,2,0);
        content.add(team,0,1);
        content.add(release,1,1);
        content.add(story,2,1);
        content.add(backlog,0,2);
        content.add(sprint,1,2);

        newItem.setDetachable(false);
        newItem.setArrowLocation(PopOver.ArrowLocation.TOP_LEFT);
        newItem.setContentNode(content);

    }

    /**
     * Creates a new workspace
     */
    @FXML
    public void newWorkspaceAction(){
        hidePopover();
        try {
            menuBar.menuNewWorkspaceAction();
        }
        catch (Exception e) {}
    }

    /**
     * Creates a popup so a item can be selected to create a new item of that type.
     * If the popup is showing then hide the item
     */
    @FXML
    public void newItemAction(){
        if(!newItem.isShowing()) {
            newItem.show(newItemButton);
        }else{
            newItem.hide();
        }
    }

    /**
     * Performs an undo action
     */
    @FXML
    public void undoAction(){
        hidePopover();
        try {
            menuBar.menuEditUndoAction();
        }
        catch (Exception e) {}
    }

    /**
     * Performs a redo action
     */
    @FXML
    public void redoAction(){
        hidePopover();
        try {
            menuBar.menuEditRedoAction();
        }
        catch (Exception e) {}
    }

    /**
     * Performs the revert action
     */
    @FXML
    public void revertAction() {
        hidePopover();
        menuBar.menuRevertAction();
    }

    /**
     * Performs the save action
     */
    @FXML
    public void saveAction(){
        hidePopover();
        menuBar.menuSaveAction();
    }

    /**
     * Performs the load action
     */
    @FXML
    public void loadAction(){
        hidePopover();
        try {
            menuBar.menuLoadAction();
        }
        catch (Exception e) {}
    }

    /**
     * Performs the export action
     */
    @FXML
    public void exportAction(){
        hidePopover();
        try {
            menuBar.menuExportStatusReport();
        }
        catch (Exception e) {}
    }

    /**
     * Performs the search action
     */
    @FXML
    public void searchAction()
    {
        hidePopover();
        windowController.showSearch();
    }

    /**
     * Creates the Create Glyph Buttons
     * @param text the text the button should have
     * @param glyphName the name of the glyph
     * @return The Glyph Button
     */
    private Button createGlyphButton(String text, String glyphName){
        FontAwesomeIcon glyph = new FontAwesomeIcon();
        glyph.setIconName(glyphName);
        glyph.setSize("3em");
        Button button = new Button(text, glyph);
        button.setContentDisplay(ContentDisplay.TOP);
        return button;
    }

    /**
     * Enables or disables undo and redo menu items depending on whether there
     * are any changes to undo or redo
     * @param undoMessage The command message from the current undo command (null if none)
     * @param redoMessage The command message from the current redo command (null if none)
     */
    public void updateUndoRedo(CommandMessage undoMessage, CommandMessage redoMessage) {
        undoButton.setDisable(undoMessage == null);
        redoButton.setDisable(redoMessage == null);
    }

    /**
     * Enables or disabled the save button based on whether there are saved changes or not
     * @param saved Whether there are any saved changes (true = no changes)
     */
    public void updateSaved(Boolean saved) {
        saveButton.setDisable(saved);
    }

    /**
     * Hides the new item popover
     */
    public void hidePopover()
    {
        newItem.hide();
    }

    /**
     * Handles a mouse click on the toolbar.
     */
    @FXML
    public void handleMouseClick()
    {
        hidePopover();
    }
}
