package com.example.quick_cash.Adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.quick_cash.Notification.NotificationActivity;
import com.example.quick_cash.Model.ModelResume;
import com.example.quick_cash.R;

import java.util.List;

/**
 * Adapter class for resume result
 * Use to display the user application for a job result
 */
public class AdapterResumeResult extends BaseAdapter {
    private Context context;
    private List<ModelResume> list;

    /**
     * Constructor
     * @param context -> Current app activity
     * @param list -> List of all resume (application result)
     */
    public AdapterResumeResult(Context context, List<ModelResume> list){
        this.context =context;
        this.list =list;

    }

    /**
     * Get the count of all item
     * @return -> number of total item
     */
    @Override
    public int getCount() {
        return list.size();
    }

    /**
     * Get the current item on the list with onclick
     * @param i -> index of the item
     * @return -> the object in the list (Model_Resume_Result)
     */
    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    /**
     * Get the list id of the on click item
     * @param i -> current item index
     * @return -> id of the index (long data type)
     */
    @Override
    public long getItemId(int i) {
        return i;
    }

    /**
     * Get the list from the adapter class
     * @return -> ModelResume list that connect with this adapter
     */
    public List<ModelResume> getDataList(){
        return list;
    }

    /**
     * Link the list to it layout list view
     * See all data into list
     * @param position -> list position
     * @param convertview -> ListView in layout file
     * @param viewGroup -> Group of the ListView
     * @return -> List with all data
     */
    @Override
    public View getView(int position, View convertview, ViewGroup viewGroup) {
        ViewHolder holder = null;
        if (convertview == null){
            convertview = View.inflate(context, R.layout.adapter_resumeresult,null);
           holder = new ViewHolder();

            holder.Result_jobName= convertview.findViewById(R.id.resumeResult_jobName);
            holder.Result_employer = convertview.findViewById(R.id.resumeResult_employer);
            holder.Result_jobKey = convertview.findViewById(R.id.resumeResult_jobkey);
            holder.Result_key = convertview.findViewById(R.id.resumeResult_key);
            holder.Result_flag = convertview.findViewById(R.id.resumeResult_flag);
            holder.resumeResult_ignore = convertview.findViewById(R.id.resumeResult_ignore);
            holder.resumeResult_read = convertview.findViewById(R.id.resumeResult_read);

            convertview.setTag(holder);
        } else {
            holder = (ViewHolder) convertview.getTag();
        }


        ModelResume resume =list.get(position);

        holder.Result_jobName.setText("Job Name: "+resume.getJobName());
        holder.Result_employer.setText("Employee: "+resume.getEmployer());
        holder.Result_jobKey.setText(resume.getJobsKey());
        holder.Result_key.setText(resume.getKey());
        holder.Result_flag.setText("Status: "+resume.getFlag());


        ViewHolder finalHolder = holder;

        /**
         * Set onclick when ignore the result
         */
        holder.resumeResult_ignore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ((NotificationActivity)context).submitResumeStatus("Ignore", finalHolder.Result_key.getText().toString());
                Toast.makeText(context, "Ignore Success", Toast.LENGTH_SHORT).show();
                remove(position);
                notifyDataSetChanged();
            }
        });

        /**
         * Set onclick when read the result
         */
        holder.resumeResult_read.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((NotificationActivity)context).submitResumeStatus("Read", finalHolder.Result_key.getText().toString());
                Toast.makeText(context, "Read Success", Toast.LENGTH_SHORT).show();
                remove(position);
                notifyDataSetChanged();

            }
        });

        return convertview;
    }

    /**
     * Remove the data from list
     * @param position -> index to be remove
     */
    private void remove(int position) {
        if (position >=0 && position < list.size()){
            list.remove(position);
        }
    }

    /**
     * Custom class to holder all resume result fields
     */
    class ViewHolder {
        TextView Result_jobName,Result_employer,Result_jobKey,Result_key,Result_flag;
        Button resumeResult_ignore,resumeResult_read;
    }

}
