package za.co.popcorn.menu;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import za.co.popcorn.R;
import za.co.popcorn.utils.FlagUtils;


public class ComingSoonFrag extends Fragment
{

    private TextView txtNotifyUpdatingMovies;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_coming_soon, container, false);

        checkUpdatingMovies(view);

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
}
