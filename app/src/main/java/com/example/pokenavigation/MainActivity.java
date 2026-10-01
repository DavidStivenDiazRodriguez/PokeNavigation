package com.example.pokenavigation;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.pokenavigation.ui.FavoritesFragment;
import com.example.pokenavigation.ui.HomeFragment;
import com.example.pokenavigation.ui.InfoFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            cargarFragment(new HomeFragment());
        }

        initObjects();
        initListener();
    }

    private void initObjects() {
        bottomNavigation = findViewById(R.id.bottomNavigation);
    }

    private void initListener() {
        bottomNavigation.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.navigation_home) {
                cargarFragment(new HomeFragment());
                return true;
            } else if (item.getItemId() == R.id.navigation_favorites) {
                cargarFragment(new FavoritesFragment());
                return true;
            } else if (item.getItemId() == R.id.navigation_info) {
                cargarFragment(new InfoFragment());
                return true;
            }
            return false;
        });
    }

    private void cargarFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragmentContainer, fragment)
                .commit();
    }

    public void marcarMenuInfo() {
        if (bottomNavigation != null) {
            bottomNavigation.getMenu().findItem(R.id.navigation_info).setChecked(true);
        }
    }
}
