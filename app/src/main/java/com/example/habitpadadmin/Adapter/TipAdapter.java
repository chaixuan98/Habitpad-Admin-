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
import com.example.habitpadadmin.EditTipActivity;
import com.example.habitpadadmin.Model.Tip;
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

public class TipAdapter extends RecyclerView.Adapter<TipAdapter.MyViewHolder> {

    private Context mContext;
    private List<Tip> tips = new ArrayList<>();
    String encodeImageString;


    public TipAdapter(Context context, List<Tip> tips) {
        this.mContext = context;
        this.tips = tips;
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView tipDetails, tipDateTime;
        private ImageView tipPhoto;
        private MaterialButton editTip, deleteTip;

        public MyViewHolder(View view) {
            super(view);

            tipDetails = view.findViewById(R.id.desc_tv);
            tipPhoto = view.findViewById(R.id.tipimageview);
            tipDateTime = view.findViewById(R.id.dt_view);
            editTip = view.findViewById(R.id.edit_btn);
            deleteTip = view.findViewById(R.id.delete_btn);
        }
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.tip_layout, parent, false);
        return new MyViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, @SuppressLint("RecyclerView") int position) {

        final Tip tip = tips.get(position);

        holder.tipDetails.setText(tip.getTipDetails());
        Glide.with(mContext).asBitmap().load(tip.getTipPhoto())
                .fitCenter()
                .placeholder(R.drawable.ic_baseline_image_24)
                .error(R.drawable.ic_baseline_broken_image_24)
                .fallback(R.drawable.ic_baseline_image_not_supported_24)
                .dontAnimate().into(holder.tipPhoto);

        holder.tipDateTime.setText(tip.getTipDateTime());
        holder.editTip.setOnClickListener((View view) -> {

            Intent intent = new Intent(mContext, EditTipActivity.class);

            Bundle bundle = new Bundle();
            bundle.putString("tipID", tip.getTipID());
            bundle.putString("tipDetails", tip.getTipDetails());
            bundle.putString("tipPhoto", tip.getTipPhoto());
            bundle.putString("tipDateTime", tip.getTipDateTime());

            intent.putExtras(bundle);

            mContext.startActivity(intent);

        });

        holder.deleteTip.setOnClickListener((View v) -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
            builder.setTitle("Delete Tip");
            builder.setMessage("Conform to Delete");
            builder.setNegativeButton("CANCEL", (dialog, i) -> dialog.dismiss());
            builder.setPositiveButton("DELETE", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int i) {

                    StringRequest stringRequest = new StringRequest(Request.Method.POST, Urls.DELETE_TIP_URL,
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
                            deleteParams.put("tipID", tip.getTipID());

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
        return tips.size();
    }

    public void Delete(int item) {
        tips.remove(item);
        notifyItemRemoved(item);
        //notifyDataSetChanged();
    }


}
