package seng302.group6.Model.Command.Backlog;

import seng302.group6.Model.Command.Command;
import seng302.group6.Model.Command.CommandMessage;
import seng302.group6.Model.EstimationScale.ScaleType;
import seng302.group6.Model.ItemClasses.Backlog;
import seng302.group6.Model.ItemType;

/**
 * Command class which allows undo/redo functionality for changing a backlog scale
 * Created by josh on 7/07/15.
 */
public class ChangeBacklogScale implements Command
{
    private Backlog backlog;
    private ScaleType oldScale;
    private ScaleType newScale;

    /**
     * Creates a ChangeBacklogName command for changing backlog scale type
     * @param backlog the backlog to change
     * @param newScale the new scale
     */
    public ChangeBacklogScale(Backlog backlog, ScaleType newScale)
    {
        this.backlog = backlog;
        this.newScale = newScale;
        this.oldScale = backlog.getScaleType();
    }

    /**
     * Undo change backlog scale type, sets scale type to old scale type
     */
    @Override
    public void undo()
    {
        backlog.setScaleType(oldScale);
    }

    /**
     * Redo change backlog scale type, sets scale type to new scale type
     */
    @Override
    public void redo()
    {
        backlog.setScaleType(newScale);
    }

    /**
     * Return a message indicating properties of the command
     * @return a message indicating properties of the command
     */
    @Override
    public CommandMessage getMessage() {
        CommandMessage message = new CommandMessage("Change", ItemType.BACKLOG, backlog, "scaleCombo");
        message.setTab("Stories");

        return message;
    }
}
