package za.co.popcorn;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import za.co.popcorn.menu.ComingSoonFrag;
import za.co.popcorn.menu.HomeFrag;
import za.co.popcorn.menu.MenuFrag;
import za.co.popcorn.menu.RefreshFrag;
import za.co.popcorn.menu.SearchFrag;
import za.co.popcorn.services.UpdatePiAddressService;
import za.co.popcorn.utils.ConstantUtils;
import za.co.popcorn.utils.FragmentUtils;
import za.co.popcorn.utils.GeneralUtils;
import za.co.popcorn.utils.SharedPreferencesUtils;

import org.json.JSONObject;

public class MainActivity extends AppCompatActivity
{

    private ImageButton btnSearch;
    private ImageButton btnRefresh;
    private ImageButton btnHome;
    private ImageButton btnMenu;
    private ImageButton btnComingSoon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //set the custom toolbar
        wireUI();

        startUpdatePiAddressService();

        addBtnRefreshListener();
        addBtnSearchListener();
        addBtnHomeListener();
        addBtnMenuListner();
        addBtnComingSoon();

        HomeFrag homeFrag = new HomeFrag();
        FragmentUtils.startFragment(getSupportFragmentManager(), homeFrag, R.id.fragContainer, getSupportActionBar(), "Home", true, false, true, null);

        setNavIcons(true, false, false, false);
    }

    private void startUpdatePiAddressService()
    {
        try
        {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("isStart", true);

            SharedPreferencesUtils.save(this,SharedPreferencesUtils.AUTO_UPDATE_PI_ADDRESS, jsonObject);

            startService(new Intent(this, UpdatePiAddressService.class));
        }catch(Exception e)
        {
            Log.e(ConstantUtils.TAG, "\nError: " + e.getMessage()
                    + "\nMethod: MainActivity - startUpdatePiAddressService"
                    + "\nCreatedTime: " + GeneralUtils.getCurrentDateTime());
        }

    }

    private void wireUI()
    {
        Toolbar toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
    }

    private void addBtnComingSoon()
    {
        this.btnComingSoon = (ImageButton) findViewById(R.id.btnComingSoon);
        this.btnComingSoon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setNavIcons(false, false, false, false);

                ComingSoonFrag comingSoonFrag = new ComingSoonFrag();
                FragmentUtils.startFragment(getSupportFragmentManager(), comingSoonFrag, R.id.fragContainer, getSupportActionBar(), "Coming Soon", true, false, true, null);
            }
        });
    }

    private void addBtnRefreshListener()
    {
        this.btnRefresh = (ImageButton) findViewById(R.id.btnRefresh);
        btnRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                setNavIcons(false, false, true, false);
                RefreshFrag refreshFrag = new RefreshFrag();
                FragmentUtils.startFragment(getSupportFragmentManager(), refreshFrag, R.id.fragContainer, getSupportActionBar(), "Refresh Movies", true, false, true, null);
            }
        });
    }

    private void addBtnSearchListener()
    {
        this.btnSearch = findViewById(R.id.btnSearch);
        this.btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setNavIcons(false, true, false, false);
                SearchFrag searchFrag = new SearchFrag();
                FragmentUtils.startFragment(getSupportFragmentManager(), searchFrag,R.id.fragContainer, getSupportActionBar(), "Search", true, false, true, null);
            }
        });
    }

    private void addBtnHomeListener()
    {
        this.btnHome = findViewById(R.id.btnHome);
        this.btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setNavIcons(true, false, false, false);
                HomeFrag homeFrag = new HomeFrag();
                FragmentUtils.startFragment(getSupportFragmentManager(), homeFrag, R.id.fragContainer, getSupportActionBar(), "Home", true, false, true, null);
            }
        });
    }

    private void addBtnMenuListner()
    {
        this.btnMenu = findViewById(R.id.btnMenu);
        this.btnMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setNavIcons(false, false, false, true);
                MenuFrag menuFrag = new MenuFrag();
                FragmentUtils.startFragment(getSupportFragmentManager(), menuFrag, R.id.fragContainer, getSupportActionBar(), "Menu", true, false, true, null);
            }
        });
    }

    public void setNavIcons(boolean isHome, boolean isSearch, boolean isRefresh, boolean isMenu)
    {
        if(isHome)
        {
            this.btnHome.setBackgroundResource(R.drawable.home);
        }else
        {
            this.btnHome.setBackgroundResource(R.drawable.home_blank);
        }

        if(isSearch)
        {
            this.btnSearch.setBackgroundResource(R.drawable.search);
        }else
        {
            this.btnSearch.setBackgroundResource(R.drawable.search_blank);
        }

        if(isRefresh)
        {
            this.btnRefresh.setBackgroundResource(R.drawable.refresh);
        }else
        {
            this.btnRefresh.setBackgroundResource(R.drawable.refresh_blank);
        }

        if(isMenu)
        {
            this.btnMenu.setBackgroundResource(R.drawable.menu);
        }else
        {
            this.btnMenu.setBackgroundResource(R.drawable.menu_blank);
        }

    }
}
