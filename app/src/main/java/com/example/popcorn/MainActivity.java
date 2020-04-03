package com.example.popcorn;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.popcorn.activity.MovieActivity;
import com.example.popcorn.activity.SearchActivity;
import com.example.popcorn.tasks.HttpGETTask;
import com.example.popcorn.utils.SQLiteUtils;

import org.json.JSONArray;


public class MainActivity extends Activity {

    private SQLiteUtils sqLiteUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.sqLiteUtils = new SQLiteUtils(this);

        Button btnRefresh = findViewById(R.id.btnRefresh);
        btnRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try
                {
                    HttpGETTask httpGETTask = new HttpGETTask();
                    JSONArray movies = httpGETTask.execute().get();
                    sqLiteUtils.cacheMovies(movies);

                }catch(Exception e)
                {
                    e.printStackTrace();
                }
            }
        });

        Button btnSearch = findViewById(R.id.btnSearch);
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent searchIntent = new Intent(getApplicationContext(), SearchActivity.class);
                startActivity(searchIntent);
            }
        });

    }
}
