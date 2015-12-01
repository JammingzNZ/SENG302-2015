package seng302.group6.Model.Command.Story.Tasks;

import seng302.group6.Model.Command.Command;
import seng302.group6.Model.Command.CommandMessage;
import seng302.group6.Model.ItemClasses.Sprint;
import seng302.group6.Model.ItemClasses.Story;
import seng302.group6.Model.ItemClasses.Task;
import seng302.group6.Model.ItemType;

import java.util.ArrayList;

/**
 * Command class which allows undo/redo functionality for adding a person to a task in a story
 *
 * Created by Michael Wheeler on 2/08/15.
 */
public class AddPersonToTask implements Command {

    private Task task;
    private String person;
    private Sprint sprint;
    private Story story;
    private TaskTab tab;

    /**
     * Create an addPersonToTask command for adding an person to a task
     * @param sprint sprint to use - (Since person allocation is done in Sprint Frame / Scrumboard)
     * @param task Task to add person to
     * @param person Person to be added
     * @param story Story
     * @param tab tab to go back to
     */
    public AddPersonToTask(Sprint sprint, Task task, String person, Story story, TaskTab tab) {
        this.sprint = sprint;
        this.task = task;
        this.person = person;
        this.story = story;
        this.tab = tab;
    }

    /**
     * Undo the action, removing the person
     */
    @Override
    public void undo() {
        task.removePerson(person);
    }

    /**
     * Redo the action, adding the person
     */
    @Override
    public void redo() {
        task.addPerson(person);
    }

    /**
     * Return a message indicating properties of the command
     * @return a message indicating properties of the command
     * TODO find the name of the GUI element
     */
    @Override
    public CommandMessage getMessage() {
        CommandMessage message = new CommandMessage("Change", ItemType.SPRINT, sprint, "taskTable");

        if (tab == TaskTab.SCRUM_BOARD) {
            message.setTab("Scrum Board");
        } else {
            message.setTab("All Tasks");
        }

        ArrayList<Object> scl = new ArrayList<>();
        scl.add(story);
        scl.add(task);
        message.setSpecialCaseList(scl);

        return message;
    }
}
