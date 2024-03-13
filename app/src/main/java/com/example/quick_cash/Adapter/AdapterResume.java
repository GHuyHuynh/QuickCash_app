package com.example.quick_cash.Adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.quick_cash.MainHomepages.BossActivity;
import com.example.quick_cash.Model.ModelResume;
import com.example.quick_cash.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

/**
 * Adapter class resume (application)
 */
public class AdapterResume extends BaseAdapter {
    private Context context;
    private List<ModelResume> list;
    String resumeDatabaseURL = "https://group-1-job-database-1c791-default-rtdb.firebaseio.com/";
    FirebaseDatabase resumeDataBase;
    DatabaseReference resumeDatabaseReference;


    public AdapterResume(Context context, List<ModelResume> list){
        this.context =context;
        this.list =list;

    }
    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    public List getDataList(){
        return list;
    }

    /**
     * Fill data into resume list view
     * @param position -> current individual item
     * @param convertview -> view of the activity
     * @param viewGroup -> list view holder in layout
     * @return -> list of all resume that have been send to user
     */
    @Override
    public View getView(int position, View convertview, ViewGroup viewGroup) {
        ViewHolder holder;
        if (convertview == null){
            convertview = View.inflate(context, R.layout.adapter_resume,null);
            holder = new ViewHolder();
            holder.resumeJobName = convertview.findViewById(R.id.adapter_resume_jobName);
            holder.resumeEmployee = convertview.findViewById(R.id.adapter_resume_employee);
            holder.resumeJobKey = convertview.findViewById(R.id.adapter_resume_jobkey);
            holder.resumeKey = convertview.findViewById(R.id.adapter_resume_key);
            holder.resumePass = convertview.findViewById(R.id.resume_pass);
            holder.resumeRefuse = convertview.findViewById(R.id.resume_refuse);
            holder.resumeSalary = convertview.findViewById(R.id.adapter_resume_jobSalary);
        }else {
            holder = (ViewHolder) convertview.getTag();
        }
        ModelResume resume =list.get(position);
        holder.resumeJobName.setText("Job Name: "+resume.getJobName());
        holder.resumeEmployee.setText("Employee: "+resume.getEmployee());
        holder.resumeJobKey.setText(resume.getJobsKey());
        holder.resumeSalary.setText("Salary: "+resume.getSalary());
        holder.resumeKey.setText(resume.getKey());

        ViewHolder finalHolder = holder;
        /**
         * Set onclick on approve resume
         */
        holder.resumePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ((BossActivity)context).submitResumeFlag
                        ("Approve", finalHolder.resumeKey.getText().toString(), resume.getEmployee(), resume.getJobsKey(), resume.getSalary());
                Toast.makeText(context, "Approve Success!", Toast.LENGTH_SHORT).show();
                remove(position);

                notifyDataSetChanged();
                //code to remove job from database once approved.
                String valueToRemove = finalHolder.resumeJobKey.getText().toString();
                resumeDataBase = FirebaseDatabase.getInstance(resumeDatabaseURL);
                resumeDatabaseReference = resumeDataBase.getReference().child("jobs");

                resumeDatabaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                            if(dataSnapshot.getKey().equals(valueToRemove)){
                                DatabaseReference jobsNodeRef = snapshot.getRef();

                                jobsNodeRef.child(valueToRemove).removeValue();
                                break;
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        //When database is not reading
                    }
                });
            }
        });

        /**
         * Set onclick on refuse resume
         */
        holder.resumeRefuse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((BossActivity)context).submitResumeFlag("Refuse", finalHolder.resumeKey.getText().toString(), resume.getEmployee(), resume.getJobsKey(),resume.getSalary());
                Toast.makeText(context, "Refuse Success", Toast.LENGTH_SHORT).show();
                remove(position);
                notifyDataSetChanged();

            }
        });


        return convertview;
    }

    private void remove(int position) {
        if (position >=0 && position < list.size()){
            list.remove(position);
        }
    }

    /**
     * Custom class for adapter similar resume object
     */
    class ViewHolder {
        TextView resumeJobName;
        TextView resumeEmployee;
        TextView resumeJobKey;
        TextView resumeKey;
        Button resumePass;
        Button resumeRefuse;
        TextView resumeSalary;
    }

}
