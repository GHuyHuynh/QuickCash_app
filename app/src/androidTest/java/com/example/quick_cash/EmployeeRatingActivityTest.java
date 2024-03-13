package com.example.quick_cash;

import static androidx.test.espresso.Espresso.closeSoftKeyboard;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import android.content.Intent;
import android.view.View;

import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.IdlingResource;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.intent.Intents;
import androidx.test.espresso.intent.matcher.IntentMatchers;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.rule.ActivityTestRule;

import com.example.quick_cash.Employee.EmployeeProfileActivity;
import com.example.quick_cash.Employee.EmployeeRatingActivity;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

public class EmployeeRatingActivityTest {

    @Rule
    public ActivityTestRule<EmployeeRatingActivity> activityRule =
            new ActivityTestRule<>(EmployeeRatingActivity.class, true, false);


    public static final String FEEDBACK = "feedbackTest";

    static ActivityScenario<EmployeeRatingActivity> scenario;

    /**
     * setup the activity with all necessary functions
     */
    @Before
    public void setup(){

        Intent intent = new Intent();

        intent.putExtra("email", "1234@gmail.com");
        intent.putExtra("username", "tt");
        activityRule.launchActivity(intent);

        Intents.init();
    }

    /**
     * Testing submit button functionallity
     */
    @Test
    public void testSubmitButton(){
        onView(ViewMatchers.withId(R.id.user_feedback)).perform(typeText(FEEDBACK));

        onView(ViewMatchers.withId(R.id.user_rateBar))
                .perform(click());

        closeSoftKeyboard();

        onView(ViewMatchers.withId(R.id.user_submit_feedback)).perform(click());

        onView(ViewMatchers.withId(R.id.rateUser)).check(matches(isDisplayed()));
        Intents.release();
    }

    /**
     * Testing redirect button functionallity
     */
    @Test
    public void testRedirectButton(){
        onView(ViewMatchers.withId(R.id.userRedirect)).perform(click());
        onView(ViewMatchers.withId(R.id.rateUser)).check(matches(isDisplayed()));

        Intents.release();
    }

    /**
     * Testing invalid feedback,
     * should stay on same page
     */
    @Test
    public void testInvalidTextFeedBack(){
        onView(ViewMatchers.withId(R.id.user_feedback)).perform(typeText(""));

        onView(ViewMatchers.withId(R.id.user_rateBar))
                .perform(click());

        closeSoftKeyboard();

        onView(ViewMatchers.withId(R.id.user_submit_feedback)).perform(click());
        onView(ViewMatchers.withId(R.id.user_submit_feedback)).check(matches(isDisplayed()));

        Intents.release();
    }

    /**
     * Testing valid feedback
     * should change page to employee profile
     */
    @Test
    public void testValidFeedback(){
        onView(ViewMatchers.withId(R.id.user_feedback)).perform(typeText(FEEDBACK));

        onView(ViewMatchers.withId(R.id.user_rateBar))
                .perform(click());

        closeSoftKeyboard();

        onView(ViewMatchers.withId(R.id.user_submit_feedback)).perform(click());

        onView(ViewMatchers.withId(R.id.rateUser)).check(matches(isDisplayed()));
        Intents.release();
    }

}