package in.collectivegood.dbsibycgf.profiles;

import android.database.Cursor;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.List;

import in.collectivegood.dbsibycgf.R;
import in.collectivegood.dbsibycgf.database.DbHelper;
import in.collectivegood.dbsibycgf.database.Schemas;
import in.collectivegood.dbsibycgf.database.SchoolDbHelper;
import in.collectivegood.dbsibycgf.gis.Location;

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

        DatabaseReference gis = FirebaseDatabase.getInstance().getReference("gis").child(code);
        gis.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Location value = dataSnapshot.getValue(Location.class);
                TextView geoAddressView = (TextView) findViewById(R.id.geo_address);
                if (value == null) {
                    geoAddressView.setText(R.string.gis_info_not_found);
                } else {
                    String geoLocationText = value.toString();
                    Geocoder geocoder = new Geocoder(SchoolProfileActivity.this);
                    try {
                        List<Address> fromLocation = geocoder.getFromLocation(value.getLat(), value.getLon(), 1);
                        if (fromLocation.size() > 0) {
                            Address address = fromLocation.get(0);
                            String addr = "\n\n" +
                                    address.getSubLocality() + ", " +
                                    address.getLocality() + ", " +
                                    address.getAdminArea() + ", " +
                                    address.getCountryName() + ", " +
                                    address.getPostalCode();
                            geoLocationText = geoLocationText.concat(addr);

                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    geoAddressView.setText(geoLocationText);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
}

