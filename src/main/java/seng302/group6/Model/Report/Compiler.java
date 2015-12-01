package seng302.group6.Model.Report;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;
import seng302.group6.Model.ItemClasses.*;
import seng302.group6.Model.Report.Elements.*;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Used to compile the XML reports.
 * Behaviour as follows:
 * Create a new Compiler and then setting the appropriate booleans using the set commands
 * Use x.compile() to generate the report with given settings
 * Use x.writeFile(file) to write the report to the given file
 * Created by dan on 23/05/15.
 */
public class Compiler
{

    /*
    Inclusion booleans set to decide what content is written to the report
     */
    // Project Section
    boolean includeAllProjects = false;
    boolean includeTeamsInAllProjects = false;
    boolean includePeopleInTeamsInAllProjects = false;
    boolean includeSkillsInPeopleInTeamsInAllProjects = false;
    boolean includeReleasesInAllProjects = false;

    boolean includeUnallocatedTeams = false;
    boolean includePeopleInUnallocatedTeams = false;
    boolean includeSkillsInPeopleInUnallocatedTeams = false;

    boolean includeUnallocatedPeople = false;
    boolean includeSkillsInUnallocatedPeople = false;

    // Backlog Section
    boolean includeAllBacklogs = false;
    boolean includeStoriesInAllBacklogs = false;
    boolean includeACsInStoriesInAllBacklogs = false;
    boolean includeDepsInStoriesInAllBacklogs = false;
    boolean includeTasksInStoriesInAllBacklogs = false;

    boolean includeUnallocatedStories = false;
    boolean includeACsInUnallocatedStories = false;
    boolean includeDepsInUnallocatedStories = false;
    boolean includeTasksInUnallocatedStories = false;
    // Sprint Section
    boolean includeAllSprints = false;
    boolean includeTeamInAllSprints = false;
    boolean includePeopleInTeamInAllSprints = false;
    boolean includeSkillsInPeopleInTeamInAllSprints = false;
    boolean includeStoriesInAllSprints = false;
    boolean includeACsInStoriesInAllSprints = false;
    boolean includeDepsInStoriesInAllSprints = false;
    boolean includeTasksInStoriesInAllSprints = false;
    boolean includePeopleInTasksInStoriesInAllSprints = false;
    boolean includeEffortInTasksInStoriesInAllSprints = false;
    // Other Section
    boolean includeAllTeams = false;
    boolean includePeopleInAllTeams = false;
    boolean includeSkillsInPeopleInAllTeams = false;

    boolean includeAllPeople = false;
    boolean includeSkillsInAllPeople = false;

    boolean includeAllSkills = false;

    boolean includeAllStories = false;
    boolean includeACsInAllStories = false;
    boolean includeDepsInAllStories = false;
    boolean includeTasksInAllStories = false;

    static Document document;

    public Compiler()
    {
        //Do nothing
    }

    /**
     * Creates the report with appropriate inclusions
     * - Header
     * - All Projects
     * - Unallocated Teams
     * - Unallocated People
     * - All Backlogs
     * - Unallocated Stories
     * - All Sprints
     * - All Teams
     * - All People
     * - All Skills
     * - All Stories
     */
    public void compile() {
        HeaderElement header = new HeaderElement();
        document = new Document(header.getHeader());

        if (includeAllProjects) { addAllProjects(); }
        if (includeUnallocatedTeams) { addUnallocatedTeams(); }
        if (includeUnallocatedPeople) { addUnallocatedPeople(); }
        if (includeAllBacklogs) { addAllBacklogs(); }
        if (includeUnallocatedStories) { addUnallocatedStories(); }
        if (includeAllSprints) { addAllSprints(); }
        if (includeAllTeams) { addAllTeams(); }
        if (includeAllPeople) { addAllPeople(); }
        if (includeAllSkills) { addAllSkills(); }
        if (includeAllStories) { addAllStories(); }
    }

