package in.collectivegood.dbsibycgf.dashboard;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import in.collectivegood.dbsibycgf.R;

class DashboardGridAdapter extends ArrayAdapter<GridItem> {

    DashboardGridAdapter(@NonNull Context context, ArrayList<GridItem> textViewResourceId) {
        super(context, 0, textViewResourceId);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            view = (LayoutInflater.from(getContext())).inflate(R.layout.griditem, null);
        }
        GridItem GridItem = getItem(position);
        if (GridItem != null) {
            ImageView imageView = (ImageView) view.findViewById(R.id.imageforgrid);
            TextView textView = (TextView) view.findViewById(R.id.textforgrid);
            imageView.setImageResource(GridItem.getImage());
            textView.setText(GridItem.getText());
        }
        return view;

    }
}
