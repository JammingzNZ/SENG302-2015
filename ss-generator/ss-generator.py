
#!/bin/env/python

import sys
import uuid
import random
import json
from datetime import datetime, date


DATE_FMT = "%Y-%m-%d"

class Item(dict):

    def __init__(self, *args, **kwargs):
        """
        Item is just a dictionary that gets a key "uid" added at construction time.
        """
        super(Item, self).__init__(*args, **kwargs)
        self["uid"] = str(uuid.uuid4())


class DataSet(object):
    def __init__(self, filename):
        """ 
        reads all lines from the specified file into the "data list" that is held on
        the object for later reference.
        """
        with open(filename, "r") as f:
            self.data = [item.strip() for item in f.readlines()]
            self._length = len(self.data)
        print "collected %s items from %s" % (self.length, filename)

    def get(self):
        """
        returns a randomly selected item from the "data list".
        """
        return self.data[random.randint(0, self.length-1)]


    @property
    def length(self):
        """
        returns the number of items in the "data list"
        """
        return self._length


class AllData(object):

    def __init__(self):
        """
        super object containing all the data sets.
        """
        self.adjectives = DataSet("adjectives.txt")
        self.aliases = DataSet("aliases.txt")
        self.quotes = DataSet("quotes.txt")
        self.skills = DataSet("skills.txt")
        self.verbs = DataSet("verbs.txt")
        self.firstnames = DataSet("firstnames.txt")
        self.surnames = DataSet("surnames.txt")
        self.teamnames = DataSet("teamnames.txt")
        self.nouns = DataSet("nouns.txt")
        self.sprintnames = DataSet("sprintnames.txt")
        self.codenames = DataSet("codenames.txt")
        self.releasenames = DataSet("releasenames.txt")
    


poskill = Item(shortName="Product Owner", description="owns products")
smskill = Item(shortName="Scrum Master", description="master of scrums")

def make_skills(alldata):
    """
    creates a dictionary of skills keyed by uid, as a skill in the ss file would be.

    skills are populated with:

    - a random skill name (shortName)
    - a unique, silly description
    """
    items = {poskill["uid"]: poskill, smskill["uid"]:smskill}
    shortnames = []
    for shortName in alldata.skills.data:
        if shortName in shortnames:
            continue
        shortnames.append(shortName)
        description = "The skill '%s' describes people who are able to %s %sly" % \
                         (shortName, alldata.verbs.get(), alldata.adjectives.get())
        item = Item(description=description, shortName=shortName)
        items[item["uid"]] = item
    return items


def make_people(alldata, skills, maxskills):
    """
    creates a dictionary of people keyed by uid, as a person in the ss file would be.

    people are populated with:
    
    - a random alias (shortName)
    - a first name 
    - a last name
    - a list of skills (uids) up to maxskills in length
    - the inTeam value is set to False
    """
    items = dict()
    shortnames = []
    for shortName in alldata.aliases.data:
        if shortName in shortnames:
            continue
        shortnames.append(shortName)
        firstName = alldata.firstnames.get()
        lastName = alldata.surnames.get()
        userID = "%s%s%s" % (firstName[0].upper(), lastName[:2].upper(), random.randint(100,999))
        skilluids = [random.choice(skills.keys()) for i in range(random.randint(0, maxskills))]
        inTeam = False
        item = Item(lastName=lastName, firstName=firstName, userID=userID, skills=list(set(skilluids)), inTeam=inTeam, shortName=shortName)
        items[item["uid"]] = item
    return items