    /**
     * Writes the report to the given filename
     * @param fileName filename, including the path, of the file to be written
     * @throws java.io.IOException an exception
     */
    public static void writeFile(String fileName) throws IOException
    {
        // Write the formatted XML file to specified filename
        XMLOutputter xmlOutputter = new XMLOutputter();
        xmlOutputter.setFormat(Format.getPrettyFormat());
        xmlOutputter.output(document, new FileWriter(fileName));
    }

//***********************************************************************************************************************************************HERE

    /**
     * Adds all projects within the workspace to the report
     * Optional inclusions:
     * - Releases
     * - Teams
     */
    private void addAllProjects()
    {
        Element parent = new Element("Projects");
        for (String uid : Workspace.getProjects()) {
            ProjectElement projectElement = new ProjectElement();
            Element projectChild = projectElement.getProject(uid);
            if (includeTeamsInAllProjects) {
                projectChild.addContent(getTeamElements("Teams", getProjectTeams(uid),
                        includePeopleInTeamsInAllProjects, includeSkillsInPeopleInTeamsInAllProjects));
            }
            if (includeReleasesInAllProjects){
                projectChild.addContent(getReleaseElements(uid));
            }
            parent.addContent(projectChild);
        }
        document.getRootElement().addContent(parent);
    }


    /**
     * Gets all Teams from a given list and returns them as children to an element
     * @param elementName The name for the element as shown in the xml report (e.g. teams)
     * @param currentList the list to get teams from (e.g. list of teams in a project)
     * @param peopleInclusion whether to include people elements
     * @param skillsInclusion whether to include skills elements
     * @return the element containing all of the teams in the list
     */
    private Element getTeamElements(String elementName, ArrayList<String> currentList,
                                    Boolean peopleInclusion, Boolean skillsInclusion)
    {
        Element parent = new Element(elementName);

        for (String uid : currentList) {
            TeamElement teamElement = new TeamElement();
            Element teamChild = teamElement.getTeam(uid);
            if (peopleInclusion) {
                teamChild.addContent(addTeamPeople(uid, skillsInclusion));
            }
            parent.addContent(teamChild);
        }
        return parent;
    }

    /**
     * Adds all people in the workspace to the report
     */
    private void addAllPeople()
    {
        document.getRootElement().addContent(getPeopleElements("All-People", Workspace.getPeople(), includeSkillsInAllPeople));
    }

    /**
     * Passes the correct parameters to getPeopleElements() for getting all people in a team then returns the element
     * @param uid of the Team containing the people
     * @param skillsInclusion whether to include skills elements
     * @return the element containing the people from specified team
     */
    private Element addTeamPeople(String uid, Boolean skillsInclusion)
    {
        return getPeopleElements("Team-Members", Workspace.getTeam(uid).getPeople(), skillsInclusion);
    }

    /**
     * Adds all people in the workspace that are not allocated to a team to the report
     */
    private void addUnallocatedPeople()
    {
        document.getRootElement().addContent(getPeopleElements("Unallocated-People",  Workspace.getPeopleWithoutTeam(),
                includeSkillsInUnallocatedPeople));
    }

    /**
     * Gets all people from the given list and returns them as children to an element
     * @param elementName The name for the element as shown in the xml report (e.g. team-members)
     * @param currentList The list to get people from (e.g. list of people in a team)
     * @param skillsInclusion whether to include skills elements
     * @return the element containing all of the people in the list
     * Optional inclusions:
     * - Skills
     */
    private Element getPeopleElements(String elementName, ArrayList<String> currentList, Boolean skillsInclusion)
    {
        Element parent = new Element(elementName);

        for (String uid : currentList) {
            PersonElement personElement = new PersonElement();
            Element personChild = personElement.getPerson(uid);
            if (skillsInclusion) {
                personChild.addContent(getSkillElements("Skills", Workspace.getPerson(uid).getSkills()));
            }
            parent.addContent(personChild);
        }
        return parent;
    }

