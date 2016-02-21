package com.lamyatweng.mmugraduationstudent.News;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.lamyatweng.mmugraduationstudent.R;

public class NewsAdapter extends ArrayAdapter<News> {
    LayoutInflater mInflater;

    public NewsAdapter(Context context) {
        super(context, 0);
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // Create new view if no available view to reuse
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_news, parent, false);
        }

        TextView date = (TextView) convertView.findViewById(R.id.textView_news_date);
        TextView message = (TextView) convertView.findViewById(R.id.textView_news_message);

        // Set text value of views
        News news = getItem(position);
        date.setText(news.getDate());
        message.setText(news.getMessage());

        return convertView;
    }
}
