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
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class AddGiftActivity extends AppCompatActivity {

    private ImageView gPhoto;
    private MaterialButton browsePhoto, saveGift;
    private TextInputLayout gTitle,gDescription,gPoint;

    private Bitmap bitmap;
    String encodeImageString;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        setTitle("Add Gifts");
        setContentView(R.layout.activity_add_gift);

        gPhoto = findViewById(R.id.add_gift_photo);
        gTitle = findViewById(R.id.gtitle_layout);
        gDescription = findViewById(R.id.gdescription_layout);
        gPoint = findViewById(R.id.gpoint_layout);
        browsePhoto = findViewById(R.id.gbrowse_photo);
        saveGift = findViewById(R.id.save_gift);


        browsePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //if everything is ok we will open image chooser
                Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, 100);
            }
        });

        saveGift.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(!validateTitle() | !validateDesc()  |!validatePoint()){
                    return;
                }
                SaveGift();
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
                gPhoto.setImageBitmap(bitmap);
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

    private void SaveGift() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Urls.ADD_GIFT_URL, new Response.Listener<String>() {
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
                        startActivity(new Intent(AddGiftActivity.this,GiftActivity.class));


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
                params.put("giftPhoto",encodeImageString);
                params.put("giftTitle",gTitle.getEditText().getText().toString());
                params.put("giftDesc",gDescription.getEditText().getText().toString());
                params.put("giftPoint",gPoint.getEditText().getText().toString());
                return params;
            }

        };

        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
    }

    private  boolean validateTitle(){
        String val = gTitle.getEditText().getText().toString().trim();

        if(val.isEmpty()){
            gTitle.setError("Field cannot be empty");
            return false;
        } else{
            gTitle.setError(null);
            gTitle.setErrorEnabled(false);
            return true;
        }
    }

    private  boolean validateDesc(){
        String val = gDescription.getEditText().getText().toString().trim();

        if(val.isEmpty()){
            gDescription.setError("Field cannot be empty");
            return false;
        } else{
            gDescription.setError(null);
            gDescription.setErrorEnabled(false);
            return true;
        }
    }

    private  boolean validatePoint(){
        String val = gPoint.getEditText().getText().toString().trim();

        if(val.isEmpty()){
            gPoint.setError("Field cannot be empty");
            return false;
        } else{
            gPoint.setError(null);
            gPoint.setErrorEnabled(false);
            return true;
        }
    }


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    public void onBackPressed() {
        Intent intent = new Intent(AddGiftActivity.this, GiftActivity.class);
        startActivity(intent);
        finish();
    }
}