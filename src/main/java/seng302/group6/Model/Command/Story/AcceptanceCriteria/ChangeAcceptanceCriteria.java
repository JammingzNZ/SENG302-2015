package seng302.group6.Model.Command.Story.AcceptanceCriteria;

import seng302.group6.Model.Command.Command;
import seng302.group6.Model.Command.CommandMessage;
import seng302.group6.Model.ItemClasses.AcceptanceCriteria;
import seng302.group6.Model.ItemClasses.Story;
import seng302.group6.Model.ItemType;

/**
 * Command class which allows undo/redo functionality for changing the AC of a story
 * Created by Josh on 1/05/2015.
 */
public class ChangeAcceptanceCriteria implements Command {

    private Story story;
    private AcceptanceCriteria ac;
    private String oldText;
    private String newText;

    /**
     * Create a ChangeAcceptanceCriteria command for changing the text of an AC
     * @param story Story that contains the AC
     * @param ac AC to be changed
     * @param text String to change the AC text to
     */
    public ChangeAcceptanceCriteria(Story story, AcceptanceCriteria ac, String text) {
        this.story = story;
        this.ac = ac;
        this.newText = text;

        this.oldText = ac.getText();
    }

    /**
     * Undo the action, setting the AC text to the old text
     */
    @Override
    public void undo() {
        ac.setText(oldText);
    }

    /**
     * Redo the action, setting the AC text to the new text
     */
    @Override
    public void redo() {
        ac.setText(newText);
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
