package hu.ait.emergencyapp;

import android.*;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.LayoutRes;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import hu.ait.emergencyapp.adapter.NewsAdapter;
import hu.ait.emergencyapp.data.City;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        MyLocationMonitor.MyLocationListener{

    protected DrawerLayout drawer;
    private NewsAdapter newsAdapter;
    private TextView cityTitle;
    private TextView contactInfoTitle;
    private TextView contactInfo;
    private TextView newsTitle;
    private MyLocationMonitor myLocationMonitor;
    private String cityName;
    private Typeface font;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        contactInfo = (TextView) findViewById(R.id.contactInfo);
        contactInfoTitle = (TextView) findViewById(R.id.contactInfoTitle);
        cityTitle = (TextView) findViewById(R.id.cityTitle);
        newsTitle = (TextView) findViewById(R.id.newsTitle);
        font = Typeface.createFromAsset(getAssets(), "fonts/SEASRN__.ttf");
        cityTitle.setTypeface(font);
        font = Typeface.createFromAsset(getAssets(), "fonts/Raleway-Medium.ttf");
        contactInfoTitle.setTypeface(font);
        newsTitle.setTypeface(font);

        myLocationMonitor = new MyLocationMonitor(this, this);
        requestNeededPermission();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        RecyclerView newsRecycler = (RecyclerView) findViewById(R.id.newsRecycler);
        newsRecycler.setHasFixedSize(true);

        final LinearLayoutManager layoutManager =
                new LinearLayoutManager(this);

        newsRecycler.setLayoutManager(layoutManager);

        newsAdapter = new NewsAdapter();
        newsRecycler.setAdapter(newsAdapter);

        cityName = "London";

//        City city = new City("Budapest", "107", "105");
//
//        String key = FirebaseDatabase.getInstance().getReference().
//                child("cities").push().getKey();
//
//        FirebaseDatabase.getInstance().getReference().
//                child("cities").child("budapest").setValue(city);



        final DatabaseReference citiesRef = FirebaseDatabase.getInstance().
                getReference("cities");

        citiesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot citySnapshot : dataSnapshot.getChildren()){
                    City city = citySnapshot.getValue(City.class);
                    if(city.getName().equals(cityName)){
                        cityTitle.setText(cityName);
                        contactInfo.setText("Fire service: " + city.getFireNumber() +
                        "\nPolice: " + city.getPoliceNumber());
                    }
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });



    }

    private void requestNeededPermission() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.ACCESS_FINE_LOCATION)) {
                // Toast...
            }

            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 1001);
        } else {
            // we already have the permission
            myLocationMonitor.startLocationMonitoring();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 1001) {
            if (grantResults.length > 0 &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                Toast.makeText(this, "Permission granted, jupeee!", Toast.LENGTH_SHORT).show();

                myLocationMonitor.startLocationMonitoring();

            } else {
                Toast.makeText(this, "Permission not granted :(", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void showSpinnerDialog() {

        AlertDialog.Builder b = new AlertDialog.Builder(this);
        b.setTitle("Choose a City");
        String[] types = {"Budapest", "London", "Paris", "Boston"};
        b.setItems(types, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();
                switch(which){
                    case 0:
                        Toast.makeText(MainActivity.this, "BUDAPEST", Toast.LENGTH_LONG).show();
                        break;
                    case 1:
                        Toast.makeText(MainActivity.this, "LONDON", Toast.LENGTH_LONG).show();
                        break;
                    case 2:
                        Toast.makeText(MainActivity.this, "PARIS", Toast.LENGTH_LONG).show();
                        break;
                }
            }
        });
        b.show();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.home) {

        } else if (id == R.id.search) {

            showSpinnerDialog();

        } else if (id == R.id.favorites) {

        } else if (id == R.id.map) {

        } else if (id == R.id.about) {

            Toast.makeText(this, "Created by JHong and Soo", Toast.LENGTH_LONG).show();

        } else if (id == R.id.logout) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void newLocationReceived(Location location) {

        Geocoder geocoder = new Geocoder(MainActivity.this);
        List<Address> addressList = null;
        cityName = "test";

        try {
            addressList = geocoder.getFromLocation(
                    location.getLatitude(),
                    location.getLongitude(),
                    1);
            cityName = addressList.get(0).getAddressLine(0) + "\n";

        } catch (IOException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException illegalArgumentException) {
            // Catch invalid latitude or longitude values.
            Log.e("TAG_",
                    "Latitude = " + location.getLatitude() +
                    ", Longitude = " +
                    location.getLongitude(), illegalArgumentException);
        }

        Log.d("TAG_", cityName);


        final DatabaseReference citiesRef = FirebaseDatabase.getInstance().
                getReference("cities");

        citiesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot citySnapshot : dataSnapshot.getChildren()){
                    City city = citySnapshot.getValue(City.class);
                    if(city.getName().equals(cityName)){
                        contactInfo.setText("Fire service: " + city.getFireNumber() +
                                "\nPolice: " + city.getPoliceNumber());
                    }
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });


    }

    @Override
    protected void onDestroy() {
        myLocationMonitor.stopLocationMonitoring();
        super.onDestroy();
    }
}
