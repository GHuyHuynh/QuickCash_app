package com.example.quick_cash;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.example.quick_cash.MyLocalArea.MyLocalAreaActivity;

import org.junit.BeforeClass;
import org.junit.Test;

public class MyLocalAreaUnitTest {
    static MyLocalAreaActivity myLocalAreaActivity;

    /**
     * Setting up for testing
     */
    @BeforeClass
    public static void setup() {
        myLocalAreaActivity = new MyLocalAreaActivity();
    }

    /**
     * Testing for if the input contains special characters
     *
     * False if it contains
     */
    @Test
    public void testLocationIsSpecialChar() {
        assertFalse(myLocalAreaActivity.noSpecialChar("!@#$%^&*"));
    }

    /**
     * Testing for if the input does not contains special characters
     *
     * True if it isn't
     */
    @Test
    public void testLocationNoSpecialChar() {
        assertTrue(myLocalAreaActivity.noSpecialChar("Halifax Shopping Center"));
    }

    /**
     * Testing for if the null input
     *
     * False if it is
     */
    @Test
    public void testNullLocation() {
        assertFalse(myLocalAreaActivity.noSpecialChar(""));
    }

    /**
     * Testing for if the location is not real
     *
     * False if it is
     */
    @Test
    public void testLocationNotExist() {
        assertFalse(myLocalAreaActivity.isValidLocation("New Y0rk C!ty"));
    }

    /**
     * Testing for if the location exists
     *
     * True if it is
     */
    @Test
    public void testLocationExist() {
        //assertTrue(myLocalAreaActivity.isValidLocation("Dalhousie University"));
    }

}
