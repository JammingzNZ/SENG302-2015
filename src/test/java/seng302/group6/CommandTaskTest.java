//package seng302.group6;
//
//import junit.framework.Test;
//import junit.framework.TestCase;
//import junit.framework.TestSuite;
//import seng302.group6.Model.Command.CommandManager;
//import seng302.group6.Model.Command.Story.Tasks.AddPersonToTask;
//import seng302.group6.Model.Command.Story.Tasks.ChangeTaskEffort;
//import seng302.group6.Model.Command.Story.Tasks.RemovePersonFromTask;
//import seng302.group6.Model.ItemClasses.*;
//
///**
// * Created by Michael Wheeler on 2/08/15.
// */
//public class CommandTaskTest extends TestCase{
//    CommandManager manager;
//    Task t1;
//    Task t2;
//    Task t3;
//    Sprint s;
//    Person p1;
//    Person p2;
//
//    public CommandTaskTest(String testName)
//    {
//        super(testName);
//    }
//
//    public static Test suite()
//    {
//        return new TestSuite( CommandTaskTest.class );
//    }
//
//    public void setUp() throws Exception
//    {
//        manager = new CommandManager();
//        Workspace.reset();
//
//        t1 = new Task("Test Task 1", "Test Task 1 Description", 12.00);
//        t2 = new Task("Test Task 2", "Test Task 2 Description", 17.50);
//        t3 = new Task("Test Task 3", "Test Task 3 Description", 25.00);
//        s = new Sprint("testShortName", "testFullName", "testDescription");
//
//        p1 = new Person("KB", "Klomp", "Blafter", "kbl42");
//        p2 = new Person("TV", "Tyrus", "Vorran", "tvo980");
//
//        Workspace.addPerson(p1);
//        Workspace.addPerson(p2);
//
//    }
//
//    public void test_addPersonToTask(){
//
//        assertTrue(t1.getPeople().isEmpty());
//
//        manager.addCommand(new AddPersonToTask(s, t1, p1.uid(), null));
//
//        assertEquals(p1.uid(), t1.getPeople().get(0));
//
//        manager.undo();
//
//        assertTrue(t1.getPeople().isEmpty());
//
//        manager.redo();
//
//        assertEquals(p1.uid(), t1.getPeople().get(0));
//    }
//
//    public void test_removePersonFromTask(){
//
//        assertTrue(t1.getPeople().isEmpty());
//
//        t1.addPerson(p1.uid());
//
//        manager.addCommand(new RemovePersonFromTask(s, t1, p1.uid(), null));
//
//        assertTrue(t1.getPeople().isEmpty());
//
//        manager.undo();
//
//        assertEquals(t1.getPeople().get(0), p1.uid());
//
//        manager.redo();
//
//        assertTrue(t1.getPeople().isEmpty());
//    }
//
//    public void test_changeTaskEffort(){
//
//        t1.setEffort(60.00);
//
//        manager.addCommand(new ChangeTaskEffort(s, t1, 30.05, null));
//
//        assertEquals(30.05, t1.getEffort());
//
//        manager.undo();
//
//        assertEquals(60.00, t1.getEffort());
//
//        manager.redo();
//
//        assertEquals(30.05, t1.getEffort());
//
//    }
//}
