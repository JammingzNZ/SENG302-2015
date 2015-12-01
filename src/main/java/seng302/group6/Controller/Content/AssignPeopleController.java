package seng302.group6.Controller.Content;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import seng302.group6.Controller.AppController;
import seng302.group6.Controller.Window.WindowController;
import seng302.group6.Debug;
import seng302.group6.Model.Command.Story.Tasks.AddPersonToTask;
import seng302.group6.Model.Command.Story.Tasks.RemovePersonFromTask;
import seng302.group6.Model.Command.Story.Tasks.TaskTab;
import seng302.group6.Model.ItemClasses.*;

import java.util.ArrayList;

/**
 * Created by simon on 24/09/15.
 */
public class AssignPeopleController
{
    @FXML ListView<String> assignedPeopleList;
    @FXML ListView<String> unassignedPeopleList;
    @FXML Button doneButton;

    private Task task;
    private Sprint sprint;
    private Story story;
    private ScrumboardStoryController parent;
    private SprintFrameController sprintParent;
    private int taskTableIndex;

    public void setTask(Task task)
    {
        this.task = task;
    }

    public void setTaskTableIndex(int index){
        this.taskTableIndex = index;
    }

    public void setSprint(Sprint sprint)
    {
        this.sprint = sprint;
    }

    public void setStory(Story story)
    {
        this.story = story;
    }

    public void setParent(ScrumboardStoryController parent)
    {
        this.parent = parent;
    }

    public void setSprintParent(SprintFrameController parent){ this.sprintParent = parent; }

    public void update()
    {
        ArrayList<String> allPeopleUids = Workspace.getTeam(sprint.getAssociatedTeam()).getPeople();

        ArrayList<String> assignedPeopleUids = task.getPeople();
        ArrayList<String> unassingedPeopleUids = new ArrayList<>();

        for (String uid: allPeopleUids) {
            if (!assignedPeopleUids.contains(uid)) {
                unassingedPeopleUids.add(uid);
            }
        }

        assignedPeopleList.setItems(
                FXCollections.observableArrayList(Workspace.getPeopleNames(assignedPeopleUids))
        );
        unassignedPeopleList.setItems(
                FXCollections.observableArrayList(Workspace.getPeopleNames(unassingedPeopleUids))
        );

        if (parent != null) {
            parent.populateTaskTable();
        } else {
            ((SprintFrameController)WindowController.instance.selected_mpc).populateAllTasksTable();
        }
    }

    @FXML
    public void addPersonAction()
    {
        String movingPerson = unassignedPeopleList.getSelectionModel().getSelectedItem();
        if (movingPerson != null) {
            String uid = Workspace.getPersonID(movingPerson);
            if (uid != null) {
                if (parent != null) {
                    AppController.instance().addCommand(new AddPersonToTask(sprint, task, uid, story, TaskTab.SCRUM_BOARD));
                } else {
                    AppController.instance().addCommand(new AddPersonToTask(sprint, task, uid, story, TaskTab.ALL_TASKS));
                }
            }
        }
        update();
    }

    @FXML
    public void removePersonAction()
    {
        String movingPerson = assignedPeopleList.getSelectionModel().getSelectedItem();
        if (movingPerson != null) {
            String uid = Workspace.getPersonID(movingPerson);
            if (uid != null) {
                AppController.instance().addCommand(new RemovePersonFromTask(sprint, task, uid, story));
            }
        }
        update();
    }

    /**
     * Closes the dialog
     */
    @FXML
    public void doneButtonAction(){
        Stage dialog = (Stage) doneButton.getScene().getWindow();
        dialog.close();
        if(sprintParent != null) {
            sprintParent.setSelectedTask(taskTableIndex);
            sprintParent.highlight();
        }
    }
}
