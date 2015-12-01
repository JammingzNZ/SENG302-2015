package seng302.group6.Model.Command.Story;

import seng302.group6.Model.Command.Command;
import seng302.group6.Model.Command.CommandMessage;
import seng302.group6.Model.ItemClasses.Story;
import seng302.group6.Model.ItemType;

/**
 * Command class which allows undo/redo functionality for changing the readiness of a story
 * Created by Michael Wheeler on 7/07/15.
 */
public class ChangeStoryReadiness implements Command{

    private Story story;


    /**
     * Creates a ChangeReadiness command for changing the readiness state
     * @param story the story to change
     */
    public ChangeStoryReadiness(Story story)
    {
        this.story = story;
    }

    /**
     * Undo change story readiness change
     */
    @Override
    public void undo()
    {
        if(!story.getReadiness()){
            story.setReadinessToReady();
        } else {
            story.setReadinessToNotReady();
        }
    }

    /**
     * Redo change story name, sets long name to new long name
     */
    @Override
    public void redo()
    {
        if(!story.getReadiness()){
            story.setReadinessToReady();
        } else{
            story.setReadinessToNotReady();
        }
    }

    /**
     * Return a message indicating properties of the command
     * @return a message indicating properties of the command
     */
    @Override
    public CommandMessage getMessage() {
        CommandMessage message = new CommandMessage("Change", ItemType.STORY, story, "readyCheck");

        return message;
    }

}
