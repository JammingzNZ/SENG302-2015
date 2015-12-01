package seng302.group6;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import seng302.group6.Model.Command.Backlog.RemoveBacklogStory;
import seng302.group6.Model.Command.CommandManager;
import seng302.group6.Model.Command.Story.AcceptanceCriteria.AddAcceptanceCriteria;
import seng302.group6.Model.Command.Story.AcceptanceCriteria.ChangeAcceptanceCriteria;
import seng302.group6.Model.Command.Story.AcceptanceCriteria.MoveAcceptanceCriteria;
import seng302.group6.Model.Command.Story.AcceptanceCriteria.RemoveAcceptanceCriteria;
import seng302.group6.Model.Command.Story.*;
import seng302.group6.Model.Command.Story.Dependencies.AddDependency;
import seng302.group6.Model.Command.Story.Dependencies.RemoveDependency;
import seng302.group6.Model.Command.Story.Tasks.*;
import seng302.group6.Model.CyclicDependencyException;
import seng302.group6.Model.EstimationScale.ScaleType;
import seng302.group6.Model.ItemClasses.*;
import seng302.group6.Model.Status.StatusType;

/**
 * Created by simon on 27/05/15.
 */
public class CommandStoryTest extends TestCase
{
    CommandManager manager;
    Person p;
    Story s;
    AcceptanceCriteria ac1;
    AcceptanceCriteria ac2;
    AcceptanceCriteria ac3;
    Task t1;
    Task t2;
    Task t3;

    public CommandStoryTest(String testName)
    {
        super(testName);
    }

    public static Test suite()
    {
        return new TestSuite( CommandStoryTest.class );
    }

    public void setUp() throws Exception
    {
        p = new Person("John", "Smith", "John", "1234");
        manager = new CommandManager();
        Workspace.reset();
        Workspace.createPerson(p);
        s = new Story("TS", "Test Story", "A description", p.uid());
        ac1 = new AcceptanceCriteria("Must not have major bugs.");
        ac2 = new AcceptanceCriteria("Show an error message if something fails.");
        ac3 = new AcceptanceCriteria("A maximum of 10 items are allowed.");
        t1 = new Task("Test Task 1", "Test Task 1 Description", 12.00);
        t2 = new Task("Test Task 2", "Test Task 2 Description", 17.50);
        t3 = new Task("Test Task 3", "Test Task 3 Description", 25.00);


    }

    /**
     * Tests that a story can be created and undone correctly using commands.
     */
    public void testCreateStory()
    {
        Story testStory;

        manager.addCommand(new CreateStory(s));

        testStory = Workspace.getStory(s.uid());
        assertEquals("TS", testStory.getShortName());
        assertEquals("Test Story", testStory.getLongName());
        assertEquals("A description", testStory.getDescription());
        assertEquals(p.getShortName(), testStory.getCreatorName());

        manager.undo();

        testStory = Workspace.getStory(s.uid());
        assertNull(testStory);

        manager.redo();

        testStory = Workspace.getStory(s.uid());
        assertEquals("TS", testStory.getShortName());
    }

    /**
     * Tests that a story can be deleted/undo/redone
     */
    public void testDeleteStory()
    {
        Workspace.createStory(s);
        Story testStory = Workspace.getStory(s.uid());

        assertTrue(Workspace.getStories().contains(s.uid()));
        assertFalse(Workspace.getStories().isEmpty());

        manager.addCommand(new DeleteStory(s));

        assertFalse(Workspace.getStories().contains(s.uid()));
        assertTrue(Workspace.getStories().isEmpty());

        manager.undo();

        assertTrue(Workspace.getStories().contains(s.uid()));
        assertFalse(Workspace.getStories().isEmpty());

        manager.redo();

        assertFalse(Workspace.getStories().contains(s.uid()));
        assertTrue(Workspace.getStories().isEmpty());

    }

    /**
     * Tests that a story name can be changed/undone/redone
     */
    public void testChangeStoryName()
    {
        Workspace.addStory(s);
        Story testStory = Workspace.getStory(s.uid());
        manager.addCommand(new ChangeStoryName(testStory, "El Story"));

        assertEquals("El Story", testStory.getLongName());

        manager.undo();

        assertEquals("Test Story", testStory.getLongName());

        manager.redo();

        assertEquals("El Story", testStory.getLongName());

        Workspace.removeStory(s.uid());
    }

    /**
     * Tests that a story description can be changed/undone/redone
     */
    public void testChangeStoryDescription()
    {
        Workspace.addStory(s);
        Story testStory = Workspace.getStory(s.uid());
        manager.addCommand(new ChangeStoryDescription(testStory, "Cool"));

        assertEquals("Cool", testStory.getDescription());

        manager.undo();
        assertEquals("A description", testStory.getDescription());

        manager.redo();
        assertEquals("Cool", testStory.getDescription());

        Workspace.removeStory(s.uid());
    }

