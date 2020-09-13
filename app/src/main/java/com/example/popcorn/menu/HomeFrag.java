package com.example.popcorn.menu;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.example.popcorn.R;
import com.example.popcorn.utils.ConstantUtils;

public class HomeFrag extends Fragment
{
    private TextView txtNotify;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_home, container, false);

        wireUI(view);

        return view;
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
    }
}
