package com.example.habitpadadmin.Adapter;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.example.habitpadadmin.EditChallengeActivity;
import com.example.habitpadadmin.Model.Challenge;
import com.example.habitpadadmin.R;
import com.example.habitpadadmin.Urls;
import com.example.habitpadadmin.VolleySingleton;
import com.google.android.material.button.MaterialButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChallengeAdapter extends RecyclerView.Adapter<ChallengeAdapter.MyViewHolder> {

    private Context mContext;
    private List<Challenge> challenges = new ArrayList<>();
    String encodeImageString;


    public ChallengeAdapter(Context context, List<Challenge> challenges) {
        this.mContext = context;
        this.challenges = challenges;
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView challengeTitle, challengeDesc, challengeStartDate, challengeEndDate, challengeStep;
        private ImageView challengePhoto;
        private MaterialButton editChallenge, deleteChallenge;

        public MyViewHolder(View view) {
            super(view);


            challengePhoto = view.findViewById(R.id.challenge_image);
            challengeTitle = view.findViewById(R.id.challenge_title_tv);
            challengeDesc = view.findViewById(R.id.challenge_desc_tv);
            challengeStartDate = view.findViewById(R.id.challenge_start_date_tv);
            challengeEndDate = view.findViewById(R.id.challenge_end_date_tv);
            challengeStep = view.findViewById(R.id.challenge_step_tv);
            editChallenge = view.findViewById(R.id.edit_challenge_btn);
            deleteChallenge = view.findViewById(R.id.delete_challenge_btn);
        }
    }



    @NonNull
    @Override
    public ChallengeAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.challenge_layout, parent, false);
        return new ChallengeAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChallengeAdapter.MyViewHolder holder, @SuppressLint("RecyclerView") int position) {

        final Challenge challenge = challenges.get(position);


        Glide.with(mContext).asBitmap().load(challenge.getChallengeImage())
                .fitCenter()
                .placeholder(R.drawable.ic_baseline_image_24)
                .error(R.drawable.ic_baseline_broken_image_24)
                .fallback(R.drawable.ic_baseline_image_not_supported_24)
                .dontAnimate().into(holder.challengePhoto);

        holder.challengeTitle.setText(challenge.getChallengeTitle());
        holder.challengeDesc.setText(challenge.getChallengeDesc());
        holder.challengeStartDate.setText(challenge.getChallengeStartDate());
        holder.challengeEndDate.setText(challenge.getChallengeEndDate());
        holder.challengeStep.setText(challenge.getChallengeStep());

        holder.editChallenge.setOnClickListener((View view) -> {

            Intent intent = new Intent(mContext, EditChallengeActivity.class);

            Bundle bundle = new Bundle();
            bundle.putString("challengeID", challenge.getChallengeID());
            bundle.putString("challengePhoto",challenge.getChallengeImage());
            bundle.putString("challengeTitle",challenge.getChallengeTitle());
            bundle.putString("challengeDesc", challenge.getChallengeDesc());
            bundle.putString("challengeStartDate", challenge.getChallengeStartDate());
            bundle.putString("challengeEndDate", challenge.getChallengeEndDate());
            bundle.putString("challengeStep",challenge.getChallengeStep());

            intent.putExtras(bundle);

            mContext.startActivity(intent);

        });

        holder.deleteChallenge.setOnClickListener((View v) -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
            builder.setTitle("Delete Challenge");
            builder.setMessage("Conform to Delete");
            builder.setNegativeButton("CANCEL", (dialog, i) -> dialog.dismiss());
            builder.setPositiveButton("DELETE", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int i) {

                    StringRequest stringRequest = new StringRequest(Request.Method.POST, Urls.DELETE_CHALLENGE_URL,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    try {
                                        Log.i("tagconvertstr", "[" + response + "]");
                                        JSONObject jsonObject = new JSONObject(response);
                                        String check = jsonObject.getString("success");
                                        if (check.equals("1")) {
                                            Delete(position);
                                            Toast.makeText(mContext, "Delete Sucessfully", Toast.LENGTH_LONG).show();

                                        } else {
                                            Toast.makeText(mContext, response, Toast.LENGTH_LONG).show();
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(mContext, error.toString(), Toast.LENGTH_LONG).show();
                        }
                    }) {
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String, String> deleteParams = new HashMap<>();
                            deleteParams.put("challengeID", challenge.getChallengeID());

                            return deleteParams;
                        }
                    };
                    VolleySingleton.getInstance(mContext).addToRequestQueue(stringRequest);

                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();

        });


    }

    @Override
    public int getItemCount() {
        return challenges.size();
    }

    public void Delete(int item) {
        challenges.remove(item);
        notifyItemRemoved(item);
        //notifyDataSetChanged();
    }
}
