package in.collectivegood.dbsibycgf.activities_dashboard;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import java.util.ArrayList;

import in.collectivegood.dbsibycgf.R;
import in.collectivegood.dbsibycgf.gallery.GalleryMainActivity;

public class DashboardLayout extends AppCompatActivity {
    GridView gridView;
    DashboardGridAdapter dashboardGridAdapter;
    ArrayList<GridItem> dashboardItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard_layout);
        gridView = (GridView) findViewById(R.id.gridview);
        dashboardItems = new ArrayList<>();
        dashboardItems.add(new GridItem(R.mipmap.ic_launcher_round, "photos", GalleryMainActivity.class));
        dashboardItems.add(new GridItem(R.mipmap.ic_launcher_round, "calender"));
        dashboardItems.add(new GridItem(R.mipmap.ic_launcher_round, "HEPS data"));
        dashboardItems.add(new GridItem(R.mipmap.ic_launcher_round, "SVP data"));
        dashboardItems.add(new GridItem(R.mipmap.ic_launcher_round, " GIS"));
        dashboardItems.add(new GridItem(R.mipmap.ic_launcher_round, "Advocacy"));
        dashboardItems.add(new GridItem(R.mipmap.ic_launcher_round, "Reports"));
        dashboardItems.add(new GridItem(R.mipmap.ic_launcher_round, "Resources"));
        dashboardItems.add(new GridItem(R.mipmap.ic_launcher_round, "Others"));
        dashboardGridAdapter = new DashboardGridAdapter(this, dashboardItems);
        gridView.setAdapter(dashboardGridAdapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Class aClass = dashboardItems.get(position).getClassName();
                if (aClass != null) {
                    Intent intent = new Intent(DashboardLayout.this, aClass);
                    startActivity(intent);
                }
            }
        });

    }
}
