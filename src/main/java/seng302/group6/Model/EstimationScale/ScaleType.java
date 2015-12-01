package seng302.group6.Model.EstimationScale;

/**
 * Created by Josh on 5/07/2015.
 */
public enum ScaleType {
    PSEUDOFIBONACCI(new PseudoFibonacci()),
    SHIRTSIZES(new ShirtSizes()),
    DOGBREEDS(new DogBreeds());

    private EstimationScale scale;

    ScaleType(EstimationScale scale) {
        this.scale = scale;
    }

    public EstimationScale getScale() {
        return scale;
    }

    public String toString() {
        return scale.toString();
    }
}
