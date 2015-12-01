package seng302.group6.View;

import javafx.collections.ObservableList;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.ListCell;
import javafx.scene.image.WritableImage;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import seng302.group6.Controller.Content.StoryFrameController;
import seng302.group6.Model.ItemClasses.AcceptanceCriteria;

/**
 * Custom List View Cell which can be dragged and dropped to reorder.
 * Reordering is not done in this class and it currently only works
 * with acceptance criteria
 *
 * Created by simon on 5/07/15.
 */
public class DragAndDropCell extends ListCell<AcceptanceCriteria>
{
    /**
     * Constructs the Cell and sets all of the callbacks for drag events
     * @param parentController the parent controller of the list view
     */
    public DragAndDropCell(StoryFrameController parentController) {
        ListCell thisCell = this;

        setOnDragDetected(event -> {
            if (getItem() == null) {
                return;
            }

            // Set content of dragboard to index of dragged item
            Dragboard dragboard = startDragAndDrop(TransferMode.MOVE);
            ClipboardContent content = new ClipboardContent();
            ObservableList<AcceptanceCriteria> items = getListView().getItems();
            content.putString(Integer.toString(items.indexOf(getItem())));

            // Convert cell to image to show when dragging
            WritableImage snapshot = snapshot(new SnapshotParameters(), null);
            dragboard.setDragView(snapshot);
            dragboard.setContent(content);

            event.consume();
        });

        setOnDragOver(event -> {
            if (event.getGestureSource() != thisCell &&
                    event.getDragboard().hasString()) {
                event.acceptTransferModes(TransferMode.MOVE);
            }

            event.consume();
        });

        setOnDragEntered(event -> {
            if (event.getGestureSource() != thisCell &&
                    event.getDragboard().hasString()) {
                setOpacity(0.5);
            }
        });

        setOnDragExited(event -> {
            if (event.getGestureSource() != thisCell &&
                    event.getDragboard().hasString()) {
                setOpacity(1);
            }
        });

        setOnDragDropped(event -> {
            if (getItem() == null) {
                return;
            }

            Dragboard db = event.getDragboard();
            boolean success = false;

            if (db.hasString()) {
                ObservableList<AcceptanceCriteria> items = getListView().getItems();
                int draggedIdx = Integer.parseInt(db.getString());
                int thisIdx = items.indexOf(getItem());

                parentController.moveAcceptanceCriteria(draggedIdx, thisIdx);

                success = true;
            }
            event.setDropCompleted(success);

            event.consume();
        });

        setOnDragDone(DragEvent::consume);
    }

    /**
     * Sets the content of the Cell to the toString of the item
     * @param item the item to be displayed in the cell
     * @param empty whether or not the cell represents data from the list
     */
    @Override
    protected void updateItem(AcceptanceCriteria item, boolean empty) {
        super.updateItem(item, empty);

        if (empty || item == null) {
            setText(null);
        } else {
            setText(item.toString());
        }
    }
}