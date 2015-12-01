//package seng302.group6;
//
//import junit.framework.Test;
//import junit.framework.TestCase;
//import junit.framework.TestSuite;
//import seng302.group6.Model.ItemClasses.Person;
//import seng302.group6.Model.ItemClasses.Task;
//import seng302.group6.Model.ItemClasses.Workspace;
//import seng302.group6.Model.Status.StatusType;
//
///**
// * Created by Michael Wheeler on 24/07/15.
// */
//public class TaskTest extends TestCase{
//
//    Task task;
//
//    final String SHORTNAME = "Test Task short name";
//    final String DESCRIPTION = "Test Task description";
//    final Double ESTIMATION = 17.50;
//    final Double EFFORT = 0.0;
//    final Person TESTPERSON = new Person("TaskTester", "Tester", "Task", "tt001");
//
//
//    public TaskTest(String testName)
//    {
//        super(testName);
//    }
//
//    public static Test suite()
//    {
//        return new TestSuite( TaskTest.class );
//    }
//
//    public void setUp() throws Exception{
//        task = new Task(SHORTNAME, DESCRIPTION, ESTIMATION);
//        task.setEffort(EFFORT);
//        Workspace.addPerson(TESTPERSON);
//    }
//
//    public void tearDown() throws Exception
//    {
//        task = null;
//    }
//
//    public void test_shortName_not_null(){
//        try{
//            task.setShortName(null);
//            assertEquals(task.getShortName(), SHORTNAME);
//        }catch(IllegalArgumentException e){}
//    }
//
//    public void test_setStatus(){
//        assertEquals(task.getStatus().toString(), "Not Started");
//        task.setStatus(StatusType.BLOCKED);
//        assertEquals(task.getStatus().toString(), "Blocked");
//        task.setStatus(StatusType.DONE);
//        assertEquals(task.getStatus().toString(), "Done");
//    }
//
//    public void test_setShortName(){
//        assertEquals(task.getShortName(), "Test Task short name");
//        task.setShortName("new Test Task short name");
//        assertEquals(task.getShortName(), "new Test Task short name");
//    }
//
//    public void test_setDescription(){
//        assertEquals(task.getDescription(), "Test Task description");
//        task.setDescription("new Test Task description");
//        assertEquals(task.getDescription(), "new Test Task description");
//    }
//
//    public void test_setEstimation(){
//        assertEquals(task.getEstimation(), 17.50);
//        task.setEstimation(20.7);
//        assertEquals(task.getEstimation(), 20.7);
//    }
//
//    public void test_setEffort(){
//        assertEquals(task.getEffort(), 0.0);
//        task.setEffort(10.0);
//        assertEquals(task.getEffort(), 10.0);
//    }
//
//    public void test_addEffort(){
//        assertEquals(task.getEffort(), 0.0);
//        task.addEffort(10.0);
//        assertEquals(task.getEffort(), 10.0);
//        task.addEffort(2.7);
//        assertEquals(task.getEffort(), 12.7);
//    }
//
//    public void test_clearEffort(){
//        task.addEffort(13.2);
//        assertEquals(task.getEffort(), 13.2);
//        task.clearEffort();
//        assertEquals(task.getEffort(), 0.0);
//    }
//    public void test_addPerson(){
//        assertTrue(task.getPeople().isEmpty());
//        task.addPerson(TESTPERSON.uid());
//        assertTrue(task.getPeople().contains(TESTPERSON.uid()));
//    }
//
//    public void test_removePerson(){
//
//        task.addPerson(TESTPERSON.uid());
//        assertTrue(task.getPeople().contains(TESTPERSON.uid()));
//        task.removePerson(TESTPERSON.uid());
//        assertTrue(task.getPeople().isEmpty());
//    }
//
//
//}
