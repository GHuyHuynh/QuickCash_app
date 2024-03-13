package com.example.quick_cash;

import android.content.Intent;

import androidx.test.espresso.Espresso;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.assertion.ViewAssertions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.rule.ActivityTestRule;

import com.example.quick_cash.MainHomepages.PreferredJobActivity;

import org.junit.Rule;
import org.junit.Test;

public class PreferredJobActivityUITest {

    /**
     * setup the activity with all necessary functions
     */
    @Rule
    public ActivityTestRule<PreferredJobActivity> activityRule = new ActivityTestRule<>(
            PreferredJobActivity.class,
            true,     // initialTouchMode
            false);   // launchActivity. False to customize the intent per test method


    /**
     * Testing adding username for preferred user
     */
    @Test
    public void testAddKeyword() {
        // Set up the intent with a test username
        Intent intent = new Intent();
        intent.putExtra("extractUsername", "testUser");
        activityRule.launchActivity(intent);

        // Type a keyword in the editText
        Espresso.onView(ViewMatchers.withId(R.id.editTextNewKeyword))
                .perform(ViewActions.typeText("TestKeyword"), ViewActions.closeSoftKeyboard());

        // Click the "Add Keyword" button
        Espresso.onView(ViewMatchers.withId(R.id.buttonAddKeyword))
                .perform(ViewActions.click());

        // Check if the keyword is displayed in the list
        Espresso.onView(ViewMatchers.withId(R.id.listViewExistingKeywords))
                .check(ViewAssertions.matches(ViewMatchers.withText("TestKeyword")));
    }

    /**
     * Removing preferred user test
     */
    @Test
    public void testDeleteKeyword() {
        // Set up the intent with a test username
        Intent intent = new Intent();
        intent.putExtra("extractUsername", "testUser");
        activityRule.launchActivity(intent);

        // Type a keyword in the editText
        Espresso.onView(ViewMatchers.withId(R.id.editTextNewKeyword))
                .perform(ViewActions.typeText("TestKeyword"), ViewActions.closeSoftKeyboard());

        // Click the "Add Keyword" button
        Espresso.onView(ViewMatchers.withId(R.id.buttonAddKeyword))
                .perform(ViewActions.click());

        // Click the "Delete Keyword" button
        Espresso.onView(ViewMatchers.withId(R.id.buttonDeleteKeyword))
                .perform(ViewActions.click());

        // Check if the keyword is not displayed in the list after deletion
        Espresso.onView(ViewMatchers.withId(R.id.listViewExistingKeywords))
                .check(ViewAssertions.doesNotExist());
    }

}
