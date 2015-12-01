package seng302.group6.Model.Command.Project;

import seng302.group6.Model.Command.Command;
import seng302.group6.Model.Command.CommandMessage;
import seng302.group6.Model.ItemClasses.Project;
import seng302.group6.Model.ItemClasses.Workspace;
import seng302.group6.Model.ItemType;

import java.util.HashMap;

/**
 * Command class which allows undo/redo functionality for creating a project
 * Created by Josh Norton on 17/03/2015.
 */
public class CreateProject implements Command {

    private Project project;
    private HashMap<String, Project> projects;

    /**
     * Create a CreateProject command for adding/removing the project to/from the given projects hashmap.
     * @param projects A hashmap to add the project to
     * @param project Project to be added to the hashmap
     */
    public CreateProject(HashMap<String, Project> projects, Project project) {
        super();

        this.projects = projects;
        this.project = project;
    }

    /**
     * Undo the action, removing the project from the projects hashmap.
     */
    public void undo() {
        Workspace.removeProject(project.uid());
    }

    /**
     * Redo the action, adding the project to the projects hashmap.
     */
    public void redo() {
        Workspace.addProject(project);
    }


    /**
     * Return a message indicating properties of the command
     * @return a message indicating properties of the command
     */
    @Override
    public CommandMessage getMessage() {
        CommandMessage message = new CommandMessage("Create", ItemType.PROJECT, project);

        return message;
    }

}