    /**
     * Add all teams to the report
     */
    private void addAllTeams()
    {
        document.getRootElement().addContent(getTeamElements("All-Teams", Workspace.getTeams(), includePeopleInAllTeams,
                includeSkillsInPeopleInAllTeams));
    }

    /**
     * Add all unallocated teams to the report
     */
    private void addUnallocatedTeams()
    {
        document.getRootElement().addContent(getTeamElements("Unallocated-Teams", Workspace.getTeamsWithoutProject(),
                includePeopleInUnallocatedTeams, includeSkillsInPeopleInUnallocatedTeams));
    }

    /**
     * Get a list of all teams in a project
     * @param uid of the project to get teams from
     * @return the list of teams
     */
    private ArrayList getProjectTeams(String uid)
    {
        ArrayList<String> projects = new ArrayList<>();
        for (Allocation team : Workspace.getProject(uid).getCurrentDevTeams()){
            String projectTeam = team.getTeamUID();
            projects.add(projectTeam);
        }
        return projects;
    }

    /**
     * Add all skills to the report
     */
    private void addAllSkills()
    {
        document.getRootElement().addContent(getSkillElements("All-Skills", Workspace.getSkills()));
    }

    /**
     * Gets all Skills from a given list and returns them as children to an element
     * @param elementName The name to give the parent element (e.g. Skills)
     * @param currentList the list to get the skills from (e.g. A persons skills)
     * @return the element containing skills as children
     */
    private Element getSkillElements(String elementName, ArrayList<String> currentList)
    {
        Element parent = new Element(elementName);

        for (String uid : currentList) {
            SkillElement skillElement = new SkillElement();
            parent.addContent(skillElement.getSkill(uid));
        }
        return parent;
    }

    /**
     * Get all releases for a given project and return them as children to an element
     * @param uid of the project
     * @return the element containing the releases as children
     */
    private Element getReleaseElements(String uid)
    {
        Element parent = new Element("Releases");

        for (String release : Workspace.getProject(uid).getReleases()) {
            ReleaseElement releaseElement = new ReleaseElement();
            parent.addContent(releaseElement.getRelease(release));
        }

        return parent;
    }

    /**
     * Add all backlogs to the report
     */
    private void addAllBacklogs()
    {
        document.getRootElement().addContent(
                getBacklogElements("All-Backlogs", Workspace.getBacklogs())
        );
    }

    /**
     * Add all stories to the report
     */
    private void addAllStories()
    {
        document.getRootElement().addContent(
                getStoryElements("All-Stories", Workspace.getStories(), includeACsInAllStories,
                        includeDepsInAllStories, includeTasksInAllStories)
        );
    }

    /**
     * Add all sprints to the report
     */
    private void addAllSprints()
    {
        document.getRootElement().addContent(
                getSprintElements("All-Sprints", Workspace.getSprints())
        );
    }

    /**
     * Add all unallocated stories to the report
     */
    private void addUnallocatedStories()
    {
        document.getRootElement().addContent(
                getStoryElements("Unallocated-Stories", Workspace.getStoriesWithoutBacklog(),
                        includeACsInUnallocatedStories, includeDepsInUnallocatedStories,
                        includeTasksInUnallocatedStories));
    }

    /**
     * Get all backlogs from a given list and return them as children to an element
     * @param elementName name of the element (e.g. Backlogs)
     * @param currentList list of the backlogs to create child elements (e.g. backlogs in a project)
     * @return the parent element containing the backlogs as children
     * Optional Inclusions
     * - Stories
     */
    private Element getBacklogElements(String elementName, ArrayList<String> currentList)
    {
        Element parent = new Element(elementName);

        for (String uid : currentList) {
            BacklogElement backlogElement = new BacklogElement();
            Element backlogChild = backlogElement.getBacklog(uid);
            if (includeStoriesInAllBacklogs) {
                backlogChild.addContent(getStoryElements("Stories", Workspace.getBacklog(uid).getStories(),
                        includeACsInStoriesInAllBacklogs, includeDepsInStoriesInAllBacklogs,
                        includeTasksInStoriesInAllBacklogs));
            }
            parent.addContent(backlogChild);
        }
        return parent;
    }

