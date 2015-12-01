package seng302.group6.Model.Command.Story.Tasks;

import seng302.group6.Model.Command.Command;
import seng302.group6.Model.Command.CommandMessage;
import seng302.group6.Model.ItemClasses.Sprint;
import seng302.group6.Model.ItemClasses.Story;
import seng302.group6.Model.ItemClasses.Task;
import seng302.group6.Model.ItemType;

import java.util.ArrayList;

/**
 * Command class which allows undo/redo functionality for removing a person from a task in a story
 * Created by Michael Wheeler on 2/08/15.
 */
public class RemovePersonFromTask implements Command{

    private Task task;
    private String person;
    private Sprint sprint;
    private int oldIndex;
    private Story story;

    /**
     * Create an removePersonFromTask command for removing a person from a task
     * @param sprint sprint to use - (Since person allocation is done in Sprint Frame / Scrumboard)
     * @param task Task to remove person from
     * @param person Person to be removed
     * @param story Story
     */
    public RemovePersonFromTask(Sprint sprint, Task task, String person, Story story) {
        this.sprint = sprint;
        this.task = task;
        this.person = person;
        this.oldIndex = task.getPeople().indexOf(person);
        this.story = story;
    }

    /**
     * Undo the action, adding the person
     */
    @Override
    public void undo() {

        task.addPerson(person);
        task.moveTask(oldIndex, person);
    }

    /**
     * Redo the action, removing the person
     */
    @Override
    public void redo() {
        task.removePerson(person);
    }

    /**
     * Return a message indicating properties of the command
     * @return a message indicating properties of the command
     * TODO find the name of the GUI element
     */
    @Override
    public CommandMessage getMessage() {
        CommandMessage message = new CommandMessage("Change", ItemType.SPRINT, sprint, "taskTable");
        message.setTab("Scrum Board");

        ArrayList<Object> scl = new ArrayList<>();
        scl.add(story);
        scl.add(task);
        message.setSpecialCaseList(scl);

        return message;
    }
}
