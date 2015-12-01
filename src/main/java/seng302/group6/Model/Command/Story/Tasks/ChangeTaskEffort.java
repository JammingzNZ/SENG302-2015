//package seng302.group6.Model.Command.Story.Tasks;
//
//import seng302.group6.Model.Command.Command;
//import seng302.group6.Model.Command.CommandMessage;
//import seng302.group6.Model.ItemClasses.Sprint;
//import seng302.group6.Model.ItemClasses.Story;
//import seng302.group6.Model.ItemClasses.Task;
//import seng302.group6.Model.ItemType;
//
//import java.util.ArrayList;
//
///**
// * Created by Michael Wheeler on 2/08/15.
// */
//public class ChangeTaskEffort implements Command{
//
//    private Sprint sprint;
//    private Task task;
//    private Double oldEffort;
//    private Double newEffort;
//    private Story story;
//
//    /**
//     * Create a ChangeTaskEffort command for changing the effort of an Task
//     * @param sprint sprint to use - (Since person allocation is done in Sprint Frame / Scrumboard)
//     * @param task Task to be changed
//     * @param effort Double to change the Task effort to
//     * @param story Story
//     */
//    public ChangeTaskEffort(Sprint sprint, Task task, Double effort, Story story) {
//        this.sprint = sprint;
//        this.task = task;
//        this.newEffort = effort;
//        this.oldEffort = task.getEffort();
//        this.story = story;
//    }
//
//    /**
//     * Undo the action, setting the Task effort to the old effort
//     */
//    @Override
//    public void undo() {
//        task.setEffort(oldEffort);
//    }
//
//    /**
//     * Redo the action, setting the Task effort to the new effort
//     */
//    @Override
//    public void redo() {
//        task.setEffort(newEffort);
//    }
//
//    /**
//     * Return a message indicating properties of the command
//     * @return a message indicating properties of the command
//     */
//    @Override
//    public CommandMessage getMessage() {
//        CommandMessage message = new CommandMessage("Change", ItemType.SPRINT, sprint, "taskTable");
//        message.setTab("Scrum Board");
//
//        ArrayList<Object> scl = new ArrayList<>();
//        scl.add(story);
//        scl.add(task);
//        message.setSpecialCaseList(scl);
//
//        return message;
//    }
//}
