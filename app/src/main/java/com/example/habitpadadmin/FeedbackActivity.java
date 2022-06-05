package com.example.habitpadadmin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.habitpadadmin.Adapter.FeedbackAdapter;
import com.example.habitpadadmin.Adapter.VoucherAdapter;
import com.example.habitpadadmin.Model.Feedback;
import com.example.habitpadadmin.Model.Voucher;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class FeedbackActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private Toolbar toolbar;

    private RecyclerView feedbackRecyclerView;
    private List<Feedback> feedbacks;

    SharedPreferences sharedPreferences;

    public static final String fileName = "adminLogin";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);

        sharedPreferences = getSharedPreferences(fileName, Context.MODE_PRIVATE);

        feedbackRecyclerView = findViewById(R.id.feedback_recyclerview);
        feedbackRecyclerView.setHasFixedSize(true);
        LinearLayoutManager voucherLinearLayoutManager = new LinearLayoutManager(FeedbackActivity.this);
        voucherLinearLayoutManager.setReverseLayout(true);
        voucherLinearLayoutManager.setStackFromEnd(true);
        feedbackRecyclerView.setLayoutManager(voucherLinearLayoutManager);
        feedbacks = new ArrayList<>();

        getFeedback();


        drawerLayout = findViewById(R.id.feedback_drawerlayout);
        navigationView = findViewById(R.id.feedback_navigationview);
        toolbar = findViewById(R.id.feedbacktoolbar);


        //Navigation Drawer Menu
        navigationView.bringToFront();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);

    }

    @Override
    public void onBackPressed(){

        if (drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }
        else{
            super.onBackPressed();
        }
    }

    private void getFeedback(){
        // Initializing Request queue

        StringRequest stringRequest = new StringRequest(Request.Method.GET, Urls.GET_FEEDBACK_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Log.i("tagconvertstr", "["+response+"]");
                            //JSONArray array = new JSONArray(response);
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("feedback");

                            for (int i = 0; i<jsonArray.length(); i++){

                                JSONObject object = jsonArray.getJSONObject(i);
                                String feedbackID = object.getString("feedbackID").trim();
                                String email = object.getString("email").trim();
                                String feedbackDetail = object.getString("feedbackDetail").trim();
                                String feedbackDate = object.getString("feedbackDate").trim();



                                Feedback feedback = new Feedback(feedbackID,email,feedbackDetail,feedbackDate);
                                feedbacks.add(feedback);

                                FeedbackAdapter feedbackAdapter = new FeedbackAdapter(FeedbackActivity.this,feedbacks);
                                feedbackRecyclerView.setAdapter(feedbackAdapter);
                            }

                        }catch (Exception e){
                            e.printStackTrace();
                        }


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(FeedbackActivity.this, error.toString(), Toast.LENGTH_LONG).show();
            }
        });

        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.admin_nav_home:
                Intent homeIntent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(homeIntent);
                break;

            case R.id.admin_nav_tip:
                Intent i = new Intent(getApplicationContext(), AdminTipsActivity.class);
                startActivity(i);
                break;

            case R.id.admin_nav_challenge:
                Intent challengeIntent = new Intent(getApplicationContext(), ChallengesActivity.class);
                startActivity(challengeIntent);
                break;

            case R.id.admin_nav_voucher:
                Intent voucherIntent = new Intent(getApplicationContext(), VoucherActivity.class);
                startActivity(voucherIntent);
                break;

            case R.id.admin_nav_gift:
                Intent giftIntent = new Intent(getApplicationContext(), GiftActivity.class);
                startActivity(giftIntent);
                break;

            case R.id.admin_nav_feedback:
                Intent feedbackIntent = new Intent(getApplicationContext(), FeedbackActivity.class);
                startActivity(feedbackIntent);
                break;

            case R.id.admin_nav_logout:
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.clear();
                editor.commit();
                Intent logoutIntent = new Intent(getApplicationContext(), AdminLoginActivity.class);
                startActivity(logoutIntent);
                ((MainActivity) getApplicationContext()).finish();


                break;
        }
        return true;
    }
}