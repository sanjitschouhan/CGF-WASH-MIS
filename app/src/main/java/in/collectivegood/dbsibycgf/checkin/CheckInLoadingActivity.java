package in.collectivegood.dbsibycgf.checkin;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import in.collectivegood.dbsibycgf.R;

public class CheckInLoadingActivity extends AppCompatActivity {
    private static final int PERMISSIONS_REQUEST_LOCATION = 1;
    LocationManager locationManager;
    LocationListener locationListener;
    String code;
    Location schoolLocation;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PERMISSIONS_REQUEST_LOCATION) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 2, 1, locationListener);
            } else {
                Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_in_loading);
        code = getIntent().getExtras().getString("code");
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        locationListener = new mLocationListener() {
            @Override
            public void onLocationChanged(Location loc) {
                DatabaseReference gis;
                if (code != null) {
                    gis = FirebaseDatabase.getInstance().getReference("gis").child(code);
                    in.collectivegood.dbsibycgf.gis.Location location =
                            new in.collectivegood.dbsibycgf.gis.Location(loc.getLatitude(), loc.getLongitude());
                    schoolLocation = new Location(loc);
                    locationManager.removeUpdates(this);
                    gis.setValue(location).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            CheckIn();
                        }
                    });
                }
            }
        };

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("gis");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                boolean found = false;
                for (DataSnapshot g : dataSnapshot.getChildren()) {
                    if (String.valueOf(g.getKey()).equals(code)) {
                        found = true;
                        in.collectivegood.dbsibycgf.gis.Location value = g.getValue(in.collectivegood.dbsibycgf.gis.Location.class);
                        schoolLocation = new Location(LocationManager.GPS_PROVIDER);
                        schoolLocation.setLatitude(value.getLat());
                        schoolLocation.setLongitude(value.getLon());
                        CheckIn();
                        break;
                    }
                }
                if (!found) {
                    getGis();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }


    private void getGis() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                    PERMISSIONS_REQUEST_LOCATION);
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 2, 1, locationListener);
    }

    private void CheckIn() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                    PERMISSIONS_REQUEST_LOCATION);
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 2, 1, new mLocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                locationManager.removeUpdates(this);
                SharedPreferences preferences = getSharedPreferences(getString(R.string.app_name), MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("school_code", code);
                editor.putLong("start_time", System.currentTimeMillis());
                float distance = location.distanceTo(schoolLocation);
                editor.putFloat("distance", distance);
                editor.putString("lat", String.valueOf(schoolLocation.getLatitude()));
                editor.putString("lon", String.valueOf(schoolLocation.getLongitude()));
                editor.apply();
                Toast.makeText(CheckInLoadingActivity.this, "Checked In", Toast.LENGTH_SHORT).show();
                CheckInLoadingActivity.this.finish();
            }
        });
    }
}


abstract class mLocationListener implements LocationListener {
    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }

    @Override
    public void onProviderEnabled(String provider) {
    }

    @Override
    public void onProviderDisabled(String provider) {
    }
}