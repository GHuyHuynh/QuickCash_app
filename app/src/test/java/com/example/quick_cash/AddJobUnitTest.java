package com.example.quick_cash;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.example.quick_cash.JobPost.AddJobActivity;
import com.example.quick_cash.Model.ModelJobPosition;

import org.junit.BeforeClass;
import org.junit.Test;

/**
 * JUnit test class for AddJob activity
 */
public class AddJobUnitTest {
    static AddJobActivity addJobActivity;
    static ModelJobPosition firstJobPosition;

    /**
     * Set up class with arbitrage Model_Job_Position object
     */
    @BeforeClass
    public static void setup() {
        addJobActivity = new AddJobActivity();
        firstJobPosition = new ModelJobPosition("Job Title", "Job Description", "2022/10/22", 30,
                "High", 60, "Halifax");
        firstJobPosition.setJobEmployer("Huy Huynh");
    }

    /**
     * Test if date is valid
     * True if date is valid
     */
    @Test
    public void jobDateIsValid() {
        assertTrue(addJobActivity.isValidJobDate("2023/12/24"));
    }

    /**
     * Test if the date insert is to long
     * False if date is too long
     */
    @Test
    public void jodDateTooLong() {
        assertFalse(addJobActivity.isValidJobDate("2023/122/567"));
    }

    /**
     * Test if job date is expired
     * False if date is expired
     */
    @Test
    public void jobDateIsExpired() {
        assertFalse(addJobActivity.isValidJobDate("2003/10/14"));
    }

}