def make_teams(alldata, people, maxpeople, skills):
    """
    creates a dictionary of teams keyed by uid, as a team in the ss file would be.

    teams are populated with:

    - a random name (shortName)
    - a unique, silly description
    - a list of people (uids) up to maxpeople in length.
        people are added only if they are not already in a team.
    - a list of developers (uids) up to maxpeople in length
        developers are a subset of the people list, who do not hold the skill of scrum master or product owner
    - a single person as scrum master
    - a single person as product owner
    """
    items = dict()
    shortnames = []
    for shortName in alldata.teamnames.data:
        if shortName in shortnames:
            continue
        shortnames.append(shortName)
        description = "%s is the %s team. %sing is what this team does best." % \
                            (shortName, alldata.adjectives.get(), alldata.verbs.get())

        peopleuids = []
        for i in range(random.randint(0, maxpeople)):
            personuid = random.choice(people.keys())
            if personuid not in peopleuids and not people[personuid]["inTeam"]:
                peopleuids.append(personuid)
                people[personuid]["inTeam"] = True
         
        developeruids = set()
        teamSm = None
        teamPo = None
        for i in range(random.randint(0, len(peopleuids))):
            personuid = random.choice(peopleuids)
            if smskill["uid"] in people[personuid]["skills"]:
                teamSm = personuid
            if poskill["uid"] in people[personuid]["skills"]:
                teamPo = personuid
            if smskill["uid"] not in people[personuid]["skills"] and \
                poskill["uid"] not in people[personuid]["skills"]:
                developeruids.add(personuid)
        item = Item(shortName=shortName, description=description, people=peopleuids, developers=list(developeruids))
        if teamSm is not None:
            item["teamSm"] = teamSm
        if teamPo is not None:
            item["teamPo"] = teamPo
        items[item["uid"]] = item
    return items


TASK_PREFIX = [
    "Begin", 
    "Start", 
    "Do some", 
    "Panic about doing"
]

TASK_STATUSES = [
    "READY", 
    "IN_PROGRESS", 
    "PENDING", 
    "BLOCKED", 
    "DONE", 
    "DEFERRED", 
    "NOT_STARTED"
]


def make_tasks(alldata, maxtasks, people, maxpeople):
    """
    creates a lists of tasks, these would be added to a story. 
    tasks are not Items but dictionaries.

    tasks are populated with:

    - a random name (shortName)
    - a unique, silly description
    - The task status, defaults to "Not Started".
    - the estimation value set randomly between 0.1 and 20.0
    - the effort value set to 0
    - a list of people (uids) up to maxpeople in length
    """
    items = []
    shortnames = []
    ntasks = random.randint(0, maxtasks)
    for i in range(ntasks):
        shortName = "%s %s %sing" % (random.choice(TASK_PREFIX), alldata.adjectives.get(), alldata.verbs.get())
        if shortName in shortnames:
            continue
        shortnames.append(shortName)
        description = "the task at hand, is to do some %s" % (shortName)

        peopleuids = []
        for i in range(random.randint(0, maxpeople)):
            personuid = random.choice(people.keys())
            if personuid not in peopleuids:
                peopleuids.append(personuid)

        status = random.choice(TASK_STATUSES)
        effort = 0.0
        if status != "Not Started":
            effort = random.uniform(0.1, 20.0)
        estimation = random.uniform(0.1, 20.0)

        item = dict(shortName=shortName, description=description, people=peopleuids, 
                    status=status, estimation=estimation, effort=effort)
        items.append(item)  
    return items


def make_acceptance_criteria(alldata, maxacs):
    """
    creates a lists of acceptance criteria, these would be added to a story. 
    acceptance criteria are not Items but dictionaries.

    acceptance criteria are populated with:

    - a piece of text (text)
    """
    items = []
    nacs = random.randint(0, maxacs)
    for i in range(nacs):
        text = "acceptance criteria %s: the project must be demonstarbly able to %s %sly" % (i, alldata.verbs.get(), alldata.adjectives.get())
        item = dict(text=text)
        items.append(item)
    return items


