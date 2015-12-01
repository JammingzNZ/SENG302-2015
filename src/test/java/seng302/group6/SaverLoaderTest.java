package seng302.group6;

import com.google.gson.annotations.Expose;
import com.google.gson.reflect.TypeToken;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.junit.Ignore;

import java.io.File;
import java.io.IOException;


public class SaverLoaderTest extends TestCase
{
    private String TEST_FILENAME = "test.json";

    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public SaverLoaderTest(String testName)
    {
        super(testName);
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite(SaverLoaderTest.class);
    }

    /**
     * Tests save function creates new file
     */
    public void testSaverLoader_save_new()
    {
        TestObj obj = new TestObj(); 
        File file = new File(TEST_FILENAME); 
        file.delete();
        assertFalse(file.exists());
        try {
            SaverLoader.save(file, obj);
        }
        catch(IOException e) {
            Debug.println("Error saving file: " + e.getMessage());
        }
        assertTrue(file.exists());
        file.delete();
    }

    /**
     * Tests save function overwrites existing file, 
     * and load overwrites exposed object attributes.
     */
    public void testSaverLoader_save_load()
    {        
        TestObj obj = new TestObj();
        File file = new File(TEST_FILENAME);
        try {
            SaverLoader.save(file, obj);
        }
        catch(IOException e) {
            Debug.println("Error saving file: " + e.getMessage());
        }
        assertTrue(obj.publics_unchanged());
        assertTrue(obj.privates_unchanged());
        obj.var1 = 23;
        obj.var2 = "changed";
        try {
            SaverLoader.save(file, obj);
        }
        catch(IOException e) {
            Debug.println("Error saving file: " + e.getMessage());
        }
        TestObj obj2 = (TestObj)SaverLoader.load(file, new TypeToken<TestObj>(){}.getType());
        assertEquals(obj2.var1, 23);
        assertEquals(obj2.var2, "changed");
        assertFalse(obj2.publics_unchanged());
        assertTrue(obj2.privates_unchanged());
        file.delete();
    }
}

/**
 * this class is a simple test fixture for the saverloader test cases.
 */
@Ignore
class TestObj
{
    @Expose public int var1 = 9;
    private int hidden_var1 = 10;
    @Expose public String var2 = "hello";
    private String hidden_var2 = "gidday";

    /**
     * @return true if the private members retained original values (as expected)
     */
    boolean privates_unchanged()
    {
        return hidden_var1 == 10 && hidden_var2 == "gidday";
    }

    /**
     * @return true if the public members retained original values.
     */
    boolean publics_unchanged()
    {
        return var1 == 9 && var2 == "hello";
    }
}