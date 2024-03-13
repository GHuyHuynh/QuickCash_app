package com.example.quick_cash;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import com.example.quick_cash.Model.ModelJobPosition;

public class ModelJobPositionUnitTest {

    /**
     * Create mock ModelJobPosition and check if it is correctly stored.
     */
    @Test
    public void testModelJobPositionConstructor() {
        ModelJobPosition jobPosition = new ModelJobPosition(
                "Software Engineer",
                "Develop software applications",
                "2023-12-01",
                6,
                "Urgent",
                80000,
                "City A"
        );

        assertEquals("Software Engineer", jobPosition.getJobName());
        assertEquals("Develop software applications", jobPosition.getJobDes());
        assertEquals("2023-12-01", jobPosition.getJobDate());
        assertEquals(6, jobPosition.getJobDuration());
        assertEquals("Urgent", jobPosition.getJobUrgency());
        assertEquals(80000, jobPosition.getJobSalary());
        assertEquals("City A", jobPosition.getJobLocation());
    }

    /**
     * Testing setter and getting for ModelJobPosition
     */
    @Test
    public void testModelJobPositionSettersAndGetters() {
        ModelJobPosition jobPosition = new ModelJobPosition();

        jobPosition.setJobName("Data Analyst");
        jobPosition.setJobDes("Analyze data for insights");
        jobPosition.setJobDate("2023-12-15");
        jobPosition.setJobDuration(3);
        jobPosition.setJobUrgency("Normal");
        jobPosition.setJobSalary(60000);
        jobPosition.setJobLocation("City B");

        assertEquals("Data Analyst", jobPosition.getJobName());
        assertEquals("Analyze data for insights", jobPosition.getJobDes());
        assertEquals("2023-12-15", jobPosition.getJobDate());
        assertEquals(3, jobPosition.getJobDuration());
        assertEquals("Normal", jobPosition.getJobUrgency());
        assertEquals(60000, jobPosition.getJobSalary());
        assertEquals("City B", jobPosition.getJobLocation());
    }


}
