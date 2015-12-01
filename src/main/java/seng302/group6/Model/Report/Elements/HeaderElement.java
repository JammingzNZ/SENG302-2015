package seng302.group6.Model.Report.Elements;

import org.jdom2.Element;
import seng302.group6.Model.ItemClasses.Workspace;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Creates a Header Element for the XML reports
 * Created by dan on 23/05/15.
 */
public class HeaderElement
{
    public static HeaderElement header;
    private Element parent = new Element("Workspace");

    /**
     * Constructor for the HeaderElement class
     */
    public HeaderElement() {header = this; }

    /**
     * Generates a header element for an XML report, contains:
     * Date of Report
     * Application Version
     * Most recent Workspace Save Location
     * @return element containing the header information
     */
    public Element getHeader() {
        parent.addContent(getDateOfReport());
        parent.addContent(getAppVersion());
        parent.addContent(getSaveLocation());
        return parent;
    }

    /**
     * Generates an element that contains the date of the report
     * TODO: generate today's date
     * @return the element containing the date details
     */
    private Element getDateOfReport()
    {
        Element element = new Element("Detail");
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String format = formatter.format(date);
        element.setAttribute("Date-of-Report", format);
        return element;
    }

    /**
     * Generates an element that contains the save location of the workspace
     * @return the element containing save location details
     */
    private Element getSaveLocation()
    {
        Element element = new Element("Detail");
        try {
            element.setAttribute("Workspace-Save-Location", Workspace.appdata.getProjectFile().getPath());
        }
        catch (NullPointerException e) {
            element.setAttribute("Workspace-Save-Location", "Undefined");
        }
        return element;
    }

    /**
     * Generates an element that contains the application name and version
     * @return the element containing the version details
     */
    private Element getAppVersion()
    {
        Element element = new Element("Detail");
        element.setAttribute("Application-Version", Workspace.appdata.getApplicationName() + " v" + Workspace.appdata.WORKSPACE_VERSION);
        return element;
    }


}
