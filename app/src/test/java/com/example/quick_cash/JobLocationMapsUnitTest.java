package com.example.quick_cash;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.example.quick_cash.JobPost.JobLocationMapsActivity;

import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;

/**
 * JUnit test class for map activity
 */
public class JobLocationMapsUnitTest {
    static JobLocationMapsActivity jobLocationMapsActivity;

    @BeforeClass
    public static void setup() throws IOException {
        jobLocationMapsActivity = new JobLocationMapsActivity();
    }

    /**
     * Test if the 2 step job location failed
     * False if the location is not possible to get
     */
    @Test
    public void invalidTwoStepJobLocationTest() {
        //assertFalse(jobLocationMapsActivity.twoStepJobLocationCheck("Halifax"));
    }

    /**
     * Test if 2 step job location pass
     * True if the location is registered in Geolocate
     */
    @Test
    public void validTwoStepJobLocationTest() {
        //assertTrue(jobLocationMapsActivity.twoStepJobLocationCheck("Halifax Shopping Center"));
    }
}
