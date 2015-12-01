package seng302.group6.Model.Command.Story.AcceptanceCriteria;

import seng302.group6.Model.Command.Command;
import seng302.group6.Model.Command.CommandMessage;
import seng302.group6.Model.ItemClasses.AcceptanceCriteria;
import seng302.group6.Model.ItemClasses.Story;
import seng302.group6.Model.ItemType;

/**
 * Command class which allows undo/redo functionality for removing an AC in a story
 * Created by Josh on 1/05/2015.
 */
public class RemoveAcceptanceCriteria implements Command {

    private Story story;
    private AcceptanceCriteria ac;
    private int oldIndex;
    private Integer oldEstimate;
    private Boolean ready;

    /**
     * Create an AddAcceptanceCriteria command for removing an AC from a story
     * @param story Story to remove the AC from
     * @param ac AC to be removed
     */
    public RemoveAcceptanceCriteria(Story story, AcceptanceCriteria ac) {
        this.story = story;
        this.ac = ac;
        this.oldIndex = story.getAllAcceptanceCriteria().indexOf(ac);
        this.oldEstimate = story.getEstimate();
        this.ready = story.getReadiness();
    }

    /**
     * Undo the action, adding the AC
     */
    @Override
    public void undo() {
        story.addAcceptanceCriteria(ac);
        story.moveAcceptanceCriteria(oldIndex, ac);
        story.setEstimate(oldEstimate);

        if (ready) {
            story.setReadinessToReady();
        }
    }

    /**
     * Redo the action, removing the AC
     */
    @Override
    public void redo() {
        story.deleteAcceptanceCriteria(ac);
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
