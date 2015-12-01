package seng302.group6;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import seng302.group6.Model.ItemClasses.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;


public class WorkspaceTest extends TestCase
{
    private final String APPDATA_PATH = System.getProperty("user.dir") + "/src/test/java/seng302/group6/test_appdata.json";
    private final String PROJECT_PATH = System.getProperty("user.dir") + "/src/test/java/seng302/group6/test_project.ss";


    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public WorkspaceTest(String testName)
    {
        super(testName);
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite(WorkspaceTest.class);
    }


    public void setUp() throws Exception
    {
        Workspace.reset();
    }

    public void tearDown() throws Exception
    {

    }

    /**
     * tests that Workspace.reset resets its properties to default values.
     */
    public void testDataModel_reset()
    {
        assertEquals(Workspace.getCurrentProject(), null);
    }

    /**
     * tests that Workspace.loadOnStart can load the last used project..
     */
    public void testDataModel_loadOnStart() throws FileNotFoundException, WorkspaceVersionMismatchException, WorkspaceLoadException
    {
//        Workspace.loadOnStart(APPDATA_PATH);
//        assertTrue(Workspace.getProjectNames(Workspace.getProjects()).contains("bork"));
//        assertTrue(Workspace.getProjectNames(Workspace.getProjects()).contains("bork1"));
    }

    /**
     * Tests that loadData populates the projects map with the data from the file.
     */
    public void testDataModel_loadData_getProject() throws FileNotFoundException, WorkspaceVersionMismatchException, WorkspaceLoadException
    {
        File file = new File(PROJECT_PATH);

        Workspace.loadData(file);

        ArrayList<String> uids = Workspace.getProjects();
        assertEquals(uids.size(), 2);

        Project p = Workspace.getProject(uids.get(0));
        assertNotNull(p);
        Project p1 = Workspace.getProject(uids.get(1));
        assertNotNull(p1);
    }

    /**
     * Tests that saveData saves the right data to the file, and that add/remove project do as they should.
     */
    public void testDataModel_loadData_saveData_addProject_removeProject1() throws FileNotFoundException, WorkspaceVersionMismatchException, WorkspaceLoadException
    {
        Project bork = new Project("bork", "", "");
        Project bork2 = new Project("bork", "", "");
        Project bork3 = new Project("bork1", "", "");
        assertNotNull(Workspace.createProject(bork));
        assertNull(Workspace.createProject(bork2));
        assertNotNull(Workspace.createProject(bork3));
        try {
            Workspace.saveData(new File(PROJECT_PATH));
        }
        catch(IOException e) {
            Debug.println("Error saving file: " + e.getMessage());
        }
        Workspace.loadData(new File(PROJECT_PATH));

        assertNotNull(Workspace.getProject(bork.uid()));
        assertNotNull(Workspace.getProject(bork3.uid()));
        assertNull(Workspace.getProject("hello"));
        Workspace.removeProject(bork.uid());
        assertNull(Workspace.getProject(bork.uid()));
        Workspace.loadData(new File(PROJECT_PATH));
        assertNotNull(Workspace.getProject(bork.uid()));
    }

    /***********************************************
     *  get*****Name, get*****Names methods
     ***********************************************/

    public void test_getName_getNames_Skill()
    {
        Skill item = new Skill("Skill_shortname", "desc");
        String uid = Workspace.createSkill(item);
        ArrayList<String> list = Workspace.getSkillNames(Workspace.getSkills());
        assertTrue(list.contains("Skill_shortname"));
        assertEquals(item.getShortName(), Workspace.getSkillName(uid));
    }

    public void test_getName_getNames_Person()
    {
        Person item = new Person("People_shortname", "lastname", "firstname", "userid");
        String uid = Workspace.createPerson(item);
        ArrayList<String> list = Workspace.getPeopleNames(Workspace.getPeople());
        assertTrue(list.contains("People_shortname"));
        assertEquals(item.getShortName(), Workspace.getPersonName(uid));
    }

    public void test_getName_getNames_Team()
    {
        Team item = new Team("Team_shortname", "desc");
        String uid = Workspace.createTeam(item);
        ArrayList<String> list = Workspace.getTeamNames(Workspace.getTeams());
        assertTrue(list.contains("Team_shortname"));
        assertEquals(item.getShortName(), Workspace.getTeamName(uid));
    }

