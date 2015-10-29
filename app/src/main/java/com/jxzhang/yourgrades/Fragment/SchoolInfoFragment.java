package com.jxzhang.yourgrades.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.jxzhang.yourgrades.R;
import com.jxzhang.yourgrades.activity.CETSearcherActivity;
import com.jxzhang.yourgrades.util.MyApplication;

/**
 * Created by J.X.Zhang on 2015/9/22.
 * 更多Activity
 */
public class SchoolInfoFragment extends Fragment{


    TextView schoolInfoEnglish;
    TextView schoolInfoStandardChinese;
    TextView schoolInfoComputer;



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_school_info,container,false);

        schoolInfoEnglish = (TextView)view.findViewById(R.id.school_info_english);
        schoolInfoStandardChinese = (TextView)view.findViewById(R.id.school_info_standard_chinese);
        schoolInfoComputer = (TextView)view.findViewById(R.id.school_info_computer);

        schoolInfoEnglish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent  = new Intent(getActivity(), CETSearcherActivity.class);
                startActivity(intent);
            }
        });
        schoolInfoStandardChinese.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MyApplication.getContext(), "敬请期待~", Toast.LENGTH_SHORT).show();
            }
        });
        schoolInfoComputer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MyApplication.getContext(), "敬请期待~", Toast.LENGTH_SHORT).show();
            }
        });
        return view;
    }
}