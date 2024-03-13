package com.example.quick_cash;


import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.CoreMatchers.anything;
import static org.junit.Assert.assertEquals;

import android.content.Context;
import android.content.Intent;
import android.widget.ListView;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import com.example.quick_cash.Adapter.AdapterResume;
import com.example.quick_cash.MainHomepages.BossActivity;
import com.example.quick_cash.Model.ModelResume;


import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;

@RunWith(AndroidJUnit4.class)
public class ApproveResumesActivityTest {

    /**
     * Variables
     */

    private List<ModelResume> mockDataList;

    private Context mockContext;
    private AdapterResume ResumeAdapter;

    public static final String JOB_NAME = "First Job";
    public static final String JOB_EMPLOYEE = "a3";

    public static final String JOB_EMPLOTER = "a4";

    public static final String RESUME_FLAG = "NULL";

    public static final String JOB_KEY = "-NiN6zMKj73W4GAwmEQr";

    public static final String RESUME_STATUS = "no";


    @Rule
    public ActivityTestRule<BossActivity> activityRule = new ActivityTestRule<>(BossActivity.class);

    /**
     * Setup activity with all necessary functions
     *
     */

    @Before
    public void setUp() throws IllegalAccessException, InstantiationException {
        mockContext = activityRule.getActivity();
        mockDataList = new ArrayList<>();
        ModelResume resume = new ModelResume(JOB_EMPLOTER, JOB_EMPLOYEE, JOB_NAME, RESUME_FLAG, JOB_KEY, RESUME_STATUS);
        mockDataList.add(resume);
        ResumeAdapter = new AdapterResume(mockContext, mockDataList);
        activityRule = new ActivityTestRule<>(BossActivity.class);
        activityRule.launchActivity(new Intent());
    }


    /**
     * Process the entries and delete the entries
     */
    @Test
    public void testApproveClick() {
        ListView listView = activityRule.getActivity().findViewById(R.id.listView_workers);
        activityRule.getActivity().runOnUiThread(() -> {
            listView.setAdapter(ResumeAdapter);
        });

        onData(anything())
                .inAdapterView(withId(R.id.listView_workers))
                .atPosition(0)
                .onChildView(withId(R.id.resume_pass))
                .perform(click());

        assertEquals(0, ResumeAdapter.getDataList().size());

    }
}