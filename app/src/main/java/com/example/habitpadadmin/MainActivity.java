package com.example.habitpadadmin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout mainDrawerLayout;
    private NavigationView mainNavigationView;
    private Toolbar mainToolbar;

    SharedPreferences sharedPreferences;

    public static final String fileName = "adminLogin";

    private CardView tipCV, challengeCV, voucherCV, giftCV, feedbackCV;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sharedPreferences = getSharedPreferences(fileName, Context.MODE_PRIVATE);

        tipCV = findViewById(R.id.admin_tip);
        challengeCV = findViewById(R.id.admin_challenge);
        voucherCV = findViewById(R.id.admin_voucher);
        giftCV = findViewById(R.id.admin_gift);
        feedbackCV = findViewById(R.id.admin_feedback);

        mainDrawerLayout = findViewById(R.id.main_drawerlayout);
        mainNavigationView = findViewById(R.id.main_navigationview);
        mainToolbar = findViewById(R.id.maintoolbar);


        //Navigation Drawer Menu
        mainNavigationView.bringToFront();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,mainDrawerLayout,mainToolbar,R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mainDrawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        mainNavigationView.setNavigationItemSelectedListener(this);

        tipCV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent tipCVIntent = new Intent(getApplicationContext(), AdminTipsActivity.class);
                startActivity(tipCVIntent);
            }
        });

        challengeCV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent challengeCVIntent = new Intent(getApplicationContext(), ChallengesActivity.class);
                startActivity(challengeCVIntent);
            }
        });

        voucherCV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent voucherCVIntent = new Intent(getApplicationContext(), VoucherActivity.class);
                startActivity(voucherCVIntent);
            }
        });

        giftCV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent voucherCVIntent = new Intent(getApplicationContext(), GiftActivity.class);
                startActivity(voucherCVIntent);
            }
        });

        feedbackCV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent voucherCVIntent = new Intent(getApplicationContext(), FeedbackActivity.class);
                startActivity(voucherCVIntent);
            }
        });

    }

    @Override
    public void onBackPressed(){

        if (mainDrawerLayout.isDrawerOpen(GravityCompat.START)){
            mainDrawerLayout.closeDrawer(GravityCompat.START);
        }
        else{
            super.onBackPressed();
        }
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