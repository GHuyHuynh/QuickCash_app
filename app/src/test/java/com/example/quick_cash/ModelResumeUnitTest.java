package com.example.quick_cash;

import static org.junit.Assert.*;

import com.example.quick_cash.Model.ModelResume;

import org.junit.Test;

public class ModelResumeUnitTest {

    /**
     * Create mock ModelResume with status and check if it is correctly stored.
     */
    @Test
    public void testModelResumeConstructorWithStatus() {
        ModelResume resume = new ModelResume(
                "TestEmployer",
                "TestEmployee",
                "Software Engineer",
                "Pass",
                "12345",
                "Accepted",
                "80000"
        );

        assertEquals("TestEmployer", resume.getEmployer());
        assertEquals("TestEmployee", resume.getEmployee());
        assertEquals("Software Engineer", resume.getJobName());
        assertEquals("Pass", resume.getFlag());
        assertEquals("12345", resume.getJobsKey());
        assertEquals("Accepted", resume.getStatus());
        assertEquals("80000", resume.getSalary());
    }

    /**
     * Create mock ModelJobPosition without status and check if it is correctly stored.
     */
    @Test
    public void testModelResumeConstructorWithoutStatus() {
        ModelResume resume = new ModelResume(
                "AnotherEmployer",
                "AnotherEmployee",
                "Data Scientist",
                "Refuse",
                "67890",
                "70000"
        );

        assertEquals("AnotherEmployer", resume.getEmployer());
        assertEquals("AnotherEmployee", resume.getEmployee());
        assertEquals("Data Scientist", resume.getJobName());
        assertEquals("Refuse", resume.getFlag());
        assertEquals("67890", resume.getJobsKey());
        assertEquals("70000", resume.getSalary());
    }

    /**
     * Testing setters and getters in ModelResume
     */
    @Test
    public void testModelResumeSettersAndGetters() {
        ModelResume resume = new ModelResume();

        resume.setEmployer("NewEmployer");
        resume.setEmployee("NewEmployee");
        resume.setJobName("Product Manager");
        resume.setFlag("Pass");
        resume.setJobsKey("54321");
        resume.setStatus("Pending");

        assertEquals("NewEmployer", resume.getEmployer());
        assertEquals("NewEmployee", resume.getEmployee());
        assertEquals("Product Manager", resume.getJobName());
        assertEquals("Pass", resume.getFlag());
        assertEquals("54321", resume.getJobsKey());
        assertEquals("Pending", resume.getStatus());

    }

    /**
     * Testing resume key set and get.
     */
    @Test
    public void testModelResumeKey() {
        ModelResume resume = new ModelResume();
        resume.setKey("abc123");
        assertEquals("abc123", resume.getKey());
    }

}