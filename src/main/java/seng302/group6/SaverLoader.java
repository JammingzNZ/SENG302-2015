package seng302.group6;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;

import java.io.*;
import java.lang.reflect.Type;

/**
 * Saves and loads Objects to/from disk in JSON format.
 *
 * To mark an attribute on the object as saveable:
 * <pre>
 * {@code
 * import com.google.gson.annotations.Expose;
 * class MyObj 
 * { @Expose public int myvar = 0; }
 * }
 * </pre>
 *
 * To load and save data from your objects:
 * <pre>
 * {@code
 * MyObj obj = new MyObj();
 * SaverLoader.save(new File("myfile.json"), obj);
 * ...
 * MyObj obj2 = (MyObj)SaverLoader.load(new File("myfile.json"), MyObj.class);
 * }
 * </pre>
 *
 */
public class SaverLoader {

    private static Gson gson = new Gson();
    private static Gson gsonb = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().setPrettyPrinting().create();

    /**
     * Saves the exposed elements of an object instance to the specified file.
     *          
     * The Object is saved in JSON format. 
     * 
     * @param  file is a file object to write to.
     * @param  obj is the object to save.
     * @throws java.io.IOException an Exception
     */
    public static void save(File file, Object obj) throws IOException
    {
        FileOutputStream outputStream = new FileOutputStream(file);
        outputStream.write(gsonb.toJson(obj).getBytes());
        outputStream.close();
    }

    /**
     * Loads the exposed elements of an object type from the specified file.
     * 
     * @param  file is a file object to read from.
     * @param  t is the object type expected in the file. Eg MyType.
     * @return  returns an instance of the object type specified in classtype, or
     *          null if an error occurs.
     */
    public static Object load(File file, Type t)
    {
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            return gson.fromJson(br, t);
        } catch(FileNotFoundException e) {
            Debug.println("file " + file.getName() + " not found");
        } catch(JsonSyntaxException e) {
            Debug.println("file " + file.getName() + " contains a json syntax error");
        }
        return null;
    }
}