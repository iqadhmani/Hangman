package com.example.hangman;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
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

    public void onBackPressed(){
        Intent a = new Intent(Intent.ACTION_MAIN);
        a.addCategory(Intent.CATEGORY_HOME);
        a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(a);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_leaderboard, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.leaderboard_menu_main:
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                return true;
            case R.id.leaderboard_menu_newGame:
                startActivity(new Intent(getApplicationContext(), GameActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
