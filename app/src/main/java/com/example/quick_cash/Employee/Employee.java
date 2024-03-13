package com.example.quick_cash.Employee;

import com.example.quick_cash.Model.ModelJobPosition;
import com.example.quick_cash.AppUser.AppUser;

import java.util.ArrayList;

/**
 * Employee class -> in new iteration is basically an app user
 */
public class Employee extends AppUser {
    double employeeRating;
    ArrayList<ModelJobPosition> jobCompletedList;
    double totalIncome;

    /**
     * Constructor for User object
     * @param username -> username
     * @param email -> email
     */
    public Employee(String username, String email) {
        super(username, email);
        this.employeeRating = 0.0;
        this.jobCompletedList = new ArrayList<>();
        this.totalIncome = 0;
    }

    /**
     * List of all completed job
     * @return
     */
    public ArrayList<ModelJobPosition> getJobCompletedList() {
        return this.jobCompletedList;
    }

    /**
     * set the completed job list
     * @param jobCompletedList
     */
    public void setJobCompletedList (ArrayList<ModelJobPosition> jobCompletedList) {
        this.jobCompletedList = jobCompletedList;
    }

    /**
     * Get the user rating
     * @return
     */
    public double getEmployeeRating() {
        return employeeRating;
    }

    /**
     * Set user rating
     * @param employeeRating
     */
    public void setEmployeeRating(double employeeRating) {
        this.employeeRating = employeeRating;
    }

    /**
     * Get the total user income
     * @return
     */
    public double getTotalIncome() {
        return totalIncome;
    }

    /**
     * Set the user total income
     * @param totalIncome
     */
    public void setTotalIncome(double totalIncome) {
        this.totalIncome = totalIncome;
    }
}
