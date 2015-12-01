package seng302.group6.Model.Command.Story.AcceptanceCriteria;

import seng302.group6.Model.Command.Command;
import seng302.group6.Model.Command.CommandMessage;
import seng302.group6.Model.ItemClasses.AcceptanceCriteria;
import seng302.group6.Model.ItemClasses.Story;
import seng302.group6.Model.ItemType;

/**
 * Command class which allows undo/redo functionality for moving an AC in a story. (CHangign the priority)
 * Created by Josh on 1/05/2015.
 */
public class MoveAcceptanceCriteria implements Command {

    private Story story;
    private AcceptanceCriteria ac;
    private int oldIndex;
    private int newIndex;

    /**
     * Create a MoveAcceptanceCriteria command for moving an AC to a different place in a story's AC list
     * @param story Story to move the AC within
     * @param ac AC to be moved
     * @param index Place to move the AC to
     */
    public MoveAcceptanceCriteria(Story story, AcceptanceCriteria ac, int index) {
        this.story = story;
        this.ac = ac;
        this.newIndex = index;

        this.oldIndex = story.getAllAcceptanceCriteria().indexOf(ac);
    }

    /**
     * Undo the action, removing the AC
     */
    @Override
    public void undo() {
        story.moveAcceptanceCriteria(oldIndex, ac);
    }

    /**
     * Redo the action, adding the AC
     */
    @Override
    public void redo() {
        story.moveAcceptanceCriteria(newIndex, ac);
    }

    /**
     * Return a message indicating properties of the command
     * @return a message indicating properties of the command
     * TODO find the name of the GUI element
     */
    @Override
    public CommandMessage getMessage() {
        CommandMessage message = new CommandMessage("Change", ItemType.STORY, story, "storyACList");
        message.setTab("AC");

        return message;
    }
}
