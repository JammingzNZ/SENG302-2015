package seng302.group6;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import seng302.group6.Model.EstimationScale.DogBreeds;
import seng302.group6.Model.EstimationScale.EstimationScale;
import seng302.group6.Model.EstimationScale.PseudoFibonacci;
import seng302.group6.Model.EstimationScale.ShirtSizes;

/**
 * Created by josh on 4/07/15.
 */
public class EstimationScaleTest extends TestCase{

    EstimationScale fibScale = new PseudoFibonacci();
    EstimationScale shirtScale = new ShirtSizes();
    EstimationScale dogScale = new DogBreeds();

    public EstimationScaleTest(String testName)
    {
        super(testName);
    }

    public static Test suite() {return new TestSuite((EstimationScaleTest.class));}

    public void testZero() {
        assertEquals("0", fibScale.getRepresentation(0));
    }

    public void testInfinity() {
        assertEquals("Infinity", fibScale.getRepresentation(11));
    }

    public void testNull() {
        assertNull(fibScale.getRepresentation(null));
    }

    public void testInvalid() {
        try {
            fibScale.getRepresentation(123);
            assert(false);
        }
        catch (IllegalArgumentException e) {
            assert(true);
        }
        try {
            fibScale.getRepresentation(-50);
            assert(false);
        }
        catch (IllegalArgumentException e) {
            assert(true);
        }
    }

    public void testPseudoFibonacci() {
        assertEquals("1/2", fibScale.getRepresentation(1));
        assertEquals("1", fibScale.getRepresentation(2));
        assertEquals("2", fibScale.getRepresentation(3));
        assertEquals("3", fibScale.getRepresentation(4));
        assertEquals("5", fibScale.getRepresentation(5));
        assertEquals("8", fibScale.getRepresentation(6));
        assertEquals("13", fibScale.getRepresentation(7));
        assertEquals("20", fibScale.getRepresentation(8));
        assertEquals("40", fibScale.getRepresentation(9));
        assertEquals("100", fibScale.getRepresentation(10));
    }

    public void testShirtSizes() {
        assertEquals("XXXS", shirtScale.getRepresentation(1));
        assertEquals("XXS", shirtScale.getRepresentation(2));
        assertEquals("XS", shirtScale.getRepresentation(3));
        assertEquals("S", shirtScale.getRepresentation(4));
        assertEquals("M", shirtScale.getRepresentation(5));
        assertEquals("L", shirtScale.getRepresentation(6));
        assertEquals("XL", shirtScale.getRepresentation(7));
        assertEquals("XXL", shirtScale.getRepresentation(8));
        assertEquals("XXXL", shirtScale.getRepresentation(9));
        assertEquals("XXXXL", shirtScale.getRepresentation(10));
    }

    public void testDogBreeds() {
        assertEquals("Chihuahua", dogScale.getRepresentation(1));
        assertEquals("Shih Tzu", dogScale.getRepresentation(2));
        assertEquals("Pug", dogScale.getRepresentation(3));
        assertEquals("Bulldog", dogScale.getRepresentation(4));
        assertEquals("Beagle", dogScale.getRepresentation(5));
        assertEquals("Dalmation", dogScale.getRepresentation(6));
        assertEquals("Labrador", dogScale.getRepresentation(7));
        assertEquals("German Shepherd", dogScale.getRepresentation(8));
        assertEquals("Greyhound", dogScale.getRepresentation(9));
        assertEquals("Great Dane", dogScale.getRepresentation(10));
    }
}
