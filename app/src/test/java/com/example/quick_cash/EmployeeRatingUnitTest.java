package com.example.quick_cash;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.example.quick_cash.Employee.Employee;
import com.example.quick_cash.Employee.EmployeeRatingActivity;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class EmployeeRatingUnitTest {

    static EmployeeRatingActivity employeeRating;
    public static final String FEEDBACK_TYPED = "test";

    @BeforeClass
    public static void setup(){
        employeeRating = new EmployeeRatingActivity();

    }

    /**
     * Test if it accepts empty feedback
     * True if feedback is empty
     */
    @Test
    public void emptyFeedback(){
        assertTrue(employeeRating.emptyFeedback(""));
    }

    /**
     * Tests if feedback is not empty
     * False if feedback is not empty
     */
    @Test
    public void nonEmptyFeedback(){
        assertFalse(employeeRating.emptyFeedback(FEEDBACK_TYPED));
    }

    /**
     * Tests for feedback no digits
     * True if feedback is valid
     */
    @Test
    public void isValidFeebackNoDigits(){
        assertTrue(employeeRating.isValidFeedback(FEEDBACK_TYPED));
    }

    /**
     * Tests if feedback has digits
     * True if feedback is valid
     */
    @Test
    public void isValidFeedbackDigits(){
        assertTrue(employeeRating.isValidFeedback(FEEDBACK_TYPED + "123"));
    }

    /**
     * Tests if feedback has symbols
     * True if feedback is valid
     */
    @Test
    public void isValidFeedbackSymbols(){
        assertTrue(employeeRating.isValidFeedback(FEEDBACK_TYPED + "!@"));
    }

}