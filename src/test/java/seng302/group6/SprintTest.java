package seng302.group6;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import seng302.group6.Model.ItemClasses.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;

/**
* Created by Dan Tapp on 28/07/15.
*/
public class SprintTest extends TestCase{
    Sprint sprint;
    Project project;
    String startDate = LocalDate.of(2015, Month.JANUARY, 1).toString();
    String endDate = LocalDate.of(2015, Month.JANUARY, 20).toString();

    Person person;
    final String SHORTNAME = "JohnH";
    final String FIRSTNAME = "John";
    final String LASTNAME = "Hernandez";
    final String USERID = "jhe22";
    final String FULLNAME = "John Hernandez";

    Effort effort1;
    Effort effort2;
    Task task1;
    Story story1;
    LocalDateTime effortStartDate;
    LocalDateTime effortEndDate;

    public SprintTest(String testName)
    {
        super(testName);
    }

    public static Test suite()
    {
        return new TestSuite( SprintTest.class );
    }

    public void setUp() throws Exception
    {
        sprint = new Sprint("sprintTest", "Sprint Test Name", "Sprint Description");
        project = new Project("x", "x", "x");

        person = new Person(SHORTNAME, LASTNAME, FIRSTNAME, USERID);

        Workspace.addProject(project);
        Workspace.addSprint(sprint);
    }

    public void tearDown() throws Exception
    {
        sprint = null;
        person = null;
    }

    public void testShortName()
    {
        assertEquals("sprintTest", sprint.getShortName());
    }

    public void testDescription()
    {
        assertEquals("Sprint Description", sprint.getDescription());
    }

    public void testStartDate()
    {
        sprint.setStartDate(startDate);
        assertEquals("2015-01-01", sprint.getStartDate().toString());
    }

    public void testEndDate()
    {
        sprint.setEndDate(endDate);
        assertEquals("2015-01-20", sprint.getEndDate().toString());
    }

    public void testProjectAllocation() {
        assertNull(sprint.getAssociatedProject());

        sprint.setAssociatedProject(project.uid());

        assertEquals(project, Workspace.getProject(sprint.getAssociatedProject()));

    }

    public void testGetEffortList()
    {
        effortStartDate = LocalDateTime.of(2015, 01, 10, 21, 5);
        effortEndDate = LocalDateTime.of(2015, 01, 10, 22, 5);

        story1 = new Story("Story1", "Story1", "Story Description", person.uid());
        Workspace.addStory(story1);
        task1 = new Task("Task1", "Task Description", 1.0);
        effort1 = new Effort(person.uid(), "Made effort class", effortStartDate, effortEndDate);

        sprint.setStartDate(startDate);
        sprint.setEndDate(endDate);

        sprint.addStory(story1.uid());
        story1.addTask(task1);
        task1.addEffort(effort1);

        assertEquals(0.0, sprint.getEffortList().get("2015-01-01"));
        assertEquals(1.0, sprint.getEffortList().get("2015-01-10"));
        assertEquals(1.0, sprint.getEffortList().get("2015-01-11"));

        effortStartDate = LocalDateTime.of(2015, 01, 12, 21, 5);
        effortEndDate = LocalDateTime.of(2015, 01, 12, 22, 5);

        effort2 = new Effort(person.uid(), "Made effort class", effortStartDate, effortEndDate);
        task1.addEffort(effort2);

        assertEquals(2.0, sprint.getEffortList().get("2015-01-12"));
        assertEquals(2.0, sprint.getEffortList().get("2015-01-20"));
    }

}
