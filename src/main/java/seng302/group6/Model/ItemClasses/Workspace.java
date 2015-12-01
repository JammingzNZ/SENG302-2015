package seng302.group6.Model.ItemClasses;

import com.google.gson.annotations.Expose;
import com.google.gson.reflect.TypeToken;
import seng302.group6.*;
import seng302.group6.Model.Command.Backlog.CreateBacklog;
import seng302.group6.Model.Command.Backlog.DeleteBacklog;
import seng302.group6.Model.Command.Command;
import seng302.group6.Model.Command.CommandManager;
import seng302.group6.Model.Command.CommandMessage;
import seng302.group6.Model.Command.GroupCommand;
import seng302.group6.Model.Command.Person.CreatePerson;
import seng302.group6.Model.Command.Person.DeletePerson;
import seng302.group6.Model.Command.Person.RemoveSkill;
import seng302.group6.Model.Command.Project.CreateProject;
import seng302.group6.Model.Command.Project.DeallocateDevTeam;
import seng302.group6.Model.Command.Project.DeleteProject;
import seng302.group6.Model.Command.Release.CreateRelease;
import seng302.group6.Model.Command.Release.DeleteRelease;
import seng302.group6.Model.Command.Skill.CreateSkill;
import seng302.group6.Model.Command.Skill.DeleteSkill;
import seng302.group6.Model.Command.Sprint.CreateSprint;
import seng302.group6.Model.Command.Sprint.DeleteSprint;
import seng302.group6.Model.Command.Story.CreateStory;
import seng302.group6.Model.Command.Story.DeleteStory;
import seng302.group6.Model.Command.Team.CreateTeam;
import seng302.group6.Model.Command.Team.DeleteTeam;
import seng302.group6.Model.ItemType;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;


class Data
{
    @Expose public HashMap<String, Project> projects = new HashMap<>();
    @Expose public HashMap<String, Backlog> backlogs = new HashMap<>();
    @Expose public HashMap<String, Person> people = new HashMap<>();
    @Expose public HashMap<String, Skill> skills = new HashMap<>();
    @Expose public HashMap<String, Team> teams = new HashMap<>();
    @Expose public HashMap<String, Release> releases = new HashMap<>();
    @Expose public HashMap<String, Story> stories = new HashMap<>();
    @Expose public HashMap<String, Sprint> sprints = new HashMap<>();
    @Expose public String workspace_version = Appdata.WORKSPACE_VERSION;

    public HashMap<String, Item> getItems()
    {
        HashMap<String, Item> all = new HashMap<>();
        all.putAll(projects);
        all.putAll(backlogs);
        all.putAll(people);
        all.putAll(skills);
        all.putAll(teams);
        all.putAll(releases);
        all.putAll(stories);
        all.putAll(sprints);

        return all;
    }
}

/**
 * Centralised data model - holds a map of projects, which in turn hold their 
 * child objects.
 */
public class Workspace
{
    private static CommandManager manager = new CommandManager();
    private static String selectedProject = null;
    public static Appdata appdata = null;
    private static Data datamodel = new Data();

    /**
     * resets the data model to the startup state.
     */
    public static void reset()
    {
        datamodel = new Data();
        selectedProject = null;
        manager.clear();
        tryInitPoAndSm();
    }

    /***************************************************
     *
     * get, add, remove, getNames, getName, getUIDS of: people, skills, teams, projects
     *
     * get / set current project
     *
     ***************************************************/

    /**
     * Gets the item name given the uid
     * @param uid of the item
     * @return the items shortName
     */
    public static String getItemName(String uid)
    {
        Item item = datamodel.getItems().get(uid);
        return item.getShortName();
    }

    /**
     * Gets the Item of the given UID
     * @param uid uid
     * @return the item of the Given UID
     */
    public static Item getItem(String uid)
    {
        Item item = datamodel.getItems().get(uid);
        return item;
    }

    /**
     * Gets a list of all items of the given itemtype
     * @param itemType item type
     * @return A list of all items of teh given itemtypes
     */
    public static ArrayList getItems(ItemType itemType) {
        switch(itemType)
        {
            case SKILL:
                return Workspace.getSkillNames(Workspace.getSkills());
            case PERSON:
                return Workspace.getPeopleNames(Workspace.getPeople());
            case TEAM:
                return Workspace.getTeamNames(Workspace.getTeams());
            case PROJECT:
                return Workspace.getProjectNames(Workspace.getProjects());
            case RELEASE:
                return Workspace.getReleaseNames(getReleases());
            case STORY:
                return Workspace.getStoryNames(Workspace.getStories());
            case BACKLOG:
                return Workspace.getBacklogNames(getBacklogs());
            case SPRINT:
                return Workspace.getSprintNames(getSprints());
        }
        return null;
    }

    /**
     * returns the person with given unique id.
     *
     * @param uid is the unique id of the person to get.
     * @return returns a person object.
     */
    public static Person getPerson(String uid)
    {
        return datamodel.people.get(uid);
    }

    /**
     * Skills object accessor.
     *
     * @param  uid is the unique idd of the skill.
     * @return returns the Skill associated with the given uid.
     */
    public static Skill getSkill(String uid)
    {
        return datamodel.skills.get(uid);
    }

    /**
     * Team object accessor.
     *
     * @param  uid is the unique idd of the team.
     * @return returns the Team associated with the given uid.
     */
    public static Team getTeam(String uid)
    {
        return datamodel.teams.get(uid);
    }

