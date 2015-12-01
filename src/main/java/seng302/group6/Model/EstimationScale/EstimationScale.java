package seng302.group6.Model.EstimationScale;

import com.google.gson.annotations.Expose;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by Josh on 4/07/2015.
 */
public abstract class EstimationScale {
    private final String ZERO = "0";
    private final String INFINITY = "Infinity";

    /**
     * Get the string representation of given value using the estimation scale.
     * @param value Integer between 0 and 11. 11 is Infinity
     * @return String representing the value
     */
    public String getRepresentation(Integer value) {
        if (value == null) {
            return null;
        }
        else if (value == 0) {
            return ZERO;
        }
        else if (value == 11) {
            return INFINITY;
        }
        else if (value >= 1 && value <= 10) {
            return getSpecialReps().get(value-1);
        }
        else {
            throw new IllegalArgumentException("Value must be between 0 and 11");
        }
    }

    /**
     * Get a list of all possible representations for this scale
     * @return ArrayList of string representations
     */
    public Collection<String> getRepresentations() {
        ArrayList<String> out = new ArrayList<>();
        out.add(ZERO);
        for (String addStr : getSpecialReps()) {
            out.add(addStr);
        }
        out.add(INFINITY);

        return out;
    }

    /**
     * Get a list of the 1-10 representations that are unique to a certain scale
     * @return ArrayList of strings representing each value
     */
    protected abstract ArrayList<String> getSpecialReps();
}