    /**
     * Test that AC addition can be undone/redone.
     */
    public void testAddAcceptanceCriteria() {
        manager.addCommand(new AddAcceptanceCriteria(s, ac1));

        assertEquals(ac1, s.getAcceptanceCriteria(0));

        manager.undo();

        assertTrue(s.getAllAcceptanceCriteria().isEmpty());

        manager.redo();

        assertEquals(ac1, s.getAcceptanceCriteria(0));
    }

    /**
     * Test that AC removal can be undone/redone.
     */
    public void testRemoveAcceptanceCriteria() {
        s.addAcceptanceCriteria(ac1);

        manager.addCommand(new RemoveAcceptanceCriteria(s, ac1));

        assertTrue(s.getAllAcceptanceCriteria().isEmpty());

        manager.undo();

        assertEquals(ac1, s.getAcceptanceCriteria(0));

        manager.redo();

        assertTrue(s.getAllAcceptanceCriteria().isEmpty());
    }

    /**
     * Test that moving an AC can be undone/redone.
     */
    public void testMoveAcceptanceCriteria() {
        s.addAcceptanceCriteria(ac1);
        s.addAcceptanceCriteria(ac2);
        s.addAcceptanceCriteria(ac3);

        manager.addCommand(new MoveAcceptanceCriteria(s, ac1, 2));

        assertEquals(ac2, s.getAcceptanceCriteria(0));
        assertEquals(ac3, s.getAcceptanceCriteria(1));
        assertEquals(ac1, s.getAcceptanceCriteria(2));

        manager.undo();

        assertEquals(ac1, s.getAcceptanceCriteria(0));
        assertEquals(ac2, s.getAcceptanceCriteria(1));
        assertEquals(ac3, s.getAcceptanceCriteria(2));

        manager.redo();

        assertEquals(ac2, s.getAcceptanceCriteria(0));
        assertEquals(ac3, s.getAcceptanceCriteria(1));
        assertEquals(ac1, s.getAcceptanceCriteria(2));
    }

    /**
     * Test that AC editing can be undone/redone.
     */
    public void testChangeAcceptanceCriteria() {
        s.addAcceptanceCriteria(ac1);

        manager.addCommand(new ChangeAcceptanceCriteria(s, ac1, "Must have absolutely no bugs."));

        assertEquals("Must have absolutely no bugs.", s.getAcceptanceCriteria(0).getText());

        manager.undo();

        assertEquals("Must not have major bugs.", s.getAcceptanceCriteria(0).getText());

        manager.redo();

        assertEquals("Must have absolutely no bugs.", s.getAcceptanceCriteria(0).getText());
    }

    public void testChangeEstimate() {
        s.setScaleType(ScaleType.PSEUDOFIBONACCI);
        s.addAcceptanceCriteria(new AcceptanceCriteria("Test"));
        //s.setEstimate(1);

        assertEquals(null, s.getEstimateRep());

        manager.addCommand(new ChangeStoryEstimate(s, 10));

        assertEquals("100", s.getEstimateRep());

        manager.undo();

        assertEquals(null, s.getEstimateRep());

        manager.redo();

        assertEquals("100", s.getEstimateRep());
    }

    public void testChangeScaleType() {
        s.setScaleType(ScaleType.PSEUDOFIBONACCI);
        s.addAcceptanceCriteria(new AcceptanceCriteria("Test"));
        s.setEstimate(1);

        assertEquals("1/2", s.getEstimateRep());

        manager.addCommand(new ChangeStoryScale(s, ScaleType.DOGBREEDS));

        assertEquals("Chihuahua", s.getEstimateRep());

        manager.undo();

        assertEquals("1/2", s.getEstimateRep());

        manager.redo();

        assertEquals("Chihuahua", s.getEstimateRep());
    }

    public void testChangeReadiness(){

        assertFalse(s.getReadiness());

        try {
            manager.addCommand(new ChangeStoryReadiness(s));
            assert (false);
        }
        catch (IllegalArgumentException e) {
            assertEquals("Story must be estimated before it can be ready", e.getMessage());
        }

        s.addAcceptanceCriteria(ac1);
        s.setEstimate(1);

        try {
            manager.addCommand(new ChangeStoryReadiness(s));
            assert (false);
        }
        catch (IllegalArgumentException e) {
            assertEquals("Story must be in backlog before it can be ready", e.getMessage());
        }

        Workspace.createStory(s);
        Backlog b = new Backlog("Test", "Test", "Test", p.uid());
        b.addStory(s.uid(), 1);

        manager.addCommand(new ChangeStoryReadiness(s));

        assertTrue(s.getReadiness());

        manager.undo();

        assertFalse(s.getReadiness());

        manager.redo();

        assertTrue(s.getReadiness());

        manager.addCommand(new ChangeStoryEstimate(s, null));

        assertFalse(s.getReadiness());

        manager.undo();

        assertTrue(s.getReadiness());

        manager.addCommand(new RemoveAcceptanceCriteria(s, ac1));

        assertFalse(s.getReadiness());

        manager.undo();

        assertTrue(s.getReadiness());

        manager.addCommand(new RemoveBacklogStory(b, s.uid(), s.getCurrentPriority()));

        assertFalse(s.getReadiness());

        manager.undo();

        assertTrue(s.getReadiness());
    }