    /**
     * Story object accessor.
     *
     * @param  uid is the unique idd of the story.
     * @return returns the Story associated with the given uid.
     */
    public static Story getStory(String uid)
    {
        return datamodel.stories.get(uid);
    }

    /**
     * Project type accessor method.
     *
     * @param uid is the unique id of the project.
     * @return returns a reference to the specified project, or null if the project was not found.
     */
    public static Project getProject(String uid)
    {
        return datamodel.projects.get(uid);
    }

    /**
     * returns the release with given unique id.
     *
     * @param uid is the unique id of the release to get.
     * @return returns a Release object.
     */
    public static Release getRelease(String uid)
    {
        return datamodel.releases.get(uid);
    }

    /**
     * returns the backlog with given unique id.
     *
     * @param uid is the unique id of the backlog to get.
     * @return returns a Backlog object.
     */
    public static Backlog getBacklog(String uid)
    {
        return datamodel.backlogs.get(uid);
    }

    /**
     * returns the sprint with given uid
     * @param uid is the unique id of the sprint to get
     * @return returns a sprint object
     */
    public static Sprint getSprint(String uid) { return datamodel.sprints.get(uid); }

    /**
     * Person uid accessor method.
     *
     * @param shortName is the shortName of the person.
     * @return returns the unique id of the skill, or null if the skill was not found.
     */
    public static String getPersonID(String shortName)
    {
        try {
            for (String uid : datamodel.people.keySet()) {
                if (shortName.equals(getPersonName(uid))) {
                    return uid;
                }
            }
        }
        catch(Exception e)
        {
            Debug.run(() -> e.printStackTrace());
        }
        return null;
    }

    /**
     * Skill uid accessor method.
     *
     * @param shortName is the shortName of the skill.
     * @return returns the unique id of the skill, or null if the skill was not found.
     */
    public static String getSkillID(String shortName)
    {
        for(String uid : datamodel.skills.keySet()) {
            if(shortName.equals(getSkillName(uid))){
                return uid;
            }
        }
        return null;
    }

    /**
     * Team uid accessor method.
     *
     * @param shortName is the shortName of the team.
     * @return returns the unique id of the team, or null if the team was not found.
     */
    public static String getTeamID(String shortName)
    {
        for(String uid : datamodel.teams.keySet()) {
            if(shortName.equals(getTeamName(uid))){
                return uid;
            }
        }
        return null;
    }

    /**
     * Story uid accessor method.
     *
     * @param shortName is the shortName of the story.
     * @return returns the unique id of the story, or null if the story was not found.
     */
    public static String getStoryID(String shortName)
    {
        for(String uid : datamodel.stories.keySet()) {
            if(shortName.equals(getStoryName(uid))){
                return uid;
            }
        }
        return null;
    }

    /**
     * Project uid accessor method.
     *
     * @param shortName is the shortName of the project.
     * @return returns the unique id of the project, or null if the project was not found.
     */
    public static String getProjectID(String shortName)
    {
        for(String uid : datamodel.projects.keySet()) {
            if(shortName.equals(getProjectName(uid))){
                return uid;
            }
        }
        return null;
    }

    /**
     * Release uid accessor method.
     *
     * @param shortName is the shortName of the release.
     * @return returns the unique id of the release, or null if the release was not found.
     */
    public static String getReleaseID(String shortName)
    {
        for(String uid : datamodel.releases.keySet()) {
            if(shortName.equals(getReleaseName(uid))){
                return uid;
            }
        }
        return null;
    }

    /**
     * Backlog uid accessor method.
     *
     * @param shortName is the shortName of the backlog.
     * @return returns the unique id of the backlog, or null if the backlog was not found.
     */
    public static String getBacklogID(String shortName)
    {
        for(String uid : datamodel.backlogs.keySet()) {
            if(shortName.equals(getBacklogName(uid))){
                return uid;
            }
        }
        return null;
    }

    /**
     * Sprint uid accessor method.
     *
     * @param shortName is the shortName of the sprint.
     * @return returns the unique id of the sprint, or null if the sprint was not found.
     */
    public static String getSprintID(String shortName)
    {
        for(String uid : datamodel.sprints.keySet()) {
            if(shortName.equals(getSprintName(uid))){
                return uid;
            }
        }
        return null;
    }

    /**
     * returns the unique ids of all people.
     *
     * @return returns an ArrayList of ids for all people.
     */
    public static ArrayList<String> getPeople()
    {
        return new ArrayList<String>(datamodel.people.keySet());
    }

    /**
     * returns the unique ids of all projects.
     *
     * @return returns an ArrayList of ids for all projects.
     */
    public static ArrayList<String> getProjects()
    {
        return new ArrayList<String>(datamodel.projects.keySet());
    }

    /**
     * returns the unique ids of all skills.
     *
     * @return returns an ArrayList of ids for all skills.
     */
    public static ArrayList<String> getSkills()
    {
        return new ArrayList<String>(datamodel.skills.keySet());
    }

    /**
     * returns the unique ids of all teams.
     *
     * @return returns an ArrayList of ids for all teams.
     */
    public static ArrayList<String> getTeams()
    {
        return new ArrayList<String>(datamodel.teams.keySet());
    }

