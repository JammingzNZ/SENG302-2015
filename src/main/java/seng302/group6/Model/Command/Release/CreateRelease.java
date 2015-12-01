package seng302.group6.Model.Command.Release;

import seng302.group6.Model.Command.Command;
import seng302.group6.Model.Command.CommandMessage;
import seng302.group6.Model.ItemClasses.Release;
import seng302.group6.Model.ItemClasses.Workspace;
import seng302.group6.Model.ItemType;

import java.util.HashMap;

/**
 * Command class which allows undo/redo functionality for creating a release
 * Created by Michael Wheeler on 6/05/15.
 */
public class CreateRelease implements Command {

    private HashMap<String, Release> releases;
    private Release release;
    private String project;

    /**
     * Create a CreateRelease command for adding/removing the release to/from the given releases hashmap.
     * @param releases A hashmap to add the release to
     * @param release Release to be added to the hashmap
     * @param project project the release belongs to
     */
    public CreateRelease(HashMap<String, Release> releases, Release release, String project) {
        super();

        this.release = release;
        this.releases = releases;
        this.project = project;
    }

    /**
     * Undo the action, removing the release from the teams hashmap.
     */
    public void undo() {
        Workspace.getProject(project).removeRelease(release.uid());
        releases.remove(release.uid());
    }

    /**
     * Redo the action, adding the release to the releases hashmap.
     */
    public void redo() {
        releases.put(release.uid(), release);
        Workspace.getProject(project).addRelease(release.uid());
    }


    /**
     * Return a message indicating properties of the command
     * @return a message indicating properties of the command
     */
    @Override
    public CommandMessage getMessage() {
        CommandMessage message = new CommandMessage("Create", ItemType.RELEASE, release);

        return message;
    }
}
