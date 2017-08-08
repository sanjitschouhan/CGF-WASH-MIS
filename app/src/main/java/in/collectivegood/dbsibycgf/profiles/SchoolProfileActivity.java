package in.collectivegood.dbsibycgf.profiles;

import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import in.collectivegood.dbsibycgf.R;
import in.collectivegood.dbsibycgf.database.DbHelper;
import in.collectivegood.dbsibycgf.database.Schemas;
import in.collectivegood.dbsibycgf.database.SchoolDbHelper;

public class SchoolProfileActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_school);
        schoolview();

    }
    public void schoolview() {
        SchoolDbHelper schoolDbHelper = new SchoolDbHelper(new DbHelper(SchoolProfileActivity.this));
        String code = getIntent().getExtras().getString("code");
        Cursor read = schoolDbHelper.read(Schemas.SchoolDatabaseEntry.CODE, code);
        if (read.moveToNext()) {
            String name = read.getString(read.getColumnIndexOrThrow(Schemas.SchoolDatabaseEntry.NAME));
            String disccode = read.getString(read.getColumnIndexOrThrow(Schemas.SchoolDatabaseEntry.CODE));
            String village = read.getString(read.getColumnIndexOrThrow(Schemas.SchoolDatabaseEntry.VILLAGE));
            String mandal = read.getString(read.getColumnIndexOrThrow(Schemas.SchoolDatabaseEntry.VILLAGE));
            String district = read.getString(read.getColumnIndexOrThrow(Schemas.SchoolDatabaseEntry.DISTRICT));
            String state = read.getString(read.getColumnIndexOrThrow(Schemas.SchoolDatabaseEntry.STATE));

            TextView schoolname = (TextView) findViewById(R.id.school_name);
            TextView dise = (TextView) findViewById(R.id.dise_code);
            TextView villagename = (TextView) findViewById(R.id.village);
            TextView mandaldistrict = (TextView) findViewById(R.id.mandal_district);
            TextView districtname = (TextView) findViewById(R.id.district);
            TextView statename = (TextView) findViewById(R.id.state);

            schoolname.setText(name);
            dise.setText(disccode);
            villagename.setText(village);
            mandaldistrict.setText(mandal);
            districtname.setText(district);
            statename.setText(state);

        }

    }
}

