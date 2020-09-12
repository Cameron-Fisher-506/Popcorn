package com.example.popcorn.menu;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.example.popcorn.R;
import com.example.popcorn.services.AutoRefreshService;


public class RefreshFrag extends Fragment
{
    //UI
    private Switch switchAutoRefresh;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_refresh, container, false);

        setAutoRefreshListener(view);

        return view;
    }

    private void setAutoRefreshListener(View view)
    {
        this.switchAutoRefresh = (Switch) view.findViewById(R.id.switchAutoRefresh);
        this.switchAutoRefresh.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                if(isChecked)
                {
                    Intent autoRefreshService = new Intent(getActivity(), AutoRefreshService.class);
                    getActivity().startService(autoRefreshService);
                }else
                {
                    Intent autoRefreshService = new Intent(getActivity(), AutoRefreshService.class);
                    getActivity().stopService(autoRefreshService);
                }
            }
        });
    }


}
