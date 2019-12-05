package com.example.hangman;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class GameActivity extends AppCompatActivity implements View.OnClickListener {
    TextView textViewTurn;
    int turn = 0, score = 0; //when you iterate turn, check with data.size instead of just 151
    int curChances = 5;
    String triedLetters = "";
    int color;
    int colorRegular;
    String sPlayer1 = null, sPlayer2 = null;
    String nPlayer1 = "", nPlayer2 = "";

    ImageView myImgView;
    String curPokId, curPokName, curPokNameModified, curPokReg, curPokShad, playerName;
    private SharedPreferences sharedPlace;
    private HangmanDB db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        db = new HangmanDB(this.getApplicationContext());
        myImgView = findViewById(R.id.imageViewPokemon);
        //colorRegular = getResources().getColor(R.color.colorRegular);
        // initialize SharedPreferences
        sharedPlace = getSharedPreferences("SharedPlace", MODE_PRIVATE);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //getMenuInflater().inflate(R.menu.activity_game, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void createNewGame() {

    }
    @Override
    public void onClick(View view) {

    }

    private void checkGame(){

    }

    private void endGame(Button btn1, Button btn2, Button btn3) {

    }

    @Override
    protected void onPause() {
        SharedPreferences.Editor sharedEditor = sharedPlace.edit();
        sharedEditor.putString("textViewTurn", textViewTurn.getText().toString());
        sharedEditor.putString("curPokId", curPokId);
        sharedEditor.putString("curPokName", curPokName);
        sharedEditor.putString("curPokNameModified", curPokNameModified);
        sharedEditor.putString("curPokReg", curPokReg);
        sharedEditor.putString("curPokShad", curPokShad);
        sharedEditor.putString("playerName", playerName); //This will detect if there was a game paused or not. Player will enter their name before game start
        sharedEditor.putString("triedLetters", triedLetters);
        sharedEditor.putInt("turn", turn);
        sharedEditor.putInt("score", score);
        sharedEditor.putInt("curChances", curChances);
        sharedEditor.commit();
        super.onPause();
    }

    @Override
    protected void onResume(){
        super.onResume();
        db = new HangmanDB(this.getApplicationContext());
        boolean gameDone = true;
        try {
            curPokId = sharedPlace.getString("curPokId", "");
            curPokName = sharedPlace.getString("curPokName", "");
            curPokNameModified = sharedPlace.getString("curPokNameModified", "");
            curPokReg = sharedPlace.getString("curPokReg", "");
            curPokShad = sharedPlace.getString("curPokShad", "");
            playerName = sharedPlace.getString("playerName", "");
            triedLetters = sharedPlace.getString("triedLetters", "");
            turn = sharedPlace.getInt("curPokName", 0);
            score = sharedPlace.getInt("score", 0);
            curChances = sharedPlace.getInt("curChances", 5);

            textViewTurn.setText(sharedPlace.getString("textViewTurn", ""));
            //else createNewGame();

        } catch (Exception e) {
            e.printStackTrace();
            createNewGame();
        }

    }
}
