package seng302.group6;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import seng302.group6.Model.Command.CommandManager;
import seng302.group6.Model.Command.Project.ChangeBacklog;
import seng302.group6.Model.Command.Project.ChangeProjectDescription;
import seng302.group6.Model.Command.Project.ChangeProjectName;
import seng302.group6.Model.Command.Project.CreateProject;
import seng302.group6.Model.ItemClasses.Backlog;
import seng302.group6.Model.ItemClasses.Project;
import seng302.group6.Model.ItemClasses.Workspace;

import java.util.HashMap;

/**
 * Created by simon on 13/03/15.
 */
public class CommandProjectTest extends TestCase
{
    CommandManager manager = new CommandManager();

    HashMap<String, Project> projects = new HashMap<>();

    Project p = new Project("Scrum Machine", "Soft.Serve(Scrum Machine)",
            "This app helps manage projects using the Agile Scrum Process");

    public CommandProjectTest(String testName)
    {
        super(testName);
    }

    public static Test suite()
    {
        return new TestSuite( CommandProjectTest.class );
    }

    public void testAdd() {
        Project testProject;

        testProject = Workspace.getProject(p.uid());
        assertNull(testProject);

        manager.addCommand(new CreateProject(projects, p));

        testProject = Workspace.getProject(p.uid());
        assertEquals("Scrum Machine", testProject.getShortName());
        assertEquals("Soft.Serve(Scrum Machine)", testProject.getLongName());
        assertEquals("This app helps manage projects using the Agile Scrum Process", testProject.getDescription());

        manager.undo();

        testProject = Workspace.getProject(p.uid());
        assertNull(testProject);

        manager.redo();

        testProject = Workspace.getProject(p.uid());
        assertEquals("Scrum Machine", testProject.getShortName());
        assertEquals("Soft.Serve(Scrum Machine)", testProject.getLongName());
        assertEquals("This app helps manage projects using the Agile Scrum Process", testProject.getDescription());
    }

    public void testGroup() {
        Project testProject;
        projects.put(p.getShortName(), p);

        testProject = projects.get(p.getShortName());
        assertEquals("Scrum Machine", testProject.getShortName());
        assertEquals("Soft.Serve(Scrum Machine)", testProject.getLongName());
        assertEquals("This app helps manage projects using the Agile Scrum Process", testProject.getDescription());

        manager.startGroupCommand();
            manager.addCommand(new ChangeProjectName(p, "Tester Project"));
            manager.addCommand(new ChangeProjectDescription(p, "This project is a test"));
        manager.endGroupCommand();

        testProject = projects.get(p.getShortName());
        //assertEquals("TestProject", testProject.getShortName());
        assertEquals("Tester Project", testProject.getLongName());
        assertEquals("This project is a test", testProject.getDescription());

        manager.undo();

        testProject = projects.get(p.getShortName());
        assertEquals("Scrum Machine", testProject.getShortName());
        assertEquals("Soft.Serve(Scrum Machine)", testProject.getLongName());
        assertEquals("This app helps manage projects using the Agile Scrum Process", testProject.getDescription());

        manager.redo();

        testProject = projects.get(p.getShortName());
        //assertEquals("TestProject", testProject.getShortName());
        assertEquals("Tester Project", testProject.getLongName());
        assertEquals("This project is a test", testProject.getDescription());

    }

    public void testBacklog() {
        Project testProject;
        Workspace.addProject(p);
        testProject = Workspace.getProject(p.uid());

        // project does not have a backlog
        assertNull(testProject.getBacklog());

        Backlog testBacklog = new Backlog("Backlog", "Test Backlog", "Backlog for testing", null);
        Workspace.addBacklog(testBacklog);
        // set backlog 1 to project
        manager.addCommand(new ChangeBacklog(testProject, testBacklog.uid()));
        assertEquals(testBacklog.uid(), testProject.getBacklog());

        // undo set backlog 1 (backlog now null)
        manager.undo();
        assertNull(testProject.getBacklog());

        // redo set backlog 1 (backlog 1 now allocated)
        manager.redo();
        assertEquals(testBacklog.uid(), testProject.getBacklog());

        Backlog testBacklog2 = new Backlog("Backlog2", "Test Backlog 2", "2nd Backlog for testing", null);
        Workspace.addBacklog(testBacklog2);
        // set backlog 2 to project
        manager.addCommand(new ChangeBacklog(testProject, testBacklog2.uid()));
        assertEquals(testBacklog2.uid(), testProject.getBacklog());

        // undo set backlog 2 (backlog 1 now allocated)
        manager.undo();
        assertEquals(testBacklog.uid(), testProject.getBacklog());

    }
}
