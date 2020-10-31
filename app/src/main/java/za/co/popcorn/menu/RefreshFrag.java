package za.co.popcorn.menu;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import za.co.popcorn.R;
import za.co.popcorn.services.AutoRefreshService;
import za.co.popcorn.utils.ConstantUtils;
import za.co.popcorn.utils.GeneralUtils;
import za.co.popcorn.utils.SharedPreferencesUtils;

import org.json.JSONObject;

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
        setSwitchAutoRefresh();

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
                txtNotify.setVisibility(View.GONE);

                try
                {
                    JSONObject autoRefresh = new JSONObject();
                    autoRefresh.put("isStart", isChecked);

                    SharedPreferencesUtils.save(getContext(), SharedPreferencesUtils.AUTO_REFRESH, autoRefresh);

                    if(isChecked)
                    {
                        startService();
                    }else
                    {
                        stopService();
                    }
                }catch(Exception e)
                {
                    Log.e(ConstantUtils.TAG, "\nError: " + e.getMessage()
                            + "\nMethod: RefreshFrag - setAutoRefreshListener"
                            + "\nCreatedTime: " + GeneralUtils.getCurrentDateTime());
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

    private void setSwitchAutoRefresh()
    {
        JSONObject jsonObject =  SharedPreferencesUtils.get(getContext(), SharedPreferencesUtils.AUTO_REFRESH);
        if(jsonObject != null && jsonObject.has("isStart"))
        {
            try
            {
                Boolean isAutoTrade = jsonObject.getBoolean("isStart");
                if(isAutoTrade != null)
                {
                    this.switchAutoRefresh.setChecked(isAutoTrade);
                }

            }catch(Exception e)
            {
                Log.e(ConstantUtils.TAG, "\nError: " + e.getMessage()
                        + "\nMethod: AutoTradeFrag - setSwitchAutoRefresh"
                        + "\nCreatedTime: " + GeneralUtils.getCurrentDateTime());
            }

        }else
        {
            this.switchAutoRefresh.setChecked(false);
        }
    }
}
