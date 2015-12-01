package seng302.group6.Model.Command.Story;

import seng302.group6.Model.Command.Command;
import seng302.group6.Model.Command.CommandMessage;
import seng302.group6.Model.ItemClasses.Story;
import seng302.group6.Model.ItemClasses.Workspace;
import seng302.group6.Model.ItemType;

/**
 * Command for creating stories
 * Created by simon on 22/05/15.
 */
public class CreateStory implements Command
{
    private Story story;

    /**
     * Create a CreateStory command for adding/removing stories to the workspace
     * @param story story to be added to the workspace
     */
    public CreateStory(Story story)
    {
        super();

        this.story = story;
    }

    /**
     * Undo the action, removing the story from the workspace
     */
    @Override
    public void undo()
    {
        Workspace.removeStory(story.uid());
    }

    /**
     * Redo the action, adding the story to the workspace
     */
    @Override
    public void redo()
    {
        Workspace.addStory(story);
    }

    /**
     * Return a message indicating the properties of the command
     * @return a message indicating the properties of the command
     */
    @Override
    public CommandMessage getMessage() {
        CommandMessage message = new CommandMessage("Create", ItemType.STORY, story);

        return message;
    }
}