    /**
     * returns the unique ids of all stories.
     *
     * @return returns an ArrayList of ids for all stories.
     */
    public static ArrayList<String> getStories()
    {
        return new ArrayList<String>(datamodel.stories.keySet());
    }

    /**
     * returns uids of all stories which aren't in a backlog
     * @return an ArrayList of uids for unallocated stories
     */
    public static ArrayList<String> getStoriesWithoutBacklog()
    {
        ArrayList<String> storiesWithoutBacklog = new ArrayList<>();
        for(String uid : datamodel.stories.keySet()) {
            Story s = getStory(uid);
            if (!s.isInBacklog()) {
                storiesWithoutBacklog.add(uid);
            }
        }
        return storiesWithoutBacklog;
    }

    /**
     * returns the unique ids of all releases.
     *
     * @return returns an ArrayList of ids for all releases.
     */
    public static ArrayList<String> getReleases()
    {
        return new ArrayList<String>(datamodel.releases.keySet());
    }

    /**
     * returns the unique ids of all backlogs.
     *
     * @return returns an ArrayList of ids for all backlogs.
     */
    public static ArrayList<String> getBacklogs()
    {
        return new ArrayList<String>(datamodel.backlogs.keySet());
    }

    /**
     * returns the unique ids of all sprints
     * @return returns an arraylist of ids for all sprints
     */
    public static ArrayList<String> getSprints() { return new ArrayList<String>(datamodel.sprints.keySet()); }

    /**
     * Adds a project to the workspace
     * @param project is the Project being added to the project list
     */
    public static void addProject(Project project)
    {
        datamodel.projects.put(project.uid(), project);
    }

    /**
     * Adds a person to the workspace
     * @param person is the Person being added to the people list
     */
    public static void addPerson(Person person)
    {
        datamodel.people.put(person.uid(), person);
    }

    /**
     * Adds a skill to the workspace
     * @param skill is the Skill being added to the skills list
     */
    public static void addSkill(Skill skill)
    {
        datamodel.skills.put(skill.uid(), skill);
    }

    /**
     * Adds a team to the workspace
     * @param team is the team being added to the teams list
     */
    public static void addTeam(Team team)
    {
        datamodel.teams.put(team.uid(), team);
    }

    /**
     * Adds a story to the workspace
     * @param story is the team being added to the stories list
     */
    public static void addStory(Story story)
    {
        datamodel.stories.put(story.uid(), story);
    }

    /**
     * Adds a release to the workspace
     * @param release is the release being added to the releases list
     */
    public static void addRelease(Release release)
    {
        datamodel.releases.put(release.uid(), release);
    }

    /**
     * Adds a backlog to the workspace
     * @param backlog is the backlog being added to the backlogs list
     */
    public static void addBacklog(Backlog backlog)
    {
        datamodel.backlogs.put(backlog.uid(), backlog);
    }

    /**
     * Adds a sprint to the workspace
     * @param sprint is the sprint being added to the sprints list
     */
    public static void addSprint(Sprint sprint) { datamodel.sprints.put(sprint.uid(), sprint); }

    /**
     * Removes a person from the Project
     * @param uid is the unique id of the person
     */
    public static void removePerson(String uid) {
        datamodel.people.remove(uid);
    }

    /**
     * Removes a skill from the project
     * @param uid is the unique id of the skill
     */
    public static void removeSkill(String uid)
    {
        datamodel.skills.remove(uid);
    }

    /**
     * remove a team from the project
     * @param uid is the unique id of the team
     */
    public static void removeTeam(String uid)
    {
        datamodel.teams.remove(uid);
    }

    /**
     * remove a story from the project
     * @param uid is the unique id of the story
     */
    public static void removeStory(String uid)
    {
        datamodel.stories.remove(uid);
    }

    /**
     * remove a release from the project
     * @param uid is the unique id of the release
     */
    public static void removeRelease(String uid)
    {
        datamodel.releases.remove(uid);
    }

    /**
     * remove a backlog from the project
     * @param uid is the unique id of the backlog
     */
    public static void removeBacklog(String uid)
    {
        datamodel.backlogs.remove(uid);
    }

    /**
     * remove a sprint from the workspace
     * @param uid is the unique id of the sprint
     */
    public static void removeSprint(String uid) { datamodel.sprints.remove(uid); }

    /**
     * Creates a new person to the data model, if a person of that name
     * isn't already mapped.
     *
     * @param person is the VALIDATED person to Create.
     * @return returns the uid of the person Created, null if not created.
     */
    public static String createPerson(Person person)
    {
        if(itemShortNameNotUnique(person)) {
            return null;
        }
        manager.addCommand(new CreatePerson(datamodel.people, person));
        return person.uid();
    }

    /**
     * Create an automatically generated person
     * @return Uid of the created person, null if not created
     */
    public static String createPerson()
    {
        int personNum = 1;
        String personName = "New Person " + personNum;
        String uid;
        while ((uid = createPerson(new Person(personName, "", "", ""))) == null) {
            personNum++;
            personName = "New Person " + personNum;
        }
        return uid;
    }

    /**
     * Deletes the given person
     * @param uid uid of the person
     */
    public static void deletePerson(String uid)
    {
        Person person = getPerson(uid);
        manager.addCommand(new DeletePerson(person));
    }


