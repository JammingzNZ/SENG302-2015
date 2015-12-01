package seng302.group6.Model.Command.Backlog;

import seng302.group6.Model.Command.CommandMessage;
import seng302.group6.Model.Command.GroupCommand;
import seng302.group6.Model.ItemClasses.Backlog;
import seng302.group6.Model.ItemClasses.Project;
import seng302.group6.Model.ItemClasses.Story;
import seng302.group6.Model.ItemClasses.Workspace;
import seng302.group6.Model.ItemType;

import java.util.HashSet;

/**
 * Command class which allows undo/redo functionality for deleting a backlog story.
 */
public class DeleteBacklog extends GroupCommand
{
    private Backlog backlog;

    /**
     * Removes the specified backlog.
     * Allows the backlog to be added / removed via undo / redo respectively.
     *
     * @param uid uid of a backlog that has been created.
     */
    public DeleteBacklog(String uid) {
        super();
        backlog = Workspace.getBacklog(uid);
        assert backlog != null;

        for (int i = 0; i < backlog.getStories().size(); i++)
        {
            String storyId = backlog.getStories().get(i);
            Story story = Workspace.getStory(storyId);
            this.addCommand(new RemoveBacklogStory(backlog, storyId, story.getCurrentPriority()));
        }
    }

    /**
     * Undo the action of delete backlog
     */
    public void undo() {

        Workspace.addBacklog(backlog);

        // Add backlog back to projects that used it
        HashSet<String> projects = backlog.getProjects();
        if (projects != null) {
            for (String project : projects) {
                Project proj = Workspace.getProject(project);
                proj.setBacklog(backlog.uid());
            }
        }
        // Makes it so that all the stories in the backlog are added
        super.undo();
    }

    /**
     * Redo the action of delete backlog
     */
    public void redo() {

        // Makes it so that all the stories in the backlog are removed
        super.redo();
        Workspace.removeBacklog(backlog.uid());

        // Remove backlog from projects
        HashSet<String> projects = backlog.getProjects();
        if (projects != null) {
            for (String project : projects) {
                Project proj = Workspace.getProject(project);
                proj.setBacklog(null);
            }
        }
    }

    /**
     * Return a message indicating properties of the command
     * @return a message indicating properties of the command
     */
    @Override
    public CommandMessage getMessage() {
        CommandMessage message = new CommandMessage("Delete", ItemType.BACKLOG, backlog);

        return message;
    }
}