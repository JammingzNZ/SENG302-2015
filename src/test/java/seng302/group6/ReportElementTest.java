package seng302.group6;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.jdom2.Element;
import org.jdom2.output.XMLOutputter;
import seng302.group6.Model.EstimationScale.ScaleType;
import seng302.group6.Model.ItemClasses.*;
import seng302.group6.Model.Report.Elements.*;

import java.time.LocalDate;
import java.time.Month;

/**
 * Created by Michael Wheeler on 28/05/15.
 */
public class ReportElementTest extends TestCase{

    Person p1 = new Person("JohnH", "Hernandez", "John", "jhe22");
    Person p2 = new Person("JillA", "Anderson", "Jill", "jdg09");
    Project pr = new Project("Scrum Machine", "Soft.Serve(Scrum Machine)", "This app helps manage projects using the Agile Scrum Process");
    Team t = new Team("Team", "Teams have people in them");
    Skill s = new Skill("Skill", "Has skill in the skills thing");
    Story story = new Story("Story", "This is a Story", "Once upon a time...", p1.uid());
    XMLOutputter xm = new XMLOutputter();

    LocalDate date = LocalDate.of(2015, Month.JANUARY, 1);
    Release release = new Release("testRelease", "Release v3000", "testProject", date.toString());

    public ReportElementTest(String testName) { super(testName); }

    public static Test suite() { return new TestSuite(ReportElementTest.class); }

    public void testPersonElement(){
        Workspace.addPerson(p1);
        PersonElement personElement = new PersonElement();
        Element personEl = personElement.getPerson(p1.uid());

        String output = xm.outputString(personEl);

        String expected = "<Person ShortName=\"JohnH\" FullName=\"John Hernandez\" UserID=\"jhe22\" />";
        assertEquals(expected, output);

    }

    public void testProjectElement(){
        Workspace.addProject(pr);
        ProjectElement projectElement = new ProjectElement();
        Element projectEl = projectElement.getProject(pr.uid());

        String output = xm.outputString(projectEl);

        String expected = "<Project ShortName=\"Scrum Machine\" LongName=\"Soft.Serve(Scrum Machine)\" Description=\"This app helps manage projects using the Agile Scrum Process\" />";
        assertTrue(output.equals(expected));
    }

    public void testTeamElement()
    {
        Workspace.addTeam(t);
        Workspace.addPerson(p1);
        Workspace.addPerson(p2);
        TeamElement teamElement = new TeamElement();
        Element teamEl = teamElement.getTeam(t.uid());

        String output = xm.outputString(teamEl);

        String expected = "<Team ShortName=\"Team\" Description=\"Teams have people in them\" Product-Owner=\"\" Scrum-Master=\"\" />";
        assertTrue(output.equals(expected));

        t.setScrumMaster(p1.uid());
        t.setProductOwner(p2.uid());
        teamEl = teamElement.getTeam(t.uid());
        output = xm.outputString(teamEl);
        expected = "<Team ShortName=\"Team\" Description=\"Teams have people in them\" Product-Owner=\"JillA\" Scrum-Master=\"JohnH\" />";

        assertTrue(output.equals(expected));
    }

    public void testSkillElement(){
        Workspace.addSkill(s);
        SkillElement skillElement = new SkillElement();
        Element skillEl = skillElement.getSkill(s.uid());

        String output = xm.outputString(skillEl);
        String expected = "<Skill ShortName=\"Skill\" Description=\"Has skill in the skills thing\" />";

        assertTrue(output.equals(expected));
    }

    public void testReleaseElement(){
        Workspace.addRelease(release);
        ReleaseElement releaseElement = new ReleaseElement();
        Element releaseEl = releaseElement.getRelease(release.uid());

        String output = xm.outputString(releaseEl);
        String expected = "<Release ShortName=\"testRelease\" Description=\"Release v3000\" Release-Date=\"2015-01-01\" />";

        assertTrue(output.equals(expected));
    }

    public void testBacklogElement()
    {
        Person p = new Person("Jack", "Hernandoz", "Jack", "jjh23");
        Workspace.addPerson(p);
        Backlog backlog = new Backlog("b1", "The Backlog", "Good backlog", Workspace.getPersonID("Jack"));
        Workspace.addBacklog(backlog);
        BacklogElement backlogElement = new BacklogElement();
        Element backlogEl = backlogElement.getBacklog(backlog.uid());

        String output = xm.outputString(backlogEl);
        String expected = "<Backlog ShortName=\"b1\" FullName=\"The Backlog\" Description=\"Good backlog\" ProductOwner=\"Jack\" EstimationScale=\"Pseudo-Fibonacci\" />";

        assertEquals(expected, output);

        Workspace.removeBacklog(backlog.uid());
        Workspace.removePerson(p.uid());
    }

    public void testStoryElement()
    {
        Person p = new Person("Jack", "Hernandoz", "Jack", "jjh23");
        Workspace.addPerson(p);
        Story s = new Story("Story1", "First Story", "Good story", p.uid());
        Workspace.addStory(s);
        StoryElement storyElement = new StoryElement();
        Element storyEl = storyElement.getStory(s.uid());

        String output = xm.outputString(storyEl);
        String expected = "<Story ShortName=\"Story1\" LongName=\"First Story\" Description=\"Good story\" Creator=\"Jack\" EstimationScale=\"Pseudo-Fibonacci\" Estimate=\"\" State=\"Not Ready\" Effort-Spent=\"0.0 minutes\" />";

        assertEquals(expected, output);

        s.addAcceptanceCriteria(new AcceptanceCriteria(""));
        s.setScaleType(ScaleType.DOGBREEDS);
        s.setEstimate(3);

        storyEl = storyElement.getStory(s.uid());
        output = xm.outputString(storyEl);
        expected = "<Story ShortName=\"Story1\" LongName=\"First Story\" Description=\"Good story\" Creator=\"Jack\" EstimationScale=\"Dog Breeds\" Estimate=\"Pug\" State=\"Not Ready\" Effort-Spent=\"0.0 minutes\" />";

        assertEquals(expected, output);

        Workspace.removeStory(s.uid());
        Workspace.removePerson(p.uid());
    }

    public void testAcceptanceCriteriaElement()
    {
        AcceptanceCriteria ac  = new AcceptanceCriteria("Must be good");
        AcceptanceCriteriaElement acElement = new AcceptanceCriteriaElement();
        Element acEl = acElement.getAcceptanceCriteria(ac);

        String output = xm.outputString(acEl);
        String expected = "<AcceptanceCriterion Description=\"Must be good\" />";

        assertEquals(expected, output);
    }

    public void testTaskElement()
    {
        Task task  = new Task("name", "desc", new Double(0.0));
        TaskElement acElement = new TaskElement();
        Element taskel = acElement.getTask(task);

        String output = xm.outputString(taskel);
        String expected = "<Task ShortName=\"name\" Description=\"desc\" Estimation=\"0.0\" Status=\"Not Started\" />";

        assertEquals(expected, output);
    }
}
