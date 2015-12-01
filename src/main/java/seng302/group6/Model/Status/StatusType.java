package seng302.group6.Model.Status;

/**
 * Used for giving Stories, Tasks and potentially Sprints a status
 * Created by Michael Wheeler on 24/07/15.
 */
public enum StatusType {
    READY("Ready"),
    IN_PROGRESS("In Progress"),
    PENDING("Pending"),
    BLOCKED("Blocked"),
    DONE("Done"),
    DEFERRED("Deferred"),
    NOT_STARTED("Not Started");

    private String status;

    /**
     * Constructor for the StatusType
     * @param status the status in string format
     */
    StatusType(String status){
        this.status = status;
    }

    /**
     * Gets the Status in string format
     * @return status in string format
     */
    public String getStatus() {
        return status;
    }

    /**
     * The tostring method for the StatusType
     * @return StatusType.getStatus()
     */
    @Override
    public String toString(){ return getStatus(); }
}
