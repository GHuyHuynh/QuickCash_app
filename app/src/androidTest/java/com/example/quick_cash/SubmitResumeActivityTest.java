package com.example.quick_cash;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.CoreMatchers.anything;
import static org.junit.Assert.assertEquals;

import android.content.Context;
import android.content.Intent;
import android.widget.ListView;

import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;

import com.example.quick_cash.Adapter.AdapterPosition;
import com.example.quick_cash.MainHomepages.WorkerActivity;
import com.example.quick_cash.Model.ModelJobPosition;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;

@RunWith(AndroidJUnit4.class)
public class SubmitResumeActivityTest {

    private Context mockContext;
    private List<ModelJobPosition> mockDataList;
    private AdapterPosition positionAdapter;

    public static final String JOB_TITLE_TYPE = "First Job";
    public static final String JOB_DES_TYPE = "No";
    public static final String JOB_DATE_TYPE = "2023/11/14";
    public static final int JOB_DURATION_TYPE = 8;
    public static final String JOB_URGENCY_TYPE = "Low";
    public static final int JOB_SALARY_TYPE = 8;
    public static final String JOB_LOCATION_TYPE = "American";

    public static final String JOB_EMPLOYER = "a4";

    public static final String JOB_KEY = "-NiJMeg3ttP-A6srzRlN";


    @Rule
    public ActivityTestRule<WorkerActivity> activityRule = new ActivityTestRule<>(WorkerActivity.class);

    /**
     * setup the activity with all necessary functions
     */
    @Before
    public void setUp() throws IllegalAccessException, InstantiationException {
        mockContext = activityRule.getActivity();
        mockDataList = new ArrayList<>();
        ModelJobPosition mockData = new ModelJobPosition(JOB_TITLE_TYPE,JOB_DES_TYPE,JOB_DATE_TYPE,JOB_DURATION_TYPE,JOB_URGENCY_TYPE,JOB_SALARY_TYPE,JOB_LOCATION_TYPE);
        mockData.setJobEmployer(JOB_EMPLOYER);
        mockData.setJobKey(JOB_KEY);
        mockDataList.add(mockData);
        positionAdapter = new AdapterPosition(mockContext, mockDataList);
        activityRule = new ActivityTestRule<>(WorkerActivity.class);
        activityRule.launchActivity(new Intent());
    }

    /**
     * Testing submit button functionality.
     */
    @Test
    public void testButtonClick() {
        ListView listView = activityRule.getActivity().findViewById(R.id.listView_positions);
        activityRule.getActivity().runOnUiThread(() -> {
            listView.setAdapter(positionAdapter);
        });

        onData(anything())
                .inAdapterView(withId(R.id.listView_positions))
                .atPosition(0)
                .onChildView(withId(R.id.worker_sendResume))
                .perform(click());

        assertEquals(0, positionAdapter.getDataList().size());

    }



}

