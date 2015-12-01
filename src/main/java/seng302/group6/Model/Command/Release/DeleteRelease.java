package seng302.group6.Model.Command.Release;

import seng302.group6.Model.Command.Command;
import seng302.group6.Model.Command.CommandMessage;
import seng302.group6.Model.ItemClasses.Release;
import seng302.group6.Model.ItemClasses.Workspace;
import seng302.group6.Model.ItemType;

/**
 * Command class which allows undo/redo functionality for deleting a release
 * Created by jaln on 12/05/15.
 */
public class DeleteRelease implements Command
{
    private Release release;

    /**
     * Constructor for the Command class of DeleteRelease
     * @param release the release that is being deleted
     */
    public DeleteRelease(Release release) {
        super();

        this.release = release;
    }


    /**
     * Undo the action of delete release
     */
    public void undo() {
        Workspace.addRelease(release);
        Workspace.getProject(release.getAssociatedProject()).addRelease(release.uid());
    }


    /**
     * Redo the action of delete person
     */
    public void redo() {
        Workspace.removeRelease(release.uid());
        Workspace.getProject(release.getAssociatedProject()).removeRelease(release.uid());
    }

    /**
     * Return a message indicating properties of the command
     * @return a message indicating properties of the command
     */
    @Override
    public CommandMessage getMessage() {
        CommandMessage message = new CommandMessage("Delete", ItemType.RELEASE, release);

        return message;
    }
}