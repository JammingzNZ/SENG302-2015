package seng302.group6.Model.Command.Story.Dependencies;

import seng302.group6.Model.Command.Command;
import seng302.group6.Model.Command.CommandMessage;
import seng302.group6.Model.CyclicDependencyException;
import seng302.group6.Model.ItemClasses.Story;
import seng302.group6.Model.ItemType;

/**
 * Command class which allows undo/redo functionality for removing a dependency from a story
 * Created by Josh on 1/05/2015.
 */
public class RemoveDependency implements Command {

    private Story story;
    private String dependency;

    /**
     * Create a RemoveDependency command for removing a dependency from a story
     * @param story Story to remove the dependency from
     * @param dependency Story to remove from given story's dependency list
     */
    public RemoveDependency(Story story, String dependency) {
        this.story = story;
        this.dependency = dependency;
    }

    /**
     * Undo the action, adding the dependency
     */
    @Override
    public void undo() {
        try {
            story.addDependency(dependency);
        }
        catch (CyclicDependencyException e) {
            throw new IllegalArgumentException("Cyclic Dependency");
        }
    }

    /**
     * Redo the action, removing the dependency
     */
    @Override
    public void redo() {
        story.removeDependency(dependency);
    }

    /**
     * Return a message indicating properties of the command
     * @return a message indicating properties of the command
     */
    @Override
    public CommandMessage getMessage() {
        CommandMessage message = new CommandMessage("Change", ItemType.STORY, story, "availableStoriesList");
        message.setTab("Dependencies");

        return message;
    }
}
