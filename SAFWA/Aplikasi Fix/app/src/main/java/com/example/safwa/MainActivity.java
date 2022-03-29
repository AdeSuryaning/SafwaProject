package com.example.safwa;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.safwa.Fragment.BerandaFragment;
import com.example.safwa.Fragment.InfoFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView bottomNav;
    FragmentManager fragmentManager;
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNav = findViewById(R.id.bottom_navigation);

        if (savedInstanceState == null) {
            bottomNav.setSelectedItemId(R.id.navigation_home);
            fragmentManager = getSupportFragmentManager();
            BerandaFragment berandaFragment = new BerandaFragment();
            fragmentManager.beginTransaction()
                    .replace(R.id.fragment_container,berandaFragment)
                    .commit();
        }

        bottomNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                Fragment fragment = null;
                switch (menuItem.getItemId()) {
                    case R.id.navigation_home:
                        fragment = new BerandaFragment();
                        break;
                    case R.id.navigation_info:
                        fragment = new InfoFragment();
                        break;
                }

                if (fragment != null) {
                    fragmentManager = getSupportFragmentManager();
                    fragmentManager.beginTransaction()
                            .replace(R.id.fragment_container, fragment)
                            .commit();
                }

                return true;
            }
        });
    }

    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference = firebaseDatabase.getReference();
    final public static SimpleDateFormat DateDataFormat = new SimpleDateFormat("yyyy-MM-dd", new Locale("tanggal", "TANGGAL"));

}