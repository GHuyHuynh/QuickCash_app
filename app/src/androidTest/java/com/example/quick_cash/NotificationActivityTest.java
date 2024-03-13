package com.example.quick_cash;

import android.content.Context;
import android.content.Intent;

import androidx.test.espresso.Espresso;
import androidx.test.espresso.assertion.ViewAssertions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import com.example.quick_cash.Adapter.AdapterPosition;
import com.example.quick_cash.Model.ModelJobPosition;
import com.example.quick_cash.Notification.NotificationActivity;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;

import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;

@RunWith(AndroidJUnit4.class)
public class NotificationActivityTest {

    private List<ModelJobPosition> mockDataList;
    private Context mockContext;
    private AdapterPosition positionAdapter;

    public static String jobName = "Testing DB 3";
    public static String jobDes = "2002020";
    public static String jobDate = "2023/12/12";
    public static int jobDuration = 210;
    public static String jobUrgency = "Low";
    public static int jobSalary = 202121;
    public static String jobLocation = "Dalplex";
    public static String jobEmployer = "test";
    public static String jobKey = "-NjxVj8V2d2ae177GJAI";
    public static String username = "admin";

    @Rule
    public ActivityTestRule<NotificationActivity> activityRule = new ActivityTestRule<>(NotificationActivity.class);

    @Rule
    public ActivityScenarioRule<NotificationActivity> activityScenarioRule =
            new ActivityScenarioRule<>(NotificationActivity.class);

    /**
     * setup the activity with all necessary functions
     */
    @Before
    public void setUp() {
        mockContext = activityRule.getActivity();
        mockDataList = new ArrayList<>();
        ModelJobPosition job = new ModelJobPosition(jobName, jobDes, jobDate, jobDuration,
                jobUrgency, jobSalary, jobLocation);
        mockDataList.add(job);
        positionAdapter = new AdapterPosition(mockContext, mockDataList);

        // Create an intent with username and launch the activity
        Intent intent = new Intent(mockContext, NotificationActivity.class);
        intent.putExtra("extractUsername", username);
        activityRule.launchActivity(intent);
    }

    @Test
    public void testListViewIsDisplayed() {
        Espresso.onView(ViewMatchers.withId(R.id.listView_preferredJobs)).check(ViewAssertions.matches(isDisplayed()));
    }

    @Test
    public void testReturnButtonIsVisible() {
        Espresso.onView(ViewMatchers.withId(R.id.btnEnableLocationTracking)).check(ViewAssertions.matches(isDisplayed()));
    }
}
