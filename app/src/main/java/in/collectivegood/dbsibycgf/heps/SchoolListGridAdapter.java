package in.collectivegood.dbsibycgf.heps;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import in.collectivegood.dbsibycgf.R;
import in.collectivegood.dbsibycgf.database.SchoolRecord;

public class SchoolListGridAdapter extends ArrayAdapter<SchoolRecord> {

    public SchoolListGridAdapter(@NonNull Context context, ArrayList<SchoolRecord> textViewResourceId) {
        super(context, 0, textViewResourceId);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            view = (LayoutInflater.from(getContext())).inflate(R.layout.school_item, null);
        }
        SchoolRecord schoolRecord = getItem(position);
        if (schoolRecord != null) {
            TextView schoolNameView = (TextView) view.findViewById(R.id.school_name);
            schoolNameView.setText(schoolRecord.getName());
        }
        return view;

    }
}