    /**
     * adds a new team to the data model, if a team of that name
     * isn't already mapped.
     *
     * @param team is the VALIDATED team to add.
     * @return returns the uid of the team if added, null otherwise.
     */
    public static String createTeam(Team team)
    {
        if(itemShortNameNotUnique(team)) {
            return null;
        }
        manager.addCommand(new CreateTeam(datamodel.teams, team));
        return team.uid();
    }

    /**
     * Create an automatically generated team
     * @return Uid of the created team, null if not created
     */
    public static String createTeam()
    {
        int teamNum = 1;
        String teamName = "New Team " + teamNum;
        String uid;
        while ((uid = createTeam(new Team(teamName, ""))) == null) {
            teamNum++;
            teamName = "New Team " + teamNum;
        }
        return uid;
    }

    /**
     * Deletes the given team
     * @param uid uid of the team
     */
    public static void deleteTeamOnly(String uid)
    {
        Team team = getTeam(uid);
        ArrayList<Allocation> allocations = new ArrayList<>();
        for (String pUid: getProjects()) {
            allocations.addAll(getProject(pUid).getDevTeams());
        }

        GroupCommand grpCmd = new GroupCommand();
        String teamName = getTeamName(uid);
        CommandMessage message = new CommandMessage("Delete", ItemType.TEAM, team);

        for (Allocation allocation: allocations) {
            if (allocation.getTeamUID().equals(uid)) {
                grpCmd.addCommand(new DeallocateDevTeam(
                        getProject(allocation.getProjectUID()), uid,
                        allocation.getStartDate(), allocation.getEndDate()
                ));
            }
        }

        grpCmd.setMessage(message);
        grpCmd.addCommand(new DeleteTeam(team));

        manager.addCommand(grpCmd);
    }

    /**
     * Deletes the given team and the people inside that team
     * @param uid uid of team
     */
    public static void deleteTeamAndPeople(String uid)
    {
        Team team = getTeam(uid);
        String teamName = getTeamName(uid);
        CommandMessage message = new CommandMessage("Delete", ItemType.TEAM, team);
        ArrayList<String> people = team.getPeople();
        GroupCommand grpCmd = new GroupCommand();

        ArrayList<Allocation> allocations = new ArrayList<>();
        for (String pUid: getProjects()) {
            allocations.addAll(getProject(pUid).getDevTeams());
        }

        for (Allocation allocation: allocations) {
            if (allocation.getTeamUID().equals(uid)) {
                grpCmd.addCommand(new DeallocateDevTeam(
                        getProject(allocation.getProjectUID()), uid,
                        allocation.getStartDate(), allocation.getEndDate()
                ));
            }
        }

        grpCmd.setMessage(message);
        // Deletes the team
        grpCmd.addCommand(new DeleteTeam(team));
        // Deletes the people in the team
        for (int i = 0; i < people.size(); i++)
        {
            String personID = team.getPeople().get(i);
            Person person = Workspace.getPerson(personID);
            grpCmd.addCommand(new DeletePerson(person));
        }
        // Adds the grpCmd to the Undo/Redo stack
        manager.addCommand(grpCmd);
    }

    /**
     * adds a new skill to the current project.
     *
     * @param skill is the VALIDATED skill to add.
     * @return returns the uid of the skill if added, null otherwise.
     */
    public static String createSkill(Skill skill)
    {
        if(itemShortNameNotUnique(skill)) {
            return null;
        }
        manager.addCommand(new CreateSkill(datamodel.skills, skill));
        return skill.uid();
    }

    /**
     * Create an automatically generated skill
     * @return Uid of the created skill, null if not created
     */
    public static String createSkill()
    {
        int skillNum = 1;
        String skillName = "New Skill " + skillNum;
        String uid;
        while ((uid = createSkill(new Skill(skillName, ""))) == null) {
            skillNum++;
            skillName = "New Skill " + skillNum;
        }
        return uid;
    }

    /**
     * Deletes the given skill
     * @param uid uid of the skill
     */
    public static void deleteSkill(String uid)
    {
        Skill skill = getSkill(uid);
        String skillName = getSkillName(uid);
        CommandMessage message = new CommandMessage("Delete", ItemType.SKILL, skill);
        ArrayList<String> people = Workspace.getPeople();

        GroupCommand grpCmd = new GroupCommand();
        grpCmd.setMessage(message);

        for (int i = 0; i < people.size(); i++) {
            String personID = people.get(i);
            Person person = Workspace.getPerson(personID);
            ArrayList<String> skills = person.getSkills();
            if (skills.contains(uid)) {
                // Removes the skill from the person
                grpCmd.addCommand(new RemoveSkill(person, uid));
                person.removeSkill(uid);
            }
        }
        // Deletes the skill
        grpCmd.addCommand(new DeleteSkill(skill));
        // Adds the grpCmd to the Undo/Redo stack
        manager.addCommand(grpCmd);
        //##################################################
    }

    /**
     * adds a new project to the data model, if a project of that name
     * isn't already mapped.
     *
     * @param project is the VALIDATED project to add.
     * @return returns the uid of the project if added, null otherwise.
     */
    public static String createProject(Project project)
    {
        if(itemShortNameNotUnique(project)) {
            return null;
        }
        manager.addCommand(new CreateProject(datamodel.projects, project));
        selectedProject = project.uid();
        return selectedProject;
    }

