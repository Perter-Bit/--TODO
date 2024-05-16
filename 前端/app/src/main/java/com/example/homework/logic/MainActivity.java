package com.example.homework.logic;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import android.os.Bundle;
import android.view.MenuItem;
import com.example.homework.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
public class MainActivity extends AppCompatActivity
{
    BottomNavigationView bottomNavigation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bottomNavigation = findViewById(R.id.bottom_navigation);
        // 默认打开 HomeFragment 页面
        openFragment(SmsFragment.newInstance("", ""));
        bottomNavigation.setOnNavigationItemSelectedListener(navigationItemSelectedListener);
    }
    public void openFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }


    BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    if (item.getItemId() == R.id.navigation_home) {
                        openFragment(HomeFragment.newInstance("", ""));
                        return true;
                    }
                    if (item.getItemId() == R.id.navigation_sms) {
                        openFragment(SmsFragment.newInstance("", ""));
                        return true;
                    }
                    if (item.getItemId() == R.id.navigation_notifications)
                    {
                        openFragment(NotificationFragment.newInstance("", ""));
                        return true;
                    }
                    if(item.getItemId() == R.id.navigation_chart)
                    {
                        openFragment(chartFragment.newInstance("", ""));
                        return true;
                    }
                    if(item.getItemId() == R.id.news)
                    {
                        openFragment(newFragment.newInstance("", ""));
                        return true;
                    }
                    return false;
                }
            };
    @Override
    public void onBackPressed() {
        finish(); // 退出应用程序
    }
}