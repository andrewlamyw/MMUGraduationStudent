package com.lamyatweng.mmugraduationstudent.Student;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.lamyatweng.mmugraduationstudent.R;

public class StudentAdapter extends ArrayAdapter<Student> {

    public StudentAdapter(Context context) {
        super(context, 0);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Student student = getItem(position);

        // Inflate view if view has not been created
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_student, parent, false);
        }

        // Get reference of views
        TextView name = (TextView) convertView.findViewById(R.id.textView_student_name);
        TextView id = (TextView) convertView.findViewById(R.id.textView_student_id);
        TextView programme = (TextView) convertView.findViewById(R.id.textView_student_programme);
        TextView status = (TextView) convertView.findViewById(R.id.textView_student_status);

        // Set text value of views
        name.setText(student.getName());
        id.setText(student.getId());
        programme.setText(student.getProgramme());
        status.setText(student.getStatus());

        return convertView;
    }
}