    /**
     * Get all Stories from a given list and return them as children to an element
     * @param elementName name of the element (e.g. Stories)
     * @param currentList list of the stories to create child elements (e.g. stories in a backlog)
     * @param ACInclusion whether to include AC elements
     * @param DepsInclusion whether to include deps elements
     * @param TasksInclusion whether to include task elements
     * @return the parent element containing the backlogs as children
     * Optional Inclusions
     * - Acceptance Criteria
     */
    private Element getStoryElements(String elementName, ArrayList<String> currentList, Boolean ACInclusion,
                                     Boolean DepsInclusion, Boolean TasksInclusion)
    {
        Element parent = new Element(elementName);

        for (String uid : currentList) {
            StoryElement storyElement = new StoryElement();
            Element storyChild = storyElement.getStory(uid);
            if (ACInclusion) {
                storyChild.addContent(getAcceptanceCriteriaElements(
                        "AcceptanceCriteria",
                        Workspace.getStory(uid).getAllAcceptanceCriteria()
                ));
            }
            if (DepsInclusion) {
                storyChild.addContent(getDependencyElements(
                        "Dependent-On",
                        new ArrayList<>(Workspace.getStory(uid).getDependencies())
                ));
            }
            if (TasksInclusion) {
                if (Workspace.getStory(uid).getAllTasks() != null) {
                    storyChild.addContent(getTaskElements(
                            "Tasks",
                            Workspace.getStory(uid).getAllTasks()
                    ));
                } else {
                    storyChild.addContent(new Element("Tasks"));
                }
            }
            parent.addContent(storyChild);
        }
        return parent;
    }

    /**
     * Get all Sprints from a given list and return them as children to an element
     * @param elementName name of the element(e.g. Sprints)
     * @param currentList list of the sprints to create child elements (e.g. Sprints in a Workspace)
     * @return the parent element
     */
    private Element getSprintElements(String elementName, ArrayList<String> currentList)
    {
        Element parent = new Element(elementName);

        for (String uid : currentList) {
            SprintElement sprintElement = new SprintElement();
            Element sprintChild = sprintElement.getSprint(uid);

            if (includeTeamInAllSprints) {
                String teamUID = Workspace.getSprint(uid).getAssociatedTeam();
                ArrayList<String> team = new ArrayList();
                team.add(teamUID);
                sprintChild.addContent(getTeamElements("Team", team,
                        includePeopleInTeamInAllSprints, includeSkillsInPeopleInTeamInAllSprints));
            }


            if (includeStoriesInAllSprints) {
                sprintChild.addContent(getSprintStoryElements("Stories", Workspace.getSprint(uid).getStories(),
                        includeACsInStoriesInAllSprints, includeDepsInStoriesInAllSprints,
                        includeTasksInStoriesInAllSprints, includePeopleInTasksInStoriesInAllSprints,
                        false, includeEffortInTasksInStoriesInAllSprints));
            }
            parent.addContent(sprintChild);
        }
        return parent;
    }

