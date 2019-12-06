package com.example.hangman;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;

public class HangmanDB {
    // database constants
    public static final String DB_NAME = "Hangman.sqlite";
    public static final int    DB_VERSION = 1;
    public static final String POKEMON_TABLE_NAME = "Pokemon";
    public static final String Leaderboard_TABLE_NAME = "Leaderboard";

    private static class DBHelper extends SQLiteOpenHelper {

        public DBHelper(Context context, String name,
                        SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            // create tables
            db.execSQL("CREATE TABLE Pokemon (id VARCHAR PRIMARY KEY NOT NULL , name VARCHAR NOT NULL)");
            db.execSQL("CREATE TABLE Leaderboard (id INTEGER PRIMARY KEY  AUTOINCREMENT  NOT NULL , name VARCHAR NOT NULL , score INTEGER NOT NULL, guessed INTEGER NOT NULL)");
            SeedPokemon.populatePokemon(db);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db,
                              int oldVersion, int newVersion) {

            db.execSQL("DROP TABLE \"Pokemon\"");
            db.execSQL("DROP TABLE \"Leaderboard\"");
            Log.d("Task list", "Upgrading db from version "
                    + oldVersion + " to " + newVersion);

            onCreate(db);
        }
    }

    // database and database helper objects
    private SQLiteDatabase db;
    private DBHelper dbHelper;

    // constructor
    public HangmanDB(Context context) {
        dbHelper = new DBHelper(context, DB_NAME, null, DB_VERSION);
        openWriteableDB();
        closeDB();
    }
    // private methods
    private void openReadableDB() {
        db = dbHelper.getReadableDatabase();
    }
    private void openWriteableDB() {
        db = dbHelper.getWritableDatabase();
    }
    private void closeDB() {
        if (db != null)
            db.close();
    }

    ArrayList<Pokemon> getPokemon(){
        ArrayList<Pokemon> data = new ArrayList<Pokemon>();
        openReadableDB();
        Cursor cursor = db.rawQuery("SELECT id, name FROM Pokemon ORDER BY Random()",null );
        while (cursor.moveToNext()) {
            Pokemon map = new Pokemon();
            map.put("id", cursor.getString(0));
            map.put("name", cursor.getString(1));
            data.add(map);
        }
        if (cursor != null)
            cursor.close();
        closeDB();
        return data;
    }

    ArrayList<HashMap<String, String>> getLeaderboardStandings(){
        ArrayList<HashMap<String,String>> data =
                new ArrayList<HashMap<String, String>>();
        openReadableDB();
        Cursor cursor = db.rawQuery("SELECT id, name, score FROM Leaderboard ORDER BY score DESC", null);
        while (cursor.moveToNext()){
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("id", cursor.getString(0));
            map.put("name", cursor.getString(1));
            map.put("score", cursor.getString(2));
            data.add(map);
        }
        if (cursor != null){
            cursor.close();
        }
        closeDB();
        return data;
    }
}
