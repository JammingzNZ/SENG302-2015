package seng302.group6.Model.ItemClasses;

import com.google.gson.annotations.Expose;

import java.time.LocalDate;

/**
 * Basic class for representing projectUID allocations so dates are supported
 * Created by simon on 12/05/15.
 */
public class Allocation
{
    private String projectUID;
    @Expose private String teamUID;
    @Expose private String startDate;
    @Expose private String endDate;

    /**
     * Creates an Allocation with parameters specified
     * @param teamUID uid of teamUID working on projectUID
     * @param startDate start date of projectUID allocation
     * @param endDate end date of projectUID allocation
     * @param projectUID projectUID the team is allocated to
     */
    public Allocation(String teamUID, LocalDate startDate, LocalDate endDate, String projectUID)
    {
        setTeamUID(teamUID);
        setStartDate(startDate);
        setEndDate(endDate);
        setProjectUID(projectUID);
    }

    /**
     * Gets end date of projectUID allocation as LocalDate
     * @return end date of projectUID
     */
    public LocalDate getEndDate()
    {
        return LocalDate.parse(endDate);
    }

    /**
     * Sets end date of projectUID allocation as LocalDate
     * @param endDate end date of projectUID
     */
    public void setEndDate(LocalDate endDate)
    {
        this.endDate = endDate.toString();
    }

    /**
     * Gets the uid of teamUID allocated to projectUID
     * @return uid of teamUID
     */
    public String getTeamUID()
    {
        return teamUID;
    }

    /**
     * Sets the teamUID allocated to the projectUID
     * @param teamUID uid of the team to allocate
     */
    public void setTeamUID(String teamUID)
    {
        this.teamUID = teamUID;
    }

    /**
     * Gets start date of projectUID allocation as LocalDate
     * @return start date of projectUID
     */
    public LocalDate getStartDate()
    {
        return LocalDate.parse(startDate);
    }

    /**
     * Sets start date of projectUID allocation as LocalDate
     * @param startDate end date of projectUID
     */
    public void setStartDate(LocalDate startDate)
    {
        this.startDate = startDate.toString();
    }

    /**
     * Returns true if the end date of the allocation is before today's date
     * @return true if allocation has finished, false otherwise
     */
    public boolean isPast()
    {
        return LocalDate.parse(endDate).isBefore(LocalDate.now()) ? true : false;
    }

    /**
     * Gets uid of projectUID associated with allocation
     * @return uid of projectUID
     */
    public String getProjectUID() {
        return projectUID;
    }

    /**
     * Set the uid of the projectUID associated with allocation
     * @param projectUID uid of projectUID
     */
    public void setProjectUID(String projectUID) {
        this.projectUID = projectUID;
    }

    /**
     * Get the Team Name of the Team involved in the allocation
     * @return teamName
     */
    public String getTeamName()
    {
        return Workspace.getTeamName(teamUID);
    }
}
