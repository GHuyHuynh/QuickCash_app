package com.example.quick_cash;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.isNotEnabled;
import static androidx.test.espresso.matcher.ViewMatchers.withEffectiveVisibility;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import android.content.Intent;

import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.assertion.ViewAssertions;
import androidx.test.espresso.intent.Intents;
import androidx.test.espresso.intent.rule.IntentsTestRule;
import androidx.test.espresso.matcher.ViewMatchers;

import com.example.quick_cash.AppUser.AppUser;
import com.example.quick_cash.Employee.Employee;
import com.example.quick_cash.Employee.EmployeeProfileActivity;
import com.example.quick_cash.JobPost.AddJobActivity ;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.ArrayList;

public class EmployeeProfileActivityTest {

    ArrayList<AppUser> userArrayList = new ArrayList<>();

    @Rule
    public IntentsTestRule<EmployeeProfileActivity> activityRule =
            new IntentsTestRule<>(EmployeeProfileActivity.class);

    /**
     * setup the activity with all necessary functions
     */
    @Before
    public void setUp() {
        Intent intent = new Intent();
        AppUser userObject = new Employee(null, null);

        userObject.setUsername("tt");
        userObject.setEmail("tt@gmail.com");

        userArrayList.add(userObject);

        intent.putExtra("username", userArrayList.get(0).getUsername());
        intent.putExtra("email", userArrayList.get(0).getEmail());

        activityRule.launchActivity(intent);


        Intents.init();
    }

    @Test
    public void testProfileActivity() {
        Espresso.onView(ViewMatchers.withId(R.id.profile_username)).check(matches(isDisplayed()));

    }
}