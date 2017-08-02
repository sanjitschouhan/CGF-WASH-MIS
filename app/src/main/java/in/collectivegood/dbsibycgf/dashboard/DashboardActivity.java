package in.collectivegood.dbsibycgf.dashboard;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import java.util.ArrayList;

import in.collectivegood.dbsibycgf.R;
import in.collectivegood.dbsibycgf.calender.CalendarActivity;
import in.collectivegood.dbsibycgf.gallery.GalleryMainActivity;
import in.collectivegood.dbsibycgf.gis.GISActivity;
import in.collectivegood.dbsibycgf.heps.HEPSDataActivity;
import in.collectivegood.dbsibycgf.wash_resources.WashResourcesActivity;

public class DashboardActivity extends AppCompatActivity {
    GridView gridView;
    DashboardGridAdapter dashboardGridAdapter;
    ArrayList<GridItem> dashboardItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        gridView = (GridView) findViewById(R.id.gridview);
        dashboardItems = new ArrayList<>();
        dashboardItems.add(new GridItem(R.mipmap.ic_launcher_round, getString(R.string.photos), GalleryMainActivity.class));
        dashboardItems.add(new GridItem(R.mipmap.ic_launcher_round, getString(R.string.calender), CalendarActivity.class));
        dashboardItems.add(new GridItem(R.mipmap.ic_launcher_round, getString(R.string.heps_data), HEPSDataActivity.class));
//        dashboardItems.add(new GridItem(R.mipmap.ic_launcher_round, getString(R.string.svp_data)));
        dashboardItems.add(new GridItem(R.mipmap.ic_launcher_round, getString(R.string.gis), GISActivity.class));
        dashboardItems.add(new GridItem(R.mipmap.ic_launcher_round, getString(R.string.advocacy)));
//        dashboardItems.add(new GridItem(R.mipmap.ic_launcher_round, getString(R.string.reports)));
        dashboardItems.add(new GridItem(R.mipmap.ic_launcher_round, getString(R.string.resources), WashResourcesActivity.class));
//        dashboardItems.add(new GridItem(R.mipmap.ic_launcher_round, getString(R.string.others)));
        dashboardGridAdapter = new DashboardGridAdapter(this, dashboardItems);
        gridView.setAdapter(dashboardGridAdapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Class className = dashboardItems.get(position).getClassName();
                if (className != null) {
                    Intent intent = new Intent(DashboardActivity.this, className);
                    startActivity(intent);
                }
            }
        });

    }
}
