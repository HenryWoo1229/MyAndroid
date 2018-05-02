package com.suma.crime.test3;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;

import java.util.ArrayList;
import java.util.UUID;

public class CrimeViewPagerActivity extends FragmentActivity {
    public static final String TAG = "CrimeViewPagerActivity";
    private ViewPager mViewPager;
    private  ArrayList<Crime> crimes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewPager = new ViewPager(this);    // create ViewPager
        mViewPager.setId(R.id.viewPager);    // set Resource Id
        setContentView(mViewPager);          // set Content View

        crimes = CrimeLab.getCrimeLab(this).getCrimes();

        FragmentManager fm = getSupportFragmentManager();
        mViewPager.setAdapter(new FragmentPagerAdapter(fm) {
            @Override
            public Fragment getItem(int position) {
                Crime crime = crimes.get(position);
                return CrimeFragment.newInstance(crime.getmId());
            }

            @Override
            public int getCount() {
                return crimes.size();
            }
        });

        UUID mCrimeId = (UUID)getIntent().getSerializableExtra(CrimeFragment.EXTRA_CRIME_ID);
        for(int i = 0; i <crimes.size(); i ++){
            if(crimes.get(i).getmId().equals(mCrimeId)){
                mViewPager.setCurrentItem(i);              //==============设置跳转后显示的初始分页，注意要放在setAdapter的下面
                Log.d(TAG, "onCreate: setCurrentItem in="+i);      //===============否则会设置不生效
                break;
            }
        }

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                Crime c = crimes.get(position);
                setTitle(c.getmTitle());
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }


}
