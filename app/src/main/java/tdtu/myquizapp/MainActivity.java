package tdtu.myquizapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

public class MainActivity extends AppCompatActivity {
    private Button startButton,bookmarkButton, learnButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        startButton = findViewById(R.id.start_btn);
        bookmarkButton = findViewById(R.id.bookmark_btn);
        learnButton = findViewById(R.id.learn_btn);
        MobileAds.initialize(this);
        loadAds();
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent categoryIntent = new Intent(MainActivity.this,CategoriesActivity.class);
                startActivity(categoryIntent);
            }
        });
        bookmarkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent bookmarkIntent = new Intent(MainActivity.this,BookmarkActivity.class);
                startActivity(bookmarkIntent);
            }
        });
        learnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent trafficsignIntent = new Intent(MainActivity.this,TrafficSign.class);
                startActivity(trafficsignIntent);
            }
        });
    }

    private void loadAds() {
        AdView mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
    }
}