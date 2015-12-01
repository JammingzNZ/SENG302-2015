package seng302.group6;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import seng302.group6.Model.Command.Backlog.*;
import seng302.group6.Model.Command.CommandManager;
import seng302.group6.Model.EstimationScale.ScaleType;
import seng302.group6.Model.ItemClasses.AcceptanceCriteria;
import seng302.group6.Model.ItemClasses.Backlog;
import seng302.group6.Model.ItemClasses.Story;
import seng302.group6.Model.ItemClasses.Workspace;

/**
 * Created by Michael Wheeler on 3/07/15.
 */
public class CommandBacklogTest extends TestCase {

    CommandManager manager = new CommandManager();

    public CommandBacklogTest(String testName)
    {
        super(testName);
    }

    public static Test suite()
    {
        return new TestSuite( CommandBacklogTest.class );
    }

    Backlog item = new Backlog("Backlog_shortname", "fullname", "description", "productowner");
    Story storya = new Story("storyA", "longName", "desc", "JohnH");
    Story storyb = new Story("storyB", "longName", "desc", "JohnH");

    public void test_CreateBacklog() {

        CreateBacklog create = new CreateBacklog(item);
        manager.addCommand(create);
        assertTrue(Workspace.getBacklog(item.uid()).getShortName().equals("Backlog_shortname"));
        manager.undo();
        assertNull(Workspace.getBacklog(item.uid()));
        manager.redo();
        assertTrue(Workspace.getBacklog(item.uid()).getShortName().equals("Backlog_shortname"));
    }

    public void test_DeleteBacklogAssertionError() {

        DeleteBacklog delete = null;
        try {
            delete = new DeleteBacklog(item.uid());
            // we should not get here
            assertTrue(false);
        }
        catch(AssertionError e){

        }
        // delete will be null if the assrtion error is raised.
        assertNull(delete);
    }

    public void test_DeleteBacklog() {
        // DeleteBacklog requires item in workspace, use CreateBacklog...

        Workspace.addStory(storya);
        Workspace.addStory(storyb);

        item.addStory(storya.uid(), 1);
        item.addStory(storyb.uid(), 2);

        CreateBacklog create = new CreateBacklog(item);
        manager.addCommand(create);
        assertTrue(Workspace.getBacklog(item.uid()).getShortName().equals("Backlog_shortname"));

        assertTrue(Workspace.getStory(storya.uid()).isInBacklog());
        assertTrue(Workspace.getStory(storyb.uid()).isInBacklog());

        DeleteBacklog delete = new DeleteBacklog(item.uid());
        manager.addCommand(delete);
        assertNull(Workspace.getBacklog(item.uid()));

        assertFalse(Workspace.getStory(storya.uid()).isInBacklog());
        assertFalse(Workspace.getStory(storyb.uid()).isInBacklog());

        manager.undo();
        assertTrue(Workspace.getBacklog(item.uid()).getShortName().equals("Backlog_shortname"));

        assertTrue(Workspace.getStory(storya.uid()).isInBacklog());
        assertTrue(Workspace.getStory(storyb.uid()).isInBacklog());
        manager.redo();
        assertNull(Workspace.getBacklog(item.uid()));

        assertFalse(Workspace.getStory(storya.uid()).isInBacklog());
        assertFalse(Workspace.getStory(storyb.uid()).isInBacklog());
    }

    public void testChangeName(){

        Workspace.addBacklog(item);

        Backlog testBacklog = Workspace.getBacklog(item.uid());

        manager.addCommand(new ChangeBacklogName(testBacklog, "newFullName"));
        assertEquals("newFullName", testBacklog.getFullName());

        manager.undo();
        assertEquals("fullname", testBacklog.getFullName());

        manager.redo();
        assertEquals("newFullName", testBacklog.getFullName());

    }

    public void testChangeDescription(){

        Workspace.addBacklog(item);

        Backlog testBacklog = Workspace.getBacklog(item.uid());

        manager.addCommand(new ChangeBacklogDescription(testBacklog, "newDescription"));
        assertEquals("newDescription", testBacklog.getDescription());

        manager.undo();
        assertEquals("description", testBacklog.getDescription());

        manager.redo();
        assertEquals("newDescription", testBacklog.getDescription());

    }

    public void testChangeBacklogProductOwner(){

        Workspace.addBacklog(item);

        Backlog testBacklog = Workspace.getBacklog(item.uid());

        manager.addCommand(new ChangeBacklogProductOwner(testBacklog, "newProductOwner"));
        assertEquals("newProductOwner", testBacklog.getProductOwner());

        manager.undo();
        assertEquals("productowner", testBacklog.getProductOwner());

        manager.redo();
        assertEquals("newProductOwner", testBacklog.getProductOwner());

    }

    public void testChangeBacklogScale() {
        Workspace.addStory(storya);
        Workspace.addStory(storyb);

        item.addStory(storya.uid(), 1);
        item.addStory(storyb.uid(), 2);

        for (String storyID : item.getStories()) {
            Story story = Workspace.getStory(storyID);
            story.addAcceptanceCriteria(new AcceptanceCriteria("Test"));
            story.setEstimate(1);
        }

        item.setScaleType(ScaleType.PSEUDOFIBONACCI);

        for (String storyID : item.getStories()) {
            Story story = Workspace.getStory(storyID);
            assertEquals("1/2", story.getEstimateRep());
        }

        manager.addCommand(new ChangeBacklogScale(item, ScaleType.SHIRTSIZES));

        for (String storyID : item.getStories()) {
            Story story = Workspace.getStory(storyID);
            assertEquals("XXXS", story.getEstimateRep());
        }

        manager.undo();

        for (String storyID : item.getStories()) {
            Story story = Workspace.getStory(storyID);
            assertEquals("1/2", story.getEstimateRep());
        }

        manager.redo();

        for (String storyID : item.getStories()) {
            Story story = Workspace.getStory(storyID);
            assertEquals("XXXS", story.getEstimateRep());
        }
    }

    public void testAddStory() {
        storya.setScaleType(ScaleType.PSEUDOFIBONACCI);
        item.setScaleType(ScaleType.DOGBREEDS);

        Workspace.addStory(storya);

        assertEquals(ScaleType.PSEUDOFIBONACCI, storya.getScaleType());

        manager.addCommand(new AddBacklogStory(item, storya.uid(), 1));

        assertEquals(ScaleType.DOGBREEDS, storya.getScaleType());

        manager.undo();

        assertEquals(ScaleType.PSEUDOFIBONACCI, storya.getScaleType());

        manager.redo();

        assertEquals(ScaleType.DOGBREEDS, storya.getScaleType());
    }
}
