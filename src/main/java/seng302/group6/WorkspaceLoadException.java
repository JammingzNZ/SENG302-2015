package seng302.group6;

/**
 * A Exception for when the workspace cannot be loaded
 * Created by mike on 16/05/15.
 */
public class WorkspaceLoadException extends Exception {
    public WorkspaceLoadException(String message) {
        super(message);
    }
}
