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
import com.bumptech.glide.Glide;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class EditChallengeActivity extends AppCompatActivity {

    private ImageView editChallengePhoto;
    private MaterialButton editBrowsePhoto, editSaveChallenge;
    private EditText editStartDate, editEndDate;
    private TextInputLayout editTitle,editDescription,editStepRequired;

    private Bitmap bitmap;
    String encodeImageString;
    private DatePickerDialog.OnDateSetListener setListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        setTitle("Edit Challenge");
        setContentView(R.layout.activity_edit_challenge);

        editChallengePhoto = findViewById(R.id.edit_challenge_photo);
        editBrowsePhoto = findViewById(R.id.edit_browse_photo);
        editSaveChallenge = findViewById(R.id.edit_save_challenge);
        editStartDate = findViewById(R.id.edit_start_date);
        editEndDate = findViewById(R.id.edit_end_date);
        editTitle = findViewById(R.id.edit_title_layout);
        editDescription = findViewById(R.id.edit_description_layout);
        editStepRequired = findViewById(R.id.edit_step_layout);

        Bundle bundle = getIntent().getExtras();
        String cID = bundle.getString("challengeID");
        String cPhoto = bundle.getString("challengePhoto");
        String cTitle = bundle.getString("challengeTitle");
        String cDesc = bundle.getString("challengeDesc");
        String cStartDate = bundle.getString("challengeStartDate");
        String cEndDate = bundle.getString("challengeEndDate");
        String cStep = bundle.getString("challengeStep");

        Glide.with(this).load(cPhoto).into(editChallengePhoto);
        editStartDate.setText(cStartDate);
        editEndDate.setText(cEndDate);
        editTitle.getEditText().setText(cTitle);
        editDescription.getEditText().setText(cDesc);
        editStepRequired.getEditText().setText(cStep);

        encodeImageString = cPhoto;


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

        editStartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(EditChallengeActivity.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,setListener,
                        year, month, day);
                datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                datePickerDialog.show();
            }
        });

        editStartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(EditChallengeActivity.this, new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int day) {

                        month = month + 1;
                        String date = day + "-" + month + "-" + year;
                        editStartDate.setText(DateHandler.dateFormat1(date));
                        //getDoctorsSlot(bookDate.getText().toString().trim());
                    }
                },year,month,day);
                datePickerDialog.show();
            }
        });

        editEndDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(EditChallengeActivity.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,setListener,
                        year, month, day);
                datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                datePickerDialog.show();
            }
        });

        editEndDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(EditChallengeActivity.this, new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int day) {

                        month = month + 1;
                        String date = day + "-" + month + "-" + year;
                        editEndDate.setText(DateHandler.dateFormat1(date));
                        //getDoctorsSlot(bookDate.getText().toString().trim());
                    }
                },year,month,day);
                datePickerDialog.show();
            }
        });



        editBrowsePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //if everything is ok we will open image chooser
                Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, 100);
            }
        });

        editSaveChallenge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(!validateTitle() | !validateDesc() | !validateStartDate() | !validateEndDate() |!validateStep()){
                    return;
                }
                SaveChallenge(cID);
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
                editChallengePhoto.setImageBitmap(bitmap);
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

    private void SaveChallenge(final String challengeID) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Urls.EDIT_CHALLENGE_URL, new Response.Listener<String>() {
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
                        startActivity(new Intent(EditChallengeActivity.this,ChallengesActivity.class));

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
                params.put("challengeID",challengeID);
                params.put("challengePhoto",encodeImageString);
                params.put("challengeTitle",editTitle.getEditText().getText().toString());
                params.put("challengeDescription",editDescription.getEditText().getText().toString());
                params.put("challengeStartDate",editStartDate.getText().toString());
                params.put("challengeEndDate",editEndDate.getText().toString());
                params.put("challengeStep",editStepRequired.getEditText().getText().toString());
                return params;
            }

        };

        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
    }


    private  boolean validateTitle(){
        String val = editTitle.getEditText().getText().toString().trim();

        if(val.isEmpty()){
            editTitle.setError("Field cannot be empty");
            return false;
        } else{
            editTitle.setError(null);
            editTitle.setErrorEnabled(false);
            return true;
        }
    }

    private  boolean validateDesc(){
        String val = editDescription.getEditText().getText().toString().trim();

        if(val.isEmpty()){
            editDescription.setError("Field cannot be empty");
            return false;
        } else{
            editDescription.setError(null);
            editDescription.setErrorEnabled(false);
            return true;
        }
    }

    private  boolean validateStartDate(){
        String val = editStartDate.getText().toString().trim();

        if(val.isEmpty()){
            editStartDate.setError("Field cannot be empty");
            return false;
        } else{
            editStartDate.setError(null);
            // birth.setErrorEnabled(false);
            return true;
        }
    }

    private  boolean validateEndDate(){
        String val = editEndDate.getText().toString().trim();

        if(val.isEmpty()){
            editEndDate.setError("Field cannot be empty");
            return false;
        } else{
            editEndDate.setError(null);
            // birth.setErrorEnabled(false);
            return true;
        }
    }

    private  boolean validateStep(){
        String val = editStepRequired.getEditText().getText().toString().trim();

        if(val.isEmpty()){
            editStepRequired.setError("Field cannot be empty");
            return false;
        } else{
            editStepRequired.setError(null);
            editStepRequired.setErrorEnabled(false);
            return true;
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    public void onBackPressed() {
        Intent intent = new Intent(EditChallengeActivity.this, ChallengesActivity.class);
        startActivity(intent);
        finish();
    }

}

