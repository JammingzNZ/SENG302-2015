package seng302.group6.Model.Command.Story.AcceptanceCriteria;

import seng302.group6.Model.Command.Command;
import seng302.group6.Model.Command.CommandMessage;
import seng302.group6.Model.ItemClasses.AcceptanceCriteria;
import seng302.group6.Model.ItemClasses.Story;
import seng302.group6.Model.ItemType;

/**
 * Command class which allows undo/redo functionality for adding an AC to a story
 * Created by Josh on 1/05/2015.
 */
public class AddAcceptanceCriteria implements Command {

    private Story story;
    private AcceptanceCriteria ac;

    /**
     * Create an AddAcceptanceCriteria command for adding an AC to a story
     * @param story Story to add the AC to
     * @param ac AC to be added
     */
    public AddAcceptanceCriteria(Story story, AcceptanceCriteria ac) {
        this.story = story;
        this.ac = ac;
    }

    /**
     * Undo the action, removing the AC
     */
    @Override
    public void undo() {
        story.deleteAcceptanceCriteria(ac);
    }

    /**
     * Redo the action, adding the AC
     */
    @Override
    public void redo() {
        story.addAcceptanceCriteria(ac);
    }

    /**
     * Return a message indicating properties of the command
     * @return a message indicating properties of the command
     */
    @Override
    public CommandMessage getMessage() {
        CommandMessage message = new CommandMessage("Change", ItemType.STORY, story, "storyACList");
        message.setTab("AC");

        return message;
    }
}
