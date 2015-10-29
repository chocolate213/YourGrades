package com.jxzhang.yourgrades.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.jxzhang.yourgrades.R;
import com.jxzhang.yourgrades.util.BookInfo;

/**
 * Created by J.X.Zhang on 2015/10/22.
 * 图书馆图书信息ListView适配器
 */

public class BookInfoAdapter extends ArrayAdapter<BookInfo> {

    private int resourceId;
    private static int bookIndex;
    public BookInfoAdapter(Context context, int resource, List<BookInfo> objects) {
        super(context, resource, objects);
        resourceId = resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        bookIndex = position+1;
        BookInfo bookInfo = getItem(position);
        View view;
        ViewHolder viewHolder;

        if (convertView == null) {
            view = LayoutInflater.from(getContext()).inflate(resourceId, null);
            viewHolder = new ViewHolder();
            viewHolder.mBookName = (TextView) view.findViewById(R.id.book_name);
            viewHolder.mBookAuthor = (TextView) view
                    .findViewById(R.id.book_author);
            viewHolder.mBookPublisher = (TextView) view
                    .findViewById(R.id.book_publisher);
            viewHolder.mBookPuilishDate = (TextView) view
                    .findViewById(R.id.book_publish_date);
            viewHolder.mBookNumber = (TextView) view
                    .findViewById(R.id.book_number);
            view.setTag(viewHolder);
        } else {
            view = convertView;
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.mBookName.setText(bookInfo.getB_1_Name());
        viewHolder.mBookAuthor.setText(bookInfo.getB_3_Author());
        viewHolder.mBookPublisher.setText(bookInfo.getB_5_Publisher());
        viewHolder.mBookPuilishDate.setText(bookInfo.getB_7_PublicationTime());
        viewHolder.mBookNumber.setText(""+bookIndex);
        return view;
    }

    class ViewHolder {
        TextView mBookName;
        TextView mBookAuthor;
        TextView mBookPublisher;
        TextView mBookPuilishDate;
        TextView mBookNumber;
    }
}