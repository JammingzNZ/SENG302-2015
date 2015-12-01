package seng302.group6.Model.Command.Release;

import seng302.group6.Model.Command.Command;
import seng302.group6.Model.Command.CommandMessage;
import seng302.group6.Model.ItemClasses.Release;
import seng302.group6.Model.ItemType;

/**
 * Command class which allows undo/redo functionality for changing the Release date of a release
 * Created by Michael Wheeler on 16/05/15.
 */
public class ChangeReleaseDate implements Command{
    private Release release;
    private String oldReleaseDate;
    private String newReleaseDate;

    public ChangeReleaseDate(Release release, String newReleaseDate){
        this.release = release;
        this.oldReleaseDate = release.getReleaseDate();
        this.newReleaseDate = newReleaseDate;
    }

    /**
     * Undo the action, setting the estimated release date to the old estimated release date
     */
    @Override
    public void undo(){ release.setReleaseDate(oldReleaseDate);}

    /**
     * Redo the action, setting the estimated release date to the new estimated release date
     */
    @Override
    public void redo() { release.setReleaseDate(newReleaseDate);}

    /**
     * Return a message indicating properties of the command
     * @return a message indicating properties of the command
     */
    @Override
    public CommandMessage getMessage() {
        CommandMessage message = new CommandMessage("Change", ItemType.RELEASE, release, "releaseEstimatedDate");

        return message;
    }

}
