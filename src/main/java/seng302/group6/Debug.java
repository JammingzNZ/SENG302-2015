package seng302.group6;

import java.util.Map;

/**
 * Provides println and print functions that can be called from anywhere, and
 * are only called when the DEBUG environment variable is set.
 *
 * To enable debugging:
 *   Click your main configuration in top right
 *   Edit configurations...
 *   Go into environment variables
 *   Add variable with name=DEBUG and value=TRUE
 *   Apply changes
 *
 * Created by simon on 27/04/15.
 */
public class Debug
{
    private static boolean debugging = false;

    /**
     * Sets the debugging variable depending on if DEBUG is set
     */
    public static void setup()
    {
        Map<String, String> env = System.getenv();
        String debug = env.get("DEBUG");
        if (debug != null) {
            if (debug.equals("TRUE")) {
                debugging = true;
            }
        }
    }

    /**
     * Prints a line to stdout if we are debugging
     * @param s String to print
     */
    public static void println(String s)
    {
        if (debugging) {
            System.out.println(s);
        }
    }

    /**
     * Prints to stdout if we are debugging
     * @param s String to print
     */
    public static void print(String s)
    {
        if (debugging) {
            System.out.print(s);
        }
    }

    /**
     * Prints a line to stderr if we are debugging
     * @param s String to print
     */
    public static void errPrintln(String s)
    {
        if (debugging) {
            System.err.println(s);
        }
    }

    /**
     * Prints to stderr if we are debugging
     * @param s String to print
     */
    public static void errPrint(String s)
    {
        if (debugging) {
            System.err.print(s);
        }
    }

    /**
     * Runs a block of code if we are debugging
     * @param code block of code to run
     */
    public static void run(Runnable code)
    {
        if (debugging) {
            code.run();
        }
    }
}
