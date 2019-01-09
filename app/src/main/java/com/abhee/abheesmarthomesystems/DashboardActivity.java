package com.abhee.abheesmarthomesystems;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.FragmentManager;
import android.app.Notification;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Geocoder;
import android.location.Location;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.os.ResultReceiver;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.abhee.abheesmarthomesystems.service.NotificationService;

import java.util.Timer;
import java.util.TimerTask;

public class DashboardActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    int cback=0;
    public static AppCompatTextView user_name;
    private Toolbar mToolbar;
    FragmentTransaction transaction;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    private Boolean exit = false;
    FragmentManager fm;
    public static AppCompatTextView farname;
  //  TextView textCartItemCount;
    int mCartItemCount ;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        startService(new Intent(getApplicationContext(),NotificationService.class));
        sharedPreferences = this.getSharedPreferences("Abhee", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        String fname = sharedPreferences.getString("fname", "");
        String lname = sharedPreferences.getString("lname", "");
        mToolbar = (Toolbar) findViewById(R.id.nav_tool);
        mToolbar.setNavigationIcon(R.drawable.ic_back);

       // textCartItemCount=(TextView)findViewById(R.id.cart_badge);
       // setupBadge();
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        setSupportActionBar(mToolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toggle.syncState();

        getSupportActionBar().setCustomView(R.layout.activity_navigationtext);
        View view = getSupportActionBar().getCustomView();
        farname=(AppCompatTextView) view.findViewById(R.id.txtName);
        farname.setText("Dashboard"+"");

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View header = navigationView.getHeaderView(0);
        user_name = (AppCompatTextView) header.findViewById(R.id.user);
        user_name.setText(fname+" "+lname);
        transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame, new ContentDashboard().newInstance());
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        transaction.commit();
        /*new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                setupBadge();
            }
        }, 100);
        Thread.setDefaultUncaughtExceptionHandler(new MyExceptionHandler(getApplicationContext(), LoginActivity.class));*/
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        int id = item.getItemId();
        if (id == R.id.action_notify) {
            transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.frame, new NotificationsActivity().newInstance());
            transaction.addToBackStack("backStack");
            transaction.addToBackStack(null);
            transaction.commit();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);

        final MenuItem menuItem = menu.findItem(R.id.action_notify);
        View actionView = MenuItemCompat.getActionView(menuItem);
        //textCartItemCount = (TextView) actionView.findViewById(R.id.cart_badge);
        /* setupBadge();*/
       /* actionView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onOptionsItemSelected(menuItem);
            }
        });*/
        return true;
    }

    @Override
    public void onBackPressed(){
    DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
        drawer.closeDrawer(GravityCompat.START);
    } else {
            editor.putString("ddd","1");
            editor.commit();
        super.onBackPressed();
    }
       /*if(cback>1)
    {
        // viewPager.setCurrentItem(0);
    }
        else if(cback==1 ){
        Intent intent = new Intent(DashboardActivity.this, DashboardActivity.class);
        startActivity(intent);
    }
        else
    {
        Intent intent = new Intent(DashboardActivity.this, DashboardActivity.class);
        startActivity(intent);
    }*/
    }

    private void displaySelectedScreen(int itemId) {
        transaction = getSupportFragmentManager().beginTransaction();
        switch (itemId) {
            case R.id.nav_home:
                cback=1;
                transaction.replace(R.id.frame, new ContentDashboard().newInstance());
                transaction.addToBackStack("tag");
                break;
            case R.id.nav_profile:
                cback=2;
                transaction.replace(R.id.frame, new ProfileActivity().newInstance());
                transaction.addToBackStack("tag");
                break;
//            case R.id.nav_products:
//                transaction.replace(R.id.frame, new Products_Names().newInstance());
//                transaction.addToBackStack("tag");
//                break;
//            case R.id.nav_notify:
//                transaction.replace(R.id.frame, new NotificationsActivity().newInstance());
//                transaction.addToBackStack("tag");
//                break;
//            case R.id.nav_ticket:
//                transaction.replace(R.id.frame, new TicketStatusActivity().newInstance());
//                transaction.addToBackStack("tag");
//                break;
//            case R.id.nav_servicerequest:
//                transaction.replace(R.id.frame, new ServiceRequestActivity().newInstance());
//                transaction.addToBackStack("tag");
//                break;
//            case R.id.nav_enquiry:
//                transaction.replace(R.id.frame, new EnquiryActivity().newInstance());
//                transaction.addToBackStack("tag");
//                break;
            case R.id.nav_logout:
                Intent intent = new Intent(DashboardActivity.this, LoginActivity.class);
                startActivity(intent);
                break;
        }
        transaction.addToBackStack(null);
        transaction.commit();

        DrawerLayout drawer = (DrawerLayout)findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
    }
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        displaySelectedScreen(item.getItemId());
        return true;
    }

    @Override
    protected void onDestroy() {
        stopService(new Intent(getApplicationContext(),NotificationService.class));
        super.onDestroy();
    }

   /* private void setupBadge() {
        if (textCartItemCount != null) {
            if (mCartItemCount == 0) {
                if (textCartItemCount.getVisibility() != View.GONE) {
                    textCartItemCount.setVisibility(View.GONE);
                }
            } else {
                textCartItemCount.setText(String.valueOf(Math.min(Integer.valueOf(sharedPreferences.getString("noticount","0")), 99)));
                if (textCartItemCount.getVisibility() != View.VISIBLE) {
                    textCartItemCount.setVisibility(View.VISIBLE);
                }
            }
        }
    }*/
}


