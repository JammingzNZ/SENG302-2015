package seng302.group6;

/**
 * An exception for when the Workspace version is a mismatch
 * Created by mike on 16/05/15.
 */
public class WorkspaceVersionMismatchException extends Exception {
    public WorkspaceVersionMismatchException(String message) {
        super(message);
    }
}
