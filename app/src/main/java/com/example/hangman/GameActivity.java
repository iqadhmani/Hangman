package com.example.hangman;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
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

public class GameActivity extends AppCompatActivity implements View.OnClickListener, TextView.OnEditorActionListener {
    int turn = 0, score = 0; //when you iterate turn, check with data.size instead of just 151
    int curChances = 5;
    int guessed = 0;
    String triedLetters = "";
    int imgResId;

    TextView pokNameTextView, triedTextViewLetters, chancesTextViewNumber, scoreTextView;
    EditText inputEditText;

    ArrayList<Pokemon> pokemonArrayList;
    String pokemonObjString;

    ImageView pokImgView;
    String curPokId, curPokName, curPokNameModified, curPokReg, curPokShad;
    public static String playerName = "";
    char[] pokDisplayCharA;
    String pokDisplayString;
    protected SharedPreferences sharedPlace;
    protected static SharedPreferences.Editor sharedEditor;
    private HangmanDB db;
    MediaPlayer mp;
    protected static boolean gamePaused;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        db = new HangmanDB(this.getApplicationContext());
        pokImgView = findViewById(R.id.imageViewPokemon);
        // initialize SharedPreferences
        sharedPlace = getSharedPreferences("SharedPlace", MODE_PRIVATE);

