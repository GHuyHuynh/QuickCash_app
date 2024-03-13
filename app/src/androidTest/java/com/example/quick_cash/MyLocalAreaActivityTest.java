package com.example.quick_cash;

import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.assertion.ViewAssertions;
import androidx.test.espresso.matcher.ViewMatchers;

import com.example.quick_cash.MyLocalArea.MyLocalAreaActivity;

import org.junit.Before;
import org.junit.Test;

public class MyLocalAreaActivityTest {
    static ActivityScenario<MyLocalAreaActivity> scenario;

    /**
     * Setting up for testing
     */
    @Before
    public void setup() {
        scenario = ActivityScenario.launch(MyLocalAreaActivity.class);
        scenario.onActivity(activity -> {
            activity.enableLocationService();
            activity.setupTypedLocationButton();
        });
    }

    /**
     * Testing for if the map is displayed when clicking on the 'Local area' button
     */
    @Test
    public void checkLocalMapIsDisplayed() {
        Espresso.onView(ViewMatchers.withId(R.id.enable_location_service_button)).perform(ViewActions.click());
        Espresso.onView(ViewMatchers.withId(R.id.local_map_view)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
    }

    /**
     * Testing for whether or not the location is existed, display if existed
     */
    @Test
    public void checkLocationExist() {
        Espresso.onView(ViewMatchers.withId(R.id.local_location)).perform((ViewActions.typeText("Halifax Shopping Center")));
        Espresso.onView(ViewMatchers.withId(R.id.submit_location_button)).perform(ViewActions.click());
        Espresso.onView(ViewMatchers.withId(R.id.local_map_view)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
    }

    /**
     * Testing if the location is not existed, then the map will not displayed
     */
    @Test
    public void checkLocationDoNotExist() {
        Espresso.onView(ViewMatchers.withId(R.id.local_location)).perform((ViewActions.typeText("Death Star")));
        Espresso.onView(ViewMatchers.withId(R.id.submit_location_button)).perform((ViewActions.click()));
        Espresso.onView(ViewMatchers.withId(R.id.local_map_view)).check(ViewAssertions.doesNotExist());
    }

    /**
     * Testing for special character location, will not displayed if it contains special characters
     */
    @Test
    public void checkSpecialCharLocation() {
        Espresso.onView(ViewMatchers.withId(R.id.local_location)).perform((ViewActions.typeText("D@lh0us13 Un1v3rs1t4")));
        Espresso.onView(ViewMatchers.withId(R.id.submit_location_button)).perform((ViewActions.click()));
        Espresso.onView(ViewMatchers.withId(R.id.local_map_view)).check(ViewAssertions.doesNotExist());
    }

    /**
     * Testing for if the submit button is existed
     */
    @Test
    public void checkSubmitButtonIsDisplayed() {
        Espresso.onView(ViewMatchers.withId(R.id.submit_location_button)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
    }

    /**
     * Testing if the my location button is existed
     */
    @Test
    public void checkMyLocationButtonIsDisplayed() {
        Espresso.onView(ViewMatchers.withId(R.id.enable_location_service_button)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
    }
}
