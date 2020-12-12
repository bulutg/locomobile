package com.g10.locomobile.admin;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;

import com.g10.locomobile.BaseActivity;
import com.g10.locomobile.HomeActivity;
import com.g10.locomobile.R;
import com.g10.locomobile.models.Findable;
import com.g10.locomobile.models.OnListFragmentInteractionListener;
import com.g10.locomobile.models.Train;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import androidx.core.app.ActivityCompat;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.Toast;

import com.g10.locomobile.ui.main.SectionsPagerAdapter;

/**
 * @author Group 10
 * Admin Activity
 */
public class AdminActivity extends BaseActivity implements Findable, OnListFragmentInteractionListener {

    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    protected FloatingActionButton fab;


    /**
     * On create
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        verifyStoragePermissions(this);

        setContentView(R.layout.activity_admin);
        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);


        fab = (FloatingActionButton) findViewById(R.id.fab);

        showRightFab(tabs.getSelectedTabPosition());

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getBaseContext(), TrainCreateActivity.class);
                startActivity(intent);

                android.os.Process.killProcess(android.os.Process.myPid());
                finish();

            }
        });

        tabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                showRightFab(tab.getPosition());
                System.out.println(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }

    /**
     * Find view by id
     * @param id
     * @return
     */
    @Override
    public Object find(int id) {
        return findViewById(id);
    }

    /**
     * On interaction of fragment list
     * @param o any Object
     */
    @Override
    public void onListFragmentInteraction(Object o) {
        if (o instanceof Train) {
            Toast.makeText(AdminActivity.this, "VALLA OLDU " + (((Train) o).toString()),
                    Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(getBaseContext(), TrainInfoActivity.class);
            intent.putExtra("train", (Train) o);
            intent.putExtra("KEY_user", "admin");
            startActivity(intent);

            android.os.Process.killProcess(android.os.Process.myPid());
            finish();
        }

    }

    /**
     * Activity on resume
     */
    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(getBaseContext(), HomeActivity.class);
        startActivity(intent);
    }

    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }

    /**
     * Show right fab
     * @param tab Tab
     */
    public void showRightFab(int tab) {
        switch (tab) {
            case 0:
                fab.show();
                break;

            default:
                fab.hide();
                break;
        }
    }
}