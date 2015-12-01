package seng302.group6;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import seng302.group6.Model.ItemClasses.Person;

import java.util.ArrayList;

/**
 * Created by simon on 13/03/15.
 */
public class PersonTest extends TestCase
{
    Person item;
    final String SHORTNAME = "JohnH";
    final String FIRSTNAME = "John";
    final String LASTNAME = "Hernandez";
    final String USERID = "jhe22";
    final String FULNAME = "John Hernandez";


    public PersonTest(String testName)
    {
        super(testName);
    }

    public static Test suite()
    {
        return new TestSuite( PersonTest.class );
    }

    public void setUp() throws Exception
    {
        item = new Person(SHORTNAME, LASTNAME, FIRSTNAME, USERID);
    }

    public void tearDown() throws Exception
    {
        item = null;
    }

    public void testShortName() {
        assertEquals(SHORTNAME, item.getShortName());
    }

    public void testFullName()
    {
        assertEquals(FULNAME, item.getFullName());
    }

    public void test_getFirstName_setFirstName()
    {
        assertEquals(FIRSTNAME,  item.getFirstName());
        item.setFirstName("peewee");
        assertEquals("peewee", item.getFirstName());
    }

    public void test_getLastName_setLastName()
    {
        assertEquals(LASTNAME, item.getLastName());
        item.setLastName("herman");
        assertEquals("herman", item.getLastName());
    }

    public void test_getUserID_setUserID()
    {
        assertEquals(USERID, item.getUserID());
        item.setUserID("jhe23");
        assertEquals("jhe23", item.getUserID());
    }

    public void test_getSkills_setSkills()
    {
        ArrayList<String> skills = new ArrayList<String>();
        assertEquals(skills, item.getSkills());
        skills.add("35593959");
        skills.add("dfgdfg");
        item.setSkills(skills);
        assertEquals(skills, item.getSkills());
    }

    public void test_addSkill_removeSkill()
    {
        ArrayList<String> skills = new ArrayList<String>();
        assertEquals(item.getSkills().size(), 0);
        item.addSkill("skill-1-uid");
        item.addSkill("skill-2-uid");
        assertEquals(item.getSkills().size(), 2);
        item.removeSkill("skill-2-uid");
        assertEquals(item.getSkills().size(), 1);
        assertTrue(item.getSkills().contains("skill-1-uid"));
    }

    public void test_joinedTeam_leftTeam_isInTeam()
    {
        assertFalse(item.isInTeam());
        item.joinedTeam();
        assertTrue(item.isInTeam());
        item.leftTeam();
        assertFalse(item.isInTeam());
    }

    public void testSearch() {
        assertTrue(item.search("HNH", false));
        assertTrue(item.search("HERNA", false));
        assertTrue(item.search("HE2", false));
        assertTrue(item.search("OHN", false));
        assertFalse(item.search("BLAH", false));

        assertTrue(item.search("HNH", true));
        assertFalse(item.search("HERNA", true));
    }
}