    /**
     * Get all Stories in Sprints from a given list and return them as children to an element
     * @param elementName name of the element(e.g. Sprints)
     * @param currentList list of the stories to create child elements
     * @param ACInclusion whether to include AC elements
     * @param DepsInclusion whether to include dep elements
     * @param taskInclusion whether to include task elements
     * @param peopleInclusion whether to include people elements
     * @param skillsInclusion whether to include skills elements
     * @param effortInclusion whether to include effort elements
     * @return the parent element
     */
    private Element getSprintStoryElements(String elementName, ArrayList<String> currentList, Boolean ACInclusion,
                                           Boolean DepsInclusion, Boolean taskInclusion, Boolean peopleInclusion,
                                           Boolean skillsInclusion, Boolean effortInclusion)
    {
        Element parent = new Element(elementName);

        for (String uid : currentList) {
            StoryElement storyElement = new StoryElement();
            Element storyChild = storyElement.getSprintStory(uid);
            if (ACInclusion) {
                storyChild.addContent(getAcceptanceCriteriaElements(
                        "AcceptanceCriteria",
                        Workspace.getStory(uid).getAllAcceptanceCriteria()
                ));
            }
            if (DepsInclusion) {
                storyChild.addContent(getDependencyElements(
                        "Dependent-On",
                        new ArrayList<>(Workspace.getStory(uid).getDependencies())
                ));
            }
            if (taskInclusion) {
                if (Workspace.getStory(uid).getAllTasks() != null) {
                    storyChild.addContent(getSprintTaskElements("Tasks", Workspace.getStory(uid).getAllTasks(), peopleInclusion, skillsInclusion, effortInclusion));
                } else {
                    storyChild.addContent(new Element("Tasks"));
                }
            }
            parent.addContent(storyChild);
        }
        return parent;
    }

    /**
     * Get all Tasks in Sprints from a given list and return them as children to an element
     * @param elementName name of the element(e.g. Sprints)
     * @param currentList list of the stories to create child elements
     * @param peopleInclusion whether to include people elements
     * @param skillsInclusion whether to include skills elements
     * @param effortInclusion whether to include effort elements
     * @return Uid of the created sprint, null if not created
     */
    private Element getSprintTaskElements(String elementName, List<Task> currentList, Boolean peopleInclusion,
                                          Boolean skillsInclusion, Boolean effortInclusion)
    {
        Element parent = new Element(elementName);

        for (Task task : currentList) {
            TaskElement taskElement = new TaskElement();
            Element storyChild = taskElement.getSprintTask(task);
            if (peopleInclusion) {
                if (task.getPeople() != null) {
                    storyChild.addContent(getPeopleElements("Assigned-People", task.getPeople(), skillsInclusion));
                } else {
                    storyChild.addContent(new Element("Assigned-People"));
                }
            }
            if(effortInclusion) {
                if (!task.getEffortList().isEmpty()) {
                    storyChild.addContent(getSprintTaskEffortElements("Effort-Logs", task.getEffortList()));
                } else {
                    storyChild.addContent(new Element("Effort-Logs"));
                }
            }
            parent.addContent(storyChild);
        }
        return parent;
    }

    private Element getSprintTaskEffortElements(String elementName, List<Effort> currentList) {
        Element parent = new Element(elementName);

        for (Effort effort : currentList) {
            EffortElement effortElement = new EffortElement();
            Element storyChild = effortElement.getEffortLogged(effort);
            parent.addContent(storyChild);
        }
        return parent;
    }

    /**
     * Get all Acceptance Criteria from a given list and return them as children to an element
     * @param elementName name of the element (e.g. AcceptanceCriteria)
     * @param currentList list of the Acceptance Criteria to create child elements from (e.g. AC's to a story)
     * @return the parent element containing all the AC's as children
     */
    private Element getAcceptanceCriteriaElements(String elementName,
                                                  List<AcceptanceCriteria> currentList)
    {
        Element parent = new Element(elementName);

        for (AcceptanceCriteria ac : currentList) {
            AcceptanceCriteriaElement acElement = new AcceptanceCriteriaElement();
            parent.addContent(acElement.getAcceptanceCriteria(ac));
        }
        return parent;
    }

    /**
     * Get all Dependencies from a given list and return them as children to an element
     * @param elementName name of the element (e.g. Dependency)
     * @param currentList list of the Dependencies to create child elements from (e.g. Dep's to a story)
     * @return the parent element containing all the Dep's as children
     */
    private Element getDependencyElements(String elementName, List<String> currentList)
    {
        Element parent = new Element(elementName);

        for (String dependency : currentList) {
            DependencyElement dependencyElement = new DependencyElement();
            parent.addContent(dependencyElement.getDependency(dependency));
        }
        return parent;
    }

