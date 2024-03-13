package com.example.quick_cash;

import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import androidx.test.espresso.Espresso;
import androidx.test.espresso.intent.Intents;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.quick_cash.MainHomepages.BossActivity;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * This class test BossActivity with Espresso Test
 */
@RunWith(AndroidJUnit4.class)
public class BossActivityTest {

    /**
     * Initialize rules and setup of the activity
     */
    @Rule
    public ActivityScenarioRule<BossActivity> activityScenarioRule =
            new ActivityScenarioRule<>(BossActivity.class);
    @Before
    public void setup() {
        Intents.init();
    }

    /**
     * Tear down class
     */
    @After
    public void teardown() {
        Intents.release();
    }

    /**
     * Check if the list view holder of worker is displayed or not
     */
    @Test
    public void testListViewIsDisplayed() {
        // Check if ListView is displayed
        Espresso.onView(withId(R.id.listView_workers)).check(matches(isDisplayed()));
    }
}
