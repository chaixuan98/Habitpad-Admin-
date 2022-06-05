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
import android.view.View;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.habitpadadmin.Adapter.ChallengeAdapter;
import com.example.habitpadadmin.Model.Challenge;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ChallengesActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    // Variable declarations
    private MaterialButton addChallengesBtn;
    private RecyclerView challengeRecyclerView;
    private List<Challenge> challenges;

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private Toolbar toolbar;


    SharedPreferences sharedPreferences;

    public static final String fileName = "adminLogin";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_challenges);

        sharedPreferences = getSharedPreferences(fileName, Context.MODE_PRIVATE);


        // Getting UI views from our xml file
        addChallengesBtn = findViewById(R.id.add_challenge_button);

        addChallengesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ChallengesActivity.this,AddChallengeActivity.class));

            }
        });

        challengeRecyclerView = findViewById(R.id.challenges_recyclerview);
        challengeRecyclerView.setHasFixedSize(true);
        LinearLayoutManager challengeLinearLayoutManager = new LinearLayoutManager(ChallengesActivity.this);
        challengeLinearLayoutManager.setReverseLayout(true);
        challengeLinearLayoutManager.setStackFromEnd(true);
        challengeRecyclerView.setLayoutManager(challengeLinearLayoutManager);
        challenges = new ArrayList<>();

        getChallenges();


        drawerLayout = findViewById(R.id.challenge_drawerlayout);
        navigationView = findViewById(R.id.challenge_navigationview);
        toolbar = findViewById(R.id.challengetoolbar);


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


    private void getChallenges(){
        // Initializing Request queue

        StringRequest stringRequest = new StringRequest(Request.Method.GET, Urls.GET_CHALLENGE_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Log.i("tagconvertstr", "["+response+"]");
                            //JSONArray array = new JSONArray(response);
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("challenge");

                            for (int i = 0; i<jsonArray.length(); i++){

                                JSONObject object = jsonArray.getJSONObject(i);
                                String challengeID = object.getString("challengeID").trim();
                                String challengePhoto = object.getString("challengePhoto").trim();
                                String challengeTitle = object.getString("challengeTitle").trim();
                                String challengeDescription = object.getString("challengeDescription").trim();
                                String challengeStartDate = object.getString("challengeStartDate").trim();
                                String challengeEndDate = object.getString("challengeEndDate").trim();
                                String challengeStep = object.getString("challengeStep").trim();


                                Challenge challenge = new Challenge(challengeID,challengePhoto,challengeTitle,challengeDescription,challengeStartDate, challengeEndDate, challengeStep);
                                challenges.add(challenge);

                                ChallengeAdapter challengeAdapter = new ChallengeAdapter(ChallengesActivity.this,challenges);
                                challengeRecyclerView.setAdapter(challengeAdapter);
                            }

                        }catch (Exception e){
                            e.printStackTrace();
                        }


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(ChallengesActivity.this, error.toString(), Toast.LENGTH_LONG).show();
            }
        });

        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

        switch (menuItem.getItemId()){
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