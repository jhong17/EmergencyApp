package hu.ait.emergencyapp;

import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import butterknife.BindView;
import butterknife.ButterKnife;
import hu.ait.emergencyapp.adapter.InfoAdapter;
import hu.ait.emergencyapp.adapter.NewsAdapter;
import hu.ait.emergencyapp.data.City;

public class InfoActivity extends AppCompatActivity {

    @BindView(R.id.infoTitle)
    TextView infoTitle;

    private Typeface font;
    private String cityName;
    private InfoAdapter infoAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        ButterKnife.bind(this);

        font = Typeface.createFromAsset(getAssets(), getString(R.string.fontType));
        infoTitle.setTypeface(font);

        if(getIntent().hasExtra(MainActivity.KEY_CITY_NAME)){

            cityName = getIntent().getStringExtra(MainActivity.KEY_CITY_NAME);
            infoTitle.setText(cityName);
        }

        RecyclerView infoRecycler = (RecyclerView) findViewById(R.id.infoRecycler);
        infoRecycler.setHasFixedSize(true);

        final LinearLayoutManager layoutManager = new LinearLayoutManager(this);

        infoRecycler.setLayoutManager(layoutManager);

        infoAdapter = new InfoAdapter();
        infoRecycler.setAdapter(infoAdapter);

        final DatabaseReference citiesRef = FirebaseDatabase.getInstance().
                getReference(getString(R.string.cities));

        citiesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                City city;
                for(DataSnapshot citySnapshot : dataSnapshot.getChildren()){
                    if(citySnapshot.getKey().trim().equals(cityName.toLowerCase().trim())){
                        city = citySnapshot.getValue(City.class);
                        infoAdapter.addCityInfo(city);
                    }
                }

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println(getString(R.string.read_failed) + databaseError.getCode());
            }
        });


        }
}
