package com.example.popcorn;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.popcorn.menu.HomeFrag;
import com.example.popcorn.menu.MenuFrag;
import com.example.popcorn.menu.RefreshFrag;
import com.example.popcorn.menu.SearchFrag;
import com.example.popcorn.utils.FragmentUtils;

public class MainActivity extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //set the custom toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);

        HomeFrag homeFrag = new HomeFrag();
        FragmentUtils.startFragment(getSupportFragmentManager(), homeFrag, R.id.fragContainer, getSupportActionBar(), "Home", true, false, true, null);

        addBtnRefreshListener();
        addBtnSearchListener();
        addBtnHomeListener();
        addBtnMenuListner();
    }

    private void addBtnRefreshListener()
    {
        ImageButton btnRefresh = findViewById(R.id.btnRefresh);
        btnRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                RefreshFrag refreshFrag = new RefreshFrag();
                FragmentUtils.startFragment(getSupportFragmentManager(), refreshFrag, R.id.fragContainer, getSupportActionBar(), "Refresh Movies", true, false, true, null);
            }
        });
    }

    private void addBtnSearchListener()
    {
        ImageButton btnSearch = findViewById(R.id.btnSearch);
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SearchFrag searchFrag = new SearchFrag();
                FragmentUtils.startFragment(getSupportFragmentManager(), searchFrag,R.id.fragContainer, getSupportActionBar(), "Search", true, false, true, null);
            }
        });
    }

    private void addBtnHomeListener()
    {
        ImageButton btnHome = findViewById(R.id.btnHome);
        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HomeFrag homeFrag = new HomeFrag();
                FragmentUtils.startFragment(getSupportFragmentManager(), homeFrag, R.id.fragContainer, getSupportActionBar(), "Home", true, false, true, null);
            }
        });
    }

    private void addBtnMenuListner()
    {
        ImageButton btnMenu = findViewById(R.id.btnMenu);
        btnMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MenuFrag menuFrag = new MenuFrag();
                FragmentUtils.startFragment(getSupportFragmentManager(), menuFrag, R.id.fragContainer, getSupportActionBar(), "Menu", true, false, true, null);
            }
        });
    }
}