    /**
     * Get all Tasks from a given list and return them as children to an element
     * @param elementName name of the element (e.g. Task)
     * @param currentList list of the Task to create child elements from (e.g. Task's to a story)
     * @return the parent element containing all the Task's as children
     */
    private Element getTaskElements(String elementName, List<Task> currentList)
    {
        Element parent = new Element(elementName);

        for (Task task : currentList) {

            TaskElement taskElement = new TaskElement();
            parent.addContent(taskElement.getTask(task));
        }
        return parent;
    }
//***********************************************************************************************************************************************TO HERE
    /**
     * The following setters are all used to specify what is and isn't included in the requested report
     * the booleans are set after the Compiler object is created and before it is compiled using compile()
     * @param includeAllProjects True to include projects, false to exclude them
     */
    public void setIncludeAllProjects(boolean includeAllProjects) {
        this.includeAllProjects = includeAllProjects;
    }

    /**
     * Setter
     * @param includeTeamsInAllProjects inclusion boolean
     */
    public void setIncludeTeamsInAllProjects(boolean includeTeamsInAllProjects) {
        this.includeTeamsInAllProjects = includeTeamsInAllProjects;
    }

    /**
     *
     * @param includePeopleInTeamsInAllProjects inclusion boolean
     */
    public void setIncludePeopleInTeamsInAllProjects(boolean includePeopleInTeamsInAllProjects) {
        this.includePeopleInTeamsInAllProjects = includePeopleInTeamsInAllProjects;
    }

    /**
     *
     * @param includeSkillsInPeopleInTeamsInAllProjects inclusion boolean
     */
    public void setIncludeSkillsInPeopleInTeamsInAllProjects(boolean includeSkillsInPeopleInTeamsInAllProjects) {
        this.includeSkillsInPeopleInTeamsInAllProjects = includeSkillsInPeopleInTeamsInAllProjects;
    }

    /**
     *
     * @param includeReleasesInAllProjects inclusion boolean
     */
    public void setIncludeReleasesInAllProjects(boolean includeReleasesInAllProjects) {
        this.includeReleasesInAllProjects = includeReleasesInAllProjects;
    }

    /**
     *
     * @param includeUnallocatedTeams inclusion boolean
     */
    public void setIncludeUnallocatedTeams(boolean includeUnallocatedTeams) {
        this.includeUnallocatedTeams = includeUnallocatedTeams;
    }

    /**
     *
     * @param includePeopleInUnallocatedTeams inclusion boolean
     */
    public void setIncludePeopleInUnallocatedTeams(boolean includePeopleInUnallocatedTeams) {
        this.includePeopleInUnallocatedTeams = includePeopleInUnallocatedTeams;
    }

    /**
     *
     * @param includeSkillsInPeopleInUnallocatedTeams inclusion boolean
     */
    public void setIncludeSkillsInPeopleInUnallocatedTeams(boolean includeSkillsInPeopleInUnallocatedTeams) {
        this.includeSkillsInPeopleInUnallocatedTeams = includeSkillsInPeopleInUnallocatedTeams;
    }

    /**
     *
     * @param includeUnallocatedPeople inclusion boolean
     */
    public void setIncludeUnallocatedPeople(boolean includeUnallocatedPeople) {
        this.includeUnallocatedPeople = includeUnallocatedPeople;
    }

    /**
     *
     * @param includeSkillsInUnallocatedPeople inclusion boolean
     */
    public void setIncludeSkillsInUnallocatedPeople(boolean includeSkillsInUnallocatedPeople) {
        this.includeSkillsInUnallocatedPeople = includeSkillsInUnallocatedPeople;
    }

    /**
     *
     * @param includeAllBacklogs inclusion boolean
     */
    public void setIncludeAllBacklogs(boolean includeAllBacklogs) {
        this.includeAllBacklogs = includeAllBacklogs;
    }

