package com.example.habitpadadmin.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.habitpadadmin.Model.Feedback;
import com.example.habitpadadmin.Model.Gift;
import com.example.habitpadadmin.R;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.List;

public class FeedbackAdapter extends RecyclerView.Adapter<FeedbackAdapter.MyViewHolder>{

    private Context mContext;
    private List<Feedback> feedbacks = new ArrayList<>();



    public FeedbackAdapter(Context context, List<Feedback> feedbacks) {
        this.mContext = context;
        this.feedbacks = feedbacks;
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView fEmail, fDetail, fDate;

        public MyViewHolder(View view) {
            super(view);

            fEmail = view.findViewById(R.id.user_email_tv);
            fDetail = view.findViewById(R.id.feedback_tv);
            fDate = view.findViewById(R.id.feeback_date_tv);

        }
    }

    @NonNull
    @Override
    public FeedbackAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.feedback_layout, parent, false);
        return new FeedbackAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FeedbackAdapter.MyViewHolder holder, int position) {

        final Feedback feedback = feedbacks.get(position);

        holder.fEmail.setText(feedback.getfEmail());
        holder.fDetail.setText(feedback.getfDetails());
        holder.fDate.setText(feedback.getfDate());


    }

    @Override
    public int getItemCount() {
        return feedbacks.size();
    }
}
