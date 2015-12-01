package seng302.group6;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import seng302.group6.Model.Command.CommandManager;
import seng302.group6.Model.Command.Sprint.CreateSprint;
import seng302.group6.Model.Command.Sprint.DeleteSprint;
import seng302.group6.Model.ItemClasses.Sprint;
import seng302.group6.Model.ItemClasses.Workspace;

/**
 * Created by Michael Wheeler on 29/07/15.
 */
public class CommandSprintTest extends TestCase{

    CommandManager manager;
    Sprint s;


    public CommandSprintTest(String testName)
    {
        super(testName);
    }

    public static Test suite()
    {
        return new TestSuite( CommandSprintTest.class );
    }

    public void setUp() throws Exception
    {
        manager = new CommandManager();
        Workspace.reset();
        s = new Sprint("testShortName", "testFullName", "testDescription");
    }

    /**
     * Tests that a sprint can be created and undone correctly using commands.
     */
    public void testCreateSprint()
    {

        Sprint testSprint;
        manager.addCommand(new CreateSprint(s));

        testSprint = Workspace.getSprint(s.uid());
        assertEquals("testShortName", testSprint.getShortName());
        assertEquals("testFullName", testSprint.getFullName());
        assertEquals("testDescription", testSprint.getDescription());

        manager.undo();

        testSprint = Workspace.getSprint(s.uid());
        assertNull(testSprint);

        manager.redo();

        testSprint = Workspace.getSprint(s.uid());
        assertEquals("testShortName", testSprint.getShortName());
    }

    /**
     * Tests that a sprint can be deleted/undo/redone
     */
    public void testDeleteSprint()
    {
        Workspace.createSprint(s);

        assertTrue(Workspace.getSprints().contains(s.uid()));
        assertFalse(Workspace.getSprints().isEmpty());

        manager.addCommand(new DeleteSprint(s));

        assertFalse(Workspace.getSprints().contains(s.uid()));
        assertTrue(Workspace.getSprints().isEmpty());

        manager.undo();

        assertTrue(Workspace.getSprints().contains(s.uid()));
        assertFalse(Workspace.getSprints().isEmpty());

        manager.redo();

        assertFalse(Workspace.getSprints().contains(s.uid()));
        assertTrue(Workspace.getSprints().isEmpty());

    }
}
