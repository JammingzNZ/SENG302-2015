package seng302.group6.Model.Report;

/**
 * Preset options for the Statur report
 * Created by simon on 11/07/15.
 */
public enum ReportPreset
{
    BLANK(""),
    PROJECTS("Projects"),
    TEAMS("Teams"),
    PEOPLE("People"),
    SKILLS("Skills"),
    BACKLOGS("Backlogs"),
    STORIES("Stories"),
    SPRINTS("Sprints"),
    STATUS("Status Report");


    private String rep;

    /**
     * Report preset string
     * @param rep the report preset string
     */
    ReportPreset(String rep) {
        this.rep = rep;
    }

    /**
     * Tostring method for report preset
     * @return the report preset string
     */
    public String toString() {
        return rep;
    }
}
