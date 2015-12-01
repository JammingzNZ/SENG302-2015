package seng302.group6;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import seng302.group6.Model.Command.CommandManager;
import seng302.group6.Model.Command.Person.*;
import seng302.group6.Model.Command.Skill.CreateSkill;
import seng302.group6.Model.ItemClasses.Person;
import seng302.group6.Model.ItemClasses.Skill;
import seng302.group6.Model.ItemClasses.Workspace;

import java.util.HashMap;

/**
 * Created by simon on 13/03/15.
 */
public class CommandPersonTest extends TestCase
{
    CommandManager manager = new CommandManager();

    HashMap<String, Person> persons = new HashMap<>();

    Person p = new Person("John H.", "Hernandez", "John", "12345678");

    HashMap<String, Skill> skills = new HashMap<>();

    Skill s = new Skill("TS", "Test Skill");

    public CommandPersonTest(String testName)
    {
        super(testName);
    }

    public static Test suite()
    {
        return new TestSuite( CommandPersonTest.class );
    }

    public void testCreate() {
        Person testPerson;

        testPerson = persons.get(p.uid());
        assertNull(testPerson);

        manager.addCommand(new CreatePerson(persons, p));

        testPerson = persons.get(p.uid());
        assertEquals("John H.", testPerson.getShortName());
        assertEquals("Hernandez", testPerson.getLastName());
        assertEquals("John", testPerson.getFirstName());
        assertEquals("12345678", testPerson.getUserID());

        manager.undo();

        testPerson = persons.get(p.uid());
        assertNull(testPerson);

        manager.redo();

        testPerson = persons.get(p.uid());
        assertEquals("John H.", testPerson.getShortName());
        assertEquals("Hernandez", testPerson.getLastName());
        assertEquals("John", testPerson.getFirstName());
        assertEquals("12345678", testPerson.getUserID());
    }

    public void testAddSkill(){
        Workspace.addPerson(p);
        Person testPerson = Workspace.getPerson(p.uid());
        Skill testSkill;
        manager.addCommand(new CreateSkill(skills, s));
        testSkill = skills.get(s.uid());

        manager.addCommand(new AddSkill(testPerson, testSkill.uid()));

        assertTrue(testPerson.getSkills().contains(s.uid()));
        assertTrue(testPerson.getSkills().size() == 1);

        manager.undo();

        assertFalse(testPerson.getSkills().contains(s.uid()));
        assertTrue(testPerson.getSkills().isEmpty());

        manager.redo();

        assertTrue(testPerson.getSkills().contains(s.uid()));
        assertTrue(testPerson.getSkills().size() == 1);

        Workspace.reset();
    }

    public void testChangeName(){
        Workspace.addPerson(p);
        Person testPerson = Workspace.getPerson(p.uid());

        manager.addCommand(new ChangeFirstName(testPerson, "Tim"));

        assertEquals("Tim", testPerson.getFirstName());

        manager.undo();

        assertEquals("John", testPerson.getFirstName());

        manager.redo();

        assertEquals("Tim", testPerson.getFirstName());

        Workspace.reset();

    }

    public void testChangeLastName(){
        Workspace.addPerson(p);
        Person testPerson = Workspace.getPerson(p.uid());

        manager.addCommand(new ChangeLastName(testPerson, "Lopez"));

        assertEquals("Lopez", testPerson.getLastName());

        manager.undo();

        assertEquals("Hernandez", testPerson.getLastName());

        manager.redo();

        assertEquals("Lopez", testPerson.getLastName());

        Workspace.reset();
    }

    public void testChangeUserID(){
        Workspace.addPerson(p);
        Person testPerson = Workspace.getPerson(p.uid());

        manager.addCommand(new ChangeUserID(testPerson, "987654321"));

        assertEquals("987654321", testPerson.getUserID());

        manager.undo();

        assertEquals("12345678", testPerson.getUserID());

        manager.redo();

        assertEquals("987654321", testPerson.getUserID());
        Workspace.reset();
    }

    public void testDeletePerson(){

        Workspace.addPerson(p);

        manager.addCommand(new DeletePerson(p));

        assertTrue(Workspace.getPeople().isEmpty());

        manager.undo();

        assertTrue(Workspace.getPeople().contains(p.uid()));
        assertFalse(Workspace.getPeople().isEmpty());

        manager.redo();

        assertTrue(Workspace.getPeople().isEmpty());
    }

    public void testRemoveSkill(){
        Workspace.addPerson(p);
        Workspace.addSkill(s);
        Person testPerson = Workspace.getPerson(p.uid());
        Skill testSkill = Workspace.getSkill(s.uid());

        manager.addCommand(new AddSkill(testPerson, testSkill.uid()));

        assertTrue(testPerson.getSkills().contains(s.uid()));
        assertFalse(testPerson.getSkills().isEmpty());

        manager.addCommand(new RemoveSkill(testPerson, testSkill.uid()));

        assertTrue(testPerson.getSkills().isEmpty());
        assertFalse(testPerson.getSkills().contains(s.uid()));

        manager.undo();

        assertTrue(testPerson.getSkills().contains(s.uid()));
        assertFalse(testPerson.getSkills().isEmpty());

        manager.redo();

        assertTrue(testPerson.getSkills().isEmpty());
        assertFalse(testPerson.getSkills().contains(s.uid()));

    }
}
