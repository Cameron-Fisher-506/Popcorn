package za.co.popcorn.menu;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import za.co.popcorn.MainActivity;
import za.co.popcorn.R;
import za.co.popcorn.adapters.MovieAdapter;
import za.co.popcorn.obj.Movie;
import za.co.popcorn.utils.ConstantUtils;
import za.co.popcorn.utils.FlagUtils;
import za.co.popcorn.utils.SQLiteUtils;

import java.util.Collections;
import java.util.List;

public class HomeFrag extends Fragment
{
    private TextView txtNotify;
    private TextView txtNotifyUpdatingMovies;
    private SQLiteUtils sqLiteUtils;
    private Spinner spinnerMovieSort;

    private GridLayoutManager layoutManager;
    private RecyclerView recyclerView;
    private MovieAdapter movieAdapter;
    private List<Movie> movies;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_home, container, false);

        this.sqLiteUtils = new SQLiteUtils(getContext());

        wireUI(view);
        checkUpdatingMovies(view);
        initMovieAdapter(view);

        return view;
    }

    private void checkUpdatingMovies(View view)
    {
        this.txtNotifyUpdatingMovies = (TextView) view.findViewById(R.id.txtNotifyUpdatingMovies);
        if(!FlagUtils.updatingMovies)
        {
            this.txtNotifyUpdatingMovies.setVisibility(View.GONE);
        }else
        {
            this.txtNotifyUpdatingMovies.setVisibility(View.VISIBLE);
        }
    }

    private void initMovieAdapter(View view)
    {
        this.layoutManager = new GridLayoutManager(getContext(), 2);
        this.layoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        this.recyclerView = view.findViewById(R.id.movieRecyclerView);
        this.recyclerView.setLayoutManager(layoutManager);

        this.movies = this.sqLiteUtils.getMovies();
        Collections.sort(movies);

        this.movieAdapter = new MovieAdapter((MainActivity)getActivity(), movies);
        this.recyclerView.setAdapter(movieAdapter);

    }

    private void wireUI(View view)
    {
        this.txtNotify = (TextView) view.findViewById(R.id.txtNotify);
        if(ConstantUtils.GLOBAL_POPCORN_URL != null)
        {
            this.txtNotify.setVisibility(View.GONE);
        }else
        {
            this.txtNotify.setVisibility(View.VISIBLE);
        }

        this.spinnerMovieSort = view.findViewById(R.id.sortMovie);
        ArrayAdapter<CharSequence> arrayAdapter = ArrayAdapter.createFromResource(getContext(), R.array.movieSort, R.layout.support_simple_spinner_dropdown_item);
        arrayAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        this.spinnerMovieSort.setAdapter(arrayAdapter);
        this.spinnerMovieSort.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                if(position == 0)
                {
                    //year: Newest
                    Collections.sort(movies, Movie.sortByYearDescending);

                }else if(position == 1)
                {
                    //Year: Oldest
                    Collections.sort(movies, Movie.sortByYearAscending);
                }else if(position == 2)
                {
                    //Title: A-Z
                    Collections.sort(movies, Movie.sortByTitleAscending);
                }else if(position == 3)
                {
                    //Tile: Z-A
                    Collections.sort(movies, Movie.sortByTitleDescending);
                }else if(position == 4)
                {
                    //Rating: Highest
                    Collections.sort(movies, Movie.sortByRatingDescending);
                }else if(position == 5)
                {
                    //Rating: Lowest
                    Collections.sort(movies, Movie.sortByRatingAscending);
                }

                movieAdapter.notifyDataSetChanged();
                recyclerView.setAdapter(movieAdapter);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
}
