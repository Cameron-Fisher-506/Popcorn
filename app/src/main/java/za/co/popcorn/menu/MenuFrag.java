package za.co.popcorn.menu;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import za.co.popcorn.R;
import za.co.popcorn.services.UpdatePiAddressService;
import za.co.popcorn.utils.ConstantUtils;
import za.co.popcorn.utils.GeneralUtils;
import za.co.popcorn.utils.SharedPreferencesUtils;

import org.json.JSONObject;

public class MenuFrag extends Fragment
{
    private Switch switchUpdatePiAddress;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_menu, container, false);

        addSwitchUpdatePiAddressListener(view);
        setSwitchUpdatePiAddress(view);

        return view;
    }

    private void addSwitchUpdatePiAddressListener(View view)
    {
        this.switchUpdatePiAddress = (Switch) view.findViewById(R.id.switchUpdatePiAddress);
        this.switchUpdatePiAddress.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                try
                {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("isStart", isChecked);

                    SharedPreferencesUtils.save(getContext(),SharedPreferencesUtils.AUTO_UPDATE_PI_ADDRESS, jsonObject);

                    Intent updatePiAddressService = new Intent(getActivity(), UpdatePiAddressService.class);
                    if(isChecked)
                    {
                        getActivity().startService(updatePiAddressService);
                    }else
                    {
                        getActivity().stopService(updatePiAddressService);
                    }
                }catch(Exception e)
                {
                    Log.e(ConstantUtils.TAG, "\nError: " + e.getMessage()
                            + "\nMethod: MenuFrag - addSwitchUpdatePiAddressListener"
                            + "\nCreatedTime: " + GeneralUtils.getCurrentDateTime());
                }

            }
        });
    }

    private void setSwitchUpdatePiAddress(View view)
    {
        try
        {
            JSONObject jsonObject = SharedPreferencesUtils.get(getContext(), SharedPreferencesUtils.AUTO_UPDATE_PI_ADDRESS);
            if(jsonObject != null && jsonObject.has("isStart"))
            {
                Boolean isStart = jsonObject.getBoolean("isStart");
                if(isStart != null)
                {
                    this.switchUpdatePiAddress.setChecked(isStart);
                }
            }
        }catch(Exception e)
        {
            Log.e(ConstantUtils.TAG, "\nError: " + e.getMessage()
                    + "\nMethod: MenuFrag - setSwitchUpdatePiAddress"
                    + "\nCreatedTime: " + GeneralUtils.getCurrentDateTime());
        }

    }
}
