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
import com.example.habitpadadmin.EditGiftActivity;
import com.example.habitpadadmin.EditVoucherActivity;
import com.example.habitpadadmin.Model.Gift;
import com.example.habitpadadmin.Model.Voucher;
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

public class GiftAdapter extends RecyclerView.Adapter<GiftAdapter.MyViewHolder>{

    private Context mContext;
    private List<Gift> gifts = new ArrayList<>();
    String encodeImageString;


    public GiftAdapter(Context context, List<Gift> gifts) {
        this.mContext = context;
        this.gifts = gifts;
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView giftTitle, giftDesc, giftPoint;
        private ImageView giftPhoto;
        private MaterialButton editGift, deleteGift;

        public MyViewHolder(View view) {
            super(view);


            giftPhoto = view.findViewById(R.id.gift_image);
            giftTitle = view.findViewById(R.id.gift_title_tv);
            giftDesc = view.findViewById(R.id.gift_desc_tv);
            giftPoint = view.findViewById(R.id.gift_point_tv);
            editGift = view.findViewById(R.id.edit_gift_btn);
            deleteGift = view.findViewById(R.id.delete_gift_btn);
        }
    }


    @NonNull
    @Override
    public GiftAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.gift_layout, parent, false);
        return new GiftAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GiftAdapter.MyViewHolder holder, @SuppressLint("RecyclerView") int position) {

        final Gift gift = gifts.get(position);


        Glide.with(mContext).asBitmap().load(gift.getGimage())
                .fitCenter()
                .placeholder(R.drawable.ic_baseline_image_24)
                .error(R.drawable.ic_baseline_broken_image_24)
                .fallback(R.drawable.ic_baseline_image_not_supported_24)
                .dontAnimate().into(holder.giftPhoto);

        holder.giftTitle.setText(gift.getGtitle());
        holder.giftDesc.setText(gift.getGdesc());
        holder.giftPoint.setText(gift.getGpoint());

        holder.editGift.setOnClickListener((View view) -> {

            Intent intent = new Intent(mContext, EditGiftActivity.class);

            Bundle bundle = new Bundle();
            bundle.putString("giftID", gift.getGiftID());
            bundle.putString("giftPhoto",gift.getGimage());
            bundle.putString("giftTitle",gift.getGtitle());
            bundle.putString("giftDesc", gift.getGdesc());
            bundle.putString("giftPoint", gift.getGpoint());


            intent.putExtras(bundle);

            mContext.startActivity(intent);

        });

        holder.deleteGift.setOnClickListener((View v) -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
            builder.setTitle("Delete Gift");
            builder.setMessage("Conform to Delete");
            builder.setNegativeButton("CANCEL", (dialog, i) -> dialog.dismiss());
            builder.setPositiveButton("DELETE", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int i) {

                    StringRequest stringRequest = new StringRequest(Request.Method.POST, Urls.DELETE_GIFT_URL,
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
                            deleteParams.put("giftID", gift.getGiftID());

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
        return gifts.size();
    }

    public void Delete(int item) {
        gifts.remove(item);
        notifyItemRemoved(item);

    }
}
