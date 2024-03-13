package com.example.quick_cash;

import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import androidx.test.espresso.Espresso;
import androidx.test.espresso.intent.Intents;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.quick_cash.MainHomepages.WorkerActivity;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Espresso test class for Worker Activity
 */
@RunWith(AndroidJUnit4.class)
public class WorkerActivityTest {
    /**
     * Set up and tear down
     */
    @Rule
    public ActivityScenarioRule<WorkerActivity> activityScenarioRule =
            new ActivityScenarioRule<>(WorkerActivity.class);
    @Before
    public void setup() {
        Intents.init();
    }
    @After
    public void teardown() {
        Intents.release();
    }

    /**
     * Test if the list of job post holder is displayed
     */
    @Test
    public void testListViewIsDisplayed() {
        // Check if ListView is displayed
        Espresso.onView(withId(R.id.listView_positions)).check(matches(isDisplayed()));
    }

    /**
     * Test if nav bar search button is visible
     */
    @Test
    public void testsearchButtonIsVisible(){
        Espresso.onView(withId(R.id.search_bar)).check(matches(isDisplayed()));
    }


}
