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
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class EditTipActivity extends AppCompatActivity {

    private ImageView uploadPhoto;
    private MaterialButton browse, saveTip;
    private TextInputEditText tipDetails;
    private Bitmap bitmap;
    String encodeImageString;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        setTitle("Edit Tip");
        setContentView(R.layout.activity_edit_tip);


        // Getting UI views from our xml file
        uploadPhoto = findViewById(R.id.edit_tip_photo);
        browse = findViewById(R.id.edit_browsePhoto);
        saveTip = findViewById(R.id.edit_savePhoto);
        tipDetails = findViewById(R.id.edit_tip_input);


        Bundle bundle = getIntent().getExtras();
        String mID = bundle.getString("tipID");
        String mDetails = bundle.getString("tipDetails");
        String mPhoto = bundle.getString("tipPhoto");

        Glide.with(this).load(mPhoto).into(uploadPhoto);
        tipDetails.setText(mDetails);

        encodeImageString = mPhoto;

        browse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //if everything is ok we will open image chooser
                Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, 100);
            }
        });

        saveTip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editTip(mID);
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
                InputStream inputStream=getContentResolver().openInputStream(imageUri);
                bitmap= BitmapFactory.decodeStream(inputStream);
                uploadPhoto.setImageBitmap(bitmap);
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


    private void editTip(final String tipID){

        tipDetails=(TextInputEditText) findViewById(R.id.edit_tip_input);
        final String tipDet=tipDetails.getText().toString().trim();


        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM YYYY, h:mm a");
        final String tipDateTime = sdf.format(new Date());


        StringRequest stringRequest = new StringRequest(Request.Method.POST, Urls.EDIT_TIP_URL, new Response.Listener<String>()  {
            @Override
            public void onResponse(String response) {

                try {
                    Log.i("tagconvertstr", "["+response+"]");
                    JSONObject jsonObject = new JSONObject(response);

                    String success = jsonObject.getString("success");
                    String message = jsonObject.getString("message");

                    if (success.equals("1")) {
                        Toast.makeText(getApplicationContext(),message,Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(EditTipActivity.this,AdminTipsActivity.class));
                    }

                }catch (Exception e){
                    //e.printStackTrace();
                    Toast.makeText(getApplicationContext(),"Edit Error!",Toast.LENGTH_LONG).show();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(EditTipActivity.this, error.toString(),Toast.LENGTH_LONG).show();

            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError
            {
                Map<String, String> params = new HashMap<>();
                params.put("tipID",tipID);
                params.put("tipDetails",tipDet);
                params.put("tipPhoto",encodeImageString);
                params.put("tipDateTime",tipDateTime);
                return params;
            }

        };

        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    public void onBackPressed() {
        Intent intent = new Intent(EditTipActivity.this, AdminTipsActivity.class);
        startActivity(intent);
        finish();
    }
}