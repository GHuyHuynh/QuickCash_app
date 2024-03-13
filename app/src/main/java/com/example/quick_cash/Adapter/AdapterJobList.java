package com.example.quick_cash.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.quick_cash.AppUser.AppUser;
import com.example.quick_cash.Model.ModelJobPosition;
import com.example.quick_cash.R;

import java.util.ArrayList;

/**
 * Adapter to for job listing of approved user job history
 */
public class AdapterJobList extends ArrayAdapter<ModelJobPosition> {
    public AdapterJobList(@NonNull Context context, ArrayList<ModelJobPosition> jobPostArrayList) {
        super(context, R.layout.user_list, jobPostArrayList);
    }

    /**
     * Put all data in the job list into listview
     * @param position -> position of the individual item
     * @param view -> Current view of the activity
     * @param parent -> Parent of the listview
     * @return -> ListView with all job history and it corresponding fields
     */
    @NonNull
    @Override
    public View getView(int position, @Nullable View view, @NonNull ViewGroup parent) {
        ModelJobPosition modelJobPosition = getItem(position);

        if (view == null) {
            view = LayoutInflater.from(getContext()).inflate(R.layout.adapter_job_post, parent, false);
        }

        TextView listJobName = view.findViewById(R.id.job_list_name);
        TextView listJobDate = view.findViewById(R.id.job_list_date);
        TextView listJobDuration = view.findViewById(R.id.job_list_duration);
        TextView listJobLocation = view.findViewById(R.id.job_list_location);
        TextView listJobEmployer = view.findViewById(R.id.job_list_employer);
        TextView listJobUrgency = view.findViewById(R.id.job_list_urgency);
        TextView listJobSalary = view.findViewById(R.id.job_list_salary);
        TextView listJobDescription = view.findViewById(R.id.job_list_description);

        listJobName.setText(modelJobPosition.getJobName());
        listJobDate.setText("Date: " + modelJobPosition.getJobDate());
        listJobDuration.setText("Duration: " + String.valueOf(modelJobPosition.getJobDuration()));
        listJobLocation.setText("Location: " + modelJobPosition.getJobLocation());
        listJobEmployer.setText("Employer: " + modelJobPosition.getJobEmployer());
        listJobUrgency.setText("Urgency: " + modelJobPosition.getJobUrgency());
        listJobSalary.setText("Salary: " + String.valueOf(modelJobPosition.getJobSalary()));
        listJobDescription.setText("Description: " + modelJobPosition.getJobDes());

        return view;
    }
}
