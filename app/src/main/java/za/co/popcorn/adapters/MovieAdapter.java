package za.co.popcorn.adapters;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import za.co.popcorn.MainActivity;
import za.co.popcorn.R;
import za.co.popcorn.fragments.MovieFrag;
import za.co.popcorn.obj.Movie;
import za.co.popcorn.utils.FragmentUtils;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder>
{
    private final String TAG = "MovieAdapter";

    private List<Movie> movies;
    private Activity activity;

    public MovieAdapter(Activity activity, List<Movie> movies)
    {
        this.movies = movies;
        this.activity = activity;
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        ImageView imgCoverImage;

        public ViewHolder(View view)
        {
            super(view);

            this.imgCoverImage = view.findViewById(R.id.imgCoverImage);

            ViewGroup.MarginLayoutParams marginParams = (ViewGroup.MarginLayoutParams) this.imgCoverImage.getLayoutParams();
            marginParams.setMargins(10, 0, 10, 0);
            this.imgCoverImage.setLayoutParams(marginParams);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.horizontal_movie_list_item, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position)
    {
        Picasso.get()
                .load(this.movies.get(position).getCoverImage())
                .into(holder.imgCoverImage);

        holder.imgCoverImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {

                JSONObject movie = movies.get(position).getJSONObject();

                Bundle bundle = new Bundle();
                bundle.putString("movie", movie.toString());

                MovieFrag movieFrag = new MovieFrag();
                movieFrag.setArguments(bundle);

                FragmentUtils.startFragment(((MainActivity)activity).getSupportFragmentManager(), movieFrag, R.id.fragContainer, ((MainActivity)activity).getSupportActionBar(), movies.get(position).getTitle(), true, false, true, null);
            }
        });
    }

    @Override
    public int getItemCount()
    {
        int toReturn = 0;

        if(this.movies != null)
        {
            toReturn = this.movies.size();
        }
        return toReturn;
    }
}
