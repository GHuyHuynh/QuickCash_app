package com.example.quick_cash;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import androidx.test.espresso.intent.Intents;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.runner.AndroidJUnit4;

import com.example.quick_cash.MainHomepages.BossActivity;
import com.example.quick_cash.MainHomepages.SelectRoleActivity;
import com.example.quick_cash.MainHomepages.WorkerActivity;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Espresso Test for select role activity
 */
@RunWith(AndroidJUnit4.class)
public class SelectRoleActivityTest {
    /**
     * Set up project and activity
     */
    @Rule
    public ActivityScenarioRule<SelectRoleActivity> activityScenarioRule =
            new ActivityScenarioRule<>(SelectRoleActivity.class);
    @Before
    public void setup() {
        Intents.init();
    }

    /**
     * Test if switch to Boss activity successfully
     */
    @Test
    public void testLaunchBossActivity() {
        onView(withId(R.id.button2)).perform(click());
        intended(hasComponent(BossActivity.class.getName()));
    }

    /**
     * Test if switch to Worker activity successfully
     */
    @Test
    public void testLaunchWorkerActivity() {
        onView(withId(R.id.button)).perform(click());
        intended(hasComponent(WorkerActivity.class.getName()));
    }

    @After
    public void tearDown() {
        Intents.release();
    }



}
