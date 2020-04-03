package com.example.popcorn.activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.popcorn.R;
import com.example.popcorn.obj.Movie;
import com.example.popcorn.tasks.HttpPOSTTask;
import com.example.popcorn.utils.ConstantUtils;
import com.example.popcorn.utils.GeneralUtils;
import com.example.popcorn.utils.SQLiteUtils;
import com.example.popcorn.utils.WSCallsUtils;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

public class MovieActivity extends Activity {

    private SQLiteUtils sqLiteUtils;
    private Movie movie;

    //views
    private ImageView imgCoverImage;
    private TextView txtTitle;
    private TextView txtYear;
    private TextView txtRating;
    private TextView txtDescription;
    private Button btnDownload;
    private ImageView imgIMDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie);

        //get movie from intent
        Intent intent = getIntent();
        String strMovie = intent.getStringExtra("movie");
        if(strMovie != null)
        {
            try
            {
                JSONObject jsonObjectMovie = new JSONObject(strMovie);
                Long id = jsonObjectMovie.getLong("id");
                String title = jsonObjectMovie.getString("title");
                String torrentURL = jsonObjectMovie.getString("torrentURL");
                Long year = jsonObjectMovie.getLong("year");
                Double rating = jsonObjectMovie.getDouble("rating");
                String coverImage = jsonObjectMovie.getString("coverImage");
                String description = jsonObjectMovie.getString("description");

                this.movie = new Movie(id, title, torrentURL, year, rating, coverImage, description);

                //set views
                setImgCoverImage();
                setTxtTitle();
                setTxtYear();
                setTxtRating();
                setTxtDescription();
                setBtnDownload();

                //set onClick listeners
                btnDownloadListener();
            }catch (Exception e)
            {
                String message = "\n\nError Message: " + e.getMessage() +
                        "\nMethod: onCreate" +
                        "\nClass: MovieActivity" +
                        "\nCreatedTime: " + GeneralUtils.getCurrentDateTime();
                Log.d(ConstantUtils.TAG, message);
            }

        }else
        {
            // go back to search intent
            Intent searchIntent = new Intent(this, SearchActivity.class);
            startActivity(searchIntent);

        }
    }

    public void setImgCoverImage() {
        this.imgCoverImage = findViewById(R.id.imgCoverImage);
        Picasso.get()
                .load(this.movie.getCoverImage())
                .resize(150, 200)
                .into(this.imgCoverImage);
    }

    public void setTxtTitle() {
        this.txtTitle = findViewById(R.id.txtTitle);
        this.txtTitle.setText(this.movie.getTitle());
    }

    public void setTxtYear() {
        this.txtYear = findViewById(R.id.txtYear);

        String year = Long.toString(this.movie.getYear());
        this.txtYear.setText(year);
    }

    public void setTxtRating() {
        this.txtRating = findViewById(R.id.txtRating);

        String rating = "IMDb " + this.movie.getRating();
        this.txtRating.setText(rating);
    }

    public void setTxtDescription() {
        this.txtDescription = findViewById(R.id.txtDescription);
        this.txtDescription.setText(this.movie.getDescription());
    }

    public void setBtnDownload() {
        this.btnDownload = findViewById(R.id.btnDownload);
    }

    public void btnDownloadListener()
    {
        this.btnDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try
                {
                    GeneralUtils.makeToast(getApplicationContext(), "Downloading...");

                    JSONObject jsonObject = new JSONObject();
                    String url = movie.getTorrentURL();
                    String magnetCode = url.replace("https://yts.mx/torrent/download/", "");
                    jsonObject.put("torrentURL", magnetCode);

                    HttpPOSTTask httpPOSTTask = new HttpPOSTTask();
                    JSONObject response = httpPOSTTask.execute(ConstantUtils.GLOBAL_POPCORN_WS_DOWNLOAD_TORRENT_URL, jsonObject.toString()).get();

                }catch(Exception e)
                {
                    String message = "\n\nError Message: " + e.getMessage() +
                            "\nURL: " + ConstantUtils.GLOBAL_POPCORN_WS_DOWNLOAD_TORRENT_URL +
                            "\nMethod: genericGET" +
                            "\nCreatedTime: " + GeneralUtils.getCurrentDateTime();
                    Log.d(ConstantUtils.TAG, message);
                }

            }
        });
    }
}
