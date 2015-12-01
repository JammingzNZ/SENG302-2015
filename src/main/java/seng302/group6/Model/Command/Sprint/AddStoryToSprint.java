package seng302.group6.Model.Command.Sprint;

import seng302.group6.Model.Command.Command;
import seng302.group6.Model.Command.CommandMessage;
import seng302.group6.Model.Command.GroupCommand;
import seng302.group6.Model.Command.Story.Tasks.ChangeTaskEffortLeft;
import seng302.group6.Model.Command.Story.Tasks.TaskTab;
import seng302.group6.Model.ItemClasses.*;
import seng302.group6.Model.ItemType;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * Command class which allows undo/redo functionality for adding a story to a sprint
 * Created by jaln on 29/07/15.
 */
public class AddStoryToSprint extends GroupCommand {

    private Sprint sprint;
    private String storyUID;

    /**
     * Constructor for the command class of AddStroyToSprint
     * @param sprint The sprint
     * @param storyUID The story being added
     */
    public AddStoryToSprint(Sprint sprint, String storyUID) {
        this.sprint = sprint;
        this.storyUID = storyUID;

        Story story = Workspace.getStory(storyUID);
        for (Task task : story.getAllTasks()) {
            addCommand(new ChangeTaskEffortLeft(story, task, task.getEstimation(), sprint,
                    new EffortLeft(
                            LocalDateTime.of(LocalDate.parse(sprint.getStartDate()),
                                    LocalTime.of(23, 59, 59)),
                            task.getEstimation()
                    ),
                    TaskTab.SCRUM_BOARD
            ));
        }
    }

    /**
     * Undo the action, removing the Story to the Sprint
     */
    @Override
    public void undo() {
        super.undo();
        sprint.removeStory(storyUID);
    }

    /**
     * Redo the action, adding the Story to the Sprint
     */
    @Override
    public void redo() {
        sprint.addStory(storyUID);
        super.redo();
    }

    /**
     * Return a message indicating properties of the command
     * @return a message indicating properties of the command
     */
    @Override
    public CommandMessage getMessage() {
        CommandMessage message = new CommandMessage("Change", ItemType.SPRINT, sprint, "sprintStories");
        message.setTab("Stories");

        return message;
    }

}
