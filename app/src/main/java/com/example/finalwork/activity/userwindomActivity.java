package com.example.finalwork.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.finalwork.R;
import com.example.finalwork.bean.LoginBean;
import com.example.finalwork.fragment.Mine;
import com.example.finalwork.fragment.browseFragment;
import com.example.finalwork.fragment.publishFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

public class userwindomActivity extends AppCompatActivity {

    private BottomNavigationView bottomNav;
    private ViewPager viewPager;
    private LoginBean loginBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = this.getIntent();
        loginBean = (LoginBean) intent.getSerializableExtra("loginBean");
        setContentView(R.layout.activity_userwindom);
        viewPager = findViewById(R.id.viewPage);
        bottomNav = findViewById(R.id.nav_view);
        bottomNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigation_one:
                        viewPager.setCurrentItem(0);
                        bottomNav.getMenu().getItem(0).setChecked(true);
                        break;
                    case R.id.navigation_two:
                        viewPager.setCurrentItem(1);
                        bottomNav.getMenu().getItem(1).setChecked(true);
                        break;
                    case R.id.navigation_three:
                        viewPager.setCurrentItem(2);
                        bottomNav.getMenu().getItem(2).setChecked(true);
                        break;
                }
                return false;
            }
        });

        initPager();
    }

    private void initPager() {

        List<Fragment> fragments = new ArrayList<>();
        fragments.add(new browseFragment());
        fragments.add(new publishFragment());
        fragments.add(new Mine());
        // ??????viewPager??????????????????
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                bottomNav.getMenu().getItem(position).setChecked(true);
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        // ??????Fragment????????????
        FragmentManager fragmentManager = getSupportFragmentManager();
        // FragmentPagerAdapter ??????????????? Fragment ??????
        FragmentPagerAdapter fragmentPagerAdapter = new FragmentPagerAdapter(fragmentManager) {
            @Override
            public Fragment getItem(int position) {
                switch (position) {
                    case 0:
                        return fragments.get(0);
                    case 1:
                        return fragments.get(1);
                    case 2:
                        return fragments.get(2);
                }
                return null;
            }

            @Override
            public int getCount() {
                return 3;
            }
        };
        // viewPager ?????? adapter
        viewPager.setAdapter(fragmentPagerAdapter);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        /**
         * 1.??????getSupportFragmentManager().getFragments()???????????????Activity????????????Fragment??????
         * 2.??????Fragment??????????????????????????????Activity??????Fragment??????onActivityResult()?????????
         */
        if (getSupportFragmentManager().getFragments() != null && getSupportFragmentManager().getFragments().size() > 0) {
            List<Fragment> fragments = getSupportFragmentManager().getFragments();
            for (Fragment mFragment : fragments) {
                mFragment.onActivityResult(requestCode, resultCode, data);
            }
        }
    }


}
