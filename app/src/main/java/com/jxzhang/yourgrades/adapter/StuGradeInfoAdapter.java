package com.jxzhang.yourgrades.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.jxzhang.yourgrades.R;
import com.jxzhang.yourgrades.util.StudentInfo;

import java.util.List;

/**
 * Created by J.X.Zhang on 2015/9/22.
 * 学生成绩信息ListView适配器
 */
public class StuGradeInfoAdapter extends ArrayAdapter<StudentInfo> {
    private int resourceId;
    public StuGradeInfoAdapter(Context context, int textViewResourceId, List<StudentInfo> objects) {
        super(context, textViewResourceId, objects);
        resourceId = textViewResourceId;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        StudentInfo studentInfo = getItem(position);
        View view;
        ViewHolder viewHolder;

        if(convertView == null){
            view = LayoutInflater.from(getContext()).inflate(resourceId, null);
            viewHolder = new ViewHolder();
            viewHolder.className = (TextView)view.findViewById(R.id.class_name_text);
            viewHolder.grade = (TextView)view.findViewById(R.id.grade_text);
            view.setTag(viewHolder);
        } else {
            view = convertView;
            viewHolder = (ViewHolder)view.getTag();
        }
        viewHolder.className.setText(studentInfo.getClassName());
        viewHolder.className.setTextColor(Color.rgb(102, 102, 102));
        viewHolder.className.setTextSize(11);

        viewHolder.grade.setText(studentInfo.getGrade());
        viewHolder.grade.setTextColor(Color.rgb(102, 102, 102));
        viewHolder.grade.setTextSize(11);
        return view;
    }
    class ViewHolder{
        TextView className;
        TextView grade;
    }
}
