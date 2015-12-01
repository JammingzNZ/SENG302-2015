package seng302.group6.Model.Command.Release;

import seng302.group6.Model.Command.Command;
import seng302.group6.Model.Command.CommandMessage;
import seng302.group6.Model.ItemClasses.Release;
import seng302.group6.Model.ItemType;

/**
 * Command class which allows undo/redo functionality for changing the assocaiated project to a release
 * Created by Michael Wheeler on 16/05/15.
 */
public class ChangeAssociatedProject implements Command {
    Release release;
    String oldAssociatedProject;
    String newAssociatedProject;

    /**
     * Constructor for the command class of ChangeAssocaitedProject
     * @param release The Release
     * @param newAssociatedProject the new associated project of the release
     */
    public ChangeAssociatedProject(Release release, String newAssociatedProject){
        this.release = release;
        this.oldAssociatedProject = release.getAssociatedProject();
        this.newAssociatedProject = newAssociatedProject;
    }

    /**
     * Undo the action, setting the associated project to the old associated project
     */
    @Override
    public void undo(){ release.setAssociatedProject(oldAssociatedProject);}

    /**
     * Redo the action, setting the associated project to the new associated project
     */
    @Override
    public void redo() { release.setAssociatedProject(newAssociatedProject);}

    /**
     * Return a message indicating properties of the command
     * @return a message indicating properties of the command
     */
    @Override
    public CommandMessage getMessage() {
        CommandMessage message = new CommandMessage("Change", ItemType.RELEASE, release, "releaseProjectComboBox");

        return message;
    }
}