    /**
     *
     * @param includeStoriesInAllBacklogs inclusion boolean
     */
    public void setIncludeStoriesInAllBacklogs(boolean includeStoriesInAllBacklogs) {
        this.includeStoriesInAllBacklogs = includeStoriesInAllBacklogs;
    }

    /**
     *
     * @param includeACsInStoriesInAllBacklogs inclusion boolean
     */
    public void setIncludeACsInStoriesInAllBacklogs(boolean includeACsInStoriesInAllBacklogs) {
        this.includeACsInStoriesInAllBacklogs = includeACsInStoriesInAllBacklogs;
    }

    /**
     *
     * @param includeDepsInStoriesInAllBacklogs inclusion boolean
     */
    public void setIncludeDepsInStoriesInAllBacklogs(boolean includeDepsInStoriesInAllBacklogs) {
        this.includeDepsInStoriesInAllBacklogs = includeDepsInStoriesInAllBacklogs;
    }

    /**
     *
     * @param includeTasksInStoriesInAllBacklogs inclusion boolean
     */
    public void setIncludeTasksInStoriesInAllBacklogs(boolean includeTasksInStoriesInAllBacklogs) {
        this.includeTasksInStoriesInAllBacklogs = includeTasksInStoriesInAllBacklogs;
    }

    /**
     *
     * @param includeUnallocatedStories inclusion boolean
     */
    public void setIncludeUnallocatedStories(boolean includeUnallocatedStories) {
        this.includeUnallocatedStories = includeUnallocatedStories;
    }

    /**
     *
     * @param includeACsInUnallocatedStories inclusion boolean
     */
    public void setIncludeACsInUnallocatedStories(boolean includeACsInUnallocatedStories) {
        this.includeACsInUnallocatedStories = includeACsInUnallocatedStories;
    }

    /**
     *
     * @param includeDepsInUnallocatedStories inclusion boolean
     */
    public void setIncludeDepsInUnallocatedStories(boolean includeDepsInUnallocatedStories) {
        this.includeDepsInUnallocatedStories = includeDepsInUnallocatedStories;
    }

    /**
     *
     * @param includeTasksInUnallocatedStories inclusion boolean
     */
    public void setIncludeTasksInUnallocatedStories(boolean includeTasksInUnallocatedStories) {
        this.includeTasksInUnallocatedStories = includeTasksInUnallocatedStories;
    }

    /**
     *
     * @param includeAllSprints inclusion boolean
     */
    public void setIncludeAllSprints(boolean includeAllSprints) {
        this.includeAllSprints = includeAllSprints;
    }

    /**
     *
     * @param includeTeamInAllSprints inclusion boolean
     */
    public void setIncludeTeamInAllSprints(boolean includeTeamInAllSprints) {
        this.includeTeamInAllSprints = includeTeamInAllSprints;
    }

    /**
     *
     * @param includePeopleInTeamInAllSprints inclusion boolean
     */
    public void setIncludePeopleInTeamInAllSprints(boolean includePeopleInTeamInAllSprints) {
        this.includePeopleInTeamInAllSprints = includePeopleInTeamInAllSprints;
    }

    /**
     *
     * @param includeSkillsInPeopleInTeamInAllSprints inclusion boolean
     */
    public void setIncludeSkillsInPeopleInTeamInAllSprints(boolean includeSkillsInPeopleInTeamInAllSprints) {
        this.includeSkillsInPeopleInTeamInAllSprints = includeSkillsInPeopleInTeamInAllSprints;
    }

    /**
     *
     * @param includeStoriesInAllSprints inclusion boolean
     */
    public void setIncludeStoriesInAllSprints(boolean includeStoriesInAllSprints) {
        this.includeStoriesInAllSprints = includeStoriesInAllSprints;
    }

