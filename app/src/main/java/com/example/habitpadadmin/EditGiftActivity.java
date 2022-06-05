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

public class EditGiftActivity extends AppCompatActivity {

    private ImageView editGPhoto;
    private MaterialButton editBrowsePhoto, editSaveGift;
    private TextInputLayout editGTitle,editGDescription,editGPoint;

    private Bitmap bitmap;
    String encodeImageString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        setTitle("Edit Gift");
        setContentView(R.layout.activity_edit_gift);

        editGPhoto = findViewById(R.id.edit_gift_photo);
        editGTitle = findViewById(R.id.edit_gtitle_layout);
        editGDescription = findViewById(R.id.edit_gdescription_layout);
        editGPoint = findViewById(R.id.edit_gpoint_layout);

        editBrowsePhoto = findViewById(R.id.edit_gbrowse_photo);
        editSaveGift = findViewById(R.id.edit_save_gift);

        Bundle bundle = getIntent().getExtras();
        String gID = bundle.getString("giftID");
        String gPhoto = bundle.getString("giftPhoto");
        String gTitle = bundle.getString("giftTitle");
        String gDesc = bundle.getString("giftDesc");
        String gPoint = bundle.getString("giftPoint");

        Glide.with(this).load(gPhoto).into(editGPhoto);
        editGTitle.getEditText().setText(gTitle);
        editGDescription.getEditText().setText(gDesc);
        editGPoint.getEditText().setText(gPoint);

        encodeImageString = gPhoto;

        editBrowsePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //if everything is ok we will open image chooser
                Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, 100);
            }
        });

        editSaveGift.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(!validateTitle() | !validateDesc()  |!validatePoint()){
                    return;
                }
                SaveGift(gID);
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
                editGPhoto.setImageBitmap(bitmap);
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

    private void SaveGift(String giftID) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Urls.EDIT_GIFT_URL, new Response.Listener<String>() {
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
                        startActivity(new Intent(EditGiftActivity.this,GiftActivity.class));


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
                params.put("giftID",giftID);
                params.put("giftPhoto",encodeImageString);
                params.put("giftTitle",editGTitle.getEditText().getText().toString());
                params.put("giftDesc",editGDescription.getEditText().getText().toString());
                params.put("giftPoint",editGPoint.getEditText().getText().toString());
                return params;
            }

        };

        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
    }

    private  boolean validateTitle(){
        String val = editGTitle.getEditText().getText().toString().trim();

        if(val.isEmpty()){
            editGTitle.setError("Field cannot be empty");
            return false;
        } else{
            editGTitle.setError(null);
            editGTitle.setErrorEnabled(false);
            return true;
        }
    }

    private  boolean validateDesc(){
        String val = editGDescription.getEditText().getText().toString().trim();

        if(val.isEmpty()){
            editGDescription.setError("Field cannot be empty");
            return false;
        } else{
            editGDescription.setError(null);
            editGDescription.setErrorEnabled(false);
            return true;
        }
    }

    private  boolean validatePoint(){
        String val = editGPoint.getEditText().getText().toString().trim();

        if(val.isEmpty()){
            editGPoint.setError("Field cannot be empty");
            return false;
        } else{
            editGPoint.setError(null);
            editGPoint.setErrorEnabled(false);
            return true;
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    public void onBackPressed() {
        Intent intent = new Intent(EditGiftActivity.this, GiftActivity.class);
        startActivity(intent);
        finish();
    }

}