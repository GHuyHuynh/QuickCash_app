package com.example.quick_cash;

import static androidx.test.espresso.Espresso.closeSoftKeyboard;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import static org.hamcrest.CoreMatchers.is;

import androidx.test.core.app.ActivityScenario;

import com.example.quick_cash.Search.SearchActivity;

import org.junit.Before;
import org.junit.Test;

/**
 * Espresso test for Search Activity class
 */
public class SearchActivityTest {
    static ActivityScenario<SearchActivity> scenario;

    /**
     * Set up activity and initialize all necessary functions
     */
    @Before
    public void setup() {
        scenario = ActivityScenario.launch(SearchActivity.class);
        scenario.onActivity(activity -> {
            activity.initialize();
            activity.setUpButton();
            activity.redirectToJob();
        });

    }

    /**
     * Test for empty fields on job search
     */
    @Test
    public void emptyFieldsTest(){
        onView(withId(R.id.search_button)).perform(click());

        onView(withId(R.id.user)).perform(click());
    }

    /**
     * Test for job name search
     */
    @Test
    public void jobNameTest(){
        onView(withId(R.id.searchByName)).perform(typeText("Job Title 2"));
        closeSoftKeyboard();
        onView(withId(R.id.search_button)).perform(click());

        onView(withId(R.id.user)).perform(click());
    }

    /**
     * Test for job date search
     */
    @Test
    public void jobDateTest(){
        onView(withId(R.id.searchByDate)).perform(typeText("2023/10/17"));
        closeSoftKeyboard();
        onView(withId(R.id.search_button)).perform(click());

        onView(withId(R.id.user)).perform(click());
    }

    /**
     * Test for job description test
     */
    @Test
    public void jobDescriptionTest(){
        onView(withId(R.id.searchByDescription)).perform(typeText("test job 2"));
        closeSoftKeyboard();
        onView(withId(R.id.search_button)).perform(click());

        onView(withId(R.id.user)).perform(click());
    }

    /**
     * Test for job location test
     */
    @Test
    public void jobLocationTest(){
        onView(withId(R.id.searchByLocation)).perform(typeText("Halifax Shopping Center"));
        closeSoftKeyboard();
        onView(withId(R.id.search_button)).perform(click());

        onView(withId(R.id.user)).perform(click());
    }

    /**
     * Test for job salary test
     */
    @Test
    public void jobSalaryTest(){
        onView(withId(R.id.searchBySalary)).perform(typeText("50"));
        closeSoftKeyboard();
        onView(withId(R.id.search_button)).perform(click());

        onView(withId(R.id.user)).perform(click());
    }

    /**
     * Test for all job fields search
     */
    @Test
    public void allFieldsFilledTest(){
        onView(withId(R.id.searchByName)).perform(typeText("Job Title 2"));
        closeSoftKeyboard();
        onView(withId(R.id.searchByDate)).perform(typeText("2023/10/17"));
        closeSoftKeyboard();
        onView(withId(R.id.searchByDescription)).perform(typeText("test job 2"));
        closeSoftKeyboard();
        onView(withId(R.id.searchByLocation)).perform(typeText("Halifax Shopping Center"));
        closeSoftKeyboard();
        onView(withId(R.id.searchBySalary)).perform(typeText("50"));
        closeSoftKeyboard();
        onView(withId(R.id.search_button)).perform(click());

        onView(withId(R.id.user)).perform(click());
    }

    /**
     * Test for selected job fields
     */
    @Test
    public void certainFieldsFilledTest(){
        onView(withId(R.id.searchByName)).perform(typeText("First Job"));
        closeSoftKeyboard();
        onView(withId(R.id.searchBySalary)).perform(typeText("50"));
        closeSoftKeyboard();
        onView(withId(R.id.search_button)).perform(click());

        onView(withId(R.id.user)).perform(click());
    }
}
