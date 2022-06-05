package com.example.habitpadadmin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class EditVoucherActivity extends AppCompatActivity {

    private ImageView editVPhoto;
    private MaterialButton editBrowsePhoto, editSaveVoucher;
    private TextInputLayout editVTitle,editVDescription,editVPoint;

    private Bitmap bitmap;
    String encodeImageString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        setTitle("Edit Voucher");
        setContentView(R.layout.activity_edit_voucher);

        editVPhoto = findViewById(R.id.edit_voucher_photo);
        editVTitle = findViewById(R.id.edit_vtitle_layout);
        editVDescription = findViewById(R.id.edit_vdescription_layout);
        editVPoint = findViewById(R.id.edit_vpoint_layout);

        editBrowsePhoto = findViewById(R.id.edit_vbrowse_photo);
        editSaveVoucher = findViewById(R.id.edit_save_voucher);

        Bundle bundle = getIntent().getExtras();
        String vID = bundle.getString("voucherID");
        String vPhoto = bundle.getString("voucherPhoto");
        String vTitle = bundle.getString("voucherTitle");
        String vDesc = bundle.getString("voucherDesc");
        String vPoint = bundle.getString("voucherPoint");

        Glide.with(this).load(vPhoto).into(editVPhoto);
        editVTitle.getEditText().setText(vTitle);
        editVDescription.getEditText().setText(vDesc);
        editVPoint.getEditText().setText(vPoint);

        encodeImageString = vPhoto;

        editBrowsePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //if everything is ok we will open image chooser
                Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, 100);
            }
        });

        editSaveVoucher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(!validateTitle() | !validateDesc()  |!validatePoint()){
                    return;
                }
                SaveVoucher(vID);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == RESULT_OK && data != null) {

            //getting the image Uri
            Uri imageUri = data.getData();
            try {
                //getting bitmap object from uri
                //Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
                InputStream inputStream=getContentResolver().openInputStream(imageUri);
                bitmap= BitmapFactory.decodeStream(inputStream);
                editVPhoto.setImageBitmap(bitmap);
                encodeBitmapImage(bitmap);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void encodeBitmapImage(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream=new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,byteArrayOutputStream);
        byte[] bytesofimage=byteArrayOutputStream.toByteArray();
        encodeImageString=android.util.Base64.encodeToString(bytesofimage, Base64.DEFAULT);
    }

    private void SaveVoucher(String voucherID) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Urls.EDIT_VOUCHER_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response)
            {

                try {
                    Log.i("tagconvertstr", "["+response+"]");
                    JSONObject jsonObject = new JSONObject(response);

                    String success = jsonObject.getString("success");
                    String message = jsonObject.getString("message");

                    if (success.equals("1")) {

                        Toast.makeText(getApplicationContext(),message,Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(EditVoucherActivity.this,VoucherActivity.class));


                    }

                } catch (JSONException e) {

                    Toast.makeText(getApplicationContext(),"Save Error!" + e.toString(),Toast.LENGTH_LONG).show();

                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),error.toString(),Toast.LENGTH_LONG).show();
            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError
            {
                Map<String, String> params = new HashMap<>();
                params.put("voucherID",voucherID);
                params.put("voucherPhoto",encodeImageString);
                params.put("voucherTitle",editVTitle.getEditText().getText().toString());
                params.put("voucherDesc",editVDescription.getEditText().getText().toString());
                params.put("voucherPoint",editVPoint.getEditText().getText().toString());
                return params;
            }

        };

        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
    }

    private  boolean validateTitle(){
        String val = editVTitle.getEditText().getText().toString().trim();

        if(val.isEmpty()){
            editVTitle.setError("Field cannot be empty");
            return false;
        } else{
            editVTitle.setError(null);
            editVTitle.setErrorEnabled(false);
            return true;
        }
    }

    private  boolean validateDesc(){
        String val = editVDescription.getEditText().getText().toString().trim();

        if(val.isEmpty()){
            editVDescription.setError("Field cannot be empty");
            return false;
        } else{
            editVDescription.setError(null);
            editVDescription.setErrorEnabled(false);
            return true;
        }
    }

    private  boolean validatePoint(){
        String val = editVPoint.getEditText().getText().toString().trim();

        if(val.isEmpty()){
            editVPoint.setError("Field cannot be empty");
            return false;
        } else{
            editVPoint.setError(null);
            editVPoint.setErrorEnabled(false);
            return true;
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    public void onBackPressed() {
        Intent intent = new Intent(EditVoucherActivity.this, VoucherActivity.class);
        startActivity(intent);
        finish();
    }

}