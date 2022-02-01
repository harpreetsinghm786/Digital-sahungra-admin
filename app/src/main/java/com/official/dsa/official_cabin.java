package com.official.dsa;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.tabs.TabLayout;

public class official_cabin extends AppCompatActivity {

    ViewPager viewPager;

    private TabLayout tabLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_official_cabin);

        Toolbar toolbar=findViewById(R.id.official_cabin_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            toolbar.getNavigationIcon().setTint(getResources().getColor(R.color.color2));
        }

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        tabLayout = findViewById(R.id.tabLayout);
        viewPager = findViewById(R.id.tab_bar_view_pager);
        tabLayout.setupWithViewPager(viewPager);
        setupViewPager(viewPager);

    }

    private void setupViewPager(ViewPager viewPager) {
        Tab_layout_adapter tab_layout_adapter = new Tab_layout_adapter(getSupportFragmentManager());
        tab_layout_adapter.addFragment(new forms(), "Forms");
        tab_layout_adapter.addFragment(new books(), "Books");
        viewPager.setAdapter(tab_layout_adapter);
    }


}
