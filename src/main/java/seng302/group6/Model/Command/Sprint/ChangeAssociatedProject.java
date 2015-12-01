package seng302.group6.Model.Command.Sprint;

import seng302.group6.Model.Command.CommandMessage;
import seng302.group6.Model.Command.GroupCommand;
import seng302.group6.Model.ItemClasses.Sprint;
import seng302.group6.Model.ItemType;

/**
 * Command class which allows undo/redo functionality for changing the associated project of a sprint
 * Created by jaln on 29/07/15.
 */
public class ChangeAssociatedProject extends GroupCommand {

    private Sprint sprint;
    private String oldProject;
    private String newProject;

    /**
     * Create a ChangeAssociatedProject command for changing the Associated Project to a Sprint
     * @param sprint sprint that is having its project changed
     * @param newProject project uid that is being associated
     */
    public ChangeAssociatedProject(Sprint sprint, String newProject) {
        this.sprint = sprint;
        this.newProject = newProject;
        this.oldProject = sprint.getAssociatedProject();

        addCommand(new ChangeAssociatedTeam(this.sprint, null));
        addCommand(new ChangeAssociatedRelease(this.sprint, null));
    }

    /**
     * Undo the action, setting the Associated Project to the new Project
     */
    @Override
    public void undo() {
        sprint.setAssociatedProject(oldProject);
        super.undo();
    }

    /**
     * Redo the action, setting the Associated Project to the new Project
     */
    @Override
    public void redo() {
        super.redo();
        sprint.setAssociatedProject(newProject);
    }

    /**
     * Return a message indicating properties of the command
     * @return a message indicating properties of the command
     */
    @Override
    public CommandMessage getMessage() {
        CommandMessage message = new CommandMessage("Change", ItemType.SPRINT, sprint, "sprintAssociatedProject");

        return message;
    }
}