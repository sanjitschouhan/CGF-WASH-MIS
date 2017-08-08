package in.collectivegood.dbsibycgf.profiles;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import in.collectivegood.dbsibycgf.R;
import in.collectivegood.dbsibycgf.checkin.CheckInActivity;
import in.collectivegood.dbsibycgf.checkin.CheckOutActivity;
import in.collectivegood.dbsibycgf.dashboard.DashboardActivity;
import in.collectivegood.dbsibycgf.database.DbHelper;
import in.collectivegood.dbsibycgf.database.Schemas;
import in.collectivegood.dbsibycgf.database.SchoolDbHelper;
import in.collectivegood.dbsibycgf.listofschools.ListOfSchoolsActivity;
import in.collectivegood.dbsibycgf.support.InfoProvider;

public class CCProfileActivity extends AppCompatActivity {

    private String ccUID;
    private int noOfSchools;
    private String mandal;
    private String district;
    private String email;
    private String name;
    private String reportingManager;
    private String phone;
    private TextView nameView;
    private TextView phoneView;
    private TextView emailView;
    private TextView reportingManagerView;
    private TextView noOfSchoolsView;
    private TextView mandalView;
    private TextView districtView;
    private TextView uidView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cc_profile);

        //noinspection ConstantConditions
        getSupportActionBar().setElevation(0);
        init();
        getCCData();
        getSchoolData();

        emailView.setText(email);
        nameView.setText(name);
        phoneView.setText(phone);
        reportingManagerView.setText(reportingManager);
        uidView.setText(ccUID);

        noOfSchoolsView.setText(String.valueOf(noOfSchools));
        mandalView.setText(mandal);
        districtView.setText(district);
    }

    /**
     * Get all schools information for current Cluster Coordinator
     */
    private void getSchoolData() {
        SchoolDbHelper schoolDbHelper = new SchoolDbHelper(new DbHelper(this));
        Cursor read = schoolDbHelper.read(Schemas.SchoolDatabaseEntry.UID_OF_CC, ccUID);
        noOfSchools = read.getCount();
        read.moveToNext();
        mandal = read.getString(read.getColumnIndexOrThrow(Schemas.SchoolDatabaseEntry.BLOCK));
        district = read.getString(read.getColumnIndexOrThrow(Schemas.SchoolDatabaseEntry.DISTRICT));
        read.close();
    }

    /**
     * Get information about current cluster coordinator
     */
    private void getCCData() {
        email = InfoProvider.getCCData(this, Schemas.CCDatabaseEntry.EMAIL);
        name = InfoProvider.getCCData(this, Schemas.CCDatabaseEntry.NAME);
        reportingManager = InfoProvider.getCCData(this, Schemas.CCDatabaseEntry.PROJECT_COORDINATOR);
        ccUID = InfoProvider.getCcUID(this);
        phone = InfoProvider.getCCData(this, Schemas.CCDatabaseEntry.PHONE);
    }

    /**
     * Initialise the references to views
     */
    private void init() {
        nameView = (TextView) findViewById(R.id.cc_name);
        phoneView = (TextView) findViewById(R.id.cc_phone);
        emailView = (TextView) findViewById(R.id.cc_email);
        reportingManagerView = (TextView) findViewById(R.id.cc_reporting_manager);
        noOfSchoolsView = (TextView) findViewById(R.id.cc_no_of_schools);
        mandalView = (TextView) findViewById(R.id.cc_mandal);
        districtView = (TextView) findViewById(R.id.cc_district);
        uidView = (TextView) findViewById(R.id.cc_uid);
    }

    /**
     * Open the activity screen
     */
    public void OpenActivityScreen(View view) {
        Intent intent = new Intent(this, DashboardActivity.class);
        startActivity(intent);
    }

    public void CheckIn(View view) {
        startActivity(new Intent(CCProfileActivity.this, CheckInActivity.class));
    }

    public void ListOfSchools(View view) {
        startActivity(new Intent(CCProfileActivity.this, ListOfSchoolsActivity.class));

    }

    @Override
    protected void onResume() {
        SharedPreferences preferences = getSharedPreferences(getString(R.string.app_name), MODE_PRIVATE);
        String school_code = preferences.getString("school_code", null);
        Button checkInButton = (Button) findViewById(R.id.btn_check_in);
        Button checkOutButton = (Button) findViewById(R.id.btn_check_out);
        if (school_code == null) {
            checkInButton.setVisibility(View.VISIBLE);
            checkOutButton.setVisibility(View.GONE);
        } else {
            checkInButton.setVisibility(View.GONE);
            checkOutButton.setVisibility(View.VISIBLE);
        }
        super.onResume();
    }


    public void CheckOut(View view) {
        startActivity(new Intent(this, CheckOutActivity.class));
    }
}