    /**
     * Create an automatically generated project
     * @return Uid of the created project, null if not created
     */
    public static String createProject()
    {
        int projectNum = 1;
        String projectName = "New Project " + projectNum;
        String uid;
        while ((uid = createProject(new Project(projectName, "", ""))) == null) {
            projectNum++;
            projectName = "New Project " + projectNum;
        }
        return uid;
    }

    /**
     * removes a project from the project map.
     *
     * @param uid is the unique id of the project.
     * @return returns true if the project was removed, false if the project was not found.
     */
    public static boolean removeProject(String uid)
    {
        if(!datamodel.projects.containsKey(uid)) {
            return false;
        }
        datamodel.projects.remove(uid);
        // update current selected project
        if(datamodel.projects.size() > 0) {
            selectedProject = (String) datamodel.projects.keySet().toArray()[0];
        }
        else {
            selectedProject = null;
        }
        return true;
    }

    /**
     * Deletes the given project and deallocates the teams allocated to that project.
     * @param uid uid of the project
     */
    static public void deleteProject(String uid)
    {
        Project project = getProject(uid);
        String projectName = getProjectName(uid);
        CommandMessage message = new CommandMessage("Delete", ItemType.PROJECT, project);
        ArrayList<Allocation> devTeams = project.getDevTeams();
        GroupCommand grpCmd = new GroupCommand();
        grpCmd.setMessage(message);
        // Deallocates the teams in the project
        for (int i = 0; i < devTeams.size(); i++)
        {
            Allocation allocation = devTeams.get(i);
            String team = allocation.getTeamUID();
            LocalDate start = allocation.getStartDate();
            LocalDate end = allocation.getEndDate();
            grpCmd.addCommand(new DeallocateDevTeam(project, team, start, end));
        }
        //Deletes all releases associated with the project
        for (String releaseUID : project.getReleases()) {
            grpCmd.addCommand(new DeleteRelease(getRelease(releaseUID)));
        }
        // Deletes the Project
        grpCmd.addCommand(new DeleteProject(project));
        // Adds the grpCmd to the Undo/Redo stack
        manager.addCommand(grpCmd);
    }


    /**
     * adds a new release to the data model, if a release of that name
     * isn't already mapped.
     *
     * @param release is the VALIDATED release to add.
     * @return returns the uid of the release added, null if not added.
     */
    public static String createRelease(Release release)
    {
        if(itemShortNameNotUnique(release)) {
            return null;
        }
        manager.addCommand(new CreateRelease(datamodel.releases, release, selectedProject));
        return release.uid();
    }

    /**
     * Create an automatically generated release
     * @return Uid of the created release, null if not created
     */
    public static String createRelease()
    {
        int releaseNum = 1;
        String releaseName = "New Release " + releaseNum;
        String uid;
        String projectID = getCurrentProject();
        String date = LocalDate.now().toString();
        while ((uid = createRelease(new Release(releaseName, "", projectID, date))) == null) {
            releaseNum++;
            releaseName = "New Release " + releaseNum;
        }
        return uid;
    }

    /**
     * Deletes the given release
     * @param uid uid of the release
     */
    public static void deleteRelease(String uid)
    {
        Release release = getRelease(uid);
        manager.addCommand(new DeleteRelease(release));
    }

    /**
     * adds a new story to the data model, if a story of that name
     * isn't already mapped.
     *
     * @param story is the VALIDATED story to add.
     * @return returns the uid of the story added, null if not added.
     */
    public static String createStory(Story story)
    {
        if(itemShortNameNotUnique(story)) {
            return null;
        }
        datamodel.stories.put(story.uid(), story);
        manager.addCommand(new CreateStory(story));
        return story.uid();
    }

    /**
     * Create an automatically generated story
     * @param creatorUid uid of the person that created the story
     * @return Uid of the created story, null if not created
     */
    public static String createStory(String creatorUid)
    {
        int storyNum = 1;
        String storyName = "New Story " + storyNum;
        String uid;
        while ((uid = createStory(new Story(storyName, "", "", creatorUid))) == null) {
            storyNum++;
            storyName = "New Story " + storyNum;
        }
        return uid;
    }

    /**
     * Deletes the given story
     * @param uid uid of the story
     */
    public static void deleteStory(String uid)
    {
        Story story = getStory(uid);
        manager.addCommand(new DeleteStory(story));
    }

    /**
     * adds a new backlog to the data model, if a person of that name
     * isn't already mapped.
     *
     * @param backlog is the VALIDATED backlog to add.
     * @return returns the uid of the backlog added, null if not added.
     */
    public static String createBacklog(Backlog backlog)
    {
        if(itemShortNameNotUnique(backlog)) {
            return null;
        }
        manager.addCommand(new CreateBacklog(backlog));
        return backlog.uid();
    }

    /**
     * Create an automatically generated backlog
     * @return Uid of the created backlog, null if not created
     */
    public static String createBacklog()
    {
        int backlogNum = 1;
        String backlogName = "New Backlog " + backlogNum;
        String uid;
        while ((uid = createBacklog(new Backlog(backlogName, "", "", ""))) == null) {
            backlogNum++;
            backlogName = "New Backlog " + backlogNum;
        }
        return uid;
    }

    /**
     * Deletes the given backlog
     * @param uid uid of the backlog
     */
    public static void deleteBacklog(String uid)
    {
        manager.addCommand(new DeleteBacklog(uid));
    }

