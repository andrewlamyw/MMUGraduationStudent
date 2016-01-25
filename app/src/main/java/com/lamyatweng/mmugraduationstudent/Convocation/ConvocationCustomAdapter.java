package com.lamyatweng.mmugraduationstudent.Convocation;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.lamyatweng.mmugraduationstudent.R;

public class ConvocationCustomAdapter extends ArrayAdapter<Convocation> {
    public ConvocationCustomAdapter(Context context) {
        super(context, 0);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Convocation convocation = getItem(position);

        // Inflate view if view has not been created
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_convocation, parent, false);
        }

        // Get reference of views
        TextView year = (TextView) convertView.findViewById(R.id.year);
        TextView open = (TextView) convertView.findViewById(R.id.open);
        TextView close = (TextView) convertView.findViewById(R.id.close);

        // Set text value of views
        year.setText(Integer.toString(convocation.getYear()));
        open.setText(convocation.getOpenRegistrationDate());
        close.setText(convocation.getCloseRegistrationDate());

        return convertView;
    }
}
