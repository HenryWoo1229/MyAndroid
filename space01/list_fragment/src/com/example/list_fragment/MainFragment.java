package com.example.list_fragment;

import java.util.ArrayList;

import android.R.integer;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class MainFragment extends ListFragment {

	ArrayList<String> arrayList;

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		for (int i = 0; i < 100; i++)
			arrayList.add("List" + i);
		
		MyAdapter adapter = new MyAdapter(arrayList);
		setListAdapter(adapter);

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_main, container, false);
	}

	private class MyAdapter extends ArrayAdapter<String> {

		public MyAdapter(ArrayList<String> arrayList) {
			super(getActivity(), 0, arrayList);
		}

		
		@Override
		public View getView(int position, View converView, ViewGroup parent) {
			if (converView == null)
				converView = getActivity().getLayoutInflater().inflate(
						R.layout.list_item_main, parent, false);
			
			TextView textView = (TextView)converView.findViewById(R.id.text);
			
			return converView;
		}

	}
}
