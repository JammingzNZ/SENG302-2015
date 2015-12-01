package seng302.group6;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import seng302.group6.Model.ItemClasses.Allocation;
import seng302.group6.Model.ItemClasses.Project;
import seng302.group6.Model.ItemClasses.Team;
import seng302.group6.Model.ItemClasses.Workspace;

import java.time.LocalDate;
import java.util.ArrayList;

/**
 * Created by simon on 13/05/15.
 */
public class AllocationTest extends TestCase
{
    private Project proj;
    private Team team1;
    private Team team2;

    public AllocationTest(String testName)
    {
        super(testName);

        Workspace.reset();

        proj = new Project("Proj", "My Project", "A project");
        team1 = new Team("Team 1", "The first team");
        team2 = new Team("Team 2", "The second team");

        Workspace.addProject(proj);
        Workspace.addTeam(team1);
        Workspace.addTeam(team2);
    }

    public static Test suite()
    {
        return new TestSuite(AllocationTest.class);
    }


    public void test_setEndDate()
    {
        Allocation a = new Allocation("team-uid", LocalDate.of(2015, 1, 1), LocalDate.of(2015, 1, 2), "project-uid");
        assertEquals(a.getEndDate(), LocalDate.of(2015, 1, 2));
        a.setEndDate(LocalDate.of(2015, 1,3));
        assertEquals(a.getEndDate(), LocalDate.of(2015, 1, 3));
    }

    public void test_setStartDate()
    {
        Allocation a = new Allocation("team-uid", LocalDate.of(2015, 1, 1), LocalDate.of(2015, 1, 2), "project-uid");
        assertEquals(a.getStartDate(), LocalDate.of(2015, 1, 1));
        a.setStartDate(LocalDate.of(2015, 1, 3));
        assertEquals(a.getStartDate(), LocalDate.of(2015, 1, 3));
    }

    public void test_setTeamUID()
    {
        Allocation a = new Allocation("team-uid", LocalDate.of(2015, 1, 1), LocalDate.of(2015, 1, 2), "project-uid");
        assertEquals(a.getTeamUID(), "team-uid");
        a.setTeamUID("team1-uid");
        assertEquals(a.getTeamUID(), "team1-uid");
    }

    public void test_isPast()
    {
        Allocation a = new Allocation("team-uid", LocalDate.of(2015, 1, 1), LocalDate.of(2013, 1, 3), "project-uid");
        assertTrue(a.isPast());
        // this test will pass for 1000 years..... and then....
        a.setEndDate(LocalDate.of(3015, 1, 3));
        assertFalse(a.isPast());
    }

    public void test_setProject()
    {
        Allocation a = new Allocation("team-uid", LocalDate.of(2015, 1, 1), LocalDate.of(2015, 1, 2), "project-uid");
        assertEquals(a.getProjectUID(), "project-uid");
        a.setProjectUID("project1-uid");
        assertEquals(a.getProjectUID(), "project1-uid");
    }

    /**
     * Tests that the team uid of an allocation assigned to a project
     * is the same when it is loaded from the project
     */
    public void testAllocationShouldRetainTeam()
    {
        Workspace.reset();
        Workspace.addProject(proj);
        Workspace.addTeam(team1);
        Workspace.addTeam(team2);

        String team1Uid = Workspace.getTeamID("Team 1");
        LocalDate startDate = LocalDate.of(2015, 1, 1);
        LocalDate endDate = LocalDate.of(2015, 1, 10);

        // Save allocation to project
        proj.addDevTeam(team1Uid, startDate, endDate);

        // Load allocation from project
        Allocation a = proj.getDevTeams().get(0);

        assertTrue(a.getTeamUID().equals(team1Uid));

        proj.removeDevTeam(team1Uid, startDate, endDate);
    }

    /**
     * Tests that the start and end dates of an allocation assigned to a
     * project are the same when loaded from the project
     */
    public void testAllocationShouldRetainDates()
    {
        String team1Uid = Workspace.getTeamID(team1.getShortName());
        LocalDate startDate = LocalDate.of(2015, 1, 1);
        LocalDate endDate = LocalDate.of(2015, 1, 10);

        // Save allocation to project
        proj.addDevTeam(team1Uid, startDate, endDate);

        // Load allocation from project
        Allocation a = proj.getDevTeams().get(0);

        assertTrue(a.getStartDate().equals(startDate));
        assertTrue(a.getEndDate().equals(endDate));

        proj.removeDevTeam(team1Uid, startDate, endDate);
    }

    /**
     * Tests that the correct teams are stored for a project when adding
     * allocations to the project
     */
    public void testProjectContainsCorrectDevTeams()
    {
        String team1Uid = Workspace.getTeamID("Team 1");
        LocalDate team1startDate = LocalDate.of(2015, 1, 1);
        LocalDate team1endDate = LocalDate.of(2015, 1, 10);

        String team2Uid = Workspace.getTeamID("Team 2");
        LocalDate team2startDate = LocalDate.of(2015, 2, 1);
        LocalDate team2endDate = LocalDate.of(2015, 1, 10);

        // Allocate teams
        proj.addDevTeam(team1Uid, team1startDate, team1endDate);
        proj.addDevTeam(team2Uid, team2startDate, team2endDate);

        ArrayList<String> teamUids = new ArrayList<>();
        for (Allocation a: proj.getDevTeams()) {
            teamUids.add(a.getTeamUID());
        }

        assertTrue(proj.getDevTeams().size() == 2);
        assertTrue(teamUids.contains(team1Uid));
        assertTrue(teamUids.contains(team2Uid));
    }

    /**
     * Tests that teams whose end dates are before today aren't included when
     * calling project.getCurrentDevTeams()
     */
    public void testGetCurrentDevTeams()
    {
        // Allocation with start date today and end date in one week
        String team1Uid = Workspace.getTeamID("Team 1");
        LocalDate team1startDate = LocalDate.now();
        LocalDate team1endDate = LocalDate.now().plusDays(7);

        // Allocation that ended three days ago
        String team2Uid = Workspace.getTeamID("Team 2");
        LocalDate team2startDate = LocalDate.now().minusDays(10);
        LocalDate team2endDate = LocalDate.now().minusDays(3);

        // Allocate teams
        proj.addDevTeam(team1Uid, team1startDate, team1endDate);
        proj.addDevTeam(team2Uid, team2startDate, team2endDate);

        ArrayList<String> currentTeamUids = new ArrayList<>();
        for (Allocation a: proj.getCurrentDevTeams()) {
            currentTeamUids.add(a.getTeamUID());
        }

        assertTrue(proj.getCurrentDevTeams().size() == 1);
        // Team 1 should be included as they finish in one week
        assertTrue(currentTeamUids.contains(team1Uid));
        // Team 2 shouldn't be included as they finished 3 days ago
        assertTrue(!currentTeamUids.contains(team2Uid));
    }
}
