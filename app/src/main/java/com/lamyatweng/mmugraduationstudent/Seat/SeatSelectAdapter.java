package com.lamyatweng.mmugraduationstudent.Seat;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lamyatweng.mmugraduationstudent.R;

public class SeatSelectAdapter extends ArrayAdapter<Seat> {

    Context mContext;

    public SeatSelectAdapter(Context mContext) {
        super(mContext, 0);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolderItem1 viewHolder;

        if (convertView == null) {
            // inflate layout
            LayoutInflater layoutInflater = (LayoutInflater) getContext()
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.item_seat_select, parent, false);
            // set up ViewHolder
            viewHolder = new ViewHolderItem1();
            viewHolder.textViewRow = (TextView) convertView.findViewById(R.id.textView_seat_row);
            viewHolder.textViewColumn = (TextView) convertView.findViewById(R.id.textView_seat_column);
            viewHolder.itemSeatContainer = (LinearLayout) convertView.findViewById(R.id.item_seat_container);
            viewHolder.textViewRowLabel = (TextView) convertView.findViewById(R.id.textView_seat_rowLabel);
            viewHolder.textViewColumnLabel = (TextView) convertView.findViewById(R.id.textView_seat_columnLabel);

            // store holder with view
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolderItem1) convertView.getTag();
        }

        Seat seat = getItem(position);

        if (seat != null) {
            viewHolder.textViewRow.setText(seat.getRow());
            viewHolder.textViewColumn.setText(seat.getColumn());

            switch (seat.getStatus()) {
                case "Available":
                    viewHolder.itemSeatContainer.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.seatAvailable));
                    viewHolder.itemSeatContainer.setVisibility(View.VISIBLE);
                    break;
                case "Occupied":
                    viewHolder.itemSeatContainer.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.seatOccupied));
                    viewHolder.itemSeatContainer.setVisibility(View.VISIBLE);
                    break;
                case "Selected":
                    viewHolder.itemSeatContainer.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.seatSelected));
                    viewHolder.itemSeatContainer.setVisibility(View.VISIBLE);
                    break;
                case "Disabled":
//                    viewHolder.itemSeatContainer.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.seatSelectDisabled));
//                    viewHolder.itemSeatContainer.setBackgroundColor(Color.TRANSPARENT);
//                    viewHolder.textViewColumn.setTextColor(Color.TRANSPARENT);
//                    viewHolder.textViewRow.setTextColor(Color.TRANSPARENT);
//                    viewHolder.textViewRowLabel.setTextColor(Color.TRANSPARENT);
//                    viewHolder.textViewColumnLabel.setTextColor(Color.TRANSPARENT);
                    viewHolder.itemSeatContainer.setVisibility(View.INVISIBLE);
                    break;
            }
        }

        return convertView;
    }
}

class ViewHolderItem1 {
    TextView textViewRow;
    TextView textViewColumn;
    LinearLayout itemSeatContainer;
    TextView textViewRowLabel;
    TextView textViewColumnLabel;
}
