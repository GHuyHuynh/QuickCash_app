package com.example.quick_cash;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.intent.Intents;

import com.example.quick_cash.Credentials.LoginActivity;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Espresso test class for Login Activity class
 */
public class LoginActivityTest {

    /**
     * Variables
     */
    public static final String USERNAME_TO_BE_TYPED = "test";
    public static final String PASSWORD_TO_BE_TYPED = "test1234!";

    static ActivityScenario<LoginActivity> scenario;

    /**
     * Set up activity with all the necessary buttons and functions
     */
    @Before
    public void setup() {
        scenario = ActivityScenario.launch(LoginActivity.class);
        scenario.onActivity(activity -> {
            activity.setupLoginButton();
            activity.setupSignupRedirect();
        });

        Intents.init();
    }

    @After
    public void cleanup(){
        Intents.release();
    }

    /**
     * Check if password match the Regex
     */
    @Test
    public void correctPasswordCheck() {
        onView(withId(R.id.login_username)).perform(typeText(USERNAME_TO_BE_TYPED));
        onView(withId(R.id.login_password)).perform(typeText("asdbasdjg@"));
        onView(withId(R.id.login_button)).perform(click());
    }

    /**
     * Check if the password match the regex
     * Stay on the same activity on complete
     */
    @Test
    public void wrongPasswordCheck() {
        onView(withId(R.id.login_username)).perform(typeText(USERNAME_TO_BE_TYPED));
        onView(withId(R.id.login_password)).perform(typeText("Password123"));
        onView(withId(R.id.login_button)).perform(click());
        onView(withId(R.id.login_button)).check(matches(isDisplayed()));
    }

    /**
     * Check if the password is empty
     * Stay on the activity on complete
     */
    @Test
    public void checkPasswordEmpty() {
        onView(withId(R.id.login_username)).perform(typeText(USERNAME_TO_BE_TYPED));
        onView(withId(R.id.login_password)).perform(typeText(" "));
        onView(withId(R.id.login_button)).perform(click());
        onView(withId(R.id.login_button)).check(matches(isDisplayed()));
    }

    /**
     * Check if the username is empty
     * Stay on the same activity on complete
     */
    @Test
    public void checkUsernameEmpty() {
        onView(withId(R.id.login_username)).perform(typeText(" "));
        onView(withId(R.id.login_password)).perform(typeText(PASSWORD_TO_BE_TYPED));
        onView(withId(R.id.login_button)).perform(click());
        onView(withId(R.id.login_button)).check(matches(isDisplayed()));
    }
}
