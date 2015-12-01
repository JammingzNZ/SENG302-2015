package seng302.group6;

import com.google.gson.annotations.Expose;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.IOException;

/**
 * Application data storage model.
 *
 * holds all parameters generic to the application itself. Provides the ability to save
 * the class contents to a JSON file.
 */
public class Appdata {
    public final static String DEFAULT_WORKSPACE_FILE_EXTENSION = "ss";
    public final static String DEFAULT_PROJECT_FILENAME = null;
    public final static String DEFAULT_APPLICATION_NAME = "Scrum Machine";
    public final static String APPDATA_FILEPATH = "appdata.json";
    public final static String WORKSPACE_VERSION = "0.6";

    @Expose
    private String projectFilePath = DEFAULT_PROJECT_FILENAME;
    @Expose
    private String projectFileExtension = DEFAULT_WORKSPACE_FILE_EXTENSION;
    @Expose
    private String applicationName = DEFAULT_APPLICATION_NAME;
    private static File file;

    /**
     * loads global application data from the specified filepath.
     *
     * @param appdataFilePath is the path to the application data file.
     * @return returns an Appdata instance, populated from the data file specified or a fresh Appdata class
     *          if there was no file to be read.
     */
    public static Appdata load(String appdataFilePath)
    {
        file = new File(appdataFilePath);

        Appdata appdata = (Appdata)SaverLoader.load(file, new TypeToken<Appdata>() {
            }.getType());

        if(appdata == null) {
            appdata = new Appdata();
            try {
                SaverLoader.save(file, appdata);
            }
            catch(IOException e) {
                Debug.println("Error creating new appdata file: " + e.getMessage());
            }
        }
        return appdata;
    }

    /**
     * Creates a workspace File instance using last known path to the workspace.
     *
     * @return returns a File() instance if there is a known workspace file, otherwise null.
     */
    public File getProjectFile()
    {
        if(projectFilePath != null) {
            return new File(projectFilePath);
        }
        return null;
    }

    /**
     * Sets the path to the workspace file, given the current workspace file instance.
     *
     * @param projectFile is an instance of a workspace file.
     */
    public void setProjectFile(File projectFile)
    {
        projectFilePath = projectFile.getPath();
        try {
            SaverLoader.save(file, this);
        }
        catch(IOException e) {
            Debug.println("Error saving appdata file: " + e.getMessage());
        }
    }

    /**
     * sets the current workspace file path to null.
     */
    public void clearProjectFilePath() {
        projectFilePath = null;
    }

    /**
     * @return returns the workspace file extension
     */
    public String getWorkspaceFileExtension()
    {
        return projectFileExtension;
    }

    /**
     * sets the workspace file extension.
     *
     * @param ext is a String to use for the workspace file extension
     */
    public void setWorkspaceFileExtension(String ext)
    {
        projectFileExtension = ext;
        try {
            SaverLoader.save(file, this);
        }
        catch(IOException e) {
            Debug.println("Error saving appdata file: " + e.getMessage());
        }
    }

    /**
     * @return returns the name of the application.
     */
    public String getApplicationName()
    {
        return applicationName;
    }

    /**
     * Sets the name of the application.
     *
     * @param name is a String to set as the name of the application.
     */
    public void setApplicationName(String name)
    {
        applicationName = name;
        try {
            SaverLoader.save(file, this);
        }
        catch(IOException e) {
            Debug.println("Error saving appdata file: " + e.getMessage());
        }
    }

    /**
     * @return returns the title string, to be put in the title bar of the application
     */
    public  String getApplicationTitle()
    {
        if(projectFilePath == null){
            return applicationName;
        }
        return applicationName + " - " + projectFilePath;
    }
}
