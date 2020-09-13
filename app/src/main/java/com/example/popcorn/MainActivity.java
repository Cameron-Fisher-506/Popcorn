package com.example.popcorn;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.example.popcorn.menu.ComingSoonFrag;
import com.example.popcorn.menu.HomeFrag;
import com.example.popcorn.menu.MenuFrag;
import com.example.popcorn.menu.RefreshFrag;
import com.example.popcorn.menu.SearchFrag;
import com.example.popcorn.services.UpdatePiAddressService;
import com.example.popcorn.utils.FragmentUtils;

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

        startService(new Intent(this, UpdatePiAddressService.class));

        addBtnRefreshListener();
        addBtnSearchListener();
        addBtnHomeListener();
        addBtnMenuListner();
        addBtnComingSoon();

        HomeFrag homeFrag = new HomeFrag();
        FragmentUtils.startFragment(getSupportFragmentManager(), homeFrag, R.id.fragContainer, getSupportActionBar(), "Home", true, false, true, null);

        setNavIcons(true, false, false, false);
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