def make_team_allocation(alldata, teams, maxteams):
    """
    creates a lists of team allocations, these would be added to a project. 
    team allocations are not Items but dictionaries.

    team allocations are populated with:

    - the uid of a team (teamUID)
    - the start date of the allocation (startDate)
    - the end date of the allocation (endDate)
    - the uid of the associated project (project) set to None initially
    """
    items = []
    nteams = random.randint(0, maxteams)
    newend = None
    for i in range(nteams):
        teamUID = random.choice(teams.keys())

        if newend is None:
            newend = date.today()

        day = newend.day-1
        month = newend.month
        year = newend.year
        if day < 1:
            month -= 1
            day = 28 + day
            if month < 1:
                month = 12
                year -= 1
        endDate = newend.replace(year=year, month=month, day=day)

        day = endDate.day-random.randint(7, 28)
        month = endDate.month
        year = endDate.year
        if day < 1:
            month -= 1
            day = 28 + day
            if month < 1:
                month = 12
                year -= 1
        startDate = endDate.replace(year=year, month=month, day=day) 

        newend = startDate

        item = dict(teamUID=teamUID, startDate=datetime.strftime(startDate, DATE_FMT),
                    endDate=datetime.strftime(endDate, DATE_FMT), project=None)
        items.append(item)
    return items


STORY_SCALES = {
    "PSEUDOFIBONACCI": [  
        "0",
        "1/2",
        "1",
        "2",
        "3",
        "5",
        "8",
        "13",
        "20",
        "40",
        "100",
        "Infinity"
    ], 
    "DOGBREEDS": [
        "0",
        "Chihuahua",
        "Shih Tzu",
        "Pug",
        "Bulldog",
        "Beagle",
        "Dalmation",
        "Labrador",
        "German Shepherd",
        "Greyhound",
        "Great Dane",
        "Infinity"
    ],
    "SHIRTSIZES": [
        "0",
        "XXXS",
        "XXS",
        "XS",
        "S",
        "M",
        "L",
        "XL",
        "XXL",
        "XXXL",
        "XXXXL",
        "Infinity"
    ]
}

def make_stories(alldata, maxstories, people, maxacs, maxtasks, maxtaskpeople, backlogs):
    """
    creates a dictionary of stories keyed by uid, as a story in the ss file would be.

    stories are populated with:

    - a random name (shortName)
    - a unique, silly description
    - a longer random name (longName)
    - the name of the createor (a person name)
    - the uid of the story creator (a person uid)
    - a list of acceptance criteria
    - a list of tasks
    - the uid  of the associated backlog
    - the scale type
    - the estimate, story scale integer
    - story priority
    - ready status, boolean
    - list of dependent story uids (left empty)
    - list of dependency story uids (left empty)
    - story state
    - inSprint, boolean (False)
    """
    items = dict()
    shortnames = []
    nstories = random.randint(0, maxstories)
    for i in range(nstories):
        shortName = "the %s %sing story" % (alldata.adjectives.get(), alldata.verbs.get())
        if shortName in shortnames:
            continue
        shortnames.append(shortName)
        description = "whats the story? the story is that as a %s I need to be able to %s, %sly" % \
                            (alldata.nouns.get(), alldata.verbs.get(), alldata.adjectives.get())

        longName = "a longer version of short name %s" % (shortName)

        creatorUID = random.choice(people.keys())
        creatorName = people[creatorUID]["shortName"]

        tasks = make_tasks(alldata, maxtasks, people, maxtaskpeople)
        acceptanceCriterias = make_acceptance_criteria(alldata, maxacs)

        backlog = random.choice(backlogs.keys() + [None])
        scaleType = random.choice(STORY_SCALES.keys())
        estimateRep = "0"
        estimate = 0
        if backlog is not None:
            scaleType = backlogs[backlog]["scaleType"]
            estimateRep = random.choice(STORY_SCALES[scaleType])
            estimate = STORY_SCALES[scaleType].index(estimateRep)
        currentPriority = random.randint(1, 100)
        ready = backlog is not None and estimate is not None and \
                    len(acceptanceCriterias) > 0
                    # and this on the end to skew the number of stories that are ready
                    # and random.choice([True]*7 + [False]*3)

        state= random.choice(TASK_STATUSES)
        inSprint = False

        dependencies = []
        dependents = []

        item = Item(shortName=shortName, description=description, longName=longName, 
                    tasks=tasks, acceptanceCriterias=acceptanceCriterias, backlog=backlog,
                    scaleType=scaleType, estimate=estimate, estimateRep=estimateRep, currentPriority=currentPriority,
                    ready=ready, state=state, inSprint=inSprint, creatorUID=creatorUID, creatorName=creatorName,
                    dependencies=dependencies, dependents=dependents)
        items[item["uid"]] = item

        if backlog is not None:
            backlogs[backlog]["stories"].append(item["uid"])
    return items