    /**
     * Creates a new sprint to the data model, if a sprint of that name
     * isn't already mapped.
     *
     * @param sprint is the VALIDATED sprint to create.
     * @return returns the uid of the sprint created, null if not created.
     */
    public static String createSprint(Sprint sprint)
    {
        if(itemShortNameNotUnique(sprint)) {
            return null;
        }
        manager.addCommand(new CreateSprint(sprint));
        return sprint.uid();
    }

    /**
     * Create an automatically generated sprint
     * @param project project
     * @param release release
     * @param startDate startdate
     * @param endDate enddate
     * @param team team
     * @return Uid of the created sprint, null if not created
     */
    public static String createSprint(String project, String release, String startDate,
                                      String endDate, String team)
    {
        int sprintNum = 1;
        String sprintName = "New Sprint " + sprintNum;
        String uid;
        while ((uid = createSprint(new Sprint(sprintName, "", "", project, release, startDate,
                endDate, team))) == null) {
            sprintNum++;
            sprintName = "New Sprint " + sprintNum;
        }
        return uid;
    }

    /**
     * Deletes the given sprint
     * @param uid uid of the sprint
     */
    public static void deleteSprint(String uid)
    {
        Sprint sprint = getSprint(uid);
        manager.addCommand(new DeleteSprint(sprint));
    }

    /**
     * @return the unique id of the selected project.
     */
    public static String getCurrentProject()
    {
        return selectedProject;
    }

    /**
     * sets the current project.
     *
     * @param uid is the unique id of the project.
     */
    public static void setCurrentProject(String uid)
    {
        if(datamodel.projects.keySet().contains(uid)) {
            selectedProject = uid;
        }
    }


    /**
     * Project names accessor method.
     *
     * @param  ids is an ArrayList of ids of projects to get the names of.
     * @return returns a ArrayList of all project names according to the ids given.
     */
    public static ArrayList<String> getProjectNames(ArrayList<String> ids)
    {
        ArrayList<String> names = new ArrayList<>();

        for (String uid : ids) {
            names.add(getProject(uid).getShortName());
        }
        return names;
    }

    /**
     * People names accessor method.
     *
     * @param  ids is an ArrayList of ids of people to get the names of.
     * @return returns a ArrayList of all people names according to the ids given.
     */
    public static ArrayList<String> getPeopleNames(ArrayList<String> ids)
    {
        ArrayList<String> names = new ArrayList<>();

        for (String uid : ids) {
            if (getPerson(uid) != null) {
                names.add(getPerson(uid).getShortName());
            }
        }
        return names;
    }

    /**
     * Skill names accessor method.
     *
     * @param  ids is an ArrayList of ids of skills to get the names of.
     * @return returns a ArrayList of all skill names according to the ids given.
     */
    public static ArrayList<String> getSkillNames(ArrayList<String> ids)
    {
        ArrayList<String> names = new ArrayList<>();

        for (String uid : ids) {
            names.add(getSkill(uid).getShortName());
        }
        return names;
    }

    /**
     * Team names accessor method.
     *
     * @param  ids is an ArrayList of ids of teams to get the names of.
     * @return returns a ArrayList of all team names according to the ids given.
     */
    public static ArrayList<String> getTeamNames(ArrayList<String> ids)
    {
        ArrayList<String> names = new ArrayList<>();

        for (String uid : ids) {
            names.add(getTeam(uid).getShortName());
        }
        return names;
    }

    /**
     * Story names accessor method.
     *
     * @return returns a ArrayList of all story names according to the ids given.
     */
    // TODO: get rid of this method?? It was different to all the others so I created another one below
    public static ArrayList<String> getStoryNames()
    {
        ArrayList<String> names = new ArrayList<>();

        for (String uid : Workspace.getStories()) {

            names.add(Workspace.getStoryName(uid));
        }
        return names;
    }

    /**
     * Story names accessor method
     *
     * @param ids an ArrayList of ids of stories to get the names of
     * @return an ArrayList of all story names according to the ids given
     */
    public static ArrayList<String> getStoryNames(ArrayList<String> ids)
    {
        ArrayList<String> names = new ArrayList<>();

        for (String uid: ids) {
            names.add(getStory(uid).getShortName());
        }
        return names;
    }

    /**
     * Release names accessor method.
     *
     * @param  ids is an ArrayList of ids of releases to get the names of.
     * @return returns a ArrayList of all release names according to the ids given.
     */
    public static ArrayList<String> getReleaseNames(ArrayList<String> ids)
    {
        ArrayList<String> names = new ArrayList<>();

        for (String uid : ids) {
            names.add(getRelease(uid).getShortName());

        }
        return names;
    }

    /**
     * Backlog names accessor method.
     *
     * @param  ids is an ArrayList of ids of backlogs to get the names of.
     * @return returns a ArrayList of all backlog names according to the ids given.
     */
    public static ArrayList<String> getBacklogNames(ArrayList<String> ids)
    {
        ArrayList<String> names = new ArrayList<>();

        for (String uid : ids) {
            names.add(getBacklog(uid).getShortName());
        }
        return names;
    }

    /**
     * Person name accessor method.
     *
     * @param  uid is the unique id of the person to get the name of.
     * @return returns a String with the person name according to the uid given.
     */
    public static String getPersonName(String uid)
    {
        if(datamodel.people.keySet().contains(uid)) {
            return getPerson(uid).getShortName();
        }
        return null;
    }

