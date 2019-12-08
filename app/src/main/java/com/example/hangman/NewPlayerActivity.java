package com.example.hangman;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.util.ArrayList;

public class NewPlayerActivity extends AppCompatActivity implements View.OnClickListener {
    public static String playerName = "";
    protected SharedPreferences sharedPlace;
    MediaPlayer mp;
    EditText playerNameEditText;
    Button btnContinue, btnCancel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_layout);
        // initialize SharedPreferences
        sharedPlace = getSharedPreferences("SharedPlace", MODE_PRIVATE);
        mp = MediaPlayer.create(getApplicationContext(), R.raw.oak_theme);
        playerNameEditText = findViewById(R.id.playerNameEditText);
        btnContinue = findViewById(R.id.btnContinue);
        btnContinue.setOnClickListener(this);
        btnCancel = findViewById(R.id.btnCancel);
        btnCancel.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == btnContinue.getId()) {
            String newPlayer = playerNameEditText.getText().toString().trim();
            if (!newPlayer.matches("") && newPlayer.length() >= 2) {
                GameActivity.playerName = newPlayer;
                GameActivity.gamePaused = false;
                startActivity(new Intent(getApplicationContext(), GameActivity.class));
            }
            else {
                Toast toast = Toast.makeText(this, "Player name needs to be at least 2 characters long.", Toast.LENGTH_LONG);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
            }
        }
        else if (view.getId() == btnCancel.getId()) {
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
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
