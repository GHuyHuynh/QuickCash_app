package com.example.quick_cash.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


import com.example.quick_cash.Model.ModelFeedback;
import com.example.quick_cash.R;

import org.w3c.dom.Text;

import java.util.ArrayList;

/**
 * Adapter class to link ListView to Model Feedback
 */
public class AdapterFeedbackList extends ArrayAdapter<ModelFeedback> {
    public AdapterFeedbackList(@NonNull Context context, ArrayList<ModelFeedback> feedbackList) {
        super(context, R.layout.adapter_feedback, feedbackList);
    }

    /**
     * Get data from the list and set it to the layout in the ListView
     * @param position -> current listview position
     * @param view -> Current ListView to be link
     * @param parent -> ViewGroup of the ListView
     * @return -> ListView with all data from the Feedback list
     */
    @NonNull
    @Override
    public View getView(int position, @Nullable View view, @NonNull ViewGroup parent) {
        ModelFeedback feedback = getItem(position);

        if (view == null) {
            view = LayoutInflater.from(getContext()).inflate(R.layout.adapter_feedback, parent, false);
        }

        TextView feedbackUsername = view.findViewById(R.id.feedback_username);
        TextView feedbackComment = view.findViewById(R.id.feedback_list_comment);

        feedbackUsername.setText(feedback.getUsername());
        feedbackComment.setText(feedback.getComment());

        return view;
    }
}
