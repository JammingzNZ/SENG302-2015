package seng302.group6;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import seng302.group6.Model.CyclicDependencyException;
import seng302.group6.Model.EstimationScale.EstimationScale;
import seng302.group6.Model.EstimationScale.PseudoFibonacci;
import seng302.group6.Model.EstimationScale.ScaleType;
import seng302.group6.Model.ItemClasses.*;

import java.time.LocalDateTime;
import java.util.Set;

/**
 * Created by dan on 21/05/15.
 */
public class StoryTest extends TestCase{

    Person p = new Person("Bob", "Charles", "Bob", "bb123");


    String shortName = "Story";
    String longName = "Test Story";
    String description = "The description of the test story";
    String creator = p.uid();
    Story s = new Story(shortName, longName, description, creator);
    Story s2 = new Story("Story2", "Story Two", "Best story ever", creator);
    Story s3 = new Story("Story3", "Story Three", "Best story ever II", creator);

    Task t1 = new Task("Task1", "Description", 3.0);
    Task t2 = new Task("Task2", "Description", 5.0);
    Task t3 = new Task("Task3", "Description", 3.0);

    public StoryTest(String testName)
    {
        super(testName);
    }

    public static Test suite() {
        return new TestSuite((StoryTest.class));
    }

    /**
     * Test that Story can be created and is assigned a uid
     */
    public void testStoryCreation()
    {
        assertTrue(s.uid() != null);
        assertEquals(s.getShortName(), shortName);
        assertEquals(s.getLongName(), longName);
        assertEquals(s.getDescription(), description);
    }

    public void testCreatorLogic()
    {
        Workspace.addPerson(p);
        // If creator still exists
        assertEquals(s.getCreatorName(), Workspace.getPersonName(creator));

        // If creators name changes
        p.setShortName("Barbara");
        assertEquals(s.getCreatorName(), "Barbara");


        // If creator is deleted
        Workspace.removePerson(creator);
        assertEquals(s.getCreatorName(), "Barbara (Deleted)");
    }

    public void testAddStory()
    {
        Workspace.addStory(s);
        assertNotNull(Workspace.getStory(s.uid()));
        Workspace.removeStory(s.uid());
    }

    public void testRemoveStory()
    {
        Workspace.addStory(s);
        assertNotNull(Workspace.getStory(s.uid()));
        Workspace.removeStory(s.uid());
        assertNull(Workspace.getStory(s.uid()));
    }

    public void testGetStories()
    {
        Workspace.reset();
        Workspace.addStory(s);
        Workspace.addStory(s2);
        Workspace.addStory(s3);
        assertTrue(Workspace.getStories().size() == 3);
        assertTrue(Workspace.getStories().contains(s.uid()));
        assertTrue(Workspace.getStories().contains(s2.uid()));
        assertTrue(Workspace.getStories().contains(s3.uid()));
        Workspace.removeStory(s.uid());
        Workspace.removeStory(s2.uid());
        Workspace.removeStory(s3.uid());
    }

    public void testGetStory()
    {
        Workspace.addStory(s);
        assertEquals(Workspace.getStory(s.uid()), s);
        assertEquals(Workspace.getStory(s.uid()).getShortName(), shortName);
        Workspace.removeStory(s.uid());
    }

    public void testGetStoryName()
    {
        Workspace.reset();
        Workspace.addStory(s);
        assertEquals(Workspace.getStoryName(s.uid()), shortName);
        Workspace.removeStory(s.uid());
    }

    public void testGetStoryNames()
    {
        Workspace.addStory(s);
        Workspace.addStory(s2);
        Workspace.addStory(s3);
        assertTrue(Workspace.getStoryNames().size() == 3);
        assertTrue(Workspace.getStoryNames().contains(shortName));
        assertTrue(Workspace.getStoryNames().contains("Story2"));
        assertTrue(Workspace.getStoryNames().contains("Story3"));
        Workspace.removeStory(s.uid());
        Workspace.removeStory(s2.uid());
        Workspace.removeStory(s3.uid());
    }

