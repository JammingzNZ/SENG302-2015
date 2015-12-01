package seng302.group6;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import seng302.group6.Model.ItemClasses.Person;
import seng302.group6.Model.ItemClasses.Team;
import seng302.group6.Model.ItemClasses.Workspace;

/**
 * Created by dan on 25/03/15.
 */
public class TeamTest  extends TestCase{

    Person p = new Person("", "", "", "");

    public TeamTest(String testName)
    {
        super(testName);
    }

    public static Test suite() {return new TestSuite(TeamTest.class);}

    public void testShortName()
    {
        Team t = new Team("Team", "Teams have people in them");
        assertEquals("Team", t.getShortName());
    }

    public void testDescription()
    {
        Team t = new Team("Team", "Teams have people in them");
        assertEquals("Teams have people in them", t.getDescription());
    }

    public void testAddPerson() {
        Team t = new Team("Team", "Teams have people in them");
        Workspace.addPerson(p);
        assertTrue(t.getPeople().isEmpty());
        t.addPerson(p.uid());

        assertFalse(t.getPeople().isEmpty());
        assertTrue(t.getPeople().contains(p.uid()));
    }

    public void testRemovePerson() {
        Team t = new Team("Team", "Teams have people in them");
        Workspace.addPerson(p);
        assertTrue(t.getPeople().isEmpty());
        t.addPerson(p.uid());

        assertFalse(t.getPeople().isEmpty());
        t.removePerson(p.uid());
        assertTrue(t.getPeople().isEmpty());
    }

    public void testSearch() {
        Team t = new Team("Team", "Teams have people in them");

        assertTrue(t.search("AM", false));
        assertTrue(t.search("EOPL", false));
        assertFalse(t.search("BLAH", false));

        assertTrue(t.search("AM", true));
        assertFalse(t.search("EOPL", true));
    }

}






