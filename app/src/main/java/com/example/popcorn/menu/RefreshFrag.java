package com.example.popcorn.menu;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.example.popcorn.R;
import com.example.popcorn.services.AutoRefreshService;
import com.example.popcorn.utils.ConstantUtils;

public class RefreshFrag extends Fragment
{
    //UI
    private Switch switchAutoRefresh;
    private TextView txtNotify;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_refresh, container, false);

        wireUI(view);

        setAutoRefreshListener(view);

        return view;
    }

    private void wireUI(View view)
    {
        this.txtNotify = (TextView) view.findViewById(R.id.txtNotify);
        this.txtNotify.setVisibility(View.GONE);
    }

    private void setAutoRefreshListener(View view)
    {
        this.switchAutoRefresh = (Switch) view.findViewById(R.id.switchAutoRefresh);
        this.switchAutoRefresh.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                if(ConstantUtils.GLOBAL_POPCORN_URL != null)
                {
                    txtNotify.setVisibility(View.GONE);

                    if(isChecked)
                    {
                        startService();
                    }else
                    {
                        stopService();
                    }

                }else
                {
                    stopService();
                    txtNotify.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    private void startService()
    {
        Intent autoRefreshService = new Intent(getActivity(), AutoRefreshService.class);
        getActivity().startService(autoRefreshService);

        this.switchAutoRefresh.setChecked(true);
    }

    private void stopService()
    {
        Intent autoRefreshService = new Intent(getActivity(), AutoRefreshService.class);
        getActivity().stopService(autoRefreshService);

        this.switchAutoRefresh.setChecked(false);
    }
}
