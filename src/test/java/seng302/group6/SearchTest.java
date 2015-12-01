package seng302.group6;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import seng302.group6.Model.ItemClasses.*;
import seng302.group6.Model.ItemType;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Test for the Search Story
 * Created by Josh on 28/07/15.
 */
public class SearchTest extends TestCase{

    Project pr1 = new Project("Project", "Test Project", "A project for testing");
    Project pr2 = new Project("Searchpr", "Search Project", "A project for searching");

    Person pe1 = new Person("Person", "Person", "Test", "tpe335");
    Person pe2 = new Person("Searchpe", "Person", "Search", "spe41");

    Skill sk1 = new Skill("Skill", "A skill for testing");
    Skill sk2 = new Skill("Searchsk", "A skill for searching");

    Team te1 = new Team("Team", "A team for testing");
    Team te2 = new Team("Searchte", "A team for searching");

    Release re1 = new Release("Release", "A release for testing", pr1.uid(), "");
    Release re2 = new Release("Searchre", "A release for searching", pr2.uid(), "");

    Story st1 = new Story("Story", "Test Story", "A story for testing", pe1.uid());
    Story st2 = new Story("Searchst", "Search Story", "A story for searching", pe2.uid());

    Backlog ba1 = new Backlog("Backlog", "Test Backlog", "A backlog for testing", pe1.uid());
    Backlog ba2 = new Backlog("Searchba", "Search Backlog", "A backlog for searching", pe2.uid());

    Searcher searcher = new Searcher();

    public SearchTest(String testName)
    {
        super(testName);
    }

    public static Test suite() {return new TestSuite(SearchTest.class);}

    public void setUp() throws Exception
    {
        Workspace.reset();

        Workspace.addProject(pr1);
        Workspace.addProject(pr2);

        Workspace.addPerson(pe1);
        Workspace.addPerson(pe2);

        Workspace.addSkill(sk1);
        Workspace.addSkill(sk2);

        Workspace.addTeam(te1);
        Workspace.addTeam(te2);

        Workspace.addRelease(re1);
        Workspace.addRelease(re2);

        Workspace.addStory(st1);
        Workspace.addStory(st2);

        Workspace.addBacklog(ba1);
        Workspace.addBacklog(ba2);
    }

    public void tearDown() throws Exception
    {
        Workspace.reset();
    }

    public void testSearchProjects() {
        ArrayList<Item> searchResult;

        searchResult = searcher.searchSingle("for testing", false, ItemType.PROJECT);

        assertTrue(searchResult.contains(pr1));
        assertFalse(searchResult.contains(pr2));

        searchResult = searcher.searchSingle("search", true, ItemType.PROJECT);

        assertFalse(searchResult.contains(pr1));
        assertTrue(searchResult.contains(pr2));
    }

    public void testSearchPeople() {
        ArrayList<Item> searchResult;

        searchResult = searcher.searchSingle("tpe", false, ItemType.PERSON);

        assertTrue(searchResult.contains(pe1));
        assertFalse(searchResult.contains(pe2));

        searchResult = searcher.searchSingle("search", true, ItemType.PERSON);

        assertFalse(searchResult.contains(pe1));
        assertTrue(searchResult.contains(pe2));
    }

    public void testSearchSkills() {
        ArrayList<Item> searchResult;

        searchResult = searcher.searchSingle("for testing", false, ItemType.SKILL);

        assertTrue(searchResult.contains(sk1));
        assertFalse(searchResult.contains(sk2));

        searchResult = searcher.searchSingle("search", true, ItemType.SKILL);

        assertFalse(searchResult.contains(sk1));
        assertTrue(searchResult.contains(sk2));
    }

    public void testSearchTeams() {
        ArrayList<Item> searchResult;

        searchResult = searcher.searchSingle("for testing", false, ItemType.TEAM);

        assertTrue(searchResult.contains(te1));
        assertFalse(searchResult.contains(te2));

        searchResult = searcher.searchSingle("search", true, ItemType.TEAM);

        assertFalse(searchResult.contains(te1));
        assertTrue(searchResult.contains(te2));
    }

    public void testSearchReleases() {
        ArrayList<Item> searchResult;

        searchResult = searcher.searchSingle("for testing", false, ItemType.RELEASE);

        assertTrue(searchResult.contains(re1));
        assertFalse(searchResult.contains(re2));

        searchResult = searcher.searchSingle("search", true, ItemType.RELEASE);

        assertFalse(searchResult.contains(re1));
        assertTrue(searchResult.contains(re2));
    }

    public void testSearchStories() {
        ArrayList<Item> searchResult;

        searchResult = searcher.searchSingle("for testing", false, ItemType.STORY);

        assertTrue(searchResult.contains(st1));
        assertFalse(searchResult.contains(st2));

        searchResult = searcher.searchSingle("search", true, ItemType.STORY);

        assertFalse(searchResult.contains(st1));
        assertTrue(searchResult.contains(st2));
    }

    public void testSearchBacklogs() {
        ArrayList<Item> searchResult;

        searchResult = searcher.searchSingle("for testing", false, ItemType.BACKLOG);

        assertTrue(searchResult.contains(ba1));
        assertFalse(searchResult.contains(ba2));

        searchResult = searcher.searchSingle("search", true, ItemType.BACKLOG);

        assertFalse(searchResult.contains(ba1));
        assertTrue(searchResult.contains(ba2));
    }

    public void testPrintSearch() {
        HashMap<ItemType, ArrayList<Item>> searchResult;

        searchResult = searcher.searchAll("t", true);

        assertTrue(searchResult.get(ItemType.PROJECT).contains(pr1));
        assertTrue(searchResult.get(ItemType.TEAM).contains(te1));
        assertTrue(searchResult.get(ItemType.TEAM).contains(te2));
        assertTrue(searchResult.get(ItemType.STORY).contains(st1));
        assertTrue(searchResult.get(ItemType.STORY).contains(st2));

        assertFalse(searchResult.get(ItemType.PROJECT).contains(pr2));
        assertTrue(searchResult.get(ItemType.PERSON).isEmpty());
        assertFalse(searchResult.get(ItemType.SKILL).contains(sk1));
        assertFalse(searchResult.get(ItemType.SKILL).contains(sk2));
        assertTrue(searchResult.get(ItemType.RELEASE).isEmpty());
        assertTrue(searchResult.get(ItemType.BACKLOG).isEmpty());

        searchResult = searcher.searchAll("a s", false);

        assertTrue(searchResult.get(ItemType.SKILL).contains(sk1));
        assertTrue(searchResult.get(ItemType.SKILL).contains(sk2));
        assertTrue(searchResult.get(ItemType.STORY).contains(st1));
        assertTrue(searchResult.get(ItemType.STORY).contains(st2));

        assertTrue(searchResult.get(ItemType.PROJECT).isEmpty());
        assertTrue(searchResult.get(ItemType.PERSON).isEmpty());
        assertTrue(searchResult.get(ItemType.TEAM).isEmpty());
        assertTrue(searchResult.get(ItemType.RELEASE).isEmpty());
        assertTrue(searchResult.get(ItemType.BACKLOG).isEmpty());
    }

}






