package seng302.group6;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import seng302.group6.Model.Command.Command;
import seng302.group6.Model.Command.CommandManager;
import seng302.group6.Model.Command.CommandMessage;
import seng302.group6.Model.ItemClasses.Skill;
import seng302.group6.Model.ItemType;

/**
 * Created by josh on 21/05/15.
 */
public class CommandManagerTest extends TestCase
{
    CommandManager manager = new CommandManager();
    Value valueA = new Value();
    Value valueB = new Value();

    private class Value {
        private int intVal;

        Value() {
            intVal = 0;
        }

        public void add() {
            intVal++;
        }

        public void sub() {
            intVal--;
        }

        public int getVal() {
            return intVal;
        }
    }

    private class Increment implements Command {
        Value val;

        public Increment(Value val) {
            this.val = val;
        }

        public void undo() {
            val.sub();
        }

        public void redo() {
            val.add();
        }

        public CommandMessage getMessage() {
            return new CommandMessage("", ItemType.PROJECT, new Skill("", ""));
        }
    }

    public CommandManagerTest(String testName)
    {
        super(testName);
    }

    public static Test suite()
    {
        return new TestSuite( CommandManagerTest.class );
    }

    public void testRevertBack() {
        manager.addCommand(new Increment(valueA));
        manager.addCommand(new Increment(valueA));
        manager.addCommand(new Increment(valueA));

        assertEquals(3, valueA.getVal());

        manager.revert();

        assertEquals(0, valueA.getVal());
    }

    public void testRevertForward() {
        manager.addCommand(new Increment(valueA));
        manager.addCommand(new Increment(valueA));
        manager.addCommand(new Increment(valueA));
        manager.setSavedIndex();

        assertEquals(3, valueA.getVal());

        manager.undo();
        manager.undo();
        manager.undo();

        assertEquals(0, valueA.getVal());

        manager.revert();

        assertEquals(3, valueA.getVal());
    }

    public void testRevertBackBranch() {
        manager.addCommand(new Increment(valueA));
        manager.addCommand(new Increment(valueA));
        manager.addCommand(new Increment(valueA));
        manager.setSavedIndex();

        assertEquals(3, valueA.getVal());

        manager.undo();
        manager.undo();
        manager.undo();

        assertEquals(0, valueA.getVal());

        manager.addCommand(new Increment(valueB));
        manager.addCommand(new Increment(valueB));
        manager.addCommand(new Increment(valueB));

        assertEquals(3, valueB.getVal());

        manager.revert();

        assertEquals(3, valueA.getVal());
        assertEquals(0, valueB.getVal());
    }

    public void testRevertForwardBranch() {
        manager.addCommand(new Increment(valueA));
        manager.addCommand(new Increment(valueA));
        manager.addCommand(new Increment(valueA));
        manager.setSavedIndex();

        assertEquals(3, valueA.getVal());

        manager.undo();

        assertEquals(2, valueA.getVal());

        manager.addCommand(new Increment(valueB));

        assertEquals(1, valueB.getVal());

        manager.undo();
        manager.undo();
        manager.undo();

        assertEquals(0, valueA.getVal());
        assertEquals(0, valueB.getVal());

        manager.revert();

        assertEquals(3, valueA.getVal());
        assertEquals(0, valueB.getVal());
    }

    public void testRevertRigorous() {
        manager.addCommand(new Increment(valueA));
        manager.addCommand(new Increment(valueA));
        manager.addCommand(new Increment(valueA));
        manager.setSavedIndex();

        assertEquals(3, valueA.getVal());

        manager.undo();

        assertEquals(2, valueA.getVal());

        manager.addCommand(new Increment(valueB));

        assertEquals(1, valueB.getVal());

        manager.undo();
        manager.undo();
        manager.addCommand(new Increment(valueB));

        manager.revert();

        assertEquals(3, valueA.getVal());
    }

    public void testAgain() {
        manager.addCommand(new Increment(valueA));//A=1
        manager.addCommand(new Increment(valueB));//B=1

        manager.setSavedIndex();

        manager.undo();//B=0

        manager.addCommand(new Increment(valueB));//B=1
        manager.addCommand(new Increment(valueB));//B=2

        manager.setSavedIndex();

        manager.undo();//B=1

        manager.addCommand(new Increment(valueA));//A=2

        manager.revert();//B=2, A=1
        manager.revert();

        assertEquals(1, valueA.getVal());
        assertEquals(2, valueB.getVal());
    }
}
