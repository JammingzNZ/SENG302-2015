package seng302.group6;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import seng302.group6.Model.ItemClasses.Effort;
import seng302.group6.Model.ItemClasses.Person;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by simon on 13/03/15.
 */
public class EffortTest extends TestCase
{
    Person person;
    final String SHORTNAME = "JohnH";
    final String FIRSTNAME = "John";
    final String LASTNAME = "Hernandez";
    final String USERID = "jhe22";
    final String FULNAME = "John Hernandez";

    Effort effort1;
    LocalDateTime startDate;
    LocalDateTime endDate;

    public EffortTest(String testName)
    {
        super(testName);
    }

    public static Test suite()
    {
        return new TestSuite( EffortTest.class );
    }

    public void setUp() throws Exception
    {
        person = new Person(SHORTNAME, LASTNAME, FIRSTNAME, USERID);
    }

    public void tearDown() throws Exception
    {
        person = null;
    }

    public void testConstructor() {
        startDate = LocalDateTime.of(1999, 12, 3, 21, 5);
        endDate = LocalDateTime.of(1999, 12, 3, 22, 10);
        effort1 = new Effort(person.uid(), "Made effort class", startDate, endDate);
        assertEquals(65, effort1.getMinutes());

        startDate = LocalDateTime.of(1999, 12, 3, 0, 0);
        endDate = LocalDateTime.of(1999, 12, 4, 0, 0);
        effort1 = new Effort(person.uid(), "Made effort class", startDate, endDate);
        assertEquals(24*60, effort1.getMinutes());

        startDate = LocalDateTime.of(1999, 12, 3, 23, 30);
        endDate = LocalDateTime.of(1999, 12, 4, 1, 0);
        effort1 = new Effort(person.uid(), "Made effort class", startDate, endDate);
        assertEquals(90, effort1.getMinutes());

        effort1 = new Effort(person.uid(), "Made effort class", startDate, 135);
        assertEquals(135, effort1.getMinutes());
    }
}
