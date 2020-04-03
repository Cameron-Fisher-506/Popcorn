package com.example.popcorn.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.example.popcorn.R;
import com.example.popcorn.obj.Movie;

import java.util.List;

public class MovieAdapter extends BaseAdapter {

    List<Movie> movies;
    Context context;

    public MovieAdapter(Context context, List<Movie> movies)
    {
        this.movies = movies;
        this.context = context;
    }

    @Override
    public int getCount() {
        return this.movies.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        return null;
    }
}