    public void test_getName_getNames_Project()
    {
        Project item = new Project("Project_shortname", "longname", "description");
        String uid = Workspace.createProject(item);
        ArrayList<String> list = Workspace.getProjectNames(Workspace.getProjects());
        assertTrue(list.contains("Project_shortname"));
        assertEquals(item.getShortName(), Workspace.getProjectName(uid));
    }

    public void test_getName_getNames_Release()
    {
        // releases need a project..
        Project project = new Project("sname", "longname", "decription");
        String puid = Workspace.createProject(project);
        Release item = new Release("Release_shortname", "description", puid, "date");
        String uid = Workspace.createRelease(item);
        ArrayList<String> list = Workspace.getReleaseNames(Workspace.getReleases());
        assertTrue(list.contains("Release_shortname"));
        assertEquals(item.getShortName(), Workspace.getReleaseName(uid));
    }

    public void test_getName_getNames_Backlog()
    {
        Backlog item = new Backlog("Backlog_shortname", "fullname", "description", "productowner-uid");
        String uid = Workspace.createBacklog(item);
        ArrayList<String> list = Workspace.getBacklogNames(Workspace.getBacklogs());
        assertTrue(list.contains("Backlog_shortname"));
        assertEquals(item.getShortName(), Workspace.getBacklogName(uid));
    }


    /***********************************************
     *  create, getItems, getItem, removeItem Model type methods
     ***********************************************/


    public void test_create_get_remove_Team() throws Exception {
        Team item = new Team("sname", "desc");
        String id = Workspace.createTeam(item);
        assertEquals(Workspace.getTeams().size(), 1);
        assertEquals(item, Workspace.getTeam(id));
        Workspace.removeTeam(item.uid());
        assertEquals(Workspace.getTeams().size(), 0);
        assertFalse(Workspace.getTeams().contains(id));

    }

    public void test_create_get_remove_Person() throws Exception {
        Person item = new Person("sname", "lastname", "firstname", "id");
        String id = Workspace.createPerson(item);
        assertEquals(Workspace.getPeople().size(), 1);
        assertEquals(item, Workspace.getPerson(id));
        Workspace.removePerson(item.uid());
        assertEquals(Workspace.getPeople().size(), 0);
        assertFalse(Workspace.getPeople().contains(id));
    }

    public void test_create_get_remove_Skill() throws Exception {
        Skill item = new Skill("sname", "desc");
        String id = Workspace.createSkill(item);
        assertEquals(Workspace.getSkills().size(), 3);
        assertEquals(item, Workspace.getSkill(id));
        Workspace.removeSkill(item.uid());
        assertEquals(Workspace.getSkills().size(), 2);
        assertFalse(Workspace.getSkills().contains(id));
    }

    public void test_create_get_remove_Release() throws Exception {
        // releases need a project..
        Project project = new Project("sname", "longname", "decription");
        String puid = Workspace.createProject(project);
        Release item = new Release("sname", "desc", puid, "date");
        String id = Workspace.createRelease(item);
        assertEquals(Workspace.getReleases().size(), 1);
        assertEquals(item, Workspace.getRelease(id));
        Workspace.removeRelease(item.uid());
        assertEquals(Workspace.getReleases().size(), 0);
        assertFalse(Workspace.getReleases().contains(id));
    }

    public void test_create_get_remove_Backlog() throws Exception {
        Backlog item = new Backlog("sname", "fullname", "description", "po-iud");
        String id = Workspace.createBacklog(item);
        assertEquals(Workspace.getBacklogs().size(), 1);
        assertEquals(item, Workspace.getBacklog(id));
        Workspace.removeBacklog(item.uid());
        assertEquals(Workspace.getBacklogs().size(), 0);
        assertFalse(Workspace.getBacklogs().contains(id));
    }

    public void test_create_get_remove_Project() throws Exception {
        Project item = new Project("sname", "longname", "decription");
        String id = Workspace.createProject(item);
        assertEquals(Workspace.getProjects().size(), 1);
        assertEquals(item, Workspace.getProject(id));
        Workspace.removeProject(item.uid());
        assertEquals(Workspace.getProjects().size(), 0);
        assertFalse(Workspace.getProjects().contains(id));
    }

