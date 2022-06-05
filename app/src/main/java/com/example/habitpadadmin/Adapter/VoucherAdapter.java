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
import com.example.habitpadadmin.EditVoucherActivity;
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

public class VoucherAdapter extends RecyclerView.Adapter<VoucherAdapter.MyViewHolder> {


    private Context mContext;
    private List<Voucher> vouchers = new ArrayList<>();
    String encodeImageString;


    public VoucherAdapter(Context context, List<Voucher> vouchers) {
        this.mContext = context;
        this.vouchers = vouchers;
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView voucherTitle, voucherDesc, voucherPoint;
        private ImageView voucherPhoto;
        private MaterialButton editVoucher, deleteVoucher;

        public MyViewHolder(View view) {
            super(view);


            voucherPhoto = view.findViewById(R.id.voucher_image);
            voucherTitle = view.findViewById(R.id.voucher_title_tv);
            voucherDesc = view.findViewById(R.id.voucher_desc_tv);
            voucherPoint = view.findViewById(R.id.voucher_point_tv);
            editVoucher = view.findViewById(R.id.edit_voucher_btn);
            deleteVoucher = view.findViewById(R.id.delete_voucher_btn);
        }
    }


    @NonNull
    @Override
    public VoucherAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.voucher_layout, parent, false);
        return new VoucherAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VoucherAdapter.MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        final Voucher voucher = vouchers.get(position);


        Glide.with(mContext).asBitmap().load(voucher.getImage())
                .fitCenter()
                .placeholder(R.drawable.ic_baseline_image_24)
                .error(R.drawable.ic_baseline_broken_image_24)
                .fallback(R.drawable.ic_baseline_image_not_supported_24)
                .dontAnimate().into(holder.voucherPhoto);

        holder.voucherTitle.setText(voucher.getTitle());
        holder.voucherDesc.setText(voucher.getDesc());
        holder.voucherPoint.setText(voucher.getPoint());

        holder.editVoucher.setOnClickListener((View view) -> {

            Intent intent = new Intent(mContext, EditVoucherActivity.class);

            Bundle bundle = new Bundle();
            bundle.putString("voucherID", voucher.getVoucherID());
            bundle.putString("voucherPhoto",voucher.getImage());
            bundle.putString("voucherTitle",voucher.getTitle());
            bundle.putString("voucherDesc", voucher.getDesc());
            bundle.putString("voucherPoint", voucher.getPoint());


            intent.putExtras(bundle);

            mContext.startActivity(intent);

        });

        holder.deleteVoucher.setOnClickListener((View v) -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
            builder.setTitle("Delete Voucher");
            builder.setMessage("Conform to Delete");
            builder.setNegativeButton("CANCEL", (dialog, i) -> dialog.dismiss());
            builder.setPositiveButton("DELETE", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int i) {

                    StringRequest stringRequest = new StringRequest(Request.Method.POST, Urls.DELETE_VOUCHER_URL,
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
                            deleteParams.put("voucherID", voucher.getVoucherID());

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
        return vouchers.size();
    }

    public void Delete(int item) {
        vouchers.remove(item);
        notifyItemRemoved(item);

    }
}