        pokNameTextView = findViewById(R.id.pokNameTextView);
        triedTextViewLetters = findViewById(R.id.triedTextViewLetters);
        chancesTextViewNumber = findViewById(R.id.chancesTextViewNumber);
        inputEditText = findViewById(R.id.inputEditText);
        inputEditText.setOnEditorActionListener(this);
        mp = MediaPlayer.create(getApplicationContext(), R.raw.whosthatpokemon);
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
        if (!playerName.equals("")) {
            pokemonArrayList = db.getPokemon();
            gamePaused = true;
            try {
                pokemonObjString = ObjectSerializer.serialize(pokemonArrayList);

            } catch (IOException e) {
                e.printStackTrace();
            }
            updateRound();
        }
        else {
            startActivity(new Intent(getApplicationContext(), NewPlayerActivity.class));
        }
    }
    //Generate round after createGame or winning last round
    public void updateRound() {
        curChances = 5;
        triedLetters = "";
        pokDisplayString = "";
        curPokId = pokemonArrayList.get(turn).get("id");
        curPokName = pokemonArrayList.get(turn).get("name");
        curPokNameModified = curPokName.toLowerCase();
        if (curPokName.contains(".")) {
            curPokNameModified = curPokNameModified.replace(".", "");
        }
        if (curPokName.contains(" ")) {
            curPokNameModified = curPokNameModified.replace(" ", "_");
        }
        curPokReg = "p" + curPokId + curPokNameModified;
        curPokShad = curPokReg + "_";
        imgResId = getResources().getIdentifier(curPokShad, "drawable", "com.example.hangman");
        pokImgView.setImageResource(imgResId);
        //play who's that pokemon mp3
        mp.start();

        pokDisplayCharA = curPokName.toCharArray();
        for (int i = 1; i < pokDisplayCharA.length - 1; i++) {
            if (pokDisplayCharA[i] != ' ' && pokDisplayCharA[i] != '.') {
                pokDisplayCharA[i] = '_';
            }
        }
        revealOccurrences(pokDisplayCharA[0]);
        revealOccurrences(pokDisplayCharA[pokDisplayCharA.length - 1]);
        updatePokTextView();
        updateTriedTextView();
        updateChancesTextView();
    }

    public void revealOccurrences(char letter) {
        String letterString = String.valueOf(letter);
        String letterStringLower = letterString.toLowerCase();
        String letterStringUpper = letterString.toUpperCase();
        char letterLower = letterStringLower.toCharArray()[0];
        char letterUpper = letterStringUpper.toCharArray()[0];
        int letterIndexLower = curPokName.indexOf(letterLower);
        int letterIndexUpper = curPokName.indexOf(letterUpper);

        //Letter Lower loop
        while (letterIndexLower >= 0) {
            pokDisplayCharA[letterIndexLower] = curPokName.charAt(letterIndexLower);
            letterIndexLower = curPokName.indexOf(letterLower, letterIndexLower + 1);
        }
        //Letter Upper loop
        while (letterIndexUpper >= 0) {
            pokDisplayCharA[letterIndexUpper] = curPokName.charAt(letterIndexUpper);
            letterIndexUpper = curPokName.indexOf(letterUpper, letterIndexUpper + 1);
        }
        pokDisplayString = String.valueOf(pokDisplayCharA);
    }

    public void updatePokTextView() {
        String displayString = "";
        for (char character : pokDisplayCharA) {
            displayString += character + " ";
        }
        pokNameTextView.setText(displayString);
    }

    public void updateTriedTextView() {
        String displayString = "";
        char[] displayCharA = triedLetters.toCharArray();
        for (int i = 0; i < displayCharA.length; i++) {
            if (i == 0) {
                displayString = String.valueOf(displayCharA[0]);
            }
            else {
                displayString += ", " + displayCharA[i];
            }
        }
        triedTextViewLetters.setText(displayString);
    }

    public void updateChancesTextView() {
        chancesTextViewNumber.setText(curChances + "/5");
    }

    @Override
    public void onClick(View view) {

    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (v.getId() == inputEditText.getId()) {
            String enteredLetter = inputEditText.getText().toString().toLowerCase();
            inputEditText.setText("");
            if (!enteredLetter.matches("[a-zA-Z]")) {
                Toast.makeText(this, "Please enter alphabetical letters only.", Toast.LENGTH_LONG).show();
            }
            else if (pokDisplayString.toLowerCase().contains(enteredLetter) || triedLetters.toLowerCase().contains(enteredLetter)) {
                Toast.makeText(this, "Please enter letters not already displayed or part of the tried letters.", Toast.LENGTH_LONG).show();
            }
            else if (curPokName.toLowerCase().contains(enteredLetter)) {
                revealOccurrences(enteredLetter.toCharArray()[0]);
                updatePokTextView();
                updateTriedTextView();
                updateChancesTextView();
                if (pokDisplayString.toLowerCase().equals(curPokName.toLowerCase())) {
                    turn++;
                    score += curChances;
                    imgResId = getResources().getIdentifier(curPokReg, "drawable", "com.example.hangman");
                    pokImgView.setImageResource(imgResId);
                    if (turn < pokemonArrayList.size()) {
                        final Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                //call updateRound() after 2 seconds
                                updateRound();
                            }
                        }, 2000);
                    }
                    else {
                        final Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                //call endGame() after 2 seconds
                                endGame();
                            }
                        }, 2000);
                    }
                }
            }
            else {
                curChances--;
                triedLetters += enteredLetter;
                Toast.makeText(this, "This Pokemon's name does not contain the letter " + enteredLetter + ".", Toast.LENGTH_LONG).show();
                if (curChances > 0) {
                    updateTriedTextView();
                    updateChancesTextView();
                }
                else {
                    imgResId = getResources().getIdentifier(curPokReg, "drawable", "com.example.hangman");
                    pokImgView.setImageResource(imgResId);
                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            //call endGame() after 2 seconds
                            endGame();
                        }
                    }, 2000);
                }
            }
        }
        return false;
    }

    private void endGame() {
        //INSERT INTO Leaderboard (name, score, guessed) VALUES (playerName, score, turn);
        try {
            db.insertScore(playerName, score, turn);
        } catch (Exception e) {
            e.printStackTrace();
        }
        playerName = "";
        gamePaused = false;
        startActivity(new Intent(getApplicationContext(), LeaderboardActivity.class));
        //sharedEditor = sharedPlace.edit();
        //sharedEditor.putString("playerName", playerName);
        //sharedEditor.putString("pokemonObjString", null);
        //show leaderboard
    }

    @Override
    protected void onPause() {
        sharedEditor = sharedPlace.edit();

        sharedEditor.putString("pokNameTextView", pokNameTextView.getText().toString()); //for pokemon incomplete name textView
        sharedEditor.putString("triedTextViewLetters", triedTextViewLetters.getText().toString()); //for excluded letters textView
        sharedEditor.putString("chancesTextViewNumber", chancesTextViewNumber.getText().toString()); //for chances left textView

        sharedEditor.putString("curPokId", curPokId);
        sharedEditor.putString("curPokName", curPokName);
        sharedEditor.putString("curPokNameModified", curPokNameModified);
        sharedEditor.putString("curPokReg", curPokReg);
        sharedEditor.putString("curPokShad", curPokShad);
        sharedEditor.putString("playerName", playerName); //This will detect if there was a game paused or not. Player will enter their name before game start
        sharedEditor.putString("triedLetters", triedLetters);
        sharedEditor.putString("pokDisplayString", pokDisplayString);
        sharedEditor.putInt("turn", turn);
        sharedEditor.putInt("guessed", guessed);
        sharedEditor.putInt("score", score);
        sharedEditor.putInt("curChances", curChances);
        sharedEditor.putInt("imgResId", imgResId);
        sharedEditor.putString("pokemonObjString", pokemonObjString);
        sharedEditor.putBoolean("gamePaused", gamePaused);
        sharedEditor.commit();
        super.onPause();
    }

    @Override
    protected void onResume(){
        super.onResume();
        db = new HangmanDB(this.getApplicationContext());
        try {
            gamePaused = sharedPlace.getBoolean("gamePaused", false);
            if (gamePaused) {
                playerName = sharedPlace.getString("playerName", "");
                curPokId = sharedPlace.getString("curPokId", "");
                curPokName = sharedPlace.getString("curPokName", "");
                curPokNameModified = sharedPlace.getString("curPokNameModified", "");
                curPokReg = sharedPlace.getString("curPokReg", "");
                curPokShad = sharedPlace.getString("curPokShad", "");

                triedLetters = sharedPlace.getString("triedLetters", "");
                turn = sharedPlace.getInt("turn", 0);
                guessed = sharedPlace.getInt("guessed", 0);
                score = sharedPlace.getInt("score", 0);
                curChances = sharedPlace.getInt("curChances", 5);
                imgResId = sharedPlace.getInt("imgResId", 0);
                pokImgView.setImageResource(imgResId);

                pokNameTextView.setText(sharedPlace.getString("pokNameTextView", ""));
                triedTextViewLetters.setText(sharedPlace.getString("triedTextViewLetters", ""));
                chancesTextViewNumber.setText(sharedPlace.getString("chancesTextViewNumber", ""));

                pokDisplayString = sharedPlace.getString("pokDisplayString", "");
                pokDisplayCharA = pokDisplayString.toCharArray();

                pokemonObjString = sharedPlace.getString("pokemonObjString", "");
                pokemonArrayList = (ArrayList<Pokemon>) ObjectSerializer.deserialize(pokemonObjString);
            }
            else {
                createNewGame();
            }
        } catch (Exception e) {
            e.printStackTrace();
            createNewGame();
        }

    }


}
