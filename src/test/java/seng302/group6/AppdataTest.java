package seng302.group6;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import java.io.File;


public class AppdataTest extends TestCase
{
    private final String APPDATA_PATH =  System.getProperty("user.dir") + "/src/test/java/seng302/group6/test_appdata.json";
    private final String PROJECT_FILE =  System.getProperty("user.dir") + "/src/test/java/seng302/group6/test_project.ss";

    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public AppdataTest(String testName)
    {
        super(testName);
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite(AppdataTest.class);
    }


    public void testAppdata_load_make_new()
    {
        //TODO Rewrite this test
        new File(APPDATA_PATH).delete();
        File pf = new File(PROJECT_FILE);
        Appdata appdata = Appdata.load(APPDATA_PATH);
        assertNull(appdata.getProjectFile());
        appdata.setProjectFile(pf);
        assertEquals(pf.getName(), appdata.getProjectFile().getName());
    }

    public void testAppdata_load_existing()
    {
        File pf = new File(PROJECT_FILE);
        Appdata appdata = Appdata.load(APPDATA_PATH);
        assertEquals(pf.getName(), appdata.getProjectFile().getName());
    }

    public void testAppdata_setApplicationName_getApplicationName()
    {
        Appdata appdata = Appdata.load(APPDATA_PATH);
        assertEquals(appdata.getApplicationName(), Appdata.DEFAULT_APPLICATION_NAME);
        appdata.setApplicationName("hello");
        assertEquals(appdata.getApplicationName(), "hello");
        appdata.setApplicationName(Appdata.DEFAULT_APPLICATION_NAME);
    }

    public void testAppdata_setProjectFileExtension_getProjectFileExtension()
    {
        Appdata appdata = Appdata.load(APPDATA_PATH);
        assertEquals(appdata.getWorkspaceFileExtension(), Appdata.DEFAULT_WORKSPACE_FILE_EXTENSION);
        appdata.setWorkspaceFileExtension("hello");
        assertEquals(appdata.getWorkspaceFileExtension(), "hello");
        appdata.setWorkspaceFileExtension(Appdata.DEFAULT_WORKSPACE_FILE_EXTENSION);
    }

    public void testAppdata_getApplicationTitle()
    {
        new File(APPDATA_PATH).delete();
        File pf = new File(PROJECT_FILE);
        Appdata appdata = Appdata.load(APPDATA_PATH);
        assertEquals(appdata.getApplicationTitle(), Appdata.DEFAULT_APPLICATION_NAME);
        appdata.setProjectFile(pf);
        assertEquals(appdata.getApplicationTitle(), Appdata.DEFAULT_APPLICATION_NAME + " - " + appdata.getProjectFile());
    }
}
