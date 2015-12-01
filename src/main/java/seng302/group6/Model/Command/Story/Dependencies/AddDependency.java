package seng302.group6.Model.Command.Story.Dependencies;

import seng302.group6.Model.Command.Command;
import seng302.group6.Model.Command.CommandMessage;
import seng302.group6.Model.CyclicDependencyException;
import seng302.group6.Model.ItemClasses.Story;
import seng302.group6.Model.ItemType;

/**
 * Command class which allows undo/redo functionality for Adding a dependency to a story
 * Created by Josh on 1/05/2015.
 */
public class AddDependency implements Command {

    private Story story;
    private String dependency;

    /**
     * Create an AddDependency command for adding a dependency to a story
     * @param story Story to add the dependency to
     * @param dependency Story to make the given story dependent on
     */
    public AddDependency(Story story, String dependency) {
        this.story = story;
        this.dependency = dependency;
    }

    /**
     * Undo the action, removing the dependency
     */
    @Override
    public void undo() {
        story.removeDependency(dependency);
    }

    /**
     * Redo the action, adding the dependency
     */
    @Override
    public void redo() {
        try {
            story.addDependency(dependency);
        }
        catch (CyclicDependencyException e) {
            throw new IllegalArgumentException("Cyclic Dependency");
        }
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
