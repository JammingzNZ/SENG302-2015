package seng302.group6.Model.EstimationScale;

import java.util.ArrayList;

/**
 * Represents a scale estimation in terms of "Dogs".
 * Created by Josh on 4/07/2015.
 */
public class DogBreeds extends EstimationScale {

    //Size order derived from http://www.dogsindepth.com/dog_breed_size_chart.html

    @Override
    protected ArrayList<String> getSpecialReps() {
        ArrayList<String> specialReps = new ArrayList<>();
        specialReps.add("Chihuahua");
        specialReps.add("Shih Tzu");
        specialReps.add("Pug");
        specialReps.add("Bulldog");
        specialReps.add("Beagle");
        specialReps.add("Dalmation");
        specialReps.add("Labrador");
        specialReps.add("German Shepherd");
        specialReps.add("Greyhound");
        specialReps.add("Great Dane");
        return specialReps;
    }

    @Override
    public String toString() {
        return "Dog Breeds";
    }
}