    /**
     * Tests that ACs added to a story can be retrieved in order
     */
    public void testAddAcceptanceCriteria()
    {
        AcceptanceCriteria ac1 = new AcceptanceCriteria("Must be very good");
        AcceptanceCriteria ac2 = new AcceptanceCriteria("Must be very bad");
        AcceptanceCriteria ac3 = new AcceptanceCriteria("Must be very ok");
        s.addAcceptanceCriteria(ac1);
        s.addAcceptanceCriteria(ac2);
        s.addAcceptanceCriteria(ac3);

        assertEquals(ac1, s.getAcceptanceCriteria(0));
        assertEquals(ac2, s.getAcceptanceCriteria(1));
        assertEquals(ac3, s.getAcceptanceCriteria(2));

        s.deleteAcceptanceCriteria(ac1);
        s.deleteAcceptanceCriteria(ac2);
        s.deleteAcceptanceCriteria(ac3);
    }

    /**
     * Tests that ACs can be deleted from a story
     */
    public void testDeleteAcceptanceCriteria()
    {
        AcceptanceCriteria ac1 = new AcceptanceCriteria("Must be very good");
        AcceptanceCriteria ac2 = new AcceptanceCriteria("Must be very bad");
        AcceptanceCriteria ac3 = new AcceptanceCriteria("Must be very ok");
        s.addAcceptanceCriteria(ac1);
        s.addAcceptanceCriteria(ac2);
        s.addAcceptanceCriteria(ac3);

        s.deleteAcceptanceCriteria(ac1);
        s.deleteAcceptanceCriteria(ac2);
        s.deleteAcceptanceCriteria(ac3);

        assertEquals(0, s.getAllAcceptanceCriteria().size());
    }

    /**
     * Tests that ACs can be moved around and the rest of the list retains
     * the correct order
     */
    public void testMoveAcceptanceCriteria()
    {
        AcceptanceCriteria ac1 = new AcceptanceCriteria("Must be very good");
        AcceptanceCriteria ac2 = new AcceptanceCriteria("Must be very bad");
        AcceptanceCriteria ac3 = new AcceptanceCriteria("Must be very ok");
        s.addAcceptanceCriteria(ac1);
        s.addAcceptanceCriteria(ac2);
        s.addAcceptanceCriteria(ac3);

        s.moveAcceptanceCriteria(0, ac3);
        assertEquals(ac3, s.getAcceptanceCriteria(0));
        assertEquals(ac1, s.getAcceptanceCriteria(1));
        assertEquals(ac2, s.getAcceptanceCriteria(2));

        s.moveAcceptanceCriteria(2, ac3);
        assertEquals(ac1, s.getAcceptanceCriteria(0));
        assertEquals(ac2, s.getAcceptanceCriteria(1));
        assertEquals(ac3, s.getAcceptanceCriteria(2));

        s.moveAcceptanceCriteria(2, ac2);
        assertEquals(ac1, s.getAcceptanceCriteria(0));
        assertEquals(ac3, s.getAcceptanceCriteria(1));
        assertEquals(ac2, s.getAcceptanceCriteria(2));

        s.moveAcceptanceCriteria(0, ac3);
        assertEquals(ac3, s.getAcceptanceCriteria(0));
        assertEquals(ac1, s.getAcceptanceCriteria(1));
        assertEquals(ac2, s.getAcceptanceCriteria(2));

        s.moveAcceptanceCriteria(1, ac2);
        assertEquals(ac3, s.getAcceptanceCriteria(0));
        assertEquals(ac2, s.getAcceptanceCriteria(1));
        assertEquals(ac1, s.getAcceptanceCriteria(2));
    }

    public void testSetGetEstimate() {
        assertNull(s.getEstimate());
        s.addAcceptanceCriteria(new AcceptanceCriteria("aaaaaargh"));
        s.setEstimate(5);
        assertEquals((Integer) 5, s.getEstimate());
        try {
            s.setEstimate(50);
            assert (false);
        }
        catch (IllegalArgumentException e) {
            assert (true);
        }
    }