def make_backlogs(alldata, maxbacklogs, people):
    """
    creates a dictionary of backlogs keyed by uid, as a backlog in the ss file would be.

    backlogs are populated with:

    - a random name (shortName)
    - a unique, silly description (description)
    - a longer random name (fullName)
    - the uid of the product owner (productOwner)
    - a list of uids of the stories in the backlog (stories) empty to begin with
    - the scale type of the baklog (scaleType)
    - a list of uids of projects assiated with the backlog (projects) empty to begin with
    """
    items = dict()
    stories_ = []
    shortnames = []
    nbacklogs = random.randint(0, maxbacklogs)
    for i in range(nbacklogs):
        shortName =  "the %s backlog" % (alldata.adjectives.get())
        if shortName in shortnames:
            continue
        shortnames.append(shortName)
        fullName = "how dost one nameth a backlog? i wilt sayeth i am tempt'd to nameth this backlog 'backlog N', but that doesnt hast a v'ry good ringeth to it. suggestions?"
        description = "and to describeth %s? thither simply art nay w'rds" % shortName

        projects = []
        stories = []
        pos = [person for person in people if poskill["uid"] in people[person]["skills"]] + [""]
        productOwner = random.choice(pos)
        scaleType = random.choice(STORY_SCALES.keys())

        item = Item(shortName=shortName, description=description, stories=stories, projects=projects,
                    fullName=fullName, productOwner=productOwner, scaleType=scaleType)
        items[item["uid"]] = item
    return items

def make_releases(alldata, maxreleases, projects):
    """
    creates a dictionary of releases keyed by uid, as a release in the ss file would be.

    releases are populated with:

    - a random name (shortName)
    - a unique, silly description (description)
    - the release date (releaseDate)
    - the uid of the associated project (associatedProject)
    """
    items = dict()
    shortnames = []
    nreleases = random.randint(0, maxreleases)
    newend = None
    for i in range(nreleases):
        shortName = "%s" % alldata.releasenames.get()
        if shortName in shortnames:
            continue
        shortnames.append(shortName)
        description = "this release may be a blessed %s, pray that the acceptance tests pass" % (shortName)

        today = date.today()
        releaseDate=datetime.strftime(today.replace(month=today.month + random.randint(-3, 3)), DATE_FMT)
        associatedProject=random.choice(projects.keys())

        item = Item(shortName=shortName, releaseDate=releaseDate, description=description,
                    associatedProject=associatedProject)
        items[item["uid"]] = item

        projects[associatedProject]["release"] = item["uid"]
    return items

def make_projects(alldata, maxprojects, backlogs, teams, maxteams):
    """
    creates a dictionary of projects keyed by uid, as a project in the ss file would be.

    projects are populated with:

    - a random name (shortName)
    - a unique, silly description (description)
    - a longer random name (longName)
    - the uid of an associated backlog (backlog)
    - a list of uids of associated releases (releases) empty to start with
    - a list of team allocations (devTeams)
    """
    items = dict()
    shortnames = []
    usedstories_ = []
    nprojects = random.randint(0, maxprojects)
    newend = None
    for i in range(nprojects):
        shortName = "%s" % alldata.codenames.get()
        longName =  "Classified Project No. %s codename '%s'" % (i, shortName)
        if shortName in shortnames:
            continue
        shortnames.append(shortName)
        description = "Project '%s' is top secret, if i tell you about it ill have to kill you" % (shortName)
        backlog = random.choice(backlogs.keys())
        releases = []
        devTeams = make_team_allocation(alldata, teams, maxteams)

        item = Item(shortName=shortName, longName=longName, description=description, 
                    backlog=backlog, releases=releases, devTeams=devTeams)
        items[item["uid"]] = item
        backlogs[backlog]["projects"].append(item["uid"])

        for team in devTeams:
            team["project"] = item["uid"]

    return items

