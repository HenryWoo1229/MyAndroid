package com.suma.crime.test3;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;


public class CrimeListFragment extends ListFragment {
    public static final String TAG = "CrimeListFragment";
    private ArrayList<Crime> mCrimeList;
    private Button emptyButton;

    public CrimeListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);               //fragment设定optionMenu时需要该方法通知fm回调onCreateOptionsMenu
        getActivity().setTitle(R.string.crimes_title);

        mCrimeList = CrimeLab.getCrimeLab(getActivity()).getCrimes();

        // 在ListView中使用ArrayAdapter默认的布局和视图
//        ArrayAdapter<Crime> adapter = new ArrayAdapter<Crime>(getActivity(),
//                android.R.layout.simple_list_item_1, mCrimeList);

        // 在ListView中使用自定义布局和视图时通过继承Adapter并重写getView来实现
        CrimeAdapter adapter = new CrimeAdapter(mCrimeList);
        setListAdapter(adapter);

    }

    private class CrimeAdapter extends ArrayAdapter<Crime>{
        public CrimeAdapter(ArrayList<Crime> crimes){
            super(getActivity(), 0, crimes);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent){
            if(convertView == null){
                convertView = getActivity().getLayoutInflater()
                        .inflate(R.layout.list_item_crime, null);
            }

            Crime c = getItem(position);

            TextView title = (TextView)convertView.findViewById(R.id.list_item_title);
            title.setText(c.getmTitle());

            TextView date = (TextView)convertView.findViewById(R.id.list_item_date);
            date.setText(c.getFormatDate());

            CheckBox solvedBox = (CheckBox)convertView.findViewById(R.id.list_item_solved);
            solvedBox.setChecked(c.isSolved());

            return convertView;
        }
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        Crime c = (Crime) getListAdapter().getItem(position);
        Log.d(TAG,  c.getmTitle()+" was clicked.");
        Intent intent = new Intent(getActivity(), CrimeViewPagerActivity.class);
        intent.putExtra(CrimeFragment.EXTRA_CRIME_ID, c.getmId());
        startActivity(intent);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.i(TAG, "onCreateView: 222222222222222222");

        View v = inflater.inflate(R.layout.fragment_crime_list, container, false);

        emptyButton = (Button) v.findViewById(R.id.empty_btn);
        emptyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addCrime();
            }
        });

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        ((CrimeAdapter)getListAdapter()).notifyDataSetChanged();   //在恢复时刷新Adapter数据
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_crime_list, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_item_new_crime:
                addCrime();
                return true;       // 返回true表明已经处理完菜单选项的内容

            default:
                return super.onOptionsItemSelected(item);   //如果没有对应的菜单ID，则调用
        }

    }

    private void addCrime(){
        Crime crime = new Crime();
        mCrimeList.add(crime);
        Intent intent = new Intent(getActivity(), CrimeViewPagerActivity.class);
        intent.putExtra(CrimeFragment.EXTRA_CRIME_ID, crime.getmId());
        startActivityForResult(intent, 0);
    }
}
