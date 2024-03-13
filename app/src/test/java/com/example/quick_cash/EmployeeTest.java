package com.example.quick_cash;

import static org.junit.Assert.*;

import com.example.quick_cash.Employee.Employee;
import com.example.quick_cash.Model.ModelJobPosition;

import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;

public class EmployeeTest {

    static Employee employee;

    @BeforeClass
    public static void setup(){
        employee = new Employee("tt", "1234@gmail.com");
    }

    /**
     * Test if employee field is blank when first initialized.
     */
    @Test
    public void initialEmployeeStateTest() {
        assertEquals(0.0, employee.getEmployeeRating(), 0.001);
        assertEquals(new ArrayList<ModelJobPosition>(), employee.getJobCompletedList());
        assertEquals(0.0, employee.getTotalIncome(), 0.001);
    }

    /**
     * Test if initial employee rating is 0.0
     *
     */
    @Test
    public void employeeRatingTest(){
        employee.setEmployeeRating(0.0);
        double expectedRating = 0.0;
        double actualRating= employee.getEmployeeRating();

        assertEquals(expectedRating ,actualRating, 0.001);
    }

    /**
     * Tests if you can set employee rating
     */
    @Test
    public void setEmployeeRatingTest(){
        double newRating = 4.5;
        employee.setEmployeeRating(newRating);

        assertEquals(newRating ,employee.getEmployeeRating(), 0.001);
    }

    /**
     * Tests if you can add a job to employee JobCompletedList
     */
    @Test
    public void jobCompletedListTest() {
        ArrayList<ModelJobPosition> jobList = new ArrayList<>();
        ModelJobPosition job1 = new ModelJobPosition();
        jobList.add(job1);

        employee.setJobCompletedList(jobList);
        assertEquals(jobList, employee.getJobCompletedList());
    }

    /**
     * Test set and get for totalIncome
     */
    @Test
    public void totalIncomeTest() {
        double newIncome = 100.0;
        employee.setTotalIncome(newIncome);
        assertEquals(newIncome, employee.getTotalIncome(), 0.001);
    }



}