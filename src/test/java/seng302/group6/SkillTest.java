package seng302.group6;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import seng302.group6.Model.ItemClasses.Skill;

/**
 * Created by dan on 17/03/15.
 */
public class SkillTest extends TestCase {
    Skill s = new Skill("Skill", "Has skill in the skills thing");

    public SkillTest(String testName)
    {
        super(testName);
    }

    public static Test suite()
    {
        return new TestSuite( SkillTest.class );
    }

    public void testShortName() {
        assertEquals("Skill", s.getShortName());
    }

    public void testDescription()
    {
        assertEquals("Has skill in the skills thing", s.getDescription());
    }

    public void testSearch() {
        assertTrue(s.search("SKI", false));
        assertTrue(s.search("HIN", false));
        assertFalse(s.search("BLAH", false));

        assertTrue(s.search("SKI", true));
        assertFalse(s.search("HIN", true));
    }
}
