package com.example.quick_cash.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.quick_cash.JobPost.JobPostDetails;
import com.example.quick_cash.Model.ModelResume;
import com.example.quick_cash.Model.ModelJobPosition;
import com.example.quick_cash.R;
import com.example.quick_cash.Employee.RecommendedEmployeesActivity;
import com.example.quick_cash.MainHomepages.WorkerActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

/**
 * Adapter class for job position
 */
public class AdapterPosition extends BaseAdapter {

    private Context mContext;

    String resumeDatabaseURL = "https://group-1-job-database-1c791-default-rtdb.firebaseio.com/";
    FirebaseDatabase resumeDataBase;
    DatabaseReference resumeDatabaseReference;
    ArrayList<String> jobKeys = new ArrayList<>();

    // The  Model List (positionInfo)
    private List<ModelJobPosition> list;

    public AdapterPosition(Context context , List<ModelJobPosition> list){
        this.list = list;
        this.mContext = context;
    }

    /**
     *
     * @return
     * get the list size
     */
    @Override
    public int getCount(){
        return list.size();
    }


    /**
     *
     * @param position
     * @return
     * get this position item
     */
    @Override
    public Object getItem(int position){
        return list.get(position);
    }

    /**
     *
     * @param position
     * @return itemId
     */
    @Override
    public long getItemId(int position){
        return position;
    }


    public List getDataList(){
        return list;
    }

    /**
     * Get data from job database and fill in the job
     * @param position -> position of the individual item
     * @param convertView -> view of the list view
     * @param parent -> list view layout parent
     * @return -> list view of job post that have all job from database and display it fields
     */
    @SuppressLint("ResourceType")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        AdapterPosition.ViewHolder holder = null;

        if (convertView == null){
            convertView = View.inflate(mContext, R.layout.adapter_positionsinfo,null);
            holder = new AdapterPosition.ViewHolder();
            holder.jobName = convertView.findViewById(R.id.adapter_job_name);
            holder.jobDes = convertView.findViewById(R.id.adapter_job_des);
            holder.jobDate = convertView.findViewById(R.id.adapter_job_date);
            holder.jobDuration = convertView.findViewById(R.id.adapter_job_duration);
            holder.jobUrgency = convertView.findViewById(R.id.adapter_job_urgency);
            holder.jobSalary = convertView.findViewById(R.id.adapter_job_salary);
            holder.jobLocation = convertView.findViewById(R.id.adapter_job_location);
            holder.jobEmployer = convertView.findViewById(R.id.adapter_job_employer);
            holder.suggestedEmployees = convertView.findViewById(R.id.btnRecommendedEmployees);
            holder.sendResume = convertView.findViewById(R.id.worker_sendResume);
            holder.jobKey = convertView.findViewById(R.id.adapter_job_key);

            convertView.setTag(holder);
        } else {
            holder = (AdapterPosition.ViewHolder)convertView.getTag();
        }

        ModelJobPosition modelPosition = list.get(position);
        holder.jobDes.setText("Description: " + modelPosition.getJobDes());
        holder.jobName.setText(modelPosition.getJobName());
        holder.jobDate.setText("Date: " + modelPosition.getJobDate());
        holder.jobDuration.setText("Duration: " + String.valueOf(modelPosition.getJobDuration()) + " hours");
        holder.jobUrgency.setText("Urgency: " + modelPosition.getJobUrgency());
        holder.jobSalary.setText("Salary: " + String.valueOf(modelPosition.getJobSalary()) + " CAD");
        holder.jobLocation.setText("Location: " + modelPosition.getJobLocation());
        holder.jobEmployer.setText("Employer: "+ modelPosition.getJobEmployer());
        holder.jobKey.setText(modelPosition.getJobKey());

        ViewHolder finalHolder = holder;
        /**
         * Set onclick for apply for job
         */
        holder.sendResume.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.getParent().requestDisallowInterceptTouchEvent(true);
                String username = ((WorkerActivity) mContext).getUsername();
                StringBuilder stringBuilder =new StringBuilder((String) finalHolder.jobEmployer.getText());
                String employer = stringBuilder.substring(9).trim();
                ModelResume modelResume =new ModelResume(employer,username, (String) finalHolder.jobName.getText(),"NULL", (String) finalHolder.jobKey.getText(), "sent",String.valueOf(modelPosition.getJobSalary()));
                ((WorkerActivity) mContext).submitResumeToDatabase(modelResume);
                Toast.makeText(mContext, "submit Success", Toast.LENGTH_SHORT).show();
                getDataList().remove(position);

                notifyDataSetChanged();
                String valueToRemove = finalHolder.jobKey.getText().toString();
                resumeDataBase = FirebaseDatabase.getInstance(resumeDatabaseURL);
                resumeDatabaseReference = resumeDataBase.getReference().child("jobs");
                //once used has applied for job its stored, in an arrayList so it wont display to user later.
                resumeDatabaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                            if(dataSnapshot.getKey().equals(valueToRemove)){
                                jobKeys.add(dataSnapshot.getKey());
                                break;
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


            }
        });
        holder.sendResume.setTag(position);

        /**
         * Set onclick for when further job post details when click on item
         */
        holder.suggestedEmployees.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent userDetailsIntent = new Intent(mContext, RecommendedEmployeesActivity.class);
                Gson gson = new Gson();
                String myJson = gson.toJson(getItem(position));
                userDetailsIntent.putExtra("extractJob", myJson);
                userDetailsIntent.putExtra("extractEmployer", modelPosition.getJobEmployer());
                userDetailsIntent.putExtra("PREVIOUS_ACTIVITY", WorkerActivity.class.getName());
                mContext.startActivity(userDetailsIntent);
            }
        });
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent jobDetails = new Intent(mContext, JobPostDetails.class);
                Gson gson = new Gson();
                String myJson = gson.toJson(getItem(position));
                jobDetails.putExtra("extractJob", myJson);
                mContext.startActivity(jobDetails);
            }
        });

        holder.jobDes.setText("Description: " + modelPosition.getJobDes());
        holder.jobName.setText(modelPosition.getJobName());
        holder.jobDate.setText("Date: " + modelPosition.getJobDate());
        holder.jobDuration.setText("Duration: " + modelPosition.getJobDuration() + " hours");
        holder.jobUrgency.setText("Urgency: " + modelPosition.getJobUrgency());
        holder.jobSalary.setText("Salary: " + modelPosition.getJobSalary() + " CAD");
        holder.jobLocation.setText("Location: " + modelPosition.getJobLocation());
        holder.jobEmployer.setText("Employer: "+ modelPosition.getJobEmployer());

        return convertView;
    }

    /**
     * Custom class for adapter class that have all fields
     */
    class ViewHolder {
        TextView jobName;
        TextView jobDes;
        TextView jobDate;
        TextView jobDuration;
        TextView jobUrgency;
        TextView jobSalary;
        TextView jobLocation;
        TextView jobEmployer;
        TextView jobKey;
        Button suggestedEmployees;
        Button sendResume;
    }

}
