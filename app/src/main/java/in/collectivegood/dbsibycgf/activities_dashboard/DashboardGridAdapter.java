package in.collectivegood.dbsibycgf.activities_dashboard;

import android.content.Context;
import android.support.annotation.IdRes;
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

public class DashboardGridAdapter extends ArrayAdapter<GridItem> {
    public DashboardGridAdapter(@NonNull Context context, @IdRes ArrayList<GridItem> textViewResourceId) {
        super(context, 0, textViewResourceId);
    }
ImageView imageView;
    TextView textView;
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        if(view==null){
            view=(LayoutInflater.from(getContext())).inflate(R.layout.griditem,null);
        }
        GridItem GridItem =getItem(position);
        imageView=(ImageView) view.findViewById(R.id.imageforgrid);
        textView=(TextView)view.findViewById(R.id.textforgrid);
        imageView.setImageResource(GridItem.getImage());
        textView.setText(GridItem.getText());
        return view;

    }
}
