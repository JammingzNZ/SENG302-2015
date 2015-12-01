package seng302.group6.Model.Command.Sprint;

import seng302.group6.Model.Command.CommandMessage;
import seng302.group6.Model.Command.GroupCommand;
import seng302.group6.Model.ItemClasses.Sprint;
import seng302.group6.Model.ItemType;

import java.util.ArrayList;

/**
 * Command class which allows undo/redo functionality for unlocking a sprint
 * Created by Josh 13/08/15.
 */
public class UnlockSprint extends GroupCommand {

    private Sprint sprint;
    private ArrayList<String> stories;

    public UnlockSprint(Sprint sprint) {
        this.sprint = sprint;

        for (String story : sprint.getStories()) {
            addCommand(new RemoveStoryFromSprint(sprint, story));
        }
    }

    /**
     * Undo the action, taking the sprint out of set up mode
     */
    @Override
    public void undo() {
        sprint.setSettingUpOff();
        super.undo();
    }

    /**
     * Redo the action, putting the sprint in set up mode
     */
    @Override
    public void redo() {
        super.redo();
        sprint.setSettingUpOn();
    }

    /**
     * Return a message indicating properties of the command
     * @return a message indicating properties of the command
     */
    @Override
    public CommandMessage getMessage() {
        CommandMessage message = new CommandMessage("Change", ItemType.SPRINT, sprint, "lockButton");

        return message;
    }

}