    public void testAddDependency() {
        Workspace.createStory(s);
        Story s2 = new Story("Test", "Test", "Test", "Test");
        Workspace.createStory(s2);

        assertEquals(0, s.getDependencies().size());

        manager.addCommand(new AddDependency(s, s2.uid()));

        assertTrue(s.getDependencies().contains(s2.uid()));

        manager.undo();

        assertEquals(0, s.getDependencies().size());

        manager.redo();

        assertTrue(s.getDependencies().contains(s2.uid()));

        try {
            manager.addCommand(new AddDependency(s, s.uid()));
            assertTrue(false);
        }
        catch (IllegalArgumentException e) {
            assertEquals("Cyclic Dependency", e.getMessage());
        }

        assertFalse(s.getDependencies().contains(s.uid()));
    }

    public void testRemoveDependency() {
        Workspace.createStory(s);
        Story s2 = new Story("Test", "Test", "Test", "Test");
        Workspace.createStory(s2);
        try {
            s.addDependency(s2.uid());
        }
        catch (CyclicDependencyException e) {
            assertTrue(false);
        }

        assertTrue(s.getDependencies().contains(s2.uid()));

        manager.addCommand(new RemoveDependency(s, s2.uid()));

        assertEquals(0, s.getDependencies().size());

        manager.undo();

        assertTrue(s.getDependencies().contains(s2.uid()));

        manager.redo();

        assertEquals(0, s.getDependencies().size());
    }

    /**
     * Test that Task addition can be undone/redone.
     */
    public void testAddTask() {
        manager.addCommand(new AddTask(s, t1));

        assertEquals(t1, s.getTask(0));

        manager.undo();

        assertTrue(s.getAllTasks().isEmpty());

        manager.redo();

        assertEquals(t1, s.getTask(0));
    }

    /**
     * Test that Task deletion can be undone/redone.
     */
    public void testDeletingTask() {
        s.addTask(t1);

        manager.addCommand(new DeleteTask(s, t1));

        assertTrue(s.getAllTasks().isEmpty());

        manager.undo();

        assertEquals(t1, s.getTask(0));

        manager.redo();

        assertTrue(s.getAllTasks().isEmpty());
    }

    /**
     * Test that moving an Task can be undone/redone.
     */
    public void testMoveTask() {
        s.addTask(t1);
        s.addTask(t2);
        s.addTask(t3);

        manager.addCommand(new MoveTask(s, t1, 2));

        assertEquals(t2, s.getTask(0));
        assertEquals(t3, s.getTask(1));
        assertEquals(t1, s.getTask(2));

        manager.undo();

        assertEquals(t1, s.getTask(0));
        assertEquals(t2, s.getTask(1));
        assertEquals(t3, s.getTask(2));

        manager.redo();

        assertEquals(t2, s.getTask(0));
        assertEquals(t3, s.getTask(1));
        assertEquals(t1, s.getTask(2));
    }

    /**
     * Test that Task description editing can be undone/redone.
     */
    public void testChangeTaskDescription() {
        s.addTask(t1);

        manager.addCommand(new ChangeTaskDescription(s, t1, "New Description"));

        assertEquals("New Description", s.getTask(0).getDescription());

        manager.undo();

        assertEquals("Test Task 1 Description", s.getTask(0).getDescription());

        manager.redo();

        assertEquals("New Description", s.getTask(0).getDescription());
    }

    /**
     * Test that Task status editing can be undone/redone.
     */
    public void testChangeTaskStatus() {
        s.addTask(t1);

        assertEquals(StatusType.NOT_STARTED, s.getTask(0).getStatus());

        manager.addCommand(new ChangeTaskStatus(s, t1, StatusType.DONE, null, null));

        assertEquals(StatusType.DONE, s.getTask(0).getStatus());

        manager.undo();

        assertEquals(StatusType.NOT_STARTED, s.getTask(0).getStatus());

        manager.redo();

        assertEquals(StatusType.DONE, s.getTask(0).getStatus());
    }

    /**
     * Test that Task estimation editing can be undone/redone.
     */
    public void testChangeTaskEstimation() {
        s.addTask(t1);

        assertEquals(12.00, s.getTask(0).getEstimation());

        manager.addCommand(new ChangeTaskEstimation(s, t1, 50.00, null, null, null));

        assertEquals(50.00, s.getTask(0).getEstimation());

        manager.undo();

        assertEquals(12.00, s.getTask(0).getEstimation());

        manager.redo();

        assertEquals(50.00, s.getTask(0).getEstimation());
    }
}
