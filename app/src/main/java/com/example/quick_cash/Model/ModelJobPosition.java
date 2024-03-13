package com.example.quick_cash.Model;

import java.io.Serializable;

/**
 * This is a Model_Job_Position object that implements serializable
 * The object includes jobName, job Description, job Date, job Duration,
 * job Urgency, job salary, job location, job employer.
 *
 * The object has an empty constructor as well as a populated one
 *
 * It also includes getters and setters
*/
public class ModelJobPosition implements Serializable {
    private String jobName;
    private String jobDes;
    private String jobDate;
    private int jobDuration;
    private String jobUrgency;
    private int jobSalary;
    private String jobLocation;
    private String jobEmployer;
    private String jobKey;

    public void setJobEmployer(String jobEmployer) {
        this.jobEmployer = jobEmployer;
    }
    public ModelJobPosition() {

    }

    public ModelJobPosition(String jobName, String jobDes, String jobDate,
                            int jobDuration, String jobUrgency, int jobSalary, String jobLocation) {
        this.jobName = jobName;
        this.jobDes = jobDes;
        this.jobDate = jobDate;
        this.jobDuration = jobDuration;
        this.jobUrgency = jobUrgency;
        this.jobSalary = jobSalary;
        this.jobLocation = jobLocation;
    }

    public String getJobName() {
        return jobName;
    }

    public String getJobDes() {
        return jobDes;
    }

    public String getJobDate() {
        return jobDate;
    }

    public int getJobDuration() {
        return jobDuration;
    }

    public String getJobUrgency() {
        return jobUrgency;
    }

    public int getJobSalary() {
        return jobSalary;
    }

    public String getJobLocation() {
        return jobLocation;
    }

    public String getJobEmployer(){
        return jobEmployer;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public void setJobDes(String jobDes) {
        this.jobDes = jobDes;
    }

    public void setJobDate(String jobDate) {
        this.jobDate = jobDate;
    }

    public void setJobDuration(int jobDuration) {
        this.jobDuration = jobDuration;
    }

    public void setJobUrgency(String jobUrgency) {
        this.jobUrgency = jobUrgency;
    }

    public void setJobSalary(int jobSalary) {
        this.jobSalary = jobSalary;
    }

    public void setJobLocation(String jobLocation) {
        this.jobLocation = jobLocation;
    }

    public String getJobKey() {return jobKey;}

    public void setJobKey(String jobKey) {this.jobKey = jobKey;}
}
