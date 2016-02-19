package com.lamyatweng.mmugraduationstudent.Seat;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lamyatweng.mmugraduationstudent.Constants;
import com.lamyatweng.mmugraduationstudent.R;

public class SeatSelectAdapter extends ArrayAdapter<Seat> {
    LayoutInflater mInflater;

    public SeatSelectAdapter(Context context) {
        super(context, 0);
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_seat_select, parent, false);
            holder = new ViewHolder();
            holder.textViewRow = (TextView) convertView.findViewById(R.id.textView_seat_row);
            holder.textViewColumn = (TextView) convertView.findViewById(R.id.textView_seat_column);
            holder.itemSeatContainer = (LinearLayout) convertView.findViewById(R.id.item_seat_container);
            holder.textViewRowLabel = (TextView) convertView.findViewById(R.id.textView_seat_rowLabel);
            holder.textViewColumnLabel = (TextView) convertView.findViewById(R.id.textView_seat_columnLabel);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Seat seat = getItem(position);
        holder.textViewRow.setText(seat.getRow());
        holder.textViewColumn.setText(seat.getColumn());

        switch (seat.getStatus()) {
            case Constants.SEAT_STATUS_AVAILABLE:
                holder.itemSeatContainer.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.seatAvailable));
                holder.itemSeatContainer.setVisibility(View.VISIBLE);
                break;
            case Constants.SEAT_STATUS_OCCUPIED:
                holder.itemSeatContainer.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.seatOccupied));
                holder.itemSeatContainer.setVisibility(View.VISIBLE);
                break;
            case Constants.SEAT_STATUS_SELECTED:
                holder.itemSeatContainer.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.seatSelected));
                holder.itemSeatContainer.setVisibility(View.VISIBLE);
                break;
            case Constants.SEAT_STATUS_DISABLED:
                holder.itemSeatContainer.setVisibility(View.INVISIBLE);
                break;
        }

        return convertView;
    }

    static class ViewHolder {
        TextView textViewRow;
        TextView textViewColumn;
        LinearLayout itemSeatContainer;
        TextView textViewRowLabel;
        TextView textViewColumnLabel;
    }
}
