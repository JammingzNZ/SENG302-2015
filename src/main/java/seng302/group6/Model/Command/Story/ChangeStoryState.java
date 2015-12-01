package seng302.group6.Model.Command.Story;

import seng302.group6.Model.Command.Command;
import seng302.group6.Model.Command.CommandMessage;
import seng302.group6.Model.ItemClasses.Sprint;
import seng302.group6.Model.ItemClasses.Story;
import seng302.group6.Model.ItemType;
import seng302.group6.Model.Status.StatusType;

import java.util.ArrayList;

/**
 * Command for changing state of a Story
 * Created by simon on 22/05/15.
 */
public class ChangeStoryState implements Command
{
    private Story story;
    private StatusType oldState;
    private StatusType newState;
    private Sprint sprint;

    /**
     * Creates a ChangeStoryState command for changing story state
     * @param story the story to change
     * @param newState the new state
     * @param sprint Sprint to use
     */
    public ChangeStoryState(Story story, StatusType newState, Sprint sprint)
    {
        this.story = story;
        this.newState = newState;
        this.oldState = story.getState();
        this.sprint = sprint;
    }

    /**
     * Undo change story state, sets state to old state
     */
    @Override
    public void undo()
    {
        story.setState(oldState);
    }

    /**
     * Redo change story state, sets state to new state
     */
    @Override
    public void redo()
    {
        story.setState(newState);
    }

    /**
     * Return a message indicating properties of the command
     * @return a message indicating properties of the command
     */
    @Override
    public CommandMessage getMessage() {
        CommandMessage message = new CommandMessage("Change", ItemType.SPRINT, sprint, "sbStoryStatusCombo");
        message.setTab("Scrum Board");

        ArrayList<Object> scl = new ArrayList<>();
        scl.add(story);
        message.setSpecialCaseList(scl);

        return message;
    }
}
