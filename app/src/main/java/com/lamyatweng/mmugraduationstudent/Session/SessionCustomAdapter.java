package com.lamyatweng.mmugraduationstudent.Session;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.lamyatweng.mmugraduationstudent.R;


public class SessionCustomAdapter extends ArrayAdapter<ConvocationSession> {
    public SessionCustomAdapter(Context context) {
        super(context, 0);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ConvocationSession session = getItem(position);

        // Inflate view if view has not been created
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_session, parent, false);
        }

        // Get reference of views
        TextView number = (TextView) convertView.findViewById(R.id.session_number);
        TextView date = (TextView) convertView.findViewById(R.id.session_date);
        TextView startTime = (TextView) convertView.findViewById(R.id.session_start_time);
        TextView closeTime = (TextView) convertView.findViewById(R.id.session_end_time);

        // Set text value of views
        number.setText(Integer.toString(session.getSessionNumber()));
        date.setText(session.getDate());
        startTime.setText(session.getStartTime());
        closeTime.setText(session.getEndTime());

        return convertView;
    }
}
