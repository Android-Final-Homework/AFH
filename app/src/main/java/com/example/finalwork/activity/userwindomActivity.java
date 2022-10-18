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
            loginBean=(LoginBean)intent.getSerializableExtra("loginBean");
            setContentView(R.layout.activity_userwindom);
            viewPager=findViewById(R.id.viewPage);
            bottomNav=findViewById(R.id.nav_view);

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

            List<Fragment> fragments=new ArrayList<>();
            fragments.add(new browseFragment());
            fragments.add(new publishFragment());
            fragments.add(new Mine());
            // 监听viewPager页面变化事件
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
            // 获取Fragment管理对象
            FragmentManager fragmentManager = getSupportFragmentManager();
            // FragmentPagerAdapter 来处理多个 Fragment 页面
            FragmentPagerAdapter fragmentPagerAdapter = new FragmentPagerAdapter(fragmentManager) {
                @Override
                public Fragment getItem(int position) {
                    switch (position){
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
            // viewPager 设置 adapter
            viewPager.setAdapter(fragmentPagerAdapter);

        }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        /**
         * 1.使用getSupportFragmentManager().getFragments()获取到当前Activity中添加的Fragment集合
         * 2.遍历Fragment集合，手动调用在当前Activity中的Fragment中的onActivityResult()方法。
         */
        if (getSupportFragmentManager().getFragments() != null && getSupportFragmentManager().getFragments().size() > 0) {
            List<Fragment> fragments = getSupportFragmentManager().getFragments();
            for (Fragment mFragment : fragments) {
                mFragment.onActivityResult(requestCode, resultCode, data);
            }
        }
    }



    }