    public void testSetScaleType() {

        s.addAcceptanceCriteria(new AcceptanceCriteria("aaaaaargh"));
        s.setEstimate(1);

        s.setScaleType(ScaleType.PSEUDOFIBONACCI);
        assertEquals("1/2", s.getEstimateRep());
        s.setScaleType(ScaleType.SHIRTSIZES);
        assertEquals("XXXS", s.getEstimateRep());
        s.setScaleType(ScaleType.DOGBREEDS);
        assertEquals("Chihuahua", s.getEstimateRep());
    }

    public void testGetEstimateScale() {
        s.setScaleType(ScaleType.PSEUDOFIBONACCI);
        EstimationScale scale = s.getEstimationScale();
        EstimationScale fib = new PseudoFibonacci();
        for (int i = 1; i <= 10; i++) {
            assertEquals(fib.getRepresentation(i), scale.getRepresentation(i));
        }
    }

    public void test_addDependency_removeDependency() {
        Workspace.addStory(s);
        Story ss = new Story("sname", "longname", "desc", "creator-uid");
        Workspace.addStory(ss);
        Set<String> sDependencies = s.getDependencies();
        Set<String> ssDependents = ss.getDependents();
        assertEquals(sDependencies.size(), 0);
        assertEquals(ssDependents.size(), 0);
        try {
            s.addDependency(ss.uid());
        }
        catch (CyclicDependencyException e) {
            e.printStackTrace();
        }
        assertTrue(sDependencies.contains(ss.uid()));
        assertTrue(ssDependents.contains(s.uid()));
        s.removeDependency(ss.uid());
        assertEquals(sDependencies.size(), 0);
        assertEquals(ssDependents.size(), 0);
        Workspace.removeStory(s.uid());
        Workspace.removeStory(ss.uid());
    }

    public void test_dependencyHasSuitablePriority_invalid_story() {
        Story ss = new Story("sname", "longname", "desc", "creator-uid");
        assertFalse(s.dependencyHasSuitablePriority(ss));
    }

    public void test_dependencyHasSuitablePriority_valid_story_null_prio() {
        Story ss = new Story("sname", "longname", "desc", "creator-uid");
        assertFalse(s.dependencyHasSuitablePriority(ss));
    }

    public void test_dependencyHasSuitablePriority_valid_story_low_prio() {
        Story ss = new Story("sname", "longname", "desc", "creator-uid");
        s.setCurrentPriority(20);
        ss.setCurrentPriority(10);
        assertFalse(s.dependencyHasSuitablePriority(ss));
    }

    public void test_dependencyHasSuitablePriority_valid_story_high_prio() {
        Story ss = new Story("sname", "longname", "desc", "creator-uid");
        s.setCurrentPriority(10);
        ss.setCurrentPriority(20);
        assertTrue(s.dependencyHasSuitablePriority(ss));
    }

    public void testPriorityConflicts()
    {
        Story s1 = new Story("s", "s", "s", "c");
        Story s2 = new Story("s", "s", "s", "c");
        Story s3 = new Story("s", "s", "s", "c");
        Workspace.addStory(s1);
        Workspace.addStory(s2);
        Workspace.addStory(s3);

        s1.setCurrentPriority(10);
        s2.setCurrentPriority(5);

        try {
            s1.addDependency(s2.uid());
        } catch (CyclicDependencyException e) {
            e.printStackTrace();
        }

        assertTrue(s1.priorityConflicts().contains(s2.uid()));
        assertTrue(s1.priorityConflicts().size() == 1);

        s3.setCurrentPriority(1);

        try {
            s2.addDependency(s3.uid());
            s1.addDependency(s3.uid());
        } catch (CyclicDependencyException e) {
            e.printStackTrace();
        }

        assertTrue(s1.priorityConflicts().contains(s2.uid()) &&
                   s1.priorityConflicts().contains(s3.uid()));
        assertTrue(s1.priorityConflicts().size() == 2);

        assertTrue(s2.priorityConflicts().contains(s3.uid()));
        assertTrue(s2.priorityConflicts().size() == 1);
    }

