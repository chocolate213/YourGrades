package com.jxzhang.yourgrades.Fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.jxzhang.yourgrades.R;
import com.jxzhang.yourgrades.adapter.StuGradeInfoAdapter;
import com.jxzhang.yourgrades.util.HTMLParser;
import com.jxzhang.yourgrades.util.HTMLParser_2;
import com.jxzhang.yourgrades.util.MyApplication;
import com.jxzhang.yourgrades.util.StudentInfo;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by J.X.Zhang on 2015/9/22.
 * 成绩Fragment
 */
public class GradeFragment extends Fragment{

    HTMLParser_2 htmlParser_2;
    private List<StudentInfo> mList;
    ListView listView;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initStudentInfo();

    }

    private void initStudentInfo() {
        SharedPreferences preferences = MyApplication.getContext().getSharedPreferences("stu_info", Context.MODE_PRIVATE);
        String mData = preferences.getString("stu_grade_info", "");
        //mPaster();
        htmlParser_2 = new HTMLParser_2(mData);
        List<StudentInfo> list_stu = htmlParser_2.getList();
        mList = new ArrayList<StudentInfo>(list_stu);
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_grade,container,false);
        SharedPreferences sharedPreferences = MyApplication.getContext().getSharedPreferences("list_size", Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sharedPreferences.edit();
        StuGradeInfoAdapter adapter = new StuGradeInfoAdapter(MyApplication.getContext(), R.layout.stu_grade_info, mList);
        int list_length = mList.size();
        edit.putInt("list_length",list_length);
        edit.commit();
        listView = (ListView)view.findViewById(R.id.list_view);
        listView.setAdapter(adapter);
        return view;
    }
}
