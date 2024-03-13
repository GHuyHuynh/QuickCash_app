package com.example.quick_cash.Model;

/**
 * Resume Model that includes the employer, employee, job key, job name, job salary,
 * and resume key. It has 2 constructors for different uses  and getters and setters
 * for all variables
 */
public class ModelResume {

    private String employer;
    private String employee;
    // pass or refuse
    private String flag;

    private String jobsKey;
    //ok or no
    private String status;
    private String jobPay;

    private String jobName;

    private String key;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public String getEmployer() {
        return employer;
    }

    public void setEmployer(String employer) {
        this.employer = employer;
    }

    public String getEmployee() {
        return employee;
    }

    public void setEmployee(String employee) {
        this.employee = employee;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public String getJobsKey() {
        return jobsKey;
    }

    public void setJobsKey(String jobsKey) {
        this.jobsKey = jobsKey;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSalary(){return this.jobPay;}

    public ModelResume(String employer, String employee, String jobName, String flag, String jobsKey, String status, String jobPay) {
        this.employer = employer;
        this.employee = employee;
        this.flag = flag;
        this.jobsKey = jobsKey;
        this.status = status;
        this.jobName=jobName;
        this.jobPay = jobPay;
    }

    public ModelResume(String employer, String employee, String jobName, String flag, String jobsKey, String jobPay) {
        this.employer = employer;
        this.employee = employee;
        this.flag = flag;
        this.jobsKey = jobsKey;
        this.jobName=jobName;
        this.jobPay = jobPay;
    }

    public ModelResume(){}



}