def make_sprints(alldata, maxsprints, stories, maxstories, backlogs, releases, projects):
    """
    creates a dictionary of sprints keyed by uid, as a sprint in the ss file would be.

    stories are populated with:

    - a random name (shortName)
    - a unique, silly description (description)
    - a longer random name (fullName)
    - the uid of the associated backlog (associatedBacklog)
    - the uid of the associated team (associatedTeam)
    - the uid of the associated project (associatedProject)
    - the uid of the associated release (associatedRelease)
    - a list of uids of the stories in the sprint (stories)
    - the story start date (startDate)
    - the story end date (endDate)

    """
    items = dict()
    shortnames = []
    usedstories_ = []
    nsprints = random.randint(0, maxsprints)
    newend = None
    for i in range(nsprints):
        fullName = alldata.sprintnames.get()
        shortName =  "%s" % (fullName)
        if shortName in shortnames:
            continue
        shortnames.append(shortName)
        description = "team members will sprint this day pursuing '%s'" % (fullName)

        associatedTeam = random.choice(teams.keys())        
        associatedRelease = random.choice(releases.keys())
        associatedProject = releases[associatedRelease]["associatedProject"]
        associatedBacklog = projects[associatedProject]["backlog"]

        stories_ = []
        # add stories from backlog, if ready
        for i in range(random.randint(0, len(backlogs[associatedBacklog]["stories"]))):
            story = random.choice(backlogs[associatedBacklog]["stories"])
            if story not in usedstories_ and stories[story]["ready"]:
                usedstories_.append(story)
                stories_.append(story)

        if newend is None:
            newend = datetime.strptime(releases[associatedRelease]["releaseDate"], DATE_FMT)

        day = newend.day-1
        month = newend.month
        year = newend.year
        if day < 1:
            month -= 1
            day = 28 + day
            if month < 1:
                month = 12
                year -= 1
        endDate = newend.replace(year=year, month=month, day=day)

        day = endDate.day-random.randint(7, 28)
        month = endDate.month
        year = endDate.year
        if day < 1:
            month -= 1
            day = 28 + day
            if month < 1:
                month = 12
                year -= 1
        startDate = endDate.replace(year=year, month=month, day=day) 

        newend = startDate

        item = Item(shortName=shortName, description=description, fullName=fullName,
                    stories=stories_, associatedTeam=associatedTeam, associatedRelease=associatedRelease,
                    associatedProject=associatedProject, associatedBacklog=associatedBacklog,
                    startDate=datetime.strftime(startDate, DATE_FMT), endDate=datetime.strftime(endDate, DATE_FMT))
        items[item["uid"]] = item
    return items




if __name__ == "__main__":

    if len(sys.argv) < 2:
        print "usage: ./ss-generator.py <output filename>"
        sys.exit(0)
        
    filename = sys.argv[1]

    alldata = AllData()

    skills = make_skills(alldata)
    people = make_people(alldata, skills, 50)
    teams = make_teams(alldata, people, 25, skills)
    backlogs = make_backlogs(alldata, 50, people)
    projects = make_projects(alldata, 50, backlogs, teams, 5)
    releases = make_releases(alldata, 50, projects)
    stories = make_stories(alldata, 500, people, 20, 20, 20, backlogs)
    sprints = make_sprints(alldata, 100, stories, 50, backlogs, releases, projects)

    output = dict(people=people,
                  skills=skills,
                  teams=teams,
                  stories=stories, 
                  sprints=sprints,
                  backlogs=backlogs,
                  projects=projects,
                  releases=releases)

    with open(filename, "w") as f:
        f.write(json.dumps(output, sort_keys=True, indent=4, separators=(',', ': ')))