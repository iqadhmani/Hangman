package com.example.hangman;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ImageView myImgView;
    private HangmanDB db;
    String curPokId, curPokName, curPokNameModified, curPokReg, curPokShad;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        db = new HangmanDB(this.getApplicationContext());
        myImgView = findViewById(R.id.imageViewPokemon);
    }
    @Override
    protected void onResume() {
        super.onResume();
        ArrayList<Pokemon> data = db.getPokemon();
        curPokId = data.get(0).get("id");
        curPokName = data.get(0).get("name");
        curPokNameModified = curPokName;
        if (curPokName.contains(".")) {
            curPokNameModified = curPokNameModified.replace(".", "");
        }
        if (curPokName.contains(" ")) {
            curPokNameModified = curPokNameModified.replace(" ", "_");
        }
        curPokReg = "p" + curPokId + curPokNameModified.toLowerCase();
        curPokShad = "p" + curPokId + curPokNameModified.toLowerCase() + "_";
        int imgResId = getResources().getIdentifier(curPokShad, "drawable", "com.example.hangman");
        myImgView.setImageResource(imgResId);
    }
}
