package seng302.group6.Model.Command.Sprint;

import seng302.group6.Model.Command.Command;
import seng302.group6.Model.Command.CommandMessage;
import seng302.group6.Model.ItemClasses.Sprint;
import seng302.group6.Model.ItemType;

/**
 * Command class which allows undo/redo functionality for changing the associated backlog
 * Created by jaln on 29/07/15.
 */
public class ChangeAssociatedBacklog implements Command {

    private Sprint sprint;
    private String oldBacklog;
    private String newBacklog;

    /**
     * Create a ChangeAssociatedBacklog command for changing the Associated Backlog to a Sprint
     * @param sprint Sprint that is getting the backlog associated to it
     * @param newBacklog uid of the backlog being associated
     */
    public ChangeAssociatedBacklog(Sprint sprint, String newBacklog) {
        this.sprint = sprint;
        this.newBacklog = newBacklog;
        this.oldBacklog = sprint.getAssociatedBacklog();
    }

    /**
     * Undo the action, setting the Associated Backlog to the new Backlog
     */
    @Override
    public void undo() {
        sprint.setAssociatedBacklog(oldBacklog);
    }

    /**
     * Redo the action, setting the Associated Backlog to the new Backlog
     */
    @Override
    public void redo() {
        sprint.setAssociatedBacklog(newBacklog);
    }

    /**
     * Return a message indicating properties of the command
     * @return a message indicating properties of the command
     */
    @Override
    public CommandMessage getMessage() {
        CommandMessage message = new CommandMessage("Change", ItemType.SPRINT, sprint, "sprintAssociatedBacklog");

        return message;
    }

}
