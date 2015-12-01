package seng302.group6.Model.Command.Story.Tasks;

import seng302.group6.Model.Command.Command;
import seng302.group6.Model.Command.CommandMessage;
import seng302.group6.Model.ItemClasses.Effort;
import seng302.group6.Model.ItemClasses.EffortLeft;
import seng302.group6.Model.ItemClasses.Sprint;
import seng302.group6.Model.ItemClasses.Task;
import seng302.group6.Model.ItemType;
import seng302.group6.Model.Status.StatusType;

import java.util.ArrayList;

/**
 * Command class which allows undo/redo functionality for clearing a tasks properties in a story
 * Created by Josh on 15/08/15.
 */
public class ClearTask implements Command{

    private Sprint sprint;
    private Task task;

    private StatusType status;
    private ArrayList<String> people;
    private ArrayList<Effort> effortList;
    private ArrayList<EffortLeft> effortLeftList;
    private Double effortLeft;

    /**
     * Create a ClearTask command for clearing a Task's properties
     * @param sprint Sprint the task was in
     * @param task Task to be cleared
     */
    public ClearTask(Sprint sprint, Task task) {
        this.sprint = sprint;
        this.task = task;

        this.status = task.getStatus();
        this.people = task.getPeople();
        this.effortList = (ArrayList) task.getEffortList().clone();
        this.effortLeftList = (ArrayList) task.getEffortLeftList().clone();
        this.effortLeft = task.getEffortLeft();
    }

    /**
     * Undo the action, removing the Task
     */
    @Override
    public void undo() {
        task.setStatus(status);
        task.setPeople(people);

        for (Effort effort : effortList) {
            task.addEffort(effort);
        }

        for (EffortLeft effortLeft : effortLeftList) {
            task.addEffortLeft(effortLeft);
        }

        task.setEffortLeft(effortLeft);
    }

    /**
     * Redo the action, adding the Task
     */
    @Override
    public void redo() {
        task.setStatus(StatusType.NOT_STARTED);
        task.setPeople(new ArrayList<>());
        task.clearEffort();
    }

    /**
     * Return a message indicating properties of the command
     * @return a message indicating properties of the command
     */
    @Override
    public CommandMessage getMessage() {
        CommandMessage message = new CommandMessage("Change", ItemType.SPRINT, sprint, "");

        return message;
    }
}
