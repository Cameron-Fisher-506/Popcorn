package com.example.popcorn.menu;

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
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.popcorn.MainActivity;
import com.example.popcorn.R;
import com.example.popcorn.fragments.MovieFrag;
import com.example.popcorn.obj.Movie;
import com.example.popcorn.utils.FragmentUtils;
import com.example.popcorn.utils.SQLiteUtils;
import com.squareup.picasso.Picasso;
import org.json.JSONObject;
import java.util.ArrayList;

public class SearchFrag extends Fragment
{
    private SearchView svMovie;
    private ListView lvMovies;
    private ArrayList<Movie> movies;
    private MovieAdapter movieAdapter;
    private SQLiteUtils sqLiteUtils;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_search, container, false);

        //set views
        setSvMovie(view);

        //get movies from sqlite
        this.sqLiteUtils = new SQLiteUtils(getActivity());

        setMovies(this.sqLiteUtils.getMovies());

        setMovieAdapter(getMovies());

        setLvMovies(view, getMovieAdapter());

        addLvMoviesListener();

        addSvMovieListener();

        return view;
    }

    private void addSvMovieListener()
    {
        this.svMovie.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query)
            {

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText)
            {

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

                Bundle bundle = new Bundle();
                bundle.putString("movie", jsonObjectMovie.toString());

                MovieFrag movieFrag = new MovieFrag();
                movieFrag.setArguments(bundle);

                FragmentUtils.startFragment(((MainActivity)getActivity()).getSupportFragmentManager(), movieFrag, R.id.fragContainer, ((MainActivity)getActivity()).getSupportActionBar(), movie.getTitle(), true, false, true, null);
            }
        });
    }

    private void setSvMovie(View view)
    {
        this.svMovie = view.findViewById(R.id.svMovie);
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

    private void setLvMovies(View view, MovieAdapter movieAdapter)
    {
        this.lvMovies = view.findViewById(R.id.lvMovies);
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
        private MovieAdapter.ItemFilter itemFilter;


        private class ItemFilter extends Filter {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {

                String filterString = constraint.toString().toLowerCase().trim();

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
            this.itemFilter = new MovieAdapter.ItemFilter();
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
