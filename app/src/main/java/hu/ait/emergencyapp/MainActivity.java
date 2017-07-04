package hu.ait.emergencyapp;

import android.*;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
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
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import hu.ait.emergencyapp.adapter.NewsAdapter;
import hu.ait.emergencyapp.data.City;
import hu.ait.emergencyapp.data.WeatherResult;
import hu.ait.emergencyapp.retrofit.WeatherAPI;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        MyLocationMonitor.MyLocationListener{

    public static final String KEY_CITY_NAME = "KEY_CITY_NAME";
    @BindView(R.id.contactInfo)
    TextView contactInfo;
    @BindView(R.id.contactInfoTitle)
    TextView contactInfoTitle;
    @BindView(R.id.cityTitle)
    TextView cityTitle;
    @BindView(R.id.newsTitle)
    TextView newsTitle;
    @BindView(R.id.contactInfoCard)
    CardView contactInfoCard;

    protected DrawerLayout drawer;
    private NewsAdapter newsAdapter;
    public Set<String> favorites = new HashSet<>();
    private MyLocationMonitor myLocationMonitor;
    private String cityName;
    private Typeface font;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        font = Typeface.createFromAsset(getAssets(), "fonts/SEASRN__.ttf");
        cityTitle.setTypeface(font);
        font = Typeface.createFromAsset(getAssets(), "fonts/Raleway-Bold.ttf");
        contactInfoTitle.setTypeface(font);
        contactInfoTitle.setPaintFlags(contactInfoTitle.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        newsTitle.setTypeface(font);

        myLocationMonitor = new MyLocationMonitor(this, this);
        requestNeededPermission();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


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

        cityName = "";

        updateCityInfo();

        contactInfoCard.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent infoActivity = new Intent(MainActivity.this, InfoActivity.class);
                infoActivity.putExtra(KEY_CITY_NAME, cityName.trim());
                startActivity(infoActivity);
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

    private void showSearchDialog() {

        String[] cityNames = new String[] { "Budapest",
                "Bukarest","Krakkó", "Bécs", "Boston", "London", "Paris", "Seattle", "Austin", "Barcelona",
                "Amsterdam, Dallas"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choose a City");

        //final EditText etTodo = new EditText(this);
        final AutoCompleteTextView autoTV = new AutoCompleteTextView(this);

        ArrayAdapter<String> cityAdapter =
                new ArrayAdapter<String>(this,
                        android.R.layout.
                                simple_dropdown_item_1line, cityNames);
        autoTV.setAdapter(cityAdapter);

        builder.setView(autoTV);

        //rando

        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                //cityName = autoTV.getText().toString();
                getTempIcon(autoTV.getText().toString());
                //cityRecyclerAdapter.addCity(etTodo.getText().toString());
                cityName = autoTV.getText().toString();
                updateCityInfo();
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.show();
    }

    private void showFavoriteDialog() {

        AlertDialog.Builder b = new AlertDialog.Builder(this);
        b.setTitle("Favorite Cities");

        final String [] favoritesArray = favorites.toArray(new String[favorites.size()]);

        b.setItems(favoritesArray, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();

                String selectedCity = favoritesArray[which];
                Log.d("TAG_HI", "selected: " + selectedCity);

                getTempIcon(selectedCity);
                cityName = selectedCity;
                updateCityInfo();

//                switch(which){
//                    case 0:
//                        Toast.makeText(MainActivity.this, "BUDAPEST", Toast.LENGTH_LONG).show();
//                        break;
//                    case 1:
//                        Toast.makeText(MainActivity.this, "LONDON", Toast.LENGTH_LONG).show();
//                        break;
//                    case 2:
//                        Toast.makeText(MainActivity.this, "PARIS", Toast.LENGTH_LONG).show();
//                        break;
//                }
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
        //getMenuInflater().inflate(R.menu.main, menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);

        if (favorites.contains(cityName)) {
            menu.getItem(0).setIcon(R.drawable.fave);
        } else {
            menu.getItem(0).setIcon(R.drawable.not_fave);
        }

//        MenuItem item = menu.findItem(R.id.fav);
//        if (item != null) {
//            item.setIcon(R.drawable.fave);
//        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.fav) {

            if (item.getIcon().getConstantState().equals(
                    getResources().getDrawable(R.drawable.not_fave).getConstantState()
            )) {

                item.setIcon(R.drawable.fave);
                favorites.add(cityName);
                //invalidateOptionsMenu();

            } else if (item.getIcon().getConstantState().equals(
                    getResources().getDrawable(R.drawable.fave).getConstantState()
            )) {

                item.setIcon(R.drawable.not_fave);
                favorites.remove(cityName);
                //invalidateOptionsMenu();
            }
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

            showSearchDialog();

        } else if (id == R.id.favorites) {

            showFavoriteDialog();

        } else if (id == R.id.map) {

            Intent mapsActivity = new Intent();
            mapsActivity.setClass(MainActivity.this, MapsActivity.class);
            startActivity(mapsActivity);

        } else if (id == R.id.about) {

            Toast.makeText(this, "Created by JHong and Soo", Toast.LENGTH_LONG).show();

        } else if (id == R.id.logout) {

            Intent loginMainActivity = new Intent();
            loginMainActivity.setClass(MainActivity.this, LoginActivity.class);
            startActivity(loginMainActivity);
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
            cityName = addressList.get(0).getLocality();

        } catch (IOException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException illegalArgumentException) {
            // Catch invalid latitude or longitude values.
            Log.d("TAG_",
                    "Latitude = " + location.getLatitude() +
                    ", Longitude = " +
                    location.getLongitude(), illegalArgumentException);
        }

        updateCityInfo();


    }

    private void updateCityInfo() {

        final DatabaseReference citiesRef = FirebaseDatabase.getInstance().
                getReference("cities");

        citiesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot citySnapshot : dataSnapshot.getChildren()){

                    if(citySnapshot.getKey().trim().equals(cityName.toLowerCase().trim())){
                        City city = citySnapshot.getValue(City.class);
                        cityTitle.setText(cityName);
                        if(city.getGeneralEmergency() != null) {
                            contactInfo.setText("General Emergency Number: " +
                                    city.getGeneralEmergency() + "");

                        } else {
                            contactInfo.setText("Police: " + city.getPoliceNumber() +
                                    "\nFire: " + city.getFireNumber() +
                                    "\nAmbulance: " + city.getAmbulanceNumber());
                        }
                        getTempIcon(cityName);
                    }
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });
        invalidateOptionsMenu();
    }

    @Override
    protected void onDestroy() {
        myLocationMonitor.stopLocationMonitoring();
        super.onDestroy();
    }

    public void getTempIcon(String name) {

        final TextView currentTemp = (TextView) findViewById(R.id.weatherTemp);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://api.openweathermap.org")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        final WeatherAPI weatherAPI = retrofit.create(WeatherAPI.class);

        Call<WeatherResult> cityWeather = weatherAPI.getWeather(name, "metric","79e23c87f571848c7c550e6b937fda62");

        cityWeather.enqueue(new Callback<WeatherResult>() {
            @Override
            public void onResponse(Call<WeatherResult> call, Response<WeatherResult> response) {

                WeatherResult weatherResult = response.body();

                if (weatherResult != null) {

                    currentTemp.setText(weatherResult.getMain().getTemp() + "°C");

                    String s = weatherResult.getWeather().get(0).getIcon();
                    ImageView imageView = (ImageView) findViewById(R.id.weatherIcon);
                    Glide.with(MainActivity.this).load("http://openweathermap.org/img/w/" + s + ".png").into(imageView);

                } else {

                    Toast.makeText(MainActivity.this, "NOT A VALID CITY", Toast.LENGTH_SHORT).show();
                }


            }

            @Override
            public void onFailure(Call<WeatherResult> call, Throwable t) {
                //minTemp.setText("Error: "+t.getMessage());

            }
        });
    }
}