    /**
     * Project name accessor method.
     *
     * @param  uid is the unique id of the project to get the name of.
     * @return returns a String with the project name according to the uid given.
     */
    public static String getProjectName(String uid)
    {
        if(datamodel.projects.keySet().contains(uid)) {
            return getProject(uid).getShortName();
        }
        return null;
    }

    /**
     * Skill name accessor method.
     *
     * @param  uid is the unique id of the skill to get the name of.
     * @return returns a String with the skill name according to the uid given.
     */
    public static String getSkillName(String uid)
    {
        if(datamodel.skills.keySet().contains(uid)) {
            return getSkill(uid).getShortName();
        }
        return null;
    }


    /**
     * Team name accessor method.
     *
     * @param  uid is the unique id of the team to get the name of.
     * @return returns a String with the team name according to the uid given.
     */
    public static String getTeamName(String uid)
    {
        if(datamodel.teams.keySet().contains(uid)) {
            return getTeam(uid).getShortName();
        }
        return null;
    }

    /**
     * Story name accessor method.
     *
     * @param  uid is the unique id of the story to get the name of.
     * @return returns a String with the story name according to the uid given.
     */
    public static String getStoryName(String uid)
    {
        if(datamodel.stories.keySet().contains(uid)) {
            return getStory(uid).getShortName();
        }
        return null;
    }

    /**
     * Release name accessor method.
     *
     * @param  uid is the unique id of the release to get the name of.
     * @return returns a String with the release name according to the uid given.
     */
    public static String getReleaseName(String uid)
    {
        if(datamodel.releases.keySet().contains(uid)) {
            return getRelease(uid).getShortName();
        }
        return null;
    }

    /**
     * Backlog name accessor method.
     *
     * @param  uid is the unique id of the backlog to get the name of.
     * @return returns a String with the backlog name according to the uid given.
     */
    public static String getBacklogName(String uid)
    {
        if(datamodel.backlogs.keySet().contains(uid)) {
            return getBacklog(uid).getShortName();
        }
        return null;
    }

    /**
     * Sprint name accessor method
     *
     * @param uid is the unique id of the sprint to get the name of
     * @return retuns a string with the sprint name according to the uid given
     */
    public static String getSprintName(String uid)
    {
        if(datamodel.sprints.keySet().contains(uid)) {
            return getSprint(uid).getShortName();
        }
        return null;
    }

    /**
     * Sprint names accessor method.
     *
     * @param  ids is an ArrayList of ids of sprints to get the names of.
     * @return returns a ArrayList of all sprint names according to the ids given.
     */
    public static ArrayList<String> getSprintNames(ArrayList<String> ids)
    {
        ArrayList<String> names = new ArrayList<>();

        for (String uid : ids) {
            names.add(getSprint(uid).getShortName());
        }
        return names;
    }

    /***************************************************
     *
     * randoms
     *
     ***************************************************/

    /**
     * used to check whether an item is mapped already, with the same short name.
     * @param <T> item object to check
     * @param item  item to check.
     * @return  returns true if the proposed new shortName is in use already, false if it is not.
     */
    public static <T extends Item> boolean itemShortNameNotUnique(T item)
    {
        String shortName = item.getShortName();

        for (Item _item: datamodel.getItems().values()) {
            if (_item.getShortName().equals(shortName) &&
                    item.getClass().equals(_item.getClass())) {
                return true;
            }
        }
        return false;
    }

    /**
     * Check if an item with a given short name already exists
     * @param item the item
     * @param <T> item object to check
     * @param shortName Short Name to check for uniqueness
     * @return True if in use, false if not
     */
    public static <T extends Item> boolean shortNameNotUnique(String shortName, T item) {
        for(Item _item : datamodel.getItems().values()) {
            if(_item.getShortName().equals(shortName) &&
                    item.getClass().equals(_item.getClass())){
                return true;
            }
        }
        return false;
    }

    /**
     * Returns the people in the project that are not in a team
     * @return returns an Arraylist of unique ids of people not in a team
     */
    public static ArrayList<String> getPeopleWithoutTeam()
    {
        ArrayList<String> peopleWithoutTeam = new ArrayList<>();
        for(String uid : datamodel.people.keySet()) {
            Person p = getPerson(uid);
            if (!p.isInTeam()) {
                peopleWithoutTeam.add(uid);
            }
        }
        return peopleWithoutTeam;
    }

    /**
     * Returns the teams which aren't currently allocated to a project
     * @return ArrayList of unique ids of teams not allocated to a project
     */
    public static ArrayList<String> getTeamsWithoutProject()
    {
        ArrayList<String> teamsWithoutProject = new ArrayList<>();
        for(String uid : datamodel.teams.keySet()) {
            Team t = getTeam(uid);
            if (!t.isAllocated()) {
                teamsWithoutProject.add(uid);
            }
        }
        return teamsWithoutProject;
    }

    /**
     * Returns the projects that have backlogs associated with them
     * @return ArrayList of unique id's of projects that have backlogs
     */
    public static ArrayList<String> getProjectsWithBacklog()
    {
        ArrayList<String> projectsWithBacklog = new ArrayList<>();
        for (String uid: datamodel.projects.keySet()) {
            Project project = getProject(uid);
            if(project.getBacklog() != null) {
                projectsWithBacklog.add(uid);
            }
        }
        return projectsWithBacklog;
    }

