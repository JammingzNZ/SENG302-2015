package seng302.group6;

import junit.framework.TestCase;
import seng302.group6.Model.ItemClasses.*;

public class ProjectTest extends TestCase {

    Project pr = new Project("Scrum Machine", "Soft.Serve(Scrum Machine)", "This app helps manage projects using the Agile Scrum Process");
    Person pe = new Person("John H.", "Hernandez", "John", "12345678");
    Skill sk = new Skill("Tester", "Tests programs");
    Team t = new Team("Test Team", "A team made for testing");

    public void testShortName() {
        assertEquals("Scrum Machine", pr.getShortName());
        pr.setShortName("bork");
        assertEquals("bork", pr.getShortName());
        pr.setShortName("Scrum Machine");
    }

    public void testSearch() {
        assertTrue(pr.search("CRUM", false));
        assertTrue(pr.search("T.S", false));
        assertTrue(pr.search("APP", false));
        assertFalse(pr.search("BLAH", false));

        assertTrue(pr.search("CRUM", true));
        assertFalse(pr.search("T.S", true));
    }

    public void testAssociatedSprints()
    {
        Workspace.reset();

        Workspace.addProject(pr);
        Sprint s1 = new Sprint("", "", "");
        Sprint s2 = new Sprint("", "", "");
        Sprint s3 = new Sprint("", "", "");
        Workspace.addSprint(s1);
        Workspace.addSprint(s2);

        Workspace.addSprint(s3);

        s1.setAssociatedProject(pr.uid());
        s2.setAssociatedProject(pr.uid());

        assertTrue(pr.associatedSprints().contains(s1.uid()));
        assertTrue(pr.associatedSprints().contains(s2.uid()));
        assertFalse(pr.associatedSprints().contains(s3.uid()));

        Workspace.reset();
    }
}