    public void testCheckCycle() {
        Workspace.addStory(s);
        Workspace.addStory(s2);
        Workspace.addStory(s3);

        try {
            s.addDependency(s2.uid());
            assert(true);
        }
        catch (CyclicDependencyException e) {
            assert(false);
        }

        assertTrue(s.getDependencies().contains(s2.uid()));

        try {
            s2.addDependency(s3.uid());
            assert(true);
        }
        catch (CyclicDependencyException e) {
            assert(false);
        }

        assertTrue(s2.getDependencies().contains(s3.uid()));

        try {
            s3.addDependency(s.uid());
            assert(false);
        }
        catch (CyclicDependencyException e) {
            assert(true);
        }

        assertFalse(s3.getDependencies().contains(s.uid()));

        Workspace.removeStory(s.uid());
        Workspace.removeStory(s2.uid());
        Workspace.removeStory(s3.uid());
    }

    /**
     * Jaln said we need Javadoc for this.
     */
    public void testCheckCycle2() {
        Workspace.addStory(s);
        Workspace.addStory(s2);
        Workspace.addStory(s3);

        assertTrue(s.checkCycle(s.uid()));

        assertFalse(s.checkCycle(s2.uid()));

        assertFalse(s.getDependencies().contains(s2.uid()));

        try {
            s.addDependency(s2.uid());
        }
        catch (CyclicDependencyException e) {}

        assertTrue(s2.checkCycle(s.uid()));

        assertFalse(s2.getDependencies().contains(s.uid()));

        Workspace.removeStory(s.uid());
        Workspace.removeStory(s2.uid());
        Workspace.removeStory(s3.uid());
    }

    public void testCyclicAssociation() {
        Workspace.addStory(s);
        Workspace.addStory(s2);
        Workspace.addStory(s3);

        try {
            s.addDependency(s2.uid());
        }
        catch (CyclicDependencyException e) {
            assert(false);
        }

        try {
            s3.addDependency(s.uid());
        }
        catch (CyclicDependencyException e) {
            assert(false);
        }

        //This causes a cyclic association, and would fail with the old cycle check
        try {
            s3.addDependency(s2.uid());
        }
        catch (CyclicDependencyException e) {
            assert(false);
        }

        assertTrue(s.getDependencies().contains(s2.uid()));
        assertTrue(s3.getDependencies().contains(s.uid()));
        assertTrue(s3.getDependencies().contains(s2.uid()));
    }

    public void test_AddTask(){
        s.addTask(t1);
        s.addTask(t2);
        s.addTask(t3);

        assertEquals(t1, s.getTask(0));
        assertEquals(t2,s.getTask(1));
        assertEquals(t3, s.getTask(2));
    }

    public void test_deleteTask(){
        s.addTask(t1);
        s.addTask(t2);
        s.addTask(t3);

        s.deleteTask(t1);
        s.deleteTask(t2);
        s.deleteTask(t3);

        assertEquals(0, s.getAllTasks().size());
    }

    public void test_moveTask(){
        s.addTask(t1);
        s.addTask(t2);
        s.addTask(t3);

        s.moveTask(0, t3);
        assertEquals(t3, s.getTask(0));
        assertEquals(t1, s.getTask(1));
        assertEquals(t2, s.getTask(2));

        s.moveTask(2, t3);
        assertEquals(t1, s.getTask(0));
        assertEquals(t2, s.getTask(1));
        assertEquals(t3, s.getTask(2));

        s.moveTask(2, t2);
        assertEquals(t1, s.getTask(0));
        assertEquals(t3, s.getTask(1));
        assertEquals(t2, s.getTask(2));

    }

    public void testSearch() {
        assertTrue(s.search("TOR", false));
        assertTrue(s.search("EST", false));
        assertTrue(s.search("TION", false));
        assertFalse(s.search("BLAH", false));

        assertTrue(s.search("TOR", true));
        assertFalse(s.search("EST", true));
    }