    /***********************************************
     *  create, deleteItem Model type methods
     ***********************************************/


    public void test_create_delete_Team() throws Exception {
        Team item = new Team("sname", "desc");
        String id = Workspace.createTeam(item);
        assertEquals(Workspace.getTeams().size(), 1);
        assertEquals(item, Workspace.getTeam(id));
        Workspace.deleteTeamOnly(item.uid());
        assertEquals(Workspace.getTeams().size(), 0);
        assertFalse(Workspace.getTeams().contains(id));
    }

    public void test_create_delete_Team_and_People() throws Exception {
        Team item = new Team("sname", "desc");
        Person p = new Person("sname", "lastname", "firstname", "userid");
        assertFalse(Workspace.getPeople().contains(p.uid()));
        String pid = Workspace.createPerson(p);
        String id = Workspace.createTeam(item);
        item.addPerson(pid);
        assertTrue(Workspace.getPeople().contains(pid));
        assertEquals(Workspace.getTeams().size(), 1);
        assertEquals(item, Workspace.getTeam(id));
        Workspace.deleteTeamAndPeople(item.uid());
        assertEquals(Workspace.getTeams().size(), 0);
        assertFalse(Workspace.getTeams().contains(id));
        assertFalse(Workspace.getPeople().contains(pid));
    }

    public void test_create_delete_Person() throws Exception {
        Person item = new Person("sname", "lastname", "firstname", "id");
        String id = Workspace.createPerson(item);
        assertEquals(Workspace.getPeople().size(), 1);
        assertEquals(item, Workspace.getPerson(id));
        Workspace.deletePerson(item.uid());
        assertEquals(Workspace.getPeople().size(), 0);
        assertFalse(Workspace.getPeople().contains(id));
    }

    public void test_create_delete_Skill() throws Exception {
        Skill item = new Skill("sname", "desc");
        String id = Workspace.createSkill(item);
        assertEquals(Workspace.getSkills().size(), 3);
        assertEquals(item, Workspace.getSkill(id));
        Workspace.deleteSkill(item.uid());
        assertEquals(Workspace.getSkills().size(), 2);
        assertFalse(Workspace.getSkills().contains(id));
    }

    public void test_create_delete_Release() throws Exception {
        // releases need a project..
        Project project = new Project("sname", "longname", "decription");
        String puid = Workspace.createProject(project);
        Release item = new Release("sname", "desc", puid, "date");
        String id = Workspace.createRelease(item);
        assertEquals(Workspace.getReleases().size(), 1);
        assertEquals(item, Workspace.getRelease(id));
        Workspace.deleteRelease(item.uid());
        assertEquals(Workspace.getReleases().size(), 0);
        assertFalse(Workspace.getReleases().contains(id));
    }

    public void test_create_delete_Backlog() throws Exception {
        Backlog item = new Backlog("sname", "fullname", "description", "po-iud");
        String id = Workspace.createBacklog(item);
        assertEquals(Workspace.getBacklogs().size(), 1);
        assertEquals(item, Workspace.getBacklog(id));
        Workspace.deleteBacklog(item.uid());
        assertEquals(Workspace.getBacklogs().size(), 0);
        assertFalse(Workspace.getBacklogs().contains(id));
    }

    public void test_create_delete_Project() throws Exception {
        Project item = new Project("sname", "longname", "decription");
        String id = Workspace.createProject(item);
        assertEquals(Workspace.getProjects().size(), 1);
        assertEquals(item, Workspace.getProject(id));
        Workspace.deleteProject(item.uid());
        assertEquals(Workspace.getProjects().size(), 0);
        assertFalse(Workspace.getProjects().contains(id));
    }


    public void test_create_delete_Sprint() throws Exception {
        Sprint item = new Sprint("sname", "longname", "decription");
        String id = Workspace.createSprint(item);
        assertEquals(Workspace.getSprints().size(), 1);
        assertEquals(item, Workspace.getSprint(id));
        Workspace.deleteSprint(item.uid());
        assertEquals(Workspace.getSprints().size(), 0);
        assertFalse(Workspace.getSprints().contains(id));
    }


    /***********************************************
     *  randoms
     ***********************************************/

