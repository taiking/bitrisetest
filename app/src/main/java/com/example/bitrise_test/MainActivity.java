package com.example.bitrise_test;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.test.espresso.idling.CountingIdlingResource;

import android.util.Log;
import android.view.View;
import android.widget.Button;

import jp.fluct.fluctsdk.FluctErrorCode;
import jp.fluct.fluctsdk.FluctRewardedVideo;
import jp.fluct.fluctsdk.FluctRewardedVideoSettings;

public class MainActivity extends AppCompatActivity {

    private FluctRewardedVideo rewardedVideo;
    private CountingIdlingResource idlingResource;
    private boolean isFinished;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        idlingResource = getOrStoreIdlingResource();

        FluctRewardedVideoSettings settings = new FluctRewardedVideoSettings.Builder()
                .debugMode(true)
                .build();

        rewardedVideo = FluctRewardedVideo.getInstance("1000117639", "1000199372", this, settings);

        rewardedVideo.setListener(new FluctRewardedVideo.Listener() {
            @Override
            public void onLoaded(String groupId, String unitId) {
                Log.d("MainActivity", "onLoaded");
                rewardedVideo.show();
            }

            @Override
            public void onFailedToLoad(String groupId, String unitId, FluctErrorCode errorCode) {
                Log.d("MainActivity", "onFailedToLoad");
            }

            @Override
            public void onOpened(String groupId, String unitId) {
                Log.d("MainActivity", "onOpened");
            }

            @Override
            public void onStarted(String groupId, String unitId) {
                Log.d("MainActivity", "onStarted");
            }

            @Override
            public void onShouldReward(String groupId, String unitId) {
                Log.d("MainActivity", "onShouldReward");
                isFinished = true;
                getOrStoreIdlingResource().decrement();
            }

            @Override
            public void onClosed(String groupId, String unitId) {
                Log.d("MainActivity", "onClosed");
            }

            @Override
            public void onFailedToPlay(String groupId, String unitId, FluctErrorCode errorCode) {
                Log.d("MainActivity", "onFailedToPlay");
            }
        });
        Button button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                idlingResource.increment();
                rewardedVideo.loadAd();
            }
        });
    }

        public CountingIdlingResource getOrStoreIdlingResource() {
            if (idlingResource == null) {
                idlingResource = new CountingIdlingResource("main");
            }
            return idlingResource;
        }

        public boolean isFinished() {
            return isFinished;
        }
}
