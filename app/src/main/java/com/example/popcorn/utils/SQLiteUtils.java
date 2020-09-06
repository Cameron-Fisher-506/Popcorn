package com.example.popcorn.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import com.example.popcorn.obj.Movie;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;

public class SQLiteUtils extends SQLiteOpenHelper {

    private static final String DB_NAME = "popcorn.db";
    private static final int DB_VERSION = 1;

    //TABLES
    private static final String MOVIE_TABLE = "movie";

    public SQLiteUtils(Context context)
    {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String createMovieTable = "CREATE TABLE "+MOVIE_TABLE+"(" +
                "id BIGINT," +
                "title VARCHAR(50)," +
                "torrentURL VARCHAR(100)," +
                "year BIGINT," +
                "rating REAL," +
                "coverImage VARCHAR(100)," +
                "description VARCHAR(200)" +
                ")";
        db.execSQL(createMovieTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void cacheMovies(JSONArray movies)
    {
        SQLiteDatabase dbWrite = this.getWritableDatabase();
        SQLiteDatabase dbRead = this.getReadableDatabase();

        try
        {

            //get all cached movies
            HashMap<Long, String> movieMap = new HashMap<Long, String>();

            String query = "SELECT * FROM " + MOVIE_TABLE;
            Cursor cursor = dbRead.rawQuery(query, null, null);
            if(cursor != null && cursor.getCount() > 0)
            {
                for(int i = 0; i < cursor.getCount(); i++)
                {
                    Long id = cursor.getLong(0);
                    String title = cursor.getString(1);

                    movieMap.put(id, title);
                }
            }

            if(movies != null && movies.length() > 0)
            {
                for(int i = 0; i < movies.length(); i++)
                {
                    JSONObject movie = movies.getJSONObject(i);

                    Long id = movie.getLong("id");
                    String title = movie.getString("title");
                    String torrentURL = movie.getString("torrentURL");
                    Long year = movie.getLong("year");
                    double rating = movie.getDouble("rating");
                    String coverImage = movie.getString("coverImage");
                    String description = movie.getString("description");

                    //check if movie is already cached
                    String cachedTitle = movieMap.get(id);
                    if(cachedTitle == null)
                    {
                        ContentValues contentValues = new ContentValues();
                        contentValues.put("id", id);
                        contentValues.put("title", title);
                        contentValues.put("torrentURL", torrentURL);
                        contentValues.put("year", year);
                        contentValues.put("rating", rating);
                        contentValues.put("coverImage", coverImage);
                        contentValues.put("description", description);

                        Log.d(ConstantUtils.TAG, contentValues.toString());

                        dbWrite.insert(MOVIE_TABLE, null, contentValues);
                    }
                }
            }
        }catch (Exception e)
        {
            String message = "\n\nError Message: " + e.getMessage() +
                    "\nClass: SQLiteUtils" +
                    "\nMethod: insertMovie" +
                    "\nCreatedTime: " + GeneralUtils.getCurrentDateTime();
            Log.d(ConstantUtils.TAG, message);
        }finally {
            if(dbWrite != null)
            {
                dbWrite.close();
                dbWrite = null;
            }

            if(dbRead != null)
            {
                dbRead.close();
                dbRead = null;
            }
        }

    }

    public Movie getMovieByTitle(String movieTile)
    {
        Movie toReturn = null;

        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT * FROM " + MOVIE_TABLE + " WHERE title like '%"+movieTile+"%'";
        Cursor cursor = db.rawQuery(query, null, null);
        if(cursor != null && cursor.getCount() > 0)
        {
            cursor.moveToFirst();

            Long id = cursor.getLong(0);
            String title = cursor.getString(1);
            String torrentURL = cursor.getString(2);
            Long year = cursor.getLong(3);
            Double rating = cursor.getDouble(4);
            String coverImage = cursor.getString(5);
            String description = cursor.getString(6);

            toReturn = new Movie(id, title, torrentURL, year, rating, coverImage, description);

        }

        return toReturn;
    }

    public ArrayList<String> getMovieTitles()
    {
        ArrayList<String> toReturn = null;

        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT title FROM " + MOVIE_TABLE;
        Cursor cursor = db.rawQuery(query, null, null);
        while(cursor != null && cursor.getCount() > 0)
        {
            toReturn = new ArrayList<>();
            cursor.moveToFirst();
            for(int i = 0; i < cursor.getCount(); i++)
            {
                String title = cursor.getString(0);
                toReturn.add(title);

                cursor.moveToNext();
            }
        }
        return toReturn;
    }

    public ArrayList<Movie> getMovies()
    {
        ArrayList<Movie> toReturn = null;

        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT * FROM " + MOVIE_TABLE;
        Cursor cursor = db.rawQuery(query, null, null);

        if(cursor != null && cursor.getCount() > 0)
        {
            toReturn = new ArrayList<>();
            cursor.moveToFirst();
            for(int i = 0; i < cursor.getCount(); i++)
            {
                Long id = cursor.getLong(0);
                String title = cursor.getString(1);
                String torrentURL = cursor.getString(2);
                Long year = cursor.getLong(3);
                Double rating = cursor.getDouble(4);
                String coverImage = cursor.getString(5);
                String description = cursor.getString(6);

                Movie movie = new Movie(id, title, torrentURL, year, rating, coverImage, description);
                toReturn.add(movie);

                cursor.moveToNext();
            }
        }

        return toReturn;
    }

    public long getTotalNumberMovies()
    {
        long toReturn = 0;

        try
        {
            String query = "SELECT * FROM "+MOVIE_TABLE+"";

            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursor = db.rawQuery(query, null, null);

            if(cursor != null)
            {
                toReturn = cursor.getCount();
                cursor.close();
            }

        }catch(Exception e)
        {
            Log.d(ConstantUtils.TAG,"Error: " + e.getMessage()
            + "Method: SQLiteUtils - getTotalNumberMovies"
            + "CreatedTime: " + GeneralUtils.getCurrentDateTime());
        }

        return toReturn;
    }
}
