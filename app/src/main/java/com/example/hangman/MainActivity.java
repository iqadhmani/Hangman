package com.example.hangman;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    Button btnStartGame, btnLeaderboard;
    MediaPlayer mp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnStartGame = findViewById(R.id.btnStartGame);
        btnLeaderboard = findViewById(R.id.btnLeaderboard);
        btnStartGame.setOnClickListener(this);
        btnLeaderboard.setOnClickListener(this);
        mp = MediaPlayer.create(getApplicationContext(), R.raw.hangmantitletheme);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == btnStartGame.getId()) {
            startActivity(new Intent(getApplicationContext(), GameActivity.class));
        }
        else if (view.getId() == btnLeaderboard.getId()) {
            startActivity(new Intent(getApplicationContext(), LeaderboardActivity.class));
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        try {
            mp.pause();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
            mp.setLooping(true);
            mp.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
