package in.collectivegood.dbsibycgf.activities_dashboard;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.GridView;

import java.util.ArrayList;

import in.collectivegood.dbsibycgf.R;

public class Dashboard_layout extends AppCompatActivity {
    GridView gridView;
    gridadapter gridadapter;
    ArrayList<griditem> ARR;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard_layout);
        gridView = (GridView) findViewById(R.id.gridview);
        ARR=new ArrayList<>();
        ARR.add(new griditem(R.mipmap.ic_launcher_round, "photos"));
        ARR.add(new griditem(R.mipmap.ic_launcher_round, "calender"));
        ARR.add(new griditem(R.mipmap.ic_launcher_round, "HEPS data"));
        ARR.add(new griditem(R.mipmap.ic_launcher_round, "SVP data"));
        ARR.add(new griditem(R.mipmap.ic_launcher_round, " GIS"));
        ARR.add(new griditem(R.mipmap.ic_launcher_round, "Advocacy"));
        ARR.add(new griditem(R.mipmap.ic_launcher_round, "Reports"));
        ARR.add(new griditem(R.mipmap.ic_launcher_round, "Resources"));
        ARR.add(new griditem(R.mipmap.ic_launcher_round, "Others"));
        gridadapter gridadapter = new gridadapter(this, ARR);
        gridView.setAdapter(gridadapter);

    }
}
