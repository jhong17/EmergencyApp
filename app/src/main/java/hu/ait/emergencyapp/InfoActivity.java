package hu.ait.emergencyapp;

import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class InfoActivity extends AppCompatActivity {

    @BindView(R.id.infoTitle)
    TextView infoTitle;

    private Typeface font;
    private String cityName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        ButterKnife.bind(this);

        font = Typeface.createFromAsset(getAssets(), "fonts/SEASRN__.ttf");
        infoTitle.setTypeface(font);

        if(getIntent().hasExtra(MainActivity.KEY_CITY_NAME)){

            cityName = getIntent().getStringExtra(MainActivity.KEY_CITY_NAME);
            infoTitle.setText(cityName);
        }


    }
}
