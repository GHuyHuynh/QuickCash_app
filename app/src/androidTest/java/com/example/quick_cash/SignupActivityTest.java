package com.example.quick_cash;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.matcher.ViewMatchers.withId;


import androidx.test.ext.junit.rules.ActivityScenarioRule;

import com.example.quick_cash.Credentials.SignupActivity;

import org.junit.Rule;
import org.junit.Test;

/**
 * Espresso test for Signup activity class
 */
public class SignupActivityTest {
    public static final String STRING_TO_BE_TYPED = "username";

    @Rule
    public ActivityScenarioRule<SignupActivity> activityScenarioRule = new ActivityScenarioRule<>(SignupActivity.class);

    /**
     * Check when typed wrong email
     */
    @Test
    public void wrongEmailCheck(){
        onView(withId(R.id.signup_username)).perform(typeText(STRING_TO_BE_TYPED));
        onView(withId(R.id.signup_email)).perform(typeText(STRING_TO_BE_TYPED));
        onView(withId(R.id.signup_password)).perform(typeText(STRING_TO_BE_TYPED + "p@ass"));
        onView(withId(R.id.signup_button)).perform(click());
    }

    /**
     * Check when typed correct email
     */
    @Test
    public void correctEmailCheck(){
        onView(withId(R.id.signup_username)).perform(typeText(STRING_TO_BE_TYPED));
        onView(withId(R.id.signup_email)).perform(typeText(STRING_TO_BE_TYPED + "@gmail.com"));
        onView(withId(R.id.signup_password)).perform(typeText(STRING_TO_BE_TYPED));
        onView(withId(R.id.signup_button)).perform(click());
    }

    /**
     * Check when type a lower limit password
     */
    @Test
    public void wrongPasswordCheckLowerLimit(){
        onView(withId(R.id.signup_username)).perform(typeText(STRING_TO_BE_TYPED));
        onView(withId(R.id.signup_email)).perform(typeText(STRING_TO_BE_TYPED + "@gmail.com"));
        onView(withId(R.id.signup_password)).perform(typeText("p@ass"));
        onView(withId(R.id.signup_button)).perform(click());
    }

    /**
     * Check when type a upper limit password
     */
    @Test
    public void wrongPasswordCheckUpperLimit(){
        onView(withId(R.id.signup_username)).perform(typeText(STRING_TO_BE_TYPED));
        onView(withId(R.id.signup_email)).perform(typeText(STRING_TO_BE_TYPED + "@gmail.com"));
        onView(withId(R.id.signup_password)).perform(typeText("passafsg@fyfhdjgyrggjff"));
        onView(withId(R.id.signup_button)).perform(click());
    }

    /**
     * Check when type a password with only characters
     */
    @Test
    public void wrongPasswordCharCheck(){
        onView(withId(R.id.signup_username)).perform(typeText(STRING_TO_BE_TYPED));
        onView(withId(R.id.signup_email)).perform(typeText(STRING_TO_BE_TYPED + "@gmail.com"));
        onView(withId(R.id.signup_password)).perform(typeText("asdbasdjg"));
        onView(withId(R.id.signup_button)).perform(click());
    }

    /**
     * Check when type a correct password
     */
    @Test
    public void correctPasswordCheck(){
        onView(withId(R.id.signup_username)).perform(typeText(STRING_TO_BE_TYPED));
        onView(withId(R.id.signup_email)).perform(typeText(STRING_TO_BE_TYPED + "@gmail.com"));
        onView(withId(R.id.signup_password)).perform(typeText("asdbasdjg@"));
        onView(withId(R.id.signup_button)).perform(click());
    }



}