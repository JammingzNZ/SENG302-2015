package seng302.group6.Controller;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.util.Callback;
import seng302.group6.Controller.Window.WindowController;
import seng302.group6.Debug;
import seng302.group6.Model.ItemClasses.Item;
import seng302.group6.Model.ItemClasses.Searcher;
import seng302.group6.Model.ItemType;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Controller for the Search functionality
 * Created by simon on 31/07/15.
 */
public class SearchController
{
    @FXML private TextField searchBox;
    @FXML private Button closeButton;

    @FXML private RadioButton currentListRadio;
    @FXML private RadioButton workspaceRadio;
    @FXML private CheckBox shortNameOnlyCheck;

    @FXML private ListView<Item> resultsList;

    private ArrayList<Item> searchResults;

    private Searcher searcher;

    /**
     * Initialize controller
     * Creates a custom list cell for the results.
     */
    public void initialize()
    {
        searcher = new Searcher();

        Platform.runLater(() -> searchBox.requestFocus());

        resultsList.setCellFactory(new Callback<ListView<Item>, ListCell<Item>>()
        {
            @Override
            public ListCell<Item> call(ListView<Item> param)
            {
                ListCell<Item> cell = new ListCell<Item>()
                {
                    @Override
                    public void updateItem(Item item, boolean empty)
                    {
                        super.updateItem(item, empty);

                        if (empty) {
                            setGraphic(null);
                            setText(null);
                        } else {
                            try {
                                // Get the top and bottom lines of text
                                String[] text = item.searchString().split("\n", 2);
                                String firstLine = text[0];
                                String secondLine = text[1];

                                // Create the labels from the text
                                Label top = new Label();
                                top.setText(firstLine);
                                top.setStyle("-fx-font-weight: bold");
                                Label bottom = new Label();
                                bottom.setText(secondLine);

                                // Add the labels to a VBox
                                VBox vbox = new VBox();
                                vbox.getChildren().addAll(top, bottom);

                                // Show the VBox in the cell
                                setGraphic(vbox);
                                setText(null);
                            } catch (IndexOutOfBoundsException e) {
                                setText(item.toString());
                            }
                        }
                    }
                };
                return cell;
            }
        });


        searchBox.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> arg0, String oldPropertyValue, String newPropertyValue) {
                search();
            }
        });
    }

    /**
     * Searches and shows results in ListView
     */
    @FXML
    public void search()
    {
        if (searchBox.getText().equals("")) {
            clearResults();
        } else {
            Boolean wholeWorkspace = workspaceRadio.isSelected();
            Boolean shortNameOnly = shortNameOnlyCheck.isSelected();

            HashMap<ItemType, ArrayList<Item>> allResults = new HashMap<>();
            ArrayList<Item> currentListResults = new ArrayList<>();

            if (wholeWorkspace) {
                allResults = searcher.searchAll(searchBox.getText(), shortNameOnly);
            } else {
                ItemType itemType = WindowController.instance.sideDisplayListController().currentList;
                currentListResults = searcher.searchSingle(searchBox.getText(), shortNameOnly, itemType);
            }
            // Checks to see if there are any results in the allResults hashmap
            Boolean allResultsEmpty = true;
            for (ItemType key : allResults.keySet()) {
                if (!allResults.get(key).isEmpty()) {
                    allResultsEmpty = false;
                    break;
                }
            }

            if (!allResultsEmpty) {
                showResults(allResults);
            } else if (!currentListResults.isEmpty()) {
                showResults(currentListResults);
            } else {
                clearResults();
            }
        }
        WindowController.instance.updateGUI();
    }

    /**
     * Closes the search pane
     */
    @FXML
    public void close()
    {
        WindowController.instance.showLast();
    }

    /**
     * Handles a mouse click event in the results list. Goes to the item if the
     * event was a double click.
     * @param event the mouse event
     */
    @FXML
    public void mouseClicked(MouseEvent event)
    {
        if(event.getButton().equals(MouseButton.PRIMARY)
                && event.getClickCount() == 2){
            goToItem(resultsList.getSelectionModel().getSelectedItem());
        }
    }

    /**
     * Handles a key event in the results list. Goes to the item if enter was
     * pressed while an item was selected.
     * @param event the key event
     */
    @FXML
    public void keyPressed(KeyEvent event)
    {
        if (event.getCode() == KeyCode.ENTER &&
                resultsList.getSelectionModel().getSelectedItem() != null) {
            goToItem(resultsList.getSelectionModel().getSelectedItem());
        }
    }

    /**
     * Shows the given item in the main content area
     * @param item the item to go to
     */
    private void goToItem(Item item)
    {
        try {
            // Show the item in the main content area
            WindowController.instance.setMainView(item.type(), item.getShortName());
            // Show the item type in the side list
            WindowController.sideDisplayListController().displayItem(item.type());
            WindowController.instance.updateGUI();
        } catch (Exception e) {
            Debug.run(() -> e.printStackTrace());
        }
    }

    /**
     * Shows all results in the list view
     * @param results Hashmap of item types and lists of items that match
     *                search string
     */
    private void showResults(HashMap<ItemType, ArrayList<Item>> results)
    {
        ArrayList<Item> itemsList = new ArrayList<>();
        for (ItemType key : results.keySet()) {
            itemsList.addAll(results.get(key));
        }

        searchResults = itemsList;

        ObservableList<Item> items = FXCollections.observableArrayList(itemsList);
        resultsList.setItems(items);
        WindowController.instance.updateGUI();
    }

    /**
     * Shows current list results in the list view
     * @param results List of items that match search string
     */
    private void showResults(ArrayList<Item> results)
    {
        searchResults = results;
        ObservableList<Item> items = FXCollections.observableArrayList(results);
        resultsList.setItems(items);
        WindowController.instance.updateGUI();
    }

    /**
     * Clears the results list view
     */
    private void clearResults()
    {
        resultsList.setItems(null);
        WindowController.instance.updateGUI();
        searchResults = null;
    }

    /**
     * Gets the search results
     * @return the search results
     */
    public ArrayList<Item> getResults()
    {
        return searchResults;
    }
}
