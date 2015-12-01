package seng302.group6;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import seng302.group6.Model.Command.CommandManager;
import seng302.group6.Model.Command.Skill.ChangeSkillDescription;
import seng302.group6.Model.Command.Skill.CreateSkill;
import seng302.group6.Model.Command.Skill.DeleteSkill;
import seng302.group6.Model.ItemClasses.Skill;
import seng302.group6.Model.ItemClasses.Workspace;

import java.util.HashMap;

/**
 * Created by simon on 13/03/15.
 */
public class CommandSkillTest extends TestCase
{
    CommandManager manager;
    HashMap<String, Skill> skills;
    Skill s;

    public CommandSkillTest(String testName)
    {
        super(testName);
    }

    public static Test suite()
    {
        return new TestSuite( CommandSkillTest.class );
    }
    public void setUp() throws Exception
    {
        skills = new HashMap<>();
        s = new Skill("TS", "Test Skill");
        manager = new CommandManager();
        Workspace.reset();
    }

    public void tearDown() throws Exception
    {

    }

    public void testCreate() {
        Skill testSkill;

        testSkill = skills.get(s.uid());
        assertNull(testSkill);

        manager.addCommand(new CreateSkill(skills, s));

        testSkill = skills.get(s.uid());
        assertEquals("TS", testSkill.getShortName());
        assertEquals("Test Skill", testSkill.getDescription());

        manager.undo();

        testSkill = skills.get(s.uid());
        assertNull(testSkill);

        manager.redo();

        testSkill = skills.get(s.uid());
        assertEquals("TS", testSkill.getShortName());
        assertEquals("Test Skill", testSkill.getDescription());
    }

    public void testChangeSkillDescription(){
        Workspace.addSkill(s);
        Skill testSkill = Workspace.getSkill(s.uid());
        manager.addCommand(new ChangeSkillDescription(testSkill, "Best skill ever"));

        assertEquals("Best skill ever", testSkill.getDescription());

        manager.undo();

        assertEquals("Test Skill", testSkill.getDescription());

        manager.redo();

        assertEquals("Best skill ever", testSkill.getDescription());

    }

    /**
     * this test is failing because the workspace already has
     * skills in it right from the start - this isnt possible ?? got to work this one out!
     */
    public void testDeleteSkill()
    {
        // Removes all skills from Workspace that were added by previous tests.
        for(String id : Workspace.getSkills()){
            Workspace.removeSkill(id);
        }

        Workspace.createSkill(s);
        Skill testSkill = Workspace.getSkill(s.uid());

        assertTrue(Workspace.getSkills().contains(s.uid()));
        assertFalse(Workspace.getSkills().isEmpty());

        manager.addCommand(new DeleteSkill(testSkill));

        assertFalse(Workspace.getSkills().contains(testSkill.uid()));
        assertTrue(Workspace.getSkills().isEmpty());

        manager.undo();

        assertTrue(Workspace.getSkills().contains(testSkill.uid()));
        assertFalse(Workspace.getSkills().isEmpty());

        manager.redo();

        assertFalse(Workspace.getSkills().contains(testSkill.uid()));
        assertTrue(Workspace.getSkills().isEmpty());
    }
}
