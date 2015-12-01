package seng302.group6.Model.EstimationScale;

import java.util.ArrayList;

/**
 * Represents a scale estimation in terms of "Shirt sizes".
 * Created by Josh on 4/07/2015.
 */
public class ShirtSizes extends EstimationScale {

    @Override
    protected ArrayList<String> getSpecialReps() {
        ArrayList<String> specialReps = new ArrayList<>();
        specialReps.add("XXXS");
        specialReps.add("XXS");
        specialReps.add("XS");
        specialReps.add("S");
        specialReps.add("M");
        specialReps.add("L");
        specialReps.add("XL");
        specialReps.add("XXL");
        specialReps.add("XXXL");
        specialReps.add("XXXXL");
        return specialReps;
    }

    @Override
    public String toString() {
        return "Shirt Sizes";
    }
}
