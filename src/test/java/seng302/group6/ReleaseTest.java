package seng302.group6;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import seng302.group6.Model.ItemClasses.Project;
import seng302.group6.Model.ItemClasses.Release;

import java.time.LocalDate;
import java.time.Month;

/**
* Created by Michael Wheeler on 3/05/15.
*/
public class ReleaseTest extends TestCase{
    LocalDate date = LocalDate.of(2015, Month.JANUARY, 1);
    Project testPro = new Project("x", "x", "x");
    String projectID = testPro.uid();
    Release release = new Release("testRelease", "Release v3000", projectID, date.toString());

    public ReleaseTest(String testName)
    {
        super(testName);
    }

    public static Test suite()
    {
        return new TestSuite( ReleaseTest.class );
    }

    public void testShortName()
    {
        assertEquals("testRelease", release.getShortName());
    }

    public void testDescription()
    {
        assertEquals("Release v3000", release.getDescription());
    }

    public void testReleaseDate()
    {
        assertEquals("2015-01-01", release.getReleaseDate().toString());
    }


    public void testSearch() {
        assertTrue(release.search("TRE", false));
        assertTrue(release.search("V3", false));
        assertFalse(release.search("BLAH", false));

        assertTrue(release.search("TRE", true));
        assertFalse(release.search("V3", true));
    }
}
