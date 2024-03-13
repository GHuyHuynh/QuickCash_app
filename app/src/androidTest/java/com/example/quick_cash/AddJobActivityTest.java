package com.example.quick_cash;

import static androidx.test.espresso.Espresso.closeSoftKeyboard;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;

import androidx.test.core.app.ActivityScenario;

import com.example.quick_cash.JobPost.AddJobActivity;

import org.junit.Before;
import org.junit.Test;

/**
 * This class included all Espresso test for AddJobActivity class
 */
public class AddJobActivityTest {

    /**
     * Variables
     */
    public static final String JOB_TITLE_TYPE = "Espresso Job Test";
    public static final String JOB_DES_TYPE = "This is a job added by espresso test";
    public static final String  JOB_DATE_TYPE = "2024/12/12";
    public static final String EXPIRED_JOB_DATE_TYPE = "2022/11/25";
    public static final int JOB_DURATION_TYPE = 50;
    public static final String JOB_URGENCY_TYPE = "Low urgent";
    public static final int JOB_SALARY_TYPE = 100;
    public static final String JOB_LOCATION_TYPE = "Halifax Shopping Center";
    public static final String INVALID_JOB_LOCATION_TYPE = "Halifax";

    static ActivityScenario<AddJobActivity> scenario;

    /**
     * Set up the activity with all the necessary start up function
     */
    @Before
    public void setup() {
        scenario = ActivityScenario.launch(AddJobActivity.class);
        scenario.onActivity(activity -> {
            activity.setupSubmitJobButton("Huy");
        });
    }

    /**
     * Check if the date is wrong in UI test
     * Should not change activity on complete
     */
    @Test
    public void wrongDateCheck() {
        onView(withId(R.id.add_job_name)).perform(typeText(JOB_TITLE_TYPE));
        onView(withId(R.id.add_job_date)).perform(typeText(EXPIRED_JOB_DATE_TYPE));
        onView(withId(R.id.add_job_duration)).perform(typeText(String.valueOf(JOB_DURATION_TYPE)));
        onView(withId(R.id.add_job_urgency)).perform(typeText(JOB_URGENCY_TYPE));
        onView(withId(R.id.add_job_salary)).perform(typeText(String.valueOf(JOB_SALARY_TYPE)));
        onView(withId(R.id.add_job_location)).perform(typeText(JOB_LOCATION_TYPE));
        closeSoftKeyboard();
        onView(withId(R.id.add_job_des)).perform(typeText(JOB_DES_TYPE));
        closeSoftKeyboard();
        onView(withId(R.id.add_job_button)).perform(click());
        onView(withId(R.id.add_job_button)).check(matches(isDisplayed()));
    }

    /**
     * Check for wronng location in UI
     * Should not change activity on complete test
     */
    @Test
    public void wrongLocationCheck() {
        onView(withId(R.id.add_job_name)).perform(typeText(JOB_TITLE_TYPE));
        onView(withId(R.id.add_job_date)).perform(typeText(JOB_DATE_TYPE));
        onView(withId(R.id.add_job_duration)).perform(typeText(String.valueOf(JOB_DURATION_TYPE)));
        onView(withId(R.id.add_job_urgency)).perform(typeText(JOB_URGENCY_TYPE));
        onView(withId(R.id.add_job_salary)).perform(typeText(String.valueOf(JOB_SALARY_TYPE)));
        onView(withId(R.id.add_job_location)).perform(typeText(INVALID_JOB_LOCATION_TYPE));
        closeSoftKeyboard();
        onView(withId(R.id.add_job_des)).perform(typeText(JOB_DES_TYPE));
        closeSoftKeyboard();
        onView(withId(R.id.add_job_button)).perform(click());
        onView(withId(R.id.add_job_button)).check(matches(isDisplayed()));
    }
}
