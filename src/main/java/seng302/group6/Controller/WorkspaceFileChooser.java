package seng302.group6.Controller;

import javafx.stage.FileChooser;
import javafx.stage.Stage;
import seng302.group6.Controller.Window.WindowController;
import seng302.group6.Model.ItemClasses.Workspace;

import java.io.File;

/**
 *File chooser abstraction...
 * creates a file chooser dialog, opening it in the location of the last saved file in the workspace.
 * when a file is to be saved by the dialog, and the extension is not found on the end of the filename,
 * the extension is appended automatically.
 * Created by jaln on 23/07/15.
 */
public class WorkspaceFileChooser {

    String mode;
    String extension;
    FileChooser fileChooser;

    /**
     * File chooser abstraction...
     * creates a file chooser dialog, opening it in the location of the last saved file in the workspace.
     * when a file is to be saved by the dialog, and the extension is not found on the end of the filename,
     * the extension is appended automatically.
     *
     * @param  title the file chooser dialog title
     * @param  mode specifies the file dialog mode: "open" or "save"
     * @param  defaultFilename the default name to put in the file name field, eg default.txt.
     * @param  fileDescription a string describing the file type eg "text files"
     * @param  extension is the file extension to filter on, eg txt for .txt files
     */
    public WorkspaceFileChooser(String title, String mode, String defaultFilename, String fileDescription,
                                String extension)
    {
        this.mode = mode;
        this.fileChooser = new javafx.stage.FileChooser();
        this.extension = extension;

        fileChooser.setTitle(title);

        // Set extension filter
        javafx.stage.FileChooser.ExtensionFilter extFilter;
        extFilter = new FileChooser.ExtensionFilter(fileDescription + " (*." + extension + ")", "*." + extension);
        fileChooser.getExtensionFilters().add(extFilter);

        // go to last opened file directory
        if(Workspace.appdata.getProjectFile() != null) {
            File last = Workspace.appdata.getProjectFile().getParentFile();
            fileChooser.setInitialDirectory(last);
        }
        fileChooser.setInitialFileName(defaultFilename);


    }

    /**
     * Gets the file
     * @return the selected file
     */
    public File getFile() {
        Stage primaryStage = (Stage) WindowController.instance.basePane.getScene().getWindow();
        switch (mode) {
            case ("open"):
                return fileChooser.showOpenDialog(primaryStage);
            case ("save"):
                File file = fileChooser.showSaveDialog(primaryStage);
                if (file != null) {
                    String path = file.getPath();
                    if (!path.endsWith("." + extension)) {
                        file = new File(path + "." + extension);
                    }
                }
                return file;
            default:
                throw new IllegalArgumentException("mode may be either save or open, it was " + mode);
        }
    }
}
