package seng302.group6;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import seng302.group6.Model.Command.CommandManager;
import seng302.group6.Model.Command.Team.*;
import seng302.group6.Model.ItemClasses.Person;
import seng302.group6.Model.ItemClasses.Team;
import seng302.group6.Model.ItemClasses.Workspace;

import java.util.HashMap;

/**
* Created by simon on 13/03/15.
*/
public class CommandTeamTest extends TestCase
{
    CommandManager manager = new CommandManager();

    HashMap<String, Team> teams = new HashMap<>();

    Team t = new Team("Test", "A team for testing.");
    Person p1 = new Person("KB", "Klomp", "Blafter", "kbl42");
    Person p2 = new Person("TV", "Tyrus", "Vorran", "tvo980");

    public CommandTeamTest(String testName)
    {
        super(testName);
    }

    public static Test suite()
    {
        return new TestSuite( CommandTeamTest.class );
    }

    public void setUp() throws Exception
    {
        Workspace.reset();
        Workspace.addPerson(p1);
        Workspace.addPerson(p2);
    }

    public void tearDown() throws Exception
    {

    }
    public void testCreate() {
        Team testTeam;

        testTeam = teams.get(t.uid());
        assertNull(testTeam);

        manager.addCommand(new CreateTeam(teams, t));

        testTeam = teams.get(t.uid());
        assertEquals("Test", testTeam.getShortName());
        assertEquals("A team for testing.", testTeam.getDescription());

        manager.undo();

        testTeam = teams.get(t.uid());
        assertNull(testTeam);

        manager.redo();

        testTeam = teams.get(t.uid());
        assertEquals("Test", testTeam.getShortName());
        assertEquals("A team for testing.", testTeam.getDescription());
    }

    public void testAddDeveloper(){
        assertTrue(t.getDevelopers().isEmpty());
        t.addPerson(p1.uid());
        manager.addCommand(new AddDeveloper(t, p1.uid()));
        assertTrue(t.getDevelopers().contains(p1.uid()));
        assertFalse(t.getDevelopers().isEmpty());
        manager.undo();
        assertTrue(t.getDevelopers().isEmpty());
        manager.redo();
        assertTrue(t.getDevelopers().contains(p1.uid()));
        assertFalse(t.getDevelopers().isEmpty());
    }

    public void testAddMember(){
        assertTrue(t.getPeople().isEmpty());
        manager.addCommand(new AddMember(t, p1.uid()));
        assertTrue(t.getPeople().contains(p1.uid()));
        assertFalse(t.getPeople().isEmpty());
        manager.undo();
        assertTrue(t.getPeople().isEmpty());
        manager.redo();
        assertTrue(t.getPeople().contains(p1.uid()));
        assertFalse(t.getPeople().isEmpty());
    }

    public void testChangeProductOwner(){
        t.setProductOwner(p2.uid());
        assertEquals(p2.uid(), t.getProductOwner());
        manager.addCommand(new ChangeProductOwner(t, p1.uid()));
        assertEquals(p1.uid(), t.getProductOwner());
        manager.undo();
        assertEquals(p2.uid(), t.getProductOwner());
        manager.redo();
        assertEquals(p1.uid(), t.getProductOwner());
    }

    public void testChangeScrumMaster(){
        t.setScrumMaster(p2.uid());
        assertEquals(p2.uid(), t.getScrumMaster());
        manager.addCommand(new ChangeScrumMaster(t, p1.uid()));
        assertEquals(p1.uid(), t.getScrumMaster());
        manager.undo();
        assertEquals(p2.uid(), t.getScrumMaster());
        manager.redo();
        assertEquals(p1.uid(), t.getScrumMaster());
    }

    public void testChangeDescription(){
        assertEquals("A team for testing.", t.getDescription());
        manager.addCommand(new ChangeTeamDescription(t, "Best team ever"));
        assertEquals("Best team ever", t.getDescription());
        manager.undo();
        assertEquals("A team for testing.", t.getDescription());
        manager.redo();
        assertEquals("Best team ever", t.getDescription());
    }

    public void testDeleteTeam(){
        Workspace.addTeam(t);
        Team testTeam = Workspace.getTeam(t.uid());
        assertTrue(Workspace.getTeams().contains(t.uid()));
        assertFalse(Workspace.getTeams().isEmpty());
        manager.addCommand(new DeleteTeam(t));
        assertTrue(Workspace.getTeams().isEmpty());
        assertFalse(Workspace.getTeams().contains(t.uid()));
        manager.undo();
        assertTrue(Workspace.getTeams().contains(t.uid()));
        assertFalse(Workspace.getTeams().isEmpty());
        manager.redo();
        assertTrue(Workspace.getTeams().isEmpty());
        assertFalse(Workspace.getTeams().contains(t.uid()));
    }

    public void testRemoveDeveloper(){
        Workspace.addTeam(t);
        t.addPerson(p1.uid());
        t.addDeveloper(p1.uid());
        assertTrue(Workspace.getTeam(t.uid()).getDevelopers().contains(p1.uid()));
        manager.addCommand(new RemoveDeveloper(t, p1.uid()));
        assertFalse(Workspace.getTeam(t.uid()).getDevelopers().contains(p1.uid()));
        assertTrue(Workspace.getTeam(t.uid()).getDevelopers().isEmpty());
        manager.undo();
        assertTrue(Workspace.getTeam(t.uid()).getDevelopers().contains(p1.uid()));
        manager.redo();
        assertFalse(Workspace.getTeam(t.uid()).getDevelopers().contains(p1.uid()));
        assertTrue(Workspace.getTeam(t.uid()).getDevelopers().isEmpty());
    }

    public void testRemoveMember(){
        t.addPerson(p1.uid());
        assertTrue(t.getPeople().contains(p1.uid()));
        assertFalse(t.getPeople().isEmpty());
        manager.addCommand(new RemoveMember(t, p1.uid()));
        assertTrue(t.getPeople().isEmpty());
        assertFalse(t.getPeople().contains(p1.uid()));
        manager.undo();
        assertTrue(t.getPeople().contains(p1.uid()));
        assertFalse(t.getPeople().isEmpty());
        manager.redo();
        assertTrue(t.getPeople().isEmpty());
        assertFalse(t.getPeople().contains(p1.uid()));

    }
}
