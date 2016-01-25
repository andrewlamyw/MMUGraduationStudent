package com.lamyatweng.mmugraduationstudent.Programme;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.lamyatweng.mmugraduationstudent.R;

public class ProgrammeCustomAdapter extends ArrayAdapter<Programme> {
    public ProgrammeCustomAdapter(Context context) {
        super(context, 0);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Programme programme = getItem(position);

        // Inflate view if view has not been created
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_programme, parent, false);
        }

        // Get reference of views
        TextView programmeName = (TextView) convertView.findViewById(R.id.programmeName);
        TextView faculty = (TextView) convertView.findViewById(R.id.faculty);

        // Set text value of views
        programmeName.setText(programme.getName());
        faculty.setText(programme.getFaculty());

        return convertView;
    }
}
