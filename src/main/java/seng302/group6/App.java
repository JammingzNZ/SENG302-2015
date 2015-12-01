package seng302.group6;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import org.controlsfx.dialog.Dialogs;
import seng302.group6.Controller.AppController;
import seng302.group6.Controller.Window.WindowController;
import seng302.group6.Model.ItemClasses.Workspace;

import java.io.FileNotFoundException;

/**
 * Entry point to the application
 *
 * App is responsible for initializing the outer frame of the application,
 * the application title, and loading the last saved workspace file.
 */
public class App extends Application
{
    @Override
    public void start(Stage primaryStage) throws Exception
    {
        Debug.setup(); // set up debugging messages
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("mainWindow.fxml"));
        Parent root = loader.load();
        WindowController mainWindowController = loader.getController();
        primaryStage.setScene(new Scene(root, 900, 650));
        primaryStage.setMinWidth(900);
        primaryStage.setMinHeight(650);
        primaryStage.show();

        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            public void handle(WindowEvent ev) {
                if (!AppController.instance().unsavedWarning()) {
                    ev.consume();
                }
            }
        });

        // message from Mike: these popups render the file menu unusable, why would that be?

        String message = "";
        Boolean exception = true;
        try {
            Workspace.loadOnStart(Appdata.APPDATA_FILEPATH);
            message = Workspace.appdata.getApplicationTitle();
            exception = false;
        }
        catch(FileNotFoundException e) {
            message = "Workspace Load Error - " + e.getMessage();
//            WindowController.showErrorAlertPopup("Open Workspace Error", e.getMessage());
        }
        catch(WorkspaceLoadException e) {
            message = "Workspace Load Error - " + e.getMessage();
//            WindowController.showErrorAlertPopup("Open Workspace Error", e.getMessage());
        }
        catch(WorkspaceVersionMismatchException e) {
            message = "Workspace Load Error - " + e.getMessage();
//            WindowController.showErrorAlertPopup("Open Workspace Error", e.getMessage());
        }
        WindowController.startupDefaultView();
        if (exception) {
            primaryStage.setTitle("Scrum Machine");
            Dialogs.create()
                    .title("Workspace Load Error")
                    .masthead(message)
                    .showError();
            Workspace.reset();
            Workspace.appdata.clearProjectFilePath();
        }
        else {
            primaryStage.setTitle(message);
            Workspace.tryInitPoAndSm();
        }
    }

    /**
     * application main function - calls Application.launch()
     *
     * @param args - not used
     */
    public static void main( String[] args )
    {

        launch(args);
    }
}
