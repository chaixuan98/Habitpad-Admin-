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
import com.example.habitpadadmin.Adapter.GiftAdapter;
import com.example.habitpadadmin.Adapter.VoucherAdapter;
import com.example.habitpadadmin.AddVoucherActivity;
import com.example.habitpadadmin.Model.Gift;
import com.example.habitpadadmin.Model.Voucher;
import com.example.habitpadadmin.R;
import com.example.habitpadadmin.VoucherActivity;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class GiftActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    // Variable declarations
    private MaterialButton addGiftBtn;
    private RecyclerView giftRecyclerView;
    private List<Gift> gifts;

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private Toolbar toolbar;


    SharedPreferences sharedPreferences;

    public static final String fileName = "adminLogin";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gift);

        sharedPreferences = getSharedPreferences(fileName, Context.MODE_PRIVATE);


        // Getting UI views from our xml file
        addGiftBtn = findViewById(R.id.add_gift_button);

        addGiftBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(GiftActivity.this, AddGiftActivity.class));

            }
        });

        giftRecyclerView = findViewById(R.id.gift_recyclerview);
        giftRecyclerView.setHasFixedSize(true);
        LinearLayoutManager giftLinearLayoutManager = new LinearLayoutManager(GiftActivity.this);
        giftLinearLayoutManager.setReverseLayout(true);
        giftLinearLayoutManager.setStackFromEnd(true);
        giftRecyclerView.setLayoutManager(giftLinearLayoutManager);
        gifts = new ArrayList<>();

        getGift();


        drawerLayout = findViewById(R.id.gift_drawerlayout);
        navigationView = findViewById(R.id.gift_navigationview);
        toolbar = findViewById(R.id.gifttoolbar);


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

    private void getGift(){
        // Initializing Request queue

        StringRequest stringRequest = new StringRequest(Request.Method.GET, Urls.GET_GIFT_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Log.i("tagconvertstr", "["+response+"]");
                            //JSONArray array = new JSONArray(response);
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("gift");

                            for (int i = 0; i<jsonArray.length(); i++){

                                JSONObject object = jsonArray.getJSONObject(i);
                                String giftID = object.getString("giftID").trim();
                                String giftPhoto = object.getString("giftPhoto").trim();
                                String giftPoint = object.getString("giftPoint").trim();
                                String giftTitle = object.getString("giftTitle").trim();
                                String giftDesc = object.getString("giftDesc").trim();


                                Gift gift = new Gift(giftID,giftPhoto,giftPoint,giftTitle,giftDesc);
                                gifts.add(gift);

                                GiftAdapter giftAdapter = new GiftAdapter(GiftActivity.this,gifts);
                                giftRecyclerView.setAdapter(giftAdapter);
                            }

                        }catch (Exception e){
                            e.printStackTrace();
                        }


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(GiftActivity.this, error.toString(), Toast.LENGTH_LONG).show();
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