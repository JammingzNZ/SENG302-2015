package seng302.group6;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import seng302.group6.Model.EstimationScale.ScaleType;
import seng302.group6.Model.ItemClasses.*;

import java.util.ArrayList;

/**
 * Created by simon on 13/03/15.
 */
public class BacklogTest extends TestCase
{
    Backlog item;
    final String TOO_LONG_SHORT_NAME = "abcdefghijklmnopqrstuvwxyzABCDEFG";
    final String TOO_SHORT_SHORT_NAME = "";
    final String SHORTNAME = "shortname";
    final String FULLNAME = "fullname";
    final String DESCRIPTION = "description";
    final String PRODUCTOWNER = "productowner";

    Story storya = new Story("storyA", "longName", "desc", "JohnH");
    Story storyb = new Story("storyB", "longName", "desc", "JohnH");
    Story storyc = new Story("storyC", "longName", "desc", "JohnH");

    public BacklogTest(String testName)
    {
        super(testName);
    }

    public static Test suite()
    {
        return new TestSuite( BacklogTest.class );
    }

    public void setUp() throws Exception
    {
        item = new Backlog(SHORTNAME, FULLNAME, DESCRIPTION, PRODUCTOWNER);
        Workspace.addStory(storya);
        Workspace.addStory(storyb);
        Workspace.addStory(storyc);
        storya.addAcceptanceCriteria(new AcceptanceCriteria("Test"));
    }

    public void tearDown() throws Exception
    {
        item = null;
    }

    public void test_setShortName_getShortName() {
        assertEquals(SHORTNAME, item.getShortName());
    }

    public void test_getFullName()
    {
        assertEquals(FULLNAME, item.getFullName());
    }

    public void test_getDescription()
    {
        assertEquals(DESCRIPTION, item.getDescription());
    }

    public void test_getProductOwner()
    {
        assertEquals(PRODUCTOWNER, item.getProductOwner());
    }

    public void test_setFullName()
    {
        item.setFullName("new full name");
        assertEquals("new full name", item.getFullName());
    }

    public void test_setDescription()
    {
        item.setDescription("new desc");
        assertEquals("new desc", item.getDescription());
    }

    public void test_setProductOwner()
    {
        item.setProductOwner("new po");
        assertEquals("new po", item.getProductOwner());
    }

    public void test_getStories_default()
    {
        ArrayList<String> stories = item.getStories();
        assertEquals(stories.size(), 0);
    }

    public void test_addStory()
    {
        item.addStory(storya.uid(), 26);
        item.addStory(storyb.uid(), 27);
        item.addStory(storyc.uid(), 28);

        assertTrue(item.getStories().contains(storya.uid()));
        assertTrue(item.getStories().contains(storyb.uid()));
        assertTrue(item.getStories().contains(storyc.uid()));

        assertTrue(Workspace.getStory(item.getStories().get(0)).isInBacklog());
        assertTrue(Workspace.getStory(item.getStories().get(1)).isInBacklog());
        assertTrue(Workspace.getStory(item.getStories().get(2)).isInBacklog());

    }

    public void test_addStory_assertion_error()
    {

        item.addStory(storya.uid(), 1);
        try {
            item.addStory(storya.uid(), 1);
            assertFalse(true);
        }
        catch(AssertionError e){

        }
    }

    public void test_getStoryPriority()
    {

        item.addStory(storya.uid(), 1);
        item.addStory(storyb.uid(), 3);
        item.addStory(storyc.uid(), 4);
        assertTrue(item.getStoryPriority(storya.uid()) == 1);
        assertTrue(item.getStoryPriority(storyb.uid()) == 3);
        assertTrue(item.getStoryPriority(storyc.uid()) == 4);

    }

    public void test_getStoryPriority_assertion_error()
    {

        item.addStory(storya.uid(), 1);
        try {
            item.getStoryPriority(storyb.uid());
            assertFalse(true);
        }
        catch(AssertionError e){

        }
    }
//
    public void test_setStoryPriority()
    {

        item.addStory(storya.uid(), 1);
        item.addStory(storyb.uid(), 3);
        item.addStory(storyc.uid(), 4);

        assertTrue(item.getStoryPriority(storya.uid()) == 1);
        assertTrue(item.getStoryPriority(storyb.uid()) == 3);
        assertTrue(item.getStoryPriority(storyc.uid()) == 4);


        item.setStoryPriority(storya.uid(), 5);
        item.setStoryPriority(storyb.uid(), 6);
        item.setStoryPriority(storyc.uid(), 7);

        assertTrue(item.getStoryPriority(storya.uid()) == 5);
        assertTrue(item.getStoryPriority(storyb.uid()) == 6);
        assertTrue(item.getStoryPriority(storyc.uid()) == 7);
    }

    public void test_setStoryPriority_assertion_error()
    {

        item.addStory(storya.uid(), 1);
        try {
            item.setStoryPriority(storyb.uid(), 2);
            assertFalse(true);
        }
        catch(AssertionError e){

        }
    }

    public void testSetScaleType() {
        for (String storyID : item.getStories()) {
            Story story = Workspace.getStory(storyID);
            story.setEstimate(1);
        }
        item.setScaleType(ScaleType.PSEUDOFIBONACCI);
        for (String storyID : item.getStories()) {
            Story story = Workspace.getStory(storyID);
            assertEquals("1/2", story.getEstimateRep());
        }
        item.setScaleType(ScaleType.SHIRTSIZES);
        for (String storyID : item.getStories()) {
            Story story = Workspace.getStory(storyID);
            assertEquals("XXXS", story.getEstimateRep());
        }
        item.setScaleType(ScaleType.DOGBREEDS);
        for (String storyID : item.getStories()) {
            Story story = Workspace.getStory(storyID);
            assertEquals("Chihuahua", story.getEstimateRep());
        }
    }

    public void testAddStoryScale() {
        item.removeStory(storya.uid());
        item.setScaleType(ScaleType.DOGBREEDS);
        storya.setScaleType(ScaleType.SHIRTSIZES);
        storya.setEstimate(1);
        item.addStory(storya.uid(), 1);
        assertEquals("Chihuahua", storya.getEstimateRep());
    }

    public void testSearch() {
        assertTrue(item.search("ORTN", false));
        assertTrue(item.search("LLN", false));
        assertTrue(item.search("ESCRI", false));
        assertFalse(item.search("BLAH", false));

        assertTrue(item.search("ORTN", true));
        assertFalse(item.search("LLN", true));
    }
}
