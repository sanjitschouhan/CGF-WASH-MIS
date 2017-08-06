package in.collectivegood.dbsibycgf.calender;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;

import in.collectivegood.dbsibycgf.R;

public class CalendarEventListAdapter extends ArrayAdapter<CalendarItem> {
    public CalendarEventListAdapter(@NonNull Context context, ArrayList<CalendarItem> textViewResourceId) {
        super(context, 0, textViewResourceId);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            view = (LayoutInflater.from(getContext())).inflate(R.layout.calendar_event_item, null);
        }
        CalendarItem item = getItem(position);
        if (item != null) {
            TextView eventTitle = (TextView) view.findViewById(R.id.event_title);
            TextView eventDateTime = (TextView) view.findViewById(R.id.date_time);
            TextView eventDetail = (TextView) view.findViewById(R.id.event_detail);

            eventTitle.setText(item.getTitle());
            eventDetail.setText(item.getDetail());

            Date date = new Date(item.getDate());
            DateFormat dateFormat = DateFormat.getDateTimeInstance();
            eventDateTime.setText(dateFormat.format(date));
        }
        return view;
    }
}
