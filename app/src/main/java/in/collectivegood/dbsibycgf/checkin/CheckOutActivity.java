package in.collectivegood.dbsibycgf.checkin;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.FirebaseDatabase;

import in.collectivegood.dbsibycgf.R;
import in.collectivegood.dbsibycgf.database.CheckInRecord;
import in.collectivegood.dbsibycgf.support.InfoProvider;

public class CheckOutActivity extends AppCompatActivity {

    private static final int PERMISSIONS_REQUEST_LOCATION = 1;
    private LocationManager locationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_out);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        CheckOut();
    }

    private void CheckOut() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                    PERMISSIONS_REQUEST_LOCATION);
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2, 1, new mLocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                locationManager.removeUpdates(this);
                SharedPreferences preferences = getSharedPreferences(getString(R.string.app_name), MODE_PRIVATE);
                String schoolCode = preferences.getString("school_code", null);

                long startTime = preferences.getLong("start_time", System.currentTimeMillis());
                long endTime = System.currentTimeMillis();

                double lat = Double.parseDouble(preferences.getString("lat", "0"));
                double lon = Double.parseDouble(preferences.getString("lon", "0"));
                Location schoolLocation = new Location(LocationManager.GPS_PROVIDER);
                schoolLocation.setLatitude(lat);
                schoolLocation.setLongitude(lon);

                float checkInDistance = preferences.getFloat("distance", 0);
                float checkOutDistance = location.distanceTo(schoolLocation);
                CheckInRecord checkInRecord = new CheckInRecord(
                        InfoProvider.getCcUID(CheckOutActivity.this),
                        schoolCode,
                        startTime, endTime,
                        checkInDistance, checkOutDistance
                );

                SharedPreferences.Editor editor = preferences.edit();
                editor.remove("school_code");
                editor.remove("start_time");
                editor.remove("distance");
                editor.apply();

                FirebaseDatabase.getInstance().getReference("check_ins")
                        .child(InfoProvider.getCcUID(CheckOutActivity.this))
                        .child(String.valueOf(System.currentTimeMillis()))
                        .setValue(checkInRecord)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(CheckOutActivity.this, "Checked Out", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        });
            }
        });
    }
}