    /**
     * Get the filename as a string. Returns null if no file is found
     * @return filename as String
     */
    public static String getFileString()
    {
        try {
            return appdata.getProjectFile().toString();
        }
        catch (NullPointerException e) {
            Debug.run(() -> e.printStackTrace());
            return null;
        }
    }

    /**
     * Indicates whether there are unsaved changes.
     * @return True if no changes, False if there are changes.
     */
    public static boolean getSaved()
    {
        return (manager.getSaved()/* && appdata.getProjectFile() != null*/);
    }

    /***************************************************
     *
     * saving and loading
     *
     ***************************************************/

    /**
     * loads the last used project on startup, if possible.
     * @param file is the appdata file to load from.
     * @throws FileNotFoundException an exception
     * @throws WorkspaceVersionMismatchException an exception
     * @throws WorkspaceLoadException an exception
     */
    public static void loadOnStart(String file) throws FileNotFoundException, WorkspaceVersionMismatchException, WorkspaceLoadException
    {
        appdata = Appdata.load(file);
        File projectfile = appdata.getProjectFile();
        if(projectfile != null) {
            loadData(projectfile);
        }
    }

    /**
     * loads data into the data model from the specified file.
     *
     * Note: loaded data will overwrite any existing data.
     *
     * @param file is the project file to load from.
     * @throws FileNotFoundException an exception
     * @throws WorkspaceVersionMismatchException an exception
     * @throws WorkspaceLoadException an exception
     */
	public static void loadData(File file) throws FileNotFoundException, WorkspaceVersionMismatchException, WorkspaceLoadException
	{
        if(file == null){
            throw new FileNotFoundException("file was not specified");
        }

        Data loaded = (Data) SaverLoader.load(file, new TypeToken<Data>() {}.getType());
		if(loaded != null) {
            if(!loaded.workspace_version.equals(appdata.WORKSPACE_VERSION)){
                Debug.println("Application workspace version (" +
                                loaded.workspace_version +
                                ") is incompatible with data file version (" +
                                appdata.WORKSPACE_VERSION + ")");
                throw new WorkspaceVersionMismatchException("The application is at version " + appdata.WORKSPACE_VERSION +
                                                    ". Can't open files of version " + loaded.workspace_version);
            }
            else {
                datamodel = loaded;
                if(datamodel.projects.keySet().size() > 0) {
                    selectedProject = (String) datamodel.projects.keySet().toArray()[0];
                }
                manager.clear();
                appdata.setProjectFile(file);

                // Makes sure allocated teams are updated properly
                for (String uid: datamodel.projects.keySet()) {
                    Project p = getProject(uid);
                    p.setDevTeams(p.getDevTeams());
                    p.updateTeams();
                }

                tryInitPoAndSm();
            }
		}
        else {
            throw new WorkspaceLoadException("Couldnt load file " + file.getPath());
        }
	}

    public static void tryInitPoAndSm()
    {
        boolean haveSm = false;
        boolean havePo = false;
        Skill sm = Skill.scrumMaster();
        Skill po = Skill.productOwner();

        for(Skill skill : datamodel.skills.values()) {
            if(skill.getShortName().equals(sm.getShortName())){
                haveSm = true;
            }
            if(skill.getShortName().equals(po.getShortName())){
                havePo = true;
            }
        }

        if(!haveSm) {
            addSkill(sm);
        }

        if(!havePo) {
            addSkill(po);
        }
    }

    /**
     * save the data model to the specified file.
     *
     * @param file is the file to save to.
     * @throws java.io.IOException an exception
     */
    public static void saveData(File file) throws IOException
    {
        if(file != null) {
            appdata.setProjectFile(file);
            saveData();
        }
    }

    /**
     * save the data model to the last loaded file.
     *
     * @return true if a default save locations exists, false otherwise.
     * @throws java.io.IOException an exception
     */
    public static boolean saveData() throws IOException
    {
        File f = appdata.getProjectFile();
        if(f != null) {
            SaverLoader.save(f, datamodel);
            manager.setSavedIndex();
            return true;
        }
        return false;
    }


    /***************************************************
     *
     * undo / redo
     *
     ***************************************************/

    /**
     * Undoes the last action
     * @return true if you can still undo
     */
    public static boolean undo() {
        CommandMessage message = manager.getUndoMessage();
        manager.undo();
        return manager.canUndo();
    }

    /**
     * Redoes the last action
     * @return true if you can still redo
     */
    public static boolean redo() {
        CommandMessage message = manager.getRedoMessage();
        manager.redo();
        return manager.canRedo();
    }

    /**
     * Returns a message about the current undo command.
     * @return CommandMessage object with various command attributes
     */
    public static CommandMessage getUndoMessage() {
        return manager.getUndoMessage();
    }


    /**
     * Returns a message about the current redo command.
     * @return CommandMessage object with various command attributes
     */
    public static CommandMessage getRedoMessage() {
        return manager.getRedoMessage();
    }


    /**
     * Manually add an undoable/redoable command to the workspace's command manager.
     * @param command Command to add and execute
     */
    public static void addCommand(Command command) {
        manager.addCommand(command);
    }


    /**
     * Return to the last save state.
     * @return Message about the final action
     */
    public static CommandMessage revert() {
        return manager.revert();
    }
}