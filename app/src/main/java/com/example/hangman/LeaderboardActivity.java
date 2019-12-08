package com.example.hangman;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.HashMap;

public class LeaderboardActivity extends AppCompatActivity{
    private ListView itemsListView;
    private HangmanDB db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leaderboard);
        itemsListView = findViewById(R.id.itemsListView);

        db = new HangmanDB(this);
        updateDisplay();
    }

    private void updateDisplay(){
        ArrayList<HashMap<String, String>> data = db.getLeaderboardStandings();

        //Create the resource, from, and to variables
        int resource = R.layout.leaderboard_listview_item;
        String[] from = {"name", "score", "guessed"};
        int[] to = {R.id.nameTextView, R.id.scoreTextView, R.id.guessedTextView};

        //create and set the adapter
        SimpleAdapter adapter =
                new SimpleAdapter(this, data, resource, from, to);
        itemsListView.setAdapter(adapter);
    }

}
