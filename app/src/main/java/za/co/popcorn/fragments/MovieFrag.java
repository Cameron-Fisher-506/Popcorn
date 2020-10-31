package za.co.popcorn.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import za.co.popcorn.MainActivity;
import za.co.popcorn.R;
import za.co.popcorn.menu.SearchFrag;
import za.co.popcorn.obj.Movie;
import za.co.popcorn.utils.ConstantUtils;
import za.co.popcorn.utils.FragmentUtils;
import za.co.popcorn.utils.GeneralUtils;
import za.co.popcorn.utils.WSCallsUtils;
import za.co.popcorn.utils.WSCallsUtilsTaskCaller;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

public class MovieFrag extends Fragment
{
    private final int REQ_CODE_DOWNLOAD = 101;

    private Movie movie;

    //views
    private ImageView imgCoverImage;
    private TextView txtTitle;
    private TextView txtYear;
    private TextView txtRating;
    private TextView txtDescription;
    private Button btnDownload;
    private ImageView imgIMDb;
    private TextView txtNotify;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_movie, container, false);

        wireUI(view);

        //get movie from intent
        String strMovie = getArguments().getString("movie");
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
                setImgCoverImage(view);
                setTxtTitle(view);
                setTxtYear(view);
                setTxtRating(view);
                setTxtDescription(view);
                setBtnDownload(view);

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
            SearchFrag searchFrag = new SearchFrag();
            FragmentUtils.startFragment(((MainActivity)getActivity()).getSupportFragmentManager(), searchFrag, R.id.fragContainer, ((MainActivity)getActivity()).getSupportActionBar(), "Search", true, false, true, null);

        }

        return view;
    }

    private void wireUI(View view)
    {
        this.txtNotify = (TextView) view.findViewById(R.id.txtNotify);
        this.txtNotify.setVisibility(View.GONE);

    }

    public void setImgCoverImage(View view) {
        this.imgCoverImage = view.findViewById(R.id.imgCoverImage);
        Picasso.get()
                .load(this.movie.getCoverImage())
                .resize(150, 200)
                .into(this.imgCoverImage);
    }

    public void setTxtTitle(View view) {
        this.txtTitle = view.findViewById(R.id.txtTitle);
        this.txtTitle.setText(this.movie.getTitle());
    }

    public void setTxtYear(View view) {
        this.txtYear = view.findViewById(R.id.txtYear);

        String year = Long.toString(this.movie.getYear());
        this.txtYear.setText(year);
    }

    public void setTxtRating(View view) {
        this.txtRating = view.findViewById(R.id.txtRating);

        String rating = Double.toString(this.movie.getRating());
        this.txtRating.setText(rating);
    }

    public void setTxtDescription(View view) {
        this.txtDescription = view.findViewById(R.id.txtDescription);
        this.txtDescription.setText(this.movie.getDescription());
    }

    public void setBtnDownload(View view) {
        this.btnDownload = view.findViewById(R.id.btnDownload);
    }

    public void btnDownloadListener()
    {
        this.btnDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try
                {
                    if(ConstantUtils.GLOBAL_POPCORN_URL != null)
                    {
                        txtNotify.setVisibility(View.GONE);

                        GeneralUtils.makeToast(getActivity(), "Downloading...");

                        JSONObject body = new JSONObject();
                        String url = movie.getTorrentURL();
                        String magnetCode = url.replace("https://yts.mx/torrent/download/", "");
                        body.put("torrentURL", magnetCode);

                        WSCallsUtils.post(new WSCallsUtilsTaskCaller() {
                            @Override
                            public void taskCompleted(String response, int reqCode)
                            {
                                if(response != null)
                                {
                                    if(reqCode == REQ_CODE_DOWNLOAD)
                                    {
                                        GeneralUtils.makeToast(getActivity(), response);
                                    }
                                }else
                                {

                                }
                            }

                            @Override
                            public Activity getActivity() {
                                return null;
                            }
                        }, ConstantUtils.GLOBAL_POPCORN_URL + "/rest/popcorn/download", body.toString(), REQ_CODE_DOWNLOAD);
                    }else
                    {
                        txtNotify.setVisibility(View.VISIBLE);
                    }



                }catch(Exception e)
                {
                    String message = "\n\nError: " + e.getMessage() +
                            "\nMethod: btnDownloadListener" +
                            "\nCreatedTime: " + GeneralUtils.getCurrentDateTime();
                    Log.d(ConstantUtils.TAG, message);
                }

            }
        });
    }
}