    public void test_itemShortNameNotUnique()
    {
        Person item = new Person("sname", "lastname", "firstname", "id");
        Person item1 = new Person("sname1", "lastname", "firstname", "id");
        Team item2 = new Team("sname", "description");
        assertFalse(Workspace.itemShortNameNotUnique(item));
        Workspace.addPerson(item);
        assertTrue(Workspace.itemShortNameNotUnique(item));
        assertFalse(Workspace.itemShortNameNotUnique(item2));
        assertFalse(Workspace.itemShortNameNotUnique(item1));
    }

    public void test_shortNameNotUnique()
    {
        Person item = new Person("sname", "lastname", "firstname", "id");
        Person item1 = new Person("sname1", "lastname", "firstname", "id");
        Team item2 = new Team("sname", "description");
        assertFalse(Workspace.shortNameNotUnique("sname", item));
        Workspace.addPerson(item);
        assertTrue(Workspace.shortNameNotUnique("sname", item));
        assertFalse(Workspace.shortNameNotUnique("sname", item2));
        assertFalse(Workspace.shortNameNotUnique("sname1", item1));
    }

    public void testDataModel_getCurrentProject()
    {
        String id = Workspace.createProject(new Project("bork", "", ""));
        assertNotNull(id);
        String prjid = Workspace.getCurrentProject();
        assertNotNull(prjid);
        assertEquals(prjid, id);
    }

    public void testDataModel_setCurrentProject()
    {
        String id = Workspace.createProject(new Project("bork", "", ""));
        assertNotNull(id);
        Workspace.setCurrentProject("16626");
        Workspace.setCurrentProject(id);
        String prjid = Workspace.getCurrentProject();
        assertNotNull(prjid);
        assertEquals(prjid, id);
    }

    public void testDefaultSkills() throws Exception {
        assertEquals(Workspace.getSkills().size(), 2);
        assertTrue(Workspace.getSkillNames(Workspace.getSkills()).contains("Scrum Master"));
        assertTrue(Workspace.getSkillNames(Workspace.getSkills()).contains("Product Owner"));
    }

    public void testGetPeopleWithoutTeam() {
        Person pe = new Person("sname", "lastname", "firstname", "mjs169");
        Team t = new Team("tname", "desc");
        Workspace.reset();
        Workspace.createTeam(t);
        String id = Workspace.createPerson(pe);
        assertFalse(Workspace.getPeopleWithoutTeam().isEmpty());
        t.addPerson(id);
        assertTrue(Workspace.getPeopleWithoutTeam().isEmpty());
    }

    public  void test_getTeamsWithoutProject()
    {
        Team t = new Team("tname", "desc");
        Workspace.addTeam(t);
        Team t1 = new Team("tname", "desc");
        Workspace.addTeam(t1);
        Project prj = new Project("shortname", "longname", "description");
        Workspace.addProject(prj);
        prj.addDevTeam(t.uid(), LocalDate.now(), LocalDate.now());
        assertTrue(Workspace.getTeamsWithoutProject().size() == 1);
        assertTrue(Workspace.getTeamsWithoutProject().contains(t1.uid()));
    }

    public void test_getFileString()
    {
        assertEquals(new File(Workspace.getFileString()), new File(PROJECT_PATH));
    }

    public void test_getSaved()
    {
        assertTrue(Workspace.getSaved());
        Workspace.createTeam(new Team("tname", "desc"));
        assertFalse(Workspace.getSaved());
    }

    public void test_deleteProject() {
        String proj1 = Workspace.createProject();
        String release1 =  Workspace.createRelease();
        String proj2 = Workspace.createProject();
        String release2 = Workspace.createRelease();

        Workspace.deleteProject(proj1);

        assertNull(Workspace.getProject(proj1));
        assertNull(Workspace.getRelease(release1));
        assertNotNull(Workspace.getProject(proj2));
        assertNotNull(Workspace.getRelease(release2));

        Workspace.undo();

        assertNotNull(Workspace.getProject(proj1));
        assertNotNull(Workspace.getRelease(release1));

        Workspace.deleteProject(proj2);

        assertNull(Workspace.getProject(proj2));
        assertNull(Workspace.getRelease(release2));
        assertNotNull(Workspace.getProject(proj1));
        assertNotNull(Workspace.getRelease(release1));

        Workspace.undo();

        assertNotNull(Workspace.getProject(proj2));
        assertNotNull(Workspace.getRelease(release2));
    }
}
