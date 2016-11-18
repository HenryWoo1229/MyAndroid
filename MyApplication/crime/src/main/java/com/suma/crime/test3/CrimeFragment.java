package com.suma.crime.test3;


import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.NavUtils;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Date;
import java.util.UUID;


public class CrimeFragment extends Fragment {
    private static final String TAG = "CrimeFragment";
    public static final String EXTRA_CRIME_ID = "com.suma.crime.test3.crime_id";
    public static final int REQUEST_CODE_DATE = 0;
    private Date mDate;

    private Crime mCrime;
    private EditText mTitle;
    private Button mDateButton;
    private CheckBox mSolvedCheckBox;

    private boolean mSubTitleVisable;

    public CrimeFragment() {
        // Required empty public constructor
    }

    // 可以理解为带Argument的构造方法
    public static CrimeFragment newInstance(UUID crimeId) {
        Bundle args = new Bundle();
        args.putSerializable(EXTRA_CRIME_ID, crimeId);
        Log.d(TAG, "newInstance: crimeId= " + crimeId);
        CrimeFragment fragment = new CrimeFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "create");
        setHasOptionsMenu(true);    //显示optionmenu菜单
        setRetainInstance(true);
        mSubTitleVisable = false;
        mCrime = new Crime();

        UUID crimeId = (UUID) getArguments().getSerializable(EXTRA_CRIME_ID);
        Log.d(TAG, "onCreate: crimeId= " + crimeId);
        mCrime = CrimeLab.getCrimeLab(getActivity()).getCrime(crimeId);
        Log.d(TAG, "onCreate: mCrime = " + mCrime);
    }

    @TargetApi(11)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.i(TAG, "createView");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            if (NavUtils.getParentActivityName(getActivity()) != null)   // 如果在manifest中配置了PARENT_ACTIVITY
                getActivity().getActionBar().setDisplayHomeAsUpEnabled(true);  //让任务栏出现返回上一页的按钮
        }                                                                //但还需单独实现上一页的功能

        View v = inflater.inflate(R.layout.fragment_crime, container, false);

        mDateButton = (Button) v.findViewById(R.id.crime_date);
        mDateButton.setEnabled(true);
        updateDate();
        mDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fm = getActivity().getSupportFragmentManager();
                DatePickerFragment dialog = DatePickerFragment.newInstance(mCrime.getDate());
                dialog.setTargetFragment(CrimeFragment.this, REQUEST_CODE_DATE);
                dialog.show(fm, "date_time");
            }
        });

        mSolvedCheckBox = (CheckBox) v.findViewById(R.id.crime_solved);
        mSolvedCheckBox.setChecked(mCrime.isSolved());
        mSolvedCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                mCrime.setSolved(b);
            }
        });

        mTitle = (EditText) v.findViewById(R.id.crime_title);
        mTitle.setText(mCrime.getmTitle());
        mTitle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                mCrime.setmTitle(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        return v;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_DATE) {      //=====判断一下，可能请求码会重复
            if (resultCode == Activity.RESULT_OK) {
                mDate = (Date) data.getSerializableExtra(DatePickerFragment.EXTRA_DATE);
                mCrime.setDate(mDate);    //更新Crime中的日期数据
                updateDate();    //更新按钮的文字
            }
        }

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_crime_list, menu);
        MenuItem menuItem = menu.findItem(R.id.menu_item_show_subtitle);
        if (mSubTitleVisable && menuItem != null) {
            getActivity().getActionBar().setSubtitle(R.string.subtitle);
            menuItem.setTitle(R.string.hide_subtitle);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if (NavUtils.getParentActivityName(getActivity()) != null) {
                    NavUtils.navigateUpFromSameTask(getActivity());
                }
                return true;
            case R.id.menu_item_show_subtitle:
                ActionBar bar = getActivity().getActionBar();
                if (bar.getSubtitle() == null) {
                    bar.setSubtitle(R.string.subtitle);
                    item.setTitle(R.string.hide_subtitle);
                    mSubTitleVisable = true;
                } else {
                    bar.setSubtitle(null);
                    item.setTitle(R.string.show_subtitle);
                    mSubTitleVisable = false;
                }
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        CrimeLab.getCrimeLab(getActivity()).saveCrimes();
        Toast.makeText(getActivity(), R.string.save_crimes, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy: ");
    }

    public void updateDate() {
        mDateButton.setText(mCrime.getFormatDate());
    }
}
