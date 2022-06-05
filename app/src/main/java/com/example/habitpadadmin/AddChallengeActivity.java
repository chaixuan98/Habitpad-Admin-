package com.example.habitpadadmin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class AddChallengeActivity extends AppCompatActivity {

    private ImageView challengePhoto;
    private MaterialButton browsePhoto, saveChallenge;
    private EditText startDate, endDate;
    private TextInputLayout title,description,stepRequired;

    private Bitmap bitmap;
    String encodeImageString;
    private DatePickerDialog.OnDateSetListener setListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        setTitle("Add Challenges");
        setContentView(R.layout.activity_add_challenge);

        challengePhoto = findViewById(R.id.add_challenge_photo);
        browsePhoto = findViewById(R.id.browse_photo);
        saveChallenge = findViewById(R.id.save_challenge);
        startDate = findViewById(R.id.start_date);
        endDate = findViewById(R.id.end_date);
        title = findViewById(R.id.title_layout);
        description = findViewById(R.id.description_layout);
        stepRequired = findViewById(R.id.step_layout);

        Calendar calendar = Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                    Uri.parse("package:" + getPackageName()));
            finish();
            startActivity(intent);
            return;
        }

        startDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(AddChallengeActivity.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,setListener,
                        year, month, day);
                datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                datePickerDialog.show();
            }
        });

        startDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(AddChallengeActivity.this, new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int day) {

                        month = month + 1;
                        String date = day + "-" + month + "-" + year;
                        startDate.setText(DateHandler.dateFormat1(date));
                        //getDoctorsSlot(bookDate.getText().toString().trim());
                    }
                },year,month,day);
                datePickerDialog.show();
            }
        });

        endDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(AddChallengeActivity.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,setListener,
                        year, month, day);
                datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                datePickerDialog.show();
            }
        });

        endDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(AddChallengeActivity.this, new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int day) {

                        month = month + 1;
                        String date = day + "-" + month + "-" + year;
                        endDate.setText(DateHandler.dateFormat1(date));
                        //getDoctorsSlot(bookDate.getText().toString().trim());
                    }
                },year,month,day);
                datePickerDialog.show();
            }
        });



        browsePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //if everything is ok we will open image chooser
                Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, 100);
            }
        });

        saveChallenge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(!validateTitle() | !validateDesc() | !validateStartDate() | !validateEndDate() |!validateStep()){
                    return;
                }
                SaveChallenge();
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
                challengePhoto.setImageBitmap(bitmap);
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

    private void SaveChallenge() {

//        tipDetails=(TextInputEditText) findViewById(R.id.tip_input);
//        final String tipDet=tipDetails.getText().toString().trim();


        StringRequest stringRequest = new StringRequest(Request.Method.POST, Urls.ADD_CHALLENGE_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response)
            {

                try {
                    Log.i("tagconvertstr", "["+response+"]");
                    JSONObject jsonObject = new JSONObject(response);

                    String success = jsonObject.getString("success");
                    String message = jsonObject.getString("message");

                    if (success.equals("1")) {

                        //tipDetails.setText("");
                        //challengePhoto.setImageResource(R.drawable.ic_launcher_foreground);
                        Toast.makeText(getApplicationContext(),message,Toast.LENGTH_SHORT).show();

                    }

                } catch (JSONException e) {

                    Toast.makeText(getApplicationContext(),"Save Error!" + e.toString(),Toast.LENGTH_LONG).show();

                }
                //tipDetails.setText("");
                //challengePhoto.setImageResource(R.drawable.ic_launcher_foreground);
                //Toast.makeText(getApplicationContext(),response.toString(),Toast.LENGTH_LONG).show();
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
                params.put("challengePhoto",encodeImageString);
                params.put("challengeTitle",title.getEditText().getText().toString());
                params.put("challengeDescription",description.getEditText().getText().toString());
                params.put("challengeStartDate",startDate.getText().toString());
                params.put("challengeEndDate",endDate.getText().toString());
                params.put("challengeStep",stepRequired.getEditText().getText().toString());
                return params;
            }

        };

        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
    }


    private  boolean validateTitle(){
        String val = title.getEditText().getText().toString().trim();

        if(val.isEmpty()){
            title.setError("Field cannot be empty");
            return false;
        } else{
            title.setError(null);
            title.setErrorEnabled(false);
            return true;
        }
    }

    private  boolean validateDesc(){
        String val = description.getEditText().getText().toString().trim();

        if(val.isEmpty()){
            description.setError("Field cannot be empty");
            return false;
        } else{
            description.setError(null);
            description.setErrorEnabled(false);
            return true;
        }
    }

    private  boolean validateStartDate(){
        String val = startDate.getText().toString().trim();

        if(val.isEmpty()){
            startDate.setError("Field cannot be empty");
            return false;
        } else{
            startDate.setError(null);
            // birth.setErrorEnabled(false);
            return true;
        }
    }

    private  boolean validateEndDate(){
        String val = endDate.getText().toString().trim();

        if(val.isEmpty()){
            endDate.setError("Field cannot be empty");
            return false;
        } else{
            endDate.setError(null);
            // birth.setErrorEnabled(false);
            return true;
        }
    }

    private  boolean validateStep(){
        String val = stepRequired.getEditText().getText().toString().trim();

        if(val.isEmpty()){
            stepRequired.setError("Field cannot be empty");
            return false;
        } else{
            stepRequired.setError(null);
            stepRequired.setErrorEnabled(false);
            return true;
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    public void onBackPressed() {
        Intent intent = new Intent(AddChallengeActivity.this, ChallengesActivity.class);
        startActivity(intent);
        finish();
    }

}