    public void testGetProgress()
    {
        Person person = new Person("Test Person", "Test Person", "Test", "tp");

        LocalDateTime effortStartDate = LocalDateTime.of(2015, 01, 10, 21, 5);
        LocalDateTime effortEndDate = LocalDateTime.of(2015, 01, 10, 22, 5);
        Effort effort1 = new Effort(person.uid(), "Made effort class", effortStartDate, effortEndDate);

        effortStartDate = LocalDateTime.of(2015, 01, 12, 21, 5);
        effortEndDate = LocalDateTime.of(2015, 01, 12, 22, 5);
        Effort effort2 = new Effort(person.uid(), "Made effort class", effortStartDate, effortEndDate);

        s.addTask(t1);
        t1.addEffort(effort1);

        t1.setEffortLeft(4.0);
        assertEquals(0.2, s.getProgress());
        t1.setEffortLeft(1.0);
        assertEquals(0.5, s.getProgress());
        t1.setEffortLeft(0.0);
        assertEquals(1.0, s.getProgress());
        t1.clearEffort();
        assertEquals(0.0, s.getProgress());
    }

    public void testGetProgressTooltipString()
    {
        Person person = new Person("Test Person", "Test Person", "Test", "tp");

        LocalDateTime effortStartDate = LocalDateTime.of(2015, 01, 10, 21, 5);
        LocalDateTime effortEndDate = LocalDateTime.of(2015, 01, 10, 22, 5);
        Effort effort1 = new Effort(person.uid(), "Made effort class", effortStartDate, effortEndDate);

        effortStartDate = LocalDateTime.of(2015, 01, 12, 22, 5);
        effortEndDate = LocalDateTime.of(2015, 01, 12, 22, 40);
        Effort effort2 = new Effort(person.uid(), "Made effort class", effortStartDate, effortEndDate);

        s.addTask(t1);
        t1.addEffort(effort1);

        t1.setEffortLeft(4.0);
        assertEquals("Progress: 20%\n" +
                "Effort Spent: 1 hour(s) and 0 minute(s)\n" +
                "Effort Left: 4 hour(s) and 0 minute(s)\n", s.getProgressTooltipString());
        t1.setEffortLeft(1.0);
        assertEquals("Progress: 50%\n" +
                "Effort Spent: 1 hour(s) and 0 minute(s)\n" +
                "Effort Left: 1 hour(s) and 0 minute(s)\n", s.getProgressTooltipString());
        t1.setEffortLeft(0.0);
        assertEquals("Progress: 100%\n" +
                "Effort Spent: 1 hour(s) and 0 minute(s)\n" +
                "Effort Left: 0 hour(s) and 0 minute(s)\n", s.getProgressTooltipString());
        t1.clearEffort();
        t1.setEffortLeft(3.0);
        assertEquals("Progress: 0%\n" +
                "Effort Spent: 0 hour(s) and 0 minute(s)\n" +
                "Effort Left: 3 hour(s) and 0 minute(s)\n", s.getProgressTooltipString());
        t1.setEffortLeft(0.0);
        assertEquals("Progress: 0%\n" +
                "Effort Spent: 0 hour(s) and 0 minute(s)\n" +
                "Effort Left: 0 hour(s) and 0 minute(s)\n", s.getProgressTooltipString());
        t1.setEffortLeft(2.3);
        t1.addEffort(effort2);
        assertEquals("Progress: 20.23%\n" +
                "Effort Spent: 0 hour(s) and 35 minute(s)\n" +
                "Effort Left: 2 hour(s) and 18 minute(s)\n", s.getProgressTooltipString());
        t1.setEffortLeft(5.8);
        t1.addEffort(effort2);
        t1.addEffort(effort2);
        assertEquals("Progress: 23.18%\n" +
                "Effort Spent: 1 hour(s) and 45 minute(s)\n" +
                "Effort Left: 5 hour(s) and 48 minute(s)\n", s.getProgressTooltipString());
    }

}

