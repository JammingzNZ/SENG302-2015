package seng302.group6.Model.Command.Story;

import seng302.group6.Model.Command.Backlog.RemoveBacklogStory;
import seng302.group6.Model.Command.CommandMessage;
import seng302.group6.Model.Command.GroupCommand;
import seng302.group6.Model.Command.Story.Dependencies.RemoveDependency;
import seng302.group6.Model.ItemClasses.Story;
import seng302.group6.Model.ItemClasses.Workspace;
import seng302.group6.Model.ItemType;

/**
 * Command for removing stories from the workspace
 * Created by simon on 22/05/15.
 */
public class DeleteStory extends GroupCommand
{
    private Story story;
    private String backlog;

    /**
     * Creates a DeleteStory command for removing a story from the workspace
     * and adding it back
     * @param story story to be deleted
     */
    public DeleteStory(Story story)
    {
        super();

        this.story = story;
        this.backlog = story.getBacklog();

        if (backlog != null) {
            this.addCommand(new RemoveBacklogStory(
                    Workspace.getBacklog(backlog),
                    story.uid(),
                    story.getCurrentPriority()
            ));
        }
        for (String dependency: story.getDependencies()) {
            this.addCommand(new RemoveDependency(story, dependency));
        }
        for (String dependent: story.getDependents()) {
            this.addCommand(new RemoveDependency(Workspace.getStory(dependent), story.uid()));
        }
    }

    /**
     * Undo the delete story action, add the story back to the workspace
     */
    @Override
    public void undo()
    {
        Workspace.addStory(story);
        super.undo();
        /*if (backlog != null) {
            Workspace.getBacklog(backlog).addStory(story.uid(), story.);
        }*/
    }

    /**
     * Redo the delete story action, removing the story from the workspace
     */
    @Override
    public void redo()
    {
        /*if (backlog != null) {
            Workspace.getBacklog(backlog).removeStory(story.uid());
        }*/
        super.redo();
        Workspace.removeStory(story.uid());
    }

    /**
     * Return a message indicating the properties of the command
     * @return Return a message indicating the properties of the command
     */
    @Override
    public CommandMessage getMessage() {
        CommandMessage message = new CommandMessage("Delete", ItemType.STORY, story);

        return message;
    }
}
