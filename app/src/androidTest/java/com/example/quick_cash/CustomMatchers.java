package com.example.quick_cash;

import com.example.quick_cash.Model.ModelJobPosition;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

/**
 * This class create custom matchers to find the job post match
 */
public class CustomMatchers {
    public static Matcher<Object> withJobName(final String jobName) {
        return new TypeSafeMatcher<Object>() {
            /**
             * Check if item is Model_Job_Position type
             * @param item -> object type pull from database
             * @return -> true if correct data type
             */
            @Override
            protected boolean matchesSafely(Object item) {
                if (item instanceof ModelJobPosition) {
                    return ((ModelJobPosition) item).getJobName().equals(jobName);
                }
                return false;
            }

            /**
             * @param description -> description class
             * The description to be built or appended to.
             */
            @Override
            public void describeTo(Description description) {
                description.appendText("with jobName: " + jobName);
            }
        };
    }
}
