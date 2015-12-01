package seng302.group6.Model.EstimationScale;

import java.util.ArrayList;

/**
 * Represents a scale estimation in terms of the Pseudo Fibonacci
 * Created by Josh on 4/07/2015.
 */
public class PseudoFibonacci extends EstimationScale {

    @Override
    protected ArrayList<String> getSpecialReps() {
        ArrayList<String> specialReps = new ArrayList<>();
        specialReps.add("1/2");
        specialReps.add("1");
        specialReps.add("2");
        specialReps.add("3");
        specialReps.add("5");
        specialReps.add("8");
        specialReps.add("13");
        specialReps.add("20");
        specialReps.add("40");
        specialReps.add("100");
        return specialReps;
    }

    @Override
    public String toString() {
        return "Pseudo-Fibonacci";
    }
}
