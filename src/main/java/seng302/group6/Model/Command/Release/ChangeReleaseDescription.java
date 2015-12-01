package seng302.group6.Model.Command.Release;

import seng302.group6.Model.Command.Command;
import seng302.group6.Model.Command.CommandMessage;
import seng302.group6.Model.ItemClasses.Release;
import seng302.group6.Model.ItemType;

/**
 * Command class which allows undo/redo functionality for changing the description of a release
 * Created by Michael Wheeler on 16/05/15.
 */
public class ChangeReleaseDescription implements Command {

    private Release release;
    private String oldDescription;
    private String newDescription;

    /**
     * Create a ChangeDescription command
     * @param release the release being modified
     * @param newDescription the new description of the release
     */
    public ChangeReleaseDescription(Release release, String newDescription){
        this.release = release;
        this.newDescription = newDescription;
        this.oldDescription = release.getDescription();
    }

    /**
     * Undo the action, setting the description to the old description
     */
    @Override
    public void undo(){release.setDescription(oldDescription);}

    /**
     * Redo the action, setting the description to the new description
     */
    @Override
    public void redo(){release.setDescription(newDescription);}

    /**
     * Return a message indicating properties of the command
     * @return a message indicating properties of the command
     */
    @Override
    public CommandMessage getMessage() {
        CommandMessage message = new CommandMessage("Change", ItemType.RELEASE, release, "releaseDescription");

        return message;
    }

}
