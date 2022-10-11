package com.example.finalwork.activity;

import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.finalwork.R;
import com.example.finalwork.ShareAdapter;
import com.example.finalwork.myFragment;
import com.example.finalwork.shareFragment;

import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    ShareAdapter shareAdapter;
    private LinearLayout mTab1;
    private LinearLayout mTab2;

    //声明两个Tab的ImageButton
    private ImageButton mImg1;
    private ImageButton mImg2;

    //声明两个Tab分别对应的Fragment
    private Fragment mFrag1;
    private Fragment mFrag2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        setTitle("图片分享");
        init();
        //selectTab(1);
    }

    private void init() {

        mTab1 = findViewById(R.id.tab1);
        mTab2 = findViewById(R.id.tab2);
        mImg1 = findViewById(R.id.tab_img1);
        mImg2 = findViewById(R.id.tab_img2);

        mTab1.setOnClickListener(this);
        mTab2.setOnClickListener(this);

        resetImgs();

    }
    @Override
    public void onClick(View v) {
        resetImgs(); //先将四个ImageButton置为深色
        switch (v.getId()) {
            case R.id.tab1:
                selectTab(1);
                break;
            case R.id.tab2:
                selectTab(2);
                break;
        }
    }

    private void resetImgs() {
        mImg1.setImageResource(R.drawable.home);
        mImg1.clearColorFilter();
        mImg2.setImageResource(R.drawable.me);
        mImg2.clearColorFilter();
    }



    private void selectTab(int i) {

        FragmentManager manager = getSupportFragmentManager();

        FragmentTransaction transaction = manager.beginTransaction();

        hideFragments(transaction);
        switch (i) {
            //当选中点击的是第一页的Tab时
            case 1:
                //设置第一页的ImageButton为深色
                mImg1.setColorFilter(R.color.black);
                //mImg1.setImageResource(R.drawable.home);
                //如果第一页对应的Fragment没有实例化，则进行实例化，并显示出来
                if (mFrag1 == null ||mFrag1==getVisibleFragment()) {
                    mFrag1 = new shareFragment();
                    transaction.add(R.id.fragment, mFrag1);
                } else {
                    //如果第一页对应的Fragment已经实例化，则直接显示出来
                    transaction.show(mFrag1);
                }
                break;
            case 2:
                mImg2.setColorFilter(R.color.black);
                //mImg2.setImageResource(R.drawable.me);
                if (mFrag2 == null||mFrag2==getVisibleFragment()) {
                    mFrag2 = new myFragment();
                    transaction.add(R.id.fragment, mFrag2);
                } else {
                    transaction.show(mFrag2);
                }
                break;
        }
        //不要忘记提交事务
        transaction.commit();
    }

    //将四个的Fragment隐藏
    private void hideFragments(FragmentTransaction transaction) {
        if (mFrag1 != null) {
            transaction.hide(mFrag1);
        }
        if (mFrag2 != null) {
            transaction.hide(mFrag2);
        }

    }

    //获取当前显示的Fragment
    public Fragment getVisibleFragment(){
        FragmentManager fragmentManager = MainActivity.this.getSupportFragmentManager();
        List<Fragment> fragments = fragmentManager.getFragments();
        for(Fragment fragment : fragments){
            if(fragment != null && fragment.isVisible())
                return fragment;
        }
        return null;
    }


}