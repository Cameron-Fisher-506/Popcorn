package za.co.popcorn.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import za.co.popcorn.obj.Movie;
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
        SQLiteDatabase dbWrite = null;
        SQLiteDatabase dbRead = null;
        Cursor cursor = null;

        try
        {

            dbWrite = this.getWritableDatabase();
            dbRead = this.getReadableDatabase();

            if(dbWrite != null && dbRead != null)
            {
                //get all cached movies
                HashMap<Long, String> movieMap = new HashMap<Long, String>();

                String query = "SELECT * FROM " + MOVIE_TABLE;
                cursor = dbRead.rawQuery(query, null, null);
                if(cursor != null && cursor.getCount() > 0)
                {
                    while(cursor.moveToNext())
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

                        if(movie != null)
                        {
                            Long id = movie.has("id")? movie.getLong("id") : null;
                            String title = movie.has("title") ? movie.getString("title") : null;

                            String torrentURL = null;
                            if(movie.has("torrents"))
                            {
                                JSONArray torrents = movie.getJSONArray("torrents");
                                if(torrents != null && torrents.length() > 0)
                                {
                                    JSONObject torrent = torrents.getJSONObject(0);
                                    if(torrent != null && torrent.has("url"))
                                    {
                                        torrentURL = torrent.getString("url");
                                    }

                                }
                            }

                            Long year = movie.has("year") ? movie.getLong("year") : null;
                            Double rating = movie.has("rating") ? movie.getDouble("rating") : null;
                            String coverImage = movie.has("large_cover_image") ? movie.getString("large_cover_image") : null;
                            String description = movie.has("description_full") ? movie.getString("description_full") : null;

                            //check if movie is already cached
                            ContentValues contentValues = new ContentValues();
                            contentValues.put("id", id);
                            contentValues.put("title", title);
                            contentValues.put("torrentURL", torrentURL);
                            contentValues.put("year", year);
                            contentValues.put("rating", rating);
                            contentValues.put("coverImage", coverImage);
                            contentValues.put("description", description);

                            Log.d(ConstantUtils.TAG, contentValues.toString());

                            if(movieMap != null && movieMap.size() > 0)
                            {
                                String cachedTitle = movieMap.get(id);
                                if(cachedTitle == null)
                                {
                                    dbWrite.insert(MOVIE_TABLE, null, contentValues);
                                }
                            }else
                            {
                                dbWrite.insert(MOVIE_TABLE, null, contentValues);
                            }
                        }
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
            if(dbWrite != null && dbWrite.isOpen())
            {
                dbWrite.close();
                dbWrite = null;
            }

            if(dbRead != null && dbRead.isOpen())
            {
                dbRead.close();
                dbRead = null;
            }

            if(cursor != null)
            {
                cursor.close();
                cursor = null;
            }
        }

    }

    public Movie getMovieByTitle(String movieTile)
    {
        Movie toReturn = null;

        SQLiteDatabase dbRead = null;
        Cursor cursor = null;
        try
        {
            dbRead = this.getReadableDatabase();

            String query = "SELECT * FROM " + MOVIE_TABLE + " WHERE title like '%"+movieTile+"%'";
            cursor = dbRead.rawQuery(query, null, null);
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
        }catch(Exception e)
        {
            Log.d(ConstantUtils.TAG, "\n\nError Message: " + e.getMessage() +
                    "\nClass: SQLiteUtils" +
                    "\nMethod: getMovieByTitle" +
                    "\nCreatedTime: " + GeneralUtils.getCurrentDateTime());
        }finally
        {
            if(dbRead != null && dbRead.isOpen())
            {
                dbRead.close();
                dbRead = null;
            }

            if(cursor != null)
            {
                cursor.close();
                cursor = null;
            }
        }


        return toReturn;
    }

    public ArrayList<String> getMovieTitles()
    {
        ArrayList<String> toReturn = null;

        SQLiteDatabase dbRead = null;
        Cursor cursor = null;
        try
        {
            dbRead = this.getReadableDatabase();

            String query = "SELECT title FROM " + MOVIE_TABLE;
            cursor = dbRead.rawQuery(query, null, null);
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
        }catch(Exception e)
        {
            Log.d(ConstantUtils.TAG, "\n\nError Message: " + e.getMessage() +
                "\nClass: SQLiteUtils" +
                "\nMethod: getMovieTitles" +
                "\nCreatedTime: " + GeneralUtils.getCurrentDateTime());

        }finally {
            if(dbRead != null && dbRead.isOpen())
            {
                dbRead.close();
                dbRead = null;
            }

            if(cursor != null)
            {
                cursor.close();
                cursor = null;
            }
        }

        return toReturn;
    }

    public ArrayList<Movie> getMovies()
    {
        ArrayList<Movie> toReturn = new ArrayList<>();

        SQLiteDatabase dbRead = null;
        Cursor cursor = null;
        try
        {
            dbRead = this.getReadableDatabase();

            if(dbRead != null)
            {
                String query = "SELECT * FROM " + MOVIE_TABLE;
                cursor = dbRead.rawQuery(query, null, null);

                if(cursor != null && cursor.getCount() > 0)
                {
                    while(cursor.moveToNext())
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
                    }
                }
            }

        }catch(Exception e)
        {
            String message = "\n\nError Message: " + e.getMessage() +
                    "\nClass: SQLiteUtils" +
                    "\nMethod: getMovies" +
                    "\nCreatedTime: " + GeneralUtils.getCurrentDateTime();
            Log.d(ConstantUtils.TAG, message);
        }finally
        {
            if(dbRead != null && dbRead.isOpen())
            {
                dbRead.close();
                dbRead = null;
            }

            if(cursor != null)
            {
                cursor.close();
                cursor = null;
            }
        }


        return toReturn;
    }

    public long getTotalNumberMovies()
    {
        long toReturn = 0;

        SQLiteDatabase dbRead = null;
        Cursor cursor = null;
        try
        {
            String query = "SELECT * FROM "+MOVIE_TABLE+"";

            dbRead = this.getReadableDatabase();
            cursor = dbRead.rawQuery(query, null, null);

            if(cursor != null)
            {
                toReturn = cursor.getCount();
            }

        }catch(Exception e)
        {
            Log.d(ConstantUtils.TAG,"Error: " + e.getMessage()
            + "Method: SQLiteUtils - getTotalNumberMovies"
            + "CreatedTime: " + GeneralUtils.getCurrentDateTime());
        }finally {

            if(dbRead != null && dbRead.isOpen())
            {
                dbRead.close();
                dbRead = null;
            }

            if(cursor != null)
            {
                cursor.close();
                cursor = null;
            }
        }

        return toReturn;
    }
}