    /**
     *
     * @param includeACsInStoriesInAllSprints inclusion boolean
     */
    public void setIncludeACsInStoriesInAllSprints(boolean includeACsInStoriesInAllSprints) {
        this.includeACsInStoriesInAllSprints = includeACsInStoriesInAllSprints;
    }

    /**
     *
     * @param includeDepsInStoriesInAllSprints inclusion boolean
     */
    public void setIncludeDepsInStoriesInAllSprints(boolean includeDepsInStoriesInAllSprints) {
        this.includeDepsInStoriesInAllSprints = includeDepsInStoriesInAllSprints;
    }

    /**
     *
     * @param includeTasksInStoriesInAllSprints inclusion boolean
     */
    public void setIncludeTasksInStoriesInAllSprints(boolean includeTasksInStoriesInAllSprints) {
        this.includeTasksInStoriesInAllSprints = includeTasksInStoriesInAllSprints;
    }

    /**
     *
     * @param includePeopleInTasksInStoriesInAllSprints inclusion boolean
     */
    public void setIncludePeopleInTasksInStoriesInAllSprints(boolean includePeopleInTasksInStoriesInAllSprints) {
        this.includePeopleInTasksInStoriesInAllSprints = includePeopleInTasksInStoriesInAllSprints;
    }

    /**
     *
     * @param includeEffortInTasksInStoriesInAllSprints inclusion boolean
     */
    public void setIncludeEffortInTasksInStoriesInAllSprints(boolean includeEffortInTasksInStoriesInAllSprints) {
        this.includeEffortInTasksInStoriesInAllSprints = includeEffortInTasksInStoriesInAllSprints;
    }

    /**
     *
     * @param includeAllTeams inclusion boolean
     */
    public void setIncludeAllTeams(boolean includeAllTeams) {
        this.includeAllTeams = includeAllTeams;
    }

    /**
     *
     * @param includePeopleInAllTeams inclusion boolean
     */
    public void setIncludePeopleInAllTeams(boolean includePeopleInAllTeams) {
        this.includePeopleInAllTeams = includePeopleInAllTeams;
    }

    /**
     *
     * @param includeSkillsInPeopleInAllTeams inclusion boolean
     */
    public void setIncludeSkillsInPeopleInAllTeams(boolean includeSkillsInPeopleInAllTeams) {
        this.includeSkillsInPeopleInAllTeams = includeSkillsInPeopleInAllTeams;
    }

    /**
     *
     * @param includeAllPeople inclusion boolean
     */
    public void setIncludeAllPeople(boolean includeAllPeople) {
        this.includeAllPeople = includeAllPeople;
    }

    /**
     *
     * @param includeSkillsInAllPeople inclusion boolean
     */
    public void setIncludeSkillsInAllPeople(boolean includeSkillsInAllPeople) {
        this.includeSkillsInAllPeople = includeSkillsInAllPeople;
    }

    /**
     *
     * @param includeAllSkills inclusion boolean
     */
    public void setIncludeAllSkills(boolean includeAllSkills) {
        this.includeAllSkills = includeAllSkills;
    }

    /**
     *
     * @param includeAllStories inclusion boolean
     */
    public void setIncludeAllStories(boolean includeAllStories) {
        this.includeAllStories = includeAllStories;
    }

    /**
     *
     * @param includeACsInAllStories inclusion boolean
     */
    public void setIncludeACsInAllStories(boolean includeACsInAllStories) {
        this.includeACsInAllStories = includeACsInAllStories;
    }

    /**
     *
     * @param includeDepsInAllStories inclusion boolean
     */
    public void setIncludeDepsInAllStories(boolean includeDepsInAllStories) {
        this.includeDepsInAllStories = includeDepsInAllStories;
    }

    /**
     *
     * @param includeTasksInAllStories inclusion boolean
     */
    public void setIncludeTasksInAllStories(boolean includeTasksInAllStories) {
        this.includeTasksInAllStories = includeTasksInAllStories;
    }

}
