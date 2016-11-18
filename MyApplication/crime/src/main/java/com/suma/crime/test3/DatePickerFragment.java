package com.suma.crime.test3;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.TimePicker;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;


public class DatePickerFragment extends DialogFragment {
    public static final String EXTRA_DATE = "com.suma.crime.test3.crime_date";
    public static final String TAG = "DatePickerFragment";

    private Date mDate;
    private View view;


    public DatePickerFragment() {
        // Required empty public constructor
    }


    public static DatePickerFragment newInstance(Date date) {
        Bundle args = new Bundle();
        args.putSerializable(EXTRA_DATE, date);
        DatePickerFragment fragment = new DatePickerFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) { // 在屏幕上显示DialogFragment时，托管activity的FragmentManager会调用
        Log.d(TAG, "onCreateDialog: ");
        mDate = (Date) getArguments().getSerializable(EXTRA_DATE);

        Calendar calendar = Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);

        view = getActivity().getLayoutInflater().inflate(R.layout.dialog_date, null);

        DatePicker datePicker = (DatePicker) view.findViewById(R.id.date_picker);
        datePicker.init(year, month, day, new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker datePicker, int i, int i1, int i2) {
                mDate = new GregorianCalendar(i, i1, i2).getTime();   //格里高利日历，也就是公历的意思, Calendar子类
                getArguments().putSerializable(EXTRA_DATE, mDate);    //为防止屏幕旋转导致实例重建时数据丢失，此处进行参数备份

            }
        });

        final TimePicker timePicker = new TimePicker(getActivity());

        timePicker.setIs24HourView(true);
        timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker timePicker, int i, int i1) {
                mDate = new GregorianCalendar(year, month, day, i, i1).getTime();
                getArguments().putSerializable(EXTRA_DATE, mDate);
            }
        });

        // DatePicker datePicker = new DatePicker(getActivity());   代码创建的方式

        return new AlertDialog.Builder(getActivity())
                .setItems(new String[]{"修改日期", "修改时间"}, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (i == 0) {  //date
                            Dialog dialog = new AlertDialog.Builder(getActivity())
                                    .setView(view)
                                    .setTitle(R.string.date_picker_title)
                                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            if (getTargetFragment() == null)
                                                return;

                                            Intent intent = new Intent();
                                            intent.putExtra(EXTRA_DATE, mDate);

                                            getTargetFragment()
                                                    .onActivityResult(getTargetRequestCode(),
                                                            Activity.RESULT_OK, intent);
                                        }
                                    }).create();
                            dialog.show();

                        }
                        if (i == 1) {  //time
                            Dialog timeDialog = new AlertDialog.Builder(getActivity())
                                    .setView(timePicker)
                                    .setTitle("时间修改")
                                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            if(getTargetFragment() == null) return;

                                            Intent intent = new Intent();
                                            intent.putExtra(EXTRA_DATE, mDate);

                                            getTargetFragment()
                                                    .onActivityResult(getTargetRequestCode(),
                                                            Activity.RESULT_OK, intent);
                                        }
                                    }).create();
                            timeDialog.show();
                        }
                    }
                })
                .setTitle(R.string.date_picker_title)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
//                        if (getTargetFragment() == null)
//                            return;
//
//                        Intent intent = new Intent();
//                        intent.putExtra(EXTRA_DATE, mDate);
//
//                        getTargetFragment()
//                                .onActivityResult(getTargetRequestCode(),
//                                        Activity.RESULT_OK, intent);
                    }
                })
                .create();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: ");
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    //    @Override   以下写法在这里面会报错requestWindowFeature must be called before adding content
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
////        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
//        return inflater.inflate(R.layout.fragment_date_picker, container, false);
//    }

}
