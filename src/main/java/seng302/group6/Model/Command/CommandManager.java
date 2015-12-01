package seng302.group6.Model.Command;

/**
 * Manages a Queue of Changeables to perform undo and/or redo operations. Clients can add implementations of the Command
 * class to this class, and it will manage undo/redo as a Queue.
 *
 * @author Greg Cope
 *
 */
public class CommandManager {

    //the current index node
    private Node currentIndex = null;
    //the parent node far left node.
    private Node parentNode = new Node();
    //the node that corresponds to the current save state.
    private Node savedIndex = parentNode;
    //a command used for grouping multiple commands
    private GroupCommand groupCommand = null;
    //Indicates whether to go back or forward to the save point
    private boolean saveBack = true;
    private Node savePathNode;
    private Node branchIndex;



    /**
     * Creates a newCommandManager object which is initially empty.
     */
    public CommandManager(){
        currentIndex = parentNode;
    }


    /**
     * Creates a newCommandManager which is a duplicate of the parameter in both contents and current index.
     * @param manager the existing manager
     */
    public CommandManager(CommandManager manager){
        this();
        currentIndex = manager.currentIndex;
    }


    /**
     * Clears all Commands contained in this manager.
     */
    public void clear(){
        parentNode.right = null;
        currentIndex = parentNode;
        setSavedIndex();
        groupCommand = null;
    }


    /**
     * Adds a Command to manage.
     * @param command being added
     */
    public void addCommand(Command command){
        if (groupCommand == null) {
            //Make sure any redo exceptions are thrown before adding the command
            command.redo();
            command.undo();

            Node node = new Node(command);

            //Store old branch for revert purposes
            if (!saveBack && currentIndex != savedIndex) {
                if (branchIndex != null) {
                    branchIndex.right = savePathNode;
                }
                branchIndex = currentIndex;
                savePathNode = currentIndex.right;
            }

            currentIndex.right = node;
            node.left = currentIndex;
            /*currentIndex = node;
            command.redo();*/
            redo();
        }
        else {
            groupCommand.addCommand(command);
        }
    }


    /**
     * Determines if an undo can be performed.
     * @return true if undo can be performed, false otherwise
     */
    public boolean canUndo(){
        return currentIndex != parentNode;
    }


    /**
     * Determines if a redo can be performed.
     * @return true if redo can be performed, false otherwise
     */
    public boolean canRedo(){
        return currentIndex.right != null;
    }


    /**
     * Undoes the Command at the current index.
     * @throws IllegalStateException if canUndo returns false.
     */
    public void undo(){
        //validate
        if ( !canUndo() ){
            throw new IllegalStateException("Cannot undo. Index is out of range.");
        }
        //undo
        currentIndex.command.undo();
        //set index
        moveLeft();
    }


    /**
     * Moves the internal pointer of the backed linked list to the left.
     * @throws IllegalStateException If the left index is null.
     */
    private void moveLeft(){
        if ( currentIndex.left == null ){
            throw new IllegalStateException("Internal index set to null.");
        }
        if (currentIndex == savedIndex || currentIndex == branchIndex) {
            saveBack = false;
        }
        currentIndex = currentIndex.left;
    }


    /**
     * Moves the internal pointer of the backed linked list to the right.
     * @throws IllegalStateException If the right index is null.
     */
    private void moveRight(){
        if ( currentIndex.right == null ){
            throw new IllegalStateException("Internal index set to null.");
        }
        if (currentIndex == savedIndex || currentIndex == branchIndex) {
            saveBack = true;
        }
        currentIndex = currentIndex.right;
    }


    /**
     * Set the point in the command list that corresponds to the current save state
     */
    public void setSavedIndex() {
        savedIndex = currentIndex;
        saveBack = true;
        branchIndex = null;
        savePathNode = null;
    }


    /**
     * Indicates whether there are saved changes. (True = no changes, False = changes)
     * @return true if there are no changes, false if there are changes to save
     */
    public boolean getSaved() {
        return (savedIndex == currentIndex);
    }


    /**
     * Redoes the Command at the current index.
     * @throws IllegalStateException if canRedo returns false.
     */
    public void redo(){
        //validate
        if ( !canRedo() ){
            throw new IllegalStateException("Cannot redo. Index is out of range.");
        }
        //reset index
        moveRight();
        //redo
        currentIndex.command.redo();
    }


    /**
     * Begin a group command: the next commands added will be grouped together
     */
    //TODO Remove and don't use elsewhere
    public void startGroupCommand() {
        groupCommand = new GroupCommand();
    }


    /**
     * End a group command, and add all the grouped commands to the command list
     */
    //TODO Remove and don't use elsewhere
    public void endGroupCommand() {
        GroupCommand temp = groupCommand;
        groupCommand = null;
        if (!temp.isEmpty()) {
            this.addCommand(temp);
        }
    }

    /**
     * Gets the message from the current undo action.
     * @return CommandMessage object
     */
    public CommandMessage getUndoMessage() {
        if (canUndo()) {
            return currentIndex.command.getMessage();
        }
        else {
            return null;
        }
    }


    /**
     * Gets the message from the current redo action.
     * @return CommandMessage object
     */
    public CommandMessage getRedoMessage() {
        if (canRedo()) {
            return currentIndex.right.command.getMessage();
        }
        else {
            return null;
        }
    }


    /**
     * Return to the save state, changing branch if necessary.
     * @return Message about what the final command updated
     */
    public CommandMessage revert() {
        while (currentIndex != savedIndex) {
            //Find the save point
            if (branchIndex == null) {
                if (saveBack) {
                    undo();
                } else {
                    redo();
                }
            } else {
                //Find the branch point
                while (currentIndex != branchIndex) {
                    if (saveBack) {
                        undo();
                    } else {
                        redo();
                    }
                }
                //Reattach the save branch
                currentIndex.right = savePathNode;
                branchIndex = null;
                savePathNode = null;
                saveBack = false;
            }
        }
        currentIndex.right = null;
        //Return an appropriate message
        if (currentIndex != parentNode) {
            return currentIndex.command.getMessage();
        }
        return null;
    }


    /**
     * Inner class to implement a doubly linked list for our queue of Commands.
     * @author Greg Cop
     *
     */
    private class Node {
        private Node left = null;
        private Node right = null;

        private final Command command;

        public Node(Command c){
            command = c;
        }

        public Node(){
            command = null;
        }
    }

}
