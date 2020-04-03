package com.example.popcorn.activity;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import com.example.popcorn.R;
import com.example.popcorn.obj.Movie;
import com.example.popcorn.utils.SQLiteUtils;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;



public class SearchActivity extends Activity {

    private SearchView svMovie;
    private ListView lvMovies;
    private ArrayList<Movie> movies;
    private MovieAdapter movieAdapter;
    private SQLiteUtils sqLiteUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        //set views
        setSvMovie();

        //get movies from sqlite
        this.sqLiteUtils = new SQLiteUtils(this);

        setMovies(this.sqLiteUtils.getMovies());

        setMovieAdapter(getMovies());

        setLvMovies(getMovieAdapter());

        addLvMoviesListener();

        addSvMovieListener();



    }

    private void addSvMovieListener()
    {
        this.svMovie.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                movieAdapter.getFilter().filter(newText);

                return false;
            }
        });
    }

    private void addLvMoviesListener()
    {
        this.lvMovies.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapter, View view, int position, long id) {

                Movie movie = (Movie) adapter.getItemAtPosition(position);
                JSONObject jsonObjectMovie = movie.getJSONObject();

                Intent movieIntent = new Intent(getApplicationContext(), MovieActivity.class);
                movieIntent.putExtra("movie", jsonObjectMovie.toString());
                startActivity(movieIntent);
            }
        });
    }

    private void setSvMovie()
    {
        this.svMovie = findViewById(R.id.svMovie);
        this.svMovie.setIconifiedByDefault(true);
        this.svMovie.setFocusable(true);
        this.svMovie.setIconified(false);
    }

    private void setMovies(ArrayList<Movie> movies)
    {
        this.movies = movies;
    }

    private ArrayList<Movie> getMovies()
    {
        return this.movies;
    }

    private void setLvMovies(MovieAdapter movieAdapter)
    {
        this.lvMovies = findViewById(R.id.lvMovies);
        this.lvMovies.setAdapter(movieAdapter);
    }

    private void setMovieAdapter(ArrayList<Movie> movies)
    {
        this.movieAdapter = new MovieAdapter(movies);
    }

    private MovieAdapter getMovieAdapter()
    {
        return this.movieAdapter;
    }


    private class MovieAdapter extends BaseAdapter implements Filterable {

        private ArrayList<Movie> movies;
        private ArrayList<Movie> filteredData;
        private ItemFilter itemFilter;


        private class ItemFilter extends Filter {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {

                String filterString = constraint.toString().toLowerCase();

                FilterResults results = new FilterResults();

                final ArrayList<Movie> list = movies;

                int count = list.size();
                final ArrayList<Movie> searchList = new ArrayList<>(count);

                String filterableString;

                for (int i = 0; i < count; i++) {
                    filterableString = list.get(i).getTitle();
                    if (filterableString.toLowerCase().contains(filterString)) {
                        searchList.add(list.get(i));
                    }
                }

                results.values = searchList;
                results.count = searchList.size();

                return results;
            }

            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                filteredData = (ArrayList<Movie>) results.values;
                notifyDataSetChanged();
            }

        }

        public Filter getFilter() {
            return this.itemFilter;
        }

        public MovieAdapter(ArrayList<Movie> movies)
        {
            this.movies = movies;
            this.filteredData = movies;
            this.itemFilter = new ItemFilter();
        }

        @Override
        public int getCount() {
            return this.filteredData.size();
        }

        @Override
        public Object getItem(int position) {
            return this.filteredData.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            if(convertView == null)
            {
                convertView =  getLayoutInflater().inflate(R.layout.search_movie_list_item, null);
            }

            ImageView imgCoverImage = convertView.findViewById(R.id.imgCoverImage);
            Picasso.get()
                    .load(this.filteredData.get(position).getCoverImage())
                    .resize(150, 200)
                    .into(imgCoverImage);

            TextView txtTitle = convertView.findViewById(R.id.txtTitle);
            txtTitle.setText(this.filteredData.get(position).getTitle());

            TextView txtYear = convertView.findViewById(R.id.txtYear);
            String year = Long.toString(this.filteredData.get(position).getYear());
            txtYear.setText(year);

            TextView txtRating = convertView.findViewById(R.id.txtRating);
            String rating = "IMDb " + this.filteredData.get(position).getRating();
            txtRating.setText(rating);

            return convertView;
        }


    }